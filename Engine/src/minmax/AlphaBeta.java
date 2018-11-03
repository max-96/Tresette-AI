package minmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.LongAdder;

import AI.AIGameState;
import AI.DeterministicAI;
import setting.Game.Info;
import util.CardsUtils;
import util.MovesStats;

public class AlphaBeta extends DeterministicAI
{
	private static final int MAX_CACHE_SIZE = 4;

	private int depth;
	private int forkCounter = 0;
	private int alphapruning = 0;
	private int betapruning = 0;

	public double winningValue = 0;

	private List<List<Integer>> alphaMoves;
	private byte[] alphaMovesClock = new byte[40];
	private List<List<Integer>> betaMoves;
	private byte[] betaMovesClock = new byte[40];

	private static boolean inizializzata = false;
	private static long[] alphacount = new long[10];
	private static long[] betacount = new long[10];

	public static long maxExecTime = 0;
	public static long sumExecTime = 0;
	public static int numExec = 0;
	
	public long executionTime = 0;

	public static class Factory extends DeterministicAI.Factory
	{
		private int depth;

		public Factory(int depth)
		{
			this.depth = depth;
		}
		
		@Override
		public RecursiveAction getAI(int playerID, List<List<Integer>> assegnamentoCarte, Info info)
		{
			return new Slave(playerID, depth, assegnamentoCarte, info, punti);
		}
	}
	
	public AlphaBeta(int playerID, int depth)
	{
		super(playerID);
		this.depth = depth;

		alphaMoves = new ArrayList<>(40);
		betaMoves = new ArrayList<>(40);
		for (int i = 0; i < 40; i++)
		{
			alphaMoves.add(new ArrayList<>(MAX_CACHE_SIZE));
			betaMoves.add(new ArrayList<>(MAX_CACHE_SIZE));
		}
		if (!inizializzata)
		{
			Arrays.fill(alphaMovesClock, (byte) 0);
			Arrays.fill(betaMovesClock, (byte) 0);
			inizializzata = true;
		}
	}

	public void setDepth(int d)
	{
		depth = d;
	}

	@Override
	public int getBestMove(List<List<Integer>> assegnamentoCarte, Info info)
	{
		/*
		 * Il caso in cui le carte rimaste siano 0 o 1 non e' considerato, e' necessario
		 * rendersene conto prima di eseguire la determinazione!
		 */

		forkCounter = 0;
		alphapruning = 0;
		betapruning = 0;
		executionTime = System.currentTimeMillis();
		
		List<Integer> cardsOnTable = info.getCardsOnTable();
		int turno = info.getTurn();

		List<Integer> mosse = CardsUtils.getPossibiliMosse(assegnamentoCarte.get(playerID), cardsOnTable);

		assert mosse.size() > 1;

		AIGameState init = new AIGameState(playerID, assegnamentoCarte, info);

		/*
		 * L'insieme delle mosse e' definito, ora dobbiamo valutarle
		 */

		double bestActionVal = Double.NEGATIVE_INFINITY;
		double alpha = Double.NEGATIVE_INFINITY;// siamo in nodo maximise
		Integer bestAction = Integer.valueOf(-1);

		for (Integer mossa : mosse)
		{
			double mmaxval = alphabeta(init.genSuccessor(mossa), alpha, Double.POSITIVE_INFINITY, 1, depth);

			if (mmaxval > bestActionVal)
			{
				bestActionVal = mmaxval;
				bestAction = mossa;

			}

			if (mmaxval > alpha)
			{
				alpha = mmaxval;

			}
		}
		
		executionTime = System.currentTimeMillis() - executionTime;
		if (executionTime > maxExecTime)
			maxExecTime = executionTime;
		sumExecTime += executionTime;
		numExec++;

		winningValue = bestActionVal;

		if (CardsUtils.STATS_COLLECT)
		{
			MovesStats ms = MovesStats.getInstance();
			int d = 10 - assegnamentoCarte.get(playerID).size();
			int domCard = (turno == 0) ? 10 : CardsUtils.getDominantCard(cardsOnTable);
			ms.addStats(d, domCard, bestAction);
		}

		return bestAction;
	}

	private double alphabeta(AIGameState gs, double alpha, double beta, int depth, final int maxDepth)
	{
		forkCounter++;

		if (gs.terminal)
			return gs.getScoreSoFar();
		if (depth >= maxDepth && gs.isCardsOnTableEmpty())
			return gs.getScoreSoFar();

		boolean isMaximise = gs.maxNode;

		List<Integer> mosse = gs.generateActions();
		killermovesSorter(isMaximise, depth, mosse);
		double bestActionVal = isMaximise ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
		double alphabetaVal;

		for (Integer mossa : mosse)
		{
			alphabetaVal = alphabeta(gs.genSuccessor(mossa), alpha, beta, depth + 1, maxDepth);

			if (isMaximise)
			{
				if (alphabetaVal > bestActionVal)
					bestActionVal = alphabetaVal;
				if (alphabetaVal > alpha)
					alpha = alphabetaVal;
				if (beta <= alpha)
				{
					alphapruning++;
					List<Integer> cache = alphaMoves.get(depth);
					if (!cache.contains(mossa))
					{
						if (cache.size() < MAX_CACHE_SIZE)
							cache.add(mossa);
						else
						{
							cache.set(alphaMovesClock[depth], mossa);
							alphaMovesClock[depth] = (byte) ((alphaMovesClock[depth] + 1) % MAX_CACHE_SIZE);
						}

					}
					alphacount[CardsUtils.getCardValue(mossa)] += 1;
					break;
				}

			}
			else
			{
				if (alphabetaVal < bestActionVal)
					bestActionVal = alphabetaVal;

				if (alphabetaVal < beta)
					beta = alphabetaVal;

				if (beta <= alpha)
				{
					betapruning++;
					List<Integer> cache = betaMoves.get(depth);
					if (!cache.contains(mossa))
					{
						if (cache.size() < MAX_CACHE_SIZE)
							cache.add(mossa);
						else
						{
							cache.set(betaMovesClock[depth], mossa);
							betaMovesClock[depth] = (byte) ((betaMovesClock[depth] + 1) % MAX_CACHE_SIZE);
						}
					}
					betacount[mossa % 10] += 1;
					break;
				}
			}
		}
		
		return bestActionVal;
	}

	private void killermovesSorter(boolean isMax, int currDepth, List<Integer> mosse)
	{
		/*
		 * Introduco la Killer Heuristic, valutando prima le mosse che hanno prodotto
		 * pruning alla stessa altezza
		 */

		if (isMax)
		{
			for (Integer killerMove : alphaMoves.get(currDepth))
				if (mosse.contains(killerMove))
				{
					mosse.remove(killerMove);
					mosse.add(0, killerMove);
				}
		}
		else
		{
			for (Integer killerMove : betaMoves.get(currDepth))
				if (mosse.contains(killerMove))
				{
					mosse.remove(killerMove);
					mosse.add(0, killerMove);
				}
		}
	}

	public int getForkCounter()
	{
		return forkCounter;
	}

	public int getAlphapruning()
	{
		return alphapruning;
	}

	public int getBetapruning()
	{
		return betapruning;
	}

	public List<List<Integer>> getAlphaMoves()
	{
		return alphaMoves;
	}

	public List<List<Integer>> getBetaMoves()
	{
		return betaMoves;
	}

	public static class Slave extends RecursiveAction
	{
		private static final long serialVersionUID = 1L;
		
		private int playerId;
		private int depth;
		private List<List<Integer>> assegnamentoCarte;
		private Info info;
		private ConcurrentHashMap<Integer, LongAdder> punti;

		private Slave(int playerId, int depth, List<List<Integer>> assegnamentoCarte,
				Info info, ConcurrentHashMap<Integer, LongAdder> punti)
		{
			this.playerId = playerId;
			this.depth = depth;
			this.assegnamentoCarte = assegnamentoCarte;
			this.info = info;
			this.punti = punti;
		}

		@Override
		protected void compute()
		{
			AlphaBeta k = new AlphaBeta(playerId, depth);
			int r = k.getBestMove(assegnamentoCarte, info);
			punti.computeIfAbsent(r, key -> new LongAdder()).increment();
		}
	}
}

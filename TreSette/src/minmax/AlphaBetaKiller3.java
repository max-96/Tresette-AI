package minmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.LongAdder;

import AI.DeterministicAI;
import util.CardsUtils;
import util.MovesStats;
import AI.AIGameState;

public class AlphaBetaKiller3 extends DeterministicAI
{

	public static long maxExecTime = 0;
	public static long sumExecTime = 0;
	public static int numExec=0;
	
	/**
	 * Player's ID
	 */
	private int playerId;
	private int forkCounter = 0;
	private int alphapruning = 0;
	private int betapruning = 0;
	public long executionTime = 0;

	public double winningValue = 0;
	private int depth;

	public List<List<Integer>> alphaMoves;
	private byte[] alphaMovesClock = new byte[40];
	public List<List<Integer>> betaMoves;
	private byte[] betaMovesClock = new byte[40];

	private static boolean inizializzata = false;
	public static long[] alphacount = new long[10];
	public static long[] betacount = new long[10];

	public static final int MAX_CACHE_SIZE = 4;

	public AlphaBetaKiller3(int playerId, int depth)
	{
		this.playerId = playerId;
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
	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> cardsOnTable, double scoreMyTeam,
			double scoreOtherTeam)
	{
		/*
		 * Il caso in cui le carte rimaste siano 0 o 1 non e' considerato, e' necessario
		 * rendersene conto prima di eseguire la determinazione!
		 */

		forkCounter = 0;
		alphapruning = 0;
		betapruning = 0;
		long executionTime = System.currentTimeMillis();
		int turno = cardsOnTable.size();

		List<Integer> mosse = turno == 0 ? new ArrayList<>(assegnamentoCarte.get(playerId))
				: DeterministicAI.possibiliMosse(assegnamentoCarte.get(playerId), cardsOnTable.get(0) / 10);

		assert mosse.size() > 1;

		AIGameState init = new AIGameState(assegnamentoCarte, cardsOnTable, playerId, true, scoreMyTeam,
				scoreOtherTeam);

		/*
		 * L'insieme delle mosse e' definito, ora dobbiamo valutarle
		 */

		double bestActionVal = Double.NEGATIVE_INFINITY;
		double alpha = Double.NEGATIVE_INFINITY;// siamo in nodo maximise
		Integer bestAction = Integer.valueOf(-1);

		for (Integer mossa : mosse)
		{

			// double mmaxval = minmax(assegnamentoCarte, (playerId - turno + 4) % 4,
			// cardsOnTable, mossa, playerId, false,
			// alpha, Double.POSITIVE_INFINITY, depth, 1, 0);
			//
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
		numExec += 1;
		
		winningValue = bestActionVal;

		if (true)
		{
			MovesStats ms = MovesStats.getInstance();
			int d = 10 - assegnamentoCarte.get(playerId).size();
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
			return gs.scoreSoFar;

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
					alphacount[mossa % 10] += 1;
					break;
				}

			} else
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
			{
				if (mosse.contains(killerMove))
				{
					mosse.remove(killerMove);
					mosse.add(0, killerMove);
				}
			}
		} else
		{
			for (Integer killerMove : betaMoves.get(currDepth))
			{
				if (mosse.contains(killerMove))
				{
					mosse.remove(killerMove);
					mosse.add(0, killerMove);
				}
			}
		}

	}

	/**
	 * 
	 * @param oldassegnamentoCarte
	 * @param startingPlayer
	 * @param oldcardsOnTable
	 * @param carta
	 * @param currPlayer
	 * @param maximise
	 * @param evenScore
	 * @param oddScore
	 * @param alpha
	 * @param beta
	 * @return
	 */
	/**
	 * @return the forkCounter
	 */
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

	public static class AlphaBetaSlave extends RecursiveAction
	{

		private int playerId;
		private int depth;
		private List<List<Integer>> assegnamentoCarte;
		private List<Integer> cardsOnTable;
		private double scoreMyTeam;
		private double scoreOtherTeam;
		private ConcurrentHashMap<Integer, LongAdder> punti;

		public AlphaBetaSlave(int playerId, int depth, List<List<Integer>> assegnamentoCarte,
				List<Integer> cardsOnTable, double scoreMyTeam, double scoreOtherTeam,
				ConcurrentHashMap<Integer, LongAdder> punti)
		{
			this.playerId = playerId;
			this.depth = depth;
			this.assegnamentoCarte = assegnamentoCarte;
			this.cardsOnTable = cardsOnTable;
			this.scoreMyTeam = scoreMyTeam;
			this.scoreOtherTeam = scoreOtherTeam;
			this.punti = punti;
		}

		@Override
		protected void compute()
		{
			AlphaBetaKiller3 k = new AlphaBetaKiller3(playerId, depth);
			Integer r = k.getBestMove(assegnamentoCarte, cardsOnTable, scoreMyTeam, scoreOtherTeam);
			punti.computeIfAbsent(r, key -> new LongAdder()).increment();

		}

	}
}

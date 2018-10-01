package minmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import AI.DeterministicAI;

public class AlphaBetaKiller extends DeterministicAI {

	/**
	 * Player's ID
	 */
	private int playerId;
	private int forkCounter=0;
	private int alphapruning=0;
	private int betapruning=0;
	public long executionTime=0;
	public double winningValue=0;
	private int depth;
	
	public List<List<Integer>> alphaMoves;
	private byte[] alphaMovesClock=new byte[40];
	public List<List<Integer>> betaMoves;
	private byte[] betaMovesClock=new byte[40];
	
//	private Random randGen= new Random(100);
	
	public static final int MAX_CACHE_SIZE=4;
	
	
	
	public AlphaBetaKiller(int playerId, int depth) {
		this.playerId = playerId;
		this.depth=depth;
		
		alphaMoves=new ArrayList<>(40);
		betaMoves=new ArrayList<>(40);
		for(int i=0;i<40;i++)
		{
			alphaMoves.add(new ArrayList<>(MAX_CACHE_SIZE));
			betaMoves.add(new ArrayList<>(MAX_CACHE_SIZE));
		}
		Arrays.fill(alphaMovesClock, (byte) 0);
		Arrays.fill(betaMovesClock, (byte) 0);
		
	}
	
	public void setDepth(int d)
	{
		depth=d;
	}

	
	@Override
	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> cardsOnTable, double a, double b) {
		/*
		 * Il caso in cui le carte rimaste siano 0 o 1 non e' considerato, e' necessario
		 * rendersene conto prima di eseguire la determinazione!
		 */

		forkCounter=0;
		alphapruning=0;
		betapruning=0;
		executionTime=System.currentTimeMillis();
		int turno = cardsOnTable.size();

		List<Integer> mosse = turno == 0 ? new ArrayList<>(assegnamentoCarte.get(playerId))
				: DeterministicAI.possibiliMosse(assegnamentoCarte.get(playerId), cardsOnTable.get(0) / 10);

		assert mosse.size() > 1;

		/*
		 * L'insieme delle mosse e' definito, ora dobbiamo valutarle
		 */

		double bestActionVal = Double.NEGATIVE_INFINITY;
		double alpha = 0 ;//Double.NEGATIVE_INFINITY;// siamo in nodo maximise
		Integer bestAction = -1;

		for (Integer mossa : mosse) {

			double mmaxval = minmax(assegnamentoCarte, (playerId -turno+4)%4, cardsOnTable, mossa, playerId, false, alpha, Double.POSITIVE_INFINITY, depth,1, 0);

			System.out.print("x");
			if (mmaxval > bestActionVal) {
				bestActionVal = mmaxval;
				bestAction = mossa;
				winningValue=mmaxval;
			}

			if (mmaxval > alpha) {
				alpha = mmaxval;
				
			}

		}
		executionTime= System.currentTimeMillis() - executionTime;
		System.out.println();
		return bestAction;
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
	private double minmax(List<List<Integer>> oldassegnamentoCarte, int startingPlayer, List<Integer> oldcardsOnTable,
			Integer carta, int currPlayer,boolean maximise,  double alpha,
			double beta,int depth, int currDepth, double oldScore) {
		
		
		forkCounter++;
		
		/*
		 * Per questioni di pulizia del codice, al figlio passo la carta e le strutture
		 * dati del padre. Ci pensera' il figlio a copiarle e aggiornarle come deve.
		 */
		double score=oldScore;
		List<List<Integer>> assegnamentoCarte = new ArrayList<>(oldassegnamentoCarte);
		List<Integer> cardsOnTable = new ArrayList<>(oldcardsOnTable);
		{
			List<Integer> nuovoAssPerCurrPlayer = new ArrayList<>(oldassegnamentoCarte.get(currPlayer));
			nuovoAssPerCurrPlayer.remove(carta);
			assegnamentoCarte.set(currPlayer, nuovoAssPerCurrPlayer);
			cardsOnTable.add(carta);
		}

		if (cardsOnTable.size() == 4) {
			/*
			 * Se tutte e quattro le carte sono state tirate dobbiamo vedere a chi assegnare
			 * i punti, chi deve iniziare la passata e settare il maximise
			 */

			int playerDominante = startingPlayer;
			int cartaDominante = cardsOnTable.get(0);
			int semeDominante = cartaDominante / 10;
			double punteggio = puntiPerCarta[cartaDominante % 10];
			for (int p = 1; p < 4; p++) {
				int cartaTemp = cardsOnTable.get(p);
				punteggio += puntiPerCarta[cartaTemp % 10];
				if (semeDominante == cartaTemp / 10
						&& dominioPerCarta[cartaDominante % 10] < dominioPerCarta[cartaTemp % 10]) {
					playerDominante = p;
					cartaDominante = cartaTemp;
				}
			}
			// assegno i punti
			if (playerDominante % 2 == playerId%2)
				score += punteggio;
			else
				score -= punteggio;
			
			// assegno player
			currPlayer = playerDominante;
			startingPlayer = playerDominante;
			// assegno il maximise (stessa squadra)
			maximise = (playerDominante % 2 == playerId % 2);
			// svuoto la lista di carte sul tavolo
			cardsOnTable.clear();
			//decremento la profondita massima
			depth--;

		}

		/*
		 * Analizzo il caso base, quello in cui siamo giunti a una foglia
		 * 
		 * Questo avviene quando non ci sono carte sul tavolo (punteggi appena
		 * attribuiti) e non ci sono piu carte da tirare fuori per il player designato
		 * (che e' anche il player dominante del turno prima). Quindi faremo i conti e
		 * attribuiremo al player corrente anche un bonus
		 */
		if (cardsOnTable.isEmpty() && assegnamentoCarte.get(currPlayer).isEmpty()) {
			if (currPlayer % 2 ==  playerId%2)
				score += 1.0 / 3;
			else
				score -= 1.0 / 3;

			return score;
		}

		/*
		 * falsa foglia, ci sarebbe ancora da analizzare ma potiamo per ragioni di efficienza
		 * 
		 */
		if(cardsOnTable.isEmpty() && depth==0)
			return score;
		
		/*
		 * non e' un nodo foglia, scorro di un player ma solo se non siamo a inizio
		 * della passata (e il player e' stato appena resettato)
		 */
		if (!cardsOnTable.isEmpty())
			currPlayer = (currPlayer + 1) % 4;

		/*
		 * Ora vediamo il caso di un nodo intermedio.
		 * 
		 * dovra' analizzare le mosse e scegliere il valore massimo (o minimo)
		 * generabile
		 */

		int turno = cardsOnTable.size();
		double bestActionVal = maximise ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
		
		List<Integer> mosse = turno == 0 ? new ArrayList<>(assegnamentoCarte.get(currPlayer))
				: DeterministicAI.possibiliMosse(assegnamentoCarte.get(currPlayer), cardsOnTable.get(0) / 10);

		/*
		 * Introduco la Killer Heuristic, valutando prima le mosse
		 * che hanno prodotto pruning alla stessa altezza
		 */
		{
			if(maximise)
			{
				for(Integer killerMove: alphaMoves.get(currDepth))
				{
					if(mosse.contains(killerMove))
					{
						mosse.remove(killerMove);
						mosse.add(0, killerMove);
					}
				}
			}
			else
			{
				for(Integer killerMove: betaMoves.get(currDepth))
				{
					if(mosse.contains(killerMove))
					{
						mosse.remove(killerMove);
						mosse.add(0, killerMove);
					}
				}
			}
			
			
		}

		for (Integer mossa : mosse) {

			double mmaxval = minmax(assegnamentoCarte, startingPlayer, cardsOnTable, mossa, currPlayer, !maximise, alpha, beta, depth, currDepth+1, score);// chiama minmax

			if (maximise) {
				if (mmaxval > bestActionVal)
					bestActionVal = mmaxval;
				if (mmaxval > alpha)
					alpha = mmaxval;
				if (beta <= alpha) {
					alphapruning++;
					List<Integer> cache=alphaMoves.get(currDepth);
					if(!cache.contains(mossa))
					{
						if(cache.size()<MAX_CACHE_SIZE)
							cache.add(mossa);
						else
						{
							cache.set(alphaMovesClock[currDepth], mossa);
							alphaMovesClock[currDepth]=(byte) ((alphaMovesClock[currDepth]+ 1) % MAX_CACHE_SIZE);
						}
							
					}
					//System.out.println("alpha pruning\t" + alpha + "\t"+mossa+"\t"+currPlayer+"\t"+(10-assegnamentoCarte.get(currPlayer).size()));
					break;
				}

			} else {
				if (mmaxval < bestActionVal)
					bestActionVal = mmaxval;

				if (mmaxval < beta)
					beta = mmaxval;
				
				if (beta <= alpha) {
					betapruning++;
					List<Integer> cache=betaMoves.get(currDepth);
					if(!cache.contains(mossa))
					{
						if(cache.size()<MAX_CACHE_SIZE)
							cache.add(mossa);
						else
						{
							cache.set(betaMovesClock[currDepth], mossa);
							betaMovesClock[currDepth]=(byte) ((betaMovesClock[currDepth]+ 1) % MAX_CACHE_SIZE);
						}
					}
					//System.out.println("beta pruning\t" + beta + "\t"+mossa+"\t"+currPlayer+"\t"+(10-assegnamentoCarte.get(currPlayer).size()));
					break;
				}
			}
		}

		return bestActionVal;
	}

	/**
	 * @return the forkCounter
	 */
	public int getForkCounter() {
		return forkCounter;
	}


	public int getAlphapruning() {
		return alphapruning;
	}
	public int getBetapruning() {
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
}

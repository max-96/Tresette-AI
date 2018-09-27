package minmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import minmax.DebugGraph.Node;
import minmax.DebugGraph;

import AI.DeterministicAI;

public class AlphaBetaTranspos extends DeterministicAI {

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
	
//	public DebugGraph debugGrapho;
	
	
	
	
	
	
	
	public AlphaBetaTranspos(int playerId, int depth) {
		this.playerId = playerId;
		this.depth=depth;
		
		
		
	}

	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> cardsOnTable) {
		/*
		 * Il caso in cui le carte rimaste siano 0 o 1 non e' considerato, e' necessario
		 * rendersene conto prima di eseguire la determinazione!
		 */

		executionTime=System.currentTimeMillis();
		int turno = cardsOnTable.size();

		List<Integer> mosse = turno == 0 ? new ArrayList<>(assegnamentoCarte.get(playerId))
				: DeterministicAI.possibiliMosse(assegnamentoCarte.get(playerId), cardsOnTable.get(0) / 10);

		assert mosse.size() > 1;

		/*
		 * L'insieme delle mosse e' definito, ora dobbiamo valutarle
		 */

		double bestActionVal = Double.NEGATIVE_INFINITY;
		double alpha = Double.NEGATIVE_INFINITY;// siamo in nodo maximise
		Integer bestAction = -1;

		HashMap<StateAction, Double> TransTable=new HashMap<>();
		
		
		
		System.out.println("Shallow Search");
		for (Integer mossa : mosse) {
			alphabeta(assegnamentoCarte, (playerId -turno+4)%4, cardsOnTable, mossa, playerId, false, alpha, Double.POSITIVE_INFINITY, 3 ,1, 0.0,TransTable, false);
			System.out.print("x");
//			if (mmaxval > bestActionVal) {
//				bestActionVal = mmaxval;
//				bestAction = mossa;
//				winningValue=mmaxval;
//			}
//
//			if (mmaxval > alpha) {
//				alpha = mmaxval;
//			}

		}
		System.out.println();
		System.out.println("Deeper Search");
		for (Integer mossa : mosse) {
			double mmaxval = alphabeta(assegnamentoCarte, (playerId -turno+4)%4, cardsOnTable, mossa, playerId, false, alpha, Double.POSITIVE_INFINITY, depth,1, 0.0,TransTable, true);
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

	/*
	 * *****************************************
	 * 
	 * 
	 * 					ALPHA BETA
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
	 */
	
	
	
	
	private double alphabeta(List<List<Integer>> oldassegnamentoCarte, int startingPlayer, List<Integer> oldcardsOnTable,
			Integer carta, int currPlayer,boolean maximise,  double alpha,
			double beta,int depth, int currDepth, double oldScore, HashMap<StateAction, Double> TransTable, boolean isExplorative) {
		
		
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

		if(currDepth<8 && !isExplorative)
		{
		Comparator<Integer> ordinaCarte= maximise ? new Comparator<Integer>()
				{
					@Override
					public int compare(Integer arg0, Integer arg1) {
						Double s0= TransTable.get(new StateAction(assegnamentoCarte, cardsOnTable, arg0));
						Double s1= TransTable.get(new StateAction(assegnamentoCarte, cardsOnTable, arg1));
						if(s0==null)
							return -1;
						if(s1==null)
							return 1;
						return (int) Math.ceil(s0-s1);
					}
				
				} : 
					new Comparator<Integer>()
				{
					@Override
					public int compare(Integer arg0, Integer arg1) {
						Double s0= TransTable.get(new StateAction(assegnamentoCarte, cardsOnTable, arg0));
						Double s1= TransTable.get(new StateAction(assegnamentoCarte, cardsOnTable, arg1));
						if(s0==null)
							return -1;
						if(s1==null)
							return 1;
						return (int) Math.ceil(s1-s0);
					}
				
				};
				
		Collections.sort(mosse, ordinaCarte);
		}
			
			
		
		for (Integer mossa : mosse) {

			double mmaxval = alphabeta(assegnamentoCarte, startingPlayer, cardsOnTable, mossa, currPlayer, !maximise, alpha, beta, depth, currDepth+1,  score, TransTable, isExplorative);// chiama minmax
			
			if(currDepth<8 && isExplorative)
				TransTable.put(new StateAction(assegnamentoCarte, cardsOnTable, mossa), mmaxval);
			
			
			if (maximise) {
				if (mmaxval > bestActionVal)
				{
					bestActionVal = mmaxval;
				}
				if (mmaxval > alpha)
					alpha = mmaxval;
				if (beta <= alpha) {
					alphapruning++;
					
					//System.out.println("alpha pruning\t" + alpha + "\t"+mossa+"\t"+currPlayer+"\t"+(10-assegnamentoCarte.get(currPlayer).size()));
					break;
				}

			} else {
				if (mmaxval < bestActionVal)
				{
					bestActionVal = mmaxval;
				}

				if (mmaxval < beta)
					beta = mmaxval;
				
				if (beta <= alpha) {
					betapruning++;
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
		return Collections.emptyList();
	}
	public List<List<Integer>> getBetaMoves()
	{
		return Collections.emptyList();
	}
	
	private class StateAction 
	{
		public final List<List<Integer>> assignment;
		public final List<Integer> cardsOnTable;
		public final Integer action;
		
		public StateAction(List<List<Integer>> assignment, List<Integer> cardsOnTable, Integer action) {
			this.assignment = assignment;
			this.cardsOnTable = cardsOnTable;
			this.action = action;
		}
		
	}
}

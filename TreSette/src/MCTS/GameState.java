package MCTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import AI.DeterministicAI;

public class GameState {

	private final List<List<Integer>> cardsAssignment;
	private final List<Integer> cardsOnTable;
	public final int currentPlayer;
	public final boolean terminal;
	public final double scoreSoFar;
	public final boolean maxNode;

	public GameState(List<List<Integer>> cardsAssignment,
			List<Integer> cardsOnTable, int currentPlayer, double score, boolean maxNode) {
		this.cardsAssignment = cardsAssignment;
		this.cardsOnTable = cardsOnTable;
		this.currentPlayer = currentPlayer;
		terminal = cardsAssignment.get(currentPlayer).isEmpty();
		if(terminal)
			score += maxNode ? 1.0/3 : - (1.0/3);
		
		scoreSoFar = score ;
		this.maxNode=maxNode;
	}

	/**
	 * Genera i successori dell'attuale GameState, mappati da mossa a stato
	 * 
	 * @return
	 */
	public Map<Integer, GameState> generateSuccessors() {
		
		
		if(terminal) return null;
		
		/*
		 * In mosse mettiamo tutte le mosse legali
		 */
		List<Integer> mosse = cardsOnTable.isEmpty() ? new ArrayList<>(cardsAssignment.get(currentPlayer))
				: DeterministicAI.possibiliMosse(cardsAssignment.get(currentPlayer), cardsOnTable.get(0) / 10);

		/*
		 * 
		 */
		HashMap<Integer, GameState> mappa = new HashMap<>();

		for (Integer m : mosse) {
			GameState g = genSuccessor(m);
			mappa.put(m, g);

		}

		return mappa;
	}

	public Integer genRandMossa()
	{
		List<Integer> mosse = cardsOnTable.isEmpty() ? new ArrayList<>(cardsAssignment.get(currentPlayer))
				: DeterministicAI.possibiliMosse(cardsAssignment.get(currentPlayer), cardsOnTable.get(0) / 10);
		
		int pos=ThreadLocalRandom.current().nextInt(mosse.size());
		
		return mosse.get(pos);
	}
	
	public GameState genSuccessor(Integer mossa) {
		List<List<Integer>> newCardsAssignment = new ArrayList<>(cardsAssignment);
		List<Integer> newCardsOnTable = new ArrayList<>(cardsOnTable);
		int newCurrentPlayer;
		
		{
			List<Integer> temp = new ArrayList<>(newCardsAssignment.get(currentPlayer));
			temp.remove(mossa);
			newCardsAssignment.set(currentPlayer, temp);
			newCardsOnTable.add(mossa);
		}
		/*
		 * Caso semplice: non e' finita la passata ne la mano. Si passa al prossimo
		 * player e lo score rimane inalterato
		 */
		if (newCardsOnTable.size() < 4) {
			newCurrentPlayer=(currentPlayer + 1) % 4;
			GameState newGS= new GameState(newCardsAssignment, newCardsOnTable, newCurrentPlayer, scoreSoFar, !maxNode);
			return newGS;			
		}
		
		/*
		 * Siamo a fine di una passata.
		 * Dobbiamo assegnare i punti e la dominanza
		 */
		{
		assert cardsOnTable.size()==4;
		double newScoreSoFar=scoreSoFar;
		int playerDominante = (currentPlayer +1)%4;
		int cartaDominante = newCardsOnTable.get(0);
		int semeDominante = cartaDominante / 10;
		double punteggio = DeterministicAI.puntiPerCarta[cartaDominante % 10];
		for (int p = 1; p < 4; p++) {
			int cartaTemp = newCardsOnTable.get(p);
			punteggio += DeterministicAI.puntiPerCarta[cartaTemp % 10];
			if (semeDominante == cartaTemp / 10
					&& DeterministicAI.dominioPerCarta[cartaDominante % 10] < DeterministicAI.dominioPerCarta[cartaTemp % 10]) {
				playerDominante = p;
				cartaDominante = cartaTemp;
			}
		}
		
		boolean newMaxNode= (maxNode && playerDominante % 2 == currentPlayer%2)
				|| (!maxNode && playerDominante % 2 != currentPlayer%2);
		// assegno i punti
		if ( newMaxNode )
			newScoreSoFar += punteggio;
		else
			newScoreSoFar -= punteggio;
		
		// assegno player
		newCurrentPlayer = playerDominante;
		// assegno il maximise (stessa squadra)
		// svuoto la lista di carte sul tavolo
		newCardsOnTable.clear();		
		GameState newGS= new GameState(newCardsAssignment, newCardsOnTable, newCurrentPlayer, newScoreSoFar, newMaxNode);
		return newGS;
		}

	}

	/**
	 * @return the cardsAssignment
	 */
	public List<List<Integer>> getCardsAssignment() {
		return Collections.unmodifiableList(cardsAssignment);
	}

	/**
	 * @return the cardsOnTable
	 */
	public List<Integer> getCardsOnTable() {
		return Collections.unmodifiableList(cardsOnTable);
	}

	/**
	 * @return the currentPlayer
	 */
	public int getCurrentPlayer() {
		return currentPlayer;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cardsAssignment == null) ? 0 : cardsAssignment.hashCode());
		result = prime * result + ((cardsOnTable == null) ? 0 : cardsOnTable.hashCode());
		result = prime * result + currentPlayer;
		result = prime * result + (maxNode ? 1231 : 1237);
		result = prime * result + (terminal ? 1231 : 1237);
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GameState other = (GameState) obj;
		if (cardsAssignment == null) {
			if (other.cardsAssignment != null)
				return false;
		} else if (!cardsAssignment.equals(other.cardsAssignment))
			return false;
		if (cardsOnTable == null) {
			if (other.cardsOnTable != null)
				return false;
		} else if (!cardsOnTable.equals(other.cardsOnTable))
			return false;
		if (currentPlayer != other.currentPlayer)
			return false;
		if (maxNode != other.maxNode)
			return false;
		if (terminal != other.terminal)
			return false;
		return true;
	}

	



}

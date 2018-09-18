package AI;

import java.util.LinkedList;

import setting.Card;
import setting.Game;

public class PlayerAI extends Player
{
	private static final int N_BOARD_GEN = 30;

	/**
	 * Numero di carte sconosciute dall'attuale player per ciascun player
	 */
	int[] unknownCards = new int[4];

	public PlayerAI(int id, LinkedList<Card> carte, Game gioco)
	{
		super(id, carte, gioco);
	}

	public Card getMossa()
	{
		
//		CSPSolver csp= new CSPSolver(id, gioco, carteInMano, unknownCards, N_BOARD_GEN, piombi);
		
		return new Card(0);
	}
}

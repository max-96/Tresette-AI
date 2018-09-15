package AI;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class PlayerAI implements Player {

	/**
	 * id del player attuale
	 */
	int id;

	private static final int N_BOARD_GEN=30;


	/**
	 * carte che il player ha in mano
	 */
	LinkedList<Integer> carteInMano;

	/**
	 * Numero di carte sconosciute dall'attuale player per ciascun player
	 */
	int[] unknownCards = new int[4];

	public PlayerAI(int id, LinkedList<Integer> carte) {
		this.id = id;
		carteInMano = carte;

		for (int i = 0; i < 4; i++)
			unknownCards[i] = (i == id) ? 0 : 10;
	}

	public int getMossa()
	{
		
		List<int[]> possBoard=new ArrayList<>();
		
		
		return 0;
	}
}

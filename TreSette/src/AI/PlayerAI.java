package AI;

import java.util.LinkedList;
import java.util.List;

public class PlayerAI implements Player {

	/**
	 * id del player attuale
	 */
	int id;



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
//		for (int i = 0; i < 40; i++) {
//			if (carteInMano.contains(i)) {
//				for (int j = 0; j < 4; j++)
//					beliefMat[i][j] = (j == id) ? 1.0f : 0.0f;
//			} else {
//				for (int j = 0; j < 4; j++)
//					beliefMat[i][j] = (j == id) ? 0.0f : (1 / 3);
//
//			}
		}


		for (int i = 0; i < 4; i++)
			unknownCards[i] = (i == id) ? 0 : 10;
	}

	public int getMossa()
	{
		
}

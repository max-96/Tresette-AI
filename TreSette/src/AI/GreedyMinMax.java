package AI;

import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe implementa un MinMax greedy, nel senso che cerca di
 * massimizzare i punti guadagnati nella singola passata. Non effettua quindi
 * una ricerca per trovare la mossa che massimizzi il guadagno finale (quando
 * tutti i giocatori avranno 0 carte), ma solo quello nella passata attuale (di
 * durata 4 lanci).
 * 
 * @author Max
 *
 */
public class GreedyMinMax extends DeterministicAI {

	private int playerId;

	public GreedyMinMax(int idPlayer) {
		playerId = idPlayer;
	}

	@Override
	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> carteInGioco) {

		boolean newPassata = carteInGioco.isEmpty();
		List<Integer> cartePossibili = newPassata ? new ArrayList<>(assegnamentoCarte.get(playerId))
				: DeterministicAI.possibiliMosse(assegnamentoCarte.get(playerId), carteInGioco.get(0) / 10);

		if (cartePossibili.size() == 1)
			return cartePossibili.get(0);

		/*
		 * Calcoliamo chi domina la partita al momento, l'ammontare della giocata
		 * 
		 */

		double valoreGiocata = 0;
		Integer cartaDominante = null;
		int startingPlayer = playerId - carteInGioco.size();
		int playerDominante = startingPlayer;
		if (!newPassata) {
			cartaDominante = carteInGioco.get(0);

			int counter = 0;
			for (Integer c : carteInGioco) {
				valoreGiocata += puntiPerCarta[c];
				if (c / 10 == cartaDominante / 10 && dominioPerCarta[c % 10] > dominioPerCarta[cartaDominante % 10]) {
					cartaDominante = c;
					playerDominante = startingPlayer + counter;

				}
				counter++;
			}
		}

		/*
		 * Scegliamo la mossa che massimizza il mio guadagno
		 */
		Integer bestMove = -1;
		double bestVal = Double.NEGATIVE_INFINITY;
		for (Integer c : cartePossibili) {

			if (carteInGioco.size() == 3) {
				/*
				 * Se sono state gia buttate 3 carte, non ci sono altri casi da considerare In
				 * modo greedy, massimizzero' il ritorno
				 */
				int newVincente = playerDominante;
				if ((c / 10 == cartaDominante / 10) && dominioPerCarta[c % 10] > dominioPerCarta[cartaDominante]) {
					newVincente = playerId;
				}

				double valore = valoreGiocata + puntiPerCarta[c % 10];
				if (newVincente % 2 != playerId % 2)
					valore = -valore;

				if (valore > bestVal) {
					bestVal = valore;
					bestMove = c;
				}

			} else {
				/*
				 * Se ci sono meno di 3 carte in gioco, posso ragionare e usare minmax per
				 * massimizzare il mio ritorno
				 */
				int newDomCarta;
				int newDomPlayer;
				
				if (newPassata) {
					newDomCarta=c;
					newDomPlayer=playerId;

				} else {
					newDomCarta = cartaDominante;
					newDomPlayer = playerDominante;
					if (c / 10 == cartaDominante / 10
							&& dominioPerCarta[c % 10] > dominioPerCarta[cartaDominante % 10]) {
						newDomCarta = c;
						newDomPlayer = playerId;
					}
				}

				double newValoreGiocata = valoreGiocata + puntiPerCarta[c];
				List<List<Integer>> newAssegn = new ArrayList<>(assegnamentoCarte);
				{
					List<Integer> carteInMano = new ArrayList<>(assegnamentoCarte.get(playerId));
					carteInMano.remove(c);
					newAssegn.set(playerId, carteInMano);
				}

				double minmaxVal = minmax(newAssegn, (playerId + 1) % 4, newValoreGiocata, newDomCarta, newDomPlayer,
						carteInGioco.size() + 1, false);

				System.out.println("Minmax value (" + c + "):" + minmaxVal);

				if (minmaxVal > bestVal) {
					bestVal = minmaxVal;
					bestMove = c;
				}
			}
		}
		return bestMove;
	}

	private double minmax(List<List<Integer>> assegnamento, int playerConsiderato, double valoreGiocata,
			int cartaDominante, int playerDominante, int turno, boolean maximise) {

		List<Integer> mosse = DeterministicAI.possibiliMosse(assegnamento.get(playerConsiderato), cartaDominante / 10);

		if (turno == 3) {

			/*
			 * Siamo in un nodo foglia L'obiettivo e' di trovare la mossa che massimizzi o
			 * minimizzi
			 */

			if (mosse.size() == 1) {
				Integer carta = mosse.get(0);

				if ((carta / 10 == cartaDominante / 10)
						&& dominioPerCarta[carta % 10] > dominioPerCarta[cartaDominante % 10]) {
					playerDominante = playerConsiderato;
				}

				double valore = valoreGiocata + puntiPerCarta[carta % 10];
				return (playerDominante % 2 == playerId % 2) ? valore : (-valore);
			}

			double bestVal = (maximise) ? Double.MIN_VALUE : Double.MAX_VALUE;

			for (Integer c : mosse) {
				int newVincente = playerDominante;
				if ((c / 10 == cartaDominante / 10) && dominioPerCarta[c % 10] > dominioPerCarta[cartaDominante % 10]) {
					newVincente = playerConsiderato;
					/*
					 * carta c e' la nuova carta dominante ma non serve cambiarlo perche' siamo in
					 * un nodo foglia
					 */
				}

				double valore = valoreGiocata + puntiPerCarta[c % 10];
				if (newVincente % 2 != playerId % 2)
					valore = -valore;

				if ((maximise && valore > bestVal) || (!maximise && valore < bestVal))
					bestVal = valore;
			}
			return bestVal;
		}

		else {
			/*
			 * Nel caso di un nodo, dobbiamo chiamare minmax su tutti gli stati generabili
			 */
			double bestVal = (maximise) ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

			for (Integer carta : mosse) {
				int newDomCarta = cartaDominante;
				int newDomPlayer = playerDominante;
				if (carta / 10 == cartaDominante / 10
						&& dominioPerCarta[carta % 10] > dominioPerCarta[cartaDominante % 10]) {
					newDomCarta = carta;
					newDomPlayer = playerConsiderato;
				}

				double newValoreGiocata = valoreGiocata + puntiPerCarta[carta];
				List<List<Integer>> newAssegn = new ArrayList<>(assegnamento);
				{
					List<Integer> carteInMano = new ArrayList<>(assegnamento.get(playerConsiderato));
					carteInMano.remove(carta);
					newAssegn.set(playerConsiderato, carteInMano);
				}

				double minmaxVal = minmax(newAssegn, (playerConsiderato + 1) % 4, newValoreGiocata, newDomCarta,
						newDomPlayer, turno + 1, !maximise);

				if ((maximise && minmaxVal > bestVal) || (!maximise && minmaxVal < bestVal))
					bestVal = minmaxVal;

			}

			return bestVal;

		}

	}

	// private double minmax(List<List<Integer>> assegnamento, int player,
	// List<Integer> carteInGioco, int semeDominante)
	// {
	// int turno=carteInGioco.size();
	//
	// List<Integer>
	// mossePoss=DeterministicAI.possibiliMosse(assegnamento.get(player),
	// semeDominante);
	//
	//
	//
	//
	// return 0;
	// }
}

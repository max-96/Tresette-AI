package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.Semaphore;

@SuppressWarnings("serial")
public class ParCSP extends RecursiveAction {

	public ParCSP(HashMap<Integer, Integer> assegnamento, HashMap<Integer, LinkedList<Integer>> dominiIniziali,
			int[] carteRimanenti, int cartaAssegnata, int playerAssegnato,
			BlockingQueue<Map<Integer, Integer>> soluzioni, Semaphore maxSol) {
		this.assegnamento = assegnamento;
		this.nCarteRimanenti = carteRimanenti;
		this.cartaAssegnata = cartaAssegnata;
		this.playerAssegnato = playerAssegnato;
		this.soluzioni = soluzioni;
		this.maxSol = maxSol;
		this.domini = dominiIniziali;

	}

	private static final int SEQ_CUTOFF = 20;
	public BlockingQueue<Map<Integer, Integer>> soluzioni;
	private Semaphore maxSol;
	private HashMap<Integer, Integer> assegnamento;
	private HashMap<Integer, LinkedList<Integer>> domini;
	private int[] nCarteRimanenti;
	private int cartaAssegnata;
	private int playerAssegnato;

	@SuppressWarnings("unchecked")
	@Override
	protected void compute() {

		if (maxSol.availablePermits() == 0)
			return;

		{
			assegnamento = new HashMap<>(assegnamento);
			nCarteRimanenti = Arrays.copyOf(nCarteRimanenti, 4);
			HashMap<Integer, LinkedList<Integer>> d = domini;
			domini = new HashMap<>();
			for (Integer k : d.keySet()) {
				if (k == cartaAssegnata)
					continue;
				domini.put(k, (LinkedList<Integer>) d.get(k).clone());
			}
		}

		assegnamento.put(cartaAssegnata, playerAssegnato);
		domini.remove((Integer) cartaAssegnata);
		nCarteRimanenti[playerAssegnato] = nCarteRimanenti[playerAssegnato] - 1;
		ensureConsistency();

		if (domini.size() == 0) {
			if (maxSol.tryAcquire()) {
				soluzioni.offer(assegnamento);
				
			}
			return;

		} else {
			ArrayList<Integer> carteRimaste = new ArrayList<Integer>(domini.keySet());
			Collections.shuffle(carteRimaste);
			Integer c = carteRimaste.get(0);
			LinkedList<ParCSP> threads = new LinkedList<>();
			for (Integer v : domini.get(c))
				threads.add(new ParCSP(assegnamento, domini, nCarteRimanenti, c, v, soluzioni, maxSol));

			if (carteRimaste.size() > SEQ_CUTOFF) {
				ParCSP lastT = threads.removeLast();
				for (ParCSP t : threads)
					t.fork();
				lastT.compute();
				for (ParCSP t : threads)
					t.join();
			} else {
				for (ParCSP t : threads)
					t.compute();

			}
		}

	}

	private void ensureConsistency() {

		assert nCarteRimanenti[playerAssegnato] >= 0;
		LinkedList<Integer> toDelete = new LinkedList<>();

		if (nCarteRimanenti[playerAssegnato] > 0)
			return;

		LinkedList<Integer> toCheck = new LinkedList<>();
		toCheck.add(playerAssegnato);

		while (!toCheck.isEmpty()) {
			int p = toCheck.pop();
			// per ogni dominio controlliamo che non sia presente p
			for (Entry<Integer, LinkedList<Integer>> k : domini.entrySet()) {
				if (k.getValue().contains(p)) {

					// Se lo contiene lo dobbiamo togliere
					LinkedList<Integer> dom = k.getValue();
					dom.remove((Integer) p);
					assert dom.size() > 0;
					// Ora controlliamo se è diventato un assegnamento
					if (dom.size() == 1) {
						int pToCheck = dom.getFirst();
						int carta = k.getKey();
						toDelete.add(carta);
						assegnamento.put(carta, pToCheck);
						nCarteRimanenti[pToCheck] = nCarteRimanenti[pToCheck] - 1;

						assert nCarteRimanenti[pToCheck] >= 0;

						// non ha più assegnamenti possibili e va controllato
						if (nCarteRimanenti[pToCheck] == 0)
							toCheck.add(pToCheck);
					}
				}

			}
			while (!toDelete.isEmpty())
				domini.remove((Integer) toDelete.pop());

		}

	}
}

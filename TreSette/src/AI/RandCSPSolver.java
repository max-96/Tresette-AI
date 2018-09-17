package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class RandCSPSolver {

	public BlockingQueue<Map<Integer, Integer>> soluzioni;
	private int maxSol;

	public long tempoEsec = 0;
	// carte non assegnate
	private LinkedList<Integer> carte = new LinkedList<>();
	// domini di tali carte
	private HashMap<Integer, Vector<Integer>> domini = new HashMap<Integer, Vector<Integer>>();

	// assegnamento delle carte sicure
	private HashMap<Integer, Integer> assegnamento = new HashMap<Integer, Integer>();
	private int[] nCarteRimanenti = new int[4];
	private int status = 0;

	public RandCSPSolver(int player, Set<Integer> Ex, List<Integer> pCards, int[] rCards, int maxStati,
			boolean[][] piombi) {

		soluzioni = new ArrayBlockingQueue<Map<Integer, Integer>>((int) (maxStati * (1.5)));
		maxSol = maxStati;
		nCarteRimanenti = Arrays.copyOf(rCards, 4);

		for (int i : pCards)
			assegnamento.put(i, player);

		// se avviene un assegnamento e un rcards va a 0
		LinkedList<Integer> toCheck = new LinkedList<>();

		for (int c = 0; c < 40; c++) {
			if (Ex.contains(c) || pCards.contains(c))
				continue;

			carte.add(c);
			LinkedList<Integer> dom = new LinkedList<>();

			for (int p = 0; p < 4; p++) {
				// se p è il nostro player, non ha più carte o ha avuto un piombo
				// per questo seme, ignora
				if (p == player || nCarteRimanenti[p] <= 0 || piombi[p][c / 10])
					continue;

				// altrimenti aggiungi al dominio

				dom.add(p);
			}

			// Se il dominio di c è solo un player, lo assegna
			if (dom.size() == 1) {
				int p = dom.get(0);
				// toglie da carte, diminuisce nCarteRimanenti, assegna
				carte.remove((Integer) c);
				nCarteRimanenti[p] = nCarteRimanenti[p] - 1;
				assegnamento.put(c, p);

				assert nCarteRimanenti[p] >= 0;

				// ricontrollare la consistenza!
				if (nCarteRimanenti[p] == 0)
					toCheck.add(p);

			} else {
				// aggiunge il dominio
				// Collections.shuffle(dom);
				domini.put(c, new Vector<>(dom));
			}

		}

		ensureConsistency(toCheck);

		if (domini.size() == 0) {
			status = 2;
			soluzioni.offer(assegnamento);
			soluzioni.offer(Collections.emptyMap());
		}

	} // ENDCOSTR

	public void produce() {
		if (status != 0) return;
		status=1;
		ForkJoinPool fjp= ForkJoinPool.commonPool();
		long t1= System.currentTimeMillis();
		LinkedList<RandCSPslave> threads=new LinkedList<>();
		for (int i = 0; i < maxSol; i++) {
			RandCSPslave t=new RandCSPslave();
			fjp.execute(t);
			threads.add(t);
		}
		for(RandCSPslave t: threads)
		{
			t.join();
		}
		t1= System.currentTimeMillis() - t1;
		tempoEsec=t1;
		
		soluzioni.offer(Collections.emptyMap());
		status = 2;
	}

	
	private class RandCSPslave extends RecursiveAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected void compute() {
			HashMap<Integer, Integer> sol = new HashMap<>(assegnamento);
			int[] carteRimanenti = Arrays.copyOf(nCarteRimanenti, 4);

			ArrayList<Integer> carte = new ArrayList<>(domini.keySet());

			Collections.shuffle(carte, ThreadLocalRandom.current());
			for (Integer c : carte) {
				Vector<Integer> players = domini.get(c);
				Collections.shuffle(players, ThreadLocalRandom.current());
				for (Integer p : players) {
					if (carteRimanenti[p] > 0) {
						sol.put(c, p);
						carteRimanenti[p]--;
						break;
					}
				}
			}
			soluzioni.offer(sol);
		}
		
	}
	public boolean isDone() {
		return status == 2;
	}

	private void ensureConsistency(LinkedList<Integer> toCheck) {

		LinkedList<Integer> toDelete = new LinkedList<>();

		while (!toCheck.isEmpty()) {
			int p = toCheck.pop();
			// per ogni dominio controlliamo che non sia presente p
			for (Entry<Integer, Vector<Integer>> k : domini.entrySet()) {
				if (k.getValue().contains(p)) {

					// Se lo contiene lo dobbiamo togliere
					Vector<Integer> dom = k.getValue();
					dom.remove((Integer) p);
					assert dom.size() > 0;
					// Ora controlliamo se è diventato un assegnamento
					//
					if (dom.size() == 1) {
						int pToCheck = dom.get(0);
						int carta = k.getKey();
						toDelete.add(carta);
						// domini.remove(carta);
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

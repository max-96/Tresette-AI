package AI;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.Semaphore;

/**
 * Solver ad hoc per generare le possibili combinazioni di stati possibili
 * 
 * 
 *
 */
public class CSPSolver {

	public BlockingQueue<HashMap<Integer, Integer>> soluzioni;
	private Semaphore maxSol;

	// carte non assegnate
	private LinkedList<Integer> carte = new LinkedList<>();
	// domini di tali carte
	private HashMap<Integer, LinkedList<Integer>> domini= new HashMap<Integer, LinkedList<Integer>>();
	
	// assegnamento delle carte sicure
	private HashMap<Integer, Integer> assegnamento = new HashMap<Integer, Integer>();
	private int[] nCarteRimanenti = new int[4];
	private int status=0;

	public CSPSolver(int player, Set<Integer> Ex, List<Integer> pCards, int[] rCards, int maxStati, boolean[][] piombi) {

		soluzioni = new ArrayBlockingQueue<HashMap<Integer, Integer>>(maxStati);
		maxSol = new Semaphore(maxStati);
		nCarteRimanenti=Arrays.copyOf(rCards,4);
		

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
				domini.put(c, dom);
			}

		}
		
		ensureConsistency(toCheck);

//		while (!toCheck.isEmpty()) {
//			int p = toCheck.pop();
//			// per ogni dominio controlliamo che non sia presente p
//			for (Entry<Integer, LinkedList<Integer>> k : domini.entrySet()) {
//				if (k.getValue().contains(p)) {
//
//					// Se lo contiene lo dobbiamo togliere
//					LinkedList<Integer> dom = k.getValue();
//					dom.remove((Integer) p);
//					assert dom.size() > 0;
//					// Ora controlliamo se è diventato un assegnamento
//					//
//					if (dom.size() == 1) {
//						int pToCheck = dom.getFirst();
//						int carta = k.getKey();
//						carte.remove((Integer) carta);
//						domini.remove(carta);
//						assegnamento.put(carta, pToCheck);
//						nCarteRimanenti[pToCheck] = nCarteRimanenti[pToCheck] - 1;
//
//						assert nCarteRimanenti[pToCheck] >= 0;
//
//						// non ha più assegnamenti possibili e va controllato
//						if (nCarteRimanenti[pToCheck] == 0)
//							toCheck.add(pToCheck);
//
//					}
//				}
//
//			}
//
//		} // ENDWHILE
		
		if(domini.size()==0)
		{
			status=2;
			soluzioni.add(assegnamento);
		}
		

	} // ENDCOSTR

	public void produce() {

		if(status != 0) return; 
		
		ForkJoinPool fjp = ForkJoinPool.commonPool();
		FirstParCSP t= new FirstParCSP();
		fjp.execute(t);

	}
	
	public boolean isDone()
	{
		return status==2;
	}
	
	
	private void ensureConsistency(LinkedList<Integer> toCheck) {

		LinkedList<Integer> toDelete= new LinkedList<>();

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
					//
					if (dom.size() == 1) {
						int pToCheck = dom.getFirst();
						int carta = k.getKey();
						toDelete.add(carta);
//						domini.remove(carta);
						assegnamento.put(carta, pToCheck);
						nCarteRimanenti[pToCheck] = nCarteRimanenti[pToCheck] - 1;

						assert nCarteRimanenti[pToCheck] >= 0;

						// non ha più assegnamenti possibili e va controllato
						if (nCarteRimanenti[pToCheck] == 0)
							toCheck.add(pToCheck);
					}
				}

			}
			while(!toDelete.isEmpty())
				domini.remove((Integer) toDelete.pop());

		}

	}
	
	
	
	
	
	
	
	@SuppressWarnings("serial")
	private class FirstParCSP extends RecursiveAction
	{

		@Override
		protected void compute() {
			if (status != 0) return;
			status=1;
			
			LinkedList<ParCSP> threads = new LinkedList<>();
			Collections.shuffle(carte);
			int c = carte.getFirst();
			for (int p : domini.get(c)) {
				ParCSP t = new ParCSP(assegnamento, domini, nCarteRimanenti, c, p, soluzioni, maxSol);
				t.fork();
				threads.add(t);
			}
			
			for(ParCSP t: threads)
			{
				t.join();
			}
			status=2;
		}
		
	}

	
	
	
} // ENDCLASS

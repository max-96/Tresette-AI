package AI;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Solver ad hoc per generare le possibili combinazioni di stati possibili
 * 
 * @author Administrator
 *
 */
public class CSPSolver {

	public BlockingQueue<HashMap<Integer, Integer>>	soluzioni;
	private int maxStati = 10;
	
	//carte non assegnate
	private LinkedList<Integer> carte = new LinkedList<>();
	//domini di tali carte
	private HashMap<Integer, LinkedList<Integer>> domini;
	
	//assegnamento delle carte sicure
	private HashMap<Integer, Integer> asseg = new HashMap<Integer, Integer>();
	private int[] rCards = new int[4];
	private boolean[][] piombi = new boolean[4][4];

	public CSPSolver(int player, Set<Integer> Ex, List<Integer> pCards, int[] rCards, int maxStati) {

		soluzioni=new ArrayBlockingQueue<HashMap<Integer, Integer>>(maxStati);
		this.maxStati=maxStati;
		for(int i: pCards)
			asseg.put(i, player);
		

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
				if (p == player || rCards[p] <= 0 || piombi[p][c / 10])
					continue;

				// altrimenti aggiungi al dominio

				dom.add(p);
			}

			// Se il dominio di c è solo un player, lo assegna
			if (dom.size() == 1) {
				int p = dom.get(0);
				// toglie da carte, diminuisce rCards, assegna
				carte.remove(c);
				rCards[p] = rCards[p] - 1;
				asseg.put(c, p);

				assert rCards[p] >= 0;

				// ricontrollare la consistenza!
				if (rCards[p] == 0)
					toCheck.add(p);

			} else {
				// aggiunge il dominio
				domini.put(c, dom);
			}

		}

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
						int carta= k.getKey();
						carte.remove(carta);
						domini.remove(carta);
						asseg.put(carta, pToCheck);
						rCards[pToCheck] = rCards[pToCheck] - 1;

						assert rCards[pToCheck] >= 0;

						// non ha più assegnamenti possibili e va controllato
						if (rCards[pToCheck] == 0)
							toCheck.add(pToCheck);

					}
				}

			}

		} // ENDWHILE

	} // ENDCOSTR

	public void produce()
	{
		
		
	}
	
	
	
} // ENDCLASS

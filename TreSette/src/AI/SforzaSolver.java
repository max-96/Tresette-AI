package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
//import java.util.Map;
import java.util.Set;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

public class SforzaSolver {

	public BlockingQueue<List<List<Integer>>> possibiliAssegnamenti;
	public long tempoEsecuzione;

	private List<Integer>[] assegnamentoCarte = (ArrayList<Integer>[]) new ArrayList[4];
	private int[] carteMancanti = new int[4];
	private boolean[][] semiAttivi = new boolean[4][4];

	private List<Integer> carteLibere = new ArrayList<>();
	private List<Integer>[] carteLiberePerSeme = (ArrayList<Integer>[]) new ArrayList[4];
	private int status = 0;

//	private int idPlayer;
	private int numeroSoluzioni;

	/********************
	 * Solver parallelo che produce soluzioni distribuendo le carte equiprobabilmente ai giocatori.
	 * 
	 * @param id
	 *            Id del player
	 * @param CarteScartate
	 *            Insieme di carte scartate
	 * @param carteInMano
	 *            Lista delle carte in mano al giocatore
	 * @param carteMancanti
	 *            Numero di carte sconosciute che ha in mano ogni player
	 * @param semiAttivi
	 *            SemiAttivi[p][s] indica se il giocatore p puo' avere seme s
	 * @param numeroSoluzioni
	 *            Il numero di soluzioni da produrre
	 */

	public SforzaSolver(int id, Set<Integer> CarteScartate, List<Integer> carteInMano, int[] carteMancanti,
			boolean[][] semiAttivi, int numeroSoluzioni) {

//		idPlayer = id;
		this.carteMancanti = carteMancanti;
		carteMancanti[id] = 0;
		this.semiAttivi = semiAttivi;
		this.numeroSoluzioni = numeroSoluzioni;
		possibiliAssegnamenti = new ArrayBlockingQueue<>(numeroSoluzioni+1);

		for (int i = 0; i < 4; i++) {
			assegnamentoCarte[i] = new ArrayList<>();
			carteLiberePerSeme[i] = new ArrayList<>();

		}

		assegnamentoCarte[id] = new ArrayList<>(carteInMano);

		for (int carta = 0; carta < 40; carta++) {
			if (CarteScartate.contains(carta) || carteInMano.contains(carta))
				continue;

			carteLibere.add(carta);
			carteLiberePerSeme[carta / 10].add(carta);

		}
	}
	
	public SforzaSolver(int i, Object info, int n_TRAILS)
	{
		// TODO Auto-generated constructor stub
	}

	public void addInfo(Object o)
	{
		//TODO
		return;
	}

	
	/**
	 * Avvia la risoluzione e inizia a produrre gli assegnamenti possibili.
	 * Gli assegnamenti prodotti vengono inseriti
	 * nella <code><b>BlockingQueue</b> assegnamentiProdotti</code> .
	 * 
	 * @return <b>true</b> se e' possibile procedere alla produzione, <b>false</b> altrimenti
	 */
	public boolean startProducing() {
		if (status != 0)
			return false;

		status = 1;
		ForkJoinPool pool = ForkJoinPool.commonPool();

		pool.execute(new SforzaFirst());

		return true;

	}

	public boolean isDone() {
		return status == 2;
	}

	private class SforzaFirst extends RecursiveAction {

		private static final long serialVersionUID = 1L;

		@Override
		protected void compute() {
			long tempo=System.currentTimeMillis();
			LinkedList<SforzaSlave> threads = new LinkedList<>();

			for (int i = 0; i < numeroSoluzioni; i++) {

				SforzaSlave t = new SforzaSlave();
				t.fork();
				threads.add(t);
			}

			while (!threads.isEmpty())
				threads.pop().join();
			
			tempo=System.currentTimeMillis() - tempo;
			tempoEsecuzione=tempo;

			List<List<Integer>> sentinella= Collections.emptyList();
			possibiliAssegnamenti.offer(sentinella);
			status = 2;

		}

	}

	private class SforzaSlave extends RecursiveAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private List<Integer>[] assCarte = (ArrayList<Integer>[]) new ArrayList[4];
		private List<Integer> carteLib = new ArrayList<>();
		private List<Integer>[] carteLibPerSeme = (ArrayList<Integer>[]) new ArrayList[4];
		private int[] carteManc;

		@Override
		protected void compute() {
			copiaStrutture();

			for (int player = 0; player < 4; player++) {

				check();

				Collections.shuffle(carteLib, ThreadLocalRandom.current());
				int i = 0;

				while (carteManc[player] > 0) {

					while (!semiAttivi[player][(carteLib.get(i) / 10)])
						i++;

					Integer carta = carteLib.remove(i);

					assCarte[player].add(carta);
					carteLibPerSeme[carta / 10].remove(carta);
					carteManc[player] = carteManc[player] - 1;
					check();
				}

			}

			
			possibiliAssegnamenti.offer(Arrays.asList(assCarte));

		}

		private void copiaStrutture() {
			for (int i = 0; i < 4; i++) {
				assCarte[i] = new ArrayList<>(assegnamentoCarte[i]);
				carteLibPerSeme[i] = new ArrayList<>(carteLiberePerSeme[i]);
			}

			carteLib = new ArrayList<>(carteLibere);
			carteManc = Arrays.copyOf(carteMancanti, 4);

		}

		private void check() {
			for (int p = 0; p < 4; p++) {
				if (carteManc[p] == 0)
					continue;

				int somma = 0;
				for (int s = 0; s < 4; s++) {
					if (semiAttivi[p][s])
						somma += carteLibPerSeme[s].size();

				}

				assert somma >= carteManc[p];
				if (somma == carteManc[p]) {
					for (int s = 0; s < 4; s++) {
						if (semiAttivi[p][s]) {
							assCarte[p].addAll(carteLibPerSeme[s]);
							carteLib.removeAll(carteLibPerSeme[s]);
							carteLibPerSeme[s].clear();
							carteManc[p] = 0;
						}

					}
				}
			}
		}

	}

}

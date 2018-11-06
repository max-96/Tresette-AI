package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

import setting.Game.Info;

public class SforzaSolver
{
	// private Info info;
	private int n_TRAILS;
	private List<List<Integer>> assegnamentoCarte = new ArrayList<>(4);
	private int[] carteMancanti = new int[4];
	private List<List<Integer>> carteLiberePerPlayer = new ArrayList<>(4);
	// private Integer[] ordineAssegnamento = new Integer[4];

	private int status = 0;
	private BlockingQueue<List<List<Integer>>> possibiliAssegnamenti;

	public long tempoEsecuzione;

	public SforzaSolver(int playerID, List<Integer> carteInMano, Info info, int n_TRAILS)
	{
		this.n_TRAILS = n_TRAILS;
		// this.info = info;
		possibiliAssegnamenti = new ArrayBlockingQueue<>(n_TRAILS + 1);
		List<Integer> carteLibere = info.getAvailableCards();

		for (int i = 0; i < 4; i++)
		{
			if (i == playerID)
			{
				assegnamentoCarte.add(new ArrayList<>(carteInMano));
				carteMancanti[playerID] = 0;
			} else
			{
				assegnamentoCarte.add(info.getKnownCardsOfPlayer(i));
				carteMancanti[i] = info.getNumeroCarteInMano(i) - assegnamentoCarte.get(i).size();
			}

			carteLibere.removeAll(assegnamentoCarte.get(i));
			carteLiberePerPlayer.add(new ArrayList<>());
			// ordineAssegnamenti = i;
		}

		for (int carta : carteLibere)
			for (int p = 0; p < 4; p++)
				if (info.isSemeAttivoForPlayer(p, carta / 10))
					carteLiberePerPlayer.get(p).add(carta);

		// Arrays.sort(ordineAssegnamento,
		// Comparator.comparingInt(info::getNumeroSemiAttivi));
	}

	/**
	 * Avvia la risoluzione e inizia a produrre gli assegnamenti possibili. Gli
	 * assegnamenti prodotti vengono inseriti nella
	 * <code><b>BlockingQueue</b> assegnamentiProdotti</code> .
	 * 
	 * @return <b>true</b> se e' possibile procedere alla produzione, <b>false</b>
	 *         altrimenti
	 */
	public boolean startProducing()
	{
		if (status != 0)
			return false;

		status = 1;
		ForkJoinPool pool = ForkJoinPool.commonPool();

		pool.execute(new SforzaFirst());

		return true;

	}

	public boolean isDone()
	{
		return status == 2;
	}

	public BlockingQueue<List<List<Integer>>> getPossibiliAssegnamenti()
	{
		return possibiliAssegnamenti;
	}

	private class SforzaFirst extends RecursiveAction
	{
		private static final long serialVersionUID = 1L;

		@Override
		protected void compute()
		{
			long tempo = System.currentTimeMillis();
			LinkedList<SforzaSlave> threads = new LinkedList<>();

			for (int i = 0; i < n_TRAILS; i++)
			{

				SforzaSlave t = new SforzaSlave();
				t.fork();
				threads.add(t);
			}

			while (!threads.isEmpty())
				threads.pop().join();

			tempo = System.currentTimeMillis() - tempo;
			tempoEsecuzione = tempo;

			List<List<Integer>> sentinella = Collections.emptyList();
			possibiliAssegnamenti.offer(sentinella);
			status = 2;
		}
	}

	private class SforzaSlave extends RecursiveAction
	{
		private static final long serialVersionUID = 1L;

		private List<List<Integer>> assCarte = new ArrayList<>(4);
		private int[] carteManc;
		// private List<Integer> carteLib = new ArrayList<>();
		private List<List<Integer>> carteLibPerPlayer = new ArrayList<>(4);

		@Override
		protected void compute()
		{
			copiaStrutture();
//			int debug_counter = 0;

			for (int player = 0; player < 4; player++)
			{
				boolean skipcard = false, goout = false;
				Collections.shuffle(carteLibPerPlayer.get(player), ThreadLocalRandom.current());

				for (Integer card : carteLibPerPlayer.get(player))
				{
					if (carteManc[player] == 0)
						break;

					for (int p = player + 1; p < 4; p++)
					{
//						debug_counter++;
						if (carteLibPerPlayer.get(p).contains(card)
								&& carteLibPerPlayer.get(p).size() - 1 < carteManc[p])
						{
							skipcard = true;
							break;
						}

						for (int p2 = player + 2; p2 < 4; p2++)
						{
							if (carteLibPerPlayer.get(p).contains(card) && carteLibPerPlayer.get(p2).contains(card))
							{
								HashSet<Integer> union = new HashSet<>(carteLibPerPlayer.get(p));
								union.addAll(carteLibPerPlayer.get(p2));

								if (union.size() - 1 < carteManc[p] + carteManc[p2])
								{
									skipcard = true;
									goout = true;
									break;
								}
							}
							if (goout)
								break;
							for (int p3 = player + 3; p3 < 4; p3++)
							{
								if (carteLibPerPlayer.get(p).contains(card) && carteLibPerPlayer.get(p2).contains(card)
										&& carteLibPerPlayer.get(p3).contains(card))
								{
									HashSet<Integer> union = new HashSet<>(carteLibPerPlayer.get(p));
									union.addAll(carteLibPerPlayer.get(p2));
									union.addAll(carteLibPerPlayer.get(p3));

									// System.out.println(intersezione);
									if (union.size() - 1 < carteManc[p] + carteManc[p2] + carteManc[p3])
									{
										skipcard = true;
										goout = true;
										break;
									}
								}

							}
						}
					}
					if (skipcard)
						continue;

					assCarte.get(player).add(card);
					for (int p = player + 1; p < 4; p++)
						carteLibPerPlayer.get(p).remove(card);
					carteManc[player]--;
				}
			}
			// if(debug_counter>100)
//			System.out.println(debug_counter);
			// System.out.println(carteLibPerPlayer);
			possibiliAssegnamenti.offer(assCarte);
		}

		protected void compute2()
		{
			copiaStrutture();

			for (int player = 0; player < 4; player++)
			{
				boolean skipcard = false;
				Collections.shuffle(carteLibPerPlayer.get(player), ThreadLocalRandom.current());

				for (Integer card : carteLibPerPlayer.get(player))
				{
					if (carteManc[player] == 0)
						break;
					for (int p = player + 1; p < 4; p++)
						if (carteLibPerPlayer.get(p).contains(card)
								&& carteLibPerPlayer.get(p).size() - 1 < carteManc[p])
						{
							skipcard = true;
							break;
						}
					if (skipcard)
						continue;

					assCarte.get(player).add(card);
					for (int p = player + 1; p < 4; p++)
						carteLibPerPlayer.get(p).remove(card);
					carteManc[player]--;
				}
			}

			possibiliAssegnamenti.offer(assCarte);
		}

		private void copiaStrutture()
		{
			for (int i = 0; i < 4; i++)
			{
				assCarte.add(new ArrayList<>(assegnamentoCarte.get(i)));
				carteLibPerPlayer.add(new ArrayList<>(carteLiberePerPlayer.get(i)));
			}

			carteManc = Arrays.copyOf(carteMancanti, 4);
			// carteLib = new ArrayList<>(carteLibere);
		}
	}

}

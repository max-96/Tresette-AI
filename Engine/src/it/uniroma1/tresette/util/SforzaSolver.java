package it.uniroma1.tresette.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.ThreadLocalRandom;

import it.uniroma1.tresette.setting.Game.Info;

public class SforzaSolver
{
	private int n_TRAILS;
	private List<List<Integer>> assegnamentoCarte = new ArrayList<>(4);
	private int[] carteMancanti = new int[4];
	private List<List<Integer>> cartePossibiliPerPlayer = new ArrayList<>(4);
	private Map<Integer, List<Integer>> carteLiberePerPlayer = new HashMap<>();
	private List<Integer> playerAttivi = new ArrayList<>(3);

	private int status = 0;
	private BlockingQueue<List<List<Integer>>> possibiliAssegnamenti;

	public long tempoEsecuzione;

	public SforzaSolver(int playerID, List<Integer> carteInMano, Info info, int n_TRAILS)
	{
		this.n_TRAILS = n_TRAILS;
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
				if (carteMancanti[i] != 0)
					playerAttivi.add(i);
			}

			carteLibere.removeAll(assegnamentoCarte.get(i));
			cartePossibiliPerPlayer.add(new ArrayList<>());
		}
		
		for (int carta : carteLibere)
		{
			carteLiberePerPlayer.put(carta, new ArrayList<>());
			
			for (int p : playerAttivi)
				if (info.isSemeAttivoForPlayer(p, carta / 10))
				{
					carteLiberePerPlayer.get(carta).add(p);
					cartePossibiliPerPlayer.get(p).add(carta);
				}
		}
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
		private List<List<Integer>> cartePossPerPlayer = new ArrayList<>(4);
		private Map<Integer, List<Integer>> carteLibPerPlayer = new HashMap<>();
		private List<Integer> serviti = new ArrayList<>(4);

		@Override
		protected void compute()
		{
			copiaStrutture();

			for (Integer carta : carteLibPerPlayer.keySet())
			{
				for (int p = 0; p < 4; p++)
					if (!serviti.contains(p) && carteManc[p] == cartePossPerPlayer.get(p).size())
					{
						for (Integer c : cartePossPerPlayer.get(p))
						{
							assCarte.get(p).add(c);
							for (int pp = 0; pp < 4; pp++)
								if (pp != p)
									cartePossPerPlayer.get(pp).remove(c);
							carteManc[p]--;
							
							if (carteManc[p] == 0)
								serviti.add(p);
						}
					}
				
				if (serviti.size() == 4)
					break;
				
				List<Integer> possibiliPlayer = carteLibPerPlayer.get(carta);
				possibiliPlayer.removeAll(serviti);
				
				int player = possibiliPlayer.get(ThreadLocalRandom.current().nextInt(possibiliPlayer.size()));
				
				assCarte.get(player).add(carta);
				for (int p = 0; p < 4; p++)
					cartePossPerPlayer.get(p).remove(carta);
				carteManc[player]--;
				
				if (carteManc[player] == 0)
					serviti.add(player);
			}
			System.out.println(assCarte);
			possibiliAssegnamenti.offer(assCarte);
		}
		
		private void copiaStrutture()
		{
			for (int i = 0; i < 4; i++)
			{
				assCarte.add(new ArrayList<>(assegnamentoCarte.get(i)));
				cartePossPerPlayer.add(new ArrayList<>(cartePossibiliPerPlayer.get(i)));
			}
			
			for (int c : carteLiberePerPlayer.keySet())
				carteLibPerPlayer.put(c, new ArrayList<>(carteLiberePerPlayer.get(c)));

			carteManc = Arrays.copyOf(carteMancanti, 4);
			
			for (int p = 0; p < 4; p++)
				if (carteManc[p] == 0)
					serviti.add(p);
		}
	}

}
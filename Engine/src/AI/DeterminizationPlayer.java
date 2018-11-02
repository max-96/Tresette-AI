package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.LongAdder;

import AI.DeterministicAI.Factory;
import setting.Card;
import setting.Game.Info;
import setting.Player;

public class DeterminizationPlayer extends Player
{
	private Factory aiFactory;
	private int n_TRAILS;

	public DeterminizationPlayer(int id, Factory aiFactory, int n_TRAILS)
	{
		super(id);
		this.aiFactory = aiFactory;
		this.n_TRAILS = n_TRAILS;
	}

	@Override
	public Card getMove()
	{
		Info info = game.getInfo();
		SforzaSolver deter = new SforzaSolver(id, carteInMano, info, n_TRAILS); // TODO
																				// Carte
																				// Card
		deter.startProducing();
		BlockingQueue<List<List<Integer>>> sols = deter.getPossibiliAssegnamenti();

		try
		{
			List<RecursiveAction> threads = new ArrayList<>();
			ForkJoinPool pool = ForkJoinPool.commonPool();

			List<List<Integer>> s = sols.take();
			while (s != Collections.EMPTY_LIST)
			{
				s = sols.take();
				RecursiveAction r = aiFactory.getAI(id, s, info);
				pool.execute(r);
				threads.add(r);
			}

			for (RecursiveAction t : threads)
				t.join();

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		int bestMove = -1;
		int bestVal = -1;

		for (Entry<Integer, LongAdder> k : aiFactory.getPunti().entrySet())
		{
			if (bestVal < k.getValue().intValue())
			{
				bestMove = k.getKey();
				bestVal = k.getValue().intValue();
			}
		}

		return new Card(bestMove);
	}
}

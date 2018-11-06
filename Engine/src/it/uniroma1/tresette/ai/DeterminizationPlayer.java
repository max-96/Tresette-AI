package it.uniroma1.tresette.ai;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.LongAdder;

import it.uniroma1.tresette.ai.DeterministicAI.Factory;
import it.uniroma1.tresette.setting.Player;
import it.uniroma1.tresette.setting.Game.Info;
import it.uniroma1.tresette.util.Determinizer;

public class DeterminizationPlayer extends Player
{
	private Factory aiFactory;
	private int n_TRAILS;

	public DeterminizationPlayer(Factory aiFactory, int n_TRAILS)
	{
		super(aiFactory.getPlayerID());
		this.aiFactory = aiFactory;
		this.n_TRAILS = n_TRAILS;
	}

	@Override
	protected Integer computeMove()
	{
		Info info = game.getInfo();
		Determinizer deter = new Determinizer(id, carteInMano, info, n_TRAILS);
		deter.startProducing();
		BlockingQueue<List<List<Integer>>> sols = deter.getPossibiliAssegnamenti();

		try
		{
			List<RecursiveAction> threads = new ArrayList<>();
			ForkJoinPool pool = ForkJoinPool.commonPool();

			List<List<Integer>> s = sols.take();
			while (s != Collections.EMPTY_LIST)
			{
				RecursiveAction r = aiFactory.getAI(s, info);
				pool.execute(r);
				threads.add(r);
				
				s = sols.take();
			}

			for (RecursiveAction t : threads)
				t.join();

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}

		Integer bestMove = -1;
		int bestVal = -1;

		for (Entry<Integer, LongAdder> k : aiFactory.getPunti().entrySet())
		{
			if (bestVal < k.getValue().intValue())
			{
				bestMove = k.getKey();
				bestVal = k.getValue().intValue();
			}
		}
		
		aiFactory.clear();
		
		return bestMove;
	}
}

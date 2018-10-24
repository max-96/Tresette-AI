package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.atomic.LongAdder;

import minmax.AlphaBeta.AlphaBetaSlave;
import setting.Card;
import setting.Game;
import setting.Player;
import setting.Game.Info;

public class PlayerAB extends Player
{

	private final int N_TRAILS = 30;

	public PlayerAB(int id, List<Card> carte, Game gioco)
	{
		super(id, carte, gioco);
	}

	@Override
	public Card getMove()
	{
		int depth = 10;
		Info info = gioco.getInfo();
		double ourScore= info.getTeamScore(id % 2);
		double opponentsScore= info.getTeamScore(1-(id % 2));
		List<Integer> cardsOnTable = info.getCardsOnTable();
		SforzaSolver deter = new SforzaSolver(0, info, N_TRAILS);
		
		
		deter.startProducing();
		BlockingQueue<List<List<Integer>>> sols = deter.possibiliAssegnamenti;
		ConcurrentHashMap<Integer, LongAdder> punti = new ConcurrentHashMap<>();

		try
		{
			List<AlphaBetaSlave> threads= new ArrayList<>();
			ForkJoinPool pool= ForkJoinPool.commonPool();
			
			List<List<Integer>> s = sols.take();
			
			while (s != Collections.EMPTY_LIST)
			{

				s = sols.take();
				AlphaBetaSlave abs = new AlphaBetaSlave(id, depth, s, cardsOnTable, ourScore,
						opponentsScore, punti);
				pool.execute(abs);
				threads.add(abs);
	
			}
			
			for(AlphaBetaSlave t: threads)
				t.join();
			

		} catch (InterruptedException e)
		{
			e.printStackTrace();
		}
		
		Integer bestMove=-1;
		int bestVal=-1;
		
		for(Entry<Integer, LongAdder> k: punti.entrySet())
		{
			if(bestVal<k.getValue().intValue())
			{
				bestMove=k.getKey();
				bestVal=k.getValue().intValue();
			}
		}
		return new Card(bestMove);

	}

}

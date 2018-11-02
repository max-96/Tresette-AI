package AI;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.LongAdder;

import setting.Game.Info;

public abstract class DeterministicAI
{
	protected int playerID;
	
	public abstract static class Factory
	{
		protected ConcurrentHashMap<Integer, LongAdder> punti = new ConcurrentHashMap<>();
		
		public ConcurrentHashMap<Integer, LongAdder> getPunti()
		{
			return punti;
		}
		
		public abstract RecursiveAction getAI(int playerID, List<List<Integer>> assegnamentoCarte, Info info);
	}
	
	public DeterministicAI(int playerID)
	{
		this.playerID = playerID;
	}
	
//	public static List<Integer> possibiliMosse(List<Integer> carte, int semeAttuale)
//	{
//		List<Integer> mosse = new ArrayList<>();
//		for (Integer c : carte)
//		{
//			if (c / 10 == semeAttuale)
//				mosse.add(c);
//		}
//
//		if (mosse.isEmpty())
//			mosse.addAll(carte);
//
//		return mosse;
//	}

	public abstract int getBestMove(List<List<Integer>> assegnamentoCarte, Info info);
}

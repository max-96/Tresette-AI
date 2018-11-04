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
			ConcurrentHashMap<Integer, LongAdder> p = punti;
			clear();
			return p;
		}
		
		protected void clear()
		{
			punti = new ConcurrentHashMap<>();
		}
		
		public abstract RecursiveAction getAI(int playerID, List<List<Integer>> assegnamentoCarte, Info info);
	}
	
	public DeterministicAI(int playerID)
	{
		this.playerID = playerID;
	}

	public int getPlayerID()
	{
		return playerID;
	}

	public abstract int getBestMove(List<List<Integer>> assegnamentoCarte, Info info);
}

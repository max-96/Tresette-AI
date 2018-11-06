package it.uniroma1.tresette.ai;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.LongAdder;

import it.uniroma1.tresette.setting.Game.Info;

public abstract class DeterministicAI
{
	protected int playerID;
	
	public abstract static class Factory
	{
		protected int playerID;
		protected ConcurrentHashMap<Integer, LongAdder> punti = new ConcurrentHashMap<>();
		
		public Factory(int playerID)
		{
			this.playerID = playerID;
		}
		
		public int getPlayerID ()
		{
			return playerID;
		}
		
		public ConcurrentHashMap<Integer, LongAdder> getPunti()
		{
			return punti;
		}
		
		public void clear()
		{
			punti = new ConcurrentHashMap<>();
		}
		
		public abstract RecursiveAction getAI(List<List<Integer>> assegnamentoCarte, Info info);
	}
	
	public DeterministicAI(int playerID)
	{
		this.playerID = playerID;
	}

	public int getPlayerID()
	{
		return playerID;
	}

	public abstract Integer getBestMove(List<List<Integer>> assegnamentoCarte, Info info);
}

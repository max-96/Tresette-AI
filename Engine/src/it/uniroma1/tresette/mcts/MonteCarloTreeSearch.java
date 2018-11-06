package it.uniroma1.tresette.mcts;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.LongAdder;

import it.uniroma1.tresette.ai.DeterministicAI;
import it.uniroma1.tresette.setting.Game.Info;

public class MonteCarloTreeSearch extends DeterministicAI
{
	private int iterations;
	
	public long execTime;
	public static long maxExecTime = 0;

	public static class Factory extends DeterministicAI.Factory
	{
		private int iterations;
		
		public Factory(int playerID, int iterations)
		{
			super(playerID);
			this.iterations = iterations;
		}
		
		@Override
		public RecursiveAction getAI(List<List<Integer>> assegnamentoCarte, Info info)
		{
			return new Slave(playerID, iterations, assegnamentoCarte, info, punti);
		}
	}
	
	public MonteCarloTreeSearch(int playerID, int iterations)
	{
		super(playerID);
		this.iterations = iterations;
	}

	@Override
	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, Info info)
	{
		long execTime = System.currentTimeMillis();
		
		GameState starting = new GameState(playerID, assegnamentoCarte, info);
		MonteCarloTree MCT = new MonteCarloTree(starting);
		int m = MCT.execute(iterations);
		
		execTime = System.currentTimeMillis() - execTime;
		if (execTime > maxExecTime)
			maxExecTime = execTime;

		return m;
	}
	
	public static class Slave extends RecursiveAction
	{
		private static final long serialVersionUID = 1L;
		
		private int playerId;
		private int iterations;
		private List<List<Integer>> assegnamentoCarte;
		private Info info;
		private ConcurrentHashMap<Integer, LongAdder> punti;

		private Slave(int playerId, int iterations, List<List<Integer>> assegnamentoCarte,
				Info info, ConcurrentHashMap<Integer, LongAdder> punti)
		{
			this.playerId = playerId;
			this.iterations = iterations;
			this.assegnamentoCarte = assegnamentoCarte;
			this.info = info;
			this.punti = punti;
		}

		@Override
		protected void compute()
		{
			MonteCarloTreeSearch k = new MonteCarloTreeSearch(playerId, iterations);
			Integer r = k.getBestMove(assegnamentoCarte, info);
			punti.computeIfAbsent(r, key -> new LongAdder()).increment();
		}
	}
}

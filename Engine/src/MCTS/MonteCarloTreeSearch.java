package MCTS;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.LongAdder;

import AI.AIGameState;
import AI.DeterministicAI;
import setting.Game.Info;

public class MonteCarloTreeSearch extends DeterministicAI
{
	private int iterations;
	private double C_PARAM;
	
	public long execTime;
	public static long maxExecTime = 0;

	public static class Factory extends DeterministicAI.Factory
	{
		private int iterations;
		private double C_PARAM;
		
		public Factory(int playerID, int iterations, double c_param)
		{
			super(playerID);
			this.iterations = iterations;
			C_PARAM = c_param;
		}
		
		@Override
		public RecursiveAction getAI(List<List<Integer>> assegnamentoCarte, Info info)
		{
			return new Slave(playerID, iterations, C_PARAM, assegnamentoCarte, info, punti);
		}
	}
	
	public MonteCarloTreeSearch(int playerID, int iterations, double c_param)
	{
		super(playerID);
		this.iterations = iterations;
		C_PARAM = c_param;
	}

	@Override
	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, Info info)
	{
		long execTime = System.currentTimeMillis();
		
		AIGameState starting = new AIGameState(playerID, assegnamentoCarte, info);
		MonteCarloTree MCT = new MonteCarloTree(starting, C_PARAM);
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
		private double C_PARAM;
		private List<List<Integer>> assegnamentoCarte;
		private Info info;
		private ConcurrentHashMap<Integer, LongAdder> punti;

		private Slave(int playerId, int iterations, double c_param, List<List<Integer>> assegnamentoCarte,
				Info info, ConcurrentHashMap<Integer, LongAdder> punti)
		{
			this.playerId = playerId;
			this.iterations = iterations;
			C_PARAM = c_param;
			this.assegnamentoCarte = assegnamentoCarte;
			this.info = info;
			this.punti = punti;
		}

		@Override
		protected void compute()
		{
			MonteCarloTreeSearch k = new MonteCarloTreeSearch(playerId, iterations, C_PARAM);
			Integer r = k.getBestMove(assegnamentoCarte, info);
			punti.computeIfAbsent(r, key -> new LongAdder()).increment();
		}
	}
}

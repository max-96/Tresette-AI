package MCTS;

import java.util.List;

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
		private int C_PARAM;
		
		public Factory(int iterations, int c_param)
		{
			this.iterations = iterations;
			C_PARAM = c_param;
		}
		
		@Override
		public DeterministicAI getAI(int playerID)
		{
			return new MonteCarloTreeSearch(playerID, iterations, C_PARAM);
		}
	}
	
	public MonteCarloTreeSearch(int playerID, int iterations, double c_param)
	{
		super(playerID);
		this.iterations = iterations;
		C_PARAM = c_param;
	}

	@Override
	public int getBestMove(List<List<Integer>> assegnamentoCarte, Info info)
	{
		long execTime = System.currentTimeMillis();
		
		AIGameState starting = new AIGameState(assegnamentoCarte, cardsOnTable, playerId, true, scoreMyTeam,
				scoreOtherTeam);
		MonteCarloTree MCT = new MonteCarloTree(starting, C_PARAM);
		int m = MCT.execute(iterations);
		
		execTime = System.currentTimeMillis() - execTime;
		if (execTime > maxExecTime)
			maxExecTime = execTime;

		return m;
	}
}

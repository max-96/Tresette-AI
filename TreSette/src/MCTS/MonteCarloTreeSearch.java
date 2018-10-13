package MCTS;

import java.util.List;

import AI.DeterministicAI;
import AI.AIGameState;

public class MonteCarloTreeSearch extends DeterministicAI{

	private int playerId;
	private int iterations;
	public long execTime;
	public static long maxExecTime=0;
	public final double C_PARAM;
	
	public MonteCarloTreeSearch(int playerId, int iterations)
	{
		this(playerId, iterations, 0.75);
	}
	
	public MonteCarloTreeSearch(int playerId, int iterations, double c_param) {
		this.playerId = playerId;
		this.iterations=iterations;
		C_PARAM=c_param;
	}
	
	@Override
	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> cardsOnTable, double scoreMyTeam, double scoreOtherTeam)
	{
		long execTime=System.currentTimeMillis();
		AIGameState starting= new AIGameState(assegnamentoCarte, cardsOnTable, playerId, true, scoreMyTeam, scoreOtherTeam);
		MonteCarloTree MCT= new MonteCarloTree(starting, C_PARAM);
		Integer m= MCT.execute(iterations);
		execTime=System.currentTimeMillis() - execTime;
		if(execTime> maxExecTime)
			maxExecTime=execTime;
		
		return m;
	}
	
	
	
}

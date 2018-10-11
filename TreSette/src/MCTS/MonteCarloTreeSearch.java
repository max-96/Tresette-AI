package MCTS;

import java.util.Collections;
import java.util.List;

import AI.DeterministicAI;
import AI.AIGameState;

public class MonteCarloTreeSearch extends DeterministicAI{

	private int playerId;
	private int iterations;
	public long execTime;
	public static long maxExecTime=0;
	
	public MonteCarloTreeSearch(int playerId, int iterations) {
		this.playerId = playerId;
		this.iterations=iterations;
	}
	
	@Override
	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> cardsOnTable, double scoreMyTeam, double scoreOtherTeam)
	{
		execTime=System.currentTimeMillis();
		AIGameState starting= new AIGameState(assegnamentoCarte, cardsOnTable, playerId, true, scoreMyTeam, scoreOtherTeam);
		MonteCarloTree MCT= new MonteCarloTree(starting);
		Integer m= MCT.execute(iterations);
		execTime=System.currentTimeMillis() - execTime;
		if(execTime> maxExecTime)
			maxExecTime=execTime;
		return m;
	}
	
	
	
}

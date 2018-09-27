package MCTS;

import java.util.Collections;
import java.util.List;

public class MonteCarloTreeSearch {

	private int playerId;
	private int iterations;
	
	public MonteCarloTreeSearch(int playerId, int iterations) {
		this.playerId = playerId;
		this.iterations=iterations;
	}
	public Integer getBestMove(List<List<Integer>> assegnamentoCarte, List<Integer> cardsOnTable, double score)
	{
		GameState starting= new GameState(assegnamentoCarte, cardsOnTable, playerId, score, true);
		MonteCarloTree MCT= new MonteCarloTree(starting);
		return MCT.execute(iterations);
	}
	
	
	
}

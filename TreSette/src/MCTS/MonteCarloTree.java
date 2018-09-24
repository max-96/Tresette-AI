package MCTS;

import java.util.Set;

public class MonteCarloTree {
	
	private MCNode root;

	public Set<MCNode> frontier;
	public final int iterations;
	
	public MonteCarloTree(int iterations) {
		this.iterations=iterations;
	}
	
	
	public Integer getBestMove()
	{
		return root.getBestMove();
	}

}

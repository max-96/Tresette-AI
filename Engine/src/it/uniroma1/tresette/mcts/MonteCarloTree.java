package it.uniroma1.tresette.mcts;

import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class MonteCarloTree
{
	private MCNode root;

	public MonteCarloTree(GameState gamestate)
	{
		root = new MCNode(null, null, gamestate, this);
		root.generateChildren();
	}

	public Integer execute(int iterations)
	{

		for (int it = 0; it < iterations; it++)
		{
			// Select
			// Expand
			MCNode node = traverseAndExpand();
			// Simulate
			double outcome = node.playout();
			// Backtrack
			node.backpropagateStats(outcome > 0);
		}

		return root.getBestMove();
	}

	public Integer getBestMove()
	{
		return root.getBestMove();
	}

	private MCNode traverseAndExpand()
	{
		MCNode node = root;
		while (!node.isLeaf)
		{
			node = Collections.max(node.children);
		}

		if (node.isTerminal())
			return node;

		node.generateChildren();
		int i = ThreadLocalRandom.current().nextInt(node.children.size());
		return node.children.get(i);
	}

}

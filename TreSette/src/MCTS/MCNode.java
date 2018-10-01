package MCTS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import AI.GameState;

public class MCNode implements Comparable<MCNode> {

	public static final double C_PARAM = 0.75;
	public static final double EPS=1e-8;

	private MCNode parent;
	private final Integer generatingAction;
	private final AI.GameState gamestate;
	private final MonteCarloTree tree;
	protected boolean isLeaf;

	protected List<MCNode> children = Collections.emptyList();

	private int winCount = 0;
	private int visitCount = 0;
	private final boolean isBlackNode; 
	
	public MCNode(MCNode parent, Integer generatingAction, AI.GameState gamestate, MonteCarloTree tree) {
		this.parent = parent;
		this.generatingAction = generatingAction;
		this.gamestate = gamestate;
		this.tree = tree;
		isLeaf = true;
		isBlackNode=! this.gamestate.maxNode;
	}

	public void generateChildren() {
		if (!children.isEmpty() || gamestate.terminal)
			return;

		Map<Integer, GameState> figli = gamestate.generateSuccessors();
		children = new ArrayList<>();
		for (Entry<Integer, GameState> node : figli.entrySet()) {
			MCNode child = new MCNode(this, node.getKey(), node.getValue(), tree);
			children.add(child);
		}
		isLeaf = false;
	}

	protected double playout() {
		GameState gs = gamestate;
		while (!gs.terminal) {
			Integer mossa = gs.genRandMossa();
			gs = gs.genSuccessor(mossa);
		}
		return gs.scoreSoFar;

	}

	public double getPriority() {
		double priority = ((double) winCount) / (visitCount + EPS);
		priority += C_PARAM * Math.sqrt((Math.log(parent.visitCount + 1)) / (visitCount + EPS));

		return priority;
	}

	protected void backpropagateStats(boolean isWin) {
		MCNode node = this;
		while (node != null) {
			if (node.isBlackNode) {
				if (isWin)
					node.winCount += 1;
			} else {
				if (!isWin)
					node.winCount += 1;
			}
			node.visitCount += 1;
			node = node.parent;
		}

	}

	public boolean isTerminal() {
		return gamestate.terminal;
	}

	public Integer getBestMove() {

		int maxVisite = 0;
		Integer bestAction = -1;
		for (MCNode c : children) {
			if (c.visitCount > maxVisite) {
				maxVisite = c.visitCount;
				bestAction = c.generatingAction;
			}
		}
		return bestAction;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gamestate == null) ? 0 : gamestate.hashCode());
		result = prime * result + ((generatingAction == null) ? 0 : generatingAction.hashCode());
		result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MCNode other = (MCNode) obj;
		if (gamestate == null) {
			if (other.gamestate != null)
				return false;
		} else if (!gamestate.equals(other.gamestate))
			return false;
		if (generatingAction == null) {
			if (other.generatingAction != null)
				return false;
		} else if (!generatingAction.equals(other.generatingAction))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
		return true;
	}

	@Override
	public int compareTo(MCNode other) {
		return (int) Math.ceil(getPriority() - other.getPriority());
	}

}

package it.uniroma1.tresette.ismcts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import it.uniroma1.tresette.ai.AIGameState;

public class ISMCNode implements Comparable<ISMCNode>
{

	public InformationSet infoset;
	public static final double C_PARAM = 0.75;
	public static final double EPS = 1e-8;
	private ISMCNode parent;
	private final Integer generatingAction;
	private final ISMonteCarloTree tree;
	protected boolean isLeaf;

	protected List<ISMCNode> children = Collections.emptyList();

	private int winCount = 0;
	private int visitCount = 0;
	private boolean isBlackNode;

	public ISMCNode(ISMCNode parent, Integer generatingAction, InformationSet infoset, ISMonteCarloTree tree)
	{
		this.parent = parent;
		this.generatingAction = generatingAction;
		this.tree = tree;
		isLeaf = true;
		this.infoset = infoset;
		isBlackNode = !infoset.maxNode;
	}

	public ISMCNode(ISMCNode parent, Integer generatingAction, ISMonteCarloTree tree)
	{
		this.parent = parent;
		this.generatingAction = generatingAction;
		this.tree = tree;
		isLeaf = true;
	}

	public void init()
	{
		if (infoset == null)
		{
			infoset = parent.infoset.genSuccessor(generatingAction);
			isBlackNode = !infoset.maxNode;
		}
	}

	
	public void generateChildren()
	{
		if (!children.isEmpty() || infoset.terminal)
			return;

		List<Integer> mosse = infoset.getPossibleMoves();
		children = new ArrayList<>();

			
		for (Integer gaction : mosse)
		{
			ISMCNode child = new ISMCNode(this, gaction, tree);
			children.add(child);
		}
		isLeaf = false;
	}
//	public void generateChildren(List<List<Integer>> determin)
//	{
//		if (!children.isEmpty() || infoset.terminal)
//			return;
//
//		List<Integer> mosse = infoset.generateActions(determin);
//
//		if(children == Collections.EMPTY_LIST)
//			children = new ArrayList<>();
//		
//		
//		for (Integer gaction : mosse)
//		{
//			ISMCNode child = new ISMCNode(this, gaction, tree);
//			if (!children.contains(child))
//				children.add(child);
//		}
//		isLeaf = false;
//	}

	protected double playout(List<List<Integer>> determin)
	{
		init();
		int player = infoset.getCurrentPlayer();
		AIGameState gs = new AIGameState(infoset.getCurrentPlayer(), determin, infoset.getCardsOnTable(), infoset.getScore(player), infoset.getScore(player+1), infoset.maxNode);
		while (!gs.terminal)
		{
			Integer mossa = gs.genRandMossa();
			gs = gs.genSuccessor(mossa);
		}
		return gs.getScoreSoFar();
	}

	public double getPriority()
	{
		assert parent.visitCount > 0;
		return ((double) winCount) / (visitCount + EPS)
				+ C_PARAM * Math.sqrt(Math.log(parent.visitCount) / (visitCount + EPS));
	}

	protected void backpropagateStats(boolean isWin)
	{
		ISMCNode node = this;
		while (node != null)
		{
			if (node.isBlackNode)
			{
				if (isWin)
					node.winCount += 1;
			} else
			{
				if (!isWin)
					node.winCount += 1;
			}
			node.visitCount += 1;
			node = node.parent;
		}
	}

	public boolean isCompatible(List<List<Integer>> determ)
	{
		//init();
		InformationSet is= (infoset==null) ? parent.infoset.genSuccessor(generatingAction): infoset;
			
		return is.isCompatible(determ);
	}

	@Override
	public int compareTo(ISMCNode other)
	{
		return (int) Math.signum(getPriority() - other.getPriority());
	}

	public Integer getBestMove()
	{

		int maxVisite = 0;
		Integer bestAction = 0;
		boolean debug_flag=false;
		for (ISMCNode c : children)
		{
			debug_flag=true;
			if (c.visitCount > maxVisite)
			{
				maxVisite = c.visitCount;
				bestAction = c.generatingAction;
			}
		}
		assert debug_flag;
		return bestAction;
	}

	public boolean isTerminal()
	{
		return infoset.terminal;
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see java.lang.Object#hashCode()
//	 */
//	@Override
//	public int hashCode()
//	{
//		final int prime = 31;
//		int result = 1;
//		result = prime * result + ((generatingAction == null) ? 0 : generatingAction.hashCode());
//		result = prime * result + ((infoset == null) ? 0 : infoset.hashCode());
//		return result;
//	}
//
//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see java.lang.Object#equals(java.lang.Object)
//	 */
//	@Override
//	public boolean equals(Object obj)
//	{
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		ISMCNode other = (ISMCNode) obj;
//		if (generatingAction == null)
//		{
//			if (other.generatingAction != null)
//				return false;
//		} else if (!generatingAction.equals(other.generatingAction))
//			return false;
//		if (infoset == null)
//		{
//			if (other.infoset != null)
//				return false;
//		} else if (!infoset.equals(other.infoset))
//			return false;
//		return true;
//	}

}

package ismcts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import AI.AIGameState;
import MCTS.MCNode;
import MCTS.MonteCarloTree;

public class ISMCNode implements Comparable<ISMCNode>
{

	private InformationSet infoset;
	public static final double C_PARAM = 1.25;
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
		if(infoset == null)
		{
			infoset=parent.infoset.genSuccessor(generatingAction);
			isBlackNode= ! infoset.maxNode;
		}
	}
	
	public void generateChildren(Object determin) {
		if (!children.isEmpty() || infoset.terminal)
			return;

		//TODO aggiornare con la determinizzazione corretta
		
		
		List<Integer> mosse=infoset.generateActions(determin);
		children = new ArrayList<>();
		for (Integer node : mosse) {
			ISMCNode child = new ISMCNode(this, node, tree);
			children.add(child);
		}
		isLeaf = false;
	}
	
	protected double playout(Object determin) {
		//TODO update con determin vera
		init();
		InformationSet is = infoset;
		while (!is.terminal) {
			Integer mossa = is.genRandMossa(determin);
			is = is.genSuccessor(mossa);
		}
		return is.getScoreSoFar();
	}
	
	public double getPriority() {
		assert parent.visitCount>0;
		return ((double) winCount) / (visitCount + EPS) + C_PARAM * Math.sqrt(Math.log(parent.visitCount) / (visitCount + EPS));
	}
	
	public boolean isCompatible(Object determ)
	{
		//TODO
		return false;
	}
	
	@Override
	public int compareTo(ISMCNode other) {
		return (int) Math.signum(getPriority() - other.getPriority());
	}
}

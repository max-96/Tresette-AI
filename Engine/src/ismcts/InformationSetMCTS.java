package ismcts;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AI.PartialInfoAI;
import AI.SforzaSolver;
import setting.Game.Info;

public class InformationSetMCTS extends PartialInfoAI
{
	private int iterations;
	
	public long execTime;
	public static long maxExecTime = 0;

	public InformationSetMCTS(int playerID, int iterations)
	{
		super(playerID);
		
		this.iterations = iterations;
	}
	
	@Override
	public Integer getBestMove(List<Integer> carteInMano, Info info)
	{
		long execTime = System.currentTimeMillis();

		SforzaSolver deter = new SforzaSolver(playerID, carteInMano, info, iterations);
		deter.startProducing();
		
		List<Set<Integer>> possibleCards = new ArrayList<>(4);
		for (int i = 0; i < 4; i++)
		{
			Set<Integer> pCards = new HashSet<>(info.getAvailableCards());
			for (int j = 0; j < 4; j++)
				if (i != j) pCards.removeAll(info.getKnownCardsOfPlayer(j));
			
			possibleCards.add(pCards);
		}
		
		InformationSet is = new InformationSet(playerID, possibleCards, info);
		ISMonteCarloTree ismct = new ISMonteCarloTree(is);
		
		int bestMove = ismct.execute(deter.getPossibiliAssegnamenti(), iterations);
		
		execTime = System.currentTimeMillis() - execTime;
		this.execTime = execTime;
		if (execTime > maxExecTime)
			maxExecTime = execTime;

		return Integer.valueOf(bestMove);
	}

}

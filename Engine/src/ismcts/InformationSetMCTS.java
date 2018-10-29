package ismcts;

import java.util.ArrayList;
import java.util.List;

import setting.Game.Info;
import AI.DeterministicAI;
import AI.AIGameState;

public class InformationSetMCTS
{

	private int playerId;
	private int iterations;
	public long execTime;
	public static long maxExecTime = 0;
	public final double C_PARAM;

	public InformationSetMCTS(int playerId, int iterations)
	{
		this(playerId, iterations, 0.75);
	}

	public InformationSetMCTS(int playerId, int iterations, double c_param)
	{
		this.playerId = playerId;
		this.iterations = iterations;
		C_PARAM = c_param;
	}

	public Integer getBestMove(Info info, List<Integer> cardsOnTable, double scoreMyTeam, double scoreOtherTeam)
	{
		long execTime = System.currentTimeMillis();

		//TODO implement getbestmove
		
		
		
		
		execTime = System.currentTimeMillis() - execTime;
		this.execTime = execTime;
		if (execTime > maxExecTime)
			maxExecTime = execTime;

		
		return 0;
	}

}

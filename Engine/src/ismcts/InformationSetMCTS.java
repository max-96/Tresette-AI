package ismcts;

import java.util.List;

import AI.PartialInfoAI;
import setting.Game.Info;

public class InformationSetMCTS extends PartialInfoAI
{
	private int iterations;
	private final double C_PARAM;
	
	public long execTime;
	public static long maxExecTime = 0;

	public InformationSetMCTS(int playerId, int iterations)
	{
		this(playerId, iterations, 0.75);
	}

	public InformationSetMCTS(int playerID, int iterations, double c_param)
	{
		super(playerID);
		this.iterations = iterations;
		C_PARAM = c_param;
	}
	
	@Override
	public int getBestMove(List<Integer> carteInmano, Info Info)
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

package AI;

import java.util.List;

import setting.Game.Info;

public abstract class PartialInfoAI
{
	protected int playerID;
	
	public PartialInfoAI(int playerID)
	{
		this.playerID = playerID;
	}

	public int getPlayerID()
	{
		return playerID;
	}
		
	public abstract int getBestMove(List<Integer> carteInMano, Info info);
}

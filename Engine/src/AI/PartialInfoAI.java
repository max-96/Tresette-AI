package AI;

import setting.Game.Info;

public abstract class PartialInfoAI
{
	protected int playerID;
	
	public PartialInfoAI(int playerID)
	{
		this.playerID = playerID;
	}

	public abstract int getBestMove(, Info info);

}

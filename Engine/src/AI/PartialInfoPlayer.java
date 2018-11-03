package AI;

import setting.Player;

public class PartialInfoPlayer extends Player
{
	private PartialInfoAI ai;
	
	public PartialInfoPlayer(PartialInfoAI ai)
	{
		super(ai.getPlayerID());
		this.ai = ai;
	}

	@Override
	public int getMove()
	{
		return ai.getBestMove(carteInMano, game.getInfo());
	}
}
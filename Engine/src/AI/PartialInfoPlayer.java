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
	protected Integer computeMove()
	{
		return ai.getBestMove(carteInMano, game.getInfo());
	}
}
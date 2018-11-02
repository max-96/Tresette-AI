package AI;

import setting.Card;
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
	public Card getMove()
	{
		int bestMove = ai.getBestMove(carteInMano, game.getInfo());
		return new Card(bestMove);
	}
}
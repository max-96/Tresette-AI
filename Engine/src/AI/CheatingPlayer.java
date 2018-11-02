package AI;

import java.util.List;

import setting.Card;
import setting.Player;

public class CheatingPlayer extends Player
{
	private DeterministicAI ai;
	
	public CheatingPlayer(DeterministicAI ai)
	{
		super(ai.playerID);
		this.ai = ai;
	}
	
	@Override
	public Card getMove()
	{		
		int bestMove = ai.getBestMove(game.getAssegnamentoCarte(), game.getInfo());
		return new Card(bestMove);
	}
}
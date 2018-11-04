package AI;

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
	protected Integer computeMove()
	{
		return ai.getBestMove(game.getAssegnamentoCarte(), game.getInfo());
	}
}
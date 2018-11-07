package it.uniroma1.tresette.ai;

import it.uniroma1.tresette.setting.Player;

public class CheatingPlayer extends Player
{
	private DeterministicAI ai;
	
	public CheatingPlayer(DeterministicAI ai)
	{
		super(ai.getPlayerID());
		this.ai = ai;
	}
	
	@Override
	protected Integer computeMove()
	{
		return ai.getBestMove(game.getAssegnamentoCarte(), game.getInfo());
	}
	
	@Override
	public String toString()
	{
		return "Cheating " + ai;
	}
}
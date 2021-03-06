package it.uniroma1.tresette.ai;

import it.uniroma1.tresette.setting.Player;

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
	
	@Override
	public String toString()
	{
		return ai.toString();
	}
}
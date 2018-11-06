package it.uniroma1.tresette.ai;

import java.util.List;

import it.uniroma1.tresette.setting.Game.Info;

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
		
	public abstract Integer getBestMove(List<Integer> carteInMano, Info info);
}

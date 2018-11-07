package it.uniroma1.tresette.ai;

import java.util.List;
import java.util.Random;

import it.uniroma1.tresette.setting.Game.Info;
import it.uniroma1.tresette.util.CardsUtils;

public class RandWalk extends PartialInfoAI
{
	private Random rand;
		
	public RandWalk(int playerID)
	{
		super(playerID);
		rand = new Random();
	}

	@Override
	public Integer getBestMove(List<Integer> carteInMano, Info info)
	{
		List<Integer> mosse = CardsUtils.getPossibiliMosse(carteInMano, info.getCardsOnTable());
		return mosse.get(rand.nextInt(mosse.size()));
	}
	
	@Override
	public String toString()
	{
		return "Random Walk";
	}
}

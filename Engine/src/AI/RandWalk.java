package AI;

import java.util.List;
import java.util.Random;

import setting.Game.Info;
import util.CardsUtils;

public class RandWalk extends PartialInfoAI
{
	private Random rand;
		
	public RandWalk(int playerID)
	{
		super(playerID);
		rand = new Random();
	}

	@Override
	public int getBestMove(List<Integer> carteInMano, Info info)
	{
		List<Integer> mosse = CardsUtils.getPossibiliMosse(carteInMano, info.getCardsOnTable());

		return mosse.get(rand.nextInt(mosse.size()));
	}
}

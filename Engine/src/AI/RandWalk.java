package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import setting.Game.Info;

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
		List<Integer> mosse = info.getTurn() == 0 ? new ArrayList<>(assegnamentoCarte.get(playerID))
				: DeterministicAI.possibiliMosse(assegnamentoCarte.get(playerID), info.getCardsOnTable().get(0) / 10);

		return mosse.get(rand.nextInt(mosse.size()));
	}
}

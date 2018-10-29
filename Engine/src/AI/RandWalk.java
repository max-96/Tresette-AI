package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import setting.Game.Info;

public class RandWalk extends DeterministicAI
{
	private Random rand;
	
	public static class Factory extends DeterministicAI.Factory
	{
		@Override
		public DeterministicAI getAI(int playerID)
		{
			return new RandWalk(playerID);
		}
	}
	
	public RandWalk(int playerID)
	{
		super(playerID);
		rand = new Random();
	}

	@Override
	public int getBestMove(List<List<Integer>> assegnamentoCarte, Info info)
	{
		List<Integer> mosse = info.getTurn() == 0 ? new ArrayList<>(assegnamentoCarte.get(playerID))
				: DeterministicAI.possibiliMosse(assegnamentoCarte.get(playerID), info.getCardsOnTable().get(0) / 10);

		return mosse.get(rand.nextInt(mosse.size()));

	}

}

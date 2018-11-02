package AI;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.RecursiveAction;

import setting.Game.Info;

public class RandWalk extends DeterministicAI
{
	private Random rand;
		
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

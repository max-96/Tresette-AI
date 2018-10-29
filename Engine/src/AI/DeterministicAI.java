package AI;

import java.util.ArrayList;
import java.util.List;

import setting.Game.Info;

public abstract class DeterministicAI
{
	protected int playerID;
	
	public abstract static class Factory
	{
		public abstract DeterministicAI getAI(int playerID);
	}
	
	public DeterministicAI(int playerID)
	{
		this.playerID = playerID;
	}
	
	public static List<Integer> possibiliMosse(List<Integer> carte, int semeAttuale)
	{
		List<Integer> mosse = new ArrayList<>();
		for (Integer c : carte)
		{
			if (c / 10 == semeAttuale)
				mosse.add(c);
		}

		if (mosse.isEmpty())
			mosse.addAll(carte);

		return mosse;
	}

	public abstract int getBestMove(List<List<Integer>> assegnamentoCarte, Info info);
}

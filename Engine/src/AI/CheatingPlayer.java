package AI;

import java.util.List;

import setting.Player;
import util.CardsUtils;

public class CheatingPlayer extends Player
{
	private DeterministicAI ai;
	
	public CheatingPlayer(DeterministicAI ai)
	{
		super(ai.playerID);
		this.ai = ai;
	}
	
	@Override
	public int getMove()
	{
		List<Integer> mosse = CardsUtils.getPossibiliMosse(carteInMano, game.getInfo().getCardsOnTable());
		if (mosse.size() == 1)
			return mosse.get(0);
		
		return ai.getBestMove(game.getAssegnamentoCarte(), game.getInfo());
	}
}
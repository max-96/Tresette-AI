package AI;

import java.util.List;

import setting.Player;
import util.CardsUtils;

public class PartialInfoPlayer extends Player
{
	private PartialInfoAI ai;
	
	public PartialInfoPlayer(PartialInfoAI ai)
	{
		super(ai.getPlayerID());
		this.ai = ai;
	}

	@Override
	public int getMove()
	{
		List<Integer> mosse = CardsUtils.getPossibiliMosse(carteInMano, game.getInfo().getCardsOnTable());
		if (mosse.size() == 1)
			return mosse.get(0);
		
		return ai.getBestMove(carteInMano, game.getInfo());
	}
}
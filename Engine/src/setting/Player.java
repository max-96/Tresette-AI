package setting;

import java.util.List;

import util.CardsUtils;

public abstract class Player
{
	protected int id;
	protected List<Integer> carteInMano;
	protected Game game;

	public Player(int id)
	{
		this.id = id;
	}

	public void setCards(List<Integer> carteInMano)
	{
		this.carteInMano = carteInMano;
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}
	
	public int getID()
	{
		return this.id;
	}
	
	protected abstract int computeMove();
	
	public int getMove()
	{
		List<Integer> mosse = CardsUtils.getPossibiliMosse(carteInMano, game.getInfo().getCardsOnTable());
		if (mosse.size() == 1)
			return mosse.get(0);
		
		Integer bestMove = computeMove();
		carteInMano.remove(bestMove);
		
		return bestMove;
	}
}

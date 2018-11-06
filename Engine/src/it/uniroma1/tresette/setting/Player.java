package it.uniroma1.tresette.setting;

import java.util.List;

import it.uniroma1.tresette.util.CardsUtils;

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
	
	protected abstract Integer computeMove();
	
	public int getMove()
	{
		List<Integer> mosse = CardsUtils.getPossibiliMosse(carteInMano, game.getInfo().getCardsOnTable());
		Integer bestMove;
		
		if (mosse.size() == 1)
			bestMove = mosse.get(0);
		else
			bestMove = computeMove();
		
		carteInMano.remove(bestMove);
		return bestMove;
	}
}

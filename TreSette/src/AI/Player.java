package AI;

import java.util.List;

import setting.Card;
import setting.Game;

public abstract class Player
{
	protected int id;
	protected List<Card> carteInMano;
	protected Game game;

	public Player(int id)
	{
		this.id = id;
	}

	public void setCards(List<Card> carteInMano)
	{
		this.carteInMano = carteInMano;
	}
	
	public void setGame(Game game)
	{
		this.game = game;
	}

	public abstract Card getMove();
}

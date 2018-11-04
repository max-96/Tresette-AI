package setting;

import java.util.List;

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
	
	public abstract int getMove();
}

package AI;

import java.util.List;

import setting.Card;
import setting.Game;

public abstract class Player
{
	protected int id;
	protected List<Card> carteInMano;
	protected Game gioco;
	
	public Player(int id, List<Card> carte, Game gioco)
	{
		this.id = id;
		carteInMano = carte;
		this.gioco=gioco;
	}
	
	public abstract Card getMossa();
}

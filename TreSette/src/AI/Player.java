package AI;

import java.util.LinkedList;

import setting.Card;
import setting.Game;

public abstract class Player
{
	protected int id;
	protected LinkedList<Card> carteInMano;
	protected Game gioco;
	
	public Player(int id, LinkedList<Card> carte, Game gioco)
	{
		this.id = id;
		carteInMano = carte;
		this.gioco=gioco;
	}
	
	public abstract Card getMossa();
}

package it.ai.tresette.player;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.ai.tresette.GameManager;
import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.CardsInHand;

public abstract class Player
{
	protected int id;
	protected CardsInHand myCards;
	protected KindOfPlayer myKind;

	public Player(int id, KindOfPlayer myKind)
	{
		this.id = id;
		this.myKind = myKind;
	}

	/**
	 * questo metodo ritorna la mossa che il player esegue nel turno
	 */
	public abstract Card getMove();

	public void draw(SpriteBatch batch)
	{
		this.myCards.draw(batch);
	}
	
	public abstract void setInfo(GameManager game);
	
	public void setCardsInHand(List<Card> myCards)
	{
		this.myCards = new CardsInHand(myCards, id);
	}
	
	/**
	 * this methods return the cards in hand represented by a list of integers
	 * 
	 * @return
	 */
	public List<Integer> getCardsInHand()
	{
		return this.myCards.toIntList();
	}

	public KindOfPlayer getKind()
	{
		return this.myKind;
	}

}

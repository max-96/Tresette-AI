package it.ai.tresette.player;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.CardsInHand;
import it.ai.tresette.objects.CardsOnTable;

public abstract class Player
{
	private static final String[] STRING_REPR = {"\u21a7", "↦", "↥", "↤"};
	
	protected int id;
	protected CardsInHand myCards;
	protected it.uniroma1.tresette.setting.Player ai;
	protected KindOfPlayer myKind;

	public Player(int id, it.uniroma1.tresette.setting.Player ai, KindOfPlayer myKind)
	{
		this.id = id;
		this.ai = ai;
		this.myKind = myKind;
	}

	/**
	 * questo metodo ritorna la mossa che il player esegue nel turno
	 */
	public abstract Card getMove(CardsOnTable cardsOnTable);

	public void draw(SpriteBatch batch)
	{
		myCards.draw(batch);
	}
	
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

	public it.uniroma1.tresette.setting.Player getAI()
	{
		return this.ai;
	}
	
	public KindOfPlayer getKind()
	{
		return this.myKind;
	}
	
	@Override
	public String toString()
	{
		return STRING_REPR[id];
	}

}

package it.ai.tresette.player;

import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.CardsInHand;
import it.ai.tresette.objects.CardsOnTable;

public abstract class Player {

	protected CardsInHand myCards;
	
	protected int id;
	
	protected KindOfPlayer myKind;
	
	public Player(int id, CardsInHand cards)
	{
		this.id = id;
		this.myCards = cards;
	}
	
	/**
	 * questo metodo ritorna la mossa che il player esegue nel turno
	 */
	public abstract Card getMossa(int id,List<List<Integer>> assegnamentoCarte,CardsOnTable cardsOnTable,
			double scoreMyTeam, double scoreOtherTeam);
	
	
	public abstract Card getMossa();
	
	public abstract void draw(SpriteBatch batch);
	/**
	 * this methods return the cards in hand represented by a list of integers
	 * @return
	 */
	public  List<Integer> getCardsInHand()
	{
		return this.myCards.toIntList();
		
	}
	
	public KindOfPlayer getKind()
	{
		return this.myKind;
	}
	
}

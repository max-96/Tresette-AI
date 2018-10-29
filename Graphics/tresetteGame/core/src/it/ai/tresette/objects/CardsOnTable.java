package it.ai.tresette.objects;

import java.util.ArrayList;
import java.util.List;
/**
 * A class created in order to manage the drawing of cards on the table
 * @author Zamma
 *
 */
public class CardsOnTable {

	private ArrayList<Card> cards;
	
	private int cardsOnTable;
	
	public CardsOnTable()
	{
		this.cards = new ArrayList<Card>();
		this.cardsOnTable = 0;
	}
	
	/**
	 * 
	 * @param a
	 * @return
	 */
	public boolean add(Card a)
	{
		if(cardsOnTable == 4) return false;
		cards.add(a);
		cardsOnTable++;
		return true;
	}
	
	/**
	 * this method returns the cards on the table represented as Integer
	 * @return
	 */
	public List<Integer> getCardsOnTable()
	{
		List<Integer> res = new ArrayList<>();
		for(Card c : cards)
			res.add(c.toInt());
		return res;
	}
	
	public void draw()
	{
		if(cardsOnTable == 0) return;
	
	}

	public void reset()
	{
		this.cards = new ArrayList<Card>();
		
		cardsOnTable = 0;
	}
	
	/**
	 * this method return the suit of this "hand" represented by an int; It actually returns the suit of the first card dropped on the table
	 * @return
	 */
	public int getActualSuit()
	{
		return (cards.get(0).getCardnr()/10);
	}
	
	/**
	 * return true if there are no cards on table
	 * @return
	 */
	public boolean isEmpty()
	{
		return (this.cardsOnTable==0);
	}
}

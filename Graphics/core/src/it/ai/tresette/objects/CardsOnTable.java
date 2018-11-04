package it.ai.tresette.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
	
	public void draw(SpriteBatch batch)
	{
		int x;
		int y = Constants.WINDOW_HEIGHT/2 - Card.getHeigth()/2;
		if(cardsOnTable == 0) return;
		else if(cardsOnTable == 1)								//se dobbiamo disegnare una carta, la disegniamo al centro
		{
			x = (Constants.WINDOW_WIDTH/2) - Card.getWidth()/2;
			for(Card a : this.cards)
				a.draw(batch,x,y,Constants.TABLE_CARD_SCALE);
		}
		else if(cardsOnTable == 2)								//se sono due...
		{
			x = (Constants.WINDOW_WIDTH/2) - Card.getWidth() - Constants.TABLE_CARDS_OFFSIDE/2;
			for(Card a : this.cards)
			{
				a.draw(batch,x,y,Constants.TABLE_CARD_SCALE);
				x += Card.getWidth() + Constants.TABLE_CARDS_OFFSIDE;
			}
		}
		else if (cardsOnTable == 3)
		{
			x = (Constants.WINDOW_WIDTH/2) - Card.getWidth() - Constants.TABLE_CARDS_OFFSIDE - Card.getWidth()/2;
			for(Card a : this.cards)
			{
				a.draw(batch,x,y,Constants.TABLE_CARD_SCALE);
				x += Card.getWidth() + Constants.TABLE_CARDS_OFFSIDE;
			}
		}
		else if(cardsOnTable == 4)								//se sono 4...
		{
			x = (Constants.WINDOW_WIDTH/2) - Card.getWidth()*2 - Constants.TABLE_CARDS_OFFSIDE*3/2;
			for(Card a : this.cards)
			{
				a.draw(batch,x,y,Constants.TABLE_CARD_SCALE);
				x += Card.getWidth() + Constants.TABLE_CARDS_OFFSIDE;
			}
		}
		
	
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
	
	@Override
	public String toString()
	{
		return this.cards.toString();
	}
}

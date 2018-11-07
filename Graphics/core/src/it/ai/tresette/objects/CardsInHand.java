package it.ai.tresette.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class CardsInHand
{
	private List<Card> cardsInHand;
	
	//private Player player; 
	
	/**
	 * the player number is used for rendering purposes
	 */
	private int playerNumber;

	@SuppressWarnings("unused")
	private int cardCounter;
	
	public CardsInHand(List<Card> cards,int playerNumber) 
	{
		this(cards,playerNumber,10);
	}
	
	public CardsInHand(List<Card> cards,int playerNumber,int cardCounter) 
	{
		cardsInHand = cards;
		this.playerNumber = playerNumber;
		this.cardCounter = cardCounter;
	}
	
	public Card remove(Card a) 
	{
		return remove(a.toInt());
	}
	
	public Card remove(int cardNr)
	{
		Card temp = null;
		for(Card a : cardsInHand)
			if(cardNr == a.toInt())
				temp = a;
		cardsInHand.remove(temp);
		cardCounter--;
		return temp;
	}
	
	/**
	 * in 2vs2 tresette this method is useless, but not in 1vs1 game
	 * @param a
	 */
	public void add(Card a) 
	{
		 cardsInHand.add(a);
		 this.cardCounter++;
	}
	
	public void draw(SpriteBatch batch)
	{
		int startingX,startingY;
		
		switch(this.playerNumber)
		{
			case 0:
				startingX = (Constants.WINDOW_WIDTH - Constants.TABLE_EDGE)/2;
				startingY = (Constants.WINDOW_HEIGHT - Constants.TABLE_EDGE)/2 - Constants.CARDS_OFFSIDE_VERTICAL;
				for(Card a : this.cardsInHand)
				{
					a.draw(batch, startingX, startingY);
					startingX += Constants.TABLE_EDGE/this.cardsInHand.size();
				}
				break;
			case 1:
				startingX = (Constants.WINDOW_WIDTH + Constants.TABLE_EDGE)/2 + Constants.CARDS_OFFSIDE_ORIZZONTAL;
				startingY = (Constants.WINDOW_HEIGHT - Constants.TABLE_EDGE)/2;
				for(Card a: this.cardsInHand)
				{
					a.draw(batch, startingX, startingY,this.playerNumber); //da fixare vengono ruotate al contrario
					startingY += Constants.TABLE_EDGE/this.cardsInHand.size();
				}
				break;
			case 2:
				startingX = (Constants.WINDOW_WIDTH + Constants.TABLE_EDGE)/2;
				startingY = (Constants.WINDOW_HEIGHT + Constants.TABLE_EDGE)/2 + Constants.CARDS_OFFSIDE_VERTICAL;
				for(Card a: this.cardsInHand)
				{
					a.draw(batch, startingX, startingY,this.playerNumber); //da fixare vengono ruotate al contrario
					startingX -= Constants.TABLE_EDGE/this.cardsInHand.size();
				}
				break;
			case 3:
				startingX = (Constants.WINDOW_WIDTH - Constants.TABLE_EDGE)/2 - Constants.CARDS_OFFSIDE_ORIZZONTAL;
				startingY = (Constants.WINDOW_HEIGHT + Constants.TABLE_EDGE)/2;
				for(Card a: this.cardsInHand)
				{
					a.draw(batch, startingX, startingY,this.playerNumber); //da fixare vengono rotate al contrario
					startingY -= Constants.TABLE_EDGE/this.cardsInHand.size();
				}
				break;
		}	
	}
	
	public boolean contains(Card b)
	{
		return this.cardsInHand.contains(b);
	}
	
	/**
	 * returns the cards in hand represented by a list of integers
	 * @return
	 */
	public List<Integer> toIntList()
	{
		List<Integer> res = new ArrayList<>();
		for(Card a : this.cardsInHand)
			res.add(a.toInt());
		return res;
	}
}

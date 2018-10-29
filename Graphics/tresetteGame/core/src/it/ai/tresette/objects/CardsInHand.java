package it.ai.tresette.objects;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.ai.tresette.player.Player;

public class CardsInHand {
	
	
	private List<Card> cardsInHand;
	
	//private Player player; 
	
	/**
	 * the player number is used for rendering purposes
	 */
	private int playerNumber;
	
	//private int playerTeam;
	

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
	
	public void remove(Card a) 
	{
		if(!cardsInHand.contains(a)) return;
		cardsInHand.remove(a);
		cardCounter--;
	}
	
	public Card remove(Integer cardNr)
	{
		Card temp = null;
		for(Card a : cardsInHand)
			if(cardNr.equals(a.toInt()))
				temp = a;
		remove(temp);
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
		if(this.playerNumber != 0)
			return;
		int startingX = (Constants.WINDOW_WIDTH - Constants.TABLE_EDGE)/2;
		int startingY = (Constants.WINDOW_HEIGTH - Constants.TABLE_EDGE)/2;
		for(Card a : this.cardsInHand)
		{
			a.draw(batch, startingX, startingY);
			startingX += Constants.TABLE_EDGE/this.cardsInHand.size();
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

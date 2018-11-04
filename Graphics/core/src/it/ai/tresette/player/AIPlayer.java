package it.ai.tresette.player;

import java.util.List;

import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;

public class AIPlayer extends Player
{
	public AIPlayer(int id, setting.Player ai)
	{
		super(id, ai, KindOfPlayer.AIPLAYER);
	}
	
	@Override
	public Card getMove(List<Integer> cardsOnTable)
	{
		Card mossa = new Card(ai.getMove());
		myCards.remove(mossa);
		
		return mossa;
	}
	
	@Override
	public void setCardsInHand(List<Card> myCards)
	{
		super.setCardsInHand(myCards);
		ai.setCards(getCardsInHand());
	}
}
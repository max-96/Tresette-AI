package it.ai.tresette.player;

import java.util.List;

import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.CardsOnTable;

public class AIPlayer extends Player
{
	public AIPlayer(it.uniroma1.tresette.setting.Player ai)
	{
		super(ai.getID(), ai, KindOfPlayer.AIPLAYER);
	}
	
	@Override
	public Card getMove(CardsOnTable cardsOnTable)
	{
		Card mossa = new Card(ai.getMove());
		return myCards.remove(mossa);
	}
	
	@Override
	public void setCardsInHand(List<Card> myCards)
	{
		super.setCardsInHand(myCards);
		ai.setCards(getCardsInHand());
	}
}
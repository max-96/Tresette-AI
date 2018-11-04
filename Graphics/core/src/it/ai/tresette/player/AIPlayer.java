package it.ai.tresette.player;

import java.util.List;

import it.ai.tresette.GameManager;
import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;

public class AIPlayer extends Player
{
	private setting.Player ai;
	
	public AIPlayer(int id, setting.Player ai)
	{
		super(id, KindOfPlayer.AIPLAYER);
		this.ai = ai;
	}

	@Override
	public Card getMove()
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
	
	@Override
	public void setInfo(GameManager game)
	{
		ai.setGame(game.getGameInfo());
	}
}
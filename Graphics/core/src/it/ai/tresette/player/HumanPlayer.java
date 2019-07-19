package it.ai.tresette.player;

import java.util.List;
import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.input.*;
import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.CardsOnTable;
import it.ai.tresette.objects.Constants;
import it.uniroma1.tresette.util.CardsUtils;

public class HumanPlayer extends Player
{
	private Scanner keyboard = new Scanner(System.in);
	
	private boolean isPrinted = false;
	
	public static class DummyPlayer extends it.uniroma1.tresette.setting.Player
	{
		private DummyPlayer(int id)
		{
			super(id);
		}

		@Override
		protected Integer computeMove()
		{
			return Integer.valueOf(-1);
		}
	}
	
	public HumanPlayer(int id)
	{
		super(id, new DummyPlayer(id), KindOfPlayer.HUMANPLAYER);
	}
	
	@Override
	public Card getMove(CardsOnTable cardsOnTable)
	{
		List<Integer> possMosse = CardsUtils.getPossibiliMosse(getCardsInHand(), cardsOnTable.getCardsOnTable());
		//print phase
		if(!isPrinted) {
			
			// System.out.print("Carte giocabili: [");
			// for (int i = 0; i < possMosse.size() - 1; i++)
			//	System.out.print(new Card(possMosse.get(i)) + ", ");
			// System.out.println(new Card(possMosse.get(possMosse.size() - 1)) + "]");
			System.out.println("What card do you want to play?");
			isPrinted = true; //cosi non ristampo ogni volta che chiamo il metodo
		}
		
		if(Gdx.input.justTouched())
		{
			for(Card c : myCards.getCards())
			{
				if(c.isOverlapped(Gdx.input.getX(), Constants.WINDOW_HEIGHT-Gdx.input.getY()))
					if(possMosse.contains(c.toInt()))
					{	
						myCards.remove(c);
						isPrinted = false;
						return c;
					}
			}
		}
		
		return null;
	}
}

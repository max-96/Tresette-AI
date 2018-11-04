package it.ai.tresette.player;

import java.util.List;
import java.util.Scanner;

import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.CardsOnTable;
import util.CardsUtils;

public class HumanPlayer extends Player
{
	private Scanner keyboard = new Scanner(System.in);
	
	public static class DummyPlayer extends setting.Player
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
	
		System.out.print("Carte giocabili: [");
		for (int i = 0; i < possMosse.size() - 1; i++)
			System.out.print(new Card(possMosse.get(i)) + ", ");
		System.out.println(new Card(possMosse.get(possMosse.size() - 1)) + "]");

		int myint;
		do
		{
			System.out.println("Che carta giochi?");
			myint = keyboard.nextInt() - 1;
			
		} while (myint < 0 || myint >= possMosse.size());

		return this.myCards.remove(possMosse.get(myint));

		// TODO pick a cards of the available one and removes it from the cards
		// in hand
	}
}

package it.ai.tresette.player;

import java.util.List;
import java.util.Scanner;

import it.ai.tresette.GameManager;
import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.CardsOnTable;
import util.CardsUtils;

public class HumanPlayer extends Player
{
	private CardsOnTable carteInGioco;
	
	public HumanPlayer(int id)
	{
		super(id, KindOfPlayer.HUMANPLAYER);
	}
	
	@Override
	public Card getMove()
	{

		List<Integer> possMosse = CardsUtils.getPossibiliMosse(getCardsInHand(), carteInGioco.getCardsOnTable());
	
		System.out.print("Carte giocabili: [");
		for (int i = 0; i < possMosse.size() - 1; i++)
			System.out.print(new Card(possMosse.get(i)) + ", ");
		System.out.println(new Card(possMosse.get(possMosse.size() - 1)) + "]");

		int myint;
		try (Scanner keyboard = new Scanner(System.in))
		{
			do
			{
				System.out.println("che carta butti");
				myint = keyboard.nextInt() - 1;
				System.out.println(myint);

			} while (myint < 0 || myint >= possMosse.size());
		}

		return this.myCards.remove(possMosse.get(myint));

		// TODO pick a cards of the available one and removes it from the cards
		// in hand
	}

	@Override
	public void setInfo(GameManager game)
	{
		carteInGioco = game.getCardsOnTable();
	}
}

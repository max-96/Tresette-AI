package it.ai.tresette.player;

import java.util.List;
import java.util.Scanner;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.ai.tresette.GameManager;
import it.ai.tresette.GameManager.KindOfPlayer;
import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.CardsInHand;
import it.ai.tresette.objects.CardsOnTable;

public class HumanPlayer extends Player {

	public HumanPlayer(int id, CardsInHand cards) {
		super(id, cards);
		this.myKind = KindOfPlayer.HUMANPLAYER;
		
	}

	@Override
	public Card getMossa(int playerId,List<List<Integer>> assegnamentoCarte, CardsOnTable carteInGioco,
			double scoreMyTeam, double scoreOtherTeam) {
		
		
		List<Integer> possMosse;
		//le mosse possibili -> in un futuro questo andrà tradotto in qualcosa di piu potente, per evidenziare graficamente le carte che posso utilizzare
		if(!carteInGioco.isEmpty())
			possMosse = GameManager.possibiliMosse(this.getCardsInHand(), carteInGioco.getActualSuit()); //we use carteInGioco.get(0) because it should be the first card dropped on table
		else
			possMosse = this.getCardsInHand();
		////////
		
		int myint;
		Scanner keyboard = new Scanner(System.in);
		
		
		System.out.print("le possibile mosse: [");
		for(Integer i : possMosse)
			System.out.print(new Card(i).toString()+",");
		System.out.println("]");
		
		do {
			
			System.out.println("che carta butti");
			myint = keyboard.nextInt();
			
			}while((!possMosse.contains(myint)));
		
		return this.myCards.remove(myint);
		
		//TODO pick a cards of the available one and removes it from the cards in hand
		
		
	}
	

	@Override
	public Card getMossa() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void draw(SpriteBatch batch) {
		this.myCards.draw(batch);
		
	}
	
	
	

}

package setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import AI.Player;
import AI.PlayerAI;
import setting.Card.Suit;
import setting.Card.Value;

public class Game
{
	
	/**
	 * list of the cards of each player.
	 * assCarte[i] returns the list of cards of player i
	 */
	private List<Card>[] assCarte = new LinkedList[4];
	/**
	 * list of players active in the current game
	 */
	private Player[] players = new PlayerAI[4];
	/**
	 * Cards currently in game
	 */
	private Set<Card> carteInGioco = new HashSet<>();
	/**
	 * Point counter for each team
	 */
	private int[] punteggi = {0, 0};
	/**
	 * Suit active for each Player
	 * semiAttivi[i] returns the list of suits that the player i can play.
	 */
	private List<Card.Suit>[] semiAttivi = new LinkedList[4]; // player x semi
	 /**
	  * startingPlayer is the player that in this turn drop the card first.
	  */
	private int startingPlayer;
	/**
	 * actual gameState
	 */
	private GameState gameState;
	public Game() {
		initialise();
	}

	/**
	 * esegue il gioco 
	 */
	public void run()
	{	
		//counter of game hands
		int turno = 1;
		while(true) {
			switch(this.gameState) {
			case GAMEREADY: 
				this.gameState = this.gameState.INGOING;
				System.out.println("We are starting the game");
				break;
			case INGOING:
				System.out.println("Starting turn number "+turno);
				playHand();
				turno++;
				if(turno>10)
					gameState = GameState.GAMEEND;
				break;
				
				
			case GAMEEND: 
				System.out.println("Now let's check who's the winner!");
				if(punteggi[0]>punteggi[1])
					System.out.println("The winner is team 1 with "+punteggi[0]+ " points!");
				else if(punteggi[1]>punteggi[0])
					System.out.println("The winnes is team 2 with "+punteggi[1]+ " points!");
				else 
					System.out.println("Incredible! draw game!");
				return;

				}
		}
			
	}
	
	
	/**
	 * this method restart the game
	 */
	public void restart()
	{
		initialise();
	}
	
	/**
	 * this method plays a full hand of tresette, handling correctly the player's turn
	 */
	private void playHand()
	{
		/**
		 * Map of card,integer that specifies the card used by each player, where Card,0 indicates the card used by player 0
		 */
		Map<Card,Integer> cardsOnTable = new HashMap<Card,Integer>();
		System.out.println("The player "+startingPlayer+" starts.");
		int nextPlayer = startingPlayer;													
		int dominatingPlayer = nextPlayer;														
		Card dominatingCard = null;																
		int points = 0;																			
		do
		{
			Card temp = players[nextPlayer].getMossa();
			//se e' la prima carta buttata allora e' sia palo sia cartadominante
			if(startingPlayer == nextPlayer)
				dominatingCard = temp;
			else    //altrimenti controlliamo se la carta appena buttata diventa la dominante
				if(temp.betterThan(dominatingCard))
				{
					dominatingCard = temp;
					dominatingPlayer = nextPlayer;
				}
			
			points += temp.getPunti();				//aggiorno i punti
			cardsOnTable.put(temp, nextPlayer);		//aggiungo la carta al "tavolo"
			nextPlayer = (nextPlayer - 1) % 4;		//il prossimo giocatore e' quello alla mia sinistra
			
		}while(nextPlayer!=startingPlayer);
		
		
		if(dominatingPlayer % 2 == 0)	//se i punti vanno alla squadra 1
			punteggi[0] += points;
		else							//altrimenti vanno alla squadra 2
			punteggi[1] += points;
		System.out.println("The team "+ (dominatingPlayer%2 +1)+ " has scored "+points+" points." );
		startingPlayer = dominatingPlayer;
		return;
	}
	
	
	public Set<Card> getExCards() {
		return new HashSet<>(carteInGioco);
	}

	public List<Card.Suit>[] getSemiAttivi() {
		List<Card.Suit>[] temp = new LinkedList[4];
		for(int i=0; i<4; i++)
			Collections.copy(temp[i], semiAttivi[i]);
		return temp;
	}

	/**
	 * Distribuisce le carte e inizializza i player
	 */
	private void initialise()
	{
		List<Integer> temp = new LinkedList<>();
		for(int i=0; i<40; i++)
			temp.add(i);
		
		Collections.shuffle(temp);
		Card fourDenari = new Card(Suit.DENARI,Value.QUATTRO);
		for(int i=0; i<4; i++)
		{
			List<Card> carteInMano = new LinkedList<>(); 
			
			for(int j= 0; j<10; j++)
				carteInMano.add(new Card(temp.remove(0)));
			
			
			assCarte[i] = carteInMano;
			semiAttivi[i] = Arrays.asList(Card.Suit.values());
			
			players[i] = new PlayerAI(i, carteInMano, this);
			//se questo giocatore possiede il 4 di denari, allora iniziera la mano
			if(carteInMano.contains(fourDenari))
				startingPlayer = i;
		}
		this.gameState = GameState.GAMEREADY;
	}
	
	private enum GameState { GAMEREADY,INGOING,GAMEEND }
}

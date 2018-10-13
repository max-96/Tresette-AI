package setting;


import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import AI.DumbPlayerAI;
import AI.Player;
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
	private Player[] players = new Player[4];
	
	/**
	 * Cards currently in game
	 */
	private Set<Card> carteInGioco = new HashSet<>();
	
	/**
	 * Cards that are used during the game
	 */
	private Set<Card> exCards = new HashSet<>();
	/**
	 * Point counter for each team
	 */
	private float[] punteggi = {0, 0};
	
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
	
	/**
	 * Map of integer,card that specifies the card used by each player, where 0,Card indicates the card used by player 0
	*/
	Map<Integer,Card> cardsOnTable = new HashMap<Integer,Card>();
	
	private int[] nrCardsInHand = {10,10,10,10};
	
	private enum GameState { GAMEREADY,INGOING,GAMEEND }
	
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
				this.gameState = GameState.INGOING;
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
		
		System.out.println("The player "+startingPlayer+" starts.");
		int nextPlayer = startingPlayer;													
		int dominatingPlayer = nextPlayer;														
		Card dominatingCard = null;																
		float points = 0;																			
		do
		{
			
			Card temp = players[nextPlayer].getMossa();
			//se e' la prima carta buttata allora e' sia palo sia cartadominante
			if(startingPlayer == nextPlayer)
				dominatingCard = temp;
			else    //altrimenti controlliamo se la carta appena buttata diventa la dominante
				if(temp.compareTo(dominatingCard)>0)
				{
					dominatingCard = temp;
					dominatingPlayer = nextPlayer;
				}
			
			points += temp.getPunti();				//aggiorno i punti
			cardsOnTable.put(nextPlayer,temp);		//aggiungo la carta al "tavolo"
			carteInGioco.remove(temp); 				//la rimuovo dalle carte in "gioco"
			exCards.add(temp);						//la aggiungo alle carte esplorate
			nrCardsInHand[nextPlayer]--;
			nextPlayer = Math.floorMod(nextPlayer - 1, 4);	//il prossimo giocatore e' quello alla mia sinistra
			
		}while(nextPlayer!=startingPlayer);
		
		cardsOnTable.clear();
		
		if(dominatingPlayer % 2 == 0)	//se i punti vanno alla squadra 1
			punteggi[0] += points;
		else							//altrimenti vanno alla squadra 2
			punteggi[1] += points;
		System.out.println("The team "+ (dominatingPlayer%2 +1)+ " has scored "+points+" points." );
		startingPlayer = dominatingPlayer;
		return;
	}
	
	
	public Set<Card> getExCards() {
		return new HashSet<>(exCards);
	}
	
	/**
	 * this method returns the set of card that can still be used
	 * @return
	 */
	public Set<Card> getCardsInGame(){
		return new HashSet<>(carteInGioco);
	}
	
	/**
	 * this method return the map of the cards on table
	 * @return
	 */
	public Map<Integer,Card> getCardsOnTable()
	{
		return cardsOnTable;
	}
	
	/**
	 * this method return the Card that your mate just throw on the table
	 * @param player is the actual player who wants to check his mate card on the table. the mate index is calculated by doing player+2%4
	 * @return
	 */
	public Card getMateCard(int player)
	{
		return cardsOnTable.get((player+2)%4);
	}
	
	/**
	 * this method returns the number of cards that the player "player" has in his hand.
	 * @param player the player you want to know the number of cards
	 * @return	the number of cards in the hand of that player
	 */
	public int getNumberOfCardsInHand(int player)
	{
		return nrCardsInHand[player];
	}
	
	/**
	 * this method returns an array containing the number of cards in hand for each player
	 * @param player
	 * @return
	 */
	public int[] getAllCardsInHand()
	{
		return Arrays.copyOf(nrCardsInHand, nrCardsInHand.length);
	}
	
	public List<Card.Suit>[] getSemiAttivi() {
		List<Card.Suit>[] temp = (List<Card.Suit>[]) new LinkedList[4];
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
			
			carteInGioco.addAll(carteInMano);				//adding these cards to the card "in game"
			assCarte[i] = carteInMano;
//			semiAttivi[i] = Arrays.asList(Card.Suit.values());
			nrCardsInHand[i] = 10;
			players[i] = new DumbPlayerAI(i, carteInMano, this);
			//se questo giocatore possiede il 4 di denari, allora iniziera la mano
			if(carteInMano.contains(fourDenari))
				startingPlayer = i;
		}
		this.gameState = GameState.GAMEREADY;
	}
	
	
}

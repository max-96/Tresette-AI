package it.ai.tresette;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.Card.Suit;
import it.ai.tresette.objects.Card.Val;
import it.ai.tresette.objects.CardsInHand;
import it.ai.tresette.objects.CardsOnTable;
import it.ai.tresette.player.HumanPlayer;
import it.ai.tresette.player.Player;






public class GameManager {

	/**
	 * list of the cards of each player. assCarte[i] returns the list of cards of
	 * player i
	 */
	private List<Card>[] assCarte = new LinkedList[4];

	/**
	 * list of players active in the current game
	 */
	private Player[] players = new Player[4];

	/**
	 * Cards currently in game
	 */
	private Set<Card> carteInGioco = new HashSet<Card>();

	/**
	 * Cards that are used during the game
	 */
	private Set<Card> exCards = new HashSet<Card>();
	/**
	 * Point counter for each team
	 */
	private float[] punteggi = { 0, 0 };

	/**
	 * Suit active for each Player semiAttivi[i] returns the list of suits that the
	 * player i can play.
	 */
	//private List<Card.Suit>[] semiAttivi = new LinkedList[4]; // player x semi

	//
	// tutti i campi utilizzati per la gestione della "mano"
	//
	
	/**
	 * startingPlayer is the player that in this turn drop the card first.
	 */
	private int startingPlayer;

	/**
	 * the player who's going to play the next hand
	 */
	private int actualPlayer;
	
	/**
	 * the actual card that's dominating in this actual "hand"
	 */
	private Card dominatingCard;
	
	/**
	 * the index of the players who's actually dominating
	 */
	private int dominatingPlayer;
	
	/**
	 * the points on the table in this "hand";
	 */
	private double points;
	
	private int turno;
	
//	/**
//	 * Map of integer,card that specifies the card used by each player, where 0,Card
//	 * indicates the card used by player 0
//	 */
//	private Map<Integer, Card> cardsOnTable = new HashMap<Integer, Card>();
	
	private TreSette game;
	/**
	 * the cards on table object relative to this Game "table"
	 */
	private CardsOnTable cardsOnTable;
	
	/**
	 * The actual state of the game
	 */
	private GameState gameState;
	
	/**
	 * The actual state of the hand
	 */
	private HandState handState;
	
	/**
	 * the actual kind of player who's playing his hand
	 */
	private KindOfPlayer playerKind;
	
	private Info info;
	
	public static class Info
	{
		private float[] scores = { 0, 0 };
		private int startingPlayer;
		private List<List<Integer>> accusi;
		private List<List<Card.Suit>> semiAttivi;
		private List<Integer> seenCards = new ArrayList<>();
		private List<Integer> cardsOnTable = new ArrayList<>();

		private Info() {}
		
		public double getTeamScore(int i)
		{
			assert i >= 0 && i < 2;
			return scores[i];
		}
		
		public List<Integer> getAccusiOfPlayer(int player)
		{
			assert  player >= 0 && player < 4;
			return Collections.unmodifiableList(accusi.get(player));
		}
		
		public List<Integer> getSeenCards()
		{
			return Collections.unmodifiableList(seenCards);
		}

		public List<Integer> getKnownCardsOfPlayer(int player)
		{
			assert  player >= 0 && player < 4;
			
			List<Integer> l = new ArrayList<>();	
			if (startingPlayer == player)
				l.add(new Card(Suit.DENARI,Val.QUATTRO).toInt());
			l.addAll(accusi.get(player));
			
			return l;
		}
		
		
		/**
		 * 	Questo metodo e' stato creato per poter chiamare getMossa() con i giusti parametri in input. POTREBBE QUINDI DOVER ESSERE CAMBIATA PIU TARDI
		 * @return
		 */
		public List<List<Integer>> getAllKnownCardsOfPlayers()
		{
			/**
			List<List<Integer>> res = new ArrayList<>();
			for(int i=0;i<4;i++)
				res.add(getKnownCardsOfPlayer(i));
			return res;
			*/
			return null;
		}

		public List<Integer> getCardsOnTable()
		{
			return Collections.unmodifiableList(cardsOnTable);
		}
	}
	
	private enum GameState
	{
		GAMEREADY, INGOING, GAMEEND
	}
	
	private enum HandState
	{
		INITIALISINGHAND, INGOINGHAND, CONCLUDINGHAND
	}
	
	public enum KindOfPlayer
	{
		HUMANPLAYER, AIPLAYER
	}
	
	public GameManager()
	{
		initialise();
	}
	
	public void run()
	{
		
			switch (this.gameState)
			{
				case GAMEREADY:
					this.gameState = gameState.INGOING;
					System.out.println("We are starting the game");
					break;
				case INGOING:
					System.out.println("playing turn number " + turno);
					handManager();
					if(this.handState == HandState.INITIALISINGHAND)
						turno++;
					if (turno > 10)
						gameState = gameState.GAMEEND;
					break;
	
				case GAMEEND:
					System.out.println("Now let's check who's the winner!");
					if (punteggi[0] > punteggi[1])
						System.out.println("The winner is team 1 with " + punteggi[0] + " points!");
					else
						System.out.println("The winnes is team 2 with " + punteggi[1] + " points!");
					return;
			}
		
			
			}
	
	
	
	/**
	 * 	this method manage the lifecycle of a tresette's hand
	 */
	public void handManager()
	{
		switch(this.handState)
		{
			case INITIALISINGHAND:
				initialiseHand();
				break;
			case INGOINGHAND:
				turnManager();
				break;
			case CONCLUDINGHAND:
				concludeHand();
				break;
			default:
				break; 
		}
	}
	
	private void concludeHand()
	{
		if (dominatingPlayer % 2 == 0) 												// se i punti vanno alla squadra 1
			punteggi[0] += points;
		else 																		// altrimenti vanno alla squadra 2
			punteggi[1] += points;
		System.out.println("The team " + (dominatingPlayer % 2 + 1) + " has scored " + points + " points.");
		startingPlayer = dominatingPlayer;
		
		cardsOnTable.reset();														//elimino le carte sul tavolo
		
																					//incremento il contatore del turno
		
		this.handState = HandState.INITIALISINGHAND;
		return;
		
	}
	
	private void initialiseHand()
	{
		this.playerKind = players[startingPlayer].getKind();
		points = 0;
		actualPlayer = startingPlayer;
		this.handState = HandState.INGOINGHAND;
	}
	
	private void turnManager()
	{
		switch(this.playerKind)
		{
			case AIPLAYER:
				AIturn();
				break;
			case HUMANPLAYER:
				humanTurn();
				break;
			default:
				break;
		
		}
	}

	
	private void AIturn()
	{
		//Chiediamo la mossa al player artificiale
		Card temp = players[actualPlayer].getMossa(actualPlayer,this.info.getAllKnownCardsOfPlayers(),cardsOnTable,this.punteggi[actualPlayer%2],this.punteggi[(actualPlayer + 1)%2]);
	
		turnPlayed(temp);
		
		
	}
	
	/**
	 * this methods handle a humanTurn; It is different from AIturn cause this methods avoids the game to freeze waiting for the user input
	 */
	private void humanTurn()
	{
		//TODO e' temporaneo, ad ora il gioco frezza mentre aspetta la mossa del player, da cambiare 
		Card temp = players[actualPlayer].getMossa(actualPlayer,this.info.getAllKnownCardsOfPlayers(),cardsOnTable,this.punteggi[actualPlayer%2],this.punteggi[(actualPlayer + 1)%2]);
	
		turnPlayed(temp); 
	}
	
	private void turnPlayed(Card card)
	{
		if (startingPlayer == actualPlayer)										//se e la prima carta buttata, allora è dominante
			dominatingCard = card;
		else 																	// altrimenti controlliamo se la carta appena buttata diventa la dominante
			if (card.compareTo(dominatingCard) > 0)
			{
				dominatingCard = card;
				dominatingPlayer = actualPlayer;
			}
	
	
		points += card.getPoints(); 											// aggiorno i punti
		cardsOnTable.add(card); 												// aggiungo la carta al "tavolo"
		carteInGioco.remove(card); 												// la rimuovo dalle carte in "gioco"
		exCards.add(card); 														// la aggiungo alle carte esplorate
		actualPlayer = Math.floorMod(actualPlayer - 1, 4); 						// il prossimo giocatore e' quello alla mia sinistra
		if(actualPlayer == startingPlayer)										// se ha giocato l'ultimo giocatore, entro nella fase conclusiva della mano
			this.handState = HandState.CONCLUDINGHAND;
		else																	//altrimenti devo stabilire se il prossimo player e umano
		{
			this.playerKind = players[actualPlayer].getKind();
			
		}
	}
	
	private void initialise()
	{
		
		//tutte le carte rappresentate con interi da 0 a 39
		List<Integer> temp = new LinkedList<Integer>();
		for (int i = 0; i < 40; i++)
			temp.add(i);

		Collections.shuffle(temp);
		Card fourDenari = new Card(Suit.DENARI, Val.QUATTRO);
		
		//estraggo e creo 4 players, dotati di 10 carte ognuno, prese dal mazzo
		//per ora vengono generati solo players umani
		for (int i = 0; i < 4; i++)
		{
			List<Card> carteInMano = new LinkedList<>();
			for (int j = 0; j < 10; j++)
				carteInMano.add(new Card(temp.remove(0)));
			
			carteInGioco.addAll(carteInMano); // adding these cards to the card "in game"
			assCarte[i] = carteInMano;
			
			// semiAttivi[i] = Arrays.asList(Card.Suit.values());
			
			
			players[i] = new HumanPlayer(i, new CardsInHand(carteInMano,i));
			// se questo giocatore possiede il 4 di denari, allora iniziera la mano
			if (carteInMano.contains(fourDenari))
				startingPlayer = i;
			
		}
		this.gameState = GameState.GAMEREADY;
		this.turno = 1;
		this.cardsOnTable = new CardsOnTable();
		this.info = new GameManager.Info();
		this.handState = HandState.INITIALISINGHAND;
	}

	
	public void draw(SpriteBatch batch)
	{
		for(int i = 0;i<4;i++)
			players[i].draw(batch);
	}
	
	public static List<Integer> possibiliMosse(List<Integer> carte, int semeAttuale)
	{
		List<Integer> mosse = new ArrayList<>();
		for (Integer c : carte)
		{
			if (c / 10 == semeAttuale)
				mosse.add(c);
		}

		if (mosse.isEmpty())
			mosse.addAll(carte);

		return mosse;
	}
	
	
	public static void main(String[] args) {
		
		GameManager test = new GameManager();
	
		test.run();
	}
}

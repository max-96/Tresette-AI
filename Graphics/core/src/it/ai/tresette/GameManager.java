package it.ai.tresette;

import static util.CardsUtils.findAccusiOfPlayer;
import static util.CardsUtils.getTeam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.ai.tresette.objects.Card;
import it.ai.tresette.objects.Card.Suit;
import it.ai.tresette.objects.Card.Val;
import it.ai.tresette.objects.CardsOnTable;
import it.ai.tresette.player.Player;
import setting.Game;

public class GameManager {

	/**
	 * list of the cards of each player. assCarte.get(i) returns the list of cards of
	 * player i
	 */
	private List<List<Card>> assCarte = new ArrayList<>(4);

	/**
	 * list of players active in the current game
	 */
	private Player[] players = new Player[4];

//	/**
//	 * Cards currently in game
//	 */
//	private Set<Card> carteInGioco = new HashSet<Card>();
//
//	/**
//	 * Cards that are used during the game
//	 */
//	private Set<Card> exCards = new HashSet<Card>();
	/**
	 * Point counter for each team
	 */
	private double[] punteggi = { 0, 0 };

	/**
	 * Suit active for each Player
	 */
	private List<List<Integer>> semiAttivi = new ArrayList<>();
	
	/**
	 * Accusi for each player
	 */
	private List<List<Integer>> accusi = new ArrayList<>();

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
	
	/**
	 * The current turn
	 */
	private int turno;
	
//	/**
//	 * Map of integer,card that specifies the card used by each player, where 0,Card
//	 * indicates the card used by player 0
//	 */
//	private Map<Integer, Card> cardsOnTable = new HashMap<Integer, Card>();
//	
//	private TreSette game;
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
	
	private Game game;
	
	/**
	 * the actual kind of player who's playing his hand
	 */
	private KindOfPlayer playerKind;
		
	private enum GameState
	{
		GAMEREADY, INGOING, GAMEEND
	}
	
	private enum HandState
	{
		INITIALISINGHAND, INGOINGHAND, CONCLUDINGHAND, SHOWINGFOURTHCARD,
	}
	
	public enum KindOfPlayer
	{
		HUMANPLAYER, AIPLAYER
	}
	
	public GameManager(Player... players)
	{
		assert players.length == 4;
		this.players = players;
		
		initialise();
	}
	
	public CardsOnTable getCardsOnTable()
	{
		return this.cardsOnTable;
	}
	
	public Game getGameInfo()
	{
		return this.game;
	}
	
	public void run()
	{		
		switch (this.gameState)
		{
			case GAMEREADY:
				this.gameState = GameState.INGOING;
				System.out.println("We are starting the game");
				break;
				
			case INGOING:
				System.out.println("Playing turn number " + turno);
				handManager();
				if(this.handState == HandState.INITIALISINGHAND)
					turno++;
				if (turno > 10)
					gameState = GameState.GAMEEND;
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
			case SHOWINGFOURTHCARD:
				showFourthCard();
				break;
			default:
				break; 
		}
	}
	
	private void showFourthCard() {
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		this.handState = HandState.CONCLUDINGHAND;
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
		game.updateScores(punteggi);
		
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
		if (turno == 1)
		{
			List<Integer> accusi = this.accusi.get(actualPlayer);
			punteggi[getTeam(actualPlayer)] += findAccusiOfPlayer(game.getAssegnamentoCarte().get(actualPlayer), accusi);
		}
		
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
		Card temp = players[actualPlayer].getMove();
		turnPlayed(temp);
	}
	
	/**
	 * this methods handle a humanTurn; It is different from AIturn cause this methods avoids the game to freeze waiting for the user input
	 */
	private void humanTurn()
	{
		//TODO e' temporaneo, ad ora il gioco frezza mentre aspetta la mossa del player, da cambiare 
		Card temp = players[actualPlayer].getMove();
		turnPlayed(temp); 
	}
	
	private void turnPlayed(Card card)
	{
		if (startingPlayer == actualPlayer)										//se e la prima carta buttata, allora e' dominante
			dominatingCard = card;
		else 																	// altrimenti controlliamo se la carta appena buttata diventa la dominante
			if (card.compareTo(dominatingCard) > 0)
			{
				dominatingCard = card;
				dominatingPlayer = actualPlayer;
			}
		
		if (card.getIntSuit() != dominatingCard.getIntSuit())
			semiAttivi.get(actualPlayer).remove((Integer) dominatingCard.getIntSuit());
	
		points += card.getPoints(); 											// aggiorno i punti
		cardsOnTable.add(card); 												// aggiungo la carta al "tavolo"
//		carteInGioco.remove(card); 												// la rimuovo dalle carte in "gioco"
//		exCards.add(card); 														// la aggiungo alle carte esplorate
		assCarte.get(actualPlayer).remove(card);
		actualPlayer = Math.floorMod(actualPlayer - 1, 4); 						// il prossimo giocatore e' quello alla mia sinistra
		if(actualPlayer == startingPlayer)										// se ha giocato l'ultimo giocatore, entro nella fase conclusiva della mano
			this.handState = HandState.SHOWINGFOURTHCARD;
		else																	//altrimenti devo stabilire se il prossimo player e umano
			this.playerKind = players[actualPlayer].getKind();
		
		game.updateInfo(card.toInt(), startingPlayer, cardsOnTable.getCardsOnTable(), accusi, semiAttivi);
	}
	
	private void initialise()
	{
		this.gameState = GameState.GAMEREADY;
		this.turno = 1;
		this.cardsOnTable = new CardsOnTable();
		this.handState = HandState.INITIALISINGHAND;
		
		game.initialise();
		//tutte le carte rappresentate con interi da 0 a 39
		List<List<Integer>> deck = game.getAssegnamentoCarte();

		Card fourDenari = new Card(Suit.DENARI, Val.QUATTRO);
		
		//estraggo 4 players, dotati di 10 carte ognuno, prese dal mazzo
		for (int i = 0; i < 4; i++)
		{
			List<Card> carteInMano = new ArrayList<>();
			for (int j = 0; j < 10; j++)
				carteInMano.add(new Card(deck.get(i).get(j)));
			
//			carteInGioco.addAll(carteInMano); // adding these cards to the card "in game"
			players[i].setCardsInHand(carteInMano);
			players[i].setInfo(this);
			
			assCarte.add(carteInMano);
			
			this.accusi.add(new ArrayList<>());
			this.semiAttivi.add(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));
			
			// se questo giocatore possiede il 4 di denari, allora iniziera la mano
			if (carteInMano.contains(fourDenari))
				startingPlayer = i;
		}
	}

	public void draw(SpriteBatch batch)
	{
		for(int i = 0;i<4;i++)
			players[i].draw(batch);
		this.cardsOnTable.draw(batch);
	}
	
//	public static List<Integer> possibiliMosse(List<Integer> carte, int semeAttuale)
//	{
//		List<Integer> mosse = new ArrayList<>();
//		for (Integer c : carte)
//		{
//			if (c / 10 == semeAttuale)
//				mosse.add(c);
//		}
//
//		if (mosse.isEmpty())
//			mosse.addAll(carte);
//
//		return mosse;
//	}
	
	public static void main(String[] args) {
		
		GameManager test = new GameManager();
	
		test.run();
	}
}

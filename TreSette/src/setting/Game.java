package setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import util.CardsUtils;

public class Game
{
	private Player[] players;
	private List<List<Card>> assCarte= new ArrayList<>(4);;
	private Info info = new Info();
	private Status status;
	
	private enum Status
	{
		GAMEREADY, INGOING, GAMEEND
	}

	public static class Info
	{
		private float[] scores = { 0, 0 };
		private int startingPlayer;
		private List<List<Integer>> accusi;
		private List<List<Card.Suit>> semiAttivi;
		private List<Integer> availableCards = new ArrayList<>();
		private List<Integer> cardsOnTable = new ArrayList<>();
		private int[] numeroCarteInMano = {10, 10, 10, 10};

		private Info() {}
		
		public double getTeamScore(int player)
		{
			assert player >= 0 && player < 4;
			return scores[player % 2];
		}
		
		public double getOpponentScore(int player)
		{
			assert player >= 0 && player < 4;
			return scores[(player + 1) % 2];
		}
		
		public List<Integer> getAvailableCards()
		{
			return new ArrayList<>(availableCards);
		}

		public List<Integer> getCardsOnTable()
		{
			return Collections.unmodifiableList(cardsOnTable);
		}

//		public List<Integer> getAccusiOfPlayer(int player)
//		{
//			assert  player >= 0 && player < 4;
//			return Collections.unmodifiableList(accusi.get(player));
//		}

		public List<Integer> getKnownCardsOfPlayer(int player)
		{
			assert  player >= 0 && player < 4;
			
			List<Integer> l = new ArrayList<>();	
			if (startingPlayer == player)
				l.add(CardsUtils.QUATTRODIDENARI);
			l.addAll(accusi.get(player));
			
			return l;
		}
		
		public boolean isSemeAttivoForPlayer(int player, int suit)
		{
			return semiAttivi.get(player).contains(Card.intToSuit[suit]);
		}
		
		public int getNumeroCarteInMano(int player)
		{
			return numeroCarteInMano[player];
		}
	}
	
	public Game(Player... players)
	{
		assert players.length == 4;
		this.players = players;
		for (Player p : players)
			p.setGame(this);
		initialise();
	}
	
	private void initialise()
	{
		List<Integer> deck = new ArrayList<>(40);
		for (int i = 0; i < 40; i++)
			deck.add(i);
		info.availableCards = new ArrayList<>(deck);
		
		Collections.shuffle(deck);
		
		for (int i = 0; i < 4; i++)
		{
			List<Card> carteInMano = new ArrayList<>();
			
			for (int j = 0; j < 10; j++)
				carteInMano.add(new Card(deck.remove(0)));

			players[i].setCards(new ArrayList<>(carteInMano));
			assCarte.add(carteInMano);
			info.semiAttivi.add(Arrays.asList(Card.Suit.values()));
			//TODO inizializzare accusi
			
			if (carteInMano.contains(new Card(CardsUtils.QUATTRODIDENARI)))
				info.startingPlayer = i;
		}
		
		status = Status.GAMEREADY;
	}

	public Info getInfo()
	{
		return info;
	}
	
	public void run()
	{
//		int turno = 1;
//		while (true)
//			switch (this.status)
//			{
//				case GAMEREADY:
//					this.status = Status.INGOING;
//					System.out.println("We are starting the game");
//					break;
//				case INGOING:
//					System.out.println("Starting turn number " + turno);
//					playHand();
//					turno++;
//					if (turno > 10)
//						status = Status.GAMEEND;
//					break;
//	
//				case GAMEEND:
//					System.out.println("Now let's check who's the winner!");
//					if (punteggi[0] > punteggi[1])
//						System.out.println("The winner is team 1 with " + punteggi[0] + " points!");
//					else
//						System.out.println("The winnes is team 2 with " + punteggi[1] + " points!");
//					return;
//			}

	}

	public void restart()
	{
		initialise();
	}

	private void playHand()
	{
//		System.out.println("The player " + startingPlayer + " starts.");
//		int nextPlayer = startingPlayer;
//		int dominatingPlayer = nextPlayer;
//		Card dominatingCard = null;
//		float points = 0;
//		do
//		{
//
//			Card temp = players[nextPlayer].getMossa();
//			// se e' la prima carta buttata allora e' sia palo sia cartadominante
//			if (startingPlayer == nextPlayer)
//				dominatingCard = temp;
//			else // altrimenti controlliamo se la carta appena buttata diventa la dominante
//			if (temp.compareTo(dominatingCard) > 0)
//			{
//				dominatingCard = temp;
//				dominatingPlayer = nextPlayer;
//			}
//
//			points += temp.getPunti(); // aggiorno i punti
//			cardsOnTable.put(nextPlayer, temp); // aggiungo la carta al "tavolo"
//			carteInGioco.remove(temp); // la rimuovo dalle carte in "gioco"
//			exCards.add(temp); // la aggiungo alle carte esplorate
//			nrCardsInHand[nextPlayer]--;
//			nextPlayer = Math.floorMod(nextPlayer - 1, 4); // il prossimo giocatore e' quello alla mia sinistra
//
//		} while (nextPlayer != startingPlayer);
//
//		cardsOnTable.clear();
//
//		if (dominatingPlayer % 2 == 0) // se i punti vanno alla squadra 1
//			punteggi[0] += points;
//		else // altrimenti vanno alla squadra 2
//			punteggi[1] += points;
//		System.out.println("The team " + (dominatingPlayer % 2 + 1) + " has scored " + points + " points.");
//		startingPlayer = dominatingPlayer;
		return;
	}
}

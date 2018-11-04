package setting;

import static util.CardsUtils.EPS;
import static util.CardsUtils.FindFourOfDenari;
import static util.CardsUtils.QUATTRODIDENARI;
import static util.CardsUtils.WINNING_SCORE;
import static util.CardsUtils.findAccusiOfPlayer;
import static util.CardsUtils.getCardSuit;
import static util.CardsUtils.getDominantPlayer;
import static util.CardsUtils.getPointsOfCards;
import static util.CardsUtils.getTeam;
import static util.CardsUtils.isSameSuit;
import static util.CardsUtils.nextPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Game
{
	private Player[] players;
	private List<List<Integer>> assCarte;
	private Info info;
	private double[] totalScores = { 0, 0 };

	public static class Info
	{
		private double[] scores = { 0, 0 };
		private int startingPlayer;
		private List<List<Integer>> accusi = new ArrayList<>();
		private List<List<Integer>> semiAttivi = new ArrayList<>();
		private List<Integer> availableCards = new ArrayList<>();
		private List<Integer> cardsOnTable = new ArrayList<>();
		private int[] numeroCarteInMano = { 10, 10, 10, 10 };

		private Info() {}
		
		public int getTurn()
		{
			return cardsOnTable.size();
		}

		public double[] getScores()
		{
			return Arrays.copyOf(scores, 2);
		}

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

		// public List<Integer> getAccusiOfPlayer(int player)
		// {
		// assert player >= 0 && player < 4;
		// return Collections.unmodifiableList(accusi.get(player));
		// }

		public List<Integer> getKnownCardsOfPlayer(int player)
		{
			assert player >= 0 && player < 4;

			List<Integer> l = new ArrayList<>();
			if (startingPlayer == player)
				l.add(QUATTRODIDENARI);
			l.addAll(accusi.get(player));

			return l;
		}

		public boolean isSemeAttivoForPlayer(int player, int suit)
		{
			return semiAttivi.get(player).contains(suit);
		}

		public int getNumeroCarteInMano(int player)
		{
			return numeroCarteInMano[player];
		}

		public byte[] getNumeroCarteInMano()
		{
			return new byte[] { (byte) numeroCarteInMano[0], (byte) numeroCarteInMano[1], (byte) numeroCarteInMano[2],
					(byte) numeroCarteInMano[3] };
		}
	}

	public Game(Player... players)
	{
		assert players.length == 4;
		this.players = players;
		for (Player p : players)
			p.setGame(this);
	}

	public void initialise()
	{
		info = new Info();
		
		List<Integer> deck = new ArrayList<>(40);
		for (int i = 0; i < 40; i++)
			deck.add(i);
		info.availableCards = new ArrayList<>(deck);

		Collections.shuffle(deck);
		assCarte = new ArrayList<>();

		for (int i = 0; i < 4; i++)
		{
			List<Integer> carteInMano = new ArrayList<>();

			for (int j = 0; j < 10; j++)
				carteInMano.add(deck.remove(0));

			players[i].setCards(new ArrayList<>(carteInMano));
			assCarte.add(carteInMano);
			info.accusi.add(new ArrayList<>());
			info.semiAttivi.add(new ArrayList<>(Arrays.asList(0, 1, 2, 3)));
		}

		info.startingPlayer = FindFourOfDenari(assCarte);
	}

	public void updateInfo(Integer card, int startingPlayer, List<Integer> cardsOnTable,
			List<List<Integer>> accusi, List<List<Integer>> semiAttivi)
	{
		for (int p = 0; p < 4; p++)
			if (assCarte.get(p).remove(card))
				info.numeroCarteInMano[p]--;
				
		info.startingPlayer = startingPlayer;
		info.accusi = accusi;
		info.semiAttivi = semiAttivi;
		info.availableCards.remove(card);
		info.cardsOnTable = cardsOnTable;
	}
	
	public void updateScores(double[] scores)
	{
		info.scores = scores;
	}
	
	public Info getInfo()
	{
		return info;
	}

	public List<List<Integer>> getAssegnamentoCarte()
	{
		return assCarte;
	}

	public int run()
	{
		while ((totalScores[0] < WINNING_SCORE && totalScores[1] < WINNING_SCORE) || totalScores[0] == totalScores[1])
		{
			initialise();
			int currentPlayer = info.startingPlayer;
			for (int round = 0; round < 10; round++)
			{
				int firstCard = 0;
				for (int p = 0; p < 4; p++)
				{
					assert currentPlayer >= 0 && currentPlayer < 4 : currentPlayer;

					List<Integer> carteInMano = assCarte.get(currentPlayer);

					if (round == 0)
					{
						List<Integer> accusi = info.accusi.get(currentPlayer);
						info.scores[getTeam(currentPlayer)] += findAccusiOfPlayer(carteInMano, accusi);
					}

					Integer move = players[currentPlayer].getMove();

					carteInMano.remove(move);
					info.availableCards.remove(move);
					info.numeroCarteInMano[currentPlayer]--;

					info.cardsOnTable.add(move);

					if (p == 0)
						firstCard = move;
					else if (!isSameSuit(move, firstCard))
						info.semiAttivi.get(currentPlayer).remove((Integer) getCardSuit(firstCard));

					currentPlayer = nextPlayer(currentPlayer);
				}

				int winningPlayer = getDominantPlayer(info.cardsOnTable, currentPlayer);
				info.scores[getTeam(winningPlayer)] += getPointsOfCards(info.cardsOnTable);

				currentPlayer = winningPlayer;
				info.cardsOnTable.clear();

				if (round == 9)
				{
					info.scores[getTeam(winningPlayer)]++;
					info.scores[0] = Math.floor(info.scores[0] + EPS);
					info.scores[1] = Math.floor(info.scores[1] + EPS);
				}
			}
			totalScores[0] += info.scores[0];
			totalScores[1] += info.scores[1];
		}

		return totalScores[0] > totalScores[1] ? 0 : 1;
	}
}

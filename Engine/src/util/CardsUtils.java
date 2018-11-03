package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class CardsUtils
{
	public static final boolean STATS_COLLECT = false; // TODO Questo e' un setting

	private final static double[] puntiPerCarta = { 1, 1.0 / 3, 1.0 / 3, 0, 0, 0, 0, 1.0 / 3, 1.0 / 3, 1.0 / 3 };
	private final static int[] dominioPerCarta = { 7, 8, 9, 0, 1, 2, 3, 4, 5, 6 };

	public static final int QUATTRODIDENARI = 3;

	/**
	 * Finds the player who owns the four of denari.
	 * 
	 * @param l
	 *            the assignment in which to look for
	 * @return the player who owns the four of denari
	 */
	public static int FindFourOfDenari(List<List<Integer>> l)
	{
		for (int i = 0; i < 4; i++)
			if (l.get(i).contains(QUATTRODIDENARI))
				return i;
		return -1;
	}

	public static double findAccusiOfPlayer(List<Integer> carteInMano, List<Integer> accusi)
	{
		double points = 0;
		HashSet<Integer> accusoCards = new HashSet<>();
		points += findBongioco(carteInMano, accusoCards);
		points += findNapoli(carteInMano, accusoCards);
		accusi = new ArrayList<>(accusoCards);
		return points;
	}

	private static double findBongioco(List<Integer> carteInMano, HashSet<Integer> accusi)
	{
		double points = 0;
		int[] accusoCards = { 0, 0, 0 };

		for (int c : carteInMano)
			if (c % 10 < 3)
				accusoCards[c % 10]++;

		for (int i = 0; i < 3; i++)
			if (accusoCards[i] > 2)
			{
				for (int c : carteInMano)
					if (c % 10 == i)
						accusi.add(c);

				points += accusoCards[i];
			}

		return points;
	}

	private static double findNapoli(List<Integer> carteInMano, HashSet<Integer> accusi)
	{
		double points = 0;
		int[] accusoCards = { 0, 0, 0, 0 };

		for (int c : carteInMano)
			if (c % 10 < 3)
				accusoCards[c / 10]++;

		for (int i = 0; i < 4; i++)
			if (accusoCards[i] == 3)
			{
				accusi.add(i * 10);
				accusi.add(i * 10 + 1);
				accusi.add(i * 10 + 2);

				points += 3;
			}

		return points;
	}

	public static int getCardSuit(int card)
	{
		return card / 10;
	}

	public static double getCardPoints(int card)
	{
		return puntiPerCarta[card % 10];
	}

	public static int getCardDominance(int card)
	{
		return dominioPerCarta[card % 10];
	}

	public static boolean isSameSuit(int card1, int card2)
	{
		return card1 / 10 == card2 / 10;
	}

	public static double getPointsOfCards(List<Integer> l)
	{
		double s = 0;
		for (int i : l)
			s += puntiPerCarta[i % 10];
		return s;
	}
	// public static int getDominantCard(List<Integer> cards)
	// {
	// assert cards.size() > 0;
	// int d = cards.get(0);
	// for (int i = 1; i < cards.size(); i++)
	// {
	// int s = cards.get(i);
	// if (d / 10 != s / 10)
	// continue;
	// if (dominioPerCarta[s % 10] > dominioPerCarta[d % 10])
	// d = s;
	// }
	// return d;
	// }

	public static int getDominantPlayer(List<Integer> cards, int startPlayer)
	{
		assert cards.size() > 0;
		int d = cards.get(0);
		for (int i = 1; i < cards.size(); i++)
		{
			int s = cards.get(i);
			if (d / 10 != s / 10)
				continue;
			if (dominioPerCarta[s % 10] > dominioPerCarta[d % 10])
				d = s;
		}
		return d;
	}

	public static List<Integer> getPossibiliMosse(List<Integer> carteInMano, List<Integer> cardsOnTable)
	{
		if (cardsOnTable.isEmpty())
			return new ArrayList<>(carteInMano);

		List<Integer> mosse = new ArrayList<>();
		int semeAttuale = cardsOnTable.get(0) / 10;

		for (Integer c : carteInMano)
			if (c / 10 == semeAttuale)
				mosse.add(c);

		if (mosse.isEmpty())
			mosse.addAll(carteInMano);

		return mosse;
	}

	/**
	 * 
	 * It sorts the moves by how much they restrict the next player's moves. (Lowest
	 * Branching Factor Heuristic)
	 * 
	 * @param onTable
	 *            cards on the table
	 * @param mosse
	 *            cards I can play
	 * @param assCarte
	 *            the current cards' assignment
	 * @param pId
	 *            my player id
	 * @return
	 */
	public static boolean LBFHeur(List<Integer> onTable, List<Integer> mosse, List<List<Integer>> assCarte, int pId,
			boolean fast)
	{
		if (mosse.isEmpty() || !onTable.isEmpty())
			return false;

		double[] score = { Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE };
		boolean[] visited = { false, false, false, false };
		List<Integer> oppCards = assCarte.get((pId + 1) % 4);

		for (int i = 0; i < mosse.size(); i++)
		{
			int val = mosse.get(i).intValue();
			int seme = val / 10;
			if (visited[seme])
				continue;
			visited[seme] = true;

			double s = 0;
			for (Integer c : oppCards)
				if (c.intValue() / 10 == seme)
					s += 1;
			if (!fast)
			{
				for (Integer c : assCarte.get((pId + 3) % 4))
					if (c.intValue() / 10 == seme)
						s += 0.2;
			}
			score[seme] = s;
		}

		Comparator<Integer> t = new Comparator<Integer>()
		{
			@Override
			public int compare(Integer arg0, Integer arg1)
			{
				return (int) ((score[arg0.intValue() / 10] - score[arg1.intValue() / 10]) * 5);
			}
		};

		Collections.sort(mosse, t);
		return true;
	}
}

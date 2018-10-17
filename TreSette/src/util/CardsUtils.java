package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import AI.DeterministicAI;

public class CardsUtils
{

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

	/**
	 * The function finds the accusi available among the given cards.
	 * 
	 * @param l
	 *            a list of cards
	 * @return a list of size 2 that contains at index 0 the list of Bongioco, and
	 *         at index 1 the list of Napoli
	 */
	public static List<List<List<Integer>>> findAccusi(List<Integer> l)
	{
		HashSet<Integer> hs = new HashSet<>(l);
		List<List<List<Integer>>> accusi = new ArrayList<>();
		accusi.add(findBongioco(hs));
		accusi.add(findNapoli(hs));
		return accusi;
	}

	/**
	 * This function looks for cards (as integers) that form a Bongioco.
	 * 
	 * @param l
	 *            a set of the cards (as integers)
	 * @return the list of Bongiocos found.
	 */
	public static List<List<Integer>> findBongioco(Set<Integer> l)
	{
		int counter = 0;
		List<List<Integer>> bons = new ArrayList<List<Integer>>();
		ArrayList<Integer> bongioco;
		Integer k;

		for (int i = 0; i < 3; i++)
		{
			counter = 0;
			bongioco = new ArrayList<>();

			for (int j = 0; j < 4; j++)
			{
				k = Integer.valueOf(10 * j + i);
				if (l.contains(k))
				{
					bongioco.add(k);
					counter++;
				}
			}

			if (counter >= 3)
				bons.add(bongioco);
		}

		if (bons.isEmpty())
			return Collections.emptyList();
		return bons;
	}

	/**
	 * This function looks for cards (as integers) that form a Napoli.
	 * 
	 * @param l
	 *            a set of the cards (as integers)
	 * @return the list of Napolis found.
	 */
	public static List<List<Integer>> findNapoli(Set<Integer> l)
	{
		List<List<Integer>> napols = new ArrayList<List<Integer>>();

		for (int i = 0; i < 4; i++)
		{
			if (l.contains(Integer.valueOf(i * 10)) && l.contains(Integer.valueOf(i * 10 + 1))
					&& l.contains(Integer.valueOf(i * 10 + 2)))
			{
				Integer[] nap = { Integer.valueOf(i * 10), Integer.valueOf(i * 10 + 1), Integer.valueOf(i * 10 + 2) };

				napols.add(Arrays.asList(nap));
			}

		}

		if (napols.isEmpty())
			return Collections.emptyList();
		return napols;

	}

	public static String getStats(double evenWinnings, double oddWinnings, double total)
	{

		if (evenWinnings + oddWinnings != total)
			throw new RuntimeException("Errore calcolo");
		double avg = evenWinnings / total;

		double t1 = 1.0 - avg;
		double t2 = avg;

		double ssd = Math.sqrt((evenWinnings * (t1 * t1) + oddWinnings * (t2 * t2)) / (total - 1));
		double moreorless = 1.96 * (ssd / Math.sqrt(total));
		double infBound = avg - moreorless;
		double uppBound = avg + moreorless;

		String format = "Avg:\t%.5f\nSsd:\t%.5f\nCI:\t[ %.5f ; %.5f ]\n";
		return String.format(format, avg, ssd, infBound, uppBound);
	}

	public static Integer getDominantCard(List<Integer> cards)
	{
		assert cards.size() > 0;
		int d = cards.get(0).intValue();
		for (int i = 1; i < cards.size(); i++)
		{
			int s = cards.get(i).intValue();
			if (d / 10 != s / 10)
				continue;
			if (DeterministicAI.dominioPerCarta[s % 10] > DeterministicAI.dominioPerCarta[d % 10])
				d = s;
		}
		return Integer.valueOf(d);
	}
}

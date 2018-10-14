package util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CardsUtils
{

	/**
	 * Finds the player who owns the four of denari.
	 * @param l the assignment in which to look for
	 * @return the player who owns the four of denari
	 */
	public static int FindFourOfDenari(List<List<Integer>> l)
	{
		for (int i = 0; i < 4; i++)
			if (l.get(i).contains(3))
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

}

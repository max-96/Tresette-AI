package AI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
//import java.util.LinkedList;
import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;

import setting.Card;

public class CSPTest
{
	public static void main(String[] args) throws InterruptedException
	{
		test3();
	}

	public static void test3() throws InterruptedException
	{

		final int numSoluzioni = 1000000;

		HashSet<Integer> ex = new HashSet<>();
		ArrayList<Integer> mano = new ArrayList<>(10);

		mano.add(0);
		mano.add(1);
		mano.add(2);
		mano.add(3);
		mano.add(4);
		mano.add(5);
		mano.add(6);
		mano.add(7);
		mano.add(8);
		mano.add(9);

		int[] rcards = { 0, 10, 10, 10 };
		boolean[][] semiAttivi = { { true, true, true, true }, { true, true, true, true }, { true, true, true, true },
				{ true, true, true, true }, };

		SforzaSolver ccc = new SforzaSolver(0, ex, mano, rcards, semiAttivi, numSoluzioni);

		ccc.startProducing();

		while (!ccc.isDone())
		{
			//System.out.println("dormo");
			Thread.sleep(100);
		}

		// Statistica

		int[][] statistiche = new int[40][4];

		for (int i = 0; i < 40; i++)
			Arrays.fill(statistiche[i], 0);

		System.out.println("Stampo:");
		int c = 1;
		while (!ccc.possibiliAssegnamenti.isEmpty())
		{

			List<List<Integer>> sol = ccc.possibiliAssegnamenti.take();
			if (sol == Collections.EMPTY_LIST)
				System.out.println("Fine delle soluzioni");
			else
			{
//				System.out.print(c + " ");
//				System.out.println(sol);
				for (int p = 0; p < 4; p++)
				{
					for (Integer k : sol.get(p))
					{
						statistiche[k][p]++;
					}
				}

			}
			c++;
		}

		System.out.println("Tempo di Esecuzione ("+numSoluzioni+"): " + ccc.tempoEsecuzione + " ms");

		// Stampa stats
		for (int i = 0; i < 40; i++)
		{
			System.out.print("Carta: " + (i) + "\t\t");
			System.out.print(((double) (statistiche[i][0]) / numSoluzioni) + "\t");
			System.out.print(((double) (statistiche[i][1]) / numSoluzioni) + "\t");
			System.out.print(((double) (statistiche[i][2]) / numSoluzioni) + "\t");
			System.out.print(((double) (statistiche[i][3]) / numSoluzioni) + "\t");
			System.out.println();
			

		}

		System.out.println(new Card(Card.Suit.BASTONI, Card.Value.DUE).compareTo(new Card(Card.Suit.BASTONI, Card.Value.TRE)));

	}

	/**
	 * @throws InterruptedException
	 */
//	public static void test2() throws InterruptedException
//	{
//
//		final int numSoluzioni = 1000;
//
//		HashSet<Integer> ex = new HashSet<>();
//		LinkedList<Integer> mano = new LinkedList<>();
//		mano.add(0);
//		mano.add(1);
//		mano.add(2);
//		mano.add(3);
//		mano.add(4);
//		mano.add(5);
//		mano.add(6);
//		mano.add(7);
//		mano.add(8);
//		mano.add(9);
//
//		int[] rcards = { 0, 10, 10, 10 };
//		boolean[][] piombi = { { false, false, false, false }, { false, false, false, false },
//				{ false, false, false, false }, { false, false, false, false }, };
//
//		RandCSPSolver ccc = new RandCSPSolver(0, ex, mano, rcards, numSoluzioni, piombi);
//
//		ccc.produce();
//
//		while (!ccc.isDone())
//		{
//			Thread.sleep(100);
//
//		}
//
//		int[][] statistiche = new int[40][4];
//
//		for (int i = 0; i < 40; i++)
//			Arrays.fill(statistiche[i], 0);
//
//		System.out.println("Stampo:");
//		int c = 1;
//		while (!ccc.soluzioni.isEmpty())
//		{
//
//			Map<Integer, Integer> sol = ccc.soluzioni.take();
//			if (sol == Collections.EMPTY_MAP)
//				System.out.println("Fine delle soluzioni");
//			else
//			{
//				System.out.print(c + " ");
//				System.out.println(sol);
//				for (Entry<Integer, Integer> k : sol.entrySet())
//				{
//					statistiche[k.getKey()][k.getValue()]++;
//				}
//			}
//			c++;
//		}
//		System.out.println("Tempo di Esecuzione: " + ccc.tempoEsec + " ms");
//
//		// Stampa stats
//		for (int i = 0; i < 40; i++)
//		{
//			System.out.print("Carta: " + (i) + "\t\t");
//			System.out.print(((double) (statistiche[i][0]) / numSoluzioni) + "\t");
//			System.out.print(((double) (statistiche[i][1]) / numSoluzioni) + "\t");
//			System.out.print(((double) (statistiche[i][2]) / numSoluzioni) + "\t");
//			System.out.print(((double) (statistiche[i][3]) / numSoluzioni) + "\t");
//			System.out.println();
//
//		}
//
//	}
}
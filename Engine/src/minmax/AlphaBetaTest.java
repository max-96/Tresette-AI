package minmax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class AlphaBetaTest
{

	public static final int DEPTH = 8;

	public static void main(String[] args)
	{
		test3();
	}

//	@Deprecated
//	private static void test2()
//	{
//		List<List<Integer>> assegnamentoCasuale = new ArrayList<>();
//		List<Integer> cardsOnTable = new ArrayList<>();
//		{
//			List<Integer> t = new ArrayList<>();
//
//			for (int i = 0; i < 40; i++)
//			{
//				t.add(i);
//			}
//			Collections.shuffle(t, new Random(1200));
//
//			for (int i = 0; i < 4; i++)
//			{
//				List<Integer> l = new ArrayList<>(t.subList(i * 10, (i + 1) * 10));
//				assegnamentoCasuale.add(l);
//			}
//		}
//
//		System.out.println("Assegnamento: ");
//		System.out.println(assegnamentoCasuale);
//		AlphaBetaKiller solver = new AlphaBetaKiller(0, 2);
//		System.out.println("Inizio Risoluzione:");
//		Integer mossa = solver.getBestMove(assegnamentoCasuale, cardsOnTable, 0, 0);
//
//		System.out.println("Mossa scelta:\t\t" + mossa);
//		System.out.println("Valore:\t\t\t" + solver.winningValue);
//		System.out.println("# Forks:\t\t" + solver.getForkCounter());
//		System.out.println("Alpha Prunes:\t\t" + solver.getAlphapruning());
//		System.out.println("Beta Prunes:\t\t" + solver.getBetapruning());
//		System.out.println("Elapsed Time(ms):\t" + solver.executionTime);
//
//		System.out.println("---------------------");
//
//		System.out.println("Alpha Moves:");
//		// for(int i=0;i<40;i++)
//		System.out.println(solver.getAlphaMoves());
//
//		System.out.println("Beta Moves:");
//		// for(int i=0;i<40;i++)
//		System.out.println(solver.getBetaMoves());
//
//	}

	public static void test3()
	{
		List<List<Integer>> assegnamentoCasuale = new ArrayList<>();
		List<Integer> cardsOnTable = new ArrayList<>();
		{
			List<Integer> t = new ArrayList<>();

			for (int i = 0; i < 40; i++)
			{
				t.add(i);
			}
			Collections.shuffle(t, new Random());

			for (int i = 0; i < 4; i++)
			{
				List<Integer> l = new ArrayList<>(t.subList(i * 10, (i + 1) * 10));
				assegnamentoCasuale.add(l);
			}
		}

		System.out.println("Assegnamento: ");
		System.out.println(assegnamentoCasuale);

		AlphaBetaLBF solver1 = new AlphaBetaLBF(0, 18);
		AlphaBeta solver2 = new AlphaBeta(0, 18);
		System.out.println("Inizio Risoluzione (killer1):");
		Integer mossa1 = solver1.getBestMove(assegnamentoCasuale, cardsOnTable, 0, 0);

		System.out.println("Inizio Risoluzione (killer2):");
		Integer mossa2 = solver2.getBestMove(assegnamentoCasuale, cardsOnTable, 0, 0);

		System.out.println("Mossa scelta:\t\t" + mossa1 + "\t" + mossa2);
		System.out.println("Valore:\t\t\t" + solver1.winningValue + "\t" + solver2.winningValue);
		System.out.println("# Forks:\t\t" + solver1.getForkCounter() + "\t" + solver2.getForkCounter());
		System.out.println("Alpha Prunes:\t\t" + solver1.getAlphapruning() + "\t" + solver2.getAlphapruning());
		System.out.println("Beta Prunes:\t\t" + solver1.getBetapruning() + "\t" + solver2.getBetapruning());
		System.out.println("Elapsed Time(ms):\t" + solver1.executionTime + "\t" + solver2.executionTime);

		System.out.println("---------------------");

		System.out.println("Alphacount:");
		System.out.println(Arrays.toString(normalize(solver2.alphacount)));
		System.out.println("Betacount:");
		System.out.println(Arrays.toString(normalize(solver2.betacount)));
	}

	public static float[] normalize(long[] array)
	{
		long t = 0L;
		for (long i : array)
			t += i;
		float[] xx = new float[array.length];
		for (int i = 0; i < array.length; i++)
			xx[i] = (((float) array[i]) / t) * 100;

		return xx;
	}
}

package AI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import MCTS.MonteCarloTreeSearch;
import minmax.AlphaBeta;
import util.*;

public class AITesting
{

	public static int NUM_TRAILS = 500;

	public static void main(String[] args)
	{

		// System.out.println(getStats(4291,709, 5000));
		Locale.setDefault(Locale.US);
		int[] a = { 16, 1, 16, 1 };
		test1(a);
		System.out.println(MovesStats.getInstance().dump());
		// System.out.println("--------");
		//// float[] alphaP= AlphaBetaTest.normalize(AlphaBetaKiller3.alphacount);
		//// float[] betaP= AlphaBetaTest.normalize(AlphaBetaKiller3.betacount);
		//// System.out.println(Arrays.toString(alphaP));
		//// System.out.println(Arrays.toString(betaP));
		System.out.println(String.format("ABK Max Time:\t%.3f s", (double) AlphaBeta.maxExecTime / 1000));
		System.out.println(
				String.format("ABK Avg Time:\t%.3f s", (((double) AlphaBeta.sumExecTime) / AlphaBeta.numExec) / 1000));

		// System.out.println(
		// String.format("MonteCarloTS Max Time:\t%.3f", (double)
		// MonteCarloTreeSearch.maxExecTime / 1000));
	}

	private static void test1(int[] assignmentAI)
	{
		assert assignmentAI.length == 4;

		int[] winnings = { 0, 0 };

		DeterministicAI[] detAIs = new DeterministicAI[4];

		for (int i = 0; i < 4; i++)
		{
			DeterministicAI a;

			if (assignmentAI[i] == 0)
				a = new HumanPlayer(i);
			else if (assignmentAI[i] == 1)
				a = genRandWalk(i);
			else if (assignmentAI[i] < 1000)
				a = genABK3(i, assignmentAI[i]);
			else if (assignmentAI[i] < 2000)
				a = genMCTS(i, (assignmentAI[i] - 1000) * 2000);
			else
				a = genRandWalk(i);

			detAIs[i] = a;
		}

		for (int k = 0; k < NUM_TRAILS; k++)
		{
			List<List<Integer>> randomAssignment = randomAssignment();
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer = CardsUtils.FindFourOfDenari(randomAssignment);
			int initTeam = currentplayer & 1;

			System.out.print(initTeam + " ");

			double scoreEven = computeAccusiTeamScore(randomAssignment, 0);
			double scoreOdd = computeAccusiTeamScore(randomAssignment, 1);

			NeutralGameState gs = new NeutralGameState(randomAssignment, cardsOnTable, currentplayer, scoreEven,
					scoreOdd);

			while (!gs.terminal)
			{
				DeterministicAI dai = detAIs[currentplayer];
				Integer mossa = dai.getBestMove(randomAssignment, cardsOnTable, 0, 0);
				gs = gs.genSuccessor(mossa);
				System.out.print("x");
				randomAssignment = gs.getCardsAssignment();
				cardsOnTable = gs.getCardsOnTable();
				currentplayer = gs.currentPlayer;

			}

			if (gs.getScoreEven() > gs.getScoreOdd())
			{
				winnings[0] += 1;
				System.out.println(".0");
			} else
			{
				winnings[1] += 1;
				System.out.println(".1");
			}
			if (k % (NUM_TRAILS / 10) == 0)
				System.out.println("Esecuzione " + k);

		}
		System.out.println("TEAM EVEN:\t" + winnings[0]);
		System.out.println("TEAM ODD:\t" + winnings[1]);
		System.out.println(getStats(winnings[0], winnings[1], NUM_TRAILS));

	}

	@SuppressWarnings("unused")
	private static void test1()
	{
		int[] winnings = { 0, 0 };
		boolean[] playerALFABETA = { true, false, true, false };
		for (int k = 0; k < NUM_TRAILS; k++)
		{
			List<List<Integer>> randomAssignment = randomAssignment();
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer = find4Denari(randomAssignment);
			int initTeam = currentplayer % 2;
			System.out.print(initTeam + " ");
			NeutralGameState gs = new NeutralGameState(randomAssignment, cardsOnTable, currentplayer, 0, 0);
			double score1 = 0, score2 = 0;

			while (!gs.terminal)
			{
				DeterministicAI dai = playerALFABETA[currentplayer] ? genABK3(currentplayer, 4)
						: genMCTS(currentplayer, 30);
				Integer mossa = dai.getBestMove(randomAssignment, cardsOnTable, score1, score2);
				gs = gs.genSuccessor(mossa);
				System.out.print("x");
				randomAssignment = gs.getCardsAssignment();
				cardsOnTable = gs.getCardsOnTable();
				currentplayer = gs.currentPlayer;

			}

			if (gs.getScoreEven() > gs.getScoreOdd())
			{
				winnings[0] += 1;
				System.out.println(".0");
			} else
			{
				winnings[1] += 1;
				System.out.println(".1");
			}

		}
		System.out.println("TEAM EVEN:\t" + winnings[0]);
		System.out.println("TEAM ODD:\t" + winnings[1]);
		System.out.println(getStats(winnings[0], winnings[1], NUM_TRAILS));

	}

	@SuppressWarnings("unused")
	private static void test2()
	{
		int[] winnings = { 0, 0 };
		boolean[] playerALFABETA = { true, false, true, false };
		for (int k = 0; k < NUM_TRAILS; k++)
		{
			List<List<Integer>> randomAssignment = randomAssignment();
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer = find4Denari(randomAssignment);
			int initTeam = currentplayer % 2;
			System.out.print(initTeam + " ");
			NeutralGameState gs = new NeutralGameState(randomAssignment, cardsOnTable, currentplayer, 0, 0);
			double score1 = 0, score2 = 0;

			while (!gs.terminal)
			{
				DeterministicAI dai = playerALFABETA[currentplayer] ? genABK3(currentplayer, 16)
						: genRandWalk(currentplayer);
				Integer mossa = dai.getBestMove(randomAssignment, cardsOnTable, 0, 0);
				gs = gs.genSuccessor(mossa);
				System.out.print("x");
				randomAssignment = gs.getCardsAssignment();
				cardsOnTable = gs.getCardsOnTable();
				currentplayer = gs.currentPlayer;

			}

			if (gs.getScoreEven() > gs.getScoreOdd())
			{
				winnings[0] += 1;
				System.out.println(".0");
			} else
			{
				winnings[1] += 1;
				System.out.println(".1");
			}

		}
		System.out.println("TEAM EVEN:\t" + winnings[0]);
		System.out.println("TEAM ODD:\t" + winnings[1]);
		System.out.println(getStats(winnings[0], winnings[1], NUM_TRAILS));

	}

	@SuppressWarnings("unused")
	private static void test3()
	{
		int[] winnings = { 0, 0 };
		boolean[] playerMCTS = { true, false, true, false };
		for (int k = 0; k < NUM_TRAILS; k++)
		{
			List<List<Integer>> randomAssignment = randomAssignment();
			List<Integer> cardsOnTable = new ArrayList<>();
			int currentplayer = find4Denari(randomAssignment);
			int initTeam = currentplayer % 2;
			System.out.print(initTeam + " ");
			NeutralGameState gs = new NeutralGameState(randomAssignment, cardsOnTable, currentplayer, 0, 0);
			double score1 = 0, score2 = 0;

			while (!gs.terminal)
			{
				DeterministicAI dai = playerMCTS[currentplayer] ? genMCTS(currentplayer, 60000)
						: genRandWalk(currentplayer);
				Integer mossa = dai.getBestMove(randomAssignment, cardsOnTable, score1, score2);
				gs = gs.genSuccessor(mossa);
				System.out.print("x");
				randomAssignment = gs.getCardsAssignment();
				cardsOnTable = gs.getCardsOnTable();
				currentplayer = gs.currentPlayer;

			}

			if (gs.getScoreEven() > gs.getScoreOdd())
			{
				winnings[0] += 1;
				System.out.println(".0");
			} else
			{
				winnings[1] += 1;
				System.out.println(".1");
			}

		}
		System.out.println("TEAM EVEN:\t" + winnings[0]);
		System.out.println("TEAM ODD:\t" + winnings[1]);
		System.out.println(getStats(winnings[0], winnings[1], NUM_TRAILS));

	}

	@SuppressWarnings("unused")
	private static void test4()
	{
		int[] winnings = { 0, 0 };
		boolean[] playerALFABETA = { true, false, true, false };
		// for (int k = 0; k < 20; k++) {
		List<List<Integer>> randomAssignment = randomAssignment();

		System.out.println(randomAssignment);
		List<Integer> cardsOnTable = new ArrayList<>();
		int currentplayer = find4Denari(randomAssignment);
		System.out.println("Inizia player: " + currentplayer);
		int initTeam = currentplayer % 2;
		AIGameState gs = new AIGameState(randomAssignment, cardsOnTable, currentplayer, true, 0, 0);
		double score1 = 0, score2 = 0;
		while (!gs.terminal)
		{

			DeterministicAI dai = playerALFABETA[currentplayer] ? genABK3(currentplayer) : genRandWalk(currentplayer);
			Integer mossa = dai.getBestMove(randomAssignment, cardsOnTable, score1, score2);
			System.out.print(currentplayer + ":" + mossa + "\t");

			gs = gs.genSuccessor(mossa, true);

			randomAssignment = gs.getCardsAssignment();
			// System.out.println(randomAssignment);

			cardsOnTable = gs.getCardsOnTable();
			if (cardsOnTable.isEmpty())
				System.out.println();

			currentplayer = gs.currentPlayer;
			if (currentplayer % 2 == initTeam)
			{
				score1 = gs.scoreMyTeam;
				score2 = gs.scoreOtherTeam;
			} else
			{
				score2 = gs.scoreMyTeam;
				score1 = gs.scoreOtherTeam;
			}

		}
		// }
	}

	@SuppressWarnings("unused")
	private static void test5()
	{
		// int[] winnings = { 0, 0 };
		boolean[] playerALFABETA = { true, false, true, false };
		// for (int k = 0; k < 20; k++) {
		List<List<Integer>> randomAssignment = randomAssignment();

		System.out.println(randomAssignment);
		List<Integer> cardsOnTable = new ArrayList<>();
		int currentplayer = find4Denari(randomAssignment);
		System.out.println("Inizia player: " + currentplayer);
		int initTeam = currentplayer % 2;
		AIGameState gs = new AIGameState(randomAssignment, cardsOnTable, currentplayer, true, 0, 0);
		double score1 = 0, score2 = 0;
		while (!gs.terminal)
		{

			DeterministicAI dai = playerALFABETA[currentplayer] ? genABK3(currentplayer)
					: new HumanPlayer(currentplayer);

			Integer mossa = dai.getBestMove(randomAssignment, cardsOnTable, score1, score2);
			System.out.print(currentplayer + ":" + mossa + "\t");

			gs = gs.genSuccessor(mossa);

			randomAssignment = gs.getCardsAssignment();
			// System.out.println(randomAssignment);

			cardsOnTable = gs.getCardsOnTable();
			if (cardsOnTable.isEmpty())
				System.out.println();

			currentplayer = gs.currentPlayer;
			if (currentplayer % 2 == initTeam)
			{
				score1 = gs.scoreMyTeam;
				score2 = gs.scoreOtherTeam;
			} else
			{
				score2 = gs.scoreMyTeam;
				score1 = gs.scoreOtherTeam;
			}

		}
		// }
	}

	private static int find4Denari(List<List<Integer>> l)
	{
		for (int i = 0; i < 4; i++)
			if (l.get(i).contains(3))
				return i;
		throw new RuntimeException("Quattro di Denari not present in the deck.");
	}

	private static List<List<Integer>> randomAssignment()
	{
		List<List<Integer>> assegnamentoCasuale = new ArrayList<>();
		List<Integer> t = new ArrayList<>();

		for (int i = 0; i < 40; i++)
		{
			t.add(i);
		}
		Collections.shuffle(t);

		for (int i = 0; i < 4; i++)
		{
			List<Integer> l = new ArrayList<>(t.subList(i * 10, (i + 1) * 10));
			assegnamentoCasuale.add(l);
		}
		return assegnamentoCasuale;

	}

	public static double computeAccusiTeamScore(List<List<Integer>> randomAssignment, int team)
	{

		assert team == 0 || team == 1;
		double d = 0;

		List<List<List<Integer>>> m = CardsUtils.findAccusi(randomAssignment.get(team));
		for (List<Integer> n : m.get(0))
			d += n.size();
		for (List<Integer> n : m.get(1))
			d += n.size();
		m = CardsUtils.findAccusi(randomAssignment.get(team + 2));
		for (List<Integer> n : m.get(0))
			d += n.size();
		for (List<Integer> n : m.get(1))
			d += n.size();

		return d;
	}

	public static String getStats(double evenWinnings, double oddWinnings, double total)
	{

		assert evenWinnings + oddWinnings == total;

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

	public static RandWalk genRandWalk(int player)
	{
		return new RandWalk(player);
	}

	public static AlphaBeta genABK3(int player)
	{
		return new AlphaBeta(player, 16);
	}

	public static AlphaBeta genABK3(int player, int depth)
	{
		return new AlphaBeta(player, depth);
	}

	public static MonteCarloTreeSearch genMCTS(int player)
	{
		return genMCTS(player, 50000);
	}

	public static MonteCarloTreeSearch genMCTS(int player, int iter)
	{
		return new MonteCarloTreeSearch(player, iter);
	}
}

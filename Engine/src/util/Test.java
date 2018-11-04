package util;

import AI.CheatingPlayer;
import AI.DeterminizationPlayer;
import AI.PartialInfoPlayer;
import AI.RandWalk;
import MCTS.MonteCarloTreeSearch;
import setting.Game;
import setting.Player;

public class Test
{
	private Player[] players;
	private int noMatches;

	public Test(int noMatches, Player... players)
	{
		this.players = players;
		this.noMatches = noMatches;
	}

	public void exec()
	{
		int[] winnings = { 0, 0 };

		for (int i = 0; i < noMatches; i++)
		{
			Game gm = new Game(players);
			int res = gm.run();
			winnings[res] += 1;
			System.out.println(String.format("Match %d won by team %d", i, res));
		}
		System.out.println();
		System.out.println(String.format("Team Even:%d\tTeam Odd:%d\n", winnings[0], winnings[1]));
		System.out.println("====== SUMMARY ======");
		System.out.println("Even Players:");
		System.out.println(players[0]);
		System.out.println(players[2]);
		System.out.println("Odd Players:");
		System.out.println(players[1]);
		System.out.println(players[3]);
		System.out.println(getStats(winnings[0], winnings[1]));
	}
	
	public static String getStats(double evenWinnings, double oddWinnings)
	{

		double total = evenWinnings + oddWinnings;

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

	public static void main(String[] args)
	{
		Player[] players = new Player[4];
		int noMatches = 100;
		
		players[0] = new DeterminizationPlayer(0, new MonteCarloTreeSearch.Factory(2000, 0.75), 10);
		players[1] = new PartialInfoPlayer(new RandWalk(1));
		players[2] = new DeterminizationPlayer(2, new MonteCarloTreeSearch.Factory(2000, 0.75), 10);
		players[3] = new PartialInfoPlayer(new RandWalk(3));
//		players[0] = new PartialInfoPlayer(new RandWalk(0));
//		players[1] = new PartialInfoPlayer(new RandWalk(1));
//		players[2] = new PartialInfoPlayer(new RandWalk(2));
//		players[3] = new PartialInfoPlayer(new RandWalk(3));
		
//		players[1] = new CheatingPlayer(new AlphaBeta(1, 8));
//		players[0] = new PartialInfoPlayer(new RandWalk(0));
//		players[3] = new CheatingPlayer(new AlphaBeta(3, 8));
//		players[2] = new PartialInfoPlayer(new RandWalk(2));
		
		//TODO assegnamento da args
		
		Test test = new Test(noMatches, players);
		
		test.exec();
	}
}

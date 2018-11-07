package it.uniroma1.tresette.util;

import it.uniroma1.tresette.setting.Game;
import it.uniroma1.tresette.setting.Player;

public class Test
{
	private Player[] players;
	private int noMatches = 200;

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
		CommandLineParser clp = new CommandLineParser(args);
		if (!clp.parseTest())
			return;
		
		int noMatches = clp.parseMatchesNr();
		if (noMatches < 0)
			return;
		
		Player[] players = clp.parseArgs();
		
		if (players == null)
			return;
		
		Test test = new Test(noMatches, players);
		test.exec();
	}
}

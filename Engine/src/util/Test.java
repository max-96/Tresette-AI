package util;

import java.util.Arrays;

import AI.AITesting;
import it.ai.tresette.GameManager;
import setting.Game;
import setting.Player;

public class Test
{
	private Player[] players;
	private int noMatches;

	public Test(Player... players, int noMatches)
	{
		this.players = players;
		this.noMatches = noMatches;
	}

	public void exec()
	{
<<<<<<< HEAD
=======
		int[] winnings = { 0, 0 };

		System.out.println("Even Players:");
		System.out.println(players[0]);
		System.out.println(players[2]);
		System.out.println("Odd Players:");
		System.out.println(players[1]);
		System.out.println(players[3]);
		System.out.println();

>>>>>>> 8a9a2520f22bbd0fe72d3adc619de75b6ce0b0d1
		for (int i = 0; i < noMatches; i++)
		{
			Game gm = new Game();
			int res = gm.run();
			winnings[res] += 1;
			System.out.println(String.format("Match %d won by team %d", i, res));
		}
<<<<<<< HEAD
=======
		System.out.println();
		System.out.println(String.format("Team Even:%d\tTeam Odd:%d\n", winnings[0], winnings[1]));
		System.out.println(getStats(winnings[0], winnings[1]));

>>>>>>> 8a9a2520f22bbd0fe72d3adc619de75b6ce0b0d1
	}

	public void dump(String filename)
	{
		// TODO
	}

	public String dump()
	{
		// TODO
		return null;
	}

	public static void main(String[] args)
	{
		Player[] players = new Player[4];
		int noMatches = 10;
		//TODO assegnamento da args
		
		Test test = new Test(players, noMatches);
		
		test.exec();
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
}

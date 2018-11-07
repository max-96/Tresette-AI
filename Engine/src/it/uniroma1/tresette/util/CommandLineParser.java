package it.uniroma1.tresette.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import it.uniroma1.tresette.ai.CheatingPlayer;
import it.uniroma1.tresette.ai.DeterminizationPlayer;
import it.uniroma1.tresette.ai.PartialInfoPlayer;
import it.uniroma1.tresette.ai.RandWalk;
import it.uniroma1.tresette.ismcts.InformationSetMCTS;
import it.uniroma1.tresette.mcts.MonteCarloTreeSearch;
import it.uniroma1.tresette.minmax.AlphaBeta;
import it.uniroma1.tresette.setting.Player;

public class CommandLineParser
{
	private static final int MATCHES = 200;
	private static final int DEPTH = 12;
	private static final int ITERATIONS = 1200;
	private static final int DETERMINIZATIONS = 100;

	private List<String> args = new ArrayList<>();
	private boolean forTest = false;
	private int aiNr;

	private enum AI
	{
		RANDOM, CHEATING_AB, CHEATING_MCTS, ALPHABETA, MCTS, // ISMTCS
	}

	public CommandLineParser(String[] args)
	{
		this.args.addAll(Arrays.asList(args));
	}

	private void printHelp()
	{
		System.out.println("Artificial Intelligences for the game of Tresette\n");
		System.out.println("Usage: choose " + aiNr + " AIs from the following list");
		System.out.println("  RANDOM");
		System.out.println("  CHEATING_AB");
		System.out.println("    -d\tExploring depth          [" + DEPTH + "]");
		System.out.println("  CHEATING_MCTS");
		System.out.println("    -i\tIterations number        [" + ITERATIONS + "]");
		System.out.println("  ALPHABETA ");
		System.out.println("    -d\tExploring depth          [" + DEPTH + "]");
		System.out.println("    -n\tDeterminizations number  [" + DETERMINIZATIONS + "]");
		System.out.println("  MCTS");
		System.out.println("    -i\tIterations number        [" + ITERATIONS + "]");
		System.out.println("    -n\tDeterminizations number  [" + DETERMINIZATIONS + "]");
//		System.out.println(" ISMTCS");
//		System.out.println("    -i\tIterations number        [" + ITERATIONS + "]");
		System.out.println("\nOptions:");
		System.out.println("  -h\tPrint this help message  [false]");
		System.out.println("  -t\tRun tests instead of GUI [false]");
		System.out.println("  -m\tTest matches number      [" + MATCHES + "]");
	}

	public boolean parseTest()
	{
		forTest = args.remove("-t");
		aiNr = forTest ? 4 : 3;
		return forTest;
	}

	public int parseMatchesNr()
	{
		int m = MATCHES;
		int i = 0;
		try
		{
			for (i = 0; i < args.size(); i++)
				if (args.get(i).equals("-m"))
				{
					m = Integer.parseInt(args.get(++i));
					args.remove(i);
					args.remove(--i);
				}
		} catch (Exception e)
		{
			if (i == args.size())
				i--;
			System.out.println("Error: invalid argument \"" + args.get(i) + "\"\n");
			printHelp();
			return -1;
		}
		return m;
	}

	public Player[] parseArgs()
	{
		if (args.contains("-h"))
		{
			printHelp();
			return null;
		}

		Player[] players = new Player[aiNr];
		int argNr = 0;
		boolean parsingOpt = false;
		
		try
		{
			for (int i = 0; i < aiNr; i++)
			{
				AI ai = AI.valueOf(args.get(argNr));

				int id = forTest ? i : i + 1;
				int depth = DEPTH;
				int iter = ITERATIONS;
				int deterNr = DETERMINIZATIONS;
				
				switch (ai)
				{
				case CHEATING_AB:
					if (argNr + 1 < args.size() && args.get(argNr + 1).equals("-d"))
					{
						parsingOpt = true;
						depth = Integer.parseInt(args.get(argNr += 2));
					}
					players[i] = new CheatingPlayer(new AlphaBeta(id, depth));
					break;

				case ALPHABETA:
					for (int j = 0; j < 2; j++)
					{
						if (argNr + 1 < args.size() && args.get(argNr + 1).equals("-d"))
						{
							parsingOpt = true;
							depth = Integer.parseInt(args.get(argNr += 2));
						}
						if (argNr + 1 < args.size() && args.get(argNr + 1).equals("-n"))
						{
							parsingOpt = true;
							deterNr = Integer.parseInt(args.get(argNr += 2));
						}
					}
					players[i] = new DeterminizationPlayer(new AlphaBeta.Factory(id, depth), deterNr);
					break;

//				case ISMTCS:
//					if (argNr + 1 < args.size() && args.get(argNr + 1).equals("-i"))
//					{
//						parsingOpt = true;
//						iter = Integer.parseInt(args.get(argNr += 2));
//					}
//					players[i] = new PartialInfoPlayer(new InformationSetMCTS(id, iter));
//					break;

				case MCTS:
					for (int j = 0; j < 2; j++)
					{
						if (argNr + 1 < args.size() && args.get(argNr + 1).equals("-i"))
						{
							parsingOpt = true;
							iter = Integer.parseInt(args.get(argNr += 2));
						}
						if (argNr + 1 < args.size() && args.get(argNr + 1).equals("-n"))
						{
							parsingOpt = true;
							deterNr = Integer.parseInt(args.get(argNr += 2));
						}
					}
					players[i] = new DeterminizationPlayer(new MonteCarloTreeSearch.Factory(id, iter), deterNr);
					break;

				case CHEATING_MCTS:
					if (argNr + 1 < args.size() && args.get(argNr + 1).equals("-i"))
					{
						parsingOpt = true;
						iter = Integer.parseInt(args.get(argNr += 2));
					}
					players[i] = new CheatingPlayer(new MonteCarloTreeSearch(id, iter));
					break;

				case RANDOM:
					players[i] = new PartialInfoPlayer(new RandWalk(id));
					break;

				default:
					break;
				}

				parsingOpt = false;
				argNr++;
			}
			
			if (argNr < args.size())
			{
				System.out.println("Error: please provide " + aiNr + " AIs\n");
				printHelp();
				return null;
			}

			return players;
		} catch (Exception e)
		{
			if (argNr == args.size() && !parsingOpt)
				System.out.println("Error: please provide " + aiNr + " AIs\n");
			else
			{
				if (argNr == args.size())
					argNr--;
				System.out.println("Error: invalid argument \"" + args.get(argNr) + "\"\n");
			}
			printHelp();
			return null;
		}
	}
}

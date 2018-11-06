package it.uniroma1.tresette.util;

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
	private final boolean forTest;
	
	private enum AI
	{
		RANDOM, AB_CHEATING, MCTS_CHEATING, ALPHABETA, MCTS, ISMTCS
	}
	
	public CommandLineParser(boolean forTest)
	{
		this.forTest = forTest;
	}
	
	private void printHelp()
	{
		System.out.println("Artificial Intelligences for the game of tresette\n");
		int aiNr = forTest ? 4 : 3;
		System.out.println("Usage: choose " + aiNr + " AIs from the following list");
		System.out.println("  RANDOM");
		System.out.println("  AB_CHEATING");
		System.out.println("    -d\tExploring depth [12]");
		System.out.println("  MCTS_CHEATING");
		System.out.println("    -i\tIterations number [1200]");
		System.out.println("  ALPHABETA ");
		System.out.println("    -d\tExploring depth [12]");
		System.out.println("    -n\tDeterminizations number [100]");
		System.out.println("  MCTS");
		System.out.println("    -i\tIterations number [1200]");
		System.out.println("    -n\tDeterminizations number [100]");
		System.out.println("  ISMTCS");
		System.out.println("    -i\tIterations number [1200]");
	}
	
	public Player[] parseArgs(String[] args)
	{
		for (String arg : args)
			if (arg.equals("-h"))
			{
				printHelp();
				return null;
			}
		
		int aiNr = forTest ? 4 : 3;
		Player[] players = new Player[aiNr];
		int argNr = 0;
		
		try
		{
			for (int i = 0; i < aiNr; i++)
			{
				AI ai = AI.valueOf(args[argNr]);
				
				int id = forTest ? i : i + 1;
				int depth = 12;
				int iter = 1200;
				int deterNr = 100;
				switch (ai)
				{
					case AB_CHEATING:
						if (argNr + 1 < args.length && args[argNr + 1].equals("-d"))
						{
							depth = Integer.parseInt(args[++argNr + 1]);
							argNr++;
						}
						players[i] = new CheatingPlayer(new AlphaBeta(id, depth));
						break;
						
					case ALPHABETA:
						for (int j = 0; j < 2; j++)
						{
							if (argNr + 1 < args.length && args[argNr + 1].equals("-d"))
							{
								depth = Integer.parseInt(args[++argNr + 1]);
								argNr++;
							}
							if (argNr + 1 < args.length && args[argNr + 1].equals("-n"))
							{
								deterNr = Integer.parseInt(args[++argNr + 1]);
								argNr++;
							}
						}
						players[i] = new DeterminizationPlayer(new AlphaBeta.Factory(id, depth), deterNr);
						break;
						
					case ISMTCS:
						if (argNr + 1 < args.length && args[argNr + 1].equals("-i"))
						{
							iter = Integer.parseInt(args[++argNr + 1]);
							argNr++;
						}
						players[i] = new PartialInfoPlayer(new InformationSetMCTS(id, iter));
						break;
						
					case MCTS:
						for (int j = 0; j < 2; j++)
						{
							if (argNr + 1 < args.length && args[argNr + 1].equals("-i"))
							{
								iter = Integer.parseInt(args[++argNr + 1]);
								argNr++;
							}
							if (argNr + 1 < args.length && args[argNr + 1].equals("-n"))
							{
								deterNr = Integer.parseInt(args[++argNr + 1]);
								argNr++;
							}
						}
						players[i] = new DeterminizationPlayer(new MonteCarloTreeSearch.Factory(id, iter), deterNr);
						break;
						
					case MCTS_CHEATING:
						if (argNr + 1 < args.length && args[argNr + 1].equals("-i"))
						{
							iter = Integer.parseInt(args[++argNr + 1]);
							argNr++;
						}
						players[i] = new CheatingPlayer(new MonteCarloTreeSearch(id, iter));
						break;
						
					case RANDOM:
						players[i] = new PartialInfoPlayer(new RandWalk(id));
						break;
						
					default:
						break;
				}
				argNr++;
			}
			if (argNr < args.length)
			{
				System.out.println("Error: invalid argument \"" + args[args.length - 1] + "\"\n");
				printHelp();
				return null;
			}
			
			return players;	
		}
		catch (Exception e)
		{
			if (argNr == args.length)
				System.out.println("Error: please provide " + aiNr + " AIs\n");
			else
				System.out.println("Error: invalid argument \"" + args[argNr] + "\"\n");
			printHelp();
			return null;
		}
	}
}

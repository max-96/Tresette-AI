package util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import AI.CheatingPlayer;
import AI.DeterminizationPlayer;
import AI.PartialInfoPlayer;
import AI.RandWalk;
import MCTS.MonteCarloTreeSearch;
import ismcts.InformationSetMCTS;
import minmax.AlphaBeta;
import setting.Player;

public class CommandLineParser
{
	private final int aiNr;
	
	private enum AI
	{
		RANDOM, AB_CHEATING, MCTS_CHEATING, ALPHABETA, MCTS, ISMTCS
	}
	
	public CommandLineParser(boolean forTest)
	{
		if (forTest)
			aiNr = 4;
		else
			aiNr = 3;
	}
	
	private void printHelp()
	{
		System.out.println("An help message");
	}
	
	public Player[] parseArgs(String[] args)
	{
		for (String arg : args)
			if (arg.equals("-h"))
			{
				printHelp();
				return null;
			}
		
		Player[] players = new Player[aiNr];
		int argNr = 0;
		
		try
		{
			for (int i = 0; i < aiNr; i++)
			{
				AI ai = AI.valueOf(args[argNr++]);
				int depth = 1;
				int iter = 1;
				int deterNr = 1;
				switch (ai)
				{
					case AB_CHEATING:
						if (argNr < args.length && args[argNr].equals("-d"))
						{
							argNr += 2;
							depth = Integer.parseInt(args[argNr++]);
						}
						players[i] = new CheatingPlayer(new AlphaBeta(i, depth));
						break;
						
					case ALPHABETA:
						for (int j = 0; j < 2; j++)
						{
							if (argNr < args.length && args[argNr].equals("-d"))
							{
								argNr++;
								depth = Integer.parseInt(args[argNr++]);
							}
							if (argNr < args.length && args[argNr].equals("-n"))
							{
								argNr++;
								deterNr = Integer.parseInt(args[argNr++]);
							}
						}
						players[i] = new DeterminizationPlayer(new AlphaBeta.Factory(i, depth), deterNr);
						break;
						
					case ISMTCS:
						if (argNr < args.length && args[argNr].equals("-i"))
						{
							argNr++;
							iter = Integer.parseInt(args[argNr++]);
						}
						players[i] = new PartialInfoPlayer(new InformationSetMCTS(i, iter));
						break;
						
					case MCTS:
						for (int j = 0; j < 2; j++)
						{
							if (args[argNr].equals("-i"))
							{
								argNr++;
								iter = Integer.parseInt(args[argNr++]);
							}
							if (args[argNr].equals("-n"))
							{
								argNr++;
								deterNr = Integer.parseInt(args[argNr++]);
							}
						}
						players[i] = new DeterminizationPlayer(new MonteCarloTreeSearch.Factory(i, iter), deterNr);
						break;
						
					case MCTS_CHEATING:
						if (args[argNr].equals("-i"))
						{
							argNr++;
							iter = Integer.parseInt(args[argNr++]);
						}
						players[i] = new CheatingPlayer(new MonteCarloTreeSearch(i, iter));
						break;
						
					case RANDOM:
						players[i] = new PartialInfoPlayer(new RandWalk(i));
						break;
						
					default:
						break;
				}
			}
			return players;	
		}
		catch (Exception e)
		{
			
//				if (argNr >= args.length - 1)
//				{
//					System.out.println("Please provide " + aiNr + " AIs\n");
//					printHelp();
//					return null;
//					
//				}
			
			System.out.println("Invalid argument \"" + args[argNr - 1] + "\"\n");
			printHelp();
			return null;
		}
	}
}

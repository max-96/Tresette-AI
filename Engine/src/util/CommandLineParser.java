package util;

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
		
		int aiNr = forTest ? 4 : 3;
		Player[] players = new Player[aiNr];
		int argNr = 0;
		
		try
		{
			for (int i = 0; i < aiNr; i++)
			{
				AI ai = AI.valueOf(args[argNr]);
				
				int id = forTest ? i : i + 1;
				int depth = 1;
				int iter = 1;
				int deterNr = 1;
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
			return players;	
		}
		catch (Exception e)
		{
			if (argNr == args.length)
				System.out.println("Please provide " + aiNr + " AIs\n");
			else
				System.out.println("Invalid argument \"" + args[argNr] + "\"\n");
			printHelp();
			return null;
		}
	}
}

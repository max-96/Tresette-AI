package util;

import AI.CheatingPlayer;
import AI.PartialInfoPlayer;
import AI.RandWalk;
import minmax.AlphaBeta;
import setting.Player;

public class CommandLineParser
{
	private enum AI
	{
		RANDOM, AB_CHEATING, MCTS_CHEATING, ALPHABETA, MCTS, ISMTCS
	}
	
	public Player[] parseArgs(String[] args)
	{
		Player[] players = new Player[4];
		
		try
		{
			for (int i = 0; i < 4; i++)
			{
				int argNr = 0;
				AI ai = AI.valueOf(args[argNr++]);
				switch (ai)
				{
					case AB_CHEATING:
						int depth = 0;
						if (args[argNr + 1].equals("-d"))
						{
							argNr += 2;
							depth = Integer.parseInt(args[argNr]);
						}
						players[i] = new CheatingPlayer(new AlphaBeta(i, depth));
						break;
						
					case ALPHABETA:
						break;
					case ISMTCS:
						break;
					case MCTS:
						break;
					case MCTS_CHEATING:
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
			return null;
		}
	}
}

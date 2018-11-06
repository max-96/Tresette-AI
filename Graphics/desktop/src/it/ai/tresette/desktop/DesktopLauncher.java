package it.ai.tresette.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import it.ai.tresette.TreSette;
import it.ai.tresette.objects.Constants;
import it.ai.tresette.player.AIPlayer;
import it.ai.tresette.player.HumanPlayer;
import it.ai.tresette.player.Player;
import util.CommandLineParser;

public class DesktopLauncher
{
	public static void main (String[] args)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		
		//la grandezza della finestra
		config.height = Constants.WINDOW_HEIGHT;
		config.width = Constants.WINDOW_WIDTH;
		
		//non puo essere modificata
		config.resizable = false;
		
		Player[] players = new Player[4];
		players[0] = new HumanPlayer(0);
		
		CommandLineParser clp = new CommandLineParser(false);
		setting.Player[] aiPlayers = clp.parseArgs(args);
		
		if (aiPlayers == null)
			return;
		
		for (int i = 0; i < 3; i++)
			players[i  + 1] = new AIPlayer(aiPlayers[i]);
		
		new LwjglApplication(new TreSette(players), config);
	}
}

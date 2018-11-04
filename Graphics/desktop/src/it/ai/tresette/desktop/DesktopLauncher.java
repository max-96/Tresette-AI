package it.ai.tresette.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import AI.PartialInfoPlayer;
import AI.RandWalk;
import it.ai.tresette.TreSette;
import it.ai.tresette.objects.Constants;
import it.ai.tresette.player.AIPlayer;
import it.ai.tresette.player.HumanPlayer;
import it.ai.tresette.player.Player;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		
		//la grandezza della finestra
		config.height = Constants.WINDOW_HEIGHT;
		config.width = Constants.WINDOW_WIDTH;
		
		//non puo essere modificata
		config.resizable = false;
		
		Player[] players = new Player[4];
		players[0] = new HumanPlayer(0);
		players[1] = new AIPlayer(1, new PartialInfoPlayer(new RandWalk(1)));
		players[2] = new AIPlayer(2, new PartialInfoPlayer(new RandWalk(2)));
		players[3] = new AIPlayer(3, new PartialInfoPlayer(new RandWalk(3)));
		
		new LwjglApplication(new TreSette(players), config);
	}
}

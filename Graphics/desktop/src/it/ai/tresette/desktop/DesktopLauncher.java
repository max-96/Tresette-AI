package it.ai.tresette.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import AI.DeterminizationPlayer;
import AI.PartialInfoPlayer;
import AI.RandWalk;
import MCTS.MonteCarloTreeSearch;
import ismcts.InformationSetMCTS;
import it.ai.tresette.TreSette;
import it.ai.tresette.objects.Constants;
import it.ai.tresette.player.AIPlayer;
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
		players[0] = new AIPlayer(new DeterminizationPlayer(0, new MonteCarloTreeSearch.Factory(2000, 0.75), 10));
		players[1] = new AIPlayer(new PartialInfoPlayer(new RandWalk(1)));
		players[2] = new AIPlayer(new DeterminizationPlayer(2, new MonteCarloTreeSearch.Factory(2000, 0.75), 10));
		players[3] = new AIPlayer(new PartialInfoPlayer(new RandWalk(3)));
		
		new LwjglApplication(new TreSette(players), config);
	}
}

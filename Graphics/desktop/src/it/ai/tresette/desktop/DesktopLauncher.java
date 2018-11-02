package it.ai.tresette.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import it.ai.tresette.TreSette;
import it.ai.tresette.objects.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.foregroundFPS = 60;
		
		//la grandezza della finestra
		config.height = Constants.WINDOW_HEIGHT;
		config.width = Constants.WINDOW_WIDTH;
		
		//non puo essere modificata
		config.resizable = false;
		new LwjglApplication(new TreSette(), config);
	}
}

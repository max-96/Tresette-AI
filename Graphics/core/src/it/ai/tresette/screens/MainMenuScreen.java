package it.ai.tresette.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;

import it.ai.tresette.TreSette;
import it.ai.tresette.objects.Constants;

public class MainMenuScreen implements Screen{


	
	
	/**
	 * calcoliamo l'altezza di ogni bottone dividendo l'altezza della finestra per 6;
	 * il primo si trovera quindi ad 1/6 altezza moltiplicato per 4;
	 * per trovare le y degli altri due bottoni semplicemente lo abbassiamo di un fattore 1.5
	 */
	
	
	TreSette game;
	
	Texture settings;
	Texture settings_ina;
	Texture exit_game;
	Texture exit_game_ina;
	Texture start_game;
	Texture start_game_ina;
	
	public MainMenuScreen(TreSette game)
	{
		settings = new Texture("mainMenu/settings.png");
		settings_ina = new Texture("mainMenu/settings_ina.png");
		exit_game = new Texture("mainMenu/exit_game.png");
		exit_game_ina = new Texture("mainMenu/exit_game_ina.png");
		start_game = new Texture("mainMenu/start_game.png");
		start_game_ina = new Texture("mainMenu/start_game_ina.png");
		
		this.game = game;
	}
	@Override
	public void show() {
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0.85f, 0.85f, 0.85f, 1);	//grey 
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		game.batch.begin();
		if(isInStartButtonArea())
		{
			game.batch.draw(start_game, Constants.WINDOW_WIDTH/2 - Constants.START_BUTTON_WIDTH/2,Constants.START_BUTTON_Y,Constants.START_BUTTON_WIDTH,Constants.START_BUTTON_HEIGHT);
			if(Gdx.input.isTouched())
			{
				this.dispose();
				game.setScreen(new MainGameScreen(game));
			}
		}
		else 
			game.batch.draw(start_game_ina,Constants.WINDOW_WIDTH/2 - Constants.START_BUTTON_WIDTH/2,Constants.START_BUTTON_Y,Constants.START_BUTTON_WIDTH,Constants.START_BUTTON_HEIGHT);
		if(isInSettingsButtonArea())
			game.batch.draw(settings, Constants.WINDOW_WIDTH/2 - Constants.SETTINGS_BUTTON_WIDTH/2, Constants.SETTINGS_BUTTON_Y,Constants.START_BUTTON_WIDTH,Constants.START_BUTTON_HEIGHT);
		else
			game.batch.draw(settings_ina, Constants.WINDOW_WIDTH/2 - Constants.SETTINGS_BUTTON_WIDTH/2, Constants.SETTINGS_BUTTON_Y,Constants.START_BUTTON_WIDTH,Constants.START_BUTTON_HEIGHT);
		if(isInExitButtonArea())
			{
				game.batch.draw(exit_game, Constants.WINDOW_WIDTH/2 - Constants.EXIT_BUTTON_WIDTH/2,Constants.EXIT_BUTTON_Y,Constants.START_BUTTON_WIDTH,Constants.START_BUTTON_HEIGHT);
				if(Gdx.input.isTouched())
					Gdx.app.exit();
			}
		else
			game.batch.draw(exit_game_ina, Constants.WINDOW_WIDTH/2 - Constants.EXIT_BUTTON_WIDTH/2,Constants.EXIT_BUTTON_Y,Constants.START_BUTTON_WIDTH,Constants.START_BUTTON_HEIGHT);
		
		game.batch.end();
		
	}

	@Override
	public void resize(int width, int height) {

	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {

	}

	@Override
	public void dispose() {
		//TO DO
	}
	
	private boolean isInExitButtonArea()
	{
		if(Gdx.input.getX() >= Constants.WINDOW_WIDTH/2 - Constants.EXIT_BUTTON_WIDTH/2 && Gdx.input.getX()<= Constants.WINDOW_WIDTH/2 + Constants.EXIT_BUTTON_WIDTH/2)
			if(Constants.WINDOW_HEIGTH - Gdx.input.getY() >= Constants.EXIT_BUTTON_Y && Constants.WINDOW_HEIGTH - Gdx.input.getY() <= Constants.EXIT_BUTTON_Y + Constants.EXIT_BUTTON_HEIGHT)
				return true;
		return false;
	}
	private boolean isInStartButtonArea()
	{
		if(Gdx.input.getX() >= Constants.WINDOW_WIDTH/2 - Constants.START_BUTTON_WIDTH/2 && Gdx.input.getX()<= Constants.WINDOW_WIDTH/2 + Constants.START_BUTTON_WIDTH/2)
			if(Constants.WINDOW_HEIGTH - Gdx.input.getY() >= Constants.START_BUTTON_Y && Constants.WINDOW_HEIGTH - Gdx.input.getY() <= Constants.START_BUTTON_Y + Constants.START_BUTTON_HEIGHT)
				return true;
		return false;
	}
	private boolean isInSettingsButtonArea()
	{
		if(Gdx.input.getX() >= Constants.WINDOW_WIDTH/2 - Constants.SETTINGS_BUTTON_WIDTH/2 && Gdx.input.getX()<= Constants.WINDOW_WIDTH/2 + Constants.SETTINGS_BUTTON_WIDTH/2)
			if(Constants.WINDOW_HEIGTH - Gdx.input.getY() >= Constants.SETTINGS_BUTTON_Y && Constants.WINDOW_HEIGTH - Gdx.input.getY() <= Constants.SETTINGS_BUTTON_Y + Constants.SETTINGS_BUTTON_HEIGHT)
				return true;
		return false;
	}
	

}

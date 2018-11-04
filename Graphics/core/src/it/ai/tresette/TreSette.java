package it.ai.tresette;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.ai.tresette.player.Player;
import it.ai.tresette.screens.MainMenuScreen;

public class TreSette extends Game {
	
	public static final int WINDOW_WIDTH = 1280;
	public static final int WINDOW_HEIGTH = 720;
	
	private Player[] players;
	
	public TreSette(Player... players)
	{
		this.players = players;
	}
	
	public Player[] getPlayers()
	{
		return this.players;
	}
	
	public SpriteBatch batch;
	@Override
	public void create () {
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
		
	}

	@Override
	public void render () {
		super.render();	
	}
	
	/*@Override
	public void dispose () {
		batch.dispose();
		
	}*/
}

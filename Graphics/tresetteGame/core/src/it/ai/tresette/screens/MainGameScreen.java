package it.ai.tresette.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import it.ai.tresette.GameManager;
import it.ai.tresette.TreSette;

public class MainGameScreen implements Screen{

	
	Texture img;
	
	TreSette game;
	
	GameManager table;
	
	public MainGameScreen(TreSette game)
	{
		this.game = game;
		this.table = new GameManager();
	}
	
	
	@Override
	public void show() {
		img = new Texture("badlogic.jpg");
		
	}

	@Override
	public void render(float delta) {
		
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		this.table.run();						//update logico
		
		
		
		game.batch.begin();  
		game.batch.draw(img, 0, 0);
		this.table.draw(game.batch);			//update draw
		
		
		game.batch.end();
		System.out.println("ok");
		
	}
	
	

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

}

package com.t3.ld40;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.t3.ld40.screen.GameScreen;

public class Main extends Game {

	private GameScreen gameScreen;
	private Assets assets;
	@Override
	public void create () {
		Gdx.app.setLogLevel(Gdx.app.LOG_DEBUG);
		assets = new Assets();
		gameScreen = new GameScreen(assets);

		setScreen(gameScreen);
	}

	@Override
	public void render () {
		super.render();
	}
	
	@Override
	public void dispose () {

	}
}

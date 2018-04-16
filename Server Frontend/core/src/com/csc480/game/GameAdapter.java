package com.csc480.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.GUI.GameScreen;


/**
 * Handles high level application features such as resizing,
 * 	gaining and losing focus, and creation/destruction of the
 * 	application as a whole.
 */
public class GameAdapter extends ApplicationAdapter {
	OswebbleGame oswebbleGame;

	@Override
	public void create () {
		oswebbleGame = new OswebbleGame();
		oswebbleGame.create();
		GameManager.getInstance().theGame = oswebbleGame;
	}

	@Override
	public void render () {
		oswebbleGame.render();
	}
	
	@Override
	public void dispose () {
		oswebbleGame.dispose();
		GameManager.getInstance().Dispose();
	}
}

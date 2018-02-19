package com.csc480.game;

import com.badlogic.gdx.ApplicationAdapter;


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
	}

	@Override
	public void render () {
		oswebbleGame.render();
	}
	
	@Override
	public void dispose () {
		oswebbleGame.dispose();
	}
}

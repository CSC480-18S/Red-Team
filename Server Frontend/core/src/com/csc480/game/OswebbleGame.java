package com.csc480.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.csc480.game.GUI.GameScreen;

/**
 * The Game class has slightly more overhead when changing screens,
 *  but simplifies switching between multiple screens
 *  !!Screens are not disposed automatically. You must handle whether you want to keep
 *          screens around or dispose of them when another screen is set.!!
 */
public class OswebbleGame extends Game {
    public static final int BOARD_SIZE = 11;
    public SpriteBatch batch;
    public SpriteBatch fontBatch;

    GameScreen theGameScreen;


    /**
     * Instantiate the game
     */
    @Override
    public void create () {
        Gdx.graphics.setTitle("Oswebble");
        batch = new SpriteBatch();
        fontBatch = new SpriteBatch();
        theGameScreen = new GameScreen(this);

        //set the screen that the game will use as
        //  the top level view
        setScreen(theGameScreen);
    }

    /**
     * Dont touch this
     */
    @Override
    public void render () {
       super.render();
    }

    /**
     * Clear up resources
     */
    @Override
    public void dispose () {
        batch.dispose();
        fontBatch.dispose();
    }
}

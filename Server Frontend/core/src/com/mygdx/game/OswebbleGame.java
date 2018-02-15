package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.GUI.GameScreen;

/**
 * The Game class has slightly more overhead when changing screens,
 *  but simplifies switching between multiple screens
 *  !!Screens are not disposed automatically. You must handle whether you want to keep
 *          screens around or dispose of them when another screen is set.!!
 */
public class OswebbleGame extends Game {
    public SpriteBatch batch;

    GameScreen theGameScreen;


    @Override
    public void create () {
        batch = new SpriteBatch();
        theGameScreen = new GameScreen(this);

        //set the screen that the game will use as
        //  the top level view
        setScreen(theGameScreen);
    }

    @Override
    public void render () {
       super.render();
    }

    @Override
    public void dispose () {
        batch.dispose();
    }
}

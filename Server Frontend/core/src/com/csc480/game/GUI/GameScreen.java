package com.csc480.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.csc480.game.OswebbleGame;

/**
 * A bare bones example of a screen
 * The OswebbleGame is passed as a parameter to the constructor
 *  so that the screen can change screens if need be
 */
public class GameScreen implements Screen {
    OswebbleGame oswebbleGame;
    Texture img;

    public GameScreen(OswebbleGame mainGame){
        oswebbleGame = mainGame;
        img = new Texture(Gdx.files.internal("badlogic.jpg"));

    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(1, 1, 1, 1);
        oswebbleGame.batch.begin();
        oswebbleGame.batch.draw(img, 0, 0);
        oswebbleGame.batch.end();
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
        img.dispose();
    }
}

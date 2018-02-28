package com.csc480.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.TestingInputProcessor;
import com.csc480.game.GUI.Actors.GameBoardActor;
import com.csc480.game.OswebbleGame;

/**
 * A bare bones example of a screen
 * The OswebbleGame is passed as a parameter to the constructor
 *  so that the screen can change screens if need be
 */
public class GameScreen implements Screen {
    public static final float TILE_PIXEL_SIZE = 8f;
    public static final int BOARD_SIZE = 11;
    //tiledmaps need the size of 1/tile_pixels to render properly
    float unitScale = 1/TILE_PIXEL_SIZE;
    Stage stage;
    OswebbleGame oswebbleGame;
    TiledMap theGameBoard;
    OrthogonalTiledMapRenderer renderer;
    OrthographicCamera camera;
    public BitmapFont font;

    GameBoardActor gba;


    public GameScreen(OswebbleGame mainGame){
        //can use this to calculate the aspect ratio
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
//THIS SIZE IS ONLY LARGE ENOUGH FOR THE BOARD ITSELF, AND DOESNT RESCALE
        camera = new OrthographicCamera(OswebbleGame.BOARD_SIZE, OswebbleGame.BOARD_SIZE);
        camera.position.x+=OswebbleGame.BOARD_SIZE/2.0f;
        camera.position.y+=OswebbleGame.BOARD_SIZE/2.0f;
        camera.update();

        //we might want to not use bitmapfonts, as they can get blurry
        //see https://github.com/libgdx/libgdx/wiki/Gdx-freetype for a better solution
        font = new BitmapFont();
        font.setColor(0,0,0,1);
        font.getData().setScale(1/16f);
        font.setUseIntegerPositions(false);

        //set the SceneGraph stage
        stage = new Stage(new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()));

        oswebbleGame = mainGame;

        gba = new GameBoardActor();
        gba.setBounds(0,0,100,100);
        stage.addActor(gba);
        //just for testing
        gba.rotateBy(45f);

        GameBoardActor gba2 = new GameBoardActor();
        gba2.setBounds(0,0,100,100);
        stage.addActor(gba2);
    }

    @Override
    public void show() {
        //THIS WILL BE REMOVED WHEN SOCKETS ARE IMPLEMENTED
        Gdx.input.setInputProcessor(new TestingInputProcessor(camera));
    }

    /**
     * Remember that things render back to front, so
     *      the first thing you render will be at the bottomost layer
     * @param delta how much time has passed
     */
    @Override
    public void render(float delta) {
        //Clear the screen from the last frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Set the entire screen to this color
        Gdx.gl.glClearColor(0, 0, 0, 1);
        //perform the actions of the actors
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width,height,true);
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


    }
}

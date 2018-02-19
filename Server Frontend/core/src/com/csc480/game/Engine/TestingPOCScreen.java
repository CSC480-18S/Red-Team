package com.csc480.game.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;


/**
 * This is just a POC game screen to aid in testing the feasibility of the engine's algorithms.
 */
public class TestingPOCScreen implements com.badlogic.gdx.Screen {
    public static final int MAX_NUM_PLACEMENTS = 7;
    public Board board;
    public OrthographicCamera theCamera;

    public ArrayList<Placement> placements;
    public SpriteBatch fontBatch;
    public BitmapFont font;

    public TestingPOCScreen(int size){
        board = new Board(size);
        placements = new ArrayList<Placement>();
        theCamera = new OrthographicCamera();
        theCamera.setToOrtho(false,size+0.0f,size+0.0f);
        font = new BitmapFont();
        fontBatch = new SpriteBatch();
        font.getData().setScale(1/16f);
        font.setUseIntegerPositions(false);
    }





    @Override
    public void show() {
        //THIS WILL CHANGE WITH ACTUAL IMPLEMENTATION
        Gdx.input.setInputProcessor(new TestingInputProcessor(this));
    }

    @Override
    public void render(float delta) {
        //THE REALLY HACKY PART THAT WONT GO IN THE IMPLEMENTATION
        theCamera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        Gdx.gl.glClearColor(0, 1, 0, 1);
        fontBatch.setProjectionMatrix(theCamera.combined);
        fontBatch.begin();
        for(int i = 0; i < board.the_game_board.length; i++){
            for(int j = 0; j < board.the_game_board[0].length; j++){
                if(board.the_game_board[i][j] != null)
                    font.draw(fontBatch,board.the_game_board[i][j].letter+"",
                            (theCamera.position.x-(theCamera.viewportWidth / 2))+i,
                            (theCamera.position.y-(theCamera.viewportHeight / 2)+1)+j);
                else
                    font.draw(fontBatch,".",
                            (theCamera.position.x-(theCamera.viewportWidth / 2))+i,
                            (theCamera.position.y-(theCamera.viewportHeight / 2)+1)+j);
            }
        }
        fontBatch.end();


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
        fontBatch.dispose();

    }
}

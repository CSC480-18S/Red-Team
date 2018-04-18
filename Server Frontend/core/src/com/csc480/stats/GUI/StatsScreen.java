package com.csc480.stats.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csc480.game.GUI.GameScreen;
import com.csc480.stats.GUI.Actors.StatsTables;
import com.csc480.stats.StatsViewer;

public class StatsScreen implements Screen {
    private StatsViewer container;
    float aspectRatio;
    //Scene2D stuff and Actors we need to keep references to
    Stage stage;
    private Viewport view;
    private OrthographicCamera viewCam;

////////////////////////////////////
    private SpriteBatch batch;
    private Sprite sprite;
///////////////////////////////

    public StatsScreen(StatsViewer belongsTo){
        container = belongsTo;
        aspectRatio = (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        System.out.println("aspectRatio: "+aspectRatio);

        //set the SceneGraph stage
        viewCam = new OrthographicCamera();
       // view = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()*aspectRatio, viewCam);
       // view = new FitViewport(GameScreen.GUI_UNIT_SIZE * 45, GameScreen.GUI_UNIT_SIZE *45 * aspectRatio, viewCam);
        //view = new FitViewport(GameScreen.GUI_UNIT_SIZE * 37, GameScreen.GUI_UNIT_SIZE *37 * aspectRatio, viewCam);
        view = new FitViewport(GameScreen.GUI_UNIT_SIZE * 37, GameScreen.GUI_UNIT_SIZE *37 * aspectRatio, viewCam);
        view.apply();
        stage = new Stage(view);

//////////////////////////////////////////////////////////////////////////
        batch = new SpriteBatch();
        //sprite = new Sprite(new Texture(Gdx.files.internal("StatsAssets/statsBackground.png")));
        sprite = new Sprite(new Texture(Gdx.files.internal("Background.jpg")));
       // sprite.setSize(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        //sprite.setSize(,Gdx.graphics.getHeight());

        StatsTables table  = new StatsTables();
        //table.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        table.setWidth(GameScreen.GUI_UNIT_SIZE * 37);
        table.setHeight(GameScreen.GUI_UNIT_SIZE * 37 *aspectRatio);
        //table.setDebug(true);

        stage.addActor(table);
///////////////////////////////////////////////////////////////////////



    }
    @Override
    public void show() {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
    }

    @Override
    public void render(float delta) {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        //Clear the screen from the last frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Set the entire screen to this color
        Gdx.gl.glClearColor(.17f, .17f, .17f, 1);
        //perform the actions of the actors
        /////////////////////////////
        batch.begin();
        sprite.draw(batch);
        batch.end();
        ////////////////////////////
        stage.act(delta);
        //render the actors
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
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
        stage.dispose();
    }
}

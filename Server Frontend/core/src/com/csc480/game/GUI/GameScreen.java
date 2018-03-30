package com.csc480.game.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.actions.RotateByAction;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.csc480.game.Engine.TestingInputProcessor;
import com.csc480.game.GUI.Actors.*;
import com.csc480.game.OswebbleGame;

/**
 * This class will serve as the meat of the GUI. One can do all the nessisary GUI actions, layouts, and displays here.
 * If you want to use another screen, you can but please speak with Chirstopher King before adding one if you dont
 * know what youre doing.
 * The OswebbleGame is passed as a parameter to the constructor
 *  so that the screen can change screens if need be
 */
public class GameScreen implements Screen {
    public static final float TILE_PIXEL_SIZE = 512f;
    public static final float GUI_UNIT_SIZE = 20f;
    public static final int BOARD_SIZE = 11;
    public static float aspectRatio;
    public BitmapFont font;
    OswebbleGame oswebbleGame;
    public final HandActor top,bottom,left,right;
    public final InfoPanelActor infoPanel;

    float unitScale = 1/TILE_PIXEL_SIZE;

    //Scene2D stuff and Actors we need to keep references to
    Stage stage;
    private Viewport view;
    private  OrthographicCamera viewCam;
    GameBoardActor gameBoardActor;


    public GameScreen(OswebbleGame mainGame){
        //must calculate the aspect ratio to resize properly
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        System.out.println("aspectRatio: "+aspectRatio);

        //we probably dont want to use bitmapfonts, as they can get blurry
        //see https://github.com/libgdx/libgdx/wiki/Gdx-freetype for a better solution
        font = new BitmapFont();
        font.setColor(0,0,0,1);
        font.getData().setScale(1/16f);
        font.setUseIntegerPositions(false);

        //set the SceneGraph stage
        viewCam = new OrthographicCamera();
        view = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()*aspectRatio, viewCam);
        view.apply();
        stage = new Stage(view);

        oswebbleGame = mainGame;


        //Define all the Actors
        Group playArea = new Group();

        //gameBoardActor = new GameBoardActor();
        GameBoardTable board = new GameBoardTable();
        board.setName("gameBoard");
        board.setPosition(GUI_UNIT_SIZE*2.75f,GUI_UNIT_SIZE*2.25f);
        //gameBoardActor.setBounds(0,0,100,100);

        //gameBoardActor.setName("gameBoard");
//THIS IS FOR TESTING ONLY //////////////////////////////////////////////////////////////
        //stage.setKeyboardFocus(gameBoardActor);
////////////////////////////////////////////////////////////////////////////////////////
        //playArea.addActor(gameBoardActor);

        //The position of actors is considered from their bottom left corner
        //bottom
        Group tileRacks = new Group();
        tileRacks.setPosition(GUI_UNIT_SIZE * 1, GUI_UNIT_SIZE * 1);
        bottom = new HandActor(false);
        tileRacks.addActor(bottom);
        bottom.setPosition(GUI_UNIT_SIZE * 2, GUI_UNIT_SIZE * 0);
        //left
        left = new HandActor(false);
        tileRacks.addActor(left);
        left.setPosition(GUI_UNIT_SIZE * 12, GUI_UNIT_SIZE * 2);
        left.rotateBy(90);
        //top
        top = new HandActor(true);
        tileRacks.addActor(top);
        top.setPosition(GUI_UNIT_SIZE * 10, GUI_UNIT_SIZE * 12);
        top.rotateBy(180);
        //right
        right = new HandActor(false);
        tileRacks.addActor(right);
        right.setPosition(GUI_UNIT_SIZE * 0, GUI_UNIT_SIZE * 10);
        right.rotateBy(-90);
        tileRacks.moveBy(-GUI_UNIT_SIZE/2,-GUI_UNIT_SIZE);
        tileRacks.scaleBy(.1f);
        playArea.addActor(tileRacks);

        infoPanel = new InfoPanelActor();
        infoPanel.moveBy(GameScreen.GUI_UNIT_SIZE*14,0);
        playArea.addActor(infoPanel);
        System.out.println(board.getChildren().size);
        playArea.addActor(board);
        playArea.scaleBy(GUI_UNIT_SIZE * .04f);//had to do this because i originally tested all the sizes at a lower dpi

        stage.addActor(playArea);



    }

    @Override
    public void show() {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
//THIS WILL BE REMOVED WHEN SOCKETS ARE IMPLEMENTED///////////////////////////////////////////////////////
        Gdx.input.setInputProcessor(new TestingInputProcessor(viewCam));
//////////////////////////////////////////////////////////////////////////////////////////////////////////
    }

    /**
     * Remember that things render back to front, so
     *      the first thing you render will be at the bottomost layer
     * @param delta how much time has passed
     */
    @Override
    public void render(float delta) {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        //Clear the screen from the last frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Set the entire screen to this color
        Gdx.gl.glClearColor(.17f, .17f, .17f, 1);
        //perform the actions of the actors
        stage.act(delta);
        //render the actors
        stage.draw();

    }

    @Override
    public void resize(int width, int height) {
        //This still isnt working correctly, but the libgdx interface should never have to resize
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
        font.dispose();
    }
}

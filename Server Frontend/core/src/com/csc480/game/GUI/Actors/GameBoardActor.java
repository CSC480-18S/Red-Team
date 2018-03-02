package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.GUI.GameScreen;
import com.csc480.game.Engine.TiledUtils;
import com.csc480.game.OswebbleGame;

import java.util.ArrayList;

/**
 * This class holds the actual game board TiledMap. Unlike normal Actors, this class's functionaliy
 * is some what limited due to TiledMaps requiring their own camera, one can @Override more methods
 * to simulate what a normal actor would do, but doing so may require some linear algebra.
 */
public class GameBoardActor extends Actor {
    TiledMap theGameBoard;
    OrthographicCamera boardCam;
    OrthogonalTiledMapRenderer renderer;

    //This will handle the events once implemented
    EventListener myEventListner = new EventListener() {
        @Override
        public boolean handle(Event event) {
            return false;
        }
    };

    /**
     * Creates and load a TiledMap
     */
    public GameBoardActor(){
        super();
        setBounds(getX(),getY(),getWidth(),getHeight());
        addListener(myEventListner);
        theGameBoard = new TmxMapLoader().load("BoardTilemap.tmx");
        renderer = new OrthogonalTiledMapRenderer(theGameBoard,1/ GameScreen.TILE_PIXEL_SIZE);

        boardCam = new OrthographicCamera(OswebbleGame.BOARD_SIZE*2, OswebbleGame.BOARD_SIZE*2);

        boardCam.setToOrtho(false, OswebbleGame.BOARD_SIZE*(2*((float)Gdx.graphics.getWidth()/(float) Gdx.graphics.getHeight())),OswebbleGame.BOARD_SIZE*2);
        boardCam.translate(-4.75f,-4.75f);
        boardCam.update();
//FOR TESTING ONLY REMOVE ONCE DONE TESTING///////////////////////////////////////////////////
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('t',5,6));
        testing.add(new Placement('e',5,5));
        testing.add(new Placement('s',5,4));
        testing.add(new Placement('t',5,3));

        GameManager.getInstance().theBoard.addWord(testing);
        GameManager.getInstance().placementsUnderConsideration.add(new Placement('t',4,5));
//////////////////////////////////////////////////////////////////////////////////////////////

    }

    @Override
    public void rotateBy(float s){
        boardCam.rotate(s);
        boardCam.update();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        //probably best not to mess with this unless you know what youre doing.
        renderer.setView(boardCam.combined, 0,0,GameScreen.BOARD_SIZE,GameScreen.BOARD_SIZE);
        TiledMapTileLayer playedLayer = TiledUtils.generatePlacementsTiledLayer(GameManager.getInstance().theBoard);
        renderer.getMap().getLayers().add(playedLayer);
        renderer.render();
        renderer.getMap().getLayers().remove(playedLayer);

        batch.begin();
    }
}

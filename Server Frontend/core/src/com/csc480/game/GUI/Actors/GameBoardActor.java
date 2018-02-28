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
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.GUI.GameScreen;
import com.csc480.game.Engine.TiledUtils;
import com.csc480.game.OswebbleGame;

import java.util.ArrayList;

public class GameBoardActor extends Actor {
    TiledMap theGameBoard;
    OrthographicCamera boardCam;
    OrthogonalTiledMapRenderer renderer;

    //This will handle the events
    EventListener myEventListner = new EventListener() {
        @Override
        public boolean handle(Event event) {
            return false;
        }
    };

    public GameBoardActor(){
        super();
        setBounds(getX(),getY(),getWidth(),getHeight());
        addListener(myEventListner);
        theGameBoard = new TmxMapLoader().load("POCBoard.tmx");
        renderer = new OrthogonalTiledMapRenderer(theGameBoard,1/ GameScreen.TILE_PIXEL_SIZE);

        boardCam = new OrthographicCamera(OswebbleGame.BOARD_SIZE*2, OswebbleGame.BOARD_SIZE*2);
        boardCam.setToOrtho(false, OswebbleGame.BOARD_SIZE*2,OswebbleGame.BOARD_SIZE*2);
       // boardCam = cam;
        boardCam.position.x=OswebbleGame.BOARD_SIZE/2.0f;
        boardCam.position.y=OswebbleGame.BOARD_SIZE/2.0f;
        boardCam.update();

        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('t',5,6));
        testing.add(new Placement('e',5,5));
        testing.add(new Placement('s',5,4));
        testing.add(new Placement('t',5,3));

        GameManager.getInstance().theBoard.addWord(testing);
        GameManager.getInstance().placementsUnderConsideration.add(new Placement('t',4,5));

    }
    @Override
    public void rotateBy(float s){
        boardCam.rotate(s);
        boardCam.update();
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        //renderer.setView(boardCam);
        renderer.setView(boardCam.combined, getX(),getY(),getWidth(),getHeight());
        TiledMapTileLayer playedLayer = TiledUtils.generatePlacementsTiledLayer(GameManager.getInstance().theBoard);
        renderer.getMap().getLayers().add(playedLayer);
        renderer.render();
        renderer.getMap().getLayers().remove(playedLayer);
    }
}

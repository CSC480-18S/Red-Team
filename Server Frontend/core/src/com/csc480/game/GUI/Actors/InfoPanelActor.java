package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.csc480.game.Engine.TextureManager;
import com.csc480.game.GUI.GameScreen;

import java.util.ArrayList;

public class InfoPanelActor extends Group{
    private List<String> eventLog;
    private Array<String> logOfEvents;
    public InfoPanelActor(){
        super();
        setWidth(GameScreen.GUI_UNIT_SIZE * 10);
        setHeight(GameScreen.GUI_UNIT_SIZE* 13);
        setPosition(0,0);
        Table myLayout = new Table();
        myLayout.setHeight(getHeight());
        myLayout.setWidth(getWidth());
        myLayout.setDebug(true);
        myLayout.top();
        //myLayout.setPosition(getWidth()/2, getHeight()/2);


        Image bg = new Image(TextureManager.getInstance().background);
        bg.setScale(.25f,.4f);
        bg.setPosition(0, 0);
        //bg.setWidth(getWidth());
        //bg.setHeight(getHeight());
        myLayout.background(bg.getDrawable());

        Label title = new Label("Scores", TextureManager.getInstance().ui, "default");
        title.setColor(Color.BLACK);
        title.setName("title");
        //title.setPosition(GameScreen.GUI_UNIT_SIZE*4,GameScreen.GUI_UNIT_SIZE*12);
        myLayout.add(title).expandX();
        myLayout.row();
        //addActor(title);
        ProgressBar green = new ProgressBar(0,100.0f,1.0f,false,TextureManager.getInstance().ui, "default-horizontal");
        //green.setWidth(getWidth());
        myLayout.add(green).fillX().padLeft(10f).padRight(10f);;


        myLayout.row();
        green.setValue(50.0f);//todo remove this//////////////////////////////////////////

        ProgressBar gold = new ProgressBar(0,250,1,false,TextureManager.getInstance().ui, "default-horizontal");
        green.setValue(10);//todo remove this////////////////////////////////////////////
        gold.setWidth(getWidth());
        myLayout.add(gold).fillX().padLeft(10f).padRight(10f);
        myLayout.row();

        logOfEvents = new Array<String>();
        eventLog = new List<String>(TextureManager.getInstance().ui, "default");
        eventLog.setPosition(GameScreen.GUI_UNIT_SIZE, GameScreen.GUI_UNIT_SIZE*5);
        eventLog.setWidth(GameScreen.GUI_UNIT_SIZE*10);
        myLayout.add(eventLog).fillX().padLeft(10f).padRight(10f).padBottom(10f);
        //addActor(eventLog);


        addActor(myLayout);


    }






    public void LogEvent(String event){
        if(logOfEvents.size >= 4){
            logOfEvents.removeIndex(0);
        }
        logOfEvents.add(event);
        eventLog.setItems(logOfEvents);
        eventLog.setSelectedIndex(logOfEvents.size-1);
        //eventLog.getItems().add(event);

    }
}

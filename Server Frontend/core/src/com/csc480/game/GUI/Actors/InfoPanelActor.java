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

    private Label p0Name, p0Score;
    private Label p1Name, p1Score;
    private Label p2Name, p2Score;
    private Label p3Name, p3Score;
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
        myLayout.add(title).expandX().padTop(10f);
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


        Table sublayout = new Table();
        sublayout.setDebug(true);
        p0Name = new Label("player0", TextureManager.getInstance().ui, "default");
        p0Score = new Label("0", TextureManager.getInstance().ui, "default");
        sublayout.add(p0Name).padLeft(10f).left().expandX().minWidth(150f);
        sublayout.add(p0Score).padRight(10f).left();
        sublayout.row();

        p1Name = new Label("player1", TextureManager.getInstance().ui, "default");
        p1Score = new Label("10", TextureManager.getInstance().ui, "default");
        sublayout.add(p1Name).padLeft(10f).left().expandX().minWidth(150f);
        sublayout.add(p1Score).padRight(10f).left();
        sublayout.row();

        p2Name = new Label("player2", TextureManager.getInstance().ui, "default");
        p2Score = new Label("100", TextureManager.getInstance().ui, "default");
        sublayout.add(p2Name).padLeft(10f).left().expandX().minWidth(150f);
        sublayout.add(p2Score).padRight(10f).left();
        sublayout.row();

        p3Name = new Label("player3333", TextureManager.getInstance().ui, "default");
        p3Score = new Label("1000", TextureManager.getInstance().ui, "default");
        sublayout.add(p3Name).padLeft(10f).left().expandX().minWidth(150f);
        sublayout.add(p3Score).padRight(10f).left();
        myLayout.add(sublayout);
        myLayout.row();



        logOfEvents = new Array<String>();
        eventLog = new List<String>(TextureManager.getInstance().ui, "default");
        eventLog.setPosition(GameScreen.GUI_UNIT_SIZE, GameScreen.GUI_UNIT_SIZE*5);
        eventLog.setWidth(GameScreen.GUI_UNIT_SIZE*10);
        myLayout.add(eventLog).fillX().padLeft(10f).padRight(10f).padBottom(10f);

        addActor(myLayout);
    }

    public void UpdatePlayerStatus(int position, String name, int score){
        switch (position){
            case 0:
                p0Name.setText(name);
                p0Score.setText(score+"");
                break;
            case 1:
                p1Name.setText(name);
                p1Score.setText(score+"");
                break;
            case 2:
                p2Name.setText(name);
                p2Score.setText(score+"");
                break;
            case 3:
                p3Name.setText(name);
                p3Score.setText(score+"");
                break;
        }
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

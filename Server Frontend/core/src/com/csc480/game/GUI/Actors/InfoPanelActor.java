package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveByAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Array;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.TextureManager;
import com.csc480.game.GUI.GameScreen;

import java.util.ArrayList;

public class InfoPanelActor extends Group{
    private List<String> eventLog;
    private Array<String> logOfEvents;
    ProgressBar green;
    ProgressBar gold;


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
        //myLayout.setDebug(true);
        myLayout.top();
        //myLayout.setPosition(getWidth()/2, getHeight()/2);
        Image bg = new Image(TextureManager.getInstance().infoBackground);
//        bg.setScale(.25f,1f);
        bg.setPosition(0, 0);
        //bg.setWidth(getWidth());
        //bg.setHeight(getHeight());
        myLayout.background(bg.getDrawable()).top();
        Label title = new Label("Scores", TextureManager.getInstance().ui, "default");
        title.setColor(Color.RED);
        title.setName("title");
        //title.setPosition(GameScreen.GUI_UNIT_SIZE*4,GameScreen.GUI_UNIT_SIZE*12);
//        myLayout.add(title).expandX().padTop(15f);
//        myLayout.row();
        //addActor(title)


        green = new ProgressBar(0f,250f,1f,false,TextureManager.getInstance().ui, "GreenProgress");
        green.getStyle().background.setMinHeight(10f);
        green.getStyle().knobBefore.setMinHeight(10f);
//        green.setScaleY(.01f);
//        green.setWidth(getWidth());
//        green.setHeight(GameScreen.GUI_UNIT_SIZE/10);
        myLayout.add(green).fillX().padBottom(5f).padTop(19f);
        myLayout.row();


//        green.setValue(250f);//todo remove this//////////////////////////////////////////

        gold = new ProgressBar(0f,250f,1f,false,TextureManager.getInstance().ui, "GoldProgress");
        gold.getStyle().background.setMinHeight(10f);
        gold.getStyle().knobBefore.setMinHeight(10f);
        myLayout.add(gold).fillX().padBottom(10);
        myLayout.row();
//        gold.setValue(50f);//todo remove this////////////////////////////////////////////
//


        Table sublayout = new Table();
        //sublayout.setDebug(true);
        p0Name = new Label("player0", TextureManager.getInstance().ui, "default");
        p0Score = new Label("0", TextureManager.getInstance().ui, "default");
        sublayout.add(p0Name).padLeft(10f).padTop(5f).left().expandX().minWidth(150f).padBottom(10f);
        sublayout.add(p0Score).padRight(10f).left().padTop(5f).padBottom(10);
        sublayout.row();

        p1Name = new Label("player1", TextureManager.getInstance().ui, "default");
        p1Score = new Label("10", TextureManager.getInstance().ui, "default");
        sublayout.add(p1Name).padLeft(10f).left().expandX().minWidth(150f).padBottom(10f);
        sublayout.add(p1Score).padRight(10f).left().padBottom(10);
        sublayout.row();

        p2Name = new Label("player2", TextureManager.getInstance().ui, "default");
        p2Score = new Label("100", TextureManager.getInstance().ui, "default");
        sublayout.add(p2Name).padLeft(10f).left().expandX().minWidth(150f).padBottom(10f);
        sublayout.add(p2Score).padRight(10f).left().padBottom(10);
        sublayout.row();

        p3Name = new Label("player3333", TextureManager.getInstance().ui, "default");
        p3Score = new Label("1000", TextureManager.getInstance().ui, "default");
        sublayout.add(p3Name).padLeft(10f).left().expandX().minWidth(150f).padBottom(10f);
        sublayout.add(p3Score).padRight(10f).padBottom(10f).left();
        myLayout.add(sublayout);
        myLayout.row();



        logOfEvents = new Array<String>();
        eventLog = new List<String>(TextureManager.getInstance().ui, "default");
        eventLog.setPosition(GameScreen.GUI_UNIT_SIZE, GameScreen.GUI_UNIT_SIZE*5);
        eventLog.setWidth(GameScreen.GUI_UNIT_SIZE*10);
        myLayout.add(eventLog).fillX().padLeft(10f).padRight(10f).padBottom(10f).maxWidth(200f).minHeight(300f);

        addActor(myLayout);
//        setDebug(true,true);

    }

    public void UpdatePlayerStatus(int position, String name, int score){
        System.out.println("updating info of "+position);
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
        UpdateProgressBars();
    }

    public void UpdateProgressBars(){
        int gr = 0, gl =0;
        if(GameManager.getInstance().theGame == null)return;
        if(GameManager.getInstance().theGame.theGameScreen == null)return;

        if(GameManager.getInstance().theGame.theGameScreen.top.associatedPlayer != null)
        if(GameManager.getInstance().theGame.theGameScreen.top.associatedPlayer.team.toLowerCase().compareTo("green") == 0){
            gr += GameManager.getInstance().theGame.theGameScreen.top.associatedPlayer.score;
        }else if(GameManager.getInstance().theGame.theGameScreen.top.associatedPlayer.team.toLowerCase().compareTo("yellow") == 0)
            {gl += GameManager.getInstance().theGame.theGameScreen.top.associatedPlayer.score;}

        if(GameManager.getInstance().theGame.theGameScreen.bottom.associatedPlayer != null)
        if(GameManager.getInstance().theGame.theGameScreen.bottom.associatedPlayer.team.toLowerCase().compareTo("green") == 0){
            gr += GameManager.getInstance().theGame.theGameScreen.bottom.associatedPlayer.score;
        }else if(GameManager.getInstance().theGame.theGameScreen.bottom.associatedPlayer.team.toLowerCase().compareTo("yellow") == 0)
            {gl += GameManager.getInstance().theGame.theGameScreen.bottom.associatedPlayer.score;}

        if(GameManager.getInstance().theGame.theGameScreen.left.associatedPlayer != null)
        if(GameManager.getInstance().theGame.theGameScreen.left.associatedPlayer.team.toLowerCase().compareTo("green") == 0){
            gr += GameManager.getInstance().theGame.theGameScreen.left.associatedPlayer.score;
        }else if(GameManager.getInstance().theGame.theGameScreen.left.associatedPlayer.team.toLowerCase().compareTo("yellow") == 0)
            { gl += GameManager.getInstance().theGame.theGameScreen.left.associatedPlayer.score;}

        if(GameManager.getInstance().theGame.theGameScreen.right.associatedPlayer != null)
        if(GameManager.getInstance().theGame.theGameScreen.right.associatedPlayer.team.toLowerCase().compareTo("green") == 0){
            gr += GameManager.getInstance().theGame.theGameScreen.right.associatedPlayer.score;
        }else if(GameManager.getInstance().theGame.theGameScreen.right.associatedPlayer.team.toLowerCase().compareTo("yellow") == 0) {
            gl += GameManager.getInstance().theGame.theGameScreen.right.associatedPlayer.score;

        }
        green.setRange(0f,gr+gl+0f);
        gold.setRange(0f,gr+gl+0f);
        green.setValue(gr+0f);
        gold.setValue(gl+0f);

    }

    /**
     * Display an event on the GUI's list of events
     * @param event text to display
     */
    public void LogEvent(String event){
        if(logOfEvents.size >= 4){
            logOfEvents.removeIndex(0);
        }
        Array<String> tempEvents = new Array<String>();
        for(String s : logOfEvents)
            tempEvents.add(s);
        logOfEvents.add(event);
        if(event.length() > 37){
            char br = '-';
            int br_index = 36;
            while (br != ' ' && br_index > 0){
                br_index--;
                br = event.charAt(br_index);
            }

            String line1 = event.substring(0,br_index);
            String line2 = event.substring(br_index,event.length());
            event = line1 + "\n" + line2;

        }
        tempEvents.add(event);
        eventLog.setItems(tempEvents);
        eventLog.setSelectedIndex(-1);//.setSelectedIndex(logOfEvents.size-1);
        //eventLog.getItems().add(event);

    }
    public void ShowBonus(String event){
        MoveByAction mba = new MoveByAction();
        mba.setAmount(2000f,0);
        mba.setDuration(20f);
//        if(event.toLowerCase().contains("oswego bonus"))
        GameManager.getInstance().theGame.theGameScreen.oswego.addAction(mba);
        if(GameManager.getInstance().theGame != null && GameManager.getInstance().theGame.theGameScreen != null)
            GameManager.getInstance().theGame.theGameScreen.oswego.setPosition(-GameScreen.GUI_UNIT_SIZE*40,GameScreen.GUI_UNIT_SIZE*5);
    }
}

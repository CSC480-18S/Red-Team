package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.ScaleToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csc480.game.Engine.TextureManager;
import com.csc480.game.GUI.GameScreen;


public class GameOverActor extends Group {
    private Label winnerMessage;
    private List<String> players;
    private Image greenbackGround;
    private Image goldbakcGround;
    private Table theTabel;
    private Label timeUntilNextGame;

    public GameOverActor(){
        super();
        goldbakcGround = new Image(TextureManager.getInstance().getGoldWinTexture());
        greenbackGround = new Image(TextureManager.getInstance().getGreenWinTexture());
        goldbakcGround.setPosition(0,0);
        greenbackGround.setPosition(0, 0);
        setHeight(GameScreen.GUI_UNIT_SIZE*8.8f);
        setWidth(GameScreen.GUI_UNIT_SIZE*12f);
        setPosition(0,0);
        theTabel = new Table();
        theTabel.setBackground(greenbackGround.getDrawable());
        theTabel.setHeight(GameScreen.GUI_UNIT_SIZE*8.8f);
        theTabel.setWidth(GameScreen.GUI_UNIT_SIZE*12f);
        theTabel.center();

        winnerMessage = new Label("Winner Message",TextureManager.getInstance().ui,"default");
        theTabel.add(winnerMessage);
        theTabel.row();
        players = new List<String>(TextureManager.getInstance().ui, "default");
        players.setWidth(GameScreen.GUI_UNIT_SIZE*10);
//        eventLog.setPosition(GameScreen.GUI_UNIT_SIZE, GameScreen.GUI_UNIT_SIZE*5);
//        eventLog.setWidth(GameScreen.GUI_UNIT_SIZE*10);
        theTabel.add(players).padLeft(30f).expandX().fillX().maxWidth(1000f).center();
        theTabel.row();
        Label timeUntilNextGameLabel = new Label("Time until next game: ",TextureManager.getInstance().ui,"default");
        Table timeTable = new Table();
        timeTable.add(timeUntilNextGameLabel);
        timeUntilNextGame = new Label("30 seconds",TextureManager.getInstance().ui,"default");
        timeTable.add(timeUntilNextGame);
        theTabel.add(timeTable);

        addActor(theTabel);


    }


    public void update(String winnerMessage, Array<String> playersScore, String winningTeam){
        //todo remove the ones that will never match the string
        if(winningTeam.toLowerCase().compareTo("green") == 0)
            theTabel.setBackground(greenbackGround.getDrawable());
        if(winningTeam.toLowerCase().compareTo("gold") == 0)
            theTabel.setBackground(goldbakcGround.getDrawable());

        this.winnerMessage.setText(winnerMessage);
        this.players.setItems(playersScore);
        this.players.setSelectedIndex(-1);

        float oldScaleX = this.getScaleX();
        float oldScaleY = this.getScaleY();
        setScale(0.00001f);
        ScaleToAction sta = new ScaleToAction();
        sta.setScale(oldScaleX,oldScaleY);
        sta.setDuration(2f);
        this.addAction(sta);
    }
    public void updateTime(int timeInSec){
        if(timeInSec > 1)
            timeUntilNextGame.setText(timeInSec+ " seconds");
        else
            timeUntilNextGame.setText(timeInSec+ " second");
    }
}

package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
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
    private Image backGround;
    private Table theTabel;
    private Label timeUntilNextGame;

    public GameOverActor(){
        super();
        backGround = new Image(TextureManager.getInstance().infoBackground);
        backGround.setPosition(0, 0);
        setHeight(GameScreen.GUI_UNIT_SIZE*8.8f);
        setWidth(GameScreen.GUI_UNIT_SIZE*12f);
        setPosition(0,0);
        theTabel = new Table();
        theTabel.setBackground(backGround.getDrawable());
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
        timeUntilNextGame = new Label("Infinity",TextureManager.getInstance().ui,"default");
        timeTable.add(timeUntilNextGame);
        theTabel.add(timeTable);

        addActor(theTabel);


    }


    public void update(String winnerMessage, Array<String> playersScore, String winningTeam){
        //todo set the back ground texture based on team
        this.winnerMessage.setText(winnerMessage);
        this.players.setItems(playersScore);
        this.players.setSelectedIndex(-1);
    }
    public void updateTime(int timeInSec){
        if(timeInSec > 1)
            timeUntilNextGame.setText(timeInSec+ " seconds");
        else
            timeUntilNextGame.setText(timeInSec+ " second");
    }
}

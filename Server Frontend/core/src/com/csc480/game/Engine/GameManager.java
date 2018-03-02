package com.csc480.game.Engine;

import com.badlogic.gdx.math.MathUtils;
import com.csc480.game.Engine.Model.AI;
import com.csc480.game.Engine.Model.Board;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.Engine.Model.Player;
import com.csc480.game.OswebbleGame;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The Class that will hold all the game state and route Events to the GUI, SocketManager, and AI
 */
public class GameManager {
    private static GameManager instance;
    public ArrayList<Placement> placementsUnderConsideration;//ones being considered
    public ArrayList<Player> thePlayers;
    public int numPlayers;
    public int currentPlayerIndex;
    public Board theBoard;
    public int GreenScore;
    public int GoldScore;


    public static GameManager getInstance() {
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        thePlayers = new ArrayList<Player>();
        placementsUnderConsideration = new ArrayList<Placement>();
        theBoard = new Board(OswebbleGame.BOARD_SIZE);
        SocketManager.getInstance().ConnectSocket();
        SocketManager.getInstance().setUpEvents();
    }

    public void Dispose(){
        TextureManager.getInstance().Dispose();
    }


    public void wordHasBeenPlayed(){

    }
    public void playerHasJoined(Player p){


    }
    public void playerHasLeft(Player p) {

    }
    public void currentAIMakePlay(){
        Player current = thePlayers.get(currentPlayerIndex);
        if(current.isAI){
            //if(current.)
        }

    }
    public ArrayList<Character> getNewTiles(int num){
        ArrayList<Character> ret = new ArrayList<Character>();
//THIS IS FOR TESTING ONLY SHOULD CONNECT TO THE DATABASE/////////////////////////////////////////////////////
        for(int i = 0; i < num; i++){
            ret.add(new Character((char) MathUtils.random(26)));
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return ret;
    }

    public class UpdatedState{
        public String message;
        public String currentPlayersMove;
        public char [][] boardState;
        public int greenScore;
        public int goldScore;

    }

}

package com.csc480.game.Engine;

import com.csc480.game.Engine.Model.AI;
import com.csc480.game.Engine.Model.Board;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.Engine.Model.Player;
import com.csc480.game.OswebbleGame;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * The Class that will hold all
 */
public class GameManager {
    private static GameManager instance;
    public ArrayList<Placement> placementsUnderConsideration;//ones being considered
    public ArrayList<Player> thePlayers;
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

}

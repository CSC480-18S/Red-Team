package com.csc480.game.Engine;

import com.csc480.game.Engine.Model.AI;
import com.csc480.game.Engine.Model.Board;
import com.csc480.game.Engine.Model.Player;

/**
 * The Class that will hold all
 */
public class GameManager {
    private static GameManager instance;
    private static Player[] thePlayers;
    private static Board theBoard;
    private static int GreenScore;
    private static int GoldScore;


    public static GameManager getInstance() {
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        SocketManager.getInstance().ConnectSocket();
        SocketManager.getInstance().setUpEvents();
    }


}

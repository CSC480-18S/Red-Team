package com.csc480.game.Engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csc480.game.Engine.Model.*;
import com.csc480.game.OswebbleGame;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

/**
 * The Class that will hold all the game state and route Events to the GUI, SocketManager, and AI
 */
public class GameManager {
    WebSocketClient connection;
    public static boolean debug = false;
    public static boolean produceAI = false;
    private static GameManager instance;
    public OswebbleGame theGame;
    public ArrayList<Placement> placementsUnderConsideration;//ones being considered
    public Player[] thePlayers;
    //public AI[] theAIs;
    public ArrayList<AI> theAIQueue;
    public Board theBoard;
    public int greenScore = 0;
    public int goldScore = 0;
    private ArrayList<String> eventBacklog;
    public ArrayList<Integer> connectAIQueue;
    public ArrayList<Integer> removeAIQueue;
    private ArrayList<String> logQueue;


    /**
     * Access THE instance of the GameManager
     * @return instance
     */
    public static GameManager getInstance() {
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    /**
     * Private because of singleton nature
     */
    private GameManager(){
        connectAIQueue = new ArrayList<>();
        removeAIQueue = new ArrayList<>();
        logQueue = new ArrayList<>();

        thePlayers = new Player[4];
        //theAIs = new AI[4];
        theAIQueue = new ArrayList<>();
        placementsUnderConsideration = new ArrayList<Placement>();
        theBoard = new Board(OswebbleGame.BOARD_SIZE);
        eventBacklog = new ArrayList<String>();
        WordVerification.getInstance();
        ConnectSocket();

        for(int i = 0; i < 4; i++){
            if(produceAI) {
                //theAIs[i] = new AI();
                //thePlayers[i] = theAIs[i];
                theAIQueue.add(new AI());
                if(GameManager.debug)
                    System.out.println("AI MADE +++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            }
            thePlayers[i] = new Player();
        }
    }

    public void Dispose(){
        connection.closeConnection(0, "dispose called");
        connection.close();
    }

    public void Update(){
        if(connectAIQueue.size() > 0){
            Integer position = connectAIQueue.remove(0);
            theAIQueue.add(new AI());
            //theAIs[position] = new AI();
            //thePlayers[position] = theAIs[position];
        }
        if(removeAIQueue.size() > 0){
            int posToRemove = removeAIQueue.get(0);
            AI dead = theAIQueue.remove(posToRemove);
            dead.disconnectAI();
//            Integer position = removeAIQueue.remove(0);
//            if(theAIs[position] != null)
//                theAIs[position].disconnectAI();
//            theAIs[position] = null;
//            thePlayers[position] = new Player();

        }
        if(logQueue.size() > 0)
            theGame.theGameScreen.debug.setText(logQueue.remove(0));

//        for(AI a: theAIs){
//            a.update();
//        }
        ApplyEventBackLog();
    }


    ///////Socket Stuff
    /**
     * Connect to the Server
     */
    public void ConnectSocket(){
        try {
            connection = new WebSocketClient(new URI("ws://localhost:3000")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    //this.connect();
                    if(GameManager.debug)
                        System.out.println("Opened");
                    JSONObject object = new JSONObject();
                    JSONObject data = new JSONObject();
                    object.put("event", "whoAmI");
                    data.put("client", "SF");
                    object.put("data", data);

                    this.send(object.toString());

                }

                @Override
                public void onMessage(String message) {
                    if(GameManager.debug)
                        System.out.println("frontend got message\n"+message);
                    JSONObject object = new JSONObject(message);
                    JSONObject data = null;
                    try {
                        data = object.getJSONObject("data");
                    } catch (JSONException ignored){}

                    switch (object.getString("event")) {
                        case "removeAI":
                            if(GameManager.debug)
                                System.out.println("frontend got removeAI");
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                //int position = data.getInt("position");
                                //removeAIQueue.add(position);
                                removeAIQueue.add(0);
                                if (debug)
                                   logQueue.add("Got removeAI. Game num: " + (theGame.theGameScreen.NUM_GAMES_SINCE_START));
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "connectAI":
                            LogEvent("Reconnecting an AI");
                            if(GameManager.debug)
                                System.out.println("connectAI");
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                if(GameManager.debug)
                                    System.out.println(data.toString());
                                //int position = data.getInt("position");
                                //reconnect an AI
                                //connectAIQueue.add(position);
                                connectAIQueue.add(0);
//                                switch (position) {
//                                    case 0:
//                                        theGame.theGameScreen.bottom.setPlayer(theAIs[(position)]);
//                                        theGame.theGameScreen.bottom.updateState();
//                                        break;
//                                    case 1:
//                                        theGame.theGameScreen.right.setPlayer(theAIs[(position)]);
//                                        theGame.theGameScreen.right.updateState();
//                                        break;
//                                    case 2:
//                                        theGame.theGameScreen.top.setPlayer(theAIs[(position)]);
//                                        theGame.theGameScreen.top.updateState();
//                                        break;
//                                    case 3:
//                                        theGame.theGameScreen.left.setPlayer(theAIs[(position)]);
//                                        theGame.theGameScreen.left.updateState();
//                                        break;
//                                }
                                if (debug)
                                    logQueue.add("Got connectAI. Game num: " + (theGame.theGameScreen.NUM_GAMES_SINCE_START));
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "scoreUpdate":
                            greenScore = data.getInt("green");
                            goldScore = data.getInt("gold");

                            break;
                        case "gameEvent":
                            if(GameManager.debug)
                                System.out.println("frontend got gameEvent");
                            try {
                                String action = data.getString("action");
                                LogEvent(action);
                                if (debug)
                                    logQueue.add("Got gameEvent. Game num: " + (theGame.theGameScreen.NUM_GAMES_SINCE_START));
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "updateState":
                            if(GameManager.debug)
                                System.out.println("frontend got updateState");
                            try {
                                if(GameManager.debug)
                                    System.out.println(data);
                                JSONArray board = data.getJSONArray("board");
                                //find the board/user state differences
                                wordHasBeenPlayed(unJSONifyBackendBoard(board));
                                //hard update the game and user states
                                hardUpdateBoardState(unJSONifyBackendBoard(board));

                                greenScore = data.getInt("green");
                                goldScore = data.getInt("gold");
                                boolean isBonus = false;
                                if (data.get("bonus") != JSONObject.NULL)
                                    isBonus = data.getBoolean("bonus");
                                //System.out.println(action);
                                if (isBonus)
                                    BonusEvent("Bonus Word!");

                                JSONArray players = data.getJSONArray("players");
                                int i;
                                for (i = 0; i < players.length(); i++) {
                                    if(players.get(i) == JSONObject.NULL) continue;
                                    JSONObject player = (JSONObject) players.get(i);
                                    //int index = player.getInt("position");
                                    boolean isAI;
                                    try {
                                        isAI = player.getBoolean("isAI");
                                        thePlayers[i].isAI = isAI;

                                        if (player.get("score") != JSONObject.NULL)
                                            thePlayers[i].score = player.getInt("score");
                                        else
                                            thePlayers[i].score = 0;

                                        if (player.get("name") != JSONObject.NULL)
                                            thePlayers[i].name = player.getString("name");
                                        else
                                            thePlayers[i].name = "";

                                        if (player.get("team") != JSONObject.NULL)
                                            thePlayers[i].team = player.getString("team");
                                        else
                                            thePlayers[i].team = "";

                                        if (player.get("tiles") != JSONObject.NULL) {
                                            JSONArray hand = player.getJSONArray("tiles");
                                            for (int h = 0; h < hand.length(); h++) {
                                                if (isAI)
                                                    thePlayers[i].tiles[h] = hand.getString(h).toLowerCase().charAt(0);
                                                else
                                                    thePlayers[i].tiles[h] = '_';
                                            }
                                        }
                                        thePlayers[i].turn = player.getBoolean("isTurn");
                                        if(GameManager.debug)
                                            System.out.println("updated player info @ index " + i+" isturn="+thePlayers[i].turn);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                                //set all other spots to null
                                i++;
                                for (;i < 4; i++) {
                                    thePlayers[i] = new Player();
                                }
                                if (theGame != null) {
                                    if (theGame.theGameScreen != null) {
                                        theGame.theGameScreen.QueueUpdatePlayers();
                                    }
                                }
                                if (debug)
                                    logQueue.add("Got updateState. Game num: " + (theGame.theGameScreen.NUM_GAMES_SINCE_START));

                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "gameOver":
                            for (int i = 0; i < theAIQueue.size(); i++) {
                                theAIQueue.get(i).state = 0;
                            }
                            if(GameManager.debug)
                                System.out.println("gameOver");
                            LogEvent("gameOver");
                            //"score": scores of game "timeout": double
                            //"gameData": array of all data of the scores and such
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                if(GameManager.debug)
                                    System.out.println(data.toString());
                                JSONArray scores = data.getJSONArray("scores");
                                Array<String> playersScores = new Array<String>();
                                for (int i = 0; i < scores.length(); i++) {
                                    JSONObject j = (JSONObject) scores.get(i);
                                    playersScores.add(j.getString("name") + " scored " + j.getInt("score") + " points!");
                                }

                                String winner = "Congratulations, Everyone!!!";
                                if (data.get("winner") != JSONObject.NULL)
                                    winner = "Congratulations, " + data.getString("winner") + "!!!";
                                String winningTeam = "No one";
                                if (data.get("winningTeam") != JSONObject.NULL)
                                    winningTeam = data.getString("winningTeam");

                                //todo call @GUI stuff
                                theGame.theGameScreen.TriggerEndGame(winner, playersScores, winningTeam);
//                                theGame.theGameScreen.gameOverActor.update(winner, playersScores, winningTeam);
//                                theGame.theGameScreen.gameOverActor.setVisible(true);
                                if (debug)
                                    logQueue.add("Got gameOverEvent. Game num: " + (theGame.theGameScreen.NUM_GAMES_SINCE_START));
                            } catch (ArrayIndexOutOfBoundsException e) {
                                e.printStackTrace();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            break;
                        case "newGame":
                            if(GameManager.debug)
                                System.out.println("newGame");
                            theGame.theGameScreen.TriggerNewGame();
                            if (debug)
                                logQueue.add("Game num: " + (theGame.theGameScreen.NUM_GAMES_SINCE_START++));
                            break;
                        case "newGameCountdown":
                            if(GameManager.debug)
                                System.out.println("ferver frontend got newGameCountdown");
                            //JSONObject data = (JSONObject) args[0];
                            int t = 20;
                            if (data.get("time") != JSONObject.NULL)
                                t = data.getInt("time");
                            theGame.theGameScreen.gameOverActor.updateTime(t);
                            //                theGame.theGameScreen.gameOverActor.setVisible(false);
                            break;
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    if(GameManager.debug)
                        System.out.println("GAMEMANAGER SOCKET CLOSED");
                    if(GameManager.debug)
                        System.out.println("REASON: " + reason + " CODE: " + code + "------------------------------------------------------------------------------------------------");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
            connection.connect();
            if(GameManager.debug)
                System.out.println(connection.isOpen() + " @#$!(&!^@*(&#$^*(#&U^!@&^#&*!@^$&*!^#@%&*(!#@^$&*!@^$*&!^%*(&!^$&(*!^@$(!#&(^$*&!@^#&!^@$&*^!&*^$&!*(@^$*&(!^@$&*(");
            if(GameManager.debug)
                System.out.println(connection.isConnecting() + " @#$!(&!^@*(&#$^*(#&U^!@&^#&*!@^$&*!^#@%&*(!#@^$&*!@^$*&!^%*(&!^$&(*!^@$(!#&(^$*&!@^#&!^@$&*^!&*^$&!*(@^$*&(!^@$&*(");
        } catch (URISyntaxException e){
            System.err.println(e);
        }
    }




    private TileData[][] parseServerBoard(JSONArray board){
        TileData[][] parsed = new TileData[board.length()][board.length()];
        JSONArray col;
        for(int i = 0; i < board.length(); i++){
            col = board.getJSONArray(i);
            for(int j = 0; j < col.length(); j++){
                //the hacky way because the server is sending Strings
                String serverTile = col.getString(j);
                if(serverTile.compareTo("null") == 0) {
                    parsed[i][j] = null;
                }else {
                    //System.out.println("serverTile ======="+serverTile);
                    TileData t = new TileData(
                            j,//serverTile.getInt("_x"),
                            10-i,//serverTile.getInt("_y"),
                            serverTile.charAt(1),
                            0,
                            0,
                           "",
                            0
                    );
                    parsed[i][j] = t;
                }
            }
        }
        return parsed;
    }
/*
    public void updatePlayers(Player[] backEndPlayers){
        //currentPlayerIndex = currentPlayer;
        //Update the Players
        if(theGame == null)return;
        if(theGame.theGameScreen == null)return;
        if(theGame.theGameScreen.bottom == null ||
                theGame.theGameScreen.top == null ||
                theGame.theGameScreen.left == null ||
                theGame.theGameScreen.right == null) return;

        for(int i = 0; i < 4; i++){
            Player p = backEndPlayers[i];
            //cover existing players
            boolean exists = false;
            Player inHand = null;
            switch (i){
                case 0:
                    inHand = theGame.theGameScreen.bottom.getPlayer();
                    break;
                case 1:
                    inHand = theGame.theGameScreen.right.getPlayer();
                    break;
                case 2:
                    inHand = theGame.theGameScreen.top.getPlayer();
                    break;
                case 3:
                    inHand = theGame.theGameScreen.left.getPlayer();
                    break;
            }
            inHand.tiles = p.tiles;
            inHand.turn = p.turn;
            inHand.team = p.team;
            inHand.score = p.score;
            inHand.isAI = p.isAI;

            switch (i){
                case 0:
                    theGame.theGameScreen.bottom.setPlayer(inHand);
                    break;
                case 1:
                    theGame.theGameScreen.right.setPlayer(inHand);
                    break;
                case 2:
                    theGame.theGameScreen.top.setPlayer(inHand);
                    break;
                case 3:
                    theGame.theGameScreen.left.setPlayer(inHand);
                    break;
            }

            theGame.theGameScreen.infoPanel.UpdatePlayerStatus(i, inHand.name, inHand.score);
        }
    }
    */
    /**
     * This will force the Board state to sync with the backend data, should only be used to recover from failures
     */
    public void hardUpdateBoardState(TileData[][] serverBoard){
        theBoard.the_game_board = serverBoard;
//        System.out.println("SERVER FRONTEND STATE UPDATED/////////////////////////////////////////////////////////////////////////////");
    }

    public void wordHasBeenPlayed(TileData[][] backendBoardState){
        synchronized (GameManager.getInstance().theBoard.the_game_board) {
            ArrayList<TileData> newPlays = getPlacementsFromBackendThatArentOnFrontEnd(backendBoardState);
            ArrayList<Placement> conversion = new ArrayList<Placement>();
            for (TileData t : newPlays) {
                conversion.add(new Placement(t.letter, t.getX(), t.getY()));
            }
            // todo call @GUI to
            // todo apply tiles from user.
            // todo hit /game/usersInGame to apply new tile additions.
            // todo @Engine hard update player states with ^^^ data
            //but for now we just add the plays
            if (conversion.size() > 0)
                theBoard.addWord(conversion);
        }

    }
    public void displayMessage(String message, double time){
        //todo call @GUI function
    }

    /**
     * This will propagate the player joined event to all necessary locations,
     * @param p
     */
    public void playerHasJoined(Player p){
        //todo call @GUI function
    }
    /**
     * This will propagate the player left event to all necessary locations
     * @param p
     */
    public void playerHasLeft(Player p) {
        //todo call @GUI function
    }

    /**
     * ASSUMES THAT THE (0,0) tile is in the bottom left tiles corner!!!
     * This returns tiles that exist on the backend, but not on the frontend
     * @param backend board state from the server
     * @return
     */
    public ArrayList<TileData> getPlacementsFromBackendThatArentOnFrontEnd(TileData[][] backend){
        ArrayList<TileData> diff = new ArrayList<TileData>();
        for(int i = 0; i < backend.length; i++){
            for(int j = 0; j < backend[0].length; j++){
                if((backend[i][j] != null && theBoard.the_game_board[i][j] == null)){
                    diff.add(backend[i][j]);
                }else if(backend[i][j] != null && (backend[i][j].letter != theBoard.the_game_board[i][j].letter)){
                    diff.add(backend[i][j]);
                }
            }
        }
        return diff;
    }

    /**
     * ASSUMES THAT THE (0,0) tile is in the bottom left tiles corner!!!
     * This returns tiles that exist on the frontend, but not on the backend
     * @param backend
     * @return
     */
    public ArrayList<TileData> getPlacementsFromFrontEndThatArentOnBackEnd(TileData[][] backend){
        ArrayList<TileData> diff = new ArrayList<TileData>();
        for(int i = 0; i < backend.length; i++){
            for(int j = 0; j < backend[0].length; j++){
                if((backend[i][j] == null && theBoard.the_game_board[i][j] != null) ||
                        (backend[i][j].letter != theBoard.the_game_board[i][j].letter)){
                    diff.add(theBoard.the_game_board[i][j]);
                }
            }
        }
        return diff;
    }

    /**
     * Add an event to be displayed to the GUI
     * @param eventName
     */
    public void LogEvent(String eventName) {
        if(!eventBacklog.contains(eventName))
            synchronized (eventBacklog) {
                eventBacklog.add(eventName);
            }
//        ApplyEventBackLog();
//        if(theGame != null && theGame.theGameScreen != null && theGame.theGameScreen.infoPanel !=null) {
//            theGame.theGameScreen.infoPanel.LogEvent(eventName);
//
//        }else {
//            System.out.println("adding "+eventName+"to backlog");
//            if(!eventBacklog.contains(eventName))
//                eventBacklog.add(eventName);
//        }

    }

    public void BonusEvent(String eventName){
        theGame.theGameScreen.infoPanel.ShowBonus(eventName);
    }

    /**
     * SHOULD ONLY EVER BE CALLED WHEN NOT RENDERING
     * Attempt to display any events gotten that have yet to be shown
     */
    private void ApplyEventBackLog(){
        if(theGame != null && theGame.theGameScreen != null && theGame.theGameScreen.infoPanel !=null) {
            synchronized (eventBacklog) {
                for(String s: eventBacklog)
                    theGame.theGameScreen.infoPanel.LogEvent(s);
                eventBacklog.clear();
            }
        }

    }

    /**
     * This function transforms a play idea into the format that the backend can read
     *
     * @param p
     * @return 2D json array of the board state with the play idea added onto it
     */
    public static JSONArray JSONifyPlayIdea(PlayIdea p, Board refBoard){
        Board temp = refBoard.getCopy();
        temp.addWord(p.placements);
        JSONArray parentJsonArray = new JSONArray();
        // loop through your elements
        for (int i=0; i<11; i++){
            JSONArray childJsonArray = new JSONArray();
            for (int j =0; j<11; j++){
                if(temp.the_game_board[j][10-i] != null)
                    childJsonArray.put(temp.the_game_board[j][10-i].letter+"");
                else
                    childJsonArray.put(JSONObject.NULL);

            }
            parentJsonArray.put(childJsonArray);
        }
        if(GameManager.debug)
            System.out.println(parentJsonArray.toString());
        return parentJsonArray;
    }

    public TileData[][] unJSONifyBackendBoard(JSONArray backend){
        TileData[][] state = new TileData[11][11];
        for (int i=0; i<11; i++){
            JSONArray childJsonArray = backend.getJSONArray(i);
            for (int j =0; j<11; j++){
                char temp = 0;
                if(childJsonArray.get(j) != JSONObject.NULL) {
                    temp = ((String) childJsonArray.get(j)).toLowerCase().charAt(0);
                    state[j][10-i] = new TileData(new Vector2(j,10-i),temp,0,0,"",0);
                }
            }
        }
        return state;
    }

    /**
     * THIS SHOULDNT BE USED IN THE FINAL PROJECT. WAITING ON BACKEND
     * This gives 7 random letters
     * @return
     */
    public char[] getNewHand(){
        char[] ret = new char[7];
        for(int i =0; i < ret.length; i++){
            ret[i] = (char)MathUtils.random(97,122);
        }
        return ret;
    }

    /**
     * Transforms the Board State to JSON that the backend can read
     * @return
     */
    public String PrintBoardState(){
        JSONArray parentJsonArray = new JSONArray();
        // loop through your elements
        for (int i=0; i<11; i++){
            JSONArray childJsonArray = new JSONArray();
            for (int j =0; j<11; j++){
                if(theBoard.the_game_board[j][10-i] != null)
                    childJsonArray.put("\""+theBoard.the_game_board[j][10-i].letter+"\"");
                else
                    childJsonArray.put("null");

            }
            parentJsonArray.put(childJsonArray);
        }
        //System.out.println(parentJsonArray.toString());
        return parentJsonArray.toString();
    }
}

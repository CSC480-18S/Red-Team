package com.csc480.game.Engine;

import com.badlogic.gdx.math.MathUtils;
import com.csc480.game.Engine.Model.*;
import com.csc480.game.OswebbleGame;
import io.socket.client.IO;
import io.socket.emitter.Emitter;
import io.socket.client.Socket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The Class that will hold all the game state and route Events to the GUI, SocketManager, and AI
 */
public class GameManager {
    private static GameManager instance;
    public OswebbleGame theGame;
    public ArrayList<Placement> placementsUnderConsideration;//ones being considered
    public Player[] thePlayers;
    public AI[] theAIs;
    public int numPlayers;
    //public int currentPlayerIndex;
    public Board theBoard;
    public int greenScore;
    public int goldScore;
    public boolean gameOver;
    //socket stuff
    private Socket socket;
    private double reconnectTimer = 2000.0;
    private ArrayList<String> eventBacklog;


    public static GameManager getInstance() {
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        thePlayers = new Player[4];
        theAIs = new AI[4];
        placementsUnderConsideration = new ArrayList<Placement>();
        theBoard = new Board(OswebbleGame.BOARD_SIZE);
        eventBacklog = new ArrayList<String>();
        //SocketManager.getInstance().ConnectSocket();
        //SocketManager.getInstance().setUpEvents();
        ConnectSocket();
        setUpEvents();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 4; i++){
            theAIs[i] = new AI();
            thePlayers[i] = theAIs[i];
        }

    }
    public void Update(){
        for(AI a: theAIs){
            a.update();
        }
        ApplyEventBackLog();
    }

    public void Dispose(){
        if(socket != null)
            socket.disconnect();
        TextureManager.getInstance().Dispose();
    }

    ///////Socket Stuff
    /**
     * Connect to the Server
     */
    public void ConnectSocket(){
        try {
            if(socket != null)
                socket.disconnect();
            socket = null;
            socket = IO.socket("http://localhost:3000");
            socket.connect();
        } catch (URISyntaxException e){
            System.err.println(e);
        }
    }
    public void ReConnectSocket(){
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = true;
            socket = IO.socket("http://localhost:3000", opts);
            socket.connect();
        } catch (URISyntaxException e){
            System.err.println(e);
        }
    }

    /**
     * Define the actions to be taken when events occur
     */
    public void setUpEvents(){
        socket.on(io.socket.client.Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogEvent("connected to the backend");
                //simple example of how to access the data sent from the server
                try {
                    //JSONObject data = (JSONObject) args[0];
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("whoAreYou", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogEvent("whoAreYou");
                JSONObject data = new JSONObject();
                data.put("isSF",true);
                socket.emit("whoAreYou",data);
            }
        }).on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogEvent("disconnection");
                System.out.println("attempting reconnection:");
                ReConnectSocket();

            }
        }).on(Socket.EVENT_RECONNECTING, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("reconnecting");
            }
        }).on(Socket.EVENT_RECONNECT_ATTEMPT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("reconnect attempt");
            }
        }).on(Socket.EVENT_RECONNECT_FAILED, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("reconnect failed");
            }
        }).on(Socket.EVENT_CONNECT_TIMEOUT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("connect timeout");
            }
        }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("reconnect");
            }
        }).on("connectAI", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogEvent("Reconnecting an AI");
                System.out.println("connectAI");
                try {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println(data.toString());
                    int position = data.getInt("position");
                    //reconnect an AI
                    theAIs[(position)].ReConnectSocket();
                    switch (position){
                        case 0:
                            theGame.theGameScreen.bottom.setPlayer(theAIs[(position)]);
                            theGame.theGameScreen.bottom.updateState();
                            break;
                        case 1:
                            theGame.theGameScreen.right.setPlayer(theAIs[(position)]);
                            theGame.theGameScreen.right.updateState();
                            break;
                        case 2:
                            theGame.theGameScreen.top.setPlayer(theAIs[(position)]);
                            theGame.theGameScreen.top.updateState();
                            break;
                        case 3:
                            theGame.theGameScreen.left.setPlayer(theAIs[(position)]);
                            theGame.theGameScreen.left.updateState();
                            break;
                    }
                    thePlayers[position] = theAIs[(position)];
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("wordPlayed", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogEvent("wordPlayed");
                System.out.println("frontend got wordPlayed");
                try {
                    JSONArray board = (JSONArray) args[0];
                    System.out.println("BACKEND BOARD STATE: "+board.toString());
                    //todo un mess this up, the state isnt being constant and the AI are generating with bad data
                    //TileData[][] parsed = parseServerBoard(board);
                    //find the board/user state differences
                    //wordHasBeenPlayed(parsed);
                    //hard update the game and user states
                    //hardUpdateBoardState(parsed);
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("gameEvent", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("frontend got gameEvent");
                try {
                    JSONObject data = (JSONObject) args[0];
                    String action = data.getString("action");
                    //System.out.println(action);
                    LogEvent(action);
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("gameOver", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("gameOver");
                LogEvent("gameOver");
                //"score": scores of game "timeout": double
                //"gameData": array of all data of the scores and such
                try {
                    JSONObject data = (JSONObject) args[0];
                    JSONObject score = data.getJSONObject("score");
                    JSONArray gameData = data.getJSONArray("gameData");
                    Double timeout = data.getDouble("timeOut");
                    //todo figure out exact format server is sending it
                    //set all player's turns to false
                    for(Player p: thePlayers){
                        p.turn = false;
                    }
                    //todo call @GUI stuff
                    //todo reset game after time
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("newGame", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("newGame");
                //todo hard reset board
                //todo Hit /game/usersInGame and hard reset players
            }
        });
    }

    private TileData[][] parseServerBoard(JSONArray board){
        TileData[][] parsed = new TileData[board.length()][board.length()];
        JSONArray col;
        for(int i = 0; i < board.length(); i++){
            col = board.getJSONArray(i);
            for(int j = 0; j < col.length(); j++){
                /*//the old, proper way to do it
                JSONObject serverTile = col.getJSONObject(j);
                //int x, int y, char the_letter, int value, int bonus, String player, long timePlayed
                if(serverTile.getBoolean("_letterPlaced ")){
                    TileData t = new TileData(
                        serverTile.getInt("_x"),
                        10-serverTile.getInt("_y"),
                        serverTile.getString("_letter").charAt(0),
                        0,
                        serverTile.getInt("_multiplier"),
                        serverTile.getString("_playedBy"),
                        serverTile.getLong("_timePlayedAt")
                    );
                parsed[i][j] = t;}
                */
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

    public void updatePlayers(Player[] backEndPlayers){
        //currentPlayerIndex = currentPlayer;
        //Update the Players
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




//
//
//
//
//
//            if(theGame.theGameScreen.top.getPlayer().name.compareTo(p.name) == 0){
//                inHand.tiles = p.tiles;
//                inHand.turn = p.turn;
//                inHand.team = p.team;
//                inHand.score = p.score;
//                inHand.isAI = p.isAI;
//
//                exists = true;
//            } else
//            if(theGame.theGameScreen.bottom.getPlayer().name.compareTo(p.name) == 0){
//                inHand.tiles = p.tiles;
//                inHand.turn = p.turn;
//                inHand.team = p.team;
//                inHand.score = p.score;
//                inHand.isAI = p.isAI;
//                theGame.theGameScreen.bottom.updateState();
//                theGame.theGameScreen.infoPanel.UpdatePlayerStatus(0, inHand.name, inHand.score);
//                exists = true;
//            } else
//            if(theGame.theGameScreen.left.getPlayer().name.compareTo(p.name) == 0){
//                inHand.tiles = p.tiles;
//                inHand.turn = p.turn;
//                inHand.team = p.team;
//                inHand.score = p.score;
//                inHand.isAI = p.isAI;
//                theGame.theGameScreen.top.updateState();
//                theGame.theGameScreen.infoPanel.UpdatePlayerStatus(3, inHand.name, inHand.score);
//                exists = true;
//            } else
//            if(theGame.theGameScreen.right.getPlayer().name.compareTo(p.name) == 0){
//                Player inHand = theGame.theGameScreen.right.getPlayer();
//                inHand.tiles = p.tiles;
//                inHand.turn = p.turn;
//                inHand.team = p.team;
//                inHand.score = p.score;
//                inHand.isAI = p.isAI;
//                theGame.theGameScreen.top.updateState();
//                theGame.theGameScreen.infoPanel.UpdatePlayerStatus(1, inHand.name, inHand.score);
//                exists = true;
//            }
//
//            //cover new players
//            if(!exists){
//                //replace an AI in thePlayers arraylist with new player
//                //replace an AI in the GUI with this new player
//                thePlayers[i]= p;
//                if(theGame.theGameScreen.right.getPlayer().isAI){
//                    theGame.theGameScreen.right.setPlayer(p);
//                    theGame.theGameScreen.right.updateState();
//                    theGame.theGameScreen.infoPanel.UpdatePlayerStatus(1, p.name, p.score);
//                }else if(theGame.theGameScreen.left.getPlayer().isAI){
//                    theGame.theGameScreen.left.setPlayer(p);
//                    theGame.theGameScreen.left.updateState();
//                    theGame.theGameScreen.infoPanel.UpdatePlayerStatus(3, p.name, p.score);
//                }else if(theGame.theGameScreen.top.getPlayer().isAI){
//                    theGame.theGameScreen.top.setPlayer(p);
//                    theGame.theGameScreen.top.updateState();
//                    theGame.theGameScreen.infoPanel.UpdatePlayerStatus(2, p.name, p.score);
//                }else if(theGame.theGameScreen.bottom.getPlayer().isAI){
//                    theGame.theGameScreen.bottom.setPlayer(p);
//                    theGame.theGameScreen.bottom.updateState();
//                    theGame.theGameScreen.infoPanel.UpdatePlayerStatus(0, p.name, p.score);
//                }else {
//                    throw new UnsupportedOperationException("There are no places for a new Player to join");
//                }
//            }
        }
        /*
        numPlayers = backEndPlayers.size();
        if(numPlayers < 4){
            //make a new AI
            int numGreenPlayers = 0;
            int numGoldPlayers = 0;
            for(Player p : backEndPlayers){
                if(p.team.compareTo("green") == 0)
                    numGreenPlayers++;
                if(p.team.compareTo("gold") == 0)
                    numGoldPlayers++;
            }
            AI tempAI = new AI();
            if(numGoldPlayers > numGreenPlayers)
                tempAI.team = "green";
            if(numGoldPlayers <= numGreenPlayers)
                tempAI.team = "gold";

            //SocketManager.getInstance().BroadcastNewAI(tempAI);
        }*/
    }

    /**
     * This will force the Board state to sync with the backend data, should only be used to recover from failures
     */
    public void hardUpdateBoardState(TileData[][] serverBoard){
        theBoard.the_game_board = serverBoard;
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
     * The GM wont do this, the individual AI will go through this process
     */
    /*
    @Deprecated
    public void currentAIMakePlay(){
        Player current = thePlayers.get(currentPlayerIndex);
        if(current.isAI){
            System.out.println("Finding all AI plays for tiles");
            Long startTime = System.nanoTime();
            ((AI)current).TESTFindPlays(theBoard);
            System.out.println("finding all possible AI plays took nanos: "+(System.nanoTime()-startTime));
            PlayIdea bestPlay = ((AI)current).PlayBestWord();
            while(bestPlay != null && !theBoard.verifyWordPlacement(bestPlay.placements)){
                bestPlay = ((AI)current).PlayBestWord();
                if(bestPlay == null) break;
            }

            if(bestPlay != null && bestPlay.myWord != null && theBoard.verifyWordPlacement(bestPlay.placements)){
                System.out.println("The AI found made a decent play");
                //SocketManager.getInstance().BroadcastAIPlay(bestPlay);
                placementsUnderConsideration.clear();
            }
        }
    }*/


    /**
     * ASSUMES THAT THE (0,0) tile is in the bottom left tiles corner!!!
     * @param backend
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


    public void LogEvent(String eventName) {
        ApplyEventBackLog();
        if(theGame != null && theGame.theGameScreen != null && theGame.theGameScreen.infoPanel !=null) {
            theGame.theGameScreen.infoPanel.LogEvent(eventName);

        }else {
            //System.out.println("adding "+eventName+"to backlog");
            eventBacklog.add(eventName);
        }

    }
    private void ApplyEventBackLog(){
        if(theGame != null && theGame.theGameScreen != null && theGame.theGameScreen.infoPanel !=null) {
            for(String s: eventBacklog)
                theGame.theGameScreen.infoPanel.LogEvent(s);
            eventBacklog.clear();
        }

    }

    public JSONArray JSONifyPlayIdea(PlayIdea p){
        Board temp = GameManager.getInstance().theBoard.getCopy();
        temp.addWord(p.placements);
        JSONArray parentJsonArray = new JSONArray();
        // loop through your elements
        for (int i=0; i<11; i++){
            JSONArray childJsonArray = new JSONArray();
            for (int j =0; j<11; j++){
                if(temp.the_game_board[j][10-i] != null)
                    childJsonArray.put("\""+temp.the_game_board[j][10-i].letter+"\"");
                else
                    childJsonArray.put(JSONObject.NULL);

            }
            parentJsonArray.put(childJsonArray);
        }
        System.out.println(parentJsonArray.toString());
        return parentJsonArray;
    }

    public char[] getNewHand(){
        char[] ret = new char[7];
        for(int i =0; i < ret.length; i++){
            ret[i] = (char)MathUtils.random(97,122);
        }
        return ret;
    }

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

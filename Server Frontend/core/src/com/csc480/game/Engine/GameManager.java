package com.csc480.game.Engine;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.csc480.game.Engine.Model.*;
import com.csc480.game.GUI.GameScreen;
import com.csc480.game.OswebbleGame;
import io.socket.client.IO;
import io.socket.emitter.Emitter;
import io.socket.client.Socket;
import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * The Class that will hold all the game state and route Events to the GUI, SocketManager, and AI
 */
public class GameManager {
    WebSocket connection;
    public static boolean debug = false;
    public static boolean produceAI = false;
    private static GameManager instance;
    public OswebbleGame theGame;
    public ArrayList<Placement> placementsUnderConsideration;//ones being considered
    public Player[] thePlayers;
    public AI[] theAIs;
    public int numPlayers;
    public int counter = 0;
    //public int currentPlayerIndex;
    public Board theBoard;
    public int greenScore;
    public int goldScore;
    public boolean gameOver;
    //socket stuff
    private Socket socket;
    private double reconnectTimer = 2000.0;
    private ArrayList<String> eventBacklog;

    private ArrayList<Integer> connectAIQueue;
    private ArrayList<Integer> removeAIQueue;


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

        thePlayers = new Player[4];
        theAIs = new AI[4];
        placementsUnderConsideration = new ArrayList<Placement>();
        theBoard = new Board(OswebbleGame.BOARD_SIZE);
        eventBacklog = new ArrayList<String>();
        //SocketManager.getInstance().ConnectSocket();
        //SocketManager.getInstance().setUpEvents();
        WordVerification.getInstance();
        ConnectSocket();
        //setUpEvents();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < 4; i++){
            if(produceAI) {
                theAIs[i] = new AI();
                thePlayers[i] = theAIs[i];
            }else {
                thePlayers[i] = new Player();
            }
        }


    }
    public void Update(){
        if(connectAIQueue.size() > 0){
            Integer position = connectAIQueue.remove(0);
            theAIs[position] = new AI();
            thePlayers[position] = theAIs[position];
        }
        if(removeAIQueue.size() > 0){
            Integer position = removeAIQueue.remove(0);
            if(theAIs[position] != null)
                theAIs[position].disconnectAI();
            theAIs[position] = null;
            thePlayers[position] = new Player();

        }
//        for(AI a: theAIs){
//            a.update();
//        }
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
            /*if(socket != null)
                socket.disconnect();
            socket = null;
            socket = IO.socket("http://localhost:3000");
            socket.connect();*/
            connection = new WebSocketClient(new URI("ws://localhost:3000")) {
                @Override
                public void onOpen(ServerHandshake handshakedata) {
                    JSONObject object = new JSONObject();
                    JSONObject data = new JSONObject();
                    object.put("event", "whoAmI");
                    data.put("client", "SF");
                    object.put("data", data);

                    this.send(object.toString());
                }

                @Override
                public void onMessage(String message) {
                    JSONObject data = (JSONObject)JSONObject.stringToValue(message);
                    switch(data.getString("event")){
                        case "removeAI":
                            System.out.println("frontend got removeAI");
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                int position = data.getInt("position");
                                removeAIQueue.add(position);
                                if(debug)
                                    theGame.theGameScreen.debug.setText("Got removeAI. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
                            }catch(ArrayIndexOutOfBoundsException e){
                                e.printStackTrace();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        case "connectAI":
                            LogEvent("Reconnecting an AI");
                            System.out.println("connectAI");
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                System.out.println(data.toString());
                                int position = data.getInt("position");
                                //reconnect an AI
                                connectAIQueue.add(position);
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
                                if(debug)
                                    theGame.theGameScreen.debug.setText("Got connectAI. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
                            }catch(ArrayIndexOutOfBoundsException e){
                                e.printStackTrace();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        case "boardUpdate":
                            System.out.println("frontend got boardUpdate");
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                System.out.println("data: "+data.toString());
                                JSONArray board = data.getJSONArray("board");
//                    System.out.println("BACKEND BOARD STATE: "+board.toString());
//                    System.out.println("PARSED BACKEND BOARD STATE: "+unJSONifyBackendBoard(board));
                                //find the board/user state differences
                                wordHasBeenPlayed(unJSONifyBackendBoard(board));
                                //hard update the game and user states
                                hardUpdateBoardState(unJSONifyBackendBoard(board));
                                if(debug)
                                    theGame.theGameScreen.debug.setText("Got boardUpdate. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
                            }catch(ArrayIndexOutOfBoundsException e){
                                e.printStackTrace();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        case "gameEvent":
                            System.out.println("frontend got gameEvent");
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                boolean isBonus = false;
                                String action = data.getString("action");
                                if(data.get("bonus") != JSONObject.NULL)
                                    isBonus = data.getBoolean("bonus");
                                //System.out.println(action);
                                if(isBonus)
                                    BonusEvent(action);
                                LogEvent(action);
                                if(debug)
                                    theGame.theGameScreen.debug.setText("Got gameEvent. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
                            }catch(ArrayIndexOutOfBoundsException e){
                                e.printStackTrace();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        case "updateState":
                            System.out.println("frontend got updateState");
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                System.out.println(data);
                                JSONArray players = data.getJSONArray("players");
                                for(int i = 0; i < players.length(); i++){
                                    JSONObject player  = (JSONObject)players.get(i);
                                    int index = player.getInt("position");
                                    boolean isAI;
                                    try {
                                        isAI = player.getBoolean("isAI");
                                    }catch(JSONException e){
                                        //the
                                        isAI = true;
                                        //todo reconnect an AI at that position
                                        theAIs[index].disconnectAI();
                                        theAIs[index] = null;
                                        theAIs[index] = new AI();
                                        thePlayers[index] = theAIs[index];
                                    }
                                    try {
                                        if(player.get("score") != JSONObject.NULL)
                                            thePlayers[index].score = player.getInt("score");
                                        else
                                            thePlayers[index].score = 0;

                                        if(player.get("name") != JSONObject.NULL)
                                            thePlayers[index].name = player.getString("name");
                                        else
                                            thePlayers[index].name = "";

                                        if(player.get("team") != JSONObject.NULL)
                                            thePlayers[index].team = player.getString("team");
                                        else
                                            thePlayers[index].team = "";

                                        if(player.get("tiles") != JSONObject.NULL){
                                            JSONArray hand = player.getJSONArray("tiles");
                                            for(int h = 0; h < hand.length(); h++){
                                                if(isAI)
                                                    thePlayers[index].tiles[h] = hand.getString(h).toLowerCase().charAt(0);
                                                else
                                                    thePlayers[index].tiles[h] = '_';
                                            }
                                        }

                                        thePlayers[index].turn = player.getBoolean("isTurn");
                                        System.out.println("updating player @ index " + index);
                                    }catch (JSONException e){
                                        e.printStackTrace();
                                    }
                        /*
                        JSONArray hand = player.getJSONArray("hand");
                        for(int j = 0; j < hand.length(); j++){
                            thePlayers[index].tiles[j] = hand.getString(j).charAt(0);
                        }
                        */
                                }
                                if(theGame!= null){
                                    if(theGame.theGameScreen != null) {
                                        if(theGame.theGameScreen.bottom != null){
                                            theGame.theGameScreen.bottom.setPlayer(thePlayers[0]);
                                            theGame.theGameScreen.bottom.updateState();
                                        }
                                        if(theGame.theGameScreen.right != null) {
                                            theGame.theGameScreen.right.setPlayer(thePlayers[1]);
                                            theGame.theGameScreen.right.updateState();
                                        }
                                        if(theGame.theGameScreen.top != null) {
                                            theGame.theGameScreen.top.setPlayer(thePlayers[2]);
                                            theGame.theGameScreen.top.updateState();
                                        }

                                        if(theGame.theGameScreen.left != null) {
                                            theGame.theGameScreen.left.setPlayer(thePlayers[3]);
                                            theGame.theGameScreen.left.updateState();
                                        }

                                        theGame.theGameScreen.UpdateInfoPanel();
                                    }
                                }
                                if(debug)
                                    theGame.theGameScreen.debug.setText("Got updateState. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));

                            }catch(ArrayIndexOutOfBoundsException e){
                                e.printStackTrace();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        case "gameOver":
                            System.out.println("gameOver");
                            LogEvent("gameOver");
                            //"score": scores of game "timeout": double
                            //"gameData": array of all data of the scores and such
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                System.out.println(data.toString());
                                JSONArray scores = data.getJSONArray("scores");
                                Array<String> playersScores = new Array<String>();
                                for(int i = 0; i < scores.length(); i++){
                                    JSONObject j = (JSONObject) scores.get(i);
                                    playersScores.add(j.getString("name")+" scored "+j.getInt("score")+ " points!");
                                }

                                String winner = "Congratulations, Everyone!!!";
                                if(data.get("winner") != JSONObject.NULL)
                                    winner = "Congratulations, "+data.getString("winner")+"!!!";
                                String winningTeam = "No one";
                                if(data.get("winningTeam") != JSONObject.NULL)
                                    winningTeam = data.getString("winningTeam");

                                //todo call @GUI stuff
                                theGame.theGameScreen.gameOverActor.update(winner, playersScores, winningTeam);
                                theGame.theGameScreen.gameOverActor.setVisible(true);
                                if(debug)
                                    theGame.theGameScreen.debug.setText("Got gameOverEvent. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
                            }catch(ArrayIndexOutOfBoundsException e){
                                e.printStackTrace();
                            }catch(JSONException e){
                                e.printStackTrace();
                            }
                        case "newGame":
                            System.out.println("newGame");
                            theGame.theGameScreen.gameOverActor.setVisible(false);
                            if(debug)
                                theGame.theGameScreen.debug.setText("Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START++));
                        case "newGameCountdown":
                            System.out.println("ferver frontend got newGameCountdown");
                            //JSONObject data = (JSONObject) args[0];
                            int t = 20;
                            if(data.get("time") != JSONObject.NULL)
                                t = data.getInt("time");
                            theGame.theGameScreen.gameOverActor.updateTime(t);
                            //                theGame.theGameScreen.gameOverActor.setVisible(false);
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("GAMEMANAGER SOCKET CLOSED");
                    System.out.println("REASON: " + reason + " CODE: " + code + "------------------------------------------------------------------------------------------------");
                }

                @Override
                public void onError(Exception ex) {
                    ex.printStackTrace();
                }
            };
        } catch (URISyntaxException e){
            System.err.println(e);
        }
    }
/*    public void ReConnectSocket(){
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = true;
            socket = IO.socket("http://localhost:3000", opts);
            socket.connect();
        } catch (URISyntaxException e){
            System.err.println(e);
        }
    }*/


    /**
     * Define the actions to be taken when events occur
     */
    /*
    public void setUpEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
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
//                LogEvent("whoAreYou");
                JSONObject data = new JSONObject();
                data.put("isSF",true);
                socket.emit("whoAreYou",data);
            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                LogEvent("disconnection");
                System.out.println("attempting reconnection:");
                if(debug)
                    theGame.theGameScreen.debug.setText("Got disconnect. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
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
                for(int i = 0; i < 4; i++){
                    if(produceAI) {
                        theAIs[i] = new AI();
                        thePlayers[i] = theAIs[i];
                    }else {
                        thePlayers[i] = new Player();
                    }
                }
            }
        }).on("removeAI", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("frontend got removeAI");
                try {
                    JSONObject data = (JSONObject) args[0];
                    int position = data.getInt("position");
                    if(theAIs[position] != null)
                        theAIs[position].disconnectAI();
                    theAIs[position] = null;
                    thePlayers[position] = new Player();

                    if(debug)
                        theGame.theGameScreen.debug.setText("Got removeAI. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
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
                    theAIs[position] = new AI();
                    thePlayers[position] = theAIs[position];
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
                    if(debug)
                        theGame.theGameScreen.debug.setText("Got connectAI. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("boardUpdate", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
//                LogEvent("boardUpdate");
                System.out.println("frontend got boardUpdate");
                try {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println("data: "+data.toString());
                    JSONArray board = data.getJSONArray("board");
                    try{
                        int greenScore = data.getInt("green");
                        int yellowScore = data.getInt("gold");
                    }catch (JSONException e){
                        System.out.println("ERROR: team names messed up");

                    }
                    //find the board/user state differences
                    wordHasBeenPlayed(unJSONifyBackendBoard(board));
                    //hard update the game and user states
                    hardUpdateBoardState(unJSONifyBackendBoard(board));
                    if(debug)
                        theGame.theGameScreen.debug.setText("Got boardUpdate. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
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
                    boolean isBonus = false;
                    String action = data.getString("action");
                    if(data.get("bonus") != JSONObject.NULL)
                        isBonus = data.getBoolean("bonus");
                    //System.out.println(action);
                    if(isBonus)
                        BonusEvent(action);
                    LogEvent(action);
                    if(debug)
                        theGame.theGameScreen.debug.setText("Got gameEvent. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("updateState", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("frontend got updateState");
                try {
                    JSONObject data = (JSONObject) args[0];
                    System.out.println(data);
                    JSONArray players = data.getJSONArray("players");
                    for(int i = 0; i < players.length(); i++){
                        JSONObject player  = (JSONObject)players.get(i);
                        int index = player.getInt("position");
                        boolean isAI;
                        try {
                            isAI = player.getBoolean("isAI");
                        }catch(JSONException e){
                            //the
                            isAI = true;
                            //todo reconnect an AI at that position
                            theAIs[index].disconnectAI();
                            theAIs[index] = null;
                            theAIs[index] = new AI();
                            thePlayers[index] = theAIs[index];
                        }
                        try {
                            if(player.get("score") != JSONObject.NULL)
                                thePlayers[index].score = player.getInt("score");
                            else
                                thePlayers[index].score = 0;

                            if(player.get("name") != JSONObject.NULL)
                                thePlayers[index].name = player.getString("name");
                            else
                                thePlayers[index].name = "";

                            if(player.get("team") != JSONObject.NULL)
                                thePlayers[index].team = player.getString("team");
                            else
                                thePlayers[index].team = "";

                            if(player.get("tiles") != JSONObject.NULL){
                                JSONArray hand = player.getJSONArray("tiles");
                                for(int h = 0; h < hand.length(); h++){
                                    if(isAI)
                                        thePlayers[index].tiles[h] = hand.getString(h).toLowerCase().charAt(0);
                                    else
                                        thePlayers[index].tiles[h] = '_';
                                }
                            }

                            thePlayers[index].turn = player.getBoolean("isTurn");
                            System.out.println("updating player @ index " + index);
                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        /*
                        JSONArray hand = player.getJSONArray("hand");
                        for(int j = 0; j < hand.length(); j++){
                            thePlayers[index].tiles[j] = hand.getString(j).charAt(0);
                        }

                    }
                    if(theGame!= null){
                        if(theGame.theGameScreen != null)
                            theGame.theGameScreen.QueueUpdate();
                    }
                    if(debug)
                        theGame.theGameScreen.debug.setText("Got updateState. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));

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
                    System.out.println(data.toString());
                    JSONArray scores = data.getJSONArray("scores");
                    Array<String> playersScores = new Array<String>();
                    for(int i = 0; i < scores.length(); i++){
                        JSONObject j = (JSONObject) scores.get(i);
                        playersScores.add(j.getString("name")+" scored "+j.getInt("score")+ " points!");
                    }

                    String winner = "Congratulations, Everyone!!!";
                    if(data.get("winner") != JSONObject.NULL)
                           winner = "Congratulations, "+data.getString("winner")+"!!!";
                    String winningTeam = "No one";
                    if(data.get("winningTeam") != JSONObject.NULL)
                        winningTeam = data.getString("winningTeam");

                    //todo call @GUI stuff
                    theGame.theGameScreen.gameOverActor.update(winner, playersScores, winningTeam);
                    theGame.theGameScreen.gameOverActor.setVisible(true);
                    if(debug)
                        theGame.theGameScreen.debug.setText("Got gameOverEvent. Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START));
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
                theGame.theGameScreen.gameOverActor.setVisible(false);
                if(debug)
                    theGame.theGameScreen.debug.setText("Game num: "+(theGame.theGameScreen.NUM_GAMES_SINCE_START++));
            }
        }).on("newGameCountdown", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("ferver frontend got newGameCountdown");
                JSONObject data = (JSONObject) args[0];
                int t = 20;
                if(data.get("time") != JSONObject.NULL)
                    t = data.getInt("time");
                theGame.theGameScreen.gameOverActor.updateTime(t);
//                theGame.theGameScreen.gameOverActor.setVisible(false);
            }
        });
    }*/

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
        ApplyEventBackLog();
        if(theGame != null && theGame.theGameScreen != null && theGame.theGameScreen.infoPanel !=null) {
            theGame.theGameScreen.infoPanel.LogEvent(eventName);

        }else {
            //System.out.println("adding "+eventName+"to backlog");
            eventBacklog.add(eventName);
        }

    }

    public void BonusEvent(String eventName){
        theGame.theGameScreen.infoPanel.ShowBonus(eventName);
    }

    /**
     * Attempt to display any events gotten that have yet to be shown
     */
    private void ApplyEventBackLog(){
        if(theGame != null && theGame.theGameScreen != null && theGame.theGameScreen.infoPanel !=null) {
            for(String s: eventBacklog)
                theGame.theGameScreen.infoPanel.LogEvent(s);
            eventBacklog.clear();
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

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

/**
 * The Class that will hold all the game state and route Events to the GUI, SocketManager, and AI
 */
public class GameManager {
    private static GameManager instance;
    public OswebbleGame theGame;
    public ArrayList<Placement> placementsUnderConsideration;//ones being considered
    public ArrayList<Player> thePlayers;
    public int numPlayers;
    public int currentPlayerIndex;
    public Board theBoard;
    public int greenScore;
    public int goldScore;
    public boolean gameOver;
    //socket stuff
    private Socket socket;
    private double reconnectTimer = 2000.0;


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

    ///////Socket Stuff
    /**
     * Connect to the Server
     */
    public void ConnectSocket(){
        try {
            socket = IO.socket("http://localhost:3000");
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
                System.out.println("connection");
                //simple example of how to access the data sent from the server
                try {
                    JSONObject data = (JSONObject) args[0];
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("whoAreYou", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("whoAreYou");
                while (!socket.connected()){
                    socket.emit(Socket.EVENT_RECONNECT,new Emitter.Listener(){
                        @Override
                        public void call(Object... args) {
                            JSONObject data = new JSONObject();
                            data.put("isServerFrontend",true);
                            socket.emit("whoAreYou",data);
                        }
                    });
                }
            }
        }).on(io.socket.client.Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("disconnection");
                while (!socket.connected()){
                    socket.emit(Socket.EVENT_RECONNECT,new Emitter.Listener(){
                        @Override
                        public void call(Object... args) {

                        }
                    });
                }

            }
        }).on(Socket.EVENT_RECONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("reconnect");

            }
        }).on("connectAI", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("connectAI");
                try {
                    JSONObject data = (JSONObject) args[0];
                    int position = data.getInt("position");
                    //reconnect an AI
                    //todo @Engine -> @James or @Chris
                    for(Player p : thePlayers){
                        if(p instanceof AI){
                            if(((AI) p).mySocket.connected()){

                            }
                        }
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("wordPlayed", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("wordPlayed");
                try {
                    JSONObject data = (JSONObject) args[0];
                    JSONArray board = data.getJSONArray("board");
                    JSONArray col;
                    TileData[][] parsed = parseServerBoard(board);
                    //find the board/user state differences
                    wordHasBeenPlayed(parsed);
                    //hard update the game and user states
                    hardUpdateBoardState(parsed);
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }).on("gameEvent", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("gameEvent");
                try {
                    JSONObject data = (JSONObject) args[0];
                    String action = data.getString("action");
                    System.out.println(action);
                   //todo call GUI stuff
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
            }
        }
        return parsed;
    }

    public void updatePlayers(ArrayList<Player> backEndPlayers, int currentPlayer){
        currentPlayerIndex = currentPlayer;
        //Update the Players
        for(Player p : backEndPlayers){
            //cover existing players
            boolean exists = false;
            if(theGame.theGameScreen.top.getPlayer().name.compareTo(p.name) == 0){
                Player inHand = theGame.theGameScreen.top.getPlayer();
                inHand.tiles = p.tiles;
                inHand.turn = p.turn;
                inHand.team = p.team;
                inHand.score = p.score;
                inHand.isAI = p.isAI;
                theGame.theGameScreen.top.updateState();
                exists = true;
            } else
            if(theGame.theGameScreen.bottom.getPlayer().name.compareTo(p.name) == 0){
                Player inHand = theGame.theGameScreen.bottom.getPlayer();
                inHand.tiles = p.tiles;
                inHand.turn = p.turn;
                inHand.team = p.team;
                inHand.score = p.score;
                inHand.isAI = p.isAI;
                theGame.theGameScreen.bottom.updateState();
                exists = true;
            } else
            if(theGame.theGameScreen.left.getPlayer().name.compareTo(p.name) == 0){
                Player inHand = theGame.theGameScreen.left.getPlayer();
                inHand.tiles = p.tiles;
                inHand.turn = p.turn;
                inHand.team = p.team;
                inHand.score = p.score;
                inHand.isAI = p.isAI;
                theGame.theGameScreen.top.updateState();
                exists = true;
            } else
            if(theGame.theGameScreen.right.getPlayer().name.compareTo(p.name) == 0){
                Player inHand = theGame.theGameScreen.right.getPlayer();
                inHand.tiles = p.tiles;
                inHand.turn = p.turn;
                inHand.team = p.team;
                inHand.score = p.score;
                inHand.isAI = p.isAI;
                theGame.theGameScreen.top.updateState();
                exists = true;
            }

            //cover new players
            if(!exists){
                //replace an AI in thePlayers arraylist with new player
                //replace an AI in the GUI with this new player
                if(theGame.theGameScreen.right.getPlayer().isAI){
                    thePlayers.remove(theGame.theGameScreen.right.getPlayer());
                    thePlayers.add(p);
                    theGame.theGameScreen.right.setPlayer(p);
                    theGame.theGameScreen.right.updateState();
                }else if(theGame.theGameScreen.left.getPlayer().isAI){
                    thePlayers.remove(theGame.theGameScreen.left.getPlayer());
                    thePlayers.add(p);
                    theGame.theGameScreen.left.setPlayer(p);
                    theGame.theGameScreen.left.updateState();
                }else if(theGame.theGameScreen.top.getPlayer().isAI){
                    thePlayers.remove(theGame.theGameScreen.top.getPlayer());
                    thePlayers.add(p);
                    theGame.theGameScreen.top.setPlayer(p);
                    theGame.theGameScreen.top.updateState();
                }else if(theGame.theGameScreen.bottom.getPlayer().isAI){
                    thePlayers.remove(theGame.theGameScreen.bottom.getPlayer());
                    thePlayers.add(p);
                    theGame.theGameScreen.bottom.setPlayer(p);
                    theGame.theGameScreen.bottom.updateState();
                }else {
                    throw new UnsupportedOperationException("There are no places for a new Player to join");
                }
            }
        }
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
        }
    }

    /**
     * This will force the Board state to sync with the backend data, should only be used to recover from failures
     */
    public void hardUpdateBoardState(TileData[][] serverBoard){
        theBoard.the_game_board = serverBoard;
    }

    public void wordHasBeenPlayed(TileData[][] backendBoardState){
        ArrayList<TileData> newPlays = getPlacementsFromBackendThatArentOnFrontEnd(backendBoardState);
        ArrayList<Placement> conversion = new ArrayList<Placement>();
        for(TileData t : newPlays){
            conversion.add(new Placement(t.letter,t.getX(),t.getY()));
        }
        // todo call @GUI to
        // todo apply tiles from user.
        // todo hit /game/usersInGame to apply new tile additions.
        // todo @Engine hard update player states with ^^^ data
        //but for now we just add the plays
        if(conversion.size() > 0)
            theBoard.addWord(conversion);


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
    }


    /**
     * ASSUMES THAT THE (0,0) tile is in the bottom left tiles corner!!!
     * @param backend
     * @return
     */
    public ArrayList<TileData> getPlacementsFromBackendThatArentOnFrontEnd(TileData[][] backend){
        ArrayList<TileData> diff = new ArrayList<TileData>();
        for(int i = 0; i < backend.length; i++){
            for(int j = 0; j < backend[0].length; j++){
                if((backend[i][j] != null && theBoard.the_game_board[i][j] == null) ||
                        (backend[i][j].letter != theBoard.the_game_board[i][j].letter)){
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
}

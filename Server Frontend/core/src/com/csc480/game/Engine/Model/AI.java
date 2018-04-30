package com.csc480.game.Engine.Model;

import com.badlogic.gdx.math.Vector2;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.WordVerification;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;


//AI used as a placeholder player for when the game begins and no players are present
public class AI extends Player {
    //naming counter. Whenever a new AI is created, this value gets incremented
    private static int counter = 0;
    //boolean that determines what time the AI belongs to
    private static boolean greenTeam = true;
    //datastructure that contains all possible plays, with higher priority given to longer words.
    public PriorityQueue myCache;
    //Socket used to communicate with the backend server
    public WebSocketClient connection;
    //local copy of the board state
    volatile Board myBoard;
    //start index that determines whether the AI begins looking for plays at the bottom left, or the top right in order to increase the spread of plays made.
    volatile boolean startIndex = true;

    /**
     * The starting state of the AI
     * 0 = waiting
     * 1 = playing
     * 2 = waitforVerification
     */
    private int state = 0;
    public AI(){
        super();
        this.isAI = true;
        this.name = "AI"+(counter++);
        if(greenTeam){
            this.team = "Green";
        }else {
            this.team = "Gold";
        }
        myCache = new PriorityQueue(200);
        myBoard = new Board(11);
        connectSocket();
    }

    /**
     * AI State Transition: used to think of words when it becomes the AI's turn and yield when it is not
     */
    public void update(){
        System.out.println("AI: " + this.name + "     CURRENT STATE: " + this.state);
            switch (state) {
                case 0://waiting
                    break;
                case 1://play
                    System.out.println(this.name+" is thinking of plays!!!");
                    //pops the best play out of the priority queue
                    PlayIdea bestPlay = PlayBestWord();
                    //loops to check if the best play is null and if it is a valid placement, if either fail, pops the next value
                    while (bestPlay != null && !myBoard.verifyWordPlacement(bestPlay.placements)) {
                        bestPlay = PlayBestWord();
                        if (bestPlay == null) break;
                    }
                    //turns the bestPlay into a JSONObject and sends it to the backend.
                    if (bestPlay != null && bestPlay.myWord != null && myBoard.verifyWordPlacement(bestPlay.placements)) {
                        System.out.println(this.name + " trying to play: "+bestPlay.myWord + "  while in state " + this.state);
                        if(GameManager.getInstance().debug) {
                            System.out.println(this.name + " JSONIFIED DATA TO BE SET: " + GameManager.JSONifyPlayIdea(bestPlay, myBoard));
                        }
                        JSONObject object = new JSONObject();
                        JSONObject data = new JSONObject();
                        object.put("event", "playWord");
                        data.put("play", GameManager.JSONifyPlayIdea(bestPlay, myBoard));
                        object.put("data", data);

                        //send play to backend
                        if(GameManager.getInstance().debug) {
                            System.out.println(AI.this.name + " sending \n" + object.toString());
                        }
                        this.connection.send(object.toString()+"");
                        //change this state to 2, meaning that the AI is now awaiting a response from the backend
                        this.state = 2;
                        GameManager.getInstance().placementsUnderConsideration.clear();
                        //remove tiles from hand
                        removeTilesFromHand(bestPlay);

                    }
                    //if tbe best play is null or the verification fails, the AI passes its turn
                    else{

                        System.out.println("no plays found, hand : "+new String(tiles));
                        //clear cache as it has no valid plays
                        myCache.Clear();
                        //create json array in order to send the tiles that must be swapped
                        JSONArray toSwap = new JSONArray();
                        for(int i = 0; i < tiles.length; i++){
                            //if the tile is not null, throw it into the tiles to be swapped
                            if(tiles[i] != 0){
                                toSwap.put((tiles[i]+"").toUpperCase());
                            }
                        }
                        JSONObject object = new JSONObject();
                        JSONObject data = new JSONObject();
                        object.put("event", "swap");
                        data.put("tiles", toSwap);
                        object.put("data", data);
                        //send the tiles to the backend to receive new ones
                        if(GameManager.getInstance().debug) {
                            System.out.println(AI.this.name + " sending \n" + object.toString());
                        }
                        this.connection.send(object.toString()+"");
                        //update state to idle
                        this.state = 0;
                    }
            }
        return;
    }

    /**
     * Remove the Tiles in a play from the AI's hand after it makes a valid play
     * @param p
     */
    private void removeTilesFromHand(PlayIdea p){
        ArrayList<Placement> copy = (ArrayList<Placement>) p.placements.clone();
        String play = "";
        for(int i = 0; i < copy.size(); i++){
            play+=copy.get(i).letter;
        }
        for(int i = 0; i < tiles.length; i++){
            if(play.contains(tiles[i]+"")){
                play.replaceFirst(tiles[i]+"", "");
                tiles[i] = 0;
            }
        }

    }

    /**
     * Connect an AI to the backend and listen for events
     */
    public void connectSocket(){
        try{
            connection = new WebSocketClient(new URI("ws://localhost:3000")) {
                @Override
                //on open, send the server a whoAmI event to allow for them to know an AI has attempted a connection
                public void onOpen(ServerHandshake handshakedata) {
                    JSONObject object = new JSONObject();
                    JSONObject data = new JSONObject();
                    object.put("event", "whoAmI");
                    data.put("client", "AI");
                    object.put("data", data);

                    this.send(object.toString());
                    System.out.println("AI CONNECTION ESTABLISHED: +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
                }

                @Override
                public void onMessage(String message) {
                    if(GameManager.getInstance().debug) {
                        System.out.println(AI.this.name + " got message\n" + message);
                    }
                    //parse the message to a JSONObject

                    JSONObject object = new JSONObject(message);
                    //put the JSONObject data from the received message in order to find the actual data
                    JSONObject data = object.getJSONObject("data");

                    switch(object.getString("event")){
                        //data update contains the board, if it is this AI's turn, and any new tiles this AI needed
                        case "dataUpdate":
                            System.out.println(AI.this.name + " got dataUpdate");
                            try {
                                //JSONObject data = (JSONObject) args[0];
                                //System.out.println(data.toString());
                                boolean myTurn = data.getBoolean("isTurn");
                                JSONArray jsonTiles = data.getJSONArray("tiles");
                                char[] newTiles = new char[jsonTiles.length()];
                                for(int i = 0; i < newTiles.length; i++){
                                    tiles[i] = jsonTiles.getString(i).toLowerCase().charAt(0);
                                }
                                JSONArray board = data.getJSONArray("board");
                                //find the board/user state differences
                                myBoard.the_game_board = GameManager.getInstance().unJSONifyBackendBoard(board);
                                //initiate play thinking
                                if (myTurn) {
                                    //grab startTime in order to stop the AI from thinking for 6 seconds
                                    long startTime = System.currentTimeMillis();
                                    //clear all words in cache
                                    myCache.Clear();
                                    //decide whether to start at top right or bottom left when finding plays
                                    if(startIndex) {
                                        FindPlays(myBoard);
                                    }
                                    else{
                                        FindPlayInverted(myBoard);
                                    }
                                    //debug
                                    if(!GameManager.debug) {
                                        while (System.currentTimeMillis() - startTime < 6000) {
                                            try {
                                                Thread.sleep(200);
                                            }
                                            catch (InterruptedException e){

                                            }
                                        }
                                    }
                                    //update state to thinking
                                    state = 1;
                                }
                                else{
                                    //not this AI's turn, state to 0
                                    state = 0;
                                }
                                //call update for AI to either wait or play
                                GameManager.getInstance().theGame.theGameScreen.QueueUpdatePlayers();
                                update();
                            } catch (ArrayIndexOutOfBoundsException e) {
                                if(GameManager.getInstance().debug) {
                                    e.printStackTrace();
                                }
                            } catch (JSONException e) {
                                if(GameManager.getInstance().debug) {
                                    e.printStackTrace();
                                }
                            }
                            break;

                        case "invalidPlay":
                            //received when an invalid play is sent to the backend
                            System.out.println(AI.this.name + " got play");
                            try {
                                if (state == 2)
                                    System.out.println(AI.this.name + " State set back to 1");
                                state = 1;
                                update();
                            } catch (Exception e) {
                                if(GameManager.getInstance().debug) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                        case "playWord":
                            myCache.Clear();
                            break;
                    }
                }
                //when the socket closes, print out all stuff related
                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("AI SOCKET CLOSED");
                    System.out.println("REASON: " + reason + " CODE: " + code + "------------------------------------------------------------------------------------------------");

                }
                //if an error occurs print it
                @Override
                public void onError(Exception ex) {
                    if(GameManager.getInstance().debug) {
                        ex.printStackTrace();
                    }
                }
            };
            //intiate actual connection
            connection.connect();
        }
        catch (Exception e){
            if(GameManager.getInstance().debug) {
                e.printStackTrace();
            }
        }
    }

    public void disconnectAI() {
        connection.closeConnection(0, "gotDisconnect");
        connection.close();
    }

    /**
     * This Should be used to actually play a word. If this returns null, call FindPlay() and run this method again.
     * CAUTION: IF A WORD ACTUALLY GETS PLAYED YOU MUST INVALIDATE THE CACHE!
     * @return the best play the AI can think of
     */
    public PlayIdea PlayBestWord(){
        if(myCache.size ==0)
            return null;
        PlayIdea best = myCache.Pull();
        //myCache.Clear();
        return best;
    }


    public void FindPlays(Board boardState){
        //Invalidate the cache
        myCache.Clear();
        this.startIndex = false;
        System.out.println("Starting at bottom left corner");
        //AI ALGORITHM HERE
        boolean hasFoundASinglePlayableTile = false;
        long startTime = System.currentTimeMillis();
        for(int i = 0; i < boardState.the_game_board.length; i ++){
            for(int j = 0; j < boardState.the_game_board[0].length; j++){
                if(boardState.the_game_board[i][j] != null) {
                    if (System.currentTimeMillis() - startTime < 10000) {
                        hasFoundASinglePlayableTile = true;
                        //parse horiz
                        char[] horConstr = new char[11];
                        for (int h = 0; h < boardState.the_game_board.length; h++) {
                            if (boardState.the_game_board[h][j] != null)
                                horConstr[h] = boardState.the_game_board[h][j].letter;
                        }
                        //get all possible plays with current tiles and boardstate
                        ArrayList<PlayIdea> possiblePlays = WordVerification.getInstance()
                                .TESTgetWordsFromHand(new String(tiles), horConstr, i, boardState.the_game_board[i][j], true);


                        //verify they dont fuck with tiles around where theyd be played
                        for (int p = 0; p < possiblePlays.size(); p++) {
                            if (possiblePlays.get(p).placements.size() > 0)
                                if (boardState.verifyWordPlacement(possiblePlays.get(p).placements)) {
                                    //update that shit
                                    myCache.Push(possiblePlays.get(p));
                                    GameManager.getInstance().placementsUnderConsideration = possiblePlays.get(p).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                                }
                        }
                        //parse vert
                        char[] vertConstr = new char[11];
                        for (int v = 0; v < boardState.the_game_board.length; v++) {
                            if (boardState.the_game_board[i][v] != null)
                                vertConstr[10 - v] = boardState.the_game_board[i][v].letter;
                        }
                        ArrayList<PlayIdea> possiblePlaysVert = WordVerification.getInstance()
                                .TESTgetWordsFromHand(new String(tiles), vertConstr, j, boardState.the_game_board[i][j], false);

                        //verify they dont fuck with tiles around where theyd be played
                        for (int p = 0; p < possiblePlaysVert.size(); p++) {
                            if (possiblePlaysVert.get(p).placements.size() > 0)
                                if (boardState.verifyWordPlacement(possiblePlaysVert.get(p).placements)) {
                                    //update that shit
                                    myCache.Push(possiblePlaysVert.get(p));
                                    GameManager.getInstance().placementsUnderConsideration = possiblePlaysVert.get(p).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                                }
                        }
                    }
                    else{
                        return;
                    }
                }
            }
        }
        if(!hasFoundASinglePlayableTile){
            TileData centerTile =  new TileData(new Vector2(5,5), (char)0,0,0, this.name, System.currentTimeMillis());
            char[] constraints = new char[11];
            ArrayList<PlayIdea> possiblePlaysCent = WordVerification.getInstance().TESTgetWordsFromHand(new String(tiles), constraints, 5, centerTile, true);
            if(!possiblePlaysCent.isEmpty()) {
                myCache.Push(possiblePlaysCent.get(0));
                GameManager.getInstance().placementsUnderConsideration = possiblePlaysCent.get(0).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
            } else{
//SKIP MY TURN AND REDRAW//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                System.err.println("should skip and get new tiles");
            }
        }

    }
    //same as abocve but the start index is inverted
    public void FindPlayInverted(Board boardState){
        //Invalidate the cache
        myCache.Clear();
        this.startIndex = true;
        System.out.println("Starting at top right corner");
        //AI ALGORITHM HERE
        boolean hasFoundASinglePlayableTile = false;
        long startTime = System.currentTimeMillis();
        for(int i = boardState.the_game_board.length - 1; i >= 0; i --){
            for(int j = boardState.the_game_board[0].length - 1 ; j >= 0; j--){
                if(boardState.the_game_board[i][j] != null) {
                    if(System.currentTimeMillis() - startTime < 10000) {
                        hasFoundASinglePlayableTile = true;
                        //parse horiz
                        char[] horConstr = new char[11];
                        for (int h = 0; h < boardState.the_game_board.length; h++) {
                            if (boardState.the_game_board[h][j] != null)
                                horConstr[h] = boardState.the_game_board[h][j].letter;
                        }
                        //get all possible plays with current tiles and boardstate
                        ArrayList<PlayIdea> possiblePlays = WordVerification.getInstance()
                                .TESTgetWordsFromHand(new String(tiles), horConstr, i, boardState.the_game_board[i][j], true);


                        //verify they dont fuck with tiles around where theyd be played
                        for (int p = 0; p < possiblePlays.size(); p++) {
                            if (possiblePlays.get(p).placements.size() > 0)
                                if (boardState.verifyWordPlacement(possiblePlays.get(p).placements)) {
                                    //update that shit
                                    myCache.Push(possiblePlays.get(p));
                                    GameManager.getInstance().placementsUnderConsideration = possiblePlays.get(p).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                                }
                        }
                        //parse vert
                        char[] vertConstr = new char[11];
                        //for(int v = boardState.the_game_board.length-1; v >= 0; v--){
                        for (int v = 0; v < boardState.the_game_board.length; v++) {
                            if (boardState.the_game_board[i][v] != null)
                                vertConstr[10 - v] = boardState.the_game_board[i][v].letter;
                        }
                        //get all possible plays with current tiles and boardstate
                        ArrayList<PlayIdea> possiblePlaysVert = WordVerification.getInstance()
                                .TESTgetWordsFromHand(new String(tiles), vertConstr, j, boardState.the_game_board[i][j], false);

                        //verify they dont fuck with tiles around where theyd be played
                        for (int p = 0; p < possiblePlaysVert.size(); p++) {
                            if (possiblePlaysVert.get(p).placements.size() > 0)
                                if (boardState.verifyWordPlacement(possiblePlaysVert.get(p).placements)) {
                                    //update that shit
                                    myCache.Push(possiblePlaysVert.get(p));
                                    GameManager.getInstance().placementsUnderConsideration = possiblePlaysVert.get(p).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                                }
                        }
                    }
                    else{
                        return;
                    }
                }
            }
        }
        if(!hasFoundASinglePlayableTile){
            TileData centerTile =  new TileData(new Vector2(5,5), (char)0,0,0, this.name, System.currentTimeMillis());
            char[] constraints = new char[11];
            ArrayList<PlayIdea> possiblePlaysCent = WordVerification.getInstance().TESTgetWordsFromHand(new String(tiles), constraints, 5, centerTile, true);
            if(!possiblePlaysCent.isEmpty()) {
                myCache.Push(possiblePlaysCent.get(0));
                GameManager.getInstance().placementsUnderConsideration = possiblePlaysCent.get(0).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
            } else{
//SKIP MY TURN AND REDRAW//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                System.err.println("should skip and get new tiles");
            }
        }

    }


    private class PriorityQueue{
        public int count;
        public int size;
        private ArrayList<PlayIdea> queue;

        public PriorityQueue(int cap) {
            queue = new ArrayList<PlayIdea>();
            count = 0;
            size = cap;
        }

        public boolean Contains(PlayIdea n){
            return queue.contains(n);
        }
        /**
         * Adds e to the queue in it's appropriate location
         * @param e
         */
        public void Push(PlayIdea e){
            if(count == size){
                queue.remove(size-1);
                count--;
            }
            int index = 0;
            while(index < count && e.proirity < queue.get(index).proirity){
                index++;
            };
            queue.add(index, e);
            count++;
        }

        /**
         * Returns the item with the highest priority
         * @return
         */
        public PlayIdea Pull(){
            if(count <= 0)
                return null;
            count--;
            return queue.remove(0);

        }

        public void ReConsider(PlayIdea e){
            queue.remove(e);
            queue.add(e);
        }

        public void Clear(){
            queue.clear();
            count = 0;
        }
    }
}


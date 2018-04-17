package com.csc480.game.Engine.Model;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Vector2;
import com.csc480.game.Engine.GameManager;
//import com.csc480.game.Engine.SocketManager;
import com.csc480.game.Engine.WordVerification;

import com.mashape.unirest.http.Unirest;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class AI extends Player {
    private static int counter = 0;
    private static boolean greenTeam = true;
    public PriorityQueue myCache;
    public Socket mySocket;
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
            greenTeam = !greenTeam;
        }else {
            this.team = "Yellow";
            greenTeam = !greenTeam;
        }
        myCache = new PriorityQueue(200);
        connectSocket();
    }

    /**
     * AI State Transition: used to think of words when it becomes the AI's turn and yield when it is not
     */
    public void update(){
        System.out.println("AI: " + this.name + "     CURRENT STATE: " + this.state);
        synchronized (GameManager.getInstance().theBoard.the_game_board) {
            switch (state) {
                case 0://waiting
                    break;
                case 1://play
                    //PlayIdea play;
                    PlayIdea bestPlay = PlayBestWord();
                    System.out.println(this.name + " Pre loop");
                    while (bestPlay != null && !GameManager.getInstance().theBoard.verifyWordPlacement(bestPlay.placements)) {
                        bestPlay = PlayBestWord();
                        if (bestPlay == null) break;
                    }
                    System.out.println(this.name + " post loop");
                    if (bestPlay != null && bestPlay.myWord != null && GameManager.getInstance().theBoard.verifyWordPlacement(bestPlay.placements)) {
                        //System.out.println("The AI found made a decent play");
                        System.out.println(this.name + " trying to play: "+bestPlay.myWord + "  while in state " + this.state);
                        System.out.println(this.name + " JSONIFIED DATA TO BE SET: "+GameManager.getInstance().JSONifyPlayIdea(bestPlay));
                        mySocket.emit("playWord", GameManager.getInstance().JSONifyPlayIdea(bestPlay));
                        this.state = 2;
                        //GameManager.getInstance().theBoard.addWord(bestPlay.placements);
                        GameManager.getInstance().placementsUnderConsideration.clear();
                        //remove tiles from hand
                        removeTilesFromHand(bestPlay);
                        GameManager.getInstance().updatePlayers(GameManager.getInstance().thePlayers);
                    }else{
                        GameManager.getInstance().updatePlayers(GameManager.getInstance().thePlayers);
//                        tiles = GameManager.getInstance().getNewHand();
                        System.out.println("no plays found, hand : "+new String(tiles));
                        System.out.println("THIS AI JUST SENT A DUMMY PLAY");
                        myCache.Clear();
                        if(startIndex) {
                            FindPlays(GameManager.getInstance().theBoard);
                        }
                        else{
                            FindPlayInverted(GameManager.getInstance().theBoard);
                        }
                        JSONArray toSwap = new JSONArray();
                        for(int i = 0; i < tiles.length; i++){
                            if(tiles[i] != 0){
                                toSwap.put((tiles[i]+"").toUpperCase());
                            }
                        }
                        mySocket.emit("swap", toSwap);
                        this.state = 0;
                        //UPDATE MUST BE CALLED OR ELSE THE AI COMES TO A STANDSTILL IF IT DOES NOT FIND A BEST WORD
                        //update();
                    }
            }
        }
            /*
                    System.out.println("my casheCount=" + myCache.count);
                    int breakCounter = 0;
                    do {
                        if (myCache.count <= 0) {
                            TESTFindPlays(GameManager.getInstance().theBoard);
                            play = PlayBestWord();
                        } else {
                            play = PlayBestWord();
                        }
                        breakCounter++;
                    } while (play == null && breakCounter < 10);
                    if (play != null) {
                        System.out.println("emmiting playWord");
                        mySocket.emit("playWord", GameManager.getInstance().JSONifyPlayIdea(play));
                        GameManager.getInstance().theBoard.addWord(play.placements);
                        state = 2;
                    } else {
                        tiles = GameManager.getInstance().getNewHand();
                        myCache.Clear();
                    }
                    break;
                case 2://waitforVerification
                    break;
            }
        }*/
        return;
    }

    /**
     * Remove the Tiles in a play from the AI's hand
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

    public PlayIdea getDummy(){
        ArrayList<Placement> dummyList = new ArrayList<Placement>();
        dummyList.add(new Placement('a', 5, 5));
        PlayIdea dummyShit = new PlayIdea("a", dummyList, (byte)0);
        return dummyShit;
    }

    /**
     * Connect an AI to the backend
     */
    public boolean connectSocket(){
        try{
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            mySocket = IO.socket("http://localhost:3000",opts);
            mySocket.connect();
            socketEvents();
            //emit to server that AI has connected.
            //fetch new tiles?
            /*
            mySocket.on("whoAreYou", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("whoAreYou");
                    JSONObject data = new JSONObject();
                    data.put("isAI",true);
                    mySocket.emit("whoAreYou",data);

                }
            });*/
        }
        catch (Exception e){
            System.err.print(e);
        }
        return false;
    }

    public boolean isConnected(){
        return mySocket.connected();
    }

    /**
     * Kill an AI's connection to the back end
     */
    public void disconnectAI(){
        mySocket.disconnect();
    }

    /**
     * Reconnect an AI to the backend
     */
    public void ReConnectSocket(){
        try {
            IO.Options opts = new IO.Options();
            opts.forceNew = true;
            opts.reconnection = true;
            mySocket = IO.socket("http://localhost:3000", opts);
            mySocket.connect();
        } catch (URISyntaxException e){
            System.err.println(e);
        }
    }

    /**
     * Creates the Socket Listeners for each event the AI should respond to
     */
    public void socketEvents(){
        //while(mySocket.connected()) {
            mySocket.on("whoAreYou", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    //System.out.println();
                    System.out.println(AI.this.name+" got whoAreYou");
                    JSONObject data = new JSONObject();
                    data.put("isAI", true);
                    data.put("team",team);
                    System.out.println(data.toString());
                    mySocket.emit("whoAreYou", data);

                }
            }).on("play", new Emitter.Listener() {
                @Override
                public void call(Object... args) {

                        System.out.println(AI.this.name + " got play");
                        try {
                            JSONObject data = (JSONObject) args[0];
                            System.out.println(data.toString());
                            boolean invalid = data.getBoolean("invalid");
                            if (invalid) {
                                if (state == 2)
                                    System.out.println(AI.this.name + " State set back to 1");
                                    state = 1;
                            } else {
                                if (state == 2)
                                    System.out.println(AI.this.name + " State set to 0");
                                    state = 0;
                            }
                            update();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                }
            }).on("dataUpdate", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println(AI.this.name + " got dataUpdate");
                    try {
                        JSONObject data = (JSONObject) args[0];
                        System.out.println(data.toString());
                        boolean myTurn = data.getBoolean("isTurn");
                        JSONArray jsonTiles = data.getJSONArray("tiles");
                        char[] newTiles = new char[jsonTiles.length()];
                        for(int i = 0; i < newTiles.length; i++){
                            tiles[i] = jsonTiles.getString(i).toLowerCase().charAt(0);
//                            System.out.println(tiles[i]);
                        }

                        //reconnect an AI
                        //System.out.println(myTurn);
                        if (myTurn) {
                            long startTime = System.currentTimeMillis();
                            while(System.currentTimeMillis() - startTime < 6000){
                                if(System.currentTimeMillis() - startTime % 100 == 0) {
                                    System.out.println(System.currentTimeMillis());
                                }
                            }
//                            tiles = GameManager.getInstance().getNewHand();
                            myCache.Clear();
                            if(startIndex) {
                                FindPlays(GameManager.getInstance().theBoard);
                            }
                            else{
                                FindPlayInverted(GameManager.getInstance().theBoard);
                            }
                            state = 1;
//                            tiles = GameManager.getInstance().getNewHand();
                            try {
                                GameManager.getInstance().updatePlayers(GameManager.getInstance().thePlayers);
                            }catch (NullPointerException e){
                                e.printStackTrace();
                            }
                        }
                        else{
                            state = 0;
                        }
                        update();
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }).on("playWord", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println(AI.this.name + " got playWord, but does nothing");
                }
            }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println(AI.this.name + " got disconnect");
                    mySocket.disconnect();
                    mySocket = null;
                }
            }).on(Socket.EVENT_ERROR, new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println(AI.this.name + " got error: "+args[0]);
                }
            });
        //}
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

    public void MyWordWasPlayed(){
        myCache.Clear();
    }







    public void FindPlays(Board boardState){
        //Invalidate the cache
        myCache.Clear();
        this.startIndex = false;
        System.out.println("Starting at bottom left corner");
        //AI ALGORITHM HERE
        boolean hasFoundASinglePlayableTile = false;
        for(int i = 0; i < boardState.the_game_board.length; i ++){
            for(int j = 0; j < boardState.the_game_board[0].length; j++){
                if(boardState.the_game_board[i][j] != null){
                    //System.out.println("Thinking at "+i+", "+j);
                    hasFoundASinglePlayableTile = true;
                    //parse horiz
                    char[] horConstr = new char[11];
                    for(int h = 0; h < boardState.the_game_board.length; h++){
                        if(boardState.the_game_board[h][j] != null)
                            horConstr[h] = boardState.the_game_board[h][j].letter;
                    }
                    //get all possible plays with current tiles and boardstate
                    //System.out.println("getting all possible plays horrizontally");
                    ArrayList<PlayIdea> possiblePlays = WordVerification.getInstance()
                            .TESTgetWordsFromHand(new String(tiles), horConstr, i, boardState.the_game_board[i][j], true);


                    //verify they dont fuck with tiles around where theyd be played
                    for(int p = 0; p < possiblePlays.size(); p++){
                        if(possiblePlays.get(p).placements.size() > 0)
                        if( boardState.verifyWordPlacement(possiblePlays.get(p).placements)){
                            //update that shit
                            myCache.Push(possiblePlays.get(p));
                            GameManager.getInstance().placementsUnderConsideration = possiblePlays.get(p).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                        }
                    }
                    //parse vert
                    char[] vertConstr = new char[11];
                    //for(int v = boardState.the_game_board.length-1; v >= 0; v--){
                    for(int v = 0; v < boardState.the_game_board.length; v++){
                        if(boardState.the_game_board[i][v] != null)
                            vertConstr[10-v] = boardState.the_game_board[i][v].letter;
                    }
                    //get all possible plays with current tiles and boardstate
                    //System.out.println("getting all possible plays vertically!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    ArrayList<PlayIdea> possiblePlaysVert = WordVerification.getInstance()
                            .TESTgetWordsFromHand(new String(tiles), vertConstr, j, boardState.the_game_board[i][j], false);

                    //verify they dont fuck with tiles around where theyd be played
                    for(int p = 0; p < possiblePlaysVert.size(); p++){
                        if(possiblePlaysVert.get(p).placements.size() > 0)
                        if( boardState.verifyWordPlacement(possiblePlaysVert.get(p).placements)){
                            //update that shit
                            myCache.Push(possiblePlaysVert.get(p));
                            GameManager.getInstance().placementsUnderConsideration = possiblePlaysVert.get(p).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                        }
                    }
                }
            }
        }
        if(!hasFoundASinglePlayableTile){
            //System.out.println("ITS THE FIRST MOVE OF THE BOARD OH BOY");
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

    public void FindPlayInverted(Board boardState){
        //Invalidate the cache
        myCache.Clear();
        this.startIndex = true;
        System.out.println("Starting at top right corner");
        //AI ALGORITHM HERE
        boolean hasFoundASinglePlayableTile = false;
        for(int i = boardState.the_game_board.length - 1; i >= 0; i --){
            for(int j = boardState.the_game_board[0].length - 1 ; j >= 0; j--){
                if(boardState.the_game_board[i][j] != null){
                    //System.out.println("Thinking at "+i+", "+j);
                    hasFoundASinglePlayableTile = true;
                    //parse horiz
                    char[] horConstr = new char[11];
                    for(int h = 0; h < boardState.the_game_board.length; h++){
                        if(boardState.the_game_board[h][j] != null)
                            horConstr[h] = boardState.the_game_board[h][j].letter;
                    }
                    //get all possible plays with current tiles and boardstate
                    //System.out.println("getting all possible plays horrizontally");
                    ArrayList<PlayIdea> possiblePlays = WordVerification.getInstance()
                            .TESTgetWordsFromHand(new String(tiles), horConstr, i, boardState.the_game_board[i][j], true);


                    //verify they dont fuck with tiles around where theyd be played
                    for(int p = 0; p < possiblePlays.size(); p++){
                        if(possiblePlays.get(p).placements.size() > 0)
                            if( boardState.verifyWordPlacement(possiblePlays.get(p).placements)){
                                //update that shit
                                myCache.Push(possiblePlays.get(p));
                                GameManager.getInstance().placementsUnderConsideration = possiblePlays.get(p).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                            }
                    }
                    //parse vert
                    char[] vertConstr = new char[11];
                    //for(int v = boardState.the_game_board.length-1; v >= 0; v--){
                    for(int v = 0; v < boardState.the_game_board.length; v++){
                        if(boardState.the_game_board[i][v] != null)
                            vertConstr[10-v] = boardState.the_game_board[i][v].letter;
                    }
                    //get all possible plays with current tiles and boardstate
                    //System.out.println("getting all possible plays vertically!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    ArrayList<PlayIdea> possiblePlaysVert = WordVerification.getInstance()
                            .TESTgetWordsFromHand(new String(tiles), vertConstr, j, boardState.the_game_board[i][j], false);

                    //verify they dont fuck with tiles around where theyd be played
                    for(int p = 0; p < possiblePlaysVert.size(); p++){
                        if(possiblePlaysVert.get(p).placements.size() > 0)
                            if( boardState.verifyWordPlacement(possiblePlaysVert.get(p).placements)){
                                //update that shit
                                myCache.Push(possiblePlaysVert.get(p));
                                GameManager.getInstance().placementsUnderConsideration = possiblePlaysVert.get(p).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                            }
                    }
                }
            }
        }
        if(!hasFoundASinglePlayableTile){
            //System.out.println("ITS THE FIRST MOVE OF THE BOARD OH BOY");
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
        //private ArrayList<ArrayList<Placement>> queue;
        private ArrayList<PlayIdea> queue;

        public PriorityQueue(int cap) {
            //queue = new ArrayList<ArrayList<Placement>>();
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
            queue.clear();;
            count = 0;
        }


    }
}


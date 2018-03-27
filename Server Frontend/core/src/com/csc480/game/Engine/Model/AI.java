package com.csc480.game.Engine.Model;

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

import java.util.ArrayList;

public class AI extends Player {
    private static int counter = 0;
    public PriorityQueue myCache;
    public Socket mySocket;
    public AI(){
        super();
        this.isAI = true;
        this.name = "AI"+(counter++);
        myCache = new PriorityQueue(200);
        connectSocket();
    }

    public boolean connectSocket(){
        try{
            mySocket = IO.socket("http://localhost:3000");
            mySocket.connect();
            //emit to server that AI has connected.
            //fetch new tiles?
            mySocket.on("whoAreYou", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("whoAreYou");
                    JSONObject data = new JSONObject();
                    data.put("isAI",true);
                    mySocket.emit("whoAreYou",data);

                }
            });
        }
        catch (Exception e){
            System.err.print(e);
        }
        return false;
    }

    public boolean isConnected(){
        return mySocket.connected();
    }

    public void disconnectAI(){
        mySocket.disconnect();
    }


    public void socketEvents(){
        while(mySocket.connected()) {
            mySocket.on("whoAreYou", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("whoAreYou");
                    JSONObject data = new JSONObject();
                    data.put("isAI", true);
                    mySocket.emit("whoAreYou", data);

                }
            }).on("newGame", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    try {
                        JSONObject data = (JSONObject) args[0];
                        System.out.println(data.toString());
                        Player response = (Player) data.get("state");
                        if (response.isAI) {
                            //clear cache
                            myCache.Clear();
                        }
                        else{
                            disconnectAI();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).on("newTurn", new Emitter.Listener() {
                @Override
                public void call(Object... args) {
                    System.out.println("newTurn");
                    try {
                        JSONObject data = (JSONObject) args[0];
                        System.out.println(data.toString());
                        boolean myTurn = data.getBoolean("isTurn");
                        //reconnect an AI
                        if (myTurn) {
                            PlayIdea play;
                            if (myCache.size == 0) {
                                TESTFindPlays(GameManager.getInstance().theBoard);
                                play = PlayBestWord();
                            } else {
                                play = PlayBestWord();
                            }
                            try {
                                JSONObject temp = new JSONObject();
                                System.out.println("best plat word=" + play.myWord);
                                temp.put("word", play.myWord);
                                Vector2 pos = play.GetStartPos();
                                temp.put("x", (int) pos.x);
                                temp.put("y", (int) pos.y);
                                temp.put("h", play.isHorizontalPlay());

                                JSONArray words = new JSONArray();
                                words.put(temp);
                                System.out.println(words.toString());
                                Unirest.post("http://localhost:3000/api/game/playWords")
                                        .header("accept", "application/json")
                                        .header("Content-Type", "application/json")
                                        .body(words.toString())
                                        .asString();
                            } catch (Exception e) {
                                System.out.println("Word send to server failed?");
                                e.printStackTrace();
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
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







    public void TESTFindPlays(Board boardState){
        //Invalidate the cache
        myCache.Clear();

        //AI ALGORITHM HERE
        boolean hasFoundASinglePlayableTile = false;
        for(int i = 0; i < boardState.the_game_board.length; i ++){
            for(int j = 0; j < boardState.the_game_board[0].length; j++){
                if(boardState.the_game_board[i][j] != null){
                    System.out.println("Thinking at "+i+", "+j);
                    hasFoundASinglePlayableTile = true;
                    //parse horiz
                    char[] horConstr = new char[11];
                    for(int h = 0; h < boardState.the_game_board.length; h++){
                        if(boardState.the_game_board[h][j] != null)
                            horConstr[h] = boardState.the_game_board[h][j].letter;
                    }
                    //get all possible plays with current tiles and boardstate
                    System.out.println("getting all possible plays horrizontally");
                    ArrayList<PlayIdea> possiblePlays = WordVerification.getInstance()
                            .TESTgetWordsFromHand(new String(tiles), horConstr, i, boardState.the_game_board[i][j], true);


                    //verify they dont fuck with tiles around where theyd be played
                    for(int p = 0; p < possiblePlays.size(); p++){
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
                    System.out.println("getting all possible plays vertically!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    ArrayList<PlayIdea> possiblePlaysVert = WordVerification.getInstance()
                            .TESTgetWordsFromHand(new String(tiles), vertConstr, j, boardState.the_game_board[i][j], false);

                    //verify they dont fuck with tiles around where theyd be played
                    for(int p = 0; p < possiblePlaysVert.size(); p++){
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
            System.out.println("ITS THE FIRST MOVE OF THE BOARD OH BOY");
            TileData centerTile =  new TileData(new Vector2(5,5), (char)0,0,0, this.name, System.currentTimeMillis());
            char[] constraints = new char[11];
            ArrayList<PlayIdea> possiblePlaysCent = WordVerification.getInstance().TESTgetWordsFromHand(new String(tiles), constraints, 5, centerTile, true);
            if(!possiblePlaysCent.isEmpty()) {
                myCache.Push(possiblePlaysCent.get(0));
                GameManager.getInstance().placementsUnderConsideration = possiblePlaysCent.get(0).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
            } else{
//SKIP MY TURN AND REDRAW//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                System.err.println("should skip anf get new tiles");
            }
        }

    }

    /**
     * This will generate all valid moves the AI could play, and add them to the cashe.
     * @param boardState the Current Board
     * @return
     */
    public void FindPlays(Board boardState){
        //Invalidate the cache
        myCache.Clear();

        //AI ALGORITHM HERE

        char ham = GameManager.getInstance().theBoard.the_game_board[0][0].letter;

        int x,y = 0;

        if (GameManager.getInstance().thePlayers.get(0).isAI){








        } else if (GameManager.getInstance().thePlayers.contains(AI.this)){



        } else {
            System.out.println("this is from ai algorithm class's else, there is no AI in thePlayers"
                    + GameManager.getInstance().thePlayers);
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


package com.csc480.game.Engine.Model;

import com.badlogic.gdx.math.Vector2;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.WordVerification;

import java.util.ArrayList;

public class AI extends Player {

    public PriorityQueue myCashe;
    public AI(){
        super();
        this.isAI = true;
        myCashe = new PriorityQueue(200);
    }


    /**
     * This Should be used to actually play a word. If this returns null, call FindPlay() and run this method again.
     * CAUTION: IF A WORD ACTUALLY GETS PLAYED YOU MUST INVALIDATE THE CASHE!
     * @return the best play the AI can think of
     */
    public ArrayList<Placement> PlayBestWord(){
        if(myCashe.size ==0)
            return null;
        ArrayList<Placement> best = myCashe.Pull();
        //myCashe.Clear();
        return best;
    }
    public void MyWordWasPlayed(){
        myCashe.Clear();
    }








    public void TESTFindPlays(Board boardState){
        //Invalidate the cashe
        myCashe.Clear();

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
                    //get all possible plays with current hand and boardstate
                    System.out.println("getting all possible plays horrizontally");
                    ArrayList<ArrayList<Placement>> possiblePlays = WordVerification.getInstance()
                            .TESTgetWordsFromHand(new String(hand), horConstr, i, boardState.the_game_board[i][j], true);

                    //print out all sets of plays
                    System.out.println("All possible plays: len "+possiblePlays.size());
                    for(int q = 0; q < possiblePlays.size(); q++){
                        for (int a = 0; a  < possiblePlays.get(q).size(); a++){
                            System.out.print("("+possiblePlays.get(q).get(a).letter+", "+possiblePlays.get(q).get(a).xPos+", "+possiblePlays.get(q).get(a).yPos+"),");
                        }
                        System.out.println();

                    }
                    //verify they dont fuck with tiles around where theyd be played
                    for(int p = 0; p < possiblePlays.size(); p++){
                        if( boardState.verifyWordPlacement(possiblePlays.get(p))){
                            //update that shit
                            System.out.println("adding to under consideration`");
                            for(int numP = 0; numP < possiblePlays.get(p).size(); numP++){
                                System.out.println(possiblePlays.get(p).get(numP).letter + ": at ("+possiblePlays.get(p).get(numP).xPos+", "+possiblePlays.get(p).get(numP).yPos+")");
                            }
                            myCashe.Push(possiblePlays.get(p));
                            GameManager.getInstance().placementsUnderConsideration = possiblePlays.get(p);
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
                    //get all possible plays with current hand and boardstate
                    System.out.println("getting all possible plays vertically!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                    ArrayList<ArrayList<Placement>> possiblePlaysVert = WordVerification.getInstance()
                            .TESTgetWordsFromHand(new String(hand), vertConstr, j, boardState.the_game_board[i][j], false);

                    //print out all sets of plays
                    System.out.println("All possible plays");
                    for(int q = 0; q < possiblePlaysVert.size(); q++){
                        for (int a = 0; a  < possiblePlaysVert.get(q).size(); a++){
                            System.out.print("("+possiblePlaysVert.get(q).get(a).letter+", "+possiblePlaysVert.get(q).get(a).xPos+", "+possiblePlaysVert.get(q).get(a).yPos+"),");
                        }
                        System.out.println();

                    }

                    //verify they dont fuck with tiles around where theyd be played
                    for(int p = 0; p < possiblePlaysVert.size(); p++){
                        if( boardState.verifyWordPlacement(possiblePlaysVert.get(p))){
                            //update that shit
                            System.out.println("adding to under consideration`");
                            for(int numP = 0; numP < possiblePlaysVert.get(p).size(); numP++){
                                System.out.println(possiblePlaysVert.get(p).get(numP).letter + ": at ("+possiblePlaysVert.get(p).get(numP).xPos+", "+possiblePlaysVert.get(p).get(numP).yPos+")");
                            }
                            myCashe.Push(possiblePlaysVert.get(p));
                            GameManager.getInstance().placementsUnderConsideration = possiblePlaysVert.get(p);
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                        }
                    }
                }
            }
        }
        if(!hasFoundASinglePlayableTile){
            System.out.println("ITS THE FIRST MOVE OF THE BOARD OH BOY");
            TileData centerTile =  new TileData(new Vector2(5,5), '\0',0);
            ArrayList<ArrayList<Placement>> possiblePlaysCent = WordVerification.getInstance().TESTgetWordsFromHand(new String(hand), new char[11], 5, centerTile, true);
            if(!possiblePlaysCent.isEmpty()) {
                myCashe.Push(possiblePlaysCent.get(0));
                GameManager.getInstance().placementsUnderConsideration = possiblePlaysCent.get(0);
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
            } else{
//SKIP MY TURN AND REDRAW//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                System.err.println("should skip anf get new hand");
            }
        }

    }

    /**
     * This will generate all valid moves the AI could play, and add them to the cashe.
     * @param boardState the Current Board
     * @return
     */
    public void FindPlays(Board boardState){
        //Invalidate the cashe
        myCashe.Clear();

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
        private ArrayList<ArrayList<Placement>> queue;

        public PriorityQueue(int cap) {
            queue = new ArrayList<ArrayList<Placement>>();
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
        public void Push(ArrayList<Placement> e){
            if(count == size){
                queue.remove(size-1);
                count--;
            }
            int index = 0;
            while(index < count && e.size() < queue.get(index).size()){
                index++;
            };
            queue.add(index, e);
            count++;
        }

        /**
         * Returns the item with the highest priority
         * @return
         */
        public ArrayList<Placement> Pull(){
            if(count <= 0)
                return null;
            count--;
            return queue.remove(0);

        }

        public void ReConsider(ArrayList<Placement> e){
            queue.remove(e);
            queue.add(e);
        }

        public void Clear(){
            queue.clear();;
            count = 0;
        }

        /**
         * Overwrites the current queue and makes the list e into the used queue
         * @param e
         */
        public void Load(ArrayList<ArrayList<Placement>> e){
            queue = e;
        }
    }
    /**
     * Play idea is literally just to transmute the way we work with word placement to
     *  the way the backend works with placement
     */
    public class PlayIdea {
        public String myWord;
        public ArrayList<Placement> placements;
        public byte proirity;

        public PlayIdea(String word, ArrayList<Placement> toBePlaced, byte c){
            myWord = word;
            placements = toBePlaced;
            proirity = c;
        }

        public boolean isHorizontalPlay(){
            int y = placements.get(0).yPos;
            for (Placement p : placements){
                if(p.yPos != y)
                    return false;
            }
            return true;
        }

        public Vector2 GetStartPos(){
            //we want the minX and maxY
            int minX = Integer.MAX_VALUE;
            int maxY = Integer.MIN_VALUE;

            for (Placement p : placements){
                if(p.xPos < minX)
                    minX = p.xPos;
                if(p.yPos > maxY)
                    maxY = p.yPos;
            }
            return new Vector2(minX,maxY);
        }
    }
}


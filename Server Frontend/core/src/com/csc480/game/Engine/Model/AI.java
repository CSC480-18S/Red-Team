package com.csc480.game.Engine.Model;

import com.badlogic.gdx.math.Vector2;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.WordVerification;

import java.util.ArrayList;

public class AI extends Player {
    private static int counter = 0;
    public PriorityQueue myCashe;
    public AI(){
        super();
        this.isAI = true;
        this.name = "AI"+(counter++);
        myCashe = new PriorityQueue(200);
    }


    /**
     * This Should be used to actually play a word. If this returns null, call FindPlay() and run this method again.
     * CAUTION: IF A WORD ACTUALLY GETS PLAYED YOU MUST INVALIDATE THE CASHE!
     * @return the best play the AI can think of
     */
    public PlayIdea PlayBestWord(){
        if(myCashe.size ==0)
            return null;
        PlayIdea best = myCashe.Pull();
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
                    //get all possible plays with current tiles and boardstate
                    System.out.println("getting all possible plays horrizontally");
                    ArrayList<PlayIdea> possiblePlays = WordVerification.getInstance()
                            .TESTgetWordsFromHand(new String(tiles), horConstr, i, boardState.the_game_board[i][j], true);


                    //verify they dont fuck with tiles around where theyd be played
                    for(int p = 0; p < possiblePlays.size(); p++){
                        if( boardState.verifyWordPlacement(possiblePlays.get(p).placements)){
                            //update that shit
                            myCashe.Push(possiblePlays.get(p));
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
                            myCashe.Push(possiblePlaysVert.get(p));
                            GameManager.getInstance().placementsUnderConsideration = possiblePlaysVert.get(p).placements;
//NEED TO ADD A PLAY IDEA TO THE QUEUE/////////////////////////////////////////////////////////////////////////////////////////////////////////
                        }
                    }
                }
            }
        }
        if(!hasFoundASinglePlayableTile){
            System.out.println("ITS THE FIRST MOVE OF THE BOARD OH BOY");
            TileData centerTile =  new TileData(new Vector2(5,5), (char)0,0);
            char[] constraints = new char[11];
            ArrayList<PlayIdea> possiblePlaysCent = WordVerification.getInstance().TESTgetWordsFromHand(new String(tiles), constraints, 5, centerTile, true);
            if(!possiblePlaysCent.isEmpty()) {
                myCashe.Push(possiblePlaysCent.get(0));
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


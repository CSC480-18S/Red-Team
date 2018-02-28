package com.csc480.game.Engine.Model;

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


    /**
     * This will generate all valid moves the AI could play, and add them to the cashe.
     * @param boardState the Current Board
     * @return
     */
    public void FindPlays(Board boardState){
        //Invalidate the cashe
        myCashe.Clear();

        //AI ALGORITHM HERE


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
            queue.clear();;
            count = 0;
        }

        /**
         * Overwrites the current queue and makes the list e into the used queue
         * @param e
         */
        public void Load(ArrayList<PlayIdea> e){
            queue = e;
        }
    }

    class PlayIdea {
        public String myWord;
        public ArrayList<Placement> placements;
        public byte proirity;

        public PlayIdea(String word, ArrayList<Placement> toBePlaced, byte c){
            myWord = word;
            placements = toBePlaced;
            proirity = c;
        }
    }
}


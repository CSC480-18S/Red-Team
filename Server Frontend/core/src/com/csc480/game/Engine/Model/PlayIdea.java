package com.csc480.game.Engine.Model;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

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

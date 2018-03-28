package com.csc480.game.Engine.Model;

/**
 * A simple data structure to hold playerData
 */
public class Player {
    public char[] hand;
    public int score;
    public String team;
    public boolean isAI;

    public Player(){
        hand = new char[]{0,0,0,0,0,0,0};

        score = 0;
        team = "Not Assigned";
        isAI = false;
    }

}

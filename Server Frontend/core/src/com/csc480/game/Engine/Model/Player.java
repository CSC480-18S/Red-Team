package com.csc480.game.Engine.Model;

public class Player {
    char[] hand;
    int score;
    String team;
    boolean isAI;

    public Player(){
        hand = new char[7];
        score = 0;
        team = "Not Assigned";
        isAI = false;
    }

}

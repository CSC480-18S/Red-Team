package com.csc480.game.Engine.Model;

/**
 * A simple data structure to hold playerData
 */
public class Player {
    public String name;
    //public int position;
    public char[] tiles;
    public boolean turn;
    public int score;
    public String team;
    public boolean isAI;

    public Player(){
        name = "Not Assigned";
        //position = 0;
        tiles = new char[]{0,0,0,0,0,0,0};
        turn = false;
        score = 0;
        team = "Not Assigned";
        isAI = false;
    }
}

package com.csc480.game.Engine.Model;

/**
 * A simple data structure to hold letter placements and their position
 */
public class Placement {
    public int xPos;
    public int yPos;
    public char letter;

    public Placement(char letter, int x, int y){
        xPos = x;
        yPos = y;
        this.letter = letter;
    }
}

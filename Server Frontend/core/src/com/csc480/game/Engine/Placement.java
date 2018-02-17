package com.csc480.game.Engine;

public class Placement {
    public int xPos;
    public int yPos;
    public char letter;

    Placement(char letter, int x, int y){
        xPos = x;
        yPos = y;
        this.letter = letter;
    }
}

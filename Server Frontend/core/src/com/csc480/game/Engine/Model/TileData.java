package com.csc480.game.Engine.Model;

import com.badlogic.gdx.math.Vector2;

/**
 * This is a data node to hold information relevant to a certain tile on the board
 * This may be able to be refactored to remove all getters and setters for prev/next. not sure if theyre needed yet
 */
public class TileData {
    public final Vector2 my_position;
    public final int letter_value;
    public final char letter;

    private Vector2 previous_horizontal;
    private Vector2 previous_vertival;
    private Vector2 next_horizontal;
    private Vector2 next_vertical;


    public TileData(Vector2 pos, char the_letter, int value){
        my_position = pos;
        letter = the_letter;
        letter_value = value;
    }
    public TileData(int x, int y, char the_letter, int value){
        my_position = new Vector2(x,y);
        letter = the_letter;
        letter_value = value;
    }

    public int getX(){
        return (int)my_position.x;
    }
    public int getY(){
        return (int)my_position.y;
    }


    public Vector2 getPrevious_horizontal() {
        return previous_horizontal;
    }

    public boolean setPrevious_horizontal(Vector2 previous_horizontal) {
        if((my_position.x == previous_horizontal.x - 1 || my_position.x == previous_horizontal.x + 1) && this.previous_horizontal == null){
            this.previous_horizontal = previous_horizontal;
            return true;
        }
        return false;

    }

    public Vector2 getPrevious_vertival() {
        return previous_vertival;
    }

    public boolean setPrevious_vertival(Vector2 previous_vertival) {
        if((my_position.y == previous_vertival.y - 1 || my_position.y == previous_vertival.y + 1) && this.previous_vertival == null){
            this.previous_vertival = previous_vertival;
            return true;
        }
        return false;
    }

    public Vector2 getNext_horizontal() {
        return next_horizontal;
    }

    public boolean setNext_horizontal(Vector2 next_horizontal) {
        if((my_position.x == previous_horizontal.x - 1 || my_position.x == next_horizontal.x + 1) && this.next_horizontal == null){
            this.next_horizontal = next_horizontal;
            return true;
        }
        return false;
    }

    public Vector2 getNext_vertical() {
        return next_vertical;
    }

    public boolean setNext_vertical(Vector2 next_vertical) {
        if((my_position.y == next_vertical.y - 1 || my_position.y == next_vertical.y + 1) && this.next_vertical == null){
            this.next_vertical = next_vertical;
            return true;
        }
        return false;
    }
}

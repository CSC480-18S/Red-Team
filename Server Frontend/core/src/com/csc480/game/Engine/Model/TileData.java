package com.csc480.game.Engine.Model;

import com.badlogic.gdx.math.Vector2;

/**
 * This is a data node to hold information relevant to a certain tile on the board
 * This may be able to be refactored to remove all getters and setters for prev/next. not sure if theyre needed yet
 */
public class TileData {
    public final Vector2 my_position;//this._x = x, this._y = y
    public final int letter_value;
    public final int multiplier; //this._multiplier = multiplier
    public final char letter;//backend uses '.' as it's null delim. We use '\0' or (char)0
    public final String playedBy;// this._playedBy = ''
    public final long timePlayedAt;//this._timePlayedAt = nul


    public TileData(Vector2 pos, char the_letter, int value, int bonus, String player, long timePlayed){
        my_position = pos;
        letter = the_letter;
        letter_value = value;
        playedBy = player;
        timePlayedAt = timePlayed;
        multiplier = bonus;
    }
    public TileData(int x, int y, char the_letter, int value, int bonus, String player, long timePlayed){
        my_position = new Vector2(x,y);
        letter = the_letter;
        letter_value = value;
        playedBy = player;
        timePlayedAt = timePlayed;
        multiplier = bonus;
    }

    public int getX(){
        return (int)my_position.x;
    }
    public int getY(){
        return (int)my_position.y;
    }

}

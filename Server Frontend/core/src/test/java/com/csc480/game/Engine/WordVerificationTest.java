package com.csc480.game.Engine;

import com.badlogic.gdx.math.Vector2;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.Engine.Model.TileData;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WordVerificationTest {
/*
    @Test
    public void TESTgetWordsFromHand1() {
        WordVerification.getInstance();
        char[] constraints = new char[11];
        constraints[5] = 'a';
        TileData root = new TileData(new Vector2(5, 5),'a', 0);
        ArrayList<ArrayList<Placement>> actual = WordVerification.getInstance().TESTgetWordsFromHand("t", constraints, 6, root,false);
        assertEquals(actual.get(0).get(0).letter,'t');
        assertEquals(5,actual.get(0).get(0).xPos);
        assertEquals(4,actual.get(0).get(0).yPos);
    }
    */
    @Test
    public void TESTgetWordsFromHand2() {
        WordVerification.getInstance();
        char[] constraints = new char[11];
        constraints[0] = 'x';
        constraints[3] = 't';
        constraints[4] = 'e';
        constraints[5] = 's';
        constraints[6] = 't';
        TileData root = new TileData(new Vector2(6, 5),'t', 0);
        ArrayList<ArrayList<Placement>> actual = WordVerification.getInstance().TESTgetWordsFromHand("ed", constraints, 6, root,true);
        assertEquals(actual.get(0).get(0).letter,'e');
        assertEquals(7,actual.get(0).get(0).xPos);
        assertEquals(5,actual.get(0).get(0).yPos);
        assertEquals(actual.get(0).get(0).letter,'d');
        assertEquals(8,actual.get(0).get(1).xPos);
        assertEquals(5,actual.get(0).get(0).yPos);
    }

    @Test
    public void genRegex() {
    }
}
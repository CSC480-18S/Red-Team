package com.csc480.game.Engine;

import com.badlogic.gdx.math.Vector2;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.Engine.Model.PlayIdea;
import com.csc480.game.Engine.Model.TileData;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class WordVerificationTest {

    @Test
    public void TESTgetWordsFromHand1() {
        WordVerification.getInstance();
        char[] constraints = new char[11];
        constraints[5] = 'a';
        TileData root = new TileData(new Vector2(5, 5),'a',0,0,"", 0);
        ArrayList<PlayIdea> actual = WordVerification.getInstance().TESTgetWordsFromHand("t", constraints, 5, root,false);
        assertEquals(actual.get(0).placements.get(0).letter,'t');
        assertEquals(5,actual.get(0).placements.get(0).xPos);
        assertEquals(4,actual.get(0).placements.get(0).yPos);
    }

    @Test
    public void TESTgetWordsFromHand2() {
        WordVerification.getInstance();
        char[] constraints = new char[11];
        constraints[0] = 'x';
        constraints[3] = 'n';
        constraints[4] = 'e';
        constraints[5] = 's';
        constraints[6] = 't';
        TileData root = new TileData(new Vector2(6, 5),'t', 0, 0, "", 0);
        ArrayList<PlayIdea> actual = WordVerification.getInstance().TESTgetWordsFromHand("ed", constraints, 6, root,true);
        assertEquals('e',actual.get(0).placements.get(0).letter);
        assertEquals(7,actual.get(0).placements.get(0).xPos);
        assertEquals(5,actual.get(0).placements.get(0).yPos);
        assertEquals('d',actual.get(0).placements.get(1).letter);
        assertEquals(8,actual.get(0).placements.get(1).xPos);
        assertEquals(5,actual.get(0).placements.get(0).yPos);
    }

    @Test
    public void TESTgetWordsFromHand3() {
        WordVerification.getInstance();
        char[] constraints = new char[11];
        constraints[4] = 'n';
        constraints[5] = 'e';
        constraints[6] = 'w';
        constraints[10] = 'x';
        TileData root = new TileData(new Vector2(4, 5),'n', 0, 0, "", 0);
        ArrayList<PlayIdea> actual = WordVerification.getInstance().TESTgetWordsFromHand("re", constraints, 4, root,true);
        System.out.println(actual.size());
        assertEquals('e',actual.get(2).placements.get(0).letter);
        assertEquals(3,actual.get(2).placements.get(0).xPos);
        assertEquals(5,actual.get(2).placements.get(0).yPos);
        assertEquals('r',actual.get(2).placements.get(1).letter);
        assertEquals(2,actual.get(2).placements.get(1).xPos);
        assertEquals(5,actual.get(2).placements.get(0).yPos);
    }
    @Test
    public void TESTgetWordsFromHand4() {
        WordVerification.getInstance();
        char[] constraints = new char[11];
        TileData root =  new TileData(new Vector2(5,5), (char)0,0, 0, "", 0);
        ArrayList<PlayIdea> actual = WordVerification.getInstance().TESTgetWordsFromHand("aa", constraints, 5, root,true);
        System.out.println(actual.size());
        assertEquals('a',actual.get(0).placements.get(0).letter);
        assertEquals(4,actual.get(0).placements.get(0).xPos);
        assertEquals(5,actual.get(0).placements.get(0).yPos);
        assertEquals('a',actual.get(0).placements.get(1).letter);
        assertEquals(5,actual.get(0).placements.get(1).xPos);
        assertEquals(5,actual.get(0).placements.get(1).yPos);

    }


    @Test
    public void genRegex() {
    }
}
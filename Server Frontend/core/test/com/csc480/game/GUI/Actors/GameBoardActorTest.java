package com.csc480.game.GUI.Actors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameBoardActorTest {

    GameBoardActor test;
    float rotatedBy;
    @Before
    public void makeBoard() {
        test = new GameBoardActor();
    }

    @Test
    public void rotateByTC_00() {
        rotatedBy = 0;
        test.rotateBy(rotatedBy);
    }

    @Test
    public void rotateByTC_01() {
        rotatedBy = 0;
        test.rotateBy(rotatedBy);
    }

    @Test
    public void rotateByTC_02() {
        rotatedBy = 0;
        test.rotateBy(rotatedBy);
    }

    @Test
    public void rotateByTC_03() {
        rotatedBy = 0;
        test.rotateBy(rotatedBy);
    }

    @Test
    public void rotateByTC_04() {
        rotatedBy = 0;
        test.rotateBy(rotatedBy);
    }

}
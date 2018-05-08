package com.csc480.game.GUI;

import com.csc480.game.OswebbleGame;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameScreenTest {
    OswebbleGame testGame;
    GameScreen test;

    @Before
    public void makeGame() {
        testGame = new OswebbleGame();
    }

    @Test
    public void GameScreenTC_00() {
        test = new GameScreen(testGame);
        assertNotNull(test);
    }

    @Test
    public void GameScreenTC_02() {
        testGame = null;
        test = new GameScreen(testGame);
        assertNotNull(test);
    }

}
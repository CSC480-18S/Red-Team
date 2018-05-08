package com.csc480.game.GUI.Actors;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileActorTest {
    TileActor test;
    char letter;

    @Test
    public void TileActorTC_00() {
        letter = 'A';
        test = new TileActor(letter);
        assertNotNull(test);
    }

    @Test
    public void TileActorTC_01() {
        letter = 'a';
        test = new TileActor(letter);
        assertNotNull(test);
    }

    @Test
    public void TileActorTC_03() {
        test = new TileActor(letter);
        assertNotNull(test);
    }

}
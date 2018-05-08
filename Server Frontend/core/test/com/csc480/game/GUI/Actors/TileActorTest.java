package com.csc480.game.GUI.Actors;

import com.csc480.game.TestRunner;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TileActorTest extends TestRunner {

    @Test
    public void TileActorTC_00() {
        TileActor tile = new TileActor('A');
        assertNotNull(tile);
    }

    @Test
    public void TileActorTC_01() {
        TileActor tile = new TileActor('a');
        assertNotNull(tile);
    }

}
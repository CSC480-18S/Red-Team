package com.csc480.game.GUI.Actors;

import com.csc480.game.Engine.Model.Player;
import com.csc480.game.TestRunner;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HandActorTest extends TestRunner {

    private HandActor test;

    @Before
    public  void setup() {
        test = new HandActor(false, 0);
    }

    @Test
    public void addTileTC_00() {
        TileActor tile = new TileActor('a');
        test.addTile(tile);
        for (int i = 0; i < test.myHand.size(); i++) {
            assertNotNull(test.myHand.get(i));
        }
    }

    @Test
    public void removeTileTC_00() {
        TileActor tile = new TileActor('A');
        test.addTile(tile);
        assertTrue(test.removeTile(tile));
    }

    @Test
    public void removeTileTC_02() {
        assertFalse(test.removeTile(null));
    }

    @Test
    public void setPlayerTC_00() {
        Player player = new Player();
        test.setPlayer(player);
        assertEquals(test.name.toString(), "name: " + player.name);
    }

    @Test
    public void setPlayerTC_02() {
        test.setPlayer(null);
        assertEquals(test.name.toString(), "name: Not Assigned");
    }

}
package com.csc480.game.GUI.Actors;

import com.csc480.game.Engine.Model.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HandActorTest {
    HandActor test;
    TileActor tileActor01;
    Player player01;
    boolean remove = false;
    @Before
    public  void makeHand() {
        test = new HandActor(false, 0);
    }

    @Test
    public void addTileTC_00() {
        tileActor01 = new TileActor('a');
        test.addTile(tileActor01);
        for (int i = 0; i < test.myHand.size(); i++) {
            System.out.println(test.myHand.get(i));
        }

    }

    @Test
    public void addTileTC_02() {
        tileActor01 = null;
        test.addTile(tileActor01);
    }

    @Test
    public void removeTileTC_00() {
        addTileTC_00();
        remove = test.removeTile(tileActor01);
        assertTrue(remove);

    }

    @Test
    public void removeTileTC_02() {
        tileActor01 = null;
        remove = test.removeTile(tileActor01);
        assertTrue(remove);

    }

    @Test
    public void setPlayerTC_00() {
            player01 = new Player();
            test.setPlayer(player01);
            assertNotNull(test.name);

    }

    @Test
    public void setPlayerTC_02() {
        player01 = null;
        test.setPlayer(player01);

    }



}
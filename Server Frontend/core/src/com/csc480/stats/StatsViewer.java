package com.csc480.stats;

import com.badlogic.gdx.Game;
import com.csc480.stats.GUI.StatsScreen;

public class StatsViewer extends Game {
    public StatsScreen screen;
    @Override
    public void create() {
        //for testing purposes only
       StatsConnection stats = new StatsConnection();
       System.out.println(stats.GreenStats().get().getData().getName());

        screen = new StatsScreen(this);
        setScreen(screen);
    }
}

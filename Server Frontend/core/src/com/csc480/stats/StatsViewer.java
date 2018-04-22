package com.csc480.stats;

import com.badlogic.gdx.Game;
import com.csc480.stats.GUI.StatsScreen;

public class StatsViewer extends Game {
    public StatsScreen screen;
    @Override
    public void create() {
        //for testing purposes only, model all connections as such
        //create global statsConnection object
       StatsConnection stats = new StatsConnection();
       if (stats.GoldStats().isPresent()) {
           //if present, unwrap the optional and get the specific attribute
           System.out.println(stats.GoldStats().get().getData().getName());
           System.out.println(stats.GoldStats().get().getData().getTotalScore());
       } else {
           //otherwise the JSON was empty, most likely meaning the backend wasn't running
           System.out.println("Server isn't running, please start the server and try again.");
       }

        screen = new StatsScreen(this);
        setScreen(screen);
    }
}

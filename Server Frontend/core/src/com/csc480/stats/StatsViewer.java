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
//           System.out.println("Main stats:");
//           System.out.println(stats.GoldStats().get().getData().getName());
//           System.out.println(stats.GoldStats().get().getData().getTotalScore());
//           System.out.println(stats.GoldStats().get().getData().getFrequentlyPlayedWords().get(0).getWord());
//           System.out.println(stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().get(0).getWord());
//           System.out.println("Other stats:");
//           System.out.println(stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().get(0).getScore());
//           System.out.println(stats.GoldGameScores().get().getData().get_embedded().getGameResults().get(0).getScore());
//           System.out.println(stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().get(0).getUsername());
//           System.out.println(stats.GoldPlayers().get().getData().get_embedded().getPlayers().get(0).getUsername());
//           System.out.println(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(0).getWord());
//           System.out.println(stats.GoldLongestWord().get().getData().getWord());
//           System.out.println("T-test: " + stats.calcTTest());
       } else {
           //otherwise the JSON was empty, most likely meaning the backend wasn't running
           System.out.println("Server isn't running, please start the server and try again.");
       }

        screen = new StatsScreen(this);
        setScreen(screen);
    }
}

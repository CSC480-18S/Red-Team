package com.csc480.stats.jsonObjects;

/**
 * This class exists solely so GSON can correctly
 * parse the JSON returned from the database representing
 * the team stats
 */

import java.util.ArrayList;

public class ScoreData {

    private ArrayList<Score> gameResults;

    public ArrayList<Score> getGameResults() {
        return gameResults;
    }

    public void setGameResults(ArrayList<Score> gameResults) {
        this.gameResults = gameResults;
    }
}

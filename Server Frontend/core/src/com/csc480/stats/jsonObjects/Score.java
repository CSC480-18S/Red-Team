package com.csc480.stats.jsonObjects;

/**
 * This class exists solely so GSON can correctly
 * parse the JSON returned from the database representing
 * the team stats
 */

public class Score {

    private String score;
    private String win;
    private String lose;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getWin() {
        return win;
    }

    public void setWin(String win) {
        this.win = win;
    }

    public String getLose() {
        return lose;
    }

    public void setLose(String lose) {
        this.lose = lose;
    }
}

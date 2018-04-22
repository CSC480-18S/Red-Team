package com.csc480.stats.jsonObjects;

/**
 * This class exists solely so GSON can correctly
 * parse the JSON returned from the database representing
 * the team stats
 */

import java.util.ArrayList;

public class Words {

    private ArrayList<Word> playedWords;

    public ArrayList<Word> getPlayedWords() {
        return playedWords;
    }

    public void setPlayedWords(ArrayList<Word> playedWords) {
        this.playedWords = playedWords;
    }
}

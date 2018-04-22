package com.csc480.stats.jsonObjects;

/**
 * This class exists so GSON can parse the JSON from database
 * representing the team stats and populate the
 * appropriate fields in the GUI
 */

public class WordData {
    private String word;
    private String frequency;

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getFrequency() {
        return frequency;
    }

    public void setFrequency(String frequency) {
        this.frequency = frequency;
    }
}

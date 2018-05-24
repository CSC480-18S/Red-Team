package com.csc480.stats.jsonObjects;

import java.util.ArrayList;

/**
 * This class exists so GSON can parse the JSON from database
 * representing the team stats and populate the
 * appropriate fields in the GUI
 */

public class TeamData {
    private String name;
    private String totalScore;
    private ArrayList<WordData> frequentlyPlayedWords;
    private String dirtyCount;
    private String specialCount;
    private String winCount;
    private String loseCount;
    private ArrayList<WordData> frequentlySpecialPlayedWords;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getWinCount() {
        return winCount;
    }

    public void setWinCount(String winCount) {
        this.winCount = winCount;
    }

    public String getLoseCount() {
        return loseCount;
    }

    public void setLoseCount(String loseCount) {
        this.loseCount = loseCount;
    }

    public String getDirtyCount() {
        return dirtyCount;
    }

    public void setDirtyCount(String dirtyCount) {
        this.dirtyCount = dirtyCount;
    }

    public String getSpecialCount() {
        return specialCount;
    }

    public void setSpecialCount(String specialCount) {
        this.specialCount = specialCount;
    }

    public ArrayList<WordData> getFrequentlyPlayedWords() {
        return frequentlyPlayedWords;
    }

    public void setFrequentlyPlayedWords(ArrayList<WordData> frequentlyPlayedWords) {
        this.frequentlyPlayedWords = frequentlyPlayedWords;
    }

    public ArrayList<WordData> getFrequentlySpecialPlayedWords() {
        return frequentlySpecialPlayedWords;
    }

    public void setFrequentlySpecialPlayedWords(ArrayList<WordData> frequentlySpecialPlayedWords) {
        this.frequentlySpecialPlayedWords = frequentlySpecialPlayedWords;
    }

}

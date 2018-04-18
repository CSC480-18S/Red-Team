package com.csc480.stats;

import java.util.ArrayList;

/**
 * This class exists so GSON can parse the JSON from database
 * representing the team stats and populate the
 * appropriate fields in the GUI
 */

public class TeamData {
    private String name;
    private String totalScore;
    private ArrayList<String> highestValueWords; //array
    private String longestWord;
    private ArrayList<String> frequentlyPlayedWords; //array
    private String dirtyCount;
    private String specialCount;
    //typo is on purpose to replicate the DB typo
    private String higestSingleGameScore;
    private String winCount;
    private String loseCount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongestWord() {
        return longestWord;
    }

    public void setLongestWord(String longestWord) {
        this.longestWord = longestWord;
    }

    public String getHigestSingleGameScore() {
        return higestSingleGameScore;
    }

    public void setHigestSingleGameScore(String higestSingleGameScore) {
        this.higestSingleGameScore = higestSingleGameScore;
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

    public ArrayList<String> getHighestValueWords() {
        return highestValueWords;
    }

    public void setHighestValueWords(ArrayList<String> highestValueWords) {
        this.highestValueWords = highestValueWords;
    }

    public ArrayList<String> getFrequentlyPlayedWords() {
        return frequentlyPlayedWords;
    }

    public void setFrequentlyPlayedWords(ArrayList<String> frequentlyPlayedWords) {
        this.frequentlyPlayedWords = frequentlyPlayedWords;
    }
}

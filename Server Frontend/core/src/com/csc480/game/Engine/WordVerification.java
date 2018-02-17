package com.csc480.game.Engine;

import java.util.ArrayList;

/**
 * THIS CLASS WILL NEED A MASSIVE OVERHAUL TO WORK PROPERLY WITH THE DATABASE
 */
public class WordVerification {
    private static WordVerification instance;
    //"validWords" TO BE USED ONLY FOR TESTING!
    private static ArrayList<String> validWords;

    public static WordVerification getInstance(){
        if (instance == null)
            new WordVerification();
        return instance;
    }

    WordVerification(){
        instance = this;
        validWords = new ArrayList<String>();
        validWords.add("a");
        validWords.add("cat");
        validWords.add("cats");
        validWords.add("at");
        validWords.add("hat");
        validWords.add("tag");
        validWords.add("bag");
        validWords.add("hag");
        validWords.add("chat");
        validWords.add("go");
        validWords.add("gos");
        validWords.add("goose");
        validWords.add("ose");
        validWords.add("so");
        validWords.add("no");
        validWords.add("boo");
        validWords.add("low");

    }

    public boolean isWord(String word){
        return validWords.contains(word);
    }
}

package com.csc480.game.Engine;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * This singleton class has utilities the AI needs for thinking of words
 */
public class WordVerification {
    private static WordVerification instance;
    private static HashSet<String> validWords;

    public static WordVerification getInstance(){
        if (instance == null)
            new WordVerification();
        return instance;
    }

    private WordVerification(){
        instance = this;

        validWords = new HashSet<String>();
        File file;
        Scanner inFileScanner;
        try{
            Long startTime = System.nanoTime();
            System.out.println("Starting Creating HashSet");
            file = new File(Gdx.files.internal("words.txt").path());
            inFileScanner = new Scanner(file);
            while (inFileScanner.hasNext()){
                validWords.add(inFileScanner.nextLine());
            }
            System.out.println("Finished Creating HashSet total nanos: "+(System.nanoTime()-startTime));
        }catch (FileNotFoundException e){
            System.err.println(e);
        }

    }

    /**
     * This function will verify that a string is a valid word in our data set
     * @param word the string to verify
     * @return if the string is in the set of know words
     */
    public boolean isWord(String word){
        Long startTime = System.nanoTime();
        System.out.println("Starting Searching: "+startTime);
        boolean has = validWords.contains(word);
        System.out.println("Finished Searching nanos: "+(System.nanoTime()-startTime));
        return has;
    }

    /**
     * This function will generate all permutations of a hand, based on
     *      the constraints of size and existing tiles
     * @param hand the chars in the AI's hand
     * @param constraints the spots that the AI could play letters,
     *                    with existing letters inserted into correct positions
     * @return an arraylist of all permutations
     */
    public ArrayList<String> generatePermutations(char[] hand, char[] constraints){
        ArrayList<String> toRet = new ArrayList<String>();

        return toRet;
    }
}

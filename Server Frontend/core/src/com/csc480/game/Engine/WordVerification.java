package com.csc480.game.Engine;

import com.badlogic.gdx.Gdx;
import com.csc480.game.Engine.Model.Placement;

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
            int c = 0;
            while (inFileScanner.hasNext()){
                c++;
                validWords.add(inFileScanner.nextLine());
            }
            System.out.println("Finished Creating HashSet total num = "+c+", nanos: "+(System.nanoTime()-startTime));
        }catch (FileNotFoundException e){
            System.err.println(e);
        }

    }

    public ArrayList<String> getWordsFromHand(String hand, char[] constraints, Placement currentTile, int index){
        String handAndReleventBoardTiles = hand;
        //generate the regex template for the current tile.
        String regex = genRegex(constraints, index);
        for(int i = 0; i < constraints.length; i++)
            if(constraints[i] != 0)
                handAndReleventBoardTiles += constraints[i];
        ArrayList<String> possibleWords = new ArrayList<String>();
        for(String e: validWords){
            String temp = handAndReleventBoardTiles;
            boolean isGoodFlag = true;
            if(e.length() <= constraints.length){
                for(int i = 0; i < e.length(); i++){
                    if(temp.contains(e.charAt(i)+"")){
                        temp.replace(e.charAt(i),'_');
                    }else {
                        isGoodFlag = false;
                        break;
                    }
                }
                if(isGoodFlag){
                    if(e.matches(regex)) {
                        possibleWords.add(e);
                    }
                }
            }
        }
        System.out.println("Found "+possibleWords.size()+" possible words.");
        return possibleWords;

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
     * @param constraints array of the current tiles in the line of the start index
     * @param startIndex current tile that verification is being check for
     * @return regex template for the given start index
     */
    public String genRegex(char constraints[], int startIndex){
        int emptySpace = 0;
        String regex = "";
        for(char temp: constraints){
            //if the char value is \0 there is a blank space there
            if(temp == '\0'){
                emptySpace++;
            }
            else{
                //if the constraint index is the start index, the char there is a mandatory char for the played word
                if(temp == constraints[startIndex]){
                    //any character 0 to emptySpace times, followed by the startIndex character
                    regex += ".{0," + emptySpace + "}" + temp;
                }
                else {
                    //if there is one empty space after a static character, optional statements come into play
                    if(emptySpace == 1){
                        //word either ends after the static character, or has a single any char followed by the static char following it
                        regex += "($|." + temp + ")";
                    }
                    else {
                        //any char 0 to emptySpace -1 then end, OR the current num of empty spaces followed by the static char
                        regex += "(.{0," + (emptySpace-1) + "}$|{"+emptySpace+"}"+temp + ")";
                    }
                }
                //reset the empty space could after it is used.
                emptySpace = 0;
            }
        }
        //constraint for the last tile to the edge of the board
        regex += ".{0," + emptySpace + "}";
        return regex;
    }
}

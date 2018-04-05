package com.csc480.game.Engine;

import com.badlogic.gdx.Gdx;
import com.csc480.game.Engine.Model.AI;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.Engine.Model.PlayIdea;
import com.csc480.game.Engine.Model.TileData;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.regex.PatternSyntaxException;

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
            //System.out.println("Starting Creating HashSet");
            file = new File(Gdx.files.internal("words.txt").path());
            //file = new File("C:\\Users\\chris\\Desktop\\Red-Team\\Server Frontend\\core\\assets\\words.txt");
            inFileScanner = new Scanner(file);
            int c = 0;
            while (inFileScanner.hasNext()){
                c++;
                validWords.add(inFileScanner.nextLine());
            }
            //System.out.println("Finished Creating HashSet total num = "+c+", nanos: "+(System.nanoTime()-startTime));
        }catch (FileNotFoundException e){
            System.err.println(e);
        }

    }

    public ArrayList<PlayIdea> TESTgetWordsFromHand(String hand, char[] constraints, int index, TileData root, boolean horrizontal){
        ////////////
        long startTime = System.currentTimeMillis();
        String TESTPRINTconstraints = "";
        for(int i = 0; i < constraints.length; i++){
            if(constraints[i] == 0)
                TESTPRINTconstraints += '-';
            else
                TESTPRINTconstraints += constraints[i];
        }
        //System.out.println("TESTgetWordsFromHand called with: tiles ="+hand+", constraints="+TESTPRINTconstraints+", index="+index+"isHor="+horrizontal);
        ////////////

        String handAndReleventBoardTiles = hand;
        //generate the regex template for the current tile.
        String regex = genRegex(constraints, index);
        for(int i = 0; i < constraints.length; i++)
            if(constraints[i] != 0)
                handAndReleventBoardTiles += constraints[i];
        ArrayList<PlayIdea> possiblePlays = new ArrayList<PlayIdea>();
        if(root.letter == 0){
            //System.out.println("First play, so we can skip a bunch");
            for(String e: validWords){
                String temp = handAndReleventBoardTiles;
                boolean isGoodFlag = true;
                if(e.length() <= constraints.length) {
                    for (int i = 0; i < e.length(); i++) {
                        if (temp.contains(e.charAt(i) + "")) {
                            temp = temp.replaceFirst(e.charAt(i) + "", '_' + "");
                        } else {
                            //System.out.println("letter: "+ e.charAt(i) +" of "+e + " doesnt fit temp: "+temp+" of handntiles: "+handAndReleventBoardTiles);
                            isGoodFlag = false;
                            break;
                        }
                    }
                    if (isGoodFlag) {
                        //System.out.println(e+" was good");
                        ArrayList<Placement> play = new ArrayList<Placement>();
                        int playRoot = e.length()/2;
                        for(int i = 0; i < e.length(); i++){
                            play.add(new Placement(e.charAt(i), ((int) root.my_position.x-playRoot+i), (int)root.my_position.y));
                        }
                        PlayIdea p = new PlayIdea(e, play, (byte)play.size());
                        possiblePlays.add(p);
                    }
                }
                if(System.currentTimeMillis() - startTime > 10000)
                    return possiblePlays;
            }
            //if there is nothing on the board then what is in the hand is valid
            return possiblePlays;
        }
        for(String e: validWords){
            String temp = handAndReleventBoardTiles;
            boolean isGoodFlag = true;
            if(e.length() <= constraints.length){
                for(int i = 0; i < e.length(); i++){
                    if(temp.contains(e.charAt(i)+"")){
                        temp = temp.replaceFirst(e.charAt(i)+"",'_'+"");

                    }else {
                        //System.out.println("letter: "+ e.charAt(i) +" of "+e + " doesnt fit temp: "+temp+" of handntiles: "+handAndReleventBoardTiles);
                        isGoodFlag = false;
                        break;
                    }
                }
                if(isGoodFlag){
                    try{
                        //System.out.println("e: "+e+" matched? regex "+regex+"="+e.matches(regex));
                        if(e.matches(regex)) {
                            //System.out.println(e + " matched regex: "+regex);
                            //Transform result to placement
                            ArrayList<Placement> play = new ArrayList<Placement>();
                            if(root.letter != 0) {
                                //System.out.println("not first play case");
                //THERE IS A MASSIVE BUG HERE WHERE IF THE WORD HAS 2+ letters the same!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                                int playRoot = e.indexOf(constraints[index]);
                                //parse left
                                for (int i = playRoot; i > 0; i--) {
                                    //create a new placement @
                                    if(index-i >= constraints.length)break;
                                    //System.out.println("constraint@" + (index+i-playRoot-1) + " = 0:" + (constraints[index+i-playRoot-1] == 0));
                                    if (constraints[index+i-playRoot-1] == 0) {
                                        if (horrizontal) {
                                            //System.out.println("adding a placement of "+e.charAt(i-1)+"("+(i-1)+") @ ("+(index+i-playRoot-1)+","+(int) root.my_position.y+") cause its blank here");
                                            play.add(new Placement(e.charAt(i - 1), index+i-playRoot-1, (int) root.my_position.y));
                                        }else {
                                            //System.out.println("adding a placement of "+e.charAt(i-1)+"("+(i-1)+") @ ("+((int) root.my_position.x)+","+(index+i-playRoot-1)+") cause its blank here");
                                            play.add(new Placement(e.charAt(i - 1), (int) root.my_position.x, 10 - (index+i-playRoot-1)));
                                        }
                                    }
                                }
                                //System.out.println("parsing right now");
                                //parse right
                                for (int i = playRoot+1; i < e.length(); i++) {
                                    //create a new placement @
                                    if(index+i-playRoot >= constraints.length){
                                        //System.out.println("breaking greater");
                                        break;
                                    }
                                    if(index+i-playRoot < 0){
                                        //System.out.println("breaking lower");
                                        break;
                                    }
                                    //System.out.println("constraint@" + (index+i-playRoot) + " = 0:" + (constraints[index + i - playRoot] == 0));
                                    if (constraints[index + i - playRoot] == 0) {
                                        if (horrizontal) {
                                            //System.out.println("adding a placement of "+e.charAt(i)+"("+(i)+") @ ("+(index + i - playRoot)+","+(int) root.my_position.y+") cause its blank here");
                                            play.add(new Placement(e.charAt(i), index + i - playRoot, (int) root.my_position.y));
                                        }
                                        else {
                                            //System.out.println("adding a placement of "+e.charAt(i)+"("+(i)+") @ ("+((int) root.my_position.x)+","+(index+i-playRoot)+") cause its blank here");
                                            play.add(new Placement(e.charAt(i), (int) root.my_position.x, 10- (index + i - playRoot)));
                                        }
                                    }
                                }
                                //System.out.println("Adding to possible plays from Word Verification:" + PrintPlay(play));
                                PlayIdea p = new PlayIdea(e,play,(byte) play.size());
                                possiblePlays.add(p);
                            }else {
                                //System.out.println("handle the first play case");
                                //if (root.letter == 0)
                                if (horrizontal) {
                                    for (int w = 0; w < e.length(); w++) {
                                        play.add(new Placement(e.charAt(w), (index - e.length() / 2) + w, (int) root.my_position.y));
                                    }
                                } else {
                                    for (int w = 0; w < e.length(); w++) {
                                        play.add(new Placement(e.charAt(w), (int) root.my_position.y, 10- ((index - e.length() / 2) + w)));
                                    }
                                }
                                //play.add(new Placement(e.charAt(playRoot),(int)root.my_position.y,(int)root.my_position.y));
                                //System.out.println("Adding to possible plays from Word Verification:" + PrintPlay(play));
                                PlayIdea p = new PlayIdea(e,play,(byte)play.size());
                                possiblePlays.add(p);
                            }
                        }
                    }catch (PatternSyntaxException exp){
                        //System.err.println(exp.getMessage());
                        exp.printStackTrace();
                    }
                }
            }else {
                //System.out.println("word cant be longer than constraints");
            }
            if(System.currentTimeMillis() - startTime > 10000)
                return possiblePlays;
        }
        //System.out.println("Found "+possiblePlays.size()+" possible words.");
        return possiblePlays;

    }

    public ArrayList<String> getWordsFromHand(String hand, char[] constraints, int index){
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
                        temp = temp.replace(e.charAt(i),'_');
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
        //System.out.println("Found "+possibleWords.size()+" possible words.");
        return possibleWords;

    }
    /**
     * This function will verify that a string is a valid word in our data set
     * @param word the string to verify
     * @return if the string is in the set of know words
     */
    public boolean isWord(String word){
        Long startTime = System.nanoTime();
        //System.out.println("Starting Searching: "+startTime);
        boolean has = validWords.contains(word);
        //System.out.println("Finished Searching nanos: "+(System.nanoTime()-startTime));
        return has;
    }

    public String genRegexOld(char constraints[], int startIndex){
        int emptySpace = 0;
        int paranthesis = 0;
        String regex = "";
        for (int i = 0; i < constraints.length; i++) {
            //if the char value is \0 there is a blank space there
            if (constraints[i] == '\0') {
                emptySpace++;
            } else {
                //if the constraint index is the start index, the char there is a mandatory char for the played word
                if (i == startIndex) {
                    //System.out.println("if " + i);
                    if (paranthesis > 0) {
                        //any character 0 to emptySpace times, followed by the startIndex character
                        if (emptySpace == 0) {

                        } else {
                            regex += ".{0," + (emptySpace - 1) + "}";
                        }
                        for (int j = 0; j < paranthesis; j++) {
                            regex += ")";
                        }
                        regex += constraints[i];

                    } else {
                        if (emptySpace == 0) {
                            regex += constraints[i];
                        } else {
                            regex += ".{0," + (emptySpace - 1) + "}" + constraints[i];
                        }
                    }
                } else if (i < startIndex) {
                    //System.out.println("else if " + i);
                    //System.out.println(constraints[i + 1]);
                    if (constraints[i + 1] == '\0') {
                        regex += "(.{0," + emptySpace + "}" + constraints[i] + "|";
                        paranthesis++;
                    } else {
                        regex += constraints[i];
                    }
                } else {
                    //System.out.println("else " + i);
                    //if there is one empty space after a static character, optional statements come into play
                    if (emptySpace == 1) {
                        //word either ends after the static character, or has a single any char followed by the static char following it
                        regex += "($|." + constraints[i] + ")";
                    } else if (emptySpace == 0) {
                        regex += constraints[i];
                    } else {
                        //any char 0 to emptySpace -1 then end, OR the current num of empty spaces followed by the static char
                        regex += "(.{0," + (emptySpace - 1) + "}$|.{" + emptySpace + "}" + constraints[i] + ")";
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
    /**
     * @param constraints array of the current tiles in the line of the start index
     * @param startIndex current tile that verification is being check for
     * @return regex template for the given start index
     */
    public static String genRegex(char constraints[], int startIndex){
        int emptySpace = 0;
        int paranthesis = 0;
        String regex = "";
        for (int i = 0; i < constraints.length; i++) {
            //if the char value is \0 there is a blank space there
            if (constraints[i] == '\0') {
                if(i == startIndex && paranthesis > 0){
                    for (int j = 0; j < paranthesis; j++) {
                        if(emptySpace == 0) {
                            regex += "|.{0})";

                        }
                        else{
                            regex += "|.{0," + (emptySpace - 1) + "})";
                        }
                    }
                }
                emptySpace++;
            } else {
                //if the constraint index is the start index, the char there is a mandatory char for the played word
                if (i == startIndex) {
                    if (paranthesis > 0) {
                        //any character 0 to emptySpace times, followed by the startIndex character
                        if (emptySpace == 0) {

                        } else {
                            regex += ".{" + (emptySpace) + "}";
                        }
                        for (int j = 0; j < paranthesis; j++) {
                            if(emptySpace == 0) {
                                regex += "|.{0})";
                            }
                            else{
                                regex += "|.{0," + (emptySpace - 1) + "})";
                            }
                        }
                        regex += constraints[i];

                    } else {
                        if (emptySpace == 0) {
                            regex += constraints[i];
                        } else {
                            regex += ".{0," + (emptySpace - 1) + "}" + constraints[i];
                        }
                    }
                } else if (i < startIndex) {
                    /*if (constraints[i + 1] == '\0') {
                        regex += "(.{0," + emptySpace + "}" + constraints[i] + "|";
                        paranthesis++;
                    } else {*/
                    if(paranthesis == 0){
                        if(emptySpace == 0){
                            regex += "(.{0}" + constraints[i];
                            paranthesis++;
                        }
                        else {
                            regex += "(.{0," + emptySpace + "}" + constraints[i];
                            paranthesis++;
                        }
                    } else if (emptySpace == 0){
                        regex += constraints[i];
                    }
                    else{
                        regex += ".{0," + emptySpace + "}" + constraints[i];
                    }
                } else {
                    //if there is one empty space after a static character, optional statements come into play
                    if (emptySpace == 1) {
                        //word either ends after the static character, or has a single any char followed by the static char following it
                        regex += "($|." + constraints[i] + ")";
                        paranthesis++;
                    } else if (emptySpace == 0) {
                        regex += constraints[i];
                    } else {
                        //any char 0 to emptySpace -1 then end, OR the current num of empty spaces followed by the static char
                        regex += "(.{0," + (emptySpace - 1) + "}$|.{" + emptySpace + "}" + constraints[i] + ")";
                        paranthesis++;
                    }
                }
                //reset the empty space could after it is used.
                emptySpace = 0;
            }
        }
        //constraint for the last tile to the edge of the board
        regex += ".{0," + emptySpace + "}";
        //System.out.println(regex);
        return regex;
    }
    public static String PrintPlay(ArrayList<Placement> toPrint){
        String print = "Play len="+toPrint.size()+": ";
        for (int a = 0; a  < toPrint.size(); a++){
            print= print+"("+toPrint.get(a).letter+", "+toPrint.get(a).xPos+", "+toPrint.get(a).yPos+"),";
        }
        return print;
    }
}

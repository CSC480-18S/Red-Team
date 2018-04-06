package com.csc480.game.Engine.Model;

import com.badlogic.gdx.math.Vector2;
import com.csc480.game.Engine.WordVerification;

import java.util.ArrayList;

/**
 * This is the data structure that holds the board state.
 * It provides functionality for word verification based on board state.
 */
public class Board {
    public TileData[][] the_game_board;

    public Board(int size){
       if(size % 2 != 1) System.err.println("Board isnt of n even size: "+size);
       the_game_board = new TileData[size][size];

    }

    /**
     * Will add a word to the board weither it is valid or not
     * @param placements the tiles to be updated, exclusive of existing tiles
     */
    public void addWord(ArrayList<Placement> placements){
        TileData temp;
        for(Placement p : placements){
            if(the_game_board[p.xPos][p.yPos] == null) {
                temp = new TileData(new Vector2(p.xPos, p.yPos), p.letter, 0, 0, "temp", System.currentTimeMillis());
                the_game_board[p.xPos][p.yPos] = temp;
            }
        }
    }

    /**
     * Determines if a set of placements is in fact a valid move given the state of the board
     * @param placements the tiles to be updated, exclusive of existing tiles
     * @return true iff the move is valid
     */
    public boolean verifyWordPlacement(ArrayList<Placement> placements){
        if(placements.isEmpty()){
            //System.out.println("cant verify an empty placements");
            return false;
        }
        //System.out.println("verifying word placement.");
        for(int i = 0; i < placements.size(); i++){
            //System.out.println("testing "+placements.get(i).letter + ": at ("+placements.get(i).xPos+", "+placements.get(i).yPos+")");

        }
        //System.out.println();

        //assume it is both
        boolean isHorrizontal = true;
        boolean isVertical = true;
        boolean connectedToWordFlag = true;
        ArrayList<Placement> myCopyOfPlacements = (ArrayList<Placement>) placements.clone();
        TileData[][] test_game_board = new TileData[the_game_board.length][the_game_board.length];
        if(myCopyOfPlacements == null){
            return false;
        }
        if(myCopyOfPlacements.get(0) == null){
            return false;
        }
        int xFlag = myCopyOfPlacements.get(0).xPos;
        int yFlag = myCopyOfPlacements.get(0).yPos;
        int minX = myCopyOfPlacements.get(0).xPos;
        int minY = myCopyOfPlacements.get(0).yPos;
        int maxX = myCopyOfPlacements.get(0).xPos;
        int maxY = myCopyOfPlacements.get(0).yPos;

        //Ensure the tiles are not overlapping any existing tiles

        for(Placement p : myCopyOfPlacements){
            if(the_game_board[p.xPos][p.yPos] != null) {
                //if(the_game_board[p.xPos][p.yPos].letter != p.letter) {
                    System.err.println("Tried to play \"" + p.letter + "\" at not null tile ("
                            + p.xPos + ", " + p.yPos + ") where \"" + the_game_board[p.xPos][p.yPos].letter + "\" already is");
                    return false;
                //}
            }
        }
        //create a test gameboard to see if everything works out
        for(int i = 0; i < the_game_board.length; i++){
            for(int j = 0; j < the_game_board[0].length; j++)
                test_game_board[i][j] = the_game_board[i][j];
        }
        for(Placement p : myCopyOfPlacements){
            test_game_board[p.xPos][p.yPos] = new TileData(new Vector2(p.xPos,p.yPos),p.letter,0, 0, "temp", System.currentTimeMillis());
        }


        //Ensure that every tile is touching an existing tile OR the sequence contains the center
        boolean inCenter = false;
        //Run through the game board and ensure that at least one tile is touching an existing tile from the real game board
        boolean somethingConnectFlag = false;
        for(Placement p : placements){
            //Something is not connected when left, right, top, and bottom are null
            //There are 9 cases. The 4 sides and the 4 corners and a middle!
            if(p.xPos == 0){//left cases
                if(p.yPos == 0){//bottom left corner
                    if(the_game_board[p.xPos +1][p.yPos] != null
                            || the_game_board[p.xPos][p.yPos +1] != null){
                        somethingConnectFlag = true;
                    }
                } else if(p.yPos == the_game_board[0].length-1){//top left corner
                    if(the_game_board[p.xPos +1][p.yPos] != null
                            || the_game_board[p.xPos][p.yPos -1] != null){
                        somethingConnectFlag = true;
                    }
                }else {//left wall
                    if(the_game_board[p.xPos +1][p.yPos] != null
                            || the_game_board[p.xPos][p.yPos +1] != null
                            || the_game_board[p.xPos][p.yPos -1] != null){
                        somethingConnectFlag = true;
                    }
                }
            } else if(p.xPos == the_game_board.length-1) {//right cases
                if(p.yPos == 0){//bottom right corner
                    if(the_game_board[p.xPos -1][p.yPos] != null
                            || the_game_board[p.xPos][p.yPos +1] != null){
                        somethingConnectFlag = true;
                    }

                } else if(p.yPos == the_game_board[0].length-1){//top right corner
                    if(the_game_board[p.xPos -1][p.yPos] != null
                            || the_game_board[p.xPos][p.yPos -1] != null){
                        somethingConnectFlag = true;
                    }
                }else {//right wall
                    if(the_game_board[p.xPos -1][p.yPos] != null
                            || the_game_board[p.xPos][p.yPos +1] != null
                            || the_game_board[p.xPos][p.yPos -1] != null){
                        somethingConnectFlag = true;
                    }
                }
            } else {//middle cases
                if(p.yPos == 0){//bottom wall
                    if(the_game_board[p.xPos +1][p.yPos] != null
                            || the_game_board[p.xPos -1][p.yPos] != null
                            || the_game_board[p.xPos][p.yPos +1] != null){
                        somethingConnectFlag = true;
                    }
                } else if(p.yPos == the_game_board[0].length-1){//top wall
                    if(the_game_board[p.xPos +1][p.yPos] != null
                            || the_game_board[p.xPos -1][p.yPos] != null
                            || the_game_board[p.xPos][p.yPos -1] != null){
                        somethingConnectFlag = true;
                    }

                }else {//middle middle
                    if(the_game_board[p.xPos +1][p.yPos] != null
                            || the_game_board[p.xPos -1][p.yPos] != null
                            || the_game_board[p.xPos][p.yPos +1] != null
                            || the_game_board[p.xPos][p.yPos -1] != null){
                        somethingConnectFlag = true;
                    }
                }
            }
        }
       if(somethingConnectFlag == false){//none of the tiles would touch a word
           //so it must be in the center to be valid
           for(Placement p: placements){
               if(p.xPos == test_game_board.length/2 && p.yPos == test_game_board[0].length/2) inCenter = true;
           }
           if(!inCenter){
               //System.out.println("not in center nor connected to a word");
               return false;
           }
       }

        for(Placement p : placements){
            //Something is not connected when left, right, top, and bottom are null
            //There are 9 cases. The 4 sides and the 4 corners and a middle!
            if(p.xPos == 0){//left cases
                if(p.yPos == 0){//bottom left corner
                    if(test_game_board[p.xPos +1][p.yPos] == null
                            && test_game_board[p.xPos][p.yPos +1] == null){
                        connectedToWordFlag = false;
                    }
                } else if(p.yPos == test_game_board[0].length-1){//top left corner
                    if(test_game_board[p.xPos +1][p.yPos] == null
                            && test_game_board[p.xPos][p.yPos -1] == null){
                        connectedToWordFlag = false;
                    }
                }else {//left wall
                    if(test_game_board[p.xPos +1][p.yPos] == null
                            && test_game_board[p.xPos][p.yPos +1] == null
                            && test_game_board[p.xPos][p.yPos -1] == null){
                        connectedToWordFlag = false;
                    }
                }
            } else if(p.xPos == test_game_board.length-1) {//right cases
                if(p.yPos == 0){//bottom right corner
                    if(test_game_board[p.xPos -1][p.yPos] == null
                            && test_game_board[p.xPos][p.yPos +1] == null){
                        connectedToWordFlag = false;
                    }

                } else if(p.yPos == test_game_board[0].length-1){//top right corner
                    if(test_game_board[p.xPos -1][p.yPos] == null
                            && test_game_board[p.xPos][p.yPos -1] == null){
                        connectedToWordFlag = false;
                    }
                }else {//right wall
                    if(test_game_board[p.xPos -1][p.yPos] == null
                            && test_game_board[p.xPos][p.yPos +1] == null
                            && test_game_board[p.xPos][p.yPos -1] == null){
                        connectedToWordFlag = false;
                    }
                }
            } else {//middle cases
                if(p.yPos == 0){//bottom wall
                    if(test_game_board[p.xPos +1][p.yPos] == null
                            && test_game_board[p.xPos -1][p.yPos] == null
                            && test_game_board[p.xPos][p.yPos +1] == null){
                        connectedToWordFlag = false;
                    }
                } else if(p.yPos == test_game_board[0].length-1){//top wall
                    if(test_game_board[p.xPos +1][p.yPos] == null
                            && test_game_board[p.xPos -1][p.yPos] == null
                            && test_game_board[p.xPos][p.yPos -1] == null){
                        connectedToWordFlag = false;
                    }

                }else {//middle middle
                    if(test_game_board[p.xPos +1][p.yPos] == null
                            && test_game_board[p.xPos -1][p.yPos] == null
                            && test_game_board[p.xPos][p.yPos +1] == null
                            && test_game_board[p.xPos][p.yPos -1] == null){
                        connectedToWordFlag = false;
                    }
                }
            }
        }
        if(!connectedToWordFlag){
            //the word is not connected to another word, so it will have to be in the center to be valid
            for(Placement p: placements){
                if(p.xPos == test_game_board.length/2 && p.yPos == test_game_board[0].length/2) inCenter = true;
            }
            if(!inCenter){
                //System.out.println("not in center nor connected to a word");
                return false;
            }
        }

        //Directional Logic assumes that more than one tile is being placed
        if(myCopyOfPlacements.size() > 1) {
            //figure out if its horizontal, vertical or neither by finding a deviation in the X's or Y's or both
            //check vertically
            for (Placement p : myCopyOfPlacements){
                if(p.xPos != xFlag) isVertical = false;
                if(p.xPos > maxX) maxX = p.xPos;
                if(p.xPos < minX) minX = p.xPos;

            }
            //check horizontally
            for (Placement p : myCopyOfPlacements){
                if(p.yPos != yFlag) isHorrizontal = false;
                if(p.yPos > maxY) maxY = p.yPos;
                if(p.yPos < minY) minY = p.yPos;
            }
            //Then if both flags are true or both flags are false the letters are diagonal
            if(isHorrizontal == isVertical){
                //System.out.println("letters are diagonal");
                return false;
            }


            //we now know the direction the user intends to play the tiles
            //we now know the min and max positions of the tiles being placed

            //now we must search through the line of tiles and ensure that all tiles in the APPLICABLE row/col sum to a valid word
            if(isHorrizontal){
                //now we must check if the word is a real allowed word if it is, check the whole horizontal word
                //System.out.println("checking base horizontal word");
                //System.out.println("Status: minX="+minX+" maxX="+maxX+" y="+minY);
                //Ensure rhe word is not null from min to max
                for(int i = minX; i <= maxX; i++){
                    if(test_game_board[i][minY] == null){
                        //System.out.println("there is a null space in the middle of word");
                        return false;
                    }
                }

                if(isHorizontalValid(minX,maxX,minY,myCopyOfPlacements,test_game_board)){
                    //the horizontal is a word
                    // we must check above and below for each letter they place iff there are letters above or below the current
                    //  in order to see if placing a tile there creates an invalid word
                    for(Placement p : myCopyOfPlacements){
                        ArrayList<Placement> temp = new ArrayList<Placement>();
                        temp.add(p);
                        //there are three cases that could arise, the word is played at the top, middle, or bottom
                        //max case
                        if(p.yPos == test_game_board[0].length-1){
                            //System.out.println("only check below");
                            if(test_game_board[p.xPos][p.yPos -1] != null){
                                //System.out.println("verifying below word");
                                if(!isVerticalValid(p.yPos,p.yPos,p.xPos,temp,test_game_board)){
                                    //System.out.println("It was not a valid vertical subset returning false");
                                    return false;
                                }
                            }
                            //min case
                        }else if(p.yPos == 0){
                            //System.out.println("only check above");
                            if(test_game_board[p.xPos][p.yPos +1] != null){
                                //System.out.println("verifying above word");
                                if(!isVerticalValid(p.yPos,p.yPos,p.xPos,temp,test_game_board)){
                                    //System.out.println("It was not a valid vertical subset returning false");
                                    return false;
                                }
                            }
                            //middle case
                        }else{
                            //System.out.println("we should only check if above is not null or below is not null");
                            if(test_game_board[p.xPos][p.yPos -1] != null || test_game_board[p.xPos][p.yPos +1] != null){
                                //System.out.println("verifying above and below word");
                                if(!isVerticalValid(p.yPos,p.yPos,p.xPos,temp,test_game_board)){
                                    //System.out.println("It was not a valid vertical subset returning false");
                                    return false;
                                }
                            }
                        }
                    }
                } else {
                   // System.out.println("it is not a valid horizontally");
                    return false;
                }
            }
            if(isVertical){
                //now we must check if the word is a real allowed word if it is,...
                //System.out.println("checking base vertical word");
                //System.out.println("Status: minX="+minX+" maxX="+maxX+" y="+minY);
                //Ensure rhe word is not null from min to max
                for(int i = minY; i <= maxY; i++){
                    if(test_game_board[minX][i] == null){
                        //System.out.println("there was a null tile in the middle of word");
                        return false;
                    }
                }
                if(isVerticalValid(minY,maxY,minX,myCopyOfPlacements,test_game_board)){
                    //the horizontal is a word
                    // we must check left and right for each letter they place iff there are letters left or right of the current
                    //  in order to see if placing a tile there creates an invalid word
                    for(Placement p : myCopyOfPlacements){
                        ArrayList<Placement> temp = new ArrayList<Placement>();
                        temp.add(p);
                        //there are three cases that could arise, the word is played at the leftmost, middle, or rightmost
                        //rightmost case
                        if(p.xPos == test_game_board.length-1){
                            //only check to the left
                            if(test_game_board[p.xPos -1][p.yPos] != null){
                                //System.out.println("verifying left of word");
                                if(!isHorizontalValid(p.xPos,p.xPos,p.yPos,temp,test_game_board)){
                                    //System.out.println("It was not a valid horizontal subset returning false");
                                    return false;
                                }
                            }
                            //leftmost case
                        }else if(p.xPos == 0){
                            //only check to the right
                            if(test_game_board[p.xPos +1][p.yPos] != null){
                                //System.out.println("verifying right of word");
                                if(!isHorizontalValid(p.xPos,p.xPos,p.yPos,temp,test_game_board)){
                                    //System.out.println("It was not a valid horizontal subset returning false");
                                    return false;
                                }
                            }
                            //middle case
                        }else{
                            //we should only check if left is not null or right is not null
                            if(test_game_board[p.xPos -1][p.yPos] != null || test_game_board[p.xPos +1][p.yPos] != null){
                                //System.out.println("verifying left and right of word");
                                if(!isHorizontalValid(p.xPos,p.xPos,p.yPos,temp,test_game_board)){
                                    //System.out.println("It was not a valid horizontal subset returning false");
                                    return false;
                                }
                            }
                        }
                    }
                } else{
                    //System.out.println("it was not valid vertically");
                    return false;
                }
            }
        } else {
            //Only one tile is being placed
            Placement p = myCopyOfPlacements.get(0);
            isHorrizontal = false;
            isVertical = false;
            //we must check both horizontally and vertically
            //horizontal check
            //if there is a neighbor to the left or right, much validate horizontally
            //there are three equivalence classes: left, right and middle
            if(p.xPos == test_game_board.length-1){
                //only check to the left
                if(test_game_board[p.xPos -1][p.yPos] != null){
                    //System.out.println("checking left of word");
                    isHorrizontal = true;
                }
                //leftmost case
            }else if(p.xPos == 0){
                //only check to the right
                if(test_game_board[p.xPos +1][p.yPos] != null){
                    //System.out.println("checking right of word");
                    isHorrizontal = true;

                }
                //middle case
            }else{
                //we should only check if left is not null or right is not null
                if(test_game_board[p.xPos -1][p.yPos] != null || test_game_board[p.xPos +1][p.yPos] != null){
                    //System.out.println("checking left and right of word");
                    isHorrizontal = true;
                }
            }
            //if there is a neighbor above or below, the word must validate vertically
            //there are three equivalence classes: top, bottom and middle
            if(p.yPos == test_game_board[0].length-1){
                //System.out.println("only check below");
                if(test_game_board[p.xPos][p.yPos -1] != null){
                    //System.out.println("checking below word");
                    isVertical = true;
                }
                //min case
            }else if(p.yPos == 0){
                //System.out.println("only check above");
                if(test_game_board[p.xPos][p.yPos +1] != null){
                    //System.out.println("checking above word");
                    isVertical = true;

                }
                //middle case
            }else{
                //System.out.println("we should only check if above is not null or below is not null");
                if(test_game_board[p.xPos][p.yPos -1] != null || test_game_board[p.xPos][p.yPos +1] != null){
                    //System.out.println("checking above and below word");
                    isVertical = true;
                }
            }
            //we now know if there are horizontally or vertically connected words
            if(isHorrizontal){//there exists a horizontally connected tile
                if(!isHorizontalValid(p.xPos,p.xPos,p.yPos,myCopyOfPlacements,test_game_board)){
                    //System.out.println("horizontal was not a valid word");
                    return false;
                }
            }
            if(isVertical){//there exists a vertically connected tile
                if(!isVerticalValid(p.yPos,p.yPos,p.xPos,myCopyOfPlacements,test_game_board)){
                    //System.out.println("vertical was not a valid word");
                    return false;
                }
            }
        }

        //System.out.println("IT WAS A VALID PLAY");
        return true;
    }

    /**
     * Determins if a set of placements is valid from a min to max start position given a hypothetical board state
     * @param minX the start X position
     * @param maxX the End X position
     * @param Y the row to check
     * @param toBePlaced the set of new placemnts to check
     * @param temp the hypothetical board state
     * @return true iff it forms a valid word
     */
    private boolean isHorizontalValid(int minX, int maxX, int Y, ArrayList<Placement> toBePlaced, TileData[][] temp){
        TileData[][] test_game_board = temp;
        //all y positions in placements are equal
        //begin searching for the true minimum x position, The true min will be when the minX-1 == null or minX == 0
        while(minX > 0 && test_game_board[minX-1][Y] != null){
            minX--;
        }
        //begin searching for the true maximum x position, The true max will be when the maxX+1 == null or maxX == the last array position
        while(maxX < test_game_board.length-1 && test_game_board[maxX+1][Y] != null){
            maxX++;
        }
        //we now have the true min and true max so we can parse through the line and generate a word
        String word = "";
        boolean usedToBePlacedLetterFlag;
        for (int i = minX; i <= maxX; i++) {
            usedToBePlacedLetterFlag = false;
            //we must make sure that the tile we are looking at isnt null, and all the to be placed Tiles would be at this time
            for (Placement p : toBePlaced) {
                if (p.xPos == i){ word += p.letter;
                usedToBePlacedLetterFlag = true;}
            }
            if(!usedToBePlacedLetterFlag){
                if(test_game_board[i][Y] == null) return false;
                word += test_game_board[i][Y].letter;
            }
        }
        //System.out.print("Checking horizontal word: "+word+" = ");

        //now we must check if the word is a real allowed word if it is,
        boolean isItAWord = WordVerification.getInstance().isWord(word);
        //System.out.println(isItAWord);
        return isItAWord;

    }

    /**
     * Determins if a set of placements is valid from a min to max start position given a hypothetical board state
     * @param minY the start Y position
     * @param maxY the End Y position
     * @param X the column to check
     * @param toBePlaced the set of new placemnts to check
     * @param temp the hypothetical board state
     * @return true iff it forms a valid word
     */
    private boolean isVerticalValid(int minY, int maxY, int X,  ArrayList<Placement> toBePlaced, TileData[][] temp){
        TileData[][] test_game_board = temp;

        while(minY > 0 && test_game_board[X][minY-1] != null){
            minY--;
        }
        //begin searching for the true maximum x position, The true max will be when the maxX+1 == null or maxX == the last array position
        while(maxY < test_game_board.length-1 && test_game_board[X][maxY+1] != null){
            maxY++;
        }
        //we now have the true min and true max so we can parse through the line and generate a word
        String word = "";
        boolean usedToBePlacedLetterFlag;
        for (int i = maxY; i >= minY; i--) {
            usedToBePlacedLetterFlag = false;
            //we must make sure that the tile we are looking at isnt null, and all the to be placed Tiles would be at this time
            for (Placement p : toBePlaced) {
                if (p.yPos == i){ word += p.letter;
                    usedToBePlacedLetterFlag = true;}
            }
            if(!usedToBePlacedLetterFlag){
                if(test_game_board[X][i] == null) return false;
                word += test_game_board[X][i].letter;
            }
        }
        //System.out.print("Checking vertical word: "+word+" = ");

        //now we must check if the word is a real allowed word if it is,
        boolean isItAWord = WordVerification.getInstance().isWord(word);
        //System.out.println(isItAWord);
        return isItAWord;
    }

    public String PrintBoard(){
        String ret = " 012345678910";
        for(int i = 0; i < the_game_board.length; i++){
            ret+= i;
            for(int j = 0; j < the_game_board[0].length; j++){
                if(the_game_board[i][j] == null)
                    ret+="_";
                else
                    ret+=the_game_board[i][j].letter;
            }
            ret+="\n";
        }
        return ret;
    }

    public Board getCopy() {
        Board temp = new Board(11);
        TileData[][] ret = new TileData[11][11];
        for(int i = 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                if(the_game_board[i][j] != null)
                    ret[i][j] = new TileData(the_game_board[i][j]);//.
            }
        }
        temp.the_game_board = ret;
        return temp;
    }
}


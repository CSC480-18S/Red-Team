package com.csc480.game.Engine;

import com.badlogic.gdx.math.MathUtils;
import com.csc480.game.Engine.Model.*;
import com.csc480.game.OswebbleGame;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

/**
 * The Class that will hold all the game state and route Events to the GUI, SocketManager, and AI
 */
public class GameManager {
    private static GameManager instance;
    public OswebbleGame theGame;
    public ArrayList<Placement> placementsUnderConsideration;//ones being considered
    public ArrayList<Player> thePlayers;
    public int numPlayers;
    public int currentPlayerIndex;
    public Board theBoard;
    public int greenScore;
    public int goldScore;
    public boolean gameOver;


    public static GameManager getInstance() {
        if(instance == null)
            instance = new GameManager();
        return instance;
    }

    private GameManager(){
        thePlayers = new ArrayList<Player>();
        placementsUnderConsideration = new ArrayList<Placement>();
        theBoard = new Board(OswebbleGame.BOARD_SIZE);
        SocketManager.getInstance().ConnectSocket();
        SocketManager.getInstance().setUpEvents();
    }

    public void Dispose(){
        TextureManager.getInstance().Dispose();
    }

    public void updatePlayers(ArrayList<Player> backEndPlayers, int currentPlayer){
        currentPlayerIndex = currentPlayer;
        //Update the Players
        for(Player p : backEndPlayers){
            //cover existing players
            boolean exists = false;
            if(theGame.theGameScreen.top.getPlayer().name.compareTo(p.name) == 0){
                Player inHand = theGame.theGameScreen.top.getPlayer();
                inHand.tiles = p.tiles;
                inHand.turn = p.turn;
                inHand.team = p.team;
                inHand.score = p.score;
                inHand.isAI = p.isAI;
                theGame.theGameScreen.top.updateState();
                exists = true;
            } else
            if(theGame.theGameScreen.bottom.getPlayer().name.compareTo(p.name) == 0){
                Player inHand = theGame.theGameScreen.bottom.getPlayer();
                inHand.tiles = p.tiles;
                inHand.turn = p.turn;
                inHand.team = p.team;
                inHand.score = p.score;
                inHand.isAI = p.isAI;
                theGame.theGameScreen.bottom.updateState();
                exists = true;
            } else
            if(theGame.theGameScreen.left.getPlayer().name.compareTo(p.name) == 0){
                Player inHand = theGame.theGameScreen.left.getPlayer();
                inHand.tiles = p.tiles;
                inHand.turn = p.turn;
                inHand.team = p.team;
                inHand.score = p.score;
                inHand.isAI = p.isAI;
                theGame.theGameScreen.top.updateState();
                exists = true;
            } else
            if(theGame.theGameScreen.right.getPlayer().name.compareTo(p.name) == 0){
                Player inHand = theGame.theGameScreen.right.getPlayer();
                inHand.tiles = p.tiles;
                inHand.turn = p.turn;
                inHand.team = p.team;
                inHand.score = p.score;
                inHand.isAI = p.isAI;
                theGame.theGameScreen.top.updateState();
                exists = true;
            }

            //cover new players
            if(!exists){
                //replace an AI in thePlayers arraylist with new player
                //replace an AI in the GUI with this new player
                if(theGame.theGameScreen.right.getPlayer().isAI){
                    thePlayers.remove(theGame.theGameScreen.right.getPlayer());
                    thePlayers.add(p);
                    theGame.theGameScreen.right.setPlayer(p);
                    theGame.theGameScreen.right.updateState();
                }else if(theGame.theGameScreen.left.getPlayer().isAI){
                    thePlayers.remove(theGame.theGameScreen.left.getPlayer());
                    thePlayers.add(p);
                    theGame.theGameScreen.left.setPlayer(p);
                    theGame.theGameScreen.left.updateState();
                }else if(theGame.theGameScreen.top.getPlayer().isAI){
                    thePlayers.remove(theGame.theGameScreen.top.getPlayer());
                    thePlayers.add(p);
                    theGame.theGameScreen.top.setPlayer(p);
                    theGame.theGameScreen.top.updateState();
                }else if(theGame.theGameScreen.bottom.getPlayer().isAI){
                    thePlayers.remove(theGame.theGameScreen.bottom.getPlayer());
                    thePlayers.add(p);
                    theGame.theGameScreen.bottom.setPlayer(p);
                    theGame.theGameScreen.bottom.updateState();
                }else {
                    throw new UnsupportedOperationException("There are no places for a new Player to join");
                }
            }
        }
        numPlayers = backEndPlayers.size();
        if(numPlayers < 4){
            //make a new AI
            int numGreenPlayers = 0;
            int numGoldPlayers = 0;
            for(Player p : backEndPlayers){
                if(p.team.compareTo("green") == 0)
                    numGreenPlayers++;
                if(p.team.compareTo("gold") == 0)
                    numGoldPlayers++;
            }
            AI tempAI = new AI();
            if(numGoldPlayers > numGreenPlayers)
                tempAI.team = "green";
            if(numGoldPlayers <= numGreenPlayers)
                tempAI.team = "gold";
            SocketManager.getInstance().BroadcastNewAI(tempAI);
        }
    }

    /**
     * This will force the Board state to sync with the backend data, should only be used to recover from failures
     */
    public void hardUpdateBoardState(){
        theBoard.the_game_board = SocketManager.getInstance().getBoardState();
    }

    public void wordHasBeenPlayed(TileData[][] backendBoardState, Player thatMadePlay, String word, int points){
        if(thatMadePlay.team.compareTo("green") == 0){
            greenScore += points;
        }else if(thatMadePlay.team.compareTo("gold") == 0){
            goldScore += points;
        } else {
            //the player doesnt have a team
        }
        ArrayList<TileData> newPlays = getPlacementsFromBackendThatArentOnFrontEnd(backendBoardState);
        ArrayList<Placement> conversion = new ArrayList<Placement>();
        for(TileData t : newPlays){
            conversion.add(new Placement(t.letter,t.getX(),t.getY()));
        }
//CALL SOMETHING FROM @GUI///////////////////////////////////////////////////////////////////////////////
        //but for now we just add the plays
        if(conversion.size() > 0)
            theBoard.addWord(conversion);
/////////////////////////////////////////////////////////////////////////////////////////////////////////


    }

    /**
     * This will propagate the player joined event to all necessary locations,
     * @param p
     */
    public void playerHasJoined(Player p){
//CALL SOMETHING FROM @GUI//////////////////////////////////////////////////////////////////////////////

    }
    /**
     * This will propagate the player left event to all necessary locations
     * @param p
     */
    public void playerHasLeft(Player p) {
//CALL SOMETHING FROM @GUI/////////////////////////////////////////////////////////////////////////////
    }

    public void currentAIMakePlay(){
        Player current = thePlayers.get(currentPlayerIndex);
        if(current.isAI){
            System.out.println("Finding all AI plays for tiles");
            Long startTime = System.nanoTime();
            ((AI)current).TESTFindPlays(theBoard);
            System.out.println("finding all possible AI plays took nanos: "+(System.nanoTime()-startTime));
            PlayIdea bestPlay = ((AI)current).PlayBestWord();
            while(bestPlay != null && !theBoard.verifyWordPlacement(bestPlay.placements)){
                bestPlay = ((AI)current).PlayBestWord();
                if(bestPlay == null) break;
            }

            if(bestPlay != null && bestPlay.myWord != null && theBoard.verifyWordPlacement(bestPlay.placements)){
                System.out.println("The AI found made a decent play");
                SocketManager.getInstance().BroadcastAIPlay(bestPlay);
                placementsUnderConsideration.clear();
            }
        }
    }

    /**
     * Not sure if i need to do this or the backend will update the AI with new tiles
     * @param num
     * @return
     */
    public ArrayList<Character> getNewTiles(int num){
        ArrayList<Character> ret = new ArrayList<Character>();
//THIS IS FOR TESTING ONLY SHOULD CONNECT TO THE BACKEND THROUGH THE SOCKET MANAGER/////////////////////////////////////////////////////
        for(int i = 0; i < num; i++){
            ret.add(new Character((char) (MathUtils.random(25)+97)));
        }
/////////////////////////////////////////////////////////////////////////////////////////////////////////////
        return ret;
    }


    /**
     * ASSUMES THAT THE (0,0) tile is in the bottom left tiles corner!!!
     * @param backend
     * @return
     */
    public ArrayList<TileData> getPlacementsFromBackendThatArentOnFrontEnd(TileData[][] backend){
        ArrayList<TileData> diff = new ArrayList<TileData>();
        for(int i = 0; i < backend.length; i++){
            for(int j = 0; j < backend[0].length; j++){
                if((backend[i][j] != null && theBoard.the_game_board[i][j] == null) ||
                        (backend[i][j].letter != theBoard.the_game_board[i][j].letter)){
                    diff.add(backend[i][j]);
                }
            }
        }
        return diff;
    }

    /**
     * ASSUMES THAT THE (0,0) tile is in the bottom left tiles corner!!!
     * @param backend
     * @return
     */
    public ArrayList<TileData> getPlacementsFromFrontEndThatArentOnBackEnd(TileData[][] backend){
        ArrayList<TileData> diff = new ArrayList<TileData>();
        for(int i = 0; i < backend.length; i++){
            for(int j = 0; j < backend[0].length; j++){
                if((backend[i][j] == null && theBoard.the_game_board[i][j] != null) ||
                        (backend[i][j].letter != theBoard.the_game_board[i][j].letter)){
                    diff.add(theBoard.the_game_board[i][j]);
                }
            }
        }
        return diff;
    }
}

package com.csc480.game.Engine;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.csc480.game.Engine.Model.AI;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.Engine.Model.PlayIdea;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * THIS CLASS IS FOR TESTING PURPOSES ONLY. SHOULD NOT BE USED AT ALL IN THE FINAL BUILD!
 * This class provides a way to take input that comes directly from the tester, not the Server
 */
public class TestingInputProcessor implements InputProcessor {
    private char lastInput = ' ';
    private boolean inClick = false;
    private OrthographicCamera gameScreen;

    private AI testingAI;
    private char[] testHandQueue;
    private int aiHandCount;
    private int c;

    public TestingInputProcessor(OrthographicCamera myCam){
        gameScreen = myCam;
        testHandQueue = new char[7];
        aiHandCount = 0;
    }
    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        return false;
    }

    /**
     * Sets the current character, clears, and submits tiles
     * @param character
     * @return
     */
    @Override
    public boolean keyTyped(char character) {
        if(character == '='){
            System.out.println("clearing");
            GameManager.getInstance().placementsUnderConsideration.clear();
        }
        else if(character == '1'){
            System.out.println("logging");
            GameManager.getInstance().LogEvent("event"+c++);
        }
        else if(character == '2'){
            //System.out.println("logging");
            GameManager.getInstance().PrintBoardState();
        }
        else if(character == '3'){
            //System.out.println("logging");
            GameManager.getInstance().updatePlayers(GameManager.getInstance().theAIs);
        }
        else if(character == '\'') {
            System.out.println("entering");
            Long startTime = System.nanoTime();
            boolean isValid = GameManager.getInstance().theBoard.verifyWordPlacement(GameManager.getInstance().placementsUnderConsideration);
            System.out.println("verification took nanos: "+(System.nanoTime()-startTime));
            if(isValid)
                GameManager.getInstance().theBoard.addWord(GameManager.getInstance().placementsUnderConsideration);
            GameManager.getInstance().placementsUnderConsideration.clear();
        }
        else if(character == '\\'){
            System.out.println("Finding all words for queue");
            Long startTime = System.nanoTime();
            String hand = "";
            for(Placement p : GameManager.getInstance().placementsUnderConsideration){
                hand+= p.letter;
            }
            char[] constraints = new char[hand.length()];
            Arrays.fill(constraints,(char)0);
            ArrayList<String> results = WordVerification.getInstance().getWordsFromHand(hand,constraints, 5);
            System.out.println("finding all possible words took nanos: "+(System.nanoTime()-startTime));
            for (String s : results)
                System.out.println(s);

        }else if(character == ']'){
            System.out.println("AI TEST YOUR THING!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
            for(int i = 0; i < testHandQueue.length; i++){
                if(testHandQueue[i] != 0) {
                    System.out.println("adding to ai:" + testHandQueue[i]);
                    GameManager.getInstance().theAIs[0].tiles[i] = testHandQueue[i];
                }
            }
            GameManager.getInstance().updatePlayers(GameManager.getInstance().theAIs);
            System.out.println("Finding all AI plays for tiles");
            Long startTime = System.nanoTime();
            GameManager.getInstance().theAIs[0].TESTFindPlays(GameManager.getInstance().theBoard);
            System.out.println("finding all possible AI plays took nanos: "+(System.nanoTime()-startTime));
            PlayIdea bestPlay = GameManager.getInstance().theAIs[0].PlayBestWord();
            while(bestPlay != null && !GameManager.getInstance().theBoard.verifyWordPlacement(bestPlay.placements)){
                bestPlay = GameManager.getInstance().theAIs[0].PlayBestWord();
                if(bestPlay == null) break;
            }

            if(bestPlay != null && bestPlay.myWord != null && GameManager.getInstance().theBoard.verifyWordPlacement(bestPlay.placements)){
                System.out.println("The AI found made a decent play");
                //delete this to specify the AI tiles
                /*
                for(int i = 0; i < bestPlay.size(); i++){
                    for(int j = 0; j < testHandQueue.length; j++){
                        if(bestPlay.get(i).letter == testHandQueue[j]) {
                            testHandQueue[j] = (char) 0;
                        }
                    }
                }
                for(int i = 0; i < bestPlay.size(); i++){
                    for(int j = 0; j < testingAI.tiles.length; j++){
                        if(bestPlay.get(i).letter == testingAI.tiles[j]) {
                            testingAI.tiles[j] = (char) 0;
                        }
                    }
                }
                */
                for(int i = 0; i < testHandQueue.length; i++){
                    //if(testHandQueue[i] == 0) testHandQueue[i] = GameManager.getInstance().getNewTiles(1).get(0).charValue();
                }

                GameManager.getInstance().theBoard.addWord(bestPlay.placements);
                GameManager.getInstance().placementsUnderConsideration.clear();
            }
        }else{
            System.out.println("changing to "+character);
            System.out.println("Adding to AI tiles "+character);
            lastInput = character;
            testHandQueue[aiHandCount] = character;
            aiHandCount++;
            aiHandCount = (aiHandCount) % 7;
        }

        return true;
    }

    /**
     * Queues a letter to be added at a postition
     * @param screenX
     * @param screenY
     * @param pointer
     * @param button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector3 test = gameScreen.unproject(new Vector3(screenX, screenY, 0));

        System.out.println("test clicked @: " + (int)test.x + ", " + (int)test.y);

        if(inClick == false) {
            inClick = true;
            Placement p = new Placement(lastInput,(int)test.x, (int)test.y);
            GameManager.getInstance().placementsUnderConsideration.add(p);
            System.out.println("Queued "+ lastInput +" at" + (int)test.x + ", " + (int)test.y);

        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        inClick = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}

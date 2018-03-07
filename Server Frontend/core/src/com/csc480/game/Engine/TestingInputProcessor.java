package com.csc480.game.Engine;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.GUI.GameScreen;
import javafx.scene.Camera;

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


    public TestingInputProcessor(OrthographicCamera myCam){
        gameScreen = myCam;
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

        }
        else{
            System.out.println("changing to "+character);
            lastInput = character;
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

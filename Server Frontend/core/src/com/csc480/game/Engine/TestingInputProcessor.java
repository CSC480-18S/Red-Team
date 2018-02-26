package com.csc480.game.Engine;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;
import com.csc480.game.Engine.Model.Placement;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * THIS CLASS IS FOR TESTING PURPOSES ONLY. SHOULD NOT BE USED AT ALL IN THE FINAL BUILD!
 * This class provides a way to take input that comes directly from the tester, not the Server
 */
public class TestingInputProcessor implements InputProcessor {
    TestingPOCScreen screenToUpdate;
    private char lastInput = ' ';
    private boolean inClick = false;


    TestingInputProcessor(TestingPOCScreen toUpdate){
        screenToUpdate = toUpdate;
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
            screenToUpdate.placements.clear();
        }
        else if(character == '\'') {
            System.out.println("entering");
            Long startTime = System.nanoTime();
            boolean isValid = screenToUpdate.board.verifyWordPlacement(screenToUpdate.placements);
            System.out.println("verification took nanos: "+(System.nanoTime()-startTime));
            if(isValid)
                screenToUpdate.board.addWord(screenToUpdate.placements);
                screenToUpdate.placements.clear();
        }
        else if(character == '\\'){
            System.out.println("Finding all words for queue");
            Long startTime = System.nanoTime();
            String hand = "";
            for(Placement p : screenToUpdate.placements){
                hand+= p.letter;
            }
            char[] constraints = new char[hand.length()];
            Arrays.fill(constraints,' ');
            ArrayList<String> results = WordVerification.getInstance().getWordsFromHand(hand,constraints);
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
        Vector3 test = screenToUpdate.theCamera.unproject(new Vector3(screenX, screenY, 0));

        //System.out.println("test clicked @: " + (int)test.x + ", " + (int)test.y);

        if(inClick == false) {
            inClick = true;
            Placement p = new Placement(lastInput,(int)test.x, (int)test.y);
            screenToUpdate.placements.add(p);
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

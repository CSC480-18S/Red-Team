package com.csc480.game.Engine;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector3;

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

    @Override
    public boolean keyTyped(char character) {
        if(character == '='){
            System.out.println("clearing");
            screenToUpdate.placements.clear();
        }
        else if(character == '\'') {
            System.out.println("entering");
            if(screenToUpdate.board.verifyWordPlacement(screenToUpdate.placements))
                screenToUpdate.board.addWord(screenToUpdate.placements);
                screenToUpdate.placements.clear();
        }
        else{
            System.out.println("changing to "+character);
            lastInput = character;
        }

        return true;
    }

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

package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.SnapshotArray;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.Model.AI;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.Engine.Model.Player;
import com.csc480.game.Engine.TextureManager;
import com.csc480.game.GUI.GameScreen;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

/**
 * This class manages the tiles in a player's tiles
 */
public class HandActor extends Group {
    private boolean flip = false;
    ArrayList<TileActor> myHand;
    //Image rack;
    Label name;
    Label yourTurn;
    Image turn;
    //Player associatedPlayer;
    int associatedPlayerIndex;

    public HandActor(boolean flipTiles, int index){
        super();
        associatedPlayerIndex = index;
        myHand = new ArrayList<TileActor>();
        flip = flipTiles;
        turn = new Image(TextureManager.getInstance().greenBar);
        if(flip)
            turn.setPosition(0,GameScreen.GUI_UNIT_SIZE);
        turn.setScale(.0575f,.1f);
        addActor(turn);
        //This could easily be put into an if statement to change the loaded image based on user color
        //GameScreen.GUI_UNIT_SIZE/2)+count*GameScreen.GUI_UNIT_SIZE, GameScreen.GUI_UNIT_SIZE/2
        //rack = new Image(TextureManager.getInstance().rack);
        //build the slots
        for(int i = 0; i < 7; i++){
            Image temp = new Image(TextureManager.getInstance().getSlotTexture());
            temp.setPosition((GameScreen.GUI_UNIT_SIZE/2)+i*GameScreen.GUI_UNIT_SIZE, GameScreen.GUI_UNIT_SIZE/2);
            temp.setScale((GameScreen.GUI_UNIT_SIZE/GameScreen.TILE_PIXEL_SIZE));
            addActor(temp);
        }

        yourTurn = new Label("Its Your Turn",TextureManager.getInstance().ui,"default");
        yourTurn.setPosition(GameScreen.GUI_UNIT_SIZE*2,0);
        yourTurn.setVisible(false);
        name = new Label("default",TextureManager.getInstance().ui,"default");
        name.setName("name");
        name.setPosition(GameScreen.GUI_UNIT_SIZE/2,0);
        //rack.setScale(.2f);
        //associatedPlayer = new Player();
        //this.addActor(rack);
        this.addActor(name);
        addTile(new TileActor('A'));
        addTile(new TileActor('B'));
        addTile(new TileActor('C'));

    }

    public Player getPlayer(){
        return GameManager.getInstance().thePlayers[associatedPlayerIndex];
    }
    /**
     * This should be used instead of addActor. This will place the new tile in the proper position.
     * @param a
     */
    public void addTile(TileActor a){
            super.addActor(a);
            a.scaleBy(-.005f);
            a.setPosition(((GameScreen.GUI_UNIT_SIZE/2)+myHand.size()*GameScreen.GUI_UNIT_SIZE)+GameScreen.GUI_UNIT_SIZE*.032f, GameScreen.GUI_UNIT_SIZE/2+GameScreen.GUI_UNIT_SIZE*.1f);//GameScreen.GUI_UNIT_SIZE/2);
            myHand.add(a);
    }

    /**
     * This should be used instead of removeActor. This will reorganize the tileActors in the tiles.
     * @param a
     * @return
     */
    public boolean removeTile(TileActor a){
        boolean rem = super.removeActor(a);
        if(rem) {
            myHand.remove(a);
            //remove the tiles in the tiles
            int count = 0;
            //SnapshotArray<Actor> copy = this.getChildren();
            int initSize = this.getChildren().size;
            for (int i = 0; i < initSize; i++) {
                Actor child = this.getChildren().get(i);
                if(child instanceof TileActor){
                    MoveToAction mta = new MoveToAction();
                    mta.setPosition(((GameScreen.GUI_UNIT_SIZE/2)+count*GameScreen.GUI_UNIT_SIZE)+GameScreen.GUI_UNIT_SIZE*.032f, GameScreen.GUI_UNIT_SIZE/2+GameScreen.GUI_UNIT_SIZE*.1f);
                    mta.setDuration(1f);
                    child.addAction(mta);
                    count++;
                }
            }
        }
        return rem;
    }

    /**
     * Set up the tiles state for a new player
     * @param newPlayer
     */
    public void setPlayer(Player newPlayer){
        //associatedPlayer = newPlayer;
        name.setText(GameManager.getInstance().thePlayers[associatedPlayerIndex].name);
        if(GameManager.getInstance().thePlayers[associatedPlayerIndex].team.toLowerCase().compareTo("gold") == 0){
            turn.setDrawable(new TextureRegionDrawable(new TextureRegion(TextureManager.getInstance().goldBar)));
        } else {
            turn.setDrawable(new TextureRegionDrawable(new TextureRegion(TextureManager.getInstance().greenBar)));
        }
        updateState();
        updateState();
    }

    /**
     * Will sync the tiles display with the GameState
     */
    public void updateState(){
        if(GameManager.getInstance().thePlayers[associatedPlayerIndex] != null) {
            if (GameManager.getInstance().thePlayers[associatedPlayerIndex].turn)
                turn.setVisible(true);
            else
                turn.setVisible(false);
        }else turn.setVisible(false);
        ArrayList<Character> whatsInHand = new ArrayList<Character>();
        //System.out.println("associa player hand size of "+associatedPlayer.tiles.length);
        for(int i = 0; i < GameManager.getInstance().thePlayers[associatedPlayerIndex].tiles.length; i++) {
            if(GameManager.getInstance().thePlayers[associatedPlayerIndex].tiles[i] != 0)
                if(GameManager.getInstance().thePlayers[associatedPlayerIndex].isAI)
                    whatsInHand.add(new Character(GameManager.getInstance().thePlayers[associatedPlayerIndex].tiles[i]));
                else
                    whatsInHand.add(new Character('_'));
        }
        //remove tiles that arent here
        int initSize = this.getChildren().size;
        for(int i = 0; i < initSize;i++){
            Actor child = this.getChildren().get(i);
            if(child instanceof TileActor){
                Character thisChar = new Character(((TileActor) child).myLetter);
                if(whatsInHand.contains(thisChar)) {
                    whatsInHand.remove(thisChar);
                }
                else {
                    initSize--;
                    i--;
                    this.removeTile((TileActor) child);
                }
            }
        }
        //add the rest
        for(Character c: whatsInHand){
            this.addTile(new TileActor(c.charValue()));
        }
        name.setText(GameManager.getInstance().thePlayers[associatedPlayerIndex].name);
        //name.act(0);
    }


    /**
     * DONT USE THIS YET IT IS NOT IMPLEMENTED NOR TESTED FULLY
     * This will remove the Actors from the tiles and move them to the board where
     * they will then be destroyed as the TiledMap updates
     * @param placements
     */
    public void PlayTileActors(ArrayList<Placement> placements){
        for(Placement p : placements){
            for(TileActor a: myHand){
                if(p.letter == a.getName().charAt(0)){

                }
            }
        }
        throw new NotImplementedException();
    }

}

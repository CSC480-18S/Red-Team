package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.Engine.Model.Player;
import com.csc480.game.GUI.GameScreen;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;

/**
 * This class manages the tiles in a player's tiles
 */
public class HandActor extends Group {
    ArrayList<TileActor> myHand;
    Player associatedPlayer;

    public HandActor(){
        super();
        myHand = new ArrayList<TileActor>();
        //This could easily be put into an if statement to change the loaded image based on user color
        //SHOULD MAKE A FUNCTION THAT MANAGES THIS TEXTURE IN THE TEXTURE MANAGER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        Image rack = new Image(new Texture(Gdx.files.internal("rack.jpg")));
        rack.setScale(.2f);
        this.addActor(rack);
    }

    public Player getPlayer(){
        return associatedPlayer;
    }
    /**
     * This should be used instead of addActor. This will place the new tile in the proper position.
     * @param a
     */
    public void addTile(TileActor a){
        super.addActor(a);
        a.setPosition((GameScreen.GUI_UNIT_SIZE/2)+myHand.size()*GameScreen.GUI_UNIT_SIZE, GameScreen.GUI_UNIT_SIZE/2);
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
            for(Actor child : this.getChildren()){
                if(child instanceof TileActor){
                    MoveToAction mta = new MoveToAction();
                    mta.setPosition((GameScreen.GUI_UNIT_SIZE/2)+count*GameScreen.GUI_UNIT_SIZE, GameScreen.GUI_UNIT_SIZE/2);
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
        associatedPlayer = newPlayer;
        for(Actor child : this.getChildren()){
            if(child instanceof TileActor){
                this.removeTile((TileActor) child);
            }
        }
        for(int i = 0; i < associatedPlayer.tiles.length; i++){
            if(associatedPlayer.tiles[i] != 0){
                TileActor temp = new TileActor(associatedPlayer.tiles[i]);
                this.addTile(temp);
            }
        }
    }

    /**
     * Will sync the tiles display with the GameState
     */
    public void updateState(){
        ArrayList<Character> whatsInHand = new ArrayList<Character>();
        for(int i = 0; i < associatedPlayer.tiles.length; i++) {
            if(associatedPlayer.tiles[i] != 0)
                whatsInHand.add(new Character(associatedPlayer.tiles[i]));
        }
        //remove tiles that arent here
        for(Actor child : this.getChildren()){
            if(child instanceof TileActor){
                Character thisChar = new Character(((TileActor) child).myLetter);
                if(whatsInHand.contains(thisChar))
                    whatsInHand.remove(thisChar);
                else
                    this.removeTile((TileActor) child);
            }
        }
        //add the rest
        for(Character c: whatsInHand){
            this.addTile(new TileActor(c.charValue()));
        }
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

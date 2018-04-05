package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.SpriteDrawable;
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
    Image rack;
    Label name;
    Player associatedPlayer;

    public HandActor(boolean flipTiles){
        super();
        myHand = new ArrayList<TileActor>();
        flip = flipTiles;
        //This could easily be put into an if statement to change the loaded image based on user color
        //SHOULD MAKE A FUNCTION THAT MANAGES THIS TEXTURE IN THE TEXTURE MANAGER!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        rack = new Image(TextureManager.getInstance().rack);
        name = new Label("default",TextureManager.getInstance().ui,"default");
        name.setName("name");
        name.setPosition(GameScreen.GUI_UNIT_SIZE/2,0);
        rack.setScale(.2f);
        associatedPlayer = new Player();
        this.addActor(rack);
        this.addActor(name);
        addTile(new TileActor('A'));
        addTile(new TileActor('B'));
        addTile(new TileActor('C'));
    }

    public Player getPlayer(){
        return associatedPlayer;
    }
    /**
     * This should be used instead of addActor. This will place the new tile in the proper position.
     * @param a
     */
    public void addTile(TileActor a){
        if(flip) {
            //todo flip the texture of the tile
        }

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
        name.setText(associatedPlayer.name);
        if(associatedPlayer.team.toLowerCase().compareTo("gold") == 0){
            //todo get these assets
            //rack.setDrawable(new SpriteDrawable(new Sprite(TextureManager.getInstance().tilesAtlas.findRegion("goldRack"))));

        } else {
            //rack.setDrawable(new SpriteDrawable(new Sprite(TextureManager.getInstance().tilesAtlas.findRegion("greenRack"))));
        }
        updateState();
        updateState();
    }

    /**
     * Will sync the tiles display with the GameState
     */
    public void updateState(){
        ArrayList<Character> whatsInHand = new ArrayList<Character>();
        //System.out.println("associa player hand size of "+associatedPlayer.tiles.length);
        for(int i = 0; i < associatedPlayer.tiles.length; i++) {
            if(associatedPlayer.tiles[i] != 0)
                whatsInHand.add(new Character(associatedPlayer.tiles[i]));
        }
        //remove tiles that arent here
        for(int i = 0; i < this.getChildren().size;i++){
            Actor child = this.getChildren().get(i);
            if(child instanceof TileActor){
                Character thisChar = new Character(((TileActor) child).myLetter);
                if(whatsInHand.contains(thisChar)) {
                    whatsInHand.remove(thisChar);
                }
                else {
                    i--;
                    this.removeTile((TileActor) child);
                }
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

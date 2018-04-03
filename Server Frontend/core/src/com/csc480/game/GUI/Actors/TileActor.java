package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csc480.game.Engine.TextureManager;
import com.csc480.game.Engine.TiledUtils;
import com.csc480.game.GUI.GameScreen;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class TileActor extends Image {
    public final char myLetter;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.setColor(this.getColor());

        ((TextureRegionDrawable)getDrawable()).draw(batch, getX(),getY(),
                getOriginX(),getOriginY(),
                getWidth(),getHeight(),
                getScaleX(),getScaleY(),
                getRotation());
    }

    /**
     * IT IS VERY IMPORTANT THAT THE ACTORS NAME MATCHES THE TILE
     * @param letter the name, and tile of the actor
     */
    public TileActor(char letter){
        super(TextureManager.getInstance().getTileTexture(letter+""));
        this.setName(letter+"");
        this.setScale(GameScreen.GUI_UNIT_SIZE/GameScreen.TILE_PIXEL_SIZE);

        myLetter = letter;
        setBounds(getX(),getY(),getWidth(),getHeight());
    }
}

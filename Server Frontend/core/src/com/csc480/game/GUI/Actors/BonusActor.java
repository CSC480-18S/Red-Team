package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csc480.game.Engine.TextureManager;

public class BonusActor extends Group{
    Image charriot;
    Label message;
    public BonusActor(){
        super();

        charriot = new Image(TextureManager.getInstance().getTileTexture("oswego"));
        charriot.setPosition(0,0);
        message = new Label("Bonus Word", TextureManager.getInstance().ui, "default");

        addActor(charriot);
        addActor(message);
    }
}

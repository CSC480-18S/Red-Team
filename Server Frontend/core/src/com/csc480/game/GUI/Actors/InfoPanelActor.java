package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.csc480.game.Engine.TextureManager;

public class InfoPanelActor extends Group{

    public InfoPanelActor(){
        super();
        Label title = new Label("Score", TextureManager.getInstance().ui, "default");
        addActor(title);


    }







    public void LogEvent(String event){

    }
}

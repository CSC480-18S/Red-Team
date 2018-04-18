package com.csc480.stats.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FontManager {
    private static FontManager instance;

    public Skin cumulative;
    public Skin title;


    public static FontManager getInstance(){
        if(instance == null)
            instance = new FontManager();
        return instance;
    }



    private FontManager(){

        //-----Title Font size
        cumulative = new Skin(Gdx.files.internal("oldSkin/exempleSkin.json"));
        cumulative.getFont("font").setUseIntegerPositions(false);
        cumulative.getFont("font").getData().setScale(.4f,.4f);

        title = new Skin(Gdx.files.internal("oldSkin/exempleSkin.json"));
        title.getFont("font").setUseIntegerPositions(false);
        title.getFont("font").getData().setScale(.6f,.6f);

    }


    public void Dispose(){
        title.dispose();
        instance = null;

    }
}

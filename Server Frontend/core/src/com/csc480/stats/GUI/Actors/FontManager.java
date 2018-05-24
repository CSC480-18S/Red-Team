package com.csc480.stats.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

public class FontManager {
    private static FontManager instance;

    public Skin cumulative;
    public Skin title;
    public Skin normal;
    public Skin tvalue;


    public static FontManager getInstance(){
        if(instance == null)
            instance = new FontManager();
        return instance;
    }



    private FontManager(){

        //-----Title Font size
        title = new Skin(Gdx.files.internal("skin/OsweebleSkinFinal.json"));
        title.getFont("BloggerSans").setUseIntegerPositions(false);
        title.getFont("BloggerSans").getData().setScale(.4f,.4f);


        //-----Cumulative Font size
        cumulative = new Skin(Gdx.files.internal("skin/OsweebleSkinFinal.json"));
        cumulative.getFont("BloggerSansBold").setUseIntegerPositions(false);
        cumulative.getFont("BloggerSansBold").getData().setScale(.5f,.5f);

        //-----Head Font size
        normal = new Skin(Gdx.files.internal("skin/OsweebleSkinFinal.json"));
        normal.getFont("BloggerSansBold").setUseIntegerPositions(false);
        normal.getFont("BloggerSansBold").getData().setScale(.3f,.3f);

        //-----Head Font size
        tvalue = new Skin(Gdx.files.internal("skin/OsweebleSkinFinal.json"));
        tvalue.getFont("BloggerSansBold").setUseIntegerPositions(false);
        tvalue.getFont("BloggerSansBold").getData().setScale(.4f,.4f);





    }


    public void Dispose(){
        title.dispose();
        instance = null;

    }
}

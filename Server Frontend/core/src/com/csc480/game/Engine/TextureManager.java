package com.csc480.game.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * THE GUI CLASSES SHOULD BE REFACTORED TO USE THIS CLASS INSTEAD OF LOAD RESOURCES DIRECTLY
 * This class manages the textures so that resources can be disposed of properly
 */
public class TextureManager {
        private static TextureManager instance;
        //public final TextureAtlas textureAtlas;
        public Skin ui;
        public final Texture rack;
        public final Texture background;
        public final Texture tile;

        public static TextureManager getInstance(){
            if(instance == null)
                instance = new TextureManager();
            return instance;
        }

        private TextureManager(){
            //TextureAtlas uiAtlas = new TextureAtlas();
            ui = new Skin(Gdx.files.internal("skin/exempleSkin.json"));
//            ui.getFont("font").getData().
            ui.getFont("font").setUseIntegerPositions(false);
            ui.getFont("font").getData().setScale(.3f,.3f);
            rack = new Texture(Gdx.files.internal("rack.jpg"));
            tile = new Texture(Gdx.files.internal("temp.png"));
            background = new Texture(Gdx.files.internal("Background.jpg"));
            //ui.addRegions(uiAtlas);
            //textureAtlas = new TextureAtlas(Gdx.files.internal("spriteAtlas.atlas"));
        }
        public void Dispose(){
            ui.dispose();
            rack.dispose();
            instance = null;
           // textureAtlas.dispose();
        }
}



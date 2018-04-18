package com.csc480.game.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * THE GUI CLASSES SHOULD BE REFACTORED TO USE THIS CLASS INSTEAD OF LOAD RESOURCES DIRECTLY
 * This class manages the textures so that resources can be disposed of properly
 */
public class TextureManager {
        private static TextureManager instance;
        //public final TextureAtlas textureAtlas;
        public Skin ui;
        //public final Texture rack;
        public final Texture background;
        public final Texture infoBackground;
        public final Texture greenBar;
        public final Texture goldBar;
//        public final Texture tile;
        public final TextureAtlas tilesAtlas;

        public static final String DARK_TILE = "~Dark";
        public static final String GREEN_TILE = "~Green";
        public static final String GOLD_TILE = "~Gold";
        public static final String BLANK_TILE = "_Blank";
        public static final String INVIS_TILE = "_Invis";
        public static final String EMPTY_TILE = "~Empty";

        public static TextureManager getInstance(){
            if(instance == null)
                instance = new TextureManager();
            return instance;
        }

        private TextureManager(){
            //TextureAtlas uiAtlas = new TextureAtlas();
//            ui = new Skin(Gdx.files.internal("skin/exempleSkin.json"));
            ui = new Skin(Gdx.files.internal("skin/OsweebleSkinFinal.json"));
//            ui.getFont("font").getData().
            ui.getFont("BloggerSans").setUseIntegerPositions(false);
            ui.getFont("BloggerSans").getData().setScale(.2f,.2f);
            ui.getFont("BloggerSansBold").setUseIntegerPositions(false);
            ui.getFont("BloggerSansBold").getData().setScale(.2f,.2f);
            //rack = new Texture(Gdx.files.internal("rack.jpg"));

//            tile = new Texture(Gdx.files.internal("temp.png"));
            tilesAtlas = new TextureAtlas(Gdx.files.internal("tilesAtlas.atlas"));
            background = new Texture(Gdx.files.internal("Background.jpg"));
            infoBackground = new Texture(Gdx.files.internal("board.png"));
            greenBar = new Texture(Gdx.files.internal("greenBar.png"));
            goldBar = new Texture(Gdx.files.internal("goldBar.png"));
            //ui.addRegions(uiAtlas);
            //textureAtlas = new TextureAtlas(Gdx.files.internal("spriteAtlas.atlas"));
        }

        public TextureAtlas.AtlasRegion getTileTexture(String tile){
            if(tile.compareTo("_") == 0) return tilesAtlas.findRegion(BLANK_TILE);
            if(tile.length() == 1) tile = tile.toUpperCase();
            return tilesAtlas.findRegion(tile);
        }
        public TextureAtlas.AtlasRegion getSlotTexture(){
            return tilesAtlas.findRegion("slot");
        }
        public void Dispose(){
            ui.dispose();
            //rack.dispose();
            instance = null;
            tilesAtlas.dispose();
           // textureAtlas.dispose();
        }
}



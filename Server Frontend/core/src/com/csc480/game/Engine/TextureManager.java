package com.csc480.game.Engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class TextureManager {
        private static TextureManager instance;
        public final TextureAtlas textureAtlas;

        public static TextureManager getInstance(){
            if(instance == null)
                instance = new TextureManager();
            return instance;
        }

        private TextureManager(){
            textureAtlas = new TextureAtlas(Gdx.files.internal("spriteAtlas.atlas"));
        }
        public void Dispose(){
            instance = null;
            textureAtlas.dispose();
        }
}



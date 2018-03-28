package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csc480.game.Engine.TextureManager;
import com.csc480.game.GUI.GameScreen;

public class GameBoardTable extends Group {
    public GameBoardTable(){
        super();
        Table board = new Table();
        board.setPosition(0,0);
        board.setHeight(Gdx.graphics.getHeight()/5);
        board.setWidth(Gdx.graphics.getWidth()/5* GameScreen.aspectRatio);
        for(int i= 0; i < 11; i++){
            for(int j = 0; j < 11; j++){

                Image tile = new Image(TextureManager.getInstance().tile);
                board.add(tile);
            }
            board.row();
        }
        System.out.println(board.getChildren().size);
        addActor(board);

    }
}

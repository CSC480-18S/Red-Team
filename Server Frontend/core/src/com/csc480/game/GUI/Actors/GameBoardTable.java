package com.csc480.game.GUI.Actors;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.Model.Board;
import com.csc480.game.Engine.Model.TileData;
import com.csc480.game.Engine.TextureManager;
import com.csc480.game.GUI.GameScreen;

public class GameBoardTable extends Group {
    Table board;
    public GameBoardTable(){
        super();
        board = new Table();
        board.setPosition(0,0);
        board.setHeight(Gdx.graphics.getHeight()/5);
        board.setWidth(Gdx.graphics.getWidth()/5* GameScreen.aspectRatio);
        for(int i= 0; i < 11; i++){
            for(int j = 0; j < 11; j++){
                Image tile = new Image(TextureManager.getInstance().getTileTexture(TextureManager.EMPTY_TILE));
                tile.setName("`");
                board.add(tile);
            }
            board.row();
        }

        setTile(5,5,TextureManager.DARK_TILE);
        setTile(4,5,TextureManager.DARK_TILE);
        setTile(6,5,TextureManager.DARK_TILE);
        setTile(5,4,TextureManager.DARK_TILE);
        setTile(5,6,TextureManager.DARK_TILE);
        setTile(0,0, TextureManager.GREEN_TILE);
        setTile(10,10, TextureManager.GREEN_TILE);
        setTile(0,10, TextureManager.GOLD_TILE);
        setTile(10,0, TextureManager.GOLD_TILE);
        System.out.println(board.getChildren().size);
        addActor(board);

    }
    public void setTile(int x, int y, String letter){
        Cell center = board.getCells().get( x+(11*(10-y)) );
        Image i = new Image(TextureManager.getInstance().getTileTexture(letter));
        i.setName(letter.toLowerCase());
        center.setActor(i);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        TileData[][] toDraw = GameManager.getInstance().theBoard.the_game_board;
        if(toDraw != null){
            for(int i = 0; i < toDraw.length; i++){
                for(int j = 0; j < toDraw.length; j++){
                    if(toDraw[i][j] != null){

                        if(toDraw[i][j].letter != board.getCells().get( i+(11*(10-j))).getActor().getName().charAt(0)){
                            setTile(i,j, toDraw[i][j].letter+"");
                        }
                    }
                }
            }
        }
        //batch.end();
        super.draw(batch,parentAlpha);
        //batch.begin();
    }
}

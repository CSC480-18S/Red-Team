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
        board.setHeight(GameScreen.GUI_UNIT_SIZE*8.75f);
        board.setWidth(GameScreen.GUI_UNIT_SIZE*8.75f);
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
        setTile(7,0, TextureManager.GREEN_TILE);
        setTile(4,2, TextureManager.GREEN_TILE);
        setTile(7,3, TextureManager.GREEN_TILE);
        setTile(10,3, TextureManager.GREEN_TILE);
        setTile(2,4, TextureManager.GREEN_TILE);
        setTile(0,7, TextureManager.GREEN_TILE);
        setTile(3,7, TextureManager.GREEN_TILE);
        setTile(6,8, TextureManager.GREEN_TILE);
        setTile(8,6, TextureManager.GREEN_TILE);
        setTile(3,10, TextureManager.GREEN_TILE);
        setTile(10,10, TextureManager.GREEN_TILE);

        setTile(0,10, TextureManager.GOLD_TILE);
        setTile(0,3, TextureManager.GOLD_TILE);
        setTile(3,0, TextureManager.GOLD_TILE);
        setTile(3,3, TextureManager.GOLD_TILE);
        setTile(6,2, TextureManager.GOLD_TILE);
        setTile(2,6, TextureManager.GOLD_TILE);
        setTile(8,4, TextureManager.GOLD_TILE);
        setTile(4,8, TextureManager.GOLD_TILE);
        setTile(7,7, TextureManager.GOLD_TILE);
        setTile(10,7, TextureManager.GOLD_TILE);
        setTile(7,10, TextureManager.GOLD_TILE);

        setTile(10,0, TextureManager.GOLD_TILE);
        System.out.println(board.getChildren().size);
        addActor(board);

    }
    public void setTile(int x, int y, String letter){
        System.out.println("setting tile:"+letter);
        Cell center = board.getCells().get( x+(11*(10-y)) );
        Image i = new Image(TextureManager.getInstance().getTileTexture(letter));
        i.setName(letter.toLowerCase());
        center.setActor(i);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        synchronized (GameManager.getInstance().theBoard.the_game_board){
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
        }
        //batch.begin();
    }
}

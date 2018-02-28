package com.csc480.game.Engine;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.Engine.Model.Board;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.GUI.GameScreen;
import com.csc480.game.OswebbleGame;

public class TiledUtils {
    public static TiledMapTileLayer allTiles;

    public static TiledMapTileLayer generatePlacementsTiledLayer(Board theBoard){
        //The easiest way to do if we want to set certain tiles of one map to another tile
        //Set up a base tileset so that we can enumerate from letters to tiles with letters on them
        if(allTiles == null)
            allTiles = (TiledMapTileLayer) (new TmxMapLoader().load("identifiersHelper.tmx")).getLayers().get(0);

        TiledMapTileLayer plays = new TiledMapTileLayer(
                OswebbleGame.BOARD_SIZE,OswebbleGame.BOARD_SIZE,
                (int) GameScreen.TILE_PIXEL_SIZE, (int)GameScreen.TILE_PIXEL_SIZE);
        for (int i = 0; i < GameManager.getInstance().theBoard.the_game_board.length; i++){
            for(int j = 0; j < GameManager.getInstance().theBoard.the_game_board[0].length; j++){
                if(GameManager.getInstance().theBoard.the_game_board[i][j] != null){
                    TiledMapTileLayer.Cell playedCell = new TiledMapTileLayer.Cell();
                    //playedCell.setTile(allTiles.getCell(0,0));
                    plays.setCell(i,j, allTiles.getCell(1,0));
                    //System.out.println("added placement");
                }
            }
        }
        for(Placement p: GameManager.getInstance().placementsUnderConsideration){
            plays.setCell(p.xPos,p.yPos, allTiles.getCell(0,0));
        }
        return plays;
    }

    public enum LettersToTileIDs{
        A('a',0),
        B('b',1),
        C('c',2),
        D('d',3),
        E('e',4),
        F('f',5),
        G('g',6),
        H('h',7),
        I('i',8),
        J('j',9),
        K('k',10),
        L('l',11),
        M('m',12),
        N('n',13),
        O('o',14),
        P('p',15),
        Q('q',16),
        R('r',17),
        S('s',18),
        T('t',19),
        U('u',20),
        V('v',21),
        W('w',22),
        X('x',23),
        Y('y',24),
        Z('z',25);


        public final char letter;
        public final int index;
        LettersToTileIDs(char letter, int index){
            this.letter = letter;
            this.index = index;
        }

    }
}

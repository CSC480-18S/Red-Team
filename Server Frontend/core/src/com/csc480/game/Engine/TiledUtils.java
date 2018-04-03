package com.csc480.game.Engine;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.csc480.game.Engine.Model.Board;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.GUI.GameScreen;
import com.csc480.game.OswebbleGame;

public class TiledUtils {
    public static TiledMapTileLayer allTiles;

    /**
     * @return a TiledMap with all the tiles in accending order, very useful for changing from one tile to another,
     *      or matching letter index to tile
     */
    public static TiledMapTileLayer getAllTiles(){
        if (allTiles == null)
            allTiles = (TiledMapTileLayer) (new TmxMapLoader().load("identifiersHelper.tmx")).getLayers().get(0);
        return allTiles;

    }

    /**
     * This function creates a TiledLayer based on the current state of the game board
     * @param theBoard the current board state
     * @return
     */
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
                    plays.setCell(i,j, allTiles.getCell(theBoard.the_game_board[i][j].letter-97,0));
                }
            }
        }

        return plays;
    }
    public static TiledMapTileLayer generateThinkingTiledLayer(){
        //The easiest way to do if we want to set certain tiles of one map to another tile
        //Set up a base tileset so that we can enumerate from letters to tiles with letters on them
        if(allTiles == null)
            allTiles = (TiledMapTileLayer) (new TmxMapLoader().load("identifiersHelper.tmx")).getLayers().get(0);

        TiledMapTileLayer plays = new TiledMapTileLayer(
                OswebbleGame.BOARD_SIZE,OswebbleGame.BOARD_SIZE,
                (int) GameScreen.TILE_PIXEL_SIZE, (int)GameScreen.TILE_PIXEL_SIZE);
        for(Placement p: GameManager.getInstance().placementsUnderConsideration){
            plays.setCell(p.xPos,p.yPos, allTiles.getCell(p.letter-97,0));
        }

        return plays;
    }

    public static int LetterToID(char l){
        return l-97;
    }

    public  static char IDToLetter(char l){return (char) (l+97);}
}

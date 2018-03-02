package com.csc480.game.Engine;


import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.csc480.game.Engine.Model.Board;
import com.csc480.game.Engine.Model.Placement;
import com.csc480.game.GUI.GameScreen;
import com.csc480.game.OswebbleGame;

/**
 * THIS WHOLE CLASS NEEDS TO BE REFACTORED ONCE THE ASSETS ARE DONE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 */
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
     * THIS NEEDS TO BE UPDATED ONCE ALL THE ASSETS ARE MADE!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
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
                    //This should probably be refactored when all the assets are done such that the index = charVal-97 to get a=0 - z=26////////////////////
                    switch (theBoard.the_game_board[i][j].letter){
                        case 'a':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'b':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'c':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'd':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'e':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'f':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'g':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'h':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'i':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'j':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'k':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'l':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'm':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'n':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'o':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'p':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'q':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'r':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 's':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 't':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'u':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'v':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'w':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'x':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'y':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case 'z':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        case '_':
                            plays.setCell(i,j, allTiles.getCell(LettersToTileIDs.A.index,0));
                        default:
                            plays.setCell(i,j, allTiles.getCell(0,0));
                    }
                }
            }
        }
        for(Placement p: GameManager.getInstance().placementsUnderConsideration){
            plays.setCell(p.xPos,p.yPos, allTiles.getCell(1,0));
        }
        return plays;
    }

    public static int LetterToID(char l){
        int index;
        switch (l){
            case 'a':
                index = LettersToTileIDs.A.index;
                break;
            case 'b':
                index = LettersToTileIDs.A.index;
                break;
            case 'c':
                index = LettersToTileIDs.A.index;
                break;
            case 'd':
                index = LettersToTileIDs.A.index;
                break;
            case 'e':
                index = LettersToTileIDs.A.index;
                break;
            case 'f':
                index = LettersToTileIDs.A.index;
                break;
            case 'g':
                index = LettersToTileIDs.A.index;
                break;
            case 'h':
                index = LettersToTileIDs.A.index;
                break;
            case 'i':
                index = LettersToTileIDs.A.index;
                break;
            case 'j':
                index = LettersToTileIDs.A.index;
                break;
            case 'k':
                index = LettersToTileIDs.A.index;
                break;
            case 'l':
                index = LettersToTileIDs.A.index;
                break;
            case 'm':
                index = LettersToTileIDs.A.index;
                break;
            case 'n':
                index = LettersToTileIDs.A.index;
                break;
            case 'o':
                index = LettersToTileIDs.A.index;
                break;
            case 'p':
                index = LettersToTileIDs.A.index;
                break;
            case 'q':
                index = LettersToTileIDs.A.index;
                break;
            case 'r':
                index = LettersToTileIDs.A.index;
                break;
            case 's':
                index = LettersToTileIDs.A.index;
                break;
            case 't':
                index = LettersToTileIDs.A.index;
                break;
            case 'u':
                index = LettersToTileIDs.A.index;
                break;
            case 'v':
                index = LettersToTileIDs.A.index;
                break;
            case 'w':
                index = LettersToTileIDs.A.index;
                break;
            case 'x':
                index = LettersToTileIDs.A.index;
                break;
            case 'y':
                index = LettersToTileIDs.A.index;
                break;
            case 'z':
                index = LettersToTileIDs.A.index;
                break;
            case '_':
                index = LettersToTileIDs.A.index;
                break;

            default:
                index = -1;
        }
        return index;
    }

    private enum LettersToTileIDs{
        A('a',0),        B('b',1),        C('c',2),        D('d',3),
        E('e',4),        F('f',5),        G('g',6),        H('h',7),
        I('i',8),        J('j',9),        K('k',10),       L('l',11),
        M('m',12),       N('n',13),       O('o',14),       P('p',15),
        Q('q',16),       R('r',17),       S('s',18),       T('t',19),
        U('u',20),       V('v',21),       W('w',22),       X('x',23),
        Y('y',24),       Z('z',25),       BLANK('_',26), Null((char)0,-1);


        public final char letter;
        public final int index;
        LettersToTileIDs(char letter, int index){
            this.letter = letter;
            this.index = index;
        }

    }
}

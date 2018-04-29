package com.csc480.game.Engine;

import com.csc480.game.Engine.Model.Board;
import com.csc480.game.Engine.Model.Placement;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class BoardTest {

    @Test
    public void addWord() {
    }

    @Test
    public void verifyWordPlacement() {
        assertEquals(false,SetUpCenterHorizonalScaffolding().verifyWordPlacement(diagonalCase()));
        assertEquals(false,SetUpCenterHorizonalScaffolding().verifyWordPlacement(overlapManyCase()));
        assertEquals(false,SetUpCenterHorizonalScaffolding().verifyWordPlacement(overlapOneCase()));
        assertEquals(false,SetUpCenterHorizonalScaffolding().verifyWordPlacement(unAttatchedManyCase()));
        assertEquals(false,SetUpCenterHorizonalScaffolding().verifyWordPlacement(unAttatchedOneCase()));
        assertEquals(false,SetUpCenterVerticalScaffolding().verifyWordPlacement(adjacentLastNotValidOnHorizontal()));

        assertEquals(true, SetUpHorizonalBreakScaffolding().verifyWordPlacement(appendingHorizontalCase()));
    }

    @Test
    public void verifyWordPlacement2(){
        Board b = SetUpScaffoldingTEST();
        System.out.println(b.PrintBoard());
        assertEquals(true, b.verifyWordPlacement(horizontalPlacementJames()));
        b.addWord(horizontalPlacementJames());
        assertEquals(false, b.verifyWordPlacement(verticalPlacementJames()));
        System.out.println(b.PrintBoard());
    }

    private ArrayList<Placement> verticalPlacementJames(){//fail
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('v',3,6));
        testing.add(new Placement('a',4,6));
        //testing.add(new Placement('t',6,5));
        testing.add(new Placement('f',6,6));
        testing.add(new Placement('u',7,6));
        testing.add(new Placement('l',8,6));
        return testing;
    }
    private ArrayList<Placement> horizontalPlacementJames(){//pass
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('p',2,5));
        testing.add(new Placement('r',3,5));
        testing.add(new Placement('o',4,5));
        //testing.add(new Placement('s',5,5));
        testing.add(new Placement('t',6,5));
        return  testing;
    }

    private Board SetUpHorizonalBreakScaffolding(){
        Board b = new Board(11);
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('x',0,5));

        testing.add(new Placement('t',3,5));
        testing.add(new Placement('e',4,5));
        testing.add(new Placement('s',5,5));
        testing.add(new Placement('t',6,5));
        b.addWord(testing);
        return b;
    }

    private Board SetUpCenterHorizonalScaffolding(){
        Board b = new Board(11);
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('t',3,5));
        testing.add(new Placement('e',4,5));
        testing.add(new Placement('s',5,5));
        testing.add(new Placement('t',6,5));
        b.addWord(testing);
        return b;
    }
    private Board SetUpCenterVerticalScaffolding(){
        Board b = new Board(11);
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('t',5,7));
        testing.add(new Placement('e',5,6));
        testing.add(new Placement('s',5,5));
        testing.add(new Placement('t',5,4));
        b.addWord(testing);
        return b;
    }
    private Board SetUpScaffoldingTEST(){
        Board b = new Board(11);
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('p',5,4));
        testing.add(new Placement('s', 5, 5));
        testing.add(new Placement('t',5,6));
        b.addWord(testing);
        testing.clear();
        /*testing.add(new Placement('p',4,5));
        testing.add(new Placement('s',5,5));
        testing.add(new Placement('t',6,5));
        b.addWord(testing);*/
        return b;
    }

    private ArrayList<Placement> appendingHorizontalCase(){//fail
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('s',7,5));
        return testing;
    }

    private ArrayList<Placement> diagonalCase(){//fail
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('t',0,0));
        testing.add(new Placement('e',1,1));
        testing.add(new Placement('s',2,2));
        testing.add(new Placement('t',3,3));
        return testing;
    }
    private ArrayList<Placement> overlapManyCase(){//fail
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('t',3,5));
        testing.add(new Placement('e',4,5));
        testing.add(new Placement('s',5,5));
        testing.add(new Placement('t',6,5));
        return testing;
    }
    private ArrayList<Placement> overlapOneCase(){//fail
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('t',6,5));
        return testing;
    }

    private ArrayList<Placement> unAttatchedManyCase(){//fail
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('t',3,7));
        testing.add(new Placement('e',4,7));
        testing.add(new Placement('s',5,7));
        testing.add(new Placement('t',6,7));
        return testing;
    }
    private ArrayList<Placement> unAttatchedOneCase(){//fail
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('a',6,7));
        return testing;
    }
    private ArrayList<Placement> adjacentLastNotValidOnHorizontal(){//fail
        ArrayList<Placement> testing = new ArrayList<Placement>();
        testing.add(new Placement('o',6,5));
        testing.add(new Placement('p',6,4));
        return testing;
    }

}
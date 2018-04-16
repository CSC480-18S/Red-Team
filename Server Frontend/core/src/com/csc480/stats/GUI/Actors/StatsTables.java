package com.csc480.stats.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.csc480.game.Engine.TestingInputProcessor;

public class StatsTables extends Group {

    private Hud hud;
    private Skin skin;


    public StatsTables() {
        super();
        skin = new Skin(Gdx.files.internal("skin/exempleSkin.json"));
        hud = new Hud();




//////////////////////////////////////////////////////////////////////////////////////////////

        //Highest Word Score Table----------------
        Table innerLeft = new Table();
        Table innerL2 = new Table();

        innerL2.add(hud.GHwScore).left();
        innerL2.row();
        innerL2.add().padBottom(1);
        innerL2.row();
        innerL2.add(hud.GHWOne).left();
        innerL2.row();
        innerL2.add(hud.GHWTwo).left();
        innerL2.row();
        innerL2.add(hud.GHWThree).left();
        innerL2.row();
        innerL2.add(hud.GHWFour).left();
        innerL2.row();
        innerL2.add(hud.GHWFive).left();

        //Highest Game Scores----------------------
        Table innerL3 = new Table();
        innerL3.add(hud.GHgScore).left();
        innerL3.row();
        innerL3.add().padBottom(1);
        innerL3.row();
        innerL3.add(hud.GHgOne).left();
        innerL3.row();
        innerL3.add(hud.GHgTwo).left();
        innerL3.row();
        innerL3.add(hud.GHgThree).left();
        innerL3.row();
        innerL3.add(hud.GHgFour).left();
        innerL3.row();
        innerL3.add(hud.GHgFive).left();

        innerLeft.add(innerL2).padLeft(5);
        innerLeft.add(innerL3).padLeft(45);

        //Frequently Played Words-----------------------
        Table innerLeft2 = new Table();

        Table innerLL2 = new Table();
        innerLL2.add(hud.GFPWords);


        //Frequent Oswego-Themed Words-------------------
        Table innerLL3 = new Table();
        innerLL3.add(hud.GFOWords);



        innerLeft2.add(innerLL2).padLeft(5);
        innerLeft2.add(innerLL3.padLeft(45));


        //----------LEFT TABLE------------------
        Table first_table = new Table();

        first_table.row();//.width(265).padRight(30);


////////////////////////////////////////////////////////////////////////////////////

//////////////////////////////////////////////////////////////////////////////////////
        //Highest Word Score Table----------------

        Table innerRight = new Table();

        Table innerR2 = new Table();

        innerR2.add(hud.YHwScore).left();
        innerR2.row();
        innerR2.add().padBottom(1);
        innerR2.row();
        innerR2.add(hud.YHWOne).left();
        innerR2.row();
        innerR2.add(hud.YHWTwo).left();
        innerR2.row();
        innerR2.add(hud.YHWThree).left();
        innerR2.row();
        innerR2.add(hud.YHWFour).left();
        innerR2.row();
        innerR2.add(hud.YHWFive).left();

        //Highest Game Scores----------------------
        Table innerR3 = new Table();
        innerR3.add(hud.YHgScore).left();
        innerR3.row();
        innerR3.add().padBottom(1);
        innerR3.row();
        innerR3.add(hud.YHgOne).left();
        innerR3.row();
        innerR3.add(hud.YHgTwo).left();
        innerR3.row();
        innerR3.add(hud.YHgThree).left();
        innerR3.row();
        innerR3.add(hud.YHgFour).left();
        innerR3.row();
        innerR3.add(hud.YHgFive).left();

        innerRight.add(innerR2).padLeft(5);
        innerRight.add(innerR3).padLeft(45);


        //Frequently Played Words-----------------------
        Table innerRight2 = new Table();
        Table innerRR2 = new Table();
        innerRR2.add(hud.YFPWords);




        innerRight2.add(innerRR2).padLeft(5);


        first_table.setDebug(true);

        //-----------RIGHT TABLE---------------
        Table second_table = new Table();
        second_table.row().width(265).padRight(30);
        second_table.add(hud.ham).padLeft(1);
        second_table.row().width(265).padRight(30);
        //second_table.add(hud.RowOneScore).padLeft(1);



////////////////////////////////////////////////////////////////////////////////////////////


        //second_table.setDebug(true);


        /*
         *===================================================================
         *-----------MAIN TABLE-----------------
         *===================================================================
         */
        Table table = new Table();
        table.top();
        table.setFillParent(true);
        table.defaults().pad(2F);
        table.setFillParent(true);

        table.setDebug(true);

        //The Green/Gold Team labels----------------
        table.add(hud.GreenTeamLabel).padTop(50).padLeft(20);
        table.add(hud.GoldTeamLabel).padTop(50).padRight(10);
        table.row();

        //the cumulative scores under the main labels
        table.add(hud.GCScore).padTop(5).padLeft(20);
        table.add(hud.YCScore).padTop(5).padRight(10);
        table.row().expandY().height(48);

        //THE TABLES FOR THE HIGEST WORD SCORE AND HIGHEST GAME SCORES
        table.add(innerLeft.top()).top().left().padLeft(30);
        table.add(innerRight.top()).top().left();
        table.row();

        //THE TABLES FOR FREQUENTLY PLAYED AND OSWEGO-THEMED WORDS
        table.add(innerLeft2.top()).top().left().padLeft(30);
        table.add(innerRight2.top()).top().left();




        //The left and right Tables withing Tables
        table.row().top();//.width(300.0f);
        table.add(first_table.left().padLeft(40)).expand(Gdx.graphics.getWidth()/2,4);
        //first_table.
        table.add(second_table).expand(Gdx.graphics.getWidth()/6, 4);

        // first_table.add(secondLeftTable);

        super.addActor(table);

    }


}


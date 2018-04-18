package com.csc480.stats.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.csc480.game.Engine.TestingInputProcessor;
import com.csc480.game.GUI.GameScreen;

public class StatsTables extends Group {

    private Hud hud;
    private Skin skin;


    public StatsTables() {
        super();
        skin = new Skin(Gdx.files.internal("oldSkin/exempleSkin.json"));
        hud = new Hud();




///////////////////////////////////////////GREEN TEAM////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        //Highest Word Score Table----------------
        Table innerLeft = new Table();
        Table innerL2 = new Table();

        innerL2.add(hud.G_HwScore).left();
        innerL2.row();
        innerL2.add().padBottom(1);
        innerL2.row();
        innerL2.add(hud.G_HWOne).left();
        innerL2.row();
        innerL2.add(hud.G_HWTwo).left();
        innerL2.row();
        innerL2.add(hud.G_HWThree).left();
        innerL2.row();
        innerL2.add(hud.G_HWFour).left();
        innerL2.row();
        innerL2.add(hud.G_HWFive).left();

        //Highest Game Scores----------------------
        Table innerL3 = new Table();
        innerL3.add(hud.G_HgScore).left();
        innerL3.row();
        innerL3.add().padBottom(1);
        innerL3.row();
        innerL3.add(hud.G_HgOne).left();
        innerL3.row();
        innerL3.add(hud.G_HgTwo).left();
        innerL3.row();
        innerL3.add(hud.G_HgThree).left();
        innerL3.row();
        innerL3.add(hud.G_HgFour).left();
        innerL3.row();
        innerL3.add(hud.G_HgFive).left();

        innerLeft.add(innerL2).padLeft(10).padTop(5);
        innerLeft.add(innerL3).padLeft(50).padTop(5);

        //Frequently Played Words-----------------------
        Table innerLeft2 = new Table();

        Table innerLL2 = new Table();
        innerLL2.add(hud.G_FPWords).left();
        innerLL2.row();
        innerL2.add().padBottom(1).left();
        innerLL2.row();
        innerLL2.add(hud.G_FPOne).left();
        innerLL2.row();
        innerLL2.add(hud.G_FPTwo).left();
        innerLL2.row();
        innerLL2.add(hud.G_FPThree).left();


        //Frequent Oswego-Themed Words-------------------
        Table innerLL3 = new Table();
        innerLL3.add(hud.G_FOWords);
        innerLL3.row();
        innerLL3.add().padBottom(1).left();
        innerLL3.row();
        innerLL3.add(hud.G_FOOne).left();
        innerLL3.row();
        innerLL3.add(hud.G_FOTwo).left();
        innerLL3.row();
        innerLL3.add(hud.G_FOThree).left();

        innerLeft2.add(innerLL2).padLeft(10).padTop(5);
        innerLeft2.add(innerLL3).padLeft(35).padTop(5);


        //Longest Word-------------------
        Table innerLeft3 = new Table();

        Table innerLLL2 = new Table();
        innerLLL2.add(hud.G_LWWords).left().padTop(2);
        innerLLL2.row();
        innerLLL2.add().padBottom(1).left();
        innerLLL2.row();
        innerLLL2.add(hud.G_LWOne);

        //Bonuses Used-------------------
        Table innerLLL3 = new Table();
        innerLLL3.add(hud.G_BUWords).top().padLeft(10);

        innerLeft3.add(innerLLL2).padLeft(10);
        innerLeft3.add(innerLLL3).padLeft(45);

        //Top Players----------------------
        Table innerLeft4 = new Table();

        Table innerLx4_2 = new Table();
        innerLx4_2.add(hud.G_TPWords).left();
        innerLx4_2.row();
        innerLx4_2.add().padBottom(1).left();
        innerLx4_2.row();
        innerLx4_2.add(hud.G_TPOne).left();
        innerLx4_2.row();
        innerLx4_2.add(hud.G_TPTwo).left();
        innerLx4_2.row();
        innerLx4_2.add(hud.G_TPThree).left();


        //Dirty Words Attempted & Average Score------------
        Table innerLx4_3 = new Table();
        innerLx4_3.add(hud.G_WAWords).top().padTop(20).padLeft(65).padBottom(20);
        innerLx4_3.row();
        innerLx4_3.add(hud.G_ASWords).padLeft(55);



        innerLeft4.add(innerLx4_2).padLeft(10);
        innerLeft4.add(innerLx4_3).padLeft(5);

        //T-test answer----------------------------------
        Table innerLeft5 = new Table();
        innerLeft5.add(hud.T_text).left();

        //----------LEFT TABLE------------------
        Table first_table = new Table();

        first_table.row();//.width(265).padRight(30);


////////////////////////////////////////////////////////////////////////////////////


///////////////////////////////////////////GOLD TEAM////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        //Highest Word Score Table----------------

        Table innerRight = new Table();

        Table innerR2 = new Table();

        innerR2.add(hud.Y_HwScore).left();
        innerR2.row();
        innerR2.add().padBottom(1);
        innerR2.row();
        innerR2.add(hud.Y_HWOne).left();
        innerR2.row();
        innerR2.add(hud.Y_HWTwo).left();
        innerR2.row();
        innerR2.add(hud.Y_HWThree).left();
        innerR2.row();
        innerR2.add(hud.Y_HWFour).left();
        innerR2.row();
        innerR2.add(hud.Y_HWFive).left();

        //Highest Game Scores----------------------
        Table innerR3 = new Table();
        innerR3.add(hud.Y_HgScore).left();
        innerR3.row();
        innerR3.add().padBottom(1);
        innerR3.row();
        innerR3.add(hud.Y_HgOne).left();
        innerR3.row();
        innerR3.add(hud.Y_HgTwo).left();
        innerR3.row();
        innerR3.add(hud.Y_HgThree).left();
        innerR3.row();
        innerR3.add(hud.Y_HgFour).left();
        innerR3.row();
        innerR3.add(hud.Y_HgFive).left();

        innerRight.add(innerR2).padLeft(15).padTop(5);
        innerRight.add(innerR3).padLeft(45).padTop(5);


        //Frequently Played Words-----------------------
        Table innerRight2 = new Table();
        Table innerRR2 = new Table();
        innerRR2.add(hud.Y_FPWords);
        innerRR2.row();
        innerRR2.add().padBottom(1).left();
        innerRR2.row();
        innerRR2.add(hud.Y_FPOne).left();
        innerRR2.row();
        innerRR2.add(hud.Y_FPTwo).left();
        innerRR2.row();
        innerRR2.add(hud.Y_FPThree).left();

        //Frequent Oswego-Themed Words-------------------
        Table innerRR3 = new Table();
        innerRR3.add(hud.Y_FOWords);
        innerRR3.row();
        innerRR3.add().padBottom(1).left();
        innerRR3.row();
        innerRR3.add(hud.Y_FOOne).left();
        innerRR3.row();
        innerRR3.add(hud.Y_FOTwo).left();
        innerRR3.row();
        innerRR3.add(hud.Y_FOThree).left();

        innerRight2.add(innerRR2).padLeft(15).padTop(5);
        innerRight2.add(innerRR3).padLeft(35).padTop(5);


        //first_table.setDebug(true);

        //Longest Word-------------------
        Table innerRight3 = new Table();

        Table innerRRR2 = new Table();
        innerRRR2.add(hud.Y_LWWords).left().padTop(2);
        innerRRR2.row();
        innerRRR2.add().padBottom(1).left();
        innerRRR2.row();
        innerRRR2.add(hud.Y_LWOne);

        //Bonuses Used-------------------
        Table innerRRR3 = new Table();
        innerRRR3.add(hud.Y_BUWords).top().padLeft(10);

        innerRight3.add(innerRRR2).padLeft(15);
        innerRight3.add(innerRRR3).padLeft(40);


        //Top Players----------------------
        Table innerRight4 = new Table();

        Table innerRx4_2 = new Table();
        innerRx4_2.add(hud.Y_TPWords).left();
        innerRx4_2.row();
        innerRx4_2.add().padBottom(1).left();
        innerRx4_2.row();
        innerRx4_2.add(hud.Y_TPOne).left();
        innerRx4_2.row();
        innerRx4_2.add(hud.Y_TPTwo).left();
        innerRx4_2.row();
        innerRx4_2.add(hud.Y_TPThree).left();


        //Dirty Words Attempted & Average Score------------
        Table innerRx4_3 = new Table();
        innerRx4_3.add(hud.Y_WAWords).top().padTop(20).padLeft(50).padBottom(25);
        innerRx4_3.row();
        innerRx4_3.add(hud.Y_ASWords).padLeft(55);


        innerRight4.add(innerRx4_2).padLeft(10);
        innerRight4.add(innerRx4_3).padLeft(1);

        //T-test answer----------------------------------
        Table innerRight5 = new Table();
        innerRight5.add(hud.T_answer).left();


        //-----------RIGHT TABLE---------------
        Table second_table = new Table();
       // second_table.row().width(265).padRight(30);
        //second_table.add(hud.T_answer);
       // second_table.row().width(265).padRight(30);
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
        table.setBackground(new TextureRegionDrawable(
               // new TextureRegion(new Texture(Gdx.files.internal("StatsAssets/statsBackground.png")))));
                new TextureRegion(new Texture(Gdx.files.internal("Background.jpg")))));
        //table.setDebug(true);

        //The Green/Gold Team labels----------------
        table.add(hud.GreenTeamLabel).padTop(25).padLeft(15);
        table.add(hud.GoldTeamLabel).padTop(25).padRight(10);
        table.row();

        //the cumulative scores under the main labels
        table.add(hud.G_CScore).padTop(5).padLeft(20);
        table.add(hud.Y_CScore).padTop(5).padRight(10);
        table.row().expandY().height(48);

        //THE TABLES FOR THE HIGEST WORD SCORE AND HIGHEST GAME SCORES
        table.add(innerLeft.top()).top().left().padLeft(30);
        table.add(innerRight.top()).top().left().padBottom(50);
        table.row();

        //THE TABLES FOR FREQUENTLY PLAYED AND OSWEGO-THEMED WORDS
        table.add(innerLeft2.top()).top().left().padLeft(30);
        table.add(innerRight2.top()).top().left().padBottom(30);
        table.row();

        //THE TABLES FOR LONGEST WORDS AND BONUSES USED
        table.add(innerLeft3.top()).top().left().padLeft(30);
        table.add(innerRight3.top()).top().left();
        table.row();

        //THE TABLES FOR TOP PLAYERS, DIRTY WORDS ATTEMPTED & AVERAGE SCORE
        table.add(innerLeft4.top()).top().left().padLeft(30);
        table.add(innerRight4.top()).top().left().padBottom(20);
        table.row();

        //THE T TEST
        table.add(innerLeft5.top()).top().right();
        table.add(innerRight5.top()).top().left();



        //The left and right Tables withing Tables
        table.row().top();
        table.add(first_table.left().padLeft(40)).expand((int) (GameScreen.GUI_UNIT_SIZE*22),4);
        table.add(second_table).expand((int) (GameScreen.GUI_UNIT_SIZE*17), 4);


        super.addActor(table);

    }


}


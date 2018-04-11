package com.csc480.stats.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csc480.game.Engine.TextureManager;



public class Hud {

    String spacer = "                           ";

    private Integer Greenscore = 123456789;

    private Integer Goldscore = 2345;


//////////////////////GREEN TEAM////////////////////////////////////////////


    //------------- Upper  Team Label
    Label GreenTeamLabel = new Label("Green Team", FontManager.getInstance().title);
    Label GCScore = new Label(String.format("Cumulative Score: %06d",Greenscore),FontManager.getInstance().cumulative);


    //-----------Highest Word Score Numbers---------------
    Label GHwScore = new Label("Highest Word Scores:" ,TextureManager.getInstance().ui);
    private String GHWPlayer1 = "Louboutin";
    private String GHWPlayer2 = "Biscuit";
    private String GHWPlayer3 = "Lambo";
    private String GHWPlayer4 = "Linux";
    private String GHWPlayer5 = "Pikachu";
    private int GHWP1 = 64;
    private int GHWP2 = 44;
    private int GHWP3 = 37;
    private int GHWP4 = 28;
    private int GHWP5 = 10;
    Label GHWOne = new Label("1) " + String.format("%.10s",GHWPlayer1) + String.format("%10d pts",GHWP1),TextureManager.getInstance().ui);
    Label GHWTwo = new Label("2) " + String.format("%.10s",GHWPlayer2) + String.format("%10d pts",GHWP2),TextureManager.getInstance().ui);
    Label GHWThree = new Label("3) " + String.format("%.10s",GHWPlayer3) + String.format("%10d pts",GHWP3),TextureManager.getInstance().ui);
    Label GHWFour = new Label("4) " + String.format("%.10s",GHWPlayer4) + String.format("%10d pts",GHWP4),TextureManager.getInstance().ui);
    Label GHWFive = new Label("5) " + String.format("%.10s",GHWPlayer5) + String.format("%10d pts",GHWP5),TextureManager.getInstance().ui);


    //-----------Highest Game Score Numbers--------------------
    Label GHgScore = new Label("Highest Game Scores: " ,TextureManager.getInstance().ui);
    private int GHgP1 = 994;
    private int GHgP2 = 774;
    private int GHgP3 = 507;
    private int GHgP4 = 485;
    private int GHgP5 = 230;
    Label GHgOne = new Label("1) "  + String.format("%10d pts",GHgP1),TextureManager.getInstance().ui);
    Label GHgTwo = new Label("2) "  + String.format("%10d pts",GHgP2),TextureManager.getInstance().ui);
    Label GHgThree = new Label("3) " + String.format("%10d pts",GHgP3),TextureManager.getInstance().ui);
    Label GHgFour = new Label("4) "  + String.format("%10d pts",GHgP4),TextureManager.getInstance().ui);
    Label GHgFive = new Label("5) "  + String.format("%10d pts",GHgP5),TextureManager.getInstance().ui);


    //-----------Frequently Played Words------------------------
    Label GFPWords = new Label("Frequently Played Words: " ,TextureManager.getInstance().ui);


    //-----------Frequent Oswego-Themed Words------------------------
    Label GFOWords = new Label("Oswego-Themed Words: " ,TextureManager.getInstance().ui);




    ////////////////////////////////////GOLD TEAM////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////
    //------------- Upper  Team Label
    Label GoldTeamLabel = new Label("Gold Team", FontManager.getInstance().title);


    //-------------------- Team Green Stat Labels--------
    Label YCScore = new Label(String.format("Cumulative Score: %06d",Goldscore),FontManager.getInstance().cumulative);
    //temp
    Label ham = new Label("Highest Word Scores:" + spacer + "Highest Game Scores:" ,TextureManager.getInstance().ui);

    //------------- Team Green Stat Labels--------
    Label YHwScore = new Label("Highest Word Scores:" ,TextureManager.getInstance().ui);
    Label YHgScore = new Label("Highest Game Scores: " ,TextureManager.getInstance().ui);



    //-----------Highest Word Score Numbers---------------
    private String YHWPlayer1 = "Straight";
    private String YHWPlayer2 = "Sauce";
    private String YHWPlayer3 = "No";
    private String YHWPlayer4 = "Ketchup";
    private String YHWPlayer5 = "Skkrraaaatt";

    private int YHWP1 = 64;
    private int YHWP2 = 44;
    private int YHWP3 = 37;
    private int YHWP4 = 28;
    private int YHWP5 = 10;
    Label YHWOne = new Label("1) " + String.format("%.10s",YHWPlayer1) + String.format("%10d pts",YHWP1),TextureManager.getInstance().ui);
    Label YHWTwo = new Label("2) " + String.format("%.10s",YHWPlayer2) + String.format("%10d pts",YHWP2),TextureManager.getInstance().ui);
    Label YHWThree = new Label("3) " + String.format("%.10s",YHWPlayer3) + String.format("%10d pts",YHWP3),TextureManager.getInstance().ui);
    Label YHWFour = new Label("4) " + String.format("%.10s",YHWPlayer4) + String.format("%10d pts",YHWP4),TextureManager.getInstance().ui);
    Label YHWFive = new Label("5) " + String.format("%.10s",YHWPlayer5) + String.format("%10d pts",YHWP5),TextureManager.getInstance().ui);

    //-----------Highest Game Score Numbers---------------

    private int YHgP1 = 994;
    private int YHgP2 = 774;
    private int YHgP3 = 507;
    private int YHgP4 = 485;
    private int YHgP5 = 230;
    Label YHgOne = new Label("1) "  + String.format("%10d pts",YHgP1),TextureManager.getInstance().ui);
    Label YHgTwo = new Label("2) "  + String.format("%10d pts",YHgP2),TextureManager.getInstance().ui);
    Label YHgThree = new Label("3) " + String.format("%10d pts",YHgP3),TextureManager.getInstance().ui);
    Label YHgFour = new Label("4) "  + String.format("%10d pts",YHgP4),TextureManager.getInstance().ui);
    Label YHgFive = new Label("5) "  + String.format("%10d pts",YHgP5),TextureManager.getInstance().ui);


    //-----------Frequently Played Words------------------------
    Label YFPWords = new Label("Frequently Played Words: " ,TextureManager.getInstance().ui);


    //-----------Frequent Oswego-Themed Words------------------------
    Label YFOWords = new Label("Oswego-Themed Words: " ,TextureManager.getInstance().ui);





    //////////////////TEST STUFF//////////////////////////////
    Label test = new Label("TEST" ,TextureManager.getInstance().ui);
    Label test2 = new Label("TEST2" ,TextureManager.getInstance().ui);
    Label test3 = new Label("TEST3" ,TextureManager.getInstance().ui);
    Label test4 = new Label("TEST4" ,TextureManager.getInstance().ui);


}


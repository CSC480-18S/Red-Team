package com.csc480.stats.GUI.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csc480.game.Engine.TextureManager;
import com.csc480.stats.StatsConnection;


public class Hud {

    StatsConnection stats = new StatsConnection();

    String spacer = "                           ";

    private Integer Greenscore = 123456789;

    private Integer Goldscore = 2345;


///////////////////////////////////////////GREEN TEAM////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //------------- Upper  Team Label
    Label GreenTeamLabel = new Label("Green Team", FontManager.getInstance().title);
    Label G_CScore = new Label(String.format("Cumulative Score: %06d",Greenscore),FontManager.getInstance().cumulative);


    //-----------Highest Word Score Numbers---------------
    Label G_HwScore = new Label("Highest Word Scores:" ,TextureManager.getInstance().ui);
    /*
    private String G_HWPosition1 = stats.GreenStats().get().getData().getTopValueWord();
    */

    //Dummy Values
    private String G_HWPosition1 = "Christian";
    private String G_HWPosition2 = "Louboutin";
    private String G_HWPosition3 = "Lambo";
    private String G_HWPosition4 = "Linux";
    private String G_HWPosition5 = "Pikachu";
    private int G_HWP1 = 64;
    private int G_HWP2 = 44;
    private int G_HWP3 = 37;
    private int G_HWP4 = 28;
    private int G_HWP5 = 10;
    Label G_HWOne = new Label("1) " + String.format("%.10s",G_HWPosition1) + String.format("%10d pts",G_HWP1),TextureManager.getInstance().ui);
    Label G_HWTwo = new Label("2) " + String.format("%.10s",G_HWPosition2) + String.format("%10d pts",G_HWP2),TextureManager.getInstance().ui);
    Label G_HWThree = new Label("3) " + String.format("%.10s",G_HWPosition3) + String.format("%10d pts",G_HWP3),TextureManager.getInstance().ui);
    Label G_HWFour = new Label("4) " + String.format("%.10s",G_HWPosition4) + String.format("%10d pts",G_HWP4),TextureManager.getInstance().ui);
    Label G_HWFive = new Label("5) " + String.format("%.10s",G_HWPosition5) + String.format("%10d pts",G_HWP5),TextureManager.getInstance().ui);


    //-----------Highest Game Score Numbers--------------------
    Label G_HgScore = new Label("Highest Game Scores: " ,TextureManager.getInstance().ui);
    /*
    private int G_HgP1 = stats.GreenStats().get().getData().getHigestSingleGameScore();

    */

    //Dummy Values
    private int G_HgP1 = 994;
    private int G_HgP2 = 774;
    private int G_HgP3 = 507;
    private int G_HgP4 = 485;
    private int G_HgP5 = 230;
    Label G_HgOne = new Label("1) "  + String.format("%10d pts",G_HgP1),TextureManager.getInstance().ui);
    Label G_HgTwo = new Label("2) "  + String.format("%10d pts",G_HgP2),TextureManager.getInstance().ui);
    Label G_HgThree = new Label("3) " + String.format("%10d pts",G_HgP3),TextureManager.getInstance().ui);
    Label G_HgFour = new Label("4) "  + String.format("%10d pts",G_HgP4),TextureManager.getInstance().ui);
    Label G_HgFive = new Label("5) "  + String.format("%10d pts",G_HgP5),TextureManager.getInstance().ui);


    //-----------Frequently Played Words------------------------
    Label G_FPWords = new Label("Frequently Played Words: " ,TextureManager.getInstance().ui);
    private String G_FPPosition1 = "Is";
    private String G_FPPosition2 = "It";
    private String G_FPPosition3 = "Friday";
    private int G_FPP1 = 99;
    private int G_FPP2 = 13;
    private int G_FPP3 = 5;
    Label G_FPOne = new Label("1) " + String.format("%.10s",G_FPPosition1) + String.format("%10d pts",G_FPP1),TextureManager.getInstance().ui);
    Label G_FPTwo = new Label("2) " + String.format("%.10s",G_FPPosition2) + String.format("%10d pts",G_FPP2),TextureManager.getInstance().ui);
    Label G_FPThree = new Label("3) " + String.format("%.10s",G_FPPosition3) + String.format("%10d pts",G_FPP3),TextureManager.getInstance().ui);


    //-----------Frequent Oswego-Themed Words------------------------
    Label G_FOWords = new Label("Oswego-Themed Words: " ,TextureManager.getInstance().ui);

    private String G_FOPosition1 = "NO";
    private String G_FOPosition2 = "Its";
    private String G_FOPosition3 = "Not";
    private int G_FOP1 = 14;
    private int G_FOP2 = 7;
    private int G_FOP3 = 5;
    Label G_FOOne = new Label("1) " + String.format("%.10s",G_FOPosition1) + String.format("%10d pts",G_FOP1),TextureManager.getInstance().ui);
    Label G_FOTwo = new Label("2) " + String.format("%.10s",G_FOPosition2) + String.format("%10d pts",G_FOP2),TextureManager.getInstance().ui);
    Label G_FOThree = new Label("3) " + String.format("%.10s",G_FOPosition3) + String.format("%10d pts",G_FOP3),TextureManager.getInstance().ui);


    //-----------------------Longest Word-------------------------------
    Label G_LWWords = new Label("Longest Word: " ,TextureManager.getInstance().ui);
    /*
    private String G_LWPosition1 = stats.GreenStats().get().getData().getLongestWord();
    */

    //Dummy Values
    private String G_LWPosition1 = "Philanthropy";
    private int G_LWP1 = 7;
    Label G_LWOne = new Label( String.format("%.15s",G_LWPosition1) + String.format("%10d pts",G_LWP1),TextureManager.getInstance().ui);


    //-----------------------Bonuses Used-------------------------------
    private int G_BUP1 = 14;

    Label G_BUWords = new Label("Bonuses Used: "+ String.format("%10d",G_BUP1) ,TextureManager.getInstance().ui);


    //-----------------------Top Players-------------------------------
    Label G_TPWords = new Label("Top Players: " ,TextureManager.getInstance().ui);
    private String G_TPPosition1 = "Me";
    private String G_TPPosition2 = "Myself";
    private String G_TPPosition3 = "AndI";
    private int G_TPP1 = 99;
    private int G_TPP2 = 13;
    private int G_TPP3 = 5;
    Label G_TPOne = new Label("1) " + String.format("%.10s",G_TPPosition1) + String.format("%10d pts",G_TPP1),TextureManager.getInstance().ui);
    Label G_TPTwo = new Label("2) " + String.format("%.10s",G_TPPosition2) + String.format("%10d pts",G_TPP2),TextureManager.getInstance().ui);
    Label G_TPThree = new Label("3) " + String.format("%.10s",G_TPPosition3) + String.format("%10d pts",G_TPP3),TextureManager.getInstance().ui);



    //-----------------------Dirty Word Attempts & Average Score -------------------------------
    private int G_WAP1 = 997;
    private int G_ASP1 = 7;

    Label G_WAWords = new Label("Bad Word Attempts:"+ String.format("%10d",G_WAP1) ,TextureManager.getInstance().ui);

    Label G_ASWords = new Label("Average Score: "+ String.format("%10d",G_ASP1) ,TextureManager.getInstance().ui);








    ///////////////////////////////////////////GOLD TEAM////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //------------- Upper  Team Label
    Label GoldTeamLabel = new Label("Gold Team", FontManager.getInstance().title);


    //-------------------- Team Green Stat Labels--------
    Label Y_CScore = new Label(String.format("Cumulative Score: %06d",Goldscore),FontManager.getInstance().cumulative);
    //temp

    //------------- Team Green Stat Labels--------
    Label Y_HwScore = new Label("Highest Word Scores:" ,TextureManager.getInstance().ui);
    Label Y_HgScore = new Label("Highest Game Scores: " ,TextureManager.getInstance().ui);



    //-----------Highest Word Score Numbers---------------
    /*
    private String Y_HWPosition1 = stats.GoldStats().get().getData().getTopValueWord();
    */

    //Dummy Values
    private String Y_HWPosition1 = "Straight";
    private String Y_HWPosition2 = "Sauce";
    private String Y_HWPosition3 = "No";
    private String Y_HWPosition4 = "Ketchup";
    private String Y_HWPosition5 = "Skkrraaaatt";

    private int Y_HWP1 = 64;
    private int Y_HWP2 = 44;
    private int Y_HWP3 = 37;
    private int Y_HWP4 = 28;
    private int Y_HWP5 = 10;
    Label Y_HWOne = new Label("1) " + String.format("%.10s",Y_HWPosition1) + String.format("%10d pts",Y_HWP1),TextureManager.getInstance().ui);
    Label Y_HWTwo = new Label("2) " + String.format("%.10s",Y_HWPosition2) + String.format("%10d pts",Y_HWP2),TextureManager.getInstance().ui);
    Label Y_HWThree = new Label("3) " + String.format("%.10s",Y_HWPosition3) + String.format("%10d pts",Y_HWP3),TextureManager.getInstance().ui);
    Label Y_HWFour = new Label("4) " + String.format("%.10s",Y_HWPosition4) + String.format("%10d pts",Y_HWP4),TextureManager.getInstance().ui);
    Label Y_HWFive = new Label("5) " + String.format("%.10s",Y_HWPosition5) + String.format("%10d pts",Y_HWP5),TextureManager.getInstance().ui);

    //-----------Highest Game Score Numbers---------------

    private int Y_HgP1 = 994;
    private int Y_HgP2 = 774;
    private int Y_HgP3 = 507;
    private int Y_HgP4 = 485;
    private int Y_HgP5 = 230;
    Label Y_HgOne = new Label("1) "  + String.format("%10d pts",Y_HgP1),TextureManager.getInstance().ui);
    Label Y_HgTwo = new Label("2) "  + String.format("%10d pts",Y_HgP2),TextureManager.getInstance().ui);
    Label Y_HgThree = new Label("3) " + String.format("%10d pts",Y_HgP3),TextureManager.getInstance().ui);
    Label Y_HgFour = new Label("4) "  + String.format("%10d pts",Y_HgP4),TextureManager.getInstance().ui);
    Label Y_HgFive = new Label("5) "  + String.format("%10d pts",Y_HgP5),TextureManager.getInstance().ui);


    //-----------Frequently Played Words------------------------
    Label Y_FPWords = new Label("Frequently Played Words: " ,TextureManager.getInstance().ui);

    private String Y_FPPosition1 = "Is";
    private String Y_FPPosition2 = "It";
    private String Y_FPPosition3 = "Friday";
    private int Y_FPP1 = 99;
    private int Y_FPP2 = 13;
    private int Y_FPP3 = 5;
    Label Y_FPOne = new Label("1) " + String.format("%.10s",Y_FPPosition1) + String.format("%10d pts",Y_FPP1),TextureManager.getInstance().ui);
    Label Y_FPTwo = new Label("2) " + String.format("%.10s",Y_FPPosition2) + String.format("%10d pts",Y_FPP2),TextureManager.getInstance().ui);
    Label Y_FPThree = new Label("3) " + String.format("%.10s",Y_FPPosition3) + String.format("%10d pts",Y_FPP3),TextureManager.getInstance().ui);


    //-----------Frequent Oswego-Themed Words------------------------
    Label Y_FOWords = new Label("Oswego-Themed Words: " ,TextureManager.getInstance().ui);

    private String Y_FOPosition1 = "NO";
    private String Y_FOPosition2 = "Its";
    private String Y_FOPosition3 = "Not";
    private int Y_FOP1 = 14;
    private int Y_FOP2 = 7;
    private int Y_FOP3 = 5;
    Label Y_FOOne = new Label("1) " + String.format("%.10s",Y_FOPosition1) + String.format("%10d pts",Y_FOP1),TextureManager.getInstance().ui);
    Label Y_FOTwo = new Label("2) " + String.format("%.10s",Y_FOPosition2) + String.format("%10d pts",Y_FOP2),TextureManager.getInstance().ui);
    Label Y_FOThree = new Label("3) " + String.format("%.10s",Y_FOPosition3) + String.format("%10d pts",Y_FOP3),TextureManager.getInstance().ui);


    //-----------------------Longest Word-------------------------------
    Label Y_LWWords = new Label("Longest Word: " ,TextureManager.getInstance().ui);
    private String Y_LWPosition1 = "Philanthropy";
    private int Y_LWP1 = 7;
    Label Y_LWOne = new Label( String.format("%.15s",Y_LWPosition1) + String.format("%10d pts",Y_LWP1),TextureManager.getInstance().ui);


    //-----------------------Bonuses Used-------------------------------
    private int Y_BUP1 = 14;

    Label Y_BUWords = new Label("Bonuses Used: "+ String.format("%10d",Y_BUP1) ,TextureManager.getInstance().ui);


    //-----------------------Top Players-------------------------------
    Label Y_TPWords = new Label("Top Players: " ,TextureManager.getInstance().ui);
    private String Y_TPPosition1 = "No";
    private String Y_TPPosition2 = "TheOther";
    private String Y_TPPosition3 = "Guy";
    private int Y_TPP1 = 22;
    private int Y_TPP2 = 15;
    private int Y_TPP3 = 11;
    Label Y_TPOne = new Label("1) " + String.format("%.10s",Y_TPPosition1) + String.format("%10d pts",Y_TPP1),TextureManager.getInstance().ui);
    Label Y_TPTwo = new Label("2) " + String.format("%.10s",Y_TPPosition2) + String.format("%10d pts",Y_TPP2),TextureManager.getInstance().ui);
    Label Y_TPThree = new Label("3) " + String.format("%.10s",Y_TPPosition3) + String.format("%10d pts",Y_TPP3),TextureManager.getInstance().ui);



    //-----------------------Dirty Word Attempts & Average Score -------------------------------
    private int Y_WAP1 = 998;
    private int Y_ASP1 = 7;

    Label Y_WAWords = new Label("Bad Word Attempts:"+ String.format("%10d",Y_WAP1) ,TextureManager.getInstance().ui);

    Label Y_ASWords = new Label("Average Score: "+ String.format("%10d",Y_ASP1) ,TextureManager.getInstance().ui);


///////////////////////////////////////////T-TEST////////////////////////////////////////////////////////////
    private String T_value = "-0.38";

    Label T_text = new Label("T-VALUE =",FontManager.getInstance().cumulative);
    Label T_answer = new Label( String.format("%10s",T_value),FontManager.getInstance().cumulative);


    //////////////////TEST STUFF//////////////////////////////
    Label test = new Label("TEST" ,TextureManager.getInstance().ui);
    Label test2 = new Label("TEST2" ,TextureManager.getInstance().ui);
    Label test3 = new Label("TEST3" ,TextureManager.getInstance().ui);
    Label test4 = new Label("TEST4" ,TextureManager.getInstance().ui);


}


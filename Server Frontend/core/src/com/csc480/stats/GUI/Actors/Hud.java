package com.csc480.stats.GUI.Actors;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.csc480.stats.StatsConnection;


public class Hud {

    private StatsConnection stats = new StatsConnection();

    private Integer Greenscore = Integer.valueOf(stats.GreenStats().get().getData().getTotalScore());

    private Integer Goldscore = Integer.valueOf(stats.GoldStats().get().getData().getTotalScore());





///////////////////////////////////////////GREEN TEAM////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //------------- Upper  Team Label
    Label GreenTeamLabel = new Label("Green Team", FontManager.getInstance().title);
    Label G_CScore = new Label(String.format("Cumulative Score: %06d",Greenscore),FontManager.getInstance().cumulative);





    //-----------Highest Word Score Numbers---------------
    Label G_HwScore = new Label("Highest Word Scores:" ,FontManager.getInstance().normal);


    //Dummy Values

    private String setG_HWPosition1(){
        String G_HWPosition1;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=1){
            G_HWPosition1 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(0).getWord();
        }else {
            G_HWPosition1 = "nada1";
        }
        return G_HWPosition1;
    }
    private String setG_HWPosition2(){
        String G_HWPosition2;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=2){
            G_HWPosition2 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(1).getWord();
        }else {
            G_HWPosition2 = "nada2";
        }
        return G_HWPosition2;
    }
    private String setG_HWPosition3(){
        String G_HWPosition3;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=3){
            G_HWPosition3 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(2).getWord();
        }else {
            G_HWPosition3 = "nada3";
        }
        return G_HWPosition3;
    }
    private String setG_HWPosition4(){
        String G_HWPosition4;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=4){
            G_HWPosition4 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(3).getWord();
        }else {
            G_HWPosition4 = "nada4";
        }
        return G_HWPosition4;
    }
    private String setG_HWPosition5(){
        String G_HWPosition5;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=5){
            G_HWPosition5 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(4).getWord();
        }else {
            G_HWPosition5 = "nada5";
        }
        return G_HWPosition5;
    }



    private String setG_HWP1(){
        String G_HWP1;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 1){
            G_HWP1 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(0).getValue();
        }else {
            G_HWP1 = "00";
        }
        return G_HWP1;
    }
    private String setG_HWP2(){
        String G_HWP2;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 2){
            G_HWP2 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(1).getValue();
        }else {
            G_HWP2 = "01";
        }
        return G_HWP2;
    }
    private String setG_HWP3(){
        String G_HWP3;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 3){
            G_HWP3 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(2).getValue();
        }else {
            G_HWP3 = "02";
        }
        return G_HWP3;
    }
    private String setG_HWP4(){
        String G_HWP4;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 4){
            G_HWP4 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(3).getValue();
        }else {
            G_HWP4 = "03";
        }
        return G_HWP4;
    }
    private String setG_HWP5(){
        String G_HWP5;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 5){
            G_HWP5 = stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().get(4).getValue();
        }else {
            G_HWP5 = "04";
        }
        return G_HWP5;
    }

    Label G_HWOne = new Label("1) " + String.format("%.10s",setG_HWPosition1()) + String.format("%10s pts",setG_HWP1()),FontManager.getInstance().normal);
    Label G_HWTwo = new Label("2) " + String.format("%.10s",setG_HWPosition2()) + String.format("%10s pts",setG_HWP2()),FontManager.getInstance().normal);
    Label G_HWThree = new Label("3) " + String.format("%.10s",setG_HWPosition3()) + String.format("%10s pts",setG_HWP3()),FontManager.getInstance().normal);
    Label G_HWFour = new Label("4) " + String.format("%.10s",setG_HWPosition4()) + String.format("%10s pts",setG_HWP4()),FontManager.getInstance().normal);
    Label G_HWFive = new Label("5) " + String.format("%.10s",setG_HWPosition5()) + String.format("%10s pts",setG_HWP5()),FontManager.getInstance().normal);




    //-----------Highest Game Score Numbers--------------------
    Label G_HgScore = new Label("Highest Game Scores: " ,FontManager.getInstance().normal);

    //Dummy Values
    private String setG_HgP1(){
        String G_HgP1;
        if(stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().size() >=1){
            G_HgP1 = stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().get(0).getScore();
        }else {
            G_HgP1 = "00";
        }
        return G_HgP1;
    }
    private String setG_HgP2(){
        String G_HgP2;
        if(stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().size() >=2){
            G_HgP2 = stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().get(1).getScore();
        }else {
            G_HgP2 = "01";
        }
        return G_HgP2;
    }
    private String setG_HgP3(){
        String G_HgP3;
        if(stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().size() >=3){
            G_HgP3 = stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().get(2).getScore();
        }else {
            G_HgP3 = "02";
        }
        return G_HgP3;
    }
    private String setG_HgP4(){
        String G_HgP4;
        if(stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().size() >=4){
            G_HgP4 = stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().get(3).getScore();
        }else {
            G_HgP4 = "03";
        }
        return G_HgP4;
    }
    private String setG_HgP5(){
        String G_HgP5;
        if(stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().size() >=5){
            G_HgP5 = stats.GreenHighestGameScores().get().getData().get_embedded().getGameResults().get(4).getScore();
        }else {
            G_HgP5 = "03";
        }
        return G_HgP5;
    }


    Label G_HgOne = new Label("1) "  + String.format("%10s pts",setG_HgP1()),FontManager.getInstance().normal);
    Label G_HgTwo = new Label("2) "  + String.format("%10s pts",setG_HgP2()),FontManager.getInstance().normal);
    Label G_HgThree = new Label("3) " + String.format("%10s pts",setG_HgP3()),FontManager.getInstance().normal);
    Label G_HgFour = new Label("4) "  + String.format("%10s pts",setG_HgP4()),FontManager.getInstance().normal);
    Label G_HgFive = new Label("5) "  + String.format("%10s pts",setG_HgP5()),FontManager.getInstance().normal);



    //-----------Frequently Played Words------------------------
    Label G_FPWords = new Label("Frequently Played Words: " ,FontManager.getInstance().normal);



    private String setG_FPPosition1(){
        String G_FPPosition1;
        if(stats.GreenStats().get().getData().getFrequentlyPlayedWords().size() >=1){
            G_FPPosition1 =stats.GreenStats().get().getData().getFrequentlyPlayedWords().get(0).getWord();
        }else {
            G_FPPosition1 = "nada1";
        }
        return G_FPPosition1;
    }
    private String setG_FPPosition2(){
        String G_FPPosition2;
        if(stats.GreenStats().get().getData().getFrequentlyPlayedWords().size() >=2){
            G_FPPosition2 = stats.GreenStats().get().getData().getFrequentlyPlayedWords().get(1).getWord();
        }else {
            G_FPPosition2 = "nada2";
        }
        return G_FPPosition2;
    }
    private String setG_FPPosition3(){
        String G_FPPosition3;
        if(stats.GreenStats().get().getData().getFrequentlyPlayedWords().size() >=3){
            G_FPPosition3 = stats.GreenStats().get().getData().getFrequentlyPlayedWords().get(2).getWord();
        }else {
            G_FPPosition3 = "nada3";
        }
        return G_FPPosition3;
    }
    private String setG_FPP1(){
        String G_FPP1;
        if(stats.GreenStats().get().getData().getFrequentlyPlayedWords().size() >=1){
            G_FPP1 = stats.GreenStats().get().getData().getFrequentlyPlayedWords().get(0).getFrequency();
        }else {
            G_FPP1 = "00";
        }
        return G_FPP1;
    }
    private String setG_FPP2(){
        String G_FPP2;
        if(stats.GreenStats().get().getData().getFrequentlyPlayedWords().size() >=2){
            G_FPP2 = stats.GreenStats().get().getData().getFrequentlyPlayedWords().get(1).getFrequency();
        }else {
            G_FPP2 = "01";
        }
        return G_FPP2;
    }
    private String setG_FPP3(){
        String G_FPP3;
        if(stats.GreenStats().get().getData().getFrequentlyPlayedWords().size() >=3){
            G_FPP3 = stats.GreenStats().get().getData().getFrequentlyPlayedWords().get(2).getFrequency();
        }else {
            G_FPP3 = "02";
        }
        return G_FPP3;
    }

    Label G_FPOne = new Label("1) " + String.format("%.10s",setG_FPPosition1()) + String.format("%10s pts",setG_FPP1()),FontManager.getInstance().normal);
    Label G_FPTwo = new Label("2) " + String.format("%.10s",setG_FPPosition2()) + String.format("%10s pts",setG_FPP2()),FontManager.getInstance().normal);
    Label G_FPThree = new Label("3) " + String.format("%.10s",setG_FPPosition3()) + String.format("%10s pts",setG_FPP3()),FontManager.getInstance().normal);



    //-----------Frequent Oswego-Themed Words------------------------
    Label G_FOWords = new Label("Oswego-Themed Words: " ,FontManager.getInstance().normal);

    private String setG_FOPosition1(){
        String G_FOPosition1;
        if(stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().size() >=1){
            G_FOPosition1 = stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().get(0).getWord();
        }else {
            G_FOPosition1 = "nada0";
        }
        return G_FOPosition1;
    }
    private String setG_FOPosition2(){
        String G_FOPosition2;
        if(stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().size() >=2){
            G_FOPosition2 = stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().get(1).getWord();
        }else {
            G_FOPosition2 = "nada1";
        }
        return G_FOPosition2;
    }

    private String setG_FOPosition3(){
        String G_FOPosition3;
        if(stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().size() >=3){
            G_FOPosition3 = stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().get(2).getWord();
        }else {
            G_FOPosition3 = "nada2";
        }
        return G_FOPosition3;
    }


    private String setG_FOP1(){
        String G_FOP1;
        if(stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().size() >=1){
            G_FOP1 = stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().get(0).getFrequency();
        }else {
            G_FOP1 = "00";
        }
        return G_FOP1;
    }
    private String setG_FOP2(){
        String G_FOP2;
        if(stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().size() >=2){
            G_FOP2 = stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().get(1).getFrequency();
        }else {
            G_FOP2 = "01";
        }
        return G_FOP2;
    }
    private String setG_FOP3(){
        String G_FOP3;
        if(stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().size() >=3){
            G_FOP3 = stats.GreenStats().get().getData().getFrequentlySpecialPlayedWords().get(2).getFrequency();
        }else {
            G_FOP3 = "01";
        }
        return G_FOP3;
    }

    Label G_FOOne = new Label("1) " + String.format("%.10s",setG_FOPosition1()) + String.format("%10s pts",setG_FOP1()),FontManager.getInstance().normal);
    Label G_FOTwo = new Label("2) " + String.format("%.10s",setG_FOPosition2()) + String.format("%10s pts",setG_FOP2()),FontManager.getInstance().normal);
    Label G_FOThree = new Label("3) " + String.format("%.10s",setG_FOPosition3()) + String.format("%10s pts",setG_FOP3()),FontManager.getInstance().normal);


















    //-----------------------Longest Word-------------------------------
    Label G_LWWords = new Label("Longest Word: " ,FontManager.getInstance().normal);

    private String setG_LWPosition1(){
        String G_LWPosition1;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() != 0 && stats.GreenLongestWord().get().getData().getWord().length() != 0){
            G_LWPosition1 = stats.GreenLongestWord().get().getData().getWord();
        }else {
            G_LWPosition1 = "nada0";
        }
        return G_LWPosition1;
    }
    private String setG_LWP1(){
        String G_LWP1;
        if(stats.GreenHighestValueWords().get().getData().get_embedded().getPlayedWords().size() != 0 && stats.GreenLongestWord().get().getData().getValue().length() != 0){
            G_LWP1 = stats.GreenLongestWord().get().getData().getValue();
        }else {
            G_LWP1 = "0";
        }
        return G_LWP1;
    }

    Label G_LWOne = new Label( String.format("%.15s",setG_LWPosition1()) + String.format("%10s pts",setG_LWP1()),FontManager.getInstance().normal);


    //-----------------------Bonuses Used-------------------------------
    private int G_BUP1 = Integer.parseInt(stats.GreenStats().get().getData().getSpecialCount());

    Label G_BUWords = new Label("Bonuses Used: "+ String.format("%10d",G_BUP1) ,FontManager.getInstance().normal);






    //-----------------------Top Players-------------------------------
    Label G_TPWords = new Label("Top Players: " ,FontManager.getInstance().normal);


    private String setG_TPPosition1(){
        String G_TPPosition1;
        if(stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().size() >=1){
            G_TPPosition1 = stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().get(0).getUsername();
        }else {
            G_TPPosition1 = "nada0";
        }
        return G_TPPosition1;
    }
    private String setG_TPPosition2(){
        String G_TPPosition2;
        if(stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().size() >=2){
            G_TPPosition2 = stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().get(1).getUsername();
        }else {
            G_TPPosition2 = "nada1";
        }
        return G_TPPosition2;
    }
    private String setG_TPPosition3(){
        String G_TPPosition3;
        if(stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().size() >=3){
            G_TPPosition3 = stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().get(2).getUsername();
        }else {
            G_TPPosition3 = "nada2";
        }
        return G_TPPosition3;
    }


    private String setG_TPP1(){
        String G_TPP1;
        if(stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().size() >=1){
            G_TPP1 = stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().get(0).getScore();
        }else {
            G_TPP1 = "00";
        }
        return G_TPP1;
    }
    private String setG_TPP2(){
        String G_TPP2;
        if(stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().size() >=2){
            G_TPP2 = stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().get(1).getScore();
        }else {
            G_TPP2 = "01";
        }
        return G_TPP2;
    }
    private String setG_TPP3(){
        String G_TPP3;
        if(stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().size() >=3){
            G_TPP3 = stats.GreenTopPlayers().get().getData().get_embedded().getPlayers().get(2).getScore();
        }else {
            G_TPP3 = "02";
        }
        return G_TPP3;
    }

    Label G_TPOne = new Label("1) " + String.format("%.10s",setG_TPPosition1()) + String.format("%10s pts",setG_TPP1()),FontManager.getInstance().normal);
    Label G_TPTwo = new Label("2) " + String.format("%.10s",setG_TPPosition2()) + String.format("%10s pts",setG_TPP2()),FontManager.getInstance().normal);
    Label G_TPThree = new Label("3) " + String.format("%.10s",setG_TPPosition3()) + String.format("%10s pts",setG_TPP3()),FontManager.getInstance().normal);









    //-----------------------Dirty Word Attempts & Average Score -------------------------------
    private int G_WAP1 = Integer.parseInt(stats.GreenStats().get().getData().getDirtyCount());
    private int G_ASP1 = Integer.parseInt(stats.GreenStats().get().getData().getWinCount());

    Label G_WAWords = new Label("Bad Word Attempts:"+ String.format("%10d",G_WAP1) ,FontManager.getInstance().normal);

    Label G_ASWords = new Label("Win Count: "+ String.format("%10d",G_ASP1) ,FontManager.getInstance().normal);








    ///////////////////////////////////////////GOLD TEAM////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //------------- Upper  Team Label
    Label GoldTeamLabel = new Label("Gold Team", FontManager.getInstance().title);


    //-------------------- Team Green Stat Labels--------
    Label Y_CScore = new Label(String.format("Cumulative Score: %06d",Goldscore),FontManager.getInstance().cumulative);
    //temp

    //------------- Team Green Stat Labels--------




    //-----------Highest Word Score Numbers---------------
    Label Y_HwScore = new Label("Highest Word Scores:" ,FontManager.getInstance().normal);


    //Dummy Values

    private String setY_HWPosition1(){
        String Y_HWPosition1;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=1){
            Y_HWPosition1 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(0).getWord();
        }else {
            Y_HWPosition1 = "nada1";
        }
        return Y_HWPosition1;
    }
    private String setY_HWPosition2(){
        String Y_HWPosition2;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=2){
            Y_HWPosition2 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(1).getWord();
        }else {
            Y_HWPosition2 = "nada2";
        }
        return Y_HWPosition2;
    }
    private String setY_HWPosition3(){
        String Y_HWPosition3;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=3){
            Y_HWPosition3 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(2).getWord();
        }else {
            Y_HWPosition3 = "nada3";
        }
        return Y_HWPosition3;
    }
    private String setY_HWPosition4(){
        String Y_HWPosition4;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=4){
            Y_HWPosition4 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(3).getWord();
        }else {
            Y_HWPosition4 = "nada4";
        }
        return Y_HWPosition4;
    }
    private String setY_HWPosition5(){
        String Y_HWPosition5;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >=5){
            Y_HWPosition5 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(4).getWord();
        }else {
            Y_HWPosition5 = "nada5";
        }
        return Y_HWPosition5;
    }



    private String setY_HWP1(){
        String Y_HWP1;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 1){
            Y_HWP1 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(0).getValue();
        }else {
            Y_HWP1 = "00";
        }
        return Y_HWP1;
    }
    private String setY_HWP2(){
        String Y_HWP2;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 2){
            Y_HWP2 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(1).getValue();
        }else {
            Y_HWP2 = "01";
        }
        return Y_HWP2;
    }
    private String setY_HWP3(){
        String Y_HWP3;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 3){
            Y_HWP3 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(2).getValue();
        }else {
            Y_HWP3 = "02";
        }
        return Y_HWP3;
    }
    private String setY_HWP4(){
        String Y_HWP4;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 4){
            Y_HWP4 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(3).getValue();
        }else {
            Y_HWP4 = "03";
        }
        return Y_HWP4;
    }
    private String setY_HWP5(){
        String Y_HWP5;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() >= 5){
            Y_HWP5 = stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().get(4).getValue();
        }else {
            Y_HWP5 = "04";
        }
        return Y_HWP5;
    }

    Label Y_HWOne = new Label("1) " + String.format("%.10s",setY_HWPosition1()) + String.format("%10s pts",setY_HWP1()),FontManager.getInstance().normal);
    Label Y_HWTwo = new Label("2) " + String.format("%.10s",setY_HWPosition2()) + String.format("%10s pts",setY_HWP2()),FontManager.getInstance().normal);
    Label Y_HWThree = new Label("3) " + String.format("%.10s",setY_HWPosition3()) + String.format("%10s pts",setY_HWP3()),FontManager.getInstance().normal);
    Label Y_HWFour = new Label("4) " + String.format("%.10s",setY_HWPosition4()) + String.format("%10s pts",setY_HWP4()),FontManager.getInstance().normal);
    Label Y_HWFive = new Label("5) " + String.format("%.10s",setY_HWPosition5()) + String.format("%10s pts",setY_HWP5()),FontManager.getInstance().normal);


    //-----------Highest Game Score Numbers--------------------
    Label Y_HgScore = new Label("Highest Game Scores: " ,FontManager.getInstance().normal);

    //Dummy Values
    private String setY_HgP1(){
        String Y_HgP1;
        if(stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().size() >=1){
            Y_HgP1 = stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().get(0).getScore();
        }else {
            Y_HgP1 = "00";
        }
        return Y_HgP1;
    }
    private String setY_HgP2(){
        String Y_HgP2;
        if(stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().size() >=2){
            Y_HgP2 = stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().get(1).getScore();
        }else {
            Y_HgP2 = "01";
        }
        return Y_HgP2;
    }
    private String setY_HgP3(){
        String Y_HgP3;
        if(stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().size() >=3){
            Y_HgP3 = stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().get(2).getScore();
        }else {
            Y_HgP3 = "02";
        }
        return Y_HgP3;
    }
    private String setY_HgP4(){
        String Y_HgP4;
        if(stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().size() >=4){
            Y_HgP4 = stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().get(3).getScore();
        }else {
            Y_HgP4 = "03";
        }
        return Y_HgP4;
    }
    private String setY_HgP5(){
        String Y_HgP5;
        if(stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().size() >=5){
            Y_HgP5 = stats.GoldHighestGameScores().get().getData().get_embedded().getGameResults().get(4).getScore();
        }else {
            Y_HgP5 = "03";
        }
        return Y_HgP5;
    }


    Label Y_HgOne = new Label("1) "  + String.format("%10s pts",setY_HgP1()),FontManager.getInstance().normal);
    Label Y_HgTwo = new Label("2) "  + String.format("%10s pts",setY_HgP2()),FontManager.getInstance().normal);
    Label Y_HgThree = new Label("3) " + String.format("%10s pts",setY_HgP3()),FontManager.getInstance().normal);
    Label Y_HgFour = new Label("4) "  + String.format("%10s pts",setY_HgP4()),FontManager.getInstance().normal);
    Label Y_HgFive = new Label("5) "  + String.format("%10s pts",setY_HgP5()),FontManager.getInstance().normal);


    //-----------Frequently Played Words------------------------
    Label Y_FPWords = new Label("Frequently Played Words: " ,FontManager.getInstance().normal);



    private String setY_FPPosition1(){
        String Y_FPPosition1;
        if(stats.GoldStats().get().getData().getFrequentlyPlayedWords().size() >=1){
            Y_FPPosition1 =stats.GoldStats().get().getData().getFrequentlyPlayedWords().get(0).getWord();
        }else {
            Y_FPPosition1 = "nada1";
        }
        return Y_FPPosition1;
    }
    private String setY_FPPosition2(){
        String Y_FPPosition2;
        if(stats.GoldStats().get().getData().getFrequentlyPlayedWords().size() >=2){
            Y_FPPosition2 = stats.GoldStats().get().getData().getFrequentlyPlayedWords().get(1).getWord();
        }else {
            Y_FPPosition2 = "nada2";
        }
        return Y_FPPosition2;
    }
    private String setY_FPPosition3(){
        String Y_FPPosition3;
        if(stats.GoldStats().get().getData().getFrequentlyPlayedWords().size() >=3){
            Y_FPPosition3 = stats.GoldStats().get().getData().getFrequentlyPlayedWords().get(2).getWord();
        }else {
            Y_FPPosition3 = "nada3";
        }
        return Y_FPPosition3;
    }


    private String setY_FPP1(){
        String Y_FPP1;
        if(stats.GoldStats().get().getData().getFrequentlyPlayedWords().size() >=1){
            Y_FPP1 = stats.GoldStats().get().getData().getFrequentlyPlayedWords().get(0).getFrequency();
        }else {
            Y_FPP1 = "00";
        }
        return Y_FPP1;
    }
    private String setY_FPP2(){
        String Y_FPP2;
        if(stats.GoldStats().get().getData().getFrequentlyPlayedWords().size() >=2){
            Y_FPP2 = stats.GoldStats().get().getData().getFrequentlyPlayedWords().get(1).getFrequency();
        }else {
            Y_FPP2 = "01";
        }
        return Y_FPP2;
    }
    private String setY_FPP3(){
        String Y_FPP3;
        if(stats.GoldStats().get().getData().getFrequentlyPlayedWords().size() >=3){
            Y_FPP3 = stats.GoldStats().get().getData().getFrequentlyPlayedWords().get(2).getFrequency();
        }else {
            Y_FPP3 = "02";
        }
        return Y_FPP3;
    }

    Label Y_FPOne = new Label("1) " + String.format("%.10s",setY_FPPosition1()) + String.format("%10s pts",setY_FPP1()),FontManager.getInstance().normal);
    Label Y_FPTwo = new Label("2) " + String.format("%.10s",setY_FPPosition2()) + String.format("%10s pts",setY_FPP2()),FontManager.getInstance().normal);
    Label Y_FPThree = new Label("3) " + String.format("%.10s",setY_FPPosition3()) + String.format("%10s pts",setY_FPP3()),FontManager.getInstance().normal);



    //-----------Frequent Oswego-Themed Words------------------------
    Label Y_FOWords = new Label("Oswego-Themed Words: " ,FontManager.getInstance().normal);

    private String setY_FOPosition1(){
        String Y_FOPosition1;
        if(stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().size() >=1){
            Y_FOPosition1 = stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().get(0).getWord();
        }else {
            Y_FOPosition1 = "nada0";
        }
        return Y_FOPosition1;
    }
    private String setY_FOPosition2(){
        String Y_FOPosition2;
        if(stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().size() >=2){
            Y_FOPosition2 = stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().get(1).getWord();
        }else {
            Y_FOPosition2 = "nada1";
        }
        return Y_FOPosition2;
    }

    private String setY_FOPosition3(){
        String Y_FOPosition3;
        if(stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().size() >=3){
            Y_FOPosition3 = stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().get(2).getWord();
        }else {
            Y_FOPosition3 = "nada2";
        }
        return Y_FOPosition3;
    }


    private String setY_FOP1(){
        String Y_FOP1;
        if(stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().size() >=1){
            Y_FOP1 = stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().get(0).getFrequency();
        }else {
            Y_FOP1 = "00";
        }
        return Y_FOP1;
    }
    private String setY_FOP2(){
        String Y_FOP2;
        if(stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().size() >=2){
            Y_FOP2 = stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().get(1).getFrequency();
        }else {
            Y_FOP2 = "01";
        }
        return Y_FOP2;
    }
    private String setY_FOP3(){
        String Y_FOP3;
        if(stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().size() >=3){
            Y_FOP3 = stats.GoldStats().get().getData().getFrequentlySpecialPlayedWords().get(2).getFrequency();
        }else {
            Y_FOP3 = "01";
        }
        return Y_FOP3;
    }

    Label Y_FOOne = new Label("1) " + String.format("%.10s",setY_FOPosition1()) + String.format("%10s pts",setY_FOP1()),FontManager.getInstance().normal);
    Label Y_FOTwo = new Label("2) " + String.format("%.10s",setY_FOPosition2()) + String.format("%10s pts",setY_FOP2()),FontManager.getInstance().normal);
    Label Y_FOThree = new Label("3) " + String.format("%.10s",setY_FOPosition3()) + String.format("%10s pts",setY_FOP3()),FontManager.getInstance().normal);




    //-----------------------Longest Word-------------------------------
    Label Y_LWWords = new Label("Longest Word: " ,FontManager.getInstance().normal);

    private String setY_LWPosition1(){
        String Y_LWPosition1;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() != 0 && stats.GoldLongestWord().get().getData().getWord().length() != 0){
            Y_LWPosition1 = stats.GoldLongestWord().get().getData().getWord();
        }else {
            Y_LWPosition1 = "nada0";
        }
        return Y_LWPosition1;
    }


    private String setY_LWP1(){
        String Y_LWP1;
        if(stats.GoldHighestValueWords().get().getData().get_embedded().getPlayedWords().size() != 0 && stats.GoldLongestWord().get().getData().getValue().length() != 0){
            Y_LWP1 = stats.GoldLongestWord().get().getData().getValue();
        }else {
            Y_LWP1 = "0";
        }
        return Y_LWP1;
    }

    Label Y_LWOne = new Label( String.format("%.15s",setY_LWPosition1()) + String.format("%10s pts",setY_LWP1()),FontManager.getInstance().normal);


    //-----------------------Bonuses Used-------------------------------
    private int Y_BUP1 = Integer.parseInt(stats.GoldStats().get().getData().getSpecialCount());

    Label Y_BUWords = new Label("Bonuses Used: "+ String.format("%10d",Y_BUP1) ,FontManager.getInstance().normal);






    //-----------------------Top Players-------------------------------
    Label Y_TPWords = new Label("Top Players: " ,FontManager.getInstance().normal);


    private String setY_TPPosition1(){
        String Y_TPPosition1;
        if(stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().size() >=1){
            Y_TPPosition1 = stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().get(0).getUsername();
        }else {
            Y_TPPosition1 = "nada0";
        }
        return Y_TPPosition1;
    }
    private String setY_TPPosition2(){
        String Y_TPPosition2;
        if(stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().size() >=2){
            Y_TPPosition2 = stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().get(1).getUsername();
        }else {
            Y_TPPosition2 = "nada1";
        }
        return Y_TPPosition2;
    }
    private String setY_TPPosition3(){
        String Y_TPPosition3;
        if(stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().size() >=3){
            Y_TPPosition3 = stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().get(2).getUsername();
        }else {
            Y_TPPosition3 = "nada2";
        }
        return Y_TPPosition3;
    }


    private String setY_TPP1(){
        String Y_TPP1;
        if(stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().size() >=1){
            Y_TPP1 = stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().get(0).getScore();
        }else {
            Y_TPP1 = "00";
        }
        return Y_TPP1;
    }
    private String setY_TPP2(){
        String Y_TPP2;
        if(stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().size() >=2){
            Y_TPP2 = stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().get(1).getScore();
        }else {
            Y_TPP2 = "01";
        }
        return Y_TPP2;
    }
    private String setY_TPP3(){
        String Y_TPP3;
        if(stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().size() >=3){
            Y_TPP3 = stats.GoldTopPlayers().get().getData().get_embedded().getPlayers().get(2).getScore();
        }else {
            Y_TPP3 = "02";
        }
        return Y_TPP3;
    }

    Label Y_TPOne = new Label("1) " + String.format("%.10s",setY_TPPosition1()) + String.format("%10s pts",setY_TPP1()),FontManager.getInstance().normal);
    Label Y_TPTwo = new Label("2) " + String.format("%.10s",setY_TPPosition2()) + String.format("%10s pts",setY_TPP2()),FontManager.getInstance().normal);
    Label Y_TPThree = new Label("3) " + String.format("%.10s",setY_TPPosition3()) + String.format("%10s pts",setY_TPP3()),FontManager.getInstance().normal);




    //-----------------------Dirty Word Attempts & Average Score -------------------------------
    private int Y_WAP1 = Integer.parseInt(stats.GoldStats().get().getData().getDirtyCount());
    private int Y_ASP1 = Integer.parseInt(stats.GoldStats().get().getData().getWinCount());

    Label Y_WAWords = new Label("Bad Word Attempts:"+ String.format("%10d",Y_WAP1) ,FontManager.getInstance().normal);

    Label Y_ASWords = new Label("Win Count: "+ String.format("%10d",Y_ASP1) ,FontManager.getInstance().normal);




    ///////////////////////////////////////////T-TEST////////////////////////////////////////////////////////////
    private double T_value = stats.calcTTest();



    Label T_text = new Label("T-VALUE =",FontManager.getInstance().tvalue);
    Label T_answer = new Label( String.format("%10s",T_value),FontManager.getInstance().tvalue);



}


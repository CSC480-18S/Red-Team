package com.csc480.stats;

import com.csc480.stats.jsonObjects.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Optional;
import java.util.Scanner;

/**
 * This class creates a connection object used to query the
 * backend for team and user stats
 */

public class StatsConnection {
    private static final String BASEURL = "http://localhost:3001/";
    private static final String CHARSET = "UTF-8";
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * Retrieve team stats JSON
     * <p>
     * This method queries the backend for a specific team's stats
     *
     * @param  team  A String representation of the name of the team to be looked up
     * @return           The JSON of the team stats in String form, otherwise an empty
     */

    public Optional<String> getTeamStats(final String team) throws UnsupportedEncodingException {
        final String query = String.format(URLEncoder.encode(team, CHARSET));

        try {
            final URLConnection connection = new URL(BASEURL + "teams/" + query).openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            return Optional.of(scanner.useDelimiter("\\A").next());
        } catch (ConnectException ce) {
            System.out.println("The server backend isn't running, please start it along with the database server" +
                    " if it isn't running already.");
            return Optional.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve Gold team data
     * <p>
     * This method takes the JSON from getTeamStats and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse object that contains a TeamData object, which contains
     * the individual team stat values
     */

    public Optional<JsonResponse> GoldStats() {
        try {
            if (getTeamStats("Gold").isPresent()) {
                String goldJson = getTeamStats("Gold").get();
                return Optional.of(gson.fromJson(goldJson, JsonResponse.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve Green team data
     * <p>
     * This method takes the JSON from getTeamStats and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse object that contains a TeamData object, which contains
     * the individual team stat values
     */

    public Optional<JsonResponse> GreenStats() {
        try {
            if (getTeamStats("Green").isPresent()) {
                String greenJson = getTeamStats("Green").get();
                return Optional.of(gson.fromJson(greenJson, JsonResponse.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve highest game scores JSON
     * <p>
     * This method queries the backend for a specific team's highest game scores
     *
     * @param  team  A String representation of the name of the team to be looked up
     * @return           The JSON of the team highest game scores in String form, otherwise an empty
     */

    public Optional<String> getHighestGameScores(final String team) throws UnsupportedEncodingException {
        final String query = String.format(URLEncoder.encode(team, CHARSET));

        try {
            final URLConnection connection = new URL(BASEURL + "teams/" + query + "/highestGameScores").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            return Optional.of(scanner.useDelimiter("\\A").next());
        } catch (ConnectException ce) {
            System.out.println("The server backend isn't running, please start it along with the database server" +
                    " if it isn't running already.");
            return Optional.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve Gold highest game scores
     * <p>
     * This method takes the JSON from getHighestGameScores and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse2 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse2> GoldHighestGameScores() {
        try {
            if (getHighestGameScores("Gold").isPresent()) {
                String goldJson = getHighestGameScores("Gold").get();
                return Optional.of(gson.fromJson(goldJson, JsonResponse2.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve Green highest game scores
     * <p>
     * This method takes the JSON from getHighestGameScores and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse2 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse2> GreenHighestGameScores() {
        try {
            if (getHighestGameScores("Green").isPresent()) {
                String greenJson = getHighestGameScores("Green").get();
                return Optional.of(gson.fromJson(greenJson, JsonResponse2.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve game scores JSON
     * <p>
     * This method queries the backend for a specific team's game scores
     *
     * @param  team  A String representation of the name of the team to be looked up
     * @return           The JSON of the team game scores in String form, otherwise an empty
     */

    public Optional<String> getAllGameScores(final String team) throws UnsupportedEncodingException {
        final String query = String.format(URLEncoder.encode(team, CHARSET));

        try {
            final URLConnection connection = new URL(BASEURL + "teams/" + query + "/gameResults").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            return Optional.of(scanner.useDelimiter("\\A").next());
        } catch (ConnectException ce) {
            System.out.println("The server backend isn't running, please start it along with the database server" +
                    " if it isn't running already.");
            return Optional.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve all of Gold's game scores
     * <p>
     * This method takes the JSON from getALlGameScores and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse2 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse2> GoldGameScores() {
        try {
            if (getAllGameScores("Gold").isPresent()) {
                String goldJson = getAllGameScores("Gold").get();
                return Optional.of(gson.fromJson(goldJson, JsonResponse2.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve all of Green's game scores
     * <p>
     * This method takes the JSON from getALlGameScores and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse2 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse2> GreenGameScores() {
        try {
            if (getAllGameScores("Green").isPresent()) {
                String greenJson = getAllGameScores("Green").get();
                return Optional.of(gson.fromJson(greenJson, JsonResponse2.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve top players JSON
     * <p>
     * This method queries the backend for a specific team's top players
     *
     * @param  team  A String representation of the name of the team to be looked up
     * @return           The JSON of the team top players in String form, otherwise an empty
     */

    public Optional<String> getTopPlayers(final String team) throws UnsupportedEncodingException {
        final String query = String.format(URLEncoder.encode(team, CHARSET));

        try {
            final URLConnection connection = new URL(BASEURL + "teams/" + query + "/topPlayers").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            return Optional.of(scanner.useDelimiter("\\A").next());
        } catch (ConnectException ce) {
            System.out.println("The server backend isn't running, please start it along with the database server" +
                    " if it isn't running already.");
            return Optional.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve all of Gold's top players
     * <p>
     * This method takes the JSON from getTopPlayers and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse3 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse3> GoldTopPlayers() {
        try {
            if (getTopPlayers("Gold").isPresent()) {
                String goldJson = getTopPlayers("Gold").get();
                return Optional.of(gson.fromJson(goldJson, JsonResponse3.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve all of Green's top players
     * <p>
     * This method takes the JSON from getTopPlayers and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse3 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse3> GreenTopPlayers() {
        try {
            if (getTopPlayers("Green").isPresent()) {
                String greenJson = getTopPlayers("Green").get();
                return Optional.of(gson.fromJson(greenJson, JsonResponse3.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve players JSON
     * <p>
     * This method queries the backend for all of a specific team's players
     *
     * @param  team  A String representation of the name of the team to be looked up
     * @return           The JSON of the team players in String form, otherwise an empty
     */

    public Optional<String> getAllPlayers(final String team) throws UnsupportedEncodingException {
        final String query = String.format(URLEncoder.encode(team, CHARSET));

        try {
            final URLConnection connection = new URL(BASEURL + "teams/" + query + "/players").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            return Optional.of(scanner.useDelimiter("\\A").next());
        } catch (ConnectException ce) {
            System.out.println("The server backend isn't running, please start it along with the database server" +
                    " if it isn't running already.");
            return Optional.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve all of Gold's players
     * <p>
     * This method takes the JSON from getAllPlayers and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse3 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse3> GoldPlayers() {
        try {
            if (getAllPlayers("Gold").isPresent()) {
                String goldJson = getAllPlayers("Gold").get();
                return Optional.of(gson.fromJson(goldJson, JsonResponse3.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve all of Green's players
     * <p>
     * This method takes the JSON from getAllPlayers and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse3 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse3> GreenPlayers() {
        try {
            if (getAllPlayers("Green").isPresent()) {
                String greenJson = getAllPlayers("Green").get();
                return Optional.of(gson.fromJson(greenJson, JsonResponse3.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve highest value words JSON
     * <p>
     * This method queries the backend for all of a specific team's highest
     * value words
     *
     * @param  team  A String representation of the name of the team to be looked up
     * @return       The JSON of the team highest value words in String form, otherwise an empty
     */

    public Optional<String> getHighestValueWords(final String team) throws UnsupportedEncodingException {
        final String query = String.format(URLEncoder.encode(team, CHARSET));

        try {
            final URLConnection connection = new URL(BASEURL + "teams/" + query + "/highestValueWords").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            return Optional.of(scanner.useDelimiter("\\A").next());
        } catch (ConnectException ce) {
            System.out.println("The server backend isn't running, please start it along with the database server" +
                    " if it isn't running already.");
            return Optional.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve all of Gold's highest value words
     * <p>
     * This method takes the JSON from getAllPlayers and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse4 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse4> GoldHighestValueWords() {
        try {
            if (getHighestValueWords("Gold").isPresent()) {
                String goldJson = getHighestValueWords("Gold").get();
                return Optional.of(gson.fromJson(goldJson, JsonResponse4.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve all of Green's highest value words
     * <p>
     * This method takes the JSON from getAllPlayers and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse4 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse4> GreenHighestValueWords() {
        try {
            if (getHighestValueWords("Green").isPresent()) {
                String greenJson = getHighestValueWords("Green").get();
                return Optional.of(gson.fromJson(greenJson, JsonResponse4.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve longest word JSON
     * <p>
     * This method queries the backend for a specific team's longest
     * word
     *
     * @param  team  A String representation of the name of the team to be looked up
     * @return       The JSON of the team longest word in String form, otherwise an empty
     */

    public Optional<String> getLongestWord(final String team) throws UnsupportedEncodingException {
        final String query = String.format(URLEncoder.encode(team, CHARSET));

        try {
            final URLConnection connection = new URL(BASEURL + "teams/" + query + "/longestWord").openConnection();
            Scanner scanner = new Scanner(connection.getInputStream());
            return Optional.of(scanner.useDelimiter("\\A").next());
        } catch (ConnectException ce) {
            System.out.println("The server backend isn't running, please start it along with the database server" +
                    " if it isn't running already.");
            return Optional.empty();
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve Gold's longest word
     * <p>
     * This method takes the JSON from getAllPlayers and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse5 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse5> GoldLongestWord() {
        try {
            if (getLongestWord("Gold").isPresent()) {
                String goldJson = getLongestWord("Gold").get();
                return Optional.of(gson.fromJson(goldJson, JsonResponse5.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * Retrieve Green's longest word
     * <p>
     * This method takes the JSON from getAllPlayers and uses Gson to
     * create an object that is used to separate the individual attributes
     * into variables that can be called on when updating the GUI
     *
     * @return       A JsonResponse5 object that contains nested object which contain the attributes
     */

    public Optional<JsonResponse5> GreenLongestWord() {
        try {
            if (getLongestWord("Green").isPresent()) {
                String greenJson = getLongestWord("Green").get();
                return Optional.of(gson.fromJson(greenJson, JsonResponse5.class));
            } else {
                return Optional.empty();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}

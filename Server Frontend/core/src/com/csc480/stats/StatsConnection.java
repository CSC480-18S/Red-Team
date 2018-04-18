package com.csc480.stats;

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
    private static final String BASEURL = "http://localhost:3000/api/";
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
     * @return       A JsonResponse object that contains a teamData object, which contains
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
     * @return       A JsonResponse object that contains a teamData object, which contains
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
}

package stats;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class StatsConnection {
    static final String BASEURL = "http://localhost:3000/api/";
    static final String CHARSET = "UTF-8";

    public InputStream getUserStats(String username) throws UnsupportedEncodingException {
        String query = String.format(URLEncoder.encode(username, CHARSET));
        InputStream response = null;

        try {
            URLConnection connection = new URL(BASEURL + "players/" + query).openConnection();
            response = connection.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
            return response;

    }
}

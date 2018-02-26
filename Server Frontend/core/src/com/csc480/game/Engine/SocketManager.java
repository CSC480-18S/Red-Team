package com.csc480.game.Engine;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;

/**
 * This singleton class will manage the socket connection to the server as well as all incoming and outgoing events.
 */
public class SocketManager {
    private Socket socket;
    private static SocketManager instance;

    public static SocketManager getInstance() {
        if(instance == null)
            instance = new SocketManager();
        return instance;
    }

    /**
     * Connect to the Server
     */
    public void ConnectSocket(){
        try {
            socket = IO.socket("http://localhost:3000");
            socket.connect();
        } catch (URISyntaxException e){
            System.err.println(e);
        }
    }

    /**
     * Define the actions to be taken when events occur
     */
    public void setUpEvents(){
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("connection");
                //simple example of how to access the data sent from the server
                JSONObject data = (JSONObject) args[0];
                try{
                    String something = data.getString("something");
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("disconnection");
            }
        });
    }

    /**
     * Pulls the JSON from the connected url into a string.
     * ASSUMES connection is active
     * @param myConn the active connection to url
     * @return the JSON from the website in string format
     */
    public static JSONObject GetJSON(HttpURLConnection myConn){
        String theJSON = "";
        String inputLine;
        StringBuilder inputBuilder;
        BufferedReader in;

        try{
            in = new BufferedReader(new InputStreamReader(myConn.getInputStream()));
            inputBuilder = new StringBuilder("");
            while((inputLine = in.readLine()) != null){
                inputBuilder.append(inputLine+"\n");
            }
            return new JSONObject(inputBuilder.toString());
        }catch(MalformedURLException e){
            System.err.println(e);
        }catch(IOException e){
            System.err.println(e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Connects to the backend API
     * @return the connection
     */
    public static HttpURLConnection GetBackendConnection(String endpoint, String method, JSONObject data){
        URL myURL;
        HttpURLConnection myConn;
        try{
            myURL = new URL("http://localhost:3000/"+endpoint);
            myConn = (HttpURLConnection)myURL.openConnection();
            myConn.setRequestMethod(method);
            byte[] outputInBytes = data.toString().getBytes("UTF-8");
            try{
                OutputStream os = myConn.getOutputStream();
                os.write( outputInBytes );
            } catch (IOException e){
                e.printStackTrace();
            }
            myConn.connect();
            return myConn;
        }catch(MalformedURLException e){
            e.printStackTrace();
        }catch(IOException e){
            e.printStackTrace();
        }
        return null;
    }

}

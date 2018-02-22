package com.csc480.game.Engine;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;
import java.net.URISyntaxException;

/**
 * This singleton class will manage the socket connection to the server as well as all incoming and outgoing events.
 */
public class SocketManager {
    public Socket socket;

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

}

package com.csc480.game.Engine;

import com.badlogic.gdx.math.Vector2;
import com.csc480.game.Engine.Model.AI;
import com.csc480.game.Engine.Model.PlayIdea;
import com.csc480.game.Engine.Model.TileData;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
                try {
                    JSONObject data = (JSONObject) args[0];
                    String something = data.getString("something");
                }catch(ArrayIndexOutOfBoundsException e){
                    e.printStackTrace();
                }catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                System.out.println("disconnection");
            }
        }).on(Socket.EVENT_MESSAGE, new Emitter.Listener() {
            @Override
            public void call(Object... args) {

            }
        });
    }

    /**
     * just an example function of how we will hit the backend's endpoints (not events)
     */
    public void ExamplePOSTBackEnd(){
        /*
        try{
            JSONObject temp = new JSONObject();
            System.out.println("best plat word="+bestPlay.myWord);
            temp.put("word",bestPlay.myWord);
            Vector2 pos = bestPlay.GetStartPos();
            temp.put("x", (int)pos.x);
            temp.put("y", (int)pos.y);
            temp.put("h", bestPlay.isHorizontalPlay());
            JSONArray words = new JSONArray();
            words.put(temp);
            System.out.println(words.toString());
            String s = words.toString();
            //System.out.println(SocketManager.GetJSON(SocketManager.GetBackendConnection("/api/game/playword", "POST", postIt)).toString());
            System.out.println(
                    Unirest.post("http://localhost:3000/api/game/playWords")
                            .header("accept", "application/json")
                            .header("Content-Type", "application/json")
                            .body(words.toString())
                            .asString());
        } catch (UnirestException e) {
            e.printStackTrace();
        }
        */
    }

    public TileData[][] getBoardState(){
        //REMEMBER THAT THE Y positions need to be inverted so (10-ypos)
        throw new NotImplementedException();
    }

    public void BroadcastNewAI(AI tempRobot){
        throw new NotImplementedException();
        //socket.emit("userConnected", )
    }

    public void BroadcastAIPlay(PlayIdea AIplay){
        //REMEMBER THAT THE Y positions need to be inverted so (10-ypos)
        throw new NotImplementedException();
    }
}

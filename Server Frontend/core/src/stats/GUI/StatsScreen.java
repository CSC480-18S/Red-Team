package stats.GUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import stats.StatsViewer;

public class StatsScreen implements Screen {
    private StatsViewer container;
    float aspectRatio;
    //Scene2D stuff and Actors we need to keep references to
    Stage stage;
    private Viewport view;
    private OrthographicCamera viewCam;

    public StatsScreen(StatsViewer belongsTo){
        container = belongsTo;
        aspectRatio = (float) Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        System.out.println("aspectRatio: "+aspectRatio);

        //set the SceneGraph stage
        viewCam = new OrthographicCamera();
        view = new FitViewport(Gdx.graphics.getHeight(), Gdx.graphics.getHeight()*aspectRatio, viewCam);
        view.apply();
        stage = new Stage(view);

        Table t = new Table();
        t.setWidth(Gdx.graphics.getWidth()*aspectRatio);
        t.setHeight(Gdx.graphics.getHeight());
        t.setDebug(true);

        stage.addActor(t);



    }
    @Override
    public void show() {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
    }

    @Override
    public void render(float delta) {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        stage.getViewport().update(Gdx.graphics.getWidth(),Gdx.graphics.getHeight(),true);
        //Clear the screen from the last frame
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        //Set the entire screen to this color
        Gdx.gl.glClearColor(.666f, .666f, .666f, 1);
        //perform the actions of the actors
        stage.act(delta);
        //render the actors
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        aspectRatio = (float)Gdx.graphics.getHeight() / (float)Gdx.graphics.getWidth();
        stage.getViewport().update(width,height,true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}

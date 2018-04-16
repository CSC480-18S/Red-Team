package com.csc480.stats;

import com.badlogic.gdx.ApplicationAdapter;

public class StatsAdapter extends ApplicationAdapter {
//    OswebbleGame oswebbleGame;
    StatsViewer statsViewer;

    @Override
    public void create () {
        statsViewer = new StatsViewer();
        statsViewer.create();
    }

    @Override
    public void render () {
        statsViewer.render();
    }

    @Override
    public void dispose () {
        statsViewer.dispose();
//        oswebbleGame.dispose();
//        GameManager.getInstance().Dispose();
    }
}

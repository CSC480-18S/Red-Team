package stats;

import com.badlogic.gdx.Game;
import stats.GUI.StatsScreen;

public class StatsViewer extends Game {
    public StatsScreen screen;
    @Override
    public void create() {
        screen = new StatsScreen(this);
        setScreen(screen);
    }
}

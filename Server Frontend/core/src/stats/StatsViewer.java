package stats;

import com.badlogic.gdx.Game;
import stats.GUI.StatsScreen;

import java.io.UnsupportedEncodingException;
import java.util.Scanner;

public class StatsViewer extends Game {
    public StatsScreen screen;
    @Override
    public void create() {
        StatsConnection stats = new StatsConnection();
        try (Scanner scanner = new Scanner(stats.getUserStats("Gage"))) {
            String responseBody = scanner.useDelimiter("\\A").next();
            System.out.println(responseBody);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        screen = new StatsScreen(this);
        setScreen(screen);
    }
}

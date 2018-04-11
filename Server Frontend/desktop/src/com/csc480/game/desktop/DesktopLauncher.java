package com.csc480.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.csc480.game.Engine.GameManager;
import com.csc480.game.GameAdapter;
import stats.StatsAdapter;



public class DesktopLauncher {
	public static void main (String[] arg) {
		boolean runStatsFlag = false;
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.fullscreen = true;
		config.height = 1080;
		config.width = 1920;
		if(arg.length == 0) {
			new LwjglApplication(new GameAdapter(), config);
		}else {
			for(int i = 0; i < arg.length; i++) {
				if (arg[i].compareTo("-stats") == 0)
					runStatsFlag = true;
				if (arg[i].compareTo("-ai") == 0)
					GameManager.produceAI = true;
			}
		}
		if(runStatsFlag)
			new LwjglApplication(new StatsAdapter(), config);
		else
			new LwjglApplication(new GameAdapter(), config);

	}
}

package com.csc480.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.csc480.game.GameAdapter;
import com.csc480.stats.StatsAdapter;



public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		//config.fullscreen = true;
		config.height = 1080;
		config.width = 1920;
		if(arg.length == 0)
			new LwjglApplication(new GameAdapter(), config);
		else if(arg[0].compareTo("-stats") == 0)
			new LwjglApplication(new StatsAdapter(),config);


	}
}

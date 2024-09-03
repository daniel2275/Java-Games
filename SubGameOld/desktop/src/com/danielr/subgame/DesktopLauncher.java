package com.danielr.subgame;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import static utilz.Constants.Game.WORLD_HEIGHT;
import static utilz.Constants.Game.WORLD_WIDTH;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("SubGame");
		config.setWindowedMode((int) WORLD_WIDTH, (int) WORLD_HEIGHT);
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setResizable(false);

		new Lwjgl3Application(new SubGame(), config);
	}
}

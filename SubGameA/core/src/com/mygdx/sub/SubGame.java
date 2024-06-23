package com.mygdx.sub;

import UI.game.GameScreen;
import UI.menu.MenuScreen;
import UI.options.Options;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;

public class SubGame extends Game {

	public static boolean pause = false;
	private MenuScreen menuScreen;

	private Options options;
	private GameScreen gameScreen;


	@Override
	public void create() {
//		if(Gdx.graphics.supportsDisplayModeChange()) {
//			Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
//			Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
//			Gdx.graphics.setFullscreenMode(displayMode);
//		}
//		camera = new OrthographicCamera();
		//camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
//
		float screenWidth = WORLD_WIDTH; // set your desired screen width
		float screenHeight = WORLD_HEIGHT; // set your desired screen height


		// Set the screen resolution
		Gdx.graphics.setWindowedMode((int) screenWidth, (int) screenHeight);

		menuScreen = new MenuScreen(this);

		options = new Options(this);
		gameScreen = new GameScreen(Gdx.graphics.getDeltaTime(), this);
		setScreen(menuScreen);


		// set up mouse cross-hair
		Pixmap cursorTexture = new Pixmap(Gdx.files.internal("CrossHair.png"));
		int xHotSpot = cursorTexture.getWidth() /2;
		int yHotSpot = cursorTexture.getHeight() /2 ;
		Cursor customCursor = Gdx.graphics.newCursor(cursorTexture, xHotSpot, yHotSpot);
		Gdx.graphics.setCursor(customCursor);
	}

	@Override
	public void dispose() {
		menuScreen.getMenuStage().dispose();
	}

	public Options getOptions() {
		return options;
	}

	public GameScreen gameScreen() {
		return gameScreen;
	}

	public MenuScreen getMenuRenderer() {
		return menuScreen;
	}


}
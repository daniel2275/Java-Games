package com.mygdx.sub;

import UI.game.GameScreen;
import UI.gameover.GameOver;
import UI.menu.MenuScreen;
import UI.options.Options;
import UI.upgrades.UpgradeStore;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;

import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;

public class SubGame extends Game {

	public static boolean pause = false;
	private MenuScreen menuScreen;

	private Options options;
	private GameScreen gameScreen;
	private GameOver gameOver;
	private UpgradeStore upgradeStore;


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
		upgradeStore = new UpgradeStore(gameScreen);
		gameOver = new GameOver(this);
		setScreen(menuScreen);
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

	public UpgradeStore getUpgradeStore() {
		return upgradeStore;
	}
}
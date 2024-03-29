package com.mygdx.sub;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import gamestates.*;

public class SubGame extends Game {

	public static boolean pause = false;
	private MenuRenderer menuRenderer;

	private Options options;
	private GamePlayScreen gamePlayScreen;
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
//		camera.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2, 0);
//

		menuRenderer = new MenuRenderer(this);


		options = new Options(this);
		gamePlayScreen = new GamePlayScreen(Gdx.graphics.getDeltaTime(), this);
		upgradeStore = new UpgradeStore(gamePlayScreen);
		gameOver = new GameOver(this);
		setScreen(new MenuRenderer(this));
	}

	@Override
	public void dispose() {
		menuRenderer.getMenuStage().dispose();
	}

	public Options getOptions() {
		return options;
	}

	public GamePlayScreen gamePlayScreen() {
		return gamePlayScreen;
	}


}
package io.github.daniel2275.subgame;

import UI.game.FontManager;
import UI.game.GameScreen;
import UI.menu.MenuScreen;
import UI.options.Options;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;
import utilities.SoundManager;

import static utilities.Constants.Game.VIRTUAL_HEIGHT;
import static utilities.Constants.Game.VIRTUAL_WIDTH;

public class SubGame extends Game {

	public static boolean pause = false;
	private MenuScreen menuScreen;

	private Options options;
	private GameScreen gameScreen;

    @Override
	public void create() {
        FontManager.loadFont(24);
//		if(Gdx.graphics.supportsDisplayModeChange()) {
//			Graphics.Monitor currMonitor = Gdx.graphics.getMonitor();
//			Graphics.DisplayMode displayMode = Gdx.graphics.getDisplayMode(currMonitor);
//			Gdx.graphics.setFullscreenMode(displayMode);
//		}
		float screenWidth = VIRTUAL_WIDTH; // set your desired screen width
		float screenHeight = VIRTUAL_HEIGHT; // set your desired screen height

		// Set the screen resolution
		Gdx.graphics.setWindowedMode((int) screenWidth, (int) screenHeight);

		menuScreen = new MenuScreen(this);

        SoundManager.getInstance(this);
		options = new Options(this);
		gameScreen = new GameScreen(Gdx.graphics.getDeltaTime(), this);
		setScreen(menuScreen);

		// set up mouse cross-hair
        if (Gdx.app.getType() != com.badlogic.gdx.Application.ApplicationType.Android) {
            Pixmap cursorTexture = new Pixmap(Gdx.files.internal("crosshair.png"));
            int xHotSpot = cursorTexture.getWidth() / 2;
            int yHotSpot = cursorTexture.getHeight() / 2;
            Cursor customCursor = Gdx.graphics.newCursor(cursorTexture, xHotSpot, yHotSpot);
            Gdx.graphics.setCursor(customCursor);
        }
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

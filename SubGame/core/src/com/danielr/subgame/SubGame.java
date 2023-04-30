package com.danielr.subgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gamestates.*;

import static utilz.Constants.Game.WORLD_HEIGHT;
import static utilz.Constants.Game.WORLD_WIDTH;

public class SubGame extends ApplicationAdapter  {

	private Sprite background;
	private Sprite pauseSprite;
	public static OrthographicCamera camera;
	public static Viewport viewport;
	public static ShapeRenderer shapeRendered;
	public static SpriteBatch batch;

	public static boolean pause = false;
	private Playing playing;
	private Menu menu;
	private Label scoreLabel;
	private Label scoreLabel1;
	private Label scoreLabel2;
	private Label scoreLabel3;
	private Stage uiStage;

	private GameOver gameOver;

	public static UpgradeStore upgradeStore;

	private float stateTime;
//	BufferedImage[] lvls;
//	private Level[] level;

//  public static InputMultiplexer multiplexer = new InputMultiplexer();

	@Override
	public void create() {
		stateTime = Gdx.graphics.getDeltaTime();
		Skin skin = new Skin(Gdx.files.internal("clean-crispy/skin/clean-crispy-ui.json"));
		// D:\Projects\Java-Games\SubGame\assets\clean-crispy\skin
		// set up mouse cross-hair
		Pixmap cursorTexture = new Pixmap(Gdx.files.internal("CrossHair.png"));
		int xHotSpot = cursorTexture.getWidth() /2;
		int yHotSpot = cursorTexture.getHeight() /2 ;
		Cursor customCursor = Gdx.graphics.newCursor(cursorTexture, xHotSpot, yHotSpot);
		Gdx.graphics.setCursor(customCursor);

		//set up UI display
//		BitmapFont font = loadFont("fonts/BwnsnwBitmap-2O9d.ttf");

		scoreLabel = new Label("Enemies remaining:", skin);
		scoreLabel1 = new Label("Score:", skin);
		scoreLabel2 = new Label("Level:", skin);
		scoreLabel3 = new Label("Health:", skin);

//		scoreLabel = new Label("Enemies remaining:", new Label.LabelStyle(font, Color.BLACK));
//		scoreLabel1 = new Label("Score:", new Label.LabelStyle(font, Color.BLACK));
//		scoreLabel2 = new Label("Level:", new Label.LabelStyle(font, Color.BLACK));

		scoreLabel.setPosition(5, WORLD_HEIGHT - 20);
		scoreLabel1.setPosition(5, WORLD_HEIGHT - 35);
		scoreLabel2.setPosition(5, WORLD_HEIGHT - 50);
		scoreLabel3.setPosition(5, WORLD_HEIGHT - 65);
		uiStage = new Stage();
		uiStage.addActor(scoreLabel);
		uiStage.addActor(scoreLabel1);
		uiStage.addActor(scoreLabel2);
		uiStage.addActor(scoreLabel3);
//		LoadSave.loadBinary();

//		lvls = LoadSave.GetAllLevels();
//		level = new Level[lvls.length];
//
//		for (int i = 0; i < lvls.length; i++) {
//			level[i] = new Level(lvls[i]);
//		}

		batch = new SpriteBatch();
		background = new Sprite(new Texture(Gdx.files.internal("sea_background.png")));
		background.setPosition(0, 0);
		background.setSize(WORLD_WIDTH, WORLD_HEIGHT);

		pauseSprite = new Sprite(new Texture(Gdx.files.internal("paused.png")));
		pauseSprite.setPosition(WORLD_WIDTH/2 - 100f, 500);
		pauseSprite.setSize(200f, 80f);

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		camera = new OrthographicCamera();

		viewport = new ExtendViewport(WORLD_WIDTH * aspectRatio, WORLD_HEIGHT, camera);
		viewport.apply();

		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

		menu =  new Menu(this);

		playing = new Playing(stateTime);

		shapeRendered = new ShapeRenderer();

		shapeRendered.setAutoShapeType(true);

		upgradeStore = new UpgradeStore(playing);

		gameOver = new GameOver(this);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
	}


	@Override
	public void render() {

		ScreenUtils.clear(0, 0, 0.2f, 1);
		shapeRendered.setProjectionMatrix(camera.combined);
		camera.update();

		batch.begin();
		background.draw(batch);
		if (pause) {
			pauseSprite.draw(batch);
//			Gamestate.state = Gamestate.PAUSE;
		}
		batch.end();

		switch (Gamestate.state) {
			case PLAYING:{
				playing.update();
				scoreLabel.setText("Enemies remaining:" + playing.getEnemyManager().getListOfEnemies().size());
				scoreLabel1.setText("Score:" + playing.getPlayer().getPlayerScore());
				scoreLabel2.setText("Level:" + playing.getLevelManager().getLevel().getTotalLevels());
				scoreLabel3.setText("Health:" + playing.getPlayer().getPlayerHealth());
				uiStage.draw();
				upgradeStore.setPlayerScore(playing.getPlayer().getPlayerScore());
			}break;
			case MENU:{
//				pause = true;
				menu.update();
//				playing.update();
			}
			break;
			case STORE:{
//				pause = true;
				upgradeStore.render(stateTime);
			}
			break;
			case GAME_OVER:{
				System.out.println("GAME_OVER");
				pause = true;
				gameOver.render(stateTime);
//				Gamestate.state = Gamestate.MENU;
			}break;
			case OPTIONS:{
				System.out.println("Options");
			}break;
//			case PAUSE:{
//				System.out.println("pause");
//				//pause = true;
//			}
//			break;
			case QUIT:{
				System.out.println("Quit");
			}break;
		}
	}

	@Override
	public void pause(){
		System.out.println("pause");
		pause = true;
	}

	public void resume() {
//		System.out.println("resume");
//		pause = false;
	}

	@Override
	public void dispose () {
		playing.getPlayer().getBatch().dispose();
		shapeRendered.dispose();
	}

	public Playing getPlaying() {
		return playing;
	}

//	private BitmapFont loadFont(String fontName){
//		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
//		FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
//		parameter.size = 16; // font size
//		BitmapFont font = generator.generateFont(parameter); // generate the BitmapFont
//		generator.dispose(); // dispose the generator when you're done
//		return font;
//	}

}



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

import static utilz.Constants.Game.*;

public class SubGame extends ApplicationAdapter  {

	private Sprite background;
	private Sprite underSea;
	private Sprite skyLine;
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
	private float stateTime;
	public static UpgradeStore upgradeStore;
	private Options options;


	@Override
	public void create() {
		stateTime = Gdx.graphics.getDeltaTime();
		Skin skin = new Skin(Gdx.files.internal("clean-crispy/skin/clean-crispy-ui.json"));
		// set up mouse cross-hair
		Pixmap cursorTexture = new Pixmap(Gdx.files.internal("CrossHair.png"));
		int xHotSpot = cursorTexture.getWidth() /2;
		int yHotSpot = cursorTexture.getHeight() /2 ;
		Cursor customCursor = Gdx.graphics.newCursor(cursorTexture, xHotSpot, yHotSpot);
		Gdx.graphics.setCursor(customCursor);

		//set up UI display
		scoreLabel = new Label("Enemies remaining:", skin);
		scoreLabel1 = new Label("Score:", skin);
		scoreLabel2 = new Label("Level:", skin);
		scoreLabel3 = new Label("Health:", skin);

		scoreLabel.setPosition(5, WORLD_HEIGHT - 20);
		scoreLabel1.setPosition(5, WORLD_HEIGHT - 35);
		scoreLabel2.setPosition(5, WORLD_HEIGHT - 50);
		scoreLabel3.setPosition(5, WORLD_HEIGHT - 65);
		uiStage = new Stage();
		uiStage.addActor(scoreLabel);
		uiStage.addActor(scoreLabel1);
		uiStage.addActor(scoreLabel2);
		uiStage.addActor(scoreLabel3);

		batch = new SpriteBatch();
		background = new Sprite(new Texture(Gdx.files.internal("sea_background.png")));
		background.setPosition(0, 0);
		background.setSize(WORLD_WIDTH, WORLD_HEIGHT);

		//background images
		skyLine =  new Sprite(new Texture(Gdx.files.internal("skyline.png")));
		skyLine.setPosition(0 , WORLD_HEIGHT );
		skyLine.setSize(1009, 450);

		underSea =  new Sprite(new Texture(Gdx.files.internal("sea1.png")));
		underSea.setPosition(0,0);
		underSea.setSize(1009, 450);
		//background images

		pauseSprite = new Sprite(new Texture(Gdx.files.internal("paused.png")));
		pauseSprite.setPosition(WORLD_WIDTH/2 - 100f, 500);
		pauseSprite.setSize(200f, 80f);

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		camera = new OrthographicCamera();

		viewport = new ExtendViewport(WORLD_WIDTH * aspectRatio, WORLD_HEIGHT, camera);
		viewport.apply();

		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

		menu =  new Menu(this);
		playing = new Playing(stateTime, this);
		shapeRendered = new ShapeRenderer();
		shapeRendered.setAutoShapeType(true);
		options = new Options(this);
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
//		background.draw(batch);
		underSea.draw(batch);
		underSea.setPosition(0, 0);
		underSea.setSize(WORLD_WIDTH,WORLD_HEIGHT - SKY_SIZE);
		if (pause) {
			pauseSprite.draw(batch);
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
				menu.update();
			}
			break;
			case STORE:{
				upgradeStore.render(stateTime);
			}
			break;
			case GAME_OVER:{
				System.out.println("GAME_OVER");
				pause = true;
				gameOver.render(stateTime);
			}break;
			case OPTIONS:{
				options.render(stateTime);
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
		System.out.println("resume");
	}

	@Override
	public void dispose () {
		System.out.println("dispose");
		getPlaying().getObjectManager().exit();
		getPlaying().getEnemyManager().exit();
		getPlaying().getPlayer().exit();
		shapeRendered.dispose();
		batch.dispose();
		super.dispose();
	}

	public Playing getPlaying() {
		return playing;
	}

	public Options getOptions() {
		return options;
	}
}



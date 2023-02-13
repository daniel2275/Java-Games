package com.danielr.subgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import gamestates.Playing;

import static utilz.Constants.Game.WORLD_HEIGHT;
import static utilz.Constants.Game.WORLD_WIDTH;

public class SubGame extends ApplicationAdapter {

	private Sprite background;
	private Sprite pauseSprite;

	public static OrthographicCamera camera;
	public static Viewport viewport;
	public static ShapeRenderer shapeRendered;
	public static SpriteBatch batch;

	public static boolean pause = false;

//	public static float pauseTime = 0;
//	public static long startTime = 0;

	private Playing playing;


//	BufferedImage[] lvls;
//	private Level[] level;

	@Override
	public void create() {

		batch = new SpriteBatch();
//		LoadSave.loadBinary();

//		lvls = LoadSave.GetAllLevels();
//		level = new Level[lvls.length];
//
//		for (int i = 0; i < lvls.length; i++) {
//			level[i] = new Level(lvls[i]);
//		}

		background = new Sprite(new Texture(Gdx.files.internal("sea_background.png")));
		background.setPosition(0, 0);
		background.setSize(800f, 600f);

		pauseSprite = new Sprite(new Texture(Gdx.files.internal("paused.png")));
		pauseSprite.setPosition(WORLD_WIDTH/2 - 100f, 500);
		pauseSprite.setSize(200f, 80f);

		float aspectRatio = (float) Gdx.graphics.getHeight() / (float) Gdx.graphics.getWidth();
		camera = new OrthographicCamera();

		viewport = new ExtendViewport(WORLD_WIDTH * aspectRatio, WORLD_HEIGHT, camera);
		viewport.apply();

		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);

		playing = new Playing();

		shapeRendered = new ShapeRenderer();

		shapeRendered.setAutoShapeType(true);
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height);
		camera.position.set(WORLD_WIDTH / 2, WORLD_HEIGHT / 2, 0);
	}


	@Override
	public void render() {
		ScreenUtils.clear(0, 0, 0.2f, 1);

			batch.begin();
			background.draw(batch);
			if (pause) {
				pauseSprite.draw(batch);
			}
			batch.end();

			camera.update();

			shapeRendered.setProjectionMatrix(camera.combined);

			playing.update();

	}

	@Override
	public void dispose () {
//		enemy1.getBatch().dispose();
//		enemy1.getCurrentFrame().getTexture().dispose();
//		enemy2.getBatch().dispose();
//		enemy2.getCurrentFrame().getTexture().dispose();
//		enemy3.getBatch().dispose();
//		enemy3.getCurrentFrame().getTexture().dispose();
	}



}



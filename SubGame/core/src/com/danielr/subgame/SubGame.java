package com.danielr.subgame;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import entities.Enemy;
import gamestates.Playing;

import java.util.ArrayList;
import java.util.Iterator;

import static utilz.Constants.Game.WORLD_HEIGHT;
import static utilz.Constants.Game.WORLD_WIDTH;
import static utilz.LoadSave.iterateEnemies;

public class SubGame extends ApplicationAdapter {


	public static OrthographicCamera camera;
	public static Viewport viewport;
	public static ShapeRenderer shapeRendered;
	public static SpriteBatch batch;

//	private Player player;
	private Playing playing;


//	private long startTime = TimeUtils.nanoTime();

	public ArrayList<Enemy> listOfEnemies = new ArrayList<>();

	private Enemy enemy1;
	private Enemy enemy2;
	private Enemy enemy3;



//	BufferedImage[] lvls;
//	private Level[] level;

	@Override
	public void create () {

		batch = new SpriteBatch();
//		LoadSave.loadBinary();

//		lvls = LoadSave.GetAllLevels();
//		level = new Level[lvls.length];
//
//		for (int i = 0; i < lvls.length; i++) {
//			level[i] = new Level(lvls[i]);
//		}



		float aspectRatio = (float) Gdx.graphics.getHeight()/(float)Gdx.graphics.getWidth();
		camera =  new OrthographicCamera();

//		player = new Player(camera);

		enemy1 = new Enemy(2,-65,-1,"tanker.png", 15f);
		enemy2 = new Enemy(20,865,1,"tanker.png", 15f);
		enemy3 = new Enemy(10,865,1,"tanker2.png",25f);

		listOfEnemies.add(enemy1);
		listOfEnemies.add(enemy2);
		listOfEnemies.add(enemy3);


		viewport = new ExtendViewport(WORLD_WIDTH * aspectRatio,WORLD_HEIGHT, camera);
		viewport.apply();

		camera.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);

		playing = new Playing(this, listOfEnemies);

		shapeRendered = new ShapeRenderer();

		shapeRendered.setAutoShapeType(true);

	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width,height);
		camera.position.set(WORLD_WIDTH/2,WORLD_HEIGHT/2,0);
	}

	@Override
	public void render () {
		ScreenUtils.clear(0, 0, 0.2f, 1);

		camera.update();

		shapeRendered.setProjectionMatrix(camera.combined);

		playing.update();

		// update enemies
		Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
		iterateEnemies(enemyIterator);
	}


	@Override
	public void dispose () {
		enemy1.getBatch().dispose();
		enemy1.getCurrentFrame().getTexture().dispose();
		enemy2.getBatch().dispose();
		enemy2.getCurrentFrame().getTexture().dispose();
		enemy3.getBatch().dispose();
		enemy3.getCurrentFrame().getTexture().dispose();
	}


	public OrthographicCamera getCamera() {
		return camera;
	}
}



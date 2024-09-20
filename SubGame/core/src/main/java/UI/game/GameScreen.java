package UI.game;

import Components.Pausable;
import UI.gameover.GameOver;
import UI.upgrades.UpgradeStore;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import entities.enemies.EnemyManager;
import entities.player.Player;
import io.github.daniel2275.subgame.SubGame;
import levels.LevelManager;
import objects.ObjectManager;

import static utilities.Constants.Game.VIRTUAL_HEIGHT;
import static utilities.Constants.Game.VIRTUAL_WIDTH;
import static utilities.Constants.UIConstants.FONT_GAME_SIZE;

public class GameScreen implements Screen {
    private Player player;
    private ObjectManager objectManager;
    private EnemyManager enemyManager;
    private LevelManager levelManager;
    private float stateTime;
    private SubGame subGame;
    private UpgradeStore upgradeStore;
    private GameStageManager gameStageManager;
    private Stage gmStage;
    private InputHandler inputHandler;
    public OrthographicCamera camera;
    public Viewport viewport;
    public SpriteBatch batch;
    private boolean paused = false;
    private GameOver gameOver;
    private Actor crossHairActor;
//    public Image crosshairImage;
//    private Texture crosshairTexture;

    public GameScreen(float delta, SubGame subGame) {
        this.subGame = subGame;

        stateTime = delta;
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT, camera);
        camera.setToOrtho(false, VIRTUAL_WIDTH, VIRTUAL_HEIGHT);

        this.gameStageManager = new GameStageManager(this);
        this.gameOver = new GameOver(this);

        gmStage = gameStageManager.build();

        initClasses();

        batch = new SpriteBatch();

        gmStage.addActor(player.getPlayerActor());
        player.getPlayerActor().toBack();

    }

    public void updateCrosshairPosition(int screenX, int screenY) {
        Vector2 stageCoords = gmStage.screenToStageCoordinates(new Vector2(screenX, screenY));
        crossHairActor = gameStageManager.getAndroidCrossHairActor();
        crossHairActor.setPosition(stageCoords.x - crossHairActor.getWidth() / 2, stageCoords.y - crossHairActor.getHeight() /  2 );
    }

    @Override
    public void show() {
        // Set up InputMultiplexer
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gmStage);
        inputMultiplexer.addProcessor(inputHandler);

        Gdx.input.setInputProcessor(inputMultiplexer);

        // Ensure the viewport is correctly initialized
        gmStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        // Optionally, set the camera's initial position or other properties
        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();

        //adjust background image
        gameStageManager.getSkyLine().setPlayerX(player.getPlayerActor().getX());
        gameStageManager.getUndersea().setPlayerX(player.getPlayerActor().getX());

        gameStageManager.getSkyLine().toBack();
        gameStageManager.getSkyLine().toBack();
    }

    private void initClasses() {
        player = new Player(this, stateTime);

        objectManager = new ObjectManager(this);
        enemyManager = new EnemyManager(this);
        levelManager = new LevelManager(this);
        upgradeStore = new UpgradeStore(this);
        inputHandler = new InputHandler(this, gmStage);
    }

    public Player getPlayer() {
        return player;
    }

    public void update() {
        if (!paused) {
            player.update();

            gameStageManager.getScoreLabel().setText("Enemies remaining:" + getEnemyManager().getListOfEnemies().size());
            gameStageManager.getScoreLabel1().setText("Score:" + getPlayer().getPlayerScore());
            gameStageManager.getScoreLabel2().setText("Level:" + getLevelManager().getLevel().getTotalLevels());
            gameStageManager.getScoreLabel3().setText("Health:" + (int) (getPlayer().getPlayerActor().getCurrentHealth()));

            gameStageManager.getScoreLabel().setFontScale(FONT_GAME_SIZE);
            gameStageManager.getScoreLabel1().setFontScale(FONT_GAME_SIZE);
            gameStageManager.getScoreLabel2().setFontScale(FONT_GAME_SIZE);
            gameStageManager.getScoreLabel3().setFontScale(FONT_GAME_SIZE);

            upgradeStore.setPlayerScore(getPlayer().getPlayerScore());

            objectManager.update();
            enemyManager.update(player, objectManager);
            levelManager.update();

            // Update and draw the touchpad
            inputHandler.update();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 51f / 255f, 102f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.update();
        batch.setProjectionMatrix(camera.combined);

        gmStage.act(Gdx.graphics.getDeltaTime());
        gmStage.draw();

        update();

//        if (player.getPlayerActor().getY() > VIRTUAL_HEIGHT - SKY_SIZE - 55) {
//            player.getPlayerActor().toFront();
//            //player.getPlayerActor().setSurfacingAnimation(true);
//        } else {
//            player.getPlayerActor().toBack();
//        }

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        camera.update();
    }

    @Override
    public void pause() {
        paused = true;
        // Pause your actors
        objectManager.setPaused(true);
        for (Actor actor : gmStage.getActors()) {
            if (actor instanceof Pausable) {
                ((Pausable) actor).setPaused(true);
            }
        }
    }

    @Override
    public void resume() {
        paused = false;
        // Pause your actors
        objectManager.setPaused(false);
        for (Actor actor : gmStage.getActors()) {
            if (actor instanceof Pausable) {
                ((Pausable) actor).setPaused(false);
            }
        }
        //getGameUIManager().getSkyLine().toBack();
        //getGameUIManager().getUndersea().toBack();
    }

    @Override
    public void hide() {

    }



    public void reset() {
        objectManager.reset();
        levelManager.reset();
        enemyManager.reset();
        player.reset();
    }

    public void resetUpgrades() {
        upgradeStore.setDefaults();
        upgradeStore.getUpgradeStageManager().resetUpgrades();
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }

    public LevelManager getLevelManager() {
        return levelManager;
    }

    public SubGame getSubGame() {
        return subGame;
    }

    public GameStageManager getGameStage() {
        return this.gameStageManager;
    }

    public Stage getGmStage() {
        return gmStage;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void updateViewport(int width, int height) {
        gameStageManager.getStage().getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        gameStageManager.dispose();
        batch.dispose();
        inputHandler.dispose();
    }

    public Viewport getViewport() {
        return viewport;
    }

    public GameStageManager getGameUIManager() {
        return gameStageManager;
    }

    public boolean isPaused() {
        return paused;
    }

    public UpgradeStore getUpgradeStore() {
        return upgradeStore;
    }

    public GameOver getGameOver() {
        return gameOver;
    }

    public Actor getCrossHairActor() {
        return crossHairActor;
    }
}


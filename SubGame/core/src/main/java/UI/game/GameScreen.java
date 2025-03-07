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

import static utilities.Settings.Game.VIRTUAL_HEIGHT;
import static utilities.Settings.Game.VIRTUAL_WIDTH;
import static utilities.Settings.UIConstants.FONT_GAME_SIZE;

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
    private boolean zIndexChanged = true;
    // If an actor is added or removed dynamically, set the flag to true


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
        //player.getPlayerActor().toBack();
        player.getPlayerActor().setZIndex(0);

    }

    public void updateCrosshairPosition(int screenX, int screenY) {
        Vector2 stageCoords = gmStage.screenToStageCoordinates(new Vector2(screenX, screenY));
        crossHairActor = gameStageManager.getAndroidCrossHairActor();
        crossHairActor.setPosition(stageCoords.x - crossHairActor.getWidth() / 2, stageCoords.y - crossHairActor.getHeight() / 2);
    }

    @Override
    public void show() {
        batch.setProjectionMatrix(camera.combined);
        // Set up InputMultiplexer
        InputMultiplexer inputMultiplexer = new InputMultiplexer();
        inputMultiplexer.addProcessor(gmStage);
        inputMultiplexer.addProcessor(inputHandler);

        Gdx.input.setInputProcessor(inputMultiplexer);

        gmStage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        camera.position.set(VIRTUAL_WIDTH / 2, VIRTUAL_HEIGHT / 2, 0);
        camera.update();
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

//            gameStageManager.getSkyLine().setZIndex(0);
//            gameStageManager.getUndersea().setZIndex(2);


            //System.out.println("Skyline : " + gameStageManager.getSkyLine().getZIndex());
            //System.out.println("Undersea : " + gameStageManager.getUndersea().getZIndex());
//            System.out.println("Player : " + getPlayer().getPlayerActor().getZIndex());
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 105f / 255f, 148f / 255f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (zIndexChanged) {
            maintainDrawOrder();
            zIndexChanged = false; // Reset flag after adjusting z-index
        }

        camera.update();
        batch.setProjectionMatrix(camera.combined);

        gmStage.act(delta);
        gmStage.draw();

        update();

    }

    private void maintainDrawOrder() {
        gameStageManager.getSkyLine().setZIndex(1);
        getPlayer().getPlayerActor().setZIndex(2);


        enemyManager.getListOfEnemies().forEach(enemy -> {
            enemy.getEnemyActor().setZIndex(2);
        });

        gameStageManager.getUndersea().setZIndex(1000);

        if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) {
            inputHandler.getTouchpadController().getLeftTouchpad().toFront();
            inputHandler.getTouchpadController().getRightTouchpad().toFront();

        }
//        System.out.println("Order-----------------------------");
//        System.out.println("Skyline : " + gameStageManager.getSkyLine().getZIndex());
//
//        enemyManager.getListOfEnemies().forEach(enemy -> {
//            System.out.print("Z index " + enemy.getEnemyActor().getZIndex() + " ");
//            System.out.println(enemy.getEnemyActor().getName());
//        });
//
//
//        System.out.println("Undersea : " + gameStageManager.getUndersea().getZIndex());

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
        gameStageManager.getUndersea().setPaused(true);
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
        gameStageManager.getUndersea().setPaused(false);
        objectManager.setPaused(false);
        for (Actor actor : gmStage.getActors()) {
            if (actor instanceof Pausable) {
                ((Pausable) actor).setPaused(false);
            }
        }
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


    public void onActorAddedOrRemoved() {
        zIndexChanged = true;
    }

}


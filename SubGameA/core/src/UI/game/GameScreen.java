package UI.game;

import Components.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.sub.SubGame;
import entities.enemies.Enemy;
import entities.enemies.EnemyManager;
import entities.player.Player;
import UI.upgrades.UpgradeStore;
import levels.LevelManager;
import objects.ObjectManager;

import java.util.Optional;

import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;


public class GameScreen implements Screen {
    private Player player;
    private ObjectManager objectManager;
    private EnemyManager enemyManager;
    private LevelManager levelManager;
    private float stateTime;
    private SubGame subGame;
    private UpgradeStore upgrades;
    private GameStageManager gameStageManager;
    private Stage gmStage;
    private InputHandler inputHandler;
    public OrthographicCamera camera;
    public Viewport viewport;
    public SpriteBatch batch;
    private boolean paused = false;
    //private ShapeRenderer shapeRenderer;

    public GameScreen(float delta, SubGame subGame) {
        this.subGame = subGame;
        this.gameStageManager = new GameStageManager(subGame);

        initClasses();
        stateTime = delta;

        camera = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        //shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
        gmStage = gameStageManager.build();
        gmStage.addActor(player.getPlayerActor());
    }

    @Override
    public void show() {
        gmStage.getViewport().setWorldSize(WORLD_WIDTH, WORLD_HEIGHT);
        Gdx.input.setInputProcessor(inputHandler);
    }

    private void initClasses() {
        player = new Player(this, stateTime);
        inputHandler = new InputHandler(this);
        objectManager = new ObjectManager(this);
        enemyManager = new EnemyManager(this);
        levelManager = new LevelManager(this);
        upgrades = new UpgradeStore(this);

        //player.getPlayerActor().setPosition(player.getPlayerMovement().getSPAWN_X(),player.getPlayerMovement().getSPAWN_Y());
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


            upgrades.setPlayerScore(getPlayer().getPlayerScore());

            //adjust background image
            gameStageManager.getSkyLine().setPlayerX(player.getPlayerActor().getX());
            gameStageManager.getUndersea().setPlayerX(player.getPlayerActor().getX());


            objectManager.update();
            enemyManager.update(player, objectManager);
            levelManager.update();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gmStage.act(Gdx.graphics.getDeltaTime());
        gmStage.draw();

        boolean isTouching = inputHandler.isTouching();
        if (isTouching) {
            Vector2 clickPosition = inputHandler.getClickPosition();
            Vector2 playerPosition = new Vector2(getPlayer().getPlayerActor().getX(), getPlayer().getPlayerActor().getY());
            getPlayer().getPlayerMovement().calculateDirection(clickPosition, playerPosition);
        }

        update();

    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(viewport.getCamera().combined);
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
        getGameUIManager().getSkyLine().toBack();
        getGameUIManager().getUndersea().toBack();
    }

    @Override
    public void hide() {

    }

    public boolean checkCollision(AnimatedActor actor, float damage) {
        Optional<Enemy> deadEnemy = enemyManager.getListOfEnemies().stream()
                .filter(enemy -> {
                    boolean isHit = enemy.checkHit(actor, damage);
                    if (isHit && enemy.getEnemyActor().getCurrentHealth() <= 0) {
                        enemy.setDying(true);
                    }
                    return isHit;
                })
                .findFirst();

        return deadEnemy.isPresent();
    }

    public void reset() {
        System.out.println("Playing reset");
        objectManager.reset();
        levelManager.reset();
        player.reset();
        upgrades.resetUpgrades();
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

//    public ShapeRenderer getShapeRenderer()
//    {
//        return shapeRenderer;
//    }

    @Override
    public void dispose() {
        gameStageManager.dispose();
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
}


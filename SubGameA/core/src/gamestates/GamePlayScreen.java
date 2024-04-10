package gamestates;

import Components.AnimatedActor;
import Components.InputHandler;
import UI.GameUIManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.sub.SubGame;
import entities.enemies.Enemy;
import entities.enemies.EnemyManager;
import entities.player.Player;
import levels.LevelManager;
import objects.ObjectManager;

import java.util.Optional;

import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;


public class GamePlayScreen implements Screen {
    private Player player;
    private ObjectManager objectManager;
    private EnemyManager enemyManager;
    private LevelManager levelManager;
    private float stateTime;
    private SubGame subGame;
    private UpgradeStore upgrades;
    private GameUIManager gameUIManager;
    private Stage gmStage;
    private InputHandler inputHandler;
    public OrthographicCamera camera;
    public Viewport viewport;
    public SpriteBatch batch;
    private ShapeRenderer shapeRenderer;

    public GamePlayScreen(float delta, SubGame subGame) {
        this.subGame = subGame;
        this.gameUIManager = new GameUIManager(subGame);

        initClasses();

        stateTime = delta;
        camera = new OrthographicCamera();

        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        shapeRenderer = new ShapeRenderer();
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        gmStage = gameUIManager.build();
        gmStage.getViewport().setWorldSize(WORLD_WIDTH, WORLD_HEIGHT );
        Gdx.input.setInputProcessor(inputHandler);
        gmStage.addActor(player.getPlayerActor());
    }

    private void initClasses() {
        player = new Player(this, stateTime);
        inputHandler = new InputHandler(this);
        objectManager =  new ObjectManager(this);
        enemyManager = new EnemyManager(this);
        levelManager = new LevelManager(this);
        upgrades = new UpgradeStore(this);

        //player.getPlayerActor().setPosition(player.getPlayerMovement().getSPAWN_X(),player.getPlayerMovement().getSPAWN_Y());
    }

    public Player getPlayer() {
        return player;
    }

    public void update() {
        player.update();

        gameUIManager.getScoreLabel().setText("Enemies remaining:" + getEnemyManager().getListOfEnemies().size());
        gameUIManager.getScoreLabel1().setText("Score:" + getPlayer().getPlayerScore());
        gameUIManager.getScoreLabel2().setText("Level:" + getLevelManager().getLevel().getTotalLevels());
        gameUIManager.getScoreLabel3().setText("Health:" + (int) (getPlayer().getPlayerActor().getCurrentHealth()));
        upgrades.setPlayerScore(getPlayer().getPlayerScore());

        objectManager.update();
        enemyManager.update(player, objectManager);
        levelManager.update();
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

    }

    @Override
    public void resume() {

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

    public GameUIManager getGameStage() {
        return this.gameUIManager;
    }

    public Stage getGmStage(){
        return gmStage;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void updateViewport(int width, int height) {
        gameUIManager.getStage().getViewport().update(width, height, true);
    }

    public ShapeRenderer getShapeRenderer()
    {
        return shapeRenderer;
    }

    @Override
    public void dispose(){
        gameUIManager.dispose();
    }

    public Viewport getViewport() {
        return viewport;
    }


}


package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import levels.LevelManager;
import objects.ObjectManager;

import java.util.Optional;

import static com.danielr.subgame.SubGame.*;

public class Playing implements InputProcessor {
    private Player player;
    private ObjectManager objectManager;
    private EnemyManager enemyManager;

    private LevelManager levelManager;

    int x,y;

    private float stateTime;


    public Playing(float delta) {
        initClasses();
        stateTime = delta;
    }

    private void initClasses() {
        player = new Player(this, stateTime);
        objectManager =  new ObjectManager(this);
        enemyManager = new EnemyManager(this);
        levelManager = new LevelManager(this);
//        upgrades = new UpgradeStore(this);
    }

    public Player getPlayer() {
        return player;
    }

    public void update() {
            Gdx.input.setInputProcessor(this);
            player.update();
            objectManager.update();
            enemyManager.update(player, objectManager);
            levelManager.update();
    }

    @Override
    public boolean keyDown(int keycode) {
        switch(keycode) {
            case Input.Keys.A: {
                player.setLeft(true);
            }
            break;
            case Input.Keys.D: {
                player.setRight(true);
            }
            break;
            case Input.Keys.S: {
                player.setDown(true);
            }
            break;
            case Input.Keys.W: {
                player.setUp(true);
            }
            break;
            case Input.Keys.SPACE: {
                objectManager.fireProjectile();
            }
            break;
            case Input.Keys.P: {
                pause = !pause;
                if (pause){
                    getEnemyManager().pause();
                } else {
                    getEnemyManager().resume();
                }
            }
            break;
            case Input.Keys.O: {
                if (Gamestate.state.equals(Gamestate.STORE)) {
                    pause = false;
                    getEnemyManager().resume();
                    Gamestate.state = Gamestate.PLAYING;
                } else if (Gamestate.state.equals(Gamestate.PLAYING)) {
                    pause = true;
                    getEnemyManager().pause();
                    Gamestate.state = Gamestate.STORE;
                }
            }
            break;
            case Input.Keys.R: {
                reset();
            }
            break;
            case Input.Keys.ESCAPE: {
                if (Gamestate.state.equals(Gamestate.MENU)) {
                    getEnemyManager().resume();
                    Gamestate.state = Gamestate.PLAYING;
                    pause = false;
                } else if (Gamestate.state.equals(Gamestate.PLAYING)) {
                    getEnemyManager().pause();
                    Gamestate.state = Gamestate.MENU;
                }
            }
            break;

        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        switch(keycode) {
            case Input.Keys.A: {
                player.setLeft(false);
            }
            break;
            case Input.Keys.D: {
                player.setRight(false);
            }
            break;
            case Input.Keys.S: {
                player.setDown(false);
            }
            break;
            case Input.Keys.W: {
                player.setUp(false);
            }
            break;
            case Input.Keys.BACK: {
                pause = true;
            }
            break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            objectManager.fireProjectile();
            return true;
        }
        return false;
    }

//    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }


    // Check collisions
    public boolean checkCollision(Rectangle hitBox, float damage) {
        Optional<Enemy> deadEnemy = enemyManager.getListOfEnemies().stream()
                .filter(enemy -> enemy.checkHit(hitBox, damage))
                .peek(enemy -> {
                    if (enemy.getCurrentHealth() <= 0) {
                        enemy.setDying(true);
                    }
                })
                .findFirst();
        return deadEnemy.isPresent();

    }


    public void reset() {
        System.out.println("Playing reset");
        objectManager.reset();
        levelManager.reset();
        player.reset();
        upgradeStore.resetUpgrades();
        //upgradeStore.render(Gdx.graphics.getDeltaTime());
//        initClasses();
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
}

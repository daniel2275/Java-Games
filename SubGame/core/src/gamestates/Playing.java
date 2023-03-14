package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Rectangle;
import entities.Enemy;
import entities.EnemyManager;
import entities.Player;
import objects.ObjectManager;

import java.util.Iterator;

import static com.danielr.subgame.SubGame.pause;

public class Playing implements InputProcessor {
    private Player player;
    private ObjectManager objectManager;
    private EnemyManager enemyManager;

    int x,y;


    public Playing() {
        initClasses();
    }

    private void initClasses() {
        player = new Player(this);
        objectManager =  new ObjectManager(this);
        enemyManager = new EnemyManager(this);
        enemyManager.create();
        Gdx.input.setInputProcessor(this);
    }

    public Player getPlayer() {
        return player;
    }

    public void update() {

            player.update();
            objectManager.update();
            enemyManager.update(player, objectManager);

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
            }
            break;
            case Input.Keys.R: {
                reset();
            }
            break;
            case Input.Keys.ESCAPE: {
                Gamestate.state = Gamestate.MENU;
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
        return false;
    }

//    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        if (!Gdx.input.isTouched()) {
//            pause = true;
//         }
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
        Iterator<Enemy> enemyIterator = enemyManager.getListOfEnemies().iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.checkHit(hitBox, damage)) {
                if (enemy.getEnemyHeath() <= 0) {
                    enemy.setDying(true);
                }
                return true;
            }

        }
        return false;
    }


    public void reset() {
        initClasses();
    }

    public EnemyManager getEnemyManager() {
        return enemyManager;
    }

    public ObjectManager getObjectManager() {
        return objectManager;
    }
}

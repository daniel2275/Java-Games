package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import com.danielr.subgame.SubGame;
import entities.Enemy;
import entities.Player;
import objects.ObjectManager;

import java.util.ArrayList;
import java.util.Iterator;

public class Playing  implements InputProcessor  {
    private Player player;
    private OrthographicCamera camera;
    private SubGame subGame;
    private ArrayList<Enemy> listOfEnemies = new ArrayList<>();

    private ObjectManager objectManager;

    public Playing(SubGame subGame, ArrayList<Enemy> listOfEnemies) {
        this.listOfEnemies = listOfEnemies;
        this.subGame = subGame;
        initClases();
    }

    private void initClases() {
        player = new Player(this);
        objectManager =  new ObjectManager(this);
        Gdx.input.setInputProcessor(this);
    }

    public Player getPlayer() {
        return player;
    }

    public void update() {
        player.update();
        objectManager.update();
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

    @Override
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

    public void checkCollision(Rectangle uBoatHitBox) {

        Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.checkHit(uBoatHitBox)) {
                System.out.println("remove");
                enemyIterator.remove();
//                LoadSave.removeEnemy(enemyIterator, enemy);
            }

        }
    }
}

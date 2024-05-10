package UI.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;

public class InputHandler implements InputProcessor {
    private GameScreen gameScreen;
    private boolean pause = false;
    private Vector2 playerPosition;
    private boolean isTouching;
    private Vector2 clickPosition;

    public InputHandler(GameScreen gameScreen) {

        this.gameScreen = gameScreen;
        this.playerPosition = (gameScreen.getPlayer().getPlayerActor().getPosition());
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isTouching = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Handle touch up events here (e.g., double tap for additional actions)
        gameScreen.getPlayer().setLeft(false);
        gameScreen.getPlayer().setRight(false);
        gameScreen.getPlayer().setDown(false);
        gameScreen.getPlayer().setUp(false);
        isTouching = false;
        System.out.println("reset");
        return true;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
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

    @Override
    public boolean keyDown(int keycode) {
        // Handle keyboard events here (e.g., movement controls)
        switch ( keycode ) {
            case Input.Keys.A:
                gameScreen.getPlayer().setLeft(true);
                break;
            case Input.Keys.D:
                gameScreen.getPlayer().setRight(true);
                break;
            case Input.Keys.S:
                gameScreen.getPlayer().setDown(true);
                break;
            case Input.Keys.W:
                gameScreen.getPlayer().setUp(true);
                break;
            case Input.Keys.SPACE:
                gameScreen.getObjectManager().fireProjectile();
                break;
            case Input.Keys.P: {
                System.out.println("PAUSED");
                pause = !pause;
                if (pause) {
                    gameScreen.pause();
                } else {
                    gameScreen.resume();
                }
            }
            break;
            case Input.Keys.O: {
                gameScreen.pause();
                gameScreen.getSubGame().setScreen(gameScreen.getSubGame().getUpgradeStore());
            }
            break;
            case Input.Keys.R: {
                gameScreen.reset();
            }
            break;
            case Input.Keys.ESCAPE: {
                gameScreen.pause();
                gameScreen.getSubGame().setScreen(gameScreen.getSubGame().getMenuRenderer());
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Handle key up events here (e.g., stop movement)
        switch ( keycode ) {
            case Input.Keys.A:
                gameScreen.getPlayer().setLeft(false);
                break;
            case Input.Keys.D:
                gameScreen.getPlayer().setRight(false);
                break;
            case Input.Keys.S:
                gameScreen.getPlayer().setDown(false);
                break;
            case Input.Keys.W:
                gameScreen.getPlayer().setUp(false);
                break;
        }
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    public boolean isTouching() {
        return isTouching;
    }

    public Vector2 getClickPosition() {
        float x = Gdx.input.getX();
        float y = Gdx.input.getY();
        clickPosition = new Vector2(x,y);
        return clickPosition;
    }
}






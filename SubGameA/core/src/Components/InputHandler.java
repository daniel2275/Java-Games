package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import gamestates.GamePlayScreen;
import gamestates.Gamestate;

public class InputHandler implements InputProcessor {

    private GamePlayScreen gamePlayScreen;
    private boolean pause = false;
    private Vector2 playerPosition;
    private boolean isTouching;
    private Vector2 clickPosition;

    public InputHandler(GamePlayScreen gamePlayScreen) {
        this.gamePlayScreen = gamePlayScreen;
        this.playerPosition = (gamePlayScreen.getPlayer().getPlayerActor().getPosition());
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        isTouching = true;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Handle touch up events here (e.g., double tap for additional actions)
        gamePlayScreen.getPlayer().setLeft(false);
        gamePlayScreen.getPlayer().setRight(false);
        gamePlayScreen.getPlayer().setDown(false);
        gamePlayScreen.getPlayer().setUp(false);
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
                gamePlayScreen.getPlayer().setLeft(true);
                break;
            case Input.Keys.D:
                gamePlayScreen.getPlayer().setRight(true);
                break;
            case Input.Keys.S:
                gamePlayScreen.getPlayer().setDown(true);
                break;
            case Input.Keys.W:
                gamePlayScreen.getPlayer().setUp(true);
                break;
            case Input.Keys.SPACE:
                gamePlayScreen.getObjectManager().fireProjectile();
                break;
            case Input.Keys.P: {
                pause = !pause;
                if (pause) {
                    gamePlayScreen.getEnemyManager().pause();
                } else {
                    gamePlayScreen.getEnemyManager().resume();
                }
            }
            break;
            case Input.Keys.O: {
                if (Gamestate.state.equals(Gamestate.STORE)) {
                    pause = false;
                    gamePlayScreen.getEnemyManager().resume();
                    Gamestate.state = Gamestate.PLAYING;
                } else if (Gamestate.state.equals(Gamestate.PLAYING)) {
                    pause = true;
                    gamePlayScreen.getEnemyManager().pause();
                    Gamestate.state = Gamestate.STORE;
                }
            }
            break;
            case Input.Keys.R: {
                gamePlayScreen.reset();
            }
            break;
            case Input.Keys.ESCAPE: {
                if (Gamestate.state.equals(Gamestate.MENU)) {
                    gamePlayScreen.getEnemyManager().resume();
                    Gamestate.state = Gamestate.PLAYING;
                    pause = false;
                } else if (Gamestate.state.equals(Gamestate.PLAYING)) {
                    gamePlayScreen.getEnemyManager().pause();
                    Gamestate.state = Gamestate.MENU;
                }
            }
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        // Handle key up events here (e.g., stop movement)
        switch ( keycode ) {
            case Input.Keys.A:
                gamePlayScreen.getPlayer().setLeft(false);
                break;
            case Input.Keys.D:
                gamePlayScreen.getPlayer().setRight(false);
                break;
            case Input.Keys.S:
                gamePlayScreen.getPlayer().setDown(false);
                break;
            case Input.Keys.W:
                gamePlayScreen.getPlayer().setUp(false);
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






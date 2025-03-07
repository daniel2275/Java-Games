package UI.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class InputHandler implements InputProcessor {
    private GameScreen gameScreen;
    private boolean pause = false;
    private Vector2 playerPosition;
    private boolean isTouching;
    private TouchpadController touchpadController;
    private int touchpadPointer = -1;
    private int projectilePointer = -1;
    private Vector2 clickPosition = new Vector2();


    public InputHandler(GameScreen gameScreen, Stage stage) {
        this.gameScreen = gameScreen;
        this.playerPosition = gameScreen.getPlayer().getPlayerActor().getPosition();
        // Initialize touchpad if on Android
        if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) {
            touchpadController = new TouchpadController(stage);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates to world coordinates
        Vector3 touchWorldCoords = new Vector3(screenX, screenY, 0);

        if (touchpadController != null && touchpadController.isTouchWithinBounds(screenX, screenY)) {
            touchpadPointer = pointer;
            return touchpadController.touchDown(screenX, screenY, pointer, button);
        } else if (projectilePointer == -1) {
            // Handle projectile firing
            projectilePointer = pointer;

            // Handle projectile firing based on platform
            if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) {
                // Offset the y-coordinate by 200 pixels
                int adjustedY = screenY - 200;
                gameScreen.updateCrosshairPosition(screenX, adjustedY);
                gameScreen.getCrossHairActor().setVisible(true);
            } else {
                gameScreen.camera.unproject(touchWorldCoords);
                float worldX = touchWorldCoords.x;
                float worldY = touchWorldCoords.y;

                gameScreen.getObjectManager().fireProjectile(worldX,worldY);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates to world coordinates
        Vector3 touchWorldCoords = new Vector3(screenX, screenY, 0);

        if (pointer == touchpadPointer) {
            touchpadPointer = -1;
            return touchpadController.touchUp(screenX, screenY, pointer, button);
        } else if (pointer == projectilePointer) {
            projectilePointer = -1;
            if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) {
                gameScreen.getCrossHairActor().setVisible(false);
                // Offset the y-coordinate by 200 pixels
                int adjustedY = screenY - 200;
                gameScreen.camera.unproject(touchWorldCoords.set(screenX, adjustedY, 0));
                float worldX = touchWorldCoords.x;
                float worldY = touchWorldCoords.y;

                gameScreen.getObjectManager().fireProjectile(worldX,worldY);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (pointer == touchpadPointer) {
            return touchpadController.touchDragged(screenX, screenY, pointer);
        } else if (pointer == projectilePointer) {
            clickPosition.set(screenX, screenY);
            //android crossHair
            if (Gdx.app.getType() == com.badlogic.gdx.Application.ApplicationType.Android) {
                // Offset the y-coordinate by 200 pixels
                int adjustedY = screenY - 200;
                gameScreen.updateCrosshairPosition(screenX, adjustedY);
                //gameScreen.updateCrosshairPosition(screenX, screenY);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
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
        switch (keycode) {
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
//                gameScreen.getObjectManager().fireProjectile();
//                break;
            case Input.Keys.P: {
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
                gameScreen.getUpgradeStore().saveGame();
                gameScreen.getSubGame().setScreen(gameScreen.getUpgradeStore());
            }
            break;
            case Input.Keys.R: {
                gameScreen.reset();
                gameScreen.resetUpgrades();
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
        switch (keycode) {
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

    public Vector3 getClickPosition() {
        return new Vector3(clickPosition,0);
    }

    public void update() {
        if (touchpadController != null && touchpadController.isTouchpadTouched()) {
            int direction = touchpadController.getCurrentDirection();
            System.out.println(touchpadController.getCurrentDirection());
            switch (direction) {
                case TouchpadController.UP:
                    gameScreen.getPlayer().setUp(true);
                    gameScreen.getPlayer().setRight(false);
                    gameScreen.getPlayer().setLeft(false);
                    gameScreen.getPlayer().setDown(false);
                    break;
                case TouchpadController.UP_RIGHT:
                    gameScreen.getPlayer().setUp(true);
                    gameScreen.getPlayer().setRight(true);
                    gameScreen.getPlayer().setLeft(false);
                    gameScreen.getPlayer().setDown(false);

                    break;
                case TouchpadController.RIGHT:
                    gameScreen.getPlayer().setRight(true);
                    gameScreen.getPlayer().setUp(false);
                    gameScreen.getPlayer().setLeft(false);
                    gameScreen.getPlayer().setDown(false);

                    break;
                case TouchpadController.DOWN_RIGHT:
                    gameScreen.getPlayer().setDown(true);
                    gameScreen.getPlayer().setRight(true);
                    gameScreen.getPlayer().setLeft(false);
                    gameScreen.getPlayer().setUp(false);

                    break;
                case TouchpadController.DOWN:
                    gameScreen.getPlayer().setDown(true);
                    gameScreen.getPlayer().setRight(false);
                    gameScreen.getPlayer().setUp(false);
                    gameScreen.getPlayer().setLeft(false);
                    break;
                case TouchpadController.DOWN_LEFT:
                    gameScreen.getPlayer().setDown(true);
                    gameScreen.getPlayer().setLeft(true);
                    gameScreen.getPlayer().setRight(false);
                    gameScreen.getPlayer().setUp(false);
                    break;
                case TouchpadController.LEFT:
                    gameScreen.getPlayer().setLeft(true);
                    gameScreen.getPlayer().setUp(false);
                    gameScreen.getPlayer().setDown(false);
                    gameScreen.getPlayer().setRight(false);
                    break;
                case TouchpadController.UP_LEFT:
                    gameScreen.getPlayer().setUp(true);
                    gameScreen.getPlayer().setLeft(true);
                    gameScreen.getPlayer().setRight(false);
                    gameScreen.getPlayer().setDown(false);
                    break;
            }
        } else if (touchpadController != null){
            // No direction when in the deadzone
            gameScreen.getPlayer().setUp(false);
            gameScreen.getPlayer().setRight(false);
            gameScreen.getPlayer().setDown(false);
            gameScreen.getPlayer().setLeft(false);
        }

        gameScreen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    public void dispose() {
        if (touchpadController != null) {
            touchpadController.dispose();
        }
    }

    public TouchpadController getTouchpadController() {
        return touchpadController;
    }
}

package UI.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TouchpadController {
    private TextureAtlas textureAtlas;
    private Touchpad touchpad;
    private int currentDirection = -1;

    // Directions
    public static final int UP = 0;
    public static final int UP_RIGHT = 1;
    public static final int RIGHT = 2;
    public static final int DOWN_RIGHT = 3;
    public static final int DOWN = 4;
    public static final int DOWN_LEFT = 5;
    public static final int LEFT = 6;
    public static final int UP_LEFT = 7;

    public TouchpadController(Stage stage) {
        // Load the texture atlas (make sure you have a texture atlas file in the assets folder)
        textureAtlas = new TextureAtlas(Gdx.files.internal("touchpad.atlas"));

        // Create the touchpad
        createTouchpad(stage);
    }

    private void createTouchpad(Stage stage) {
        // Create Touchpad Style using the texture atlas
        TouchpadStyle touchpadStyle = new TouchpadStyle();
        touchpadStyle.background = new TextureRegionDrawable(new TextureRegion(textureAtlas.findRegion("touchBackground")));
        touchpadStyle.knob = new TextureRegionDrawable(new TextureRegion(textureAtlas.findRegion("touchKnob")));

        // Create Touchpad
        touchpad = new Touchpad(10, touchpadStyle);
        touchpad.setBounds(15, 15, 200, 200);

        // Add listener to handle touchpad movement
        touchpad.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                float knobPercentX = touchpad.getKnobPercentX();
                float knobPercentY = touchpad.getKnobPercentY();

                // Calculate the angle of the knob position
                double angle = Math.atan2(knobPercentY, knobPercentX) * 180 / Math.PI;
                if (angle < 0) {
                    angle += 360;
                }

                // Map the angle to the nearest of the 8 directions
                currentDirection = getDirectionFromAngle(angle);
            }
        });

        stage.addActor(touchpad);
    }

    private int getDirectionFromAngle(double angle) {
        if (angle >= 337.5 || angle < 22.5) {
            return RIGHT;
        } else if (angle >= 22.5 && angle < 67.5) {
            return UP_RIGHT;
        } else if (angle >= 67.5 && angle < 112.5) {
            return UP;
        } else if (angle >= 112.5 && angle < 157.5) {
            return UP_LEFT;
        } else if (angle >= 157.5 && angle < 202.5) {
            return LEFT;
        } else if (angle >= 202.5 && angle < 247.5) {
            return DOWN_LEFT;
        } else if (angle >= 247.5 && angle < 292.5) {
            return DOWN;
        } else if (angle >= 292.5 && angle < 337.5) {
            return DOWN_RIGHT;
        }
        return -1; // Invalid angle
    }

    public int getCurrentDirection() {
        return currentDirection;
    }

    public boolean isTouchpadTouched() {
        return touchpad.isTouched();
    }

    public boolean isTouchWithinBounds(float x, float y) {
        return touchpad.getX() <= x && x <= touchpad.getX() + touchpad.getWidth() &&
            touchpad.getY() <= y && y <= touchpad.getY() + touchpad.getHeight();
    }

    public boolean touchDown(float x, float y, int pointer, int button) {
        if (isTouchWithinBounds(x, y)) {
            InputEvent event = new InputEvent();
            event.setType(InputEvent.Type.touchDown);
            event.setStageX(x);
            event.setStageY(y);
            event.setPointer(pointer);
            event.setButton(button);
            touchpad.fire(event);
            return true;
        }
        return false;
    }

    public boolean touchUp(float x, float y, int pointer, int button) {
        if (isTouchWithinBounds(x, y)) {
            InputEvent event = new InputEvent();
            event.setType(InputEvent.Type.touchUp);
            event.setStageX(x);
            event.setStageY(y);
            event.setPointer(pointer);
            event.setButton(button);
            touchpad.fire(event);
            return true;
        }
        return false;
    }

    public boolean touchDragged(float x, float y, int pointer) {
        if (isTouchWithinBounds(x, y)) {
            InputEvent event = new InputEvent();
            event.setType(InputEvent.Type.touchDragged);
            event.setStageX(x);
            event.setStageY(y);
            event.setPointer(pointer);
            touchpad.fire(event);
            return true;
        }
        return false;
    }

    public void dispose() {
        textureAtlas.dispose();
    }
}






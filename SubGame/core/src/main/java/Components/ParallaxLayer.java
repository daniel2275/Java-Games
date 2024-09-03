package Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static utilities.Constants.Game.VIRTUAL_WIDTH;

public class ParallaxLayer extends Actor implements  Pausable{
    private Texture texture;
    private float speed;
    private float previousX;
    private float totalWidth;
    private boolean paused = false;

    public ParallaxLayer(Texture texture, float speed,float x, float y) {
        this.texture = texture;
        this.speed = speed;
        this.previousX = VIRTUAL_WIDTH;

        // Calculate the scaled height to maintain aspect ratio
        //float scaledWidth = Gdx.graphics.getWidth();
        setBounds(x, y, texture.getWidth(), texture.getHeight());
        this.totalWidth = getWidth();
    }

    @Override
    public void act(float delta) {
        if (!paused) {
            // Update the position based on speed
            setPosition(getX() - speed * delta, getY());
            // Reset position if it moves out of the screen
            if (getX() + getWidth() <= 0) {
                moveBy(previousX + totalWidth, 0);
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}


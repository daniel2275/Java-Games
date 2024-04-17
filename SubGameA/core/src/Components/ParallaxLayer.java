package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class ParallaxLayer extends Actor {
    private Texture texture;
    private float speed;
    private float previousX;
    private float totalWidth;

    public ParallaxLayer(Texture texture, float speed,float x, float y) {
        this.texture = texture;
        this.speed = speed;
        this.previousX = Gdx.graphics.getWidth();

        // Calculate the scaled height to maintain aspect ratio
        //float scaledWidth = Gdx.graphics.getWidth();
        setBounds(x, y, texture.getWidth(), texture.getHeight());
        this.totalWidth = getWidth();
    }

    @Override
    public void act(float delta) {
        // Update the position based on speed
        setPosition(getX() - speed * delta, getY());
        // Reset position if it moves out of the screen
        if (getX() + getWidth() <= 0) {
            moveBy(previousX + totalWidth, getY());
            //setPosition(previousX + totalWidth, getY());

        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}


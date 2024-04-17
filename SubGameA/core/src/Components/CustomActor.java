package Components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static utilities.Constants.Game.WORLD_WIDTH;

public class CustomActor extends Actor {
    private Sprite sprite;
    private float playerX;

    public CustomActor(Texture texture, float x, float y) {
        sprite = new Sprite(texture);

        // Set initial position
        setPosition(x, y);

        // Set sprite size
        sprite.setSize(WORLD_WIDTH + 200, texture.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Draw sprite
        sprite.setPosition(getX() - 100, getY());
        sprite.draw(batch);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Adjust position based on playerX
        float newX = calculateNewX();
        setPosition(newX, getY());
    }

    private float calculateNewX() {
        float newX = 0;
        float stageWidth = getStage().getWidth();

        if (playerX <= 100 && playerX > 0) {
            newX = 100 - playerX;
        } else if (playerX >= stageWidth - 100) {
            newX = -100 + (stageWidth - playerX);
        }

        return newX;
    }

    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }
}

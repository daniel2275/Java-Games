//package Components;
//
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.graphics.g2d.Sprite;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import entities.enemies.EnemyManager;
//
//import static utilities.Constants.Game.WORLD_WIDTH;
//
//public class CustomActor extends Actor implements Pausable {
//    private Sprite sprite;
//    private float playerX;
//    private boolean paused = false;
//    private EnemyManager enemyManager;
//
//    public CustomActor(Texture texture, float x, float y) {
//        this.enemyManager = enemyManager;
//        sprite = new Sprite(texture);
//
//        // Set initial position
//        setPosition(x, y);
//
//        // Set sprite size
//        sprite.setSize(WORLD_WIDTH + 200, texture.getHeight());
//    }
//
//    @Override
//    public void draw(Batch batch, float parentAlpha) {
//        super.draw(batch, parentAlpha);
//        // Draw sprite
//        sprite.setPosition(getX() - 100, getY());
//        sprite.draw(batch);
//    }
//
//    @Override
//    public void act(float delta) {
//        if (!paused) {
//            super.act(delta);
//
//            // Adjust position based on playerX
//            float newX = calculateNewX();
//            setPosition(newX, getY());
//        }
//    }
//
//    private float calculateNewX() {
//        float newX = 0;
//        float stageWidth = getStage().getWidth();
//
//        if (playerX <= 100 && playerX > 0) {
//            newX = 100 - playerX;
//        } else if (playerX >= stageWidth - 100) {
//            newX = -100 + (stageWidth - playerX);
//
//        }
//
//        return newX;
//    }
//
//
//    public void setPlayerX(float playerX) {
//        this.playerX = playerX;
//    }
//
//    @Override
//    public void setPaused(boolean paused) {
//        this.paused = paused;
//    }
//}
package Components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import static utilities.Constants.Game.VIRTUAL_WIDTH;

public class BackgroundActor extends Actor implements Pausable {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float playerX;
    private boolean paused = false;
    //private EnemyManager enemyManager;

    public BackgroundActor(Animation<TextureRegion> animation, float x, float y) {
        //this.enemyManager = enemyManager;
        this.animation = animation;
        this.stateTime = 0f;

        // Set initial position
        setPosition(x, y);

        // Assuming all frames have the same size
        TextureRegion frame = animation.getKeyFrame(0);
        //float width = frame.getRegionWidth();
        float height = frame.getRegionHeight();
        setSize(VIRTUAL_WIDTH + 200, height);

    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // Get the current frame of the animation
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        // Draw current frame
        batch.draw(currentFrame, getX() - 100, getY(), getWidth(), getHeight());
    }

    @Override
    public void act(float delta) {
        if (!paused) {
            super.act(delta);
            stateTime += delta;

            // Adjust position based on playerX
            float newX = calculateNewX();
            setPosition(newX, getY());
        }
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

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}

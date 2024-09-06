package Components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class CrossHairActor extends Actor {
    private Animation<TextureRegion> animation;
    private float stateTime = 0; // Tracks elapsed time for animation

    public CrossHairActor(Animation<TextureRegion> animation) {
        this.animation = animation;
        setSize(animation.getKeyFrame(0).getRegionWidth(), animation.getKeyFrame(0).getRegionHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        // Update the stateTime with the time passed (delta)
        stateTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Get the current frame based on stateTime
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true); // 'true' loops the animation
        // Draw the current frame
        batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
    }
}



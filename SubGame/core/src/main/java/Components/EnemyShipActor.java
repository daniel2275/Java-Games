package Components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class EnemyShipActor extends BaseActor {


    public EnemyShipActor(String name,
                          Animation<TextureRegion> idleAnimationLeft,
                          Animation<TextureRegion> idleAnimationRight,
                          Animation<TextureRegion> moveAnimationLeft,
                          Animation<TextureRegion> moveAnimationRight,
                          Animation<TextureRegion> turningAnimationLeft,
                          Animation<TextureRegion> turningAnimationRight,
                          Animation<TextureRegion> hitAnimationLeft,
                          Animation<TextureRegion> hitAnimationRight,
                          Animation<TextureRegion> sunkAnimationLeft,
                          Animation<TextureRegion> sunkAnimationRight,
                          float reload,
                          float maxHealth,
                          float frameWidth,
                          float frameHeight,
                          float spawnPosX,
                          float spawnPosY,
                          boolean randomMovement) {
        super(
            name,
            idleAnimationLeft,
            idleAnimationRight,
            moveAnimationLeft,
            moveAnimationRight,
            turningAnimationLeft,
            turningAnimationRight,
            hitAnimationLeft,
            hitAnimationRight,
            sunkAnimationLeft,
            sunkAnimationRight,
            reload,
            maxHealth,
            frameWidth,
            frameHeight,
            spawnPosX,
            spawnPosY,
            randomMovement);
    }

    @Override
    public void surface() {
    }

    @Override
    public void handleSinking() {
            addAction(Actions.sequence(
                Actions.parallel(
                    Actions.fadeOut(1.0f),
                    Actions.moveBy(0, -50f, 4.0f)
                ),
                Actions.removeActor()
            ));
        isSunk(true);
    }


    @Override
    public Rectangle getBounding() {
        return new Rectangle(getX() + 5, getY() , getWidth() - 15, getHeight());
    }
}


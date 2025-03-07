package Components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class EnemySubActor extends BaseActor {


    public EnemySubActor(String name,
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
    public Rectangle getBounding() {
        return new Rectangle(getX(), getY() , getWidth() , getHeight() - 9);
    }

    @Override
    public void surface(){
    }

}

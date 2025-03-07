package Components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class PlayerActor extends BaseActor{


    public PlayerActor(String name, Animation<TextureRegion> idleAnimationLeft, Animation<TextureRegion> moveAnimationLeft, Animation<TextureRegion> upAnimationLeft, Animation<TextureRegion> downAnimationLeft, Animation<TextureRegion> hitAnimationLeft, Animation<TextureRegion> sunkAnimationLeft, Animation<TextureRegion> surfacingAnimationLeft, Animation<TextureRegion> turningAnimationLeft, Animation<TextureRegion> idleAnimationRight, Animation<TextureRegion> moveAnimationRight, Animation<TextureRegion> upAnimationRight, Animation<TextureRegion> downAnimationRight, Animation<TextureRegion> hitAnimationRight, Animation<TextureRegion> sunkAnimationRight, Animation<TextureRegion> surfacingAnimationRight, Animation<TextureRegion> turningAnimationRight, float reload, float maxHealth, float frameWidth, float frameHeight, float spawnPosX, float spawnPosY) {
        super(name, idleAnimationLeft, moveAnimationLeft, upAnimationLeft, downAnimationLeft, hitAnimationLeft, sunkAnimationLeft, surfacingAnimationLeft, turningAnimationLeft, idleAnimationRight, moveAnimationRight, upAnimationRight, downAnimationRight, hitAnimationRight, sunkAnimationRight, surfacingAnimationRight, turningAnimationRight, reload, maxHealth, frameWidth, frameHeight, spawnPosX, spawnPosY);
    }

    @Override
    public void setHit(boolean hit, float damage) {
        super.setHit(hit, damage);
        setMoveSpeed(Math.max((getMoveSpeed() - (0.25f * getMoveSpeed())), 0));
    }

    @Override
    public Rectangle getBounding() {
        rotate();
        return new Rectangle(getX(), getY(), getWidth(), getHeight() - 5);
    }


    @Override
    public boolean directionChanged() {
        // Check if the direction has changed
        if (!overrideActive) {
            if (!newHorizontalDirection.equals(previousHorizontalDirection)) {
                // This is a new direction change, store the X position for buffer calculation
                if (firstChangeX == -1) {
                    firstChangeX = getX(); // Store X position when direction changes for the first time
                }
            } else {
                // If direction hasn't changed, reset firstChangeX to -1 to avoid outdated tracking
                firstChangeX = -1;
            }

            // If firstChangeX has been set, calculate the movement distance
            if (firstChangeX != -1) {
                float movementDistance = Math.abs(getX() - firstChangeX);

                // Trigger the turning animation if the movement exceeds the buffer zone
                if (movementDistance >= TURN_BUFFER) {
                    isTurning = true;
                    horizontalDirection = newHorizontalDirection;

                    // Set the turning animation
                    if (horizontalDirection.equals("L")) {
                        tempAnimation = turningAnimationRight;
                    } else {
                        tempAnimation = turningAnimationLeft;
                    }

                    // Set overrideAnimation to tempAnimation
                    stateTime = 0;
                    setOverrideAnimation(tempAnimation);
                    previousHorizontalDirection = horizontalDirection; // Update previous direction

                    // Reset firstChangeX after the turn animation
                    firstChangeX = -1;

                    return true;
                }
            }
        }

        return false;
    }




}

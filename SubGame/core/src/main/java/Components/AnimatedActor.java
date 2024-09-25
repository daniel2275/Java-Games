package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import utilities.SoundManager;

import static utilities.Constants.Game.*;

public class AnimatedActor extends Actor implements Pausable {
    private static final float FADE_DURATION = 6f;
    private float moveSpeed = 10.5f;
    private float damage = 0;
    private final String name;

    // Left-facing animations
    private Animation<TextureRegion> idleAnimationLeft;
    private Animation<TextureRegion> moveAnimationLeft;
    private Animation<TextureRegion> upAnimationLeft;
    private Animation<TextureRegion> downAnimationLeft;
    private Animation<TextureRegion> hitAnimationLeft;
    private Animation<TextureRegion> sunkAnimationLeft;
    private Animation<TextureRegion> surfacingAnimationLeft;
    private Animation<TextureRegion> turningAnimationLeft;

    // Right-facing animations
    private Animation<TextureRegion> idleAnimationRight;
    private Animation<TextureRegion> moveAnimationRight;
    private Animation<TextureRegion> upAnimationRight;
    private Animation<TextureRegion> downAnimationRight;
    private Animation<TextureRegion> hitAnimationRight;
    private Animation<TextureRegion> sunkAnimationRight;
    private Animation<TextureRegion> surfacingAnimationRight;
    private Animation<TextureRegion> turningAnimationRight;

    // Current and temporary animations
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> tempAnimation;

    // Health bar texture
    private TextureRegion healthBarTextureRegion;

    // Movement and state tracking
    private float stateTime;
    private float previousX;
    private float previousY;
    private float reload;
    private float reloadSpeed;
    private final float maxHealth;
    private float currentHealth;
    private float deltaTime = 0;
    private boolean isHit;
    private boolean sunk;
    private boolean paused = false;
    private boolean parked = false;
    private float angle;

    // Override animation
    private boolean overrideActive = false;
    private Animation<TextureRegion> overrideAnimation = null;

    private final boolean isSub;
    private boolean hasReloadCompleted = false;

    // Surfacing and reversing flags
    private boolean surfaceAnimationPlayed = false;
    private boolean reversed = false;
    private float triggerSurfaceDive = VIRTUAL_HEIGHT - SKY_SIZE - 35;

    // Direction tracking
    private String previousHorizontalDirection;
    private boolean isTurning = false;
    private boolean isChangingDepth = false;
    private String newHorizontalDirection;
    private String previousVerticalDirection;
    private String horizontalDirection;
    private String verticalDirection;

    // Collision state
    private boolean collision;

    private ShapeRenderer shapeRenderer;

    // Constructor
    public AnimatedActor(
        String name,
        Animation<TextureRegion> idleAnimationLeft,
        Animation<TextureRegion> moveAnimationLeft,
        Animation<TextureRegion> upAnimationLeft,
        Animation<TextureRegion> downAnimationLeft,
        Animation<TextureRegion> hitAnimationLeft,
        Animation<TextureRegion> sunkAnimationLeft,
        Animation<TextureRegion> surfacingAnimationLeft,
        float reload,
        float maxHealth,
        float frameWidth,
        float frameHeight,
        float spawnPosX,
        float spawnPosY,
        boolean isSub
    ) {
        this.name = name;
        setName(name);

        // Assign left-facing animations
        this.idleAnimationLeft = idleAnimationLeft;
        this.moveAnimationLeft = moveAnimationLeft;
        this.upAnimationLeft = upAnimationLeft;
        this.downAnimationLeft = downAnimationLeft;
        this.hitAnimationLeft = hitAnimationLeft;
        this.sunkAnimationLeft = sunkAnimationLeft;
        this.surfacingAnimationLeft = surfacingAnimationLeft;

        // Generate right-facing animations by flipping left-facing animations
        this.idleAnimationRight = invertHorizontal(idleAnimationLeft);
        this.moveAnimationRight = invertHorizontal(moveAnimationLeft);
        this.upAnimationRight = invertHorizontal(upAnimationLeft);
        this.downAnimationRight = invertHorizontal(downAnimationLeft);
        this.hitAnimationRight = invertHorizontal(hitAnimationLeft);
        this.sunkAnimationRight = invertHorizontal(sunkAnimationLeft);
        this.surfacingAnimationRight = invertHorizontal(surfacingAnimationLeft);

        // Reload and health setup
        this.reload = reload;
        this.maxHealth = maxHealth;
        this.isSub = isSub;
        this.currentHealth = maxHealth;

        this.isHit = false;
        this.sunk = false;
        this.reloadSpeed = 0;
        this.angle = 0;

        // Set frame dimensions and position
        setWidth(frameWidth);
        setHeight(frameHeight);
        setPosition(spawnPosX, spawnPosY);
        this.previousX = getX();
        this.previousY = getY();

        // Initialize directions and animations
        this.horizontalDirection = "L";
        this.previousHorizontalDirection = this.horizontalDirection;
        this.previousVerticalDirection = "idle";
        this.tempAnimation = idleAnimationLeft;
        this.currentAnimation = idleAnimationLeft;

        // Collision state
        collision = false;

        // Initialize health bar
        healthBarTextureRegion = new TextureRegion(new Texture("health_bar.png"));
        healthBarTextureRegion.setRegionWidth((int) frameWidth);

        shapeRenderer = new ShapeRenderer();

        // Set player to the back
        if (name.equals("Player")) {
            toBack();
        }
    }

    public AnimatedActor(String name,
                         Animation<TextureRegion> idleAnimationLeft,
                         Animation<TextureRegion> moveAnimationLeft,
                         Animation<TextureRegion> upAnimationLeft,
                         Animation<TextureRegion> downAnimationLeft,
                         Animation<TextureRegion> hitAnimationLeft,
                         Animation<TextureRegion> sunkAnimationLeft,
                         Animation<TextureRegion> surfacingAnimationLeft,
                         Animation<TextureRegion> turningAnimationLeft,
                         float reload,
                         float maxHealth,
                         float frameWidth,
                         float frameHeight,
                         float spawnPosX,
                         float spawnPosY,
                         boolean isSub
    ) {
        this(name, idleAnimationLeft, moveAnimationLeft, upAnimationLeft, downAnimationLeft, hitAnimationLeft, sunkAnimationLeft, surfacingAnimationLeft, reload, maxHealth, frameWidth, frameHeight, spawnPosX, spawnPosY, isSub);
        this.turningAnimationLeft = turningAnimationLeft;
        this.turningAnimationRight = invertHorizontal(turningAnimationLeft);
    }

    @Override
    public void act(float delta) {
        if (currentAnimation == null)
            return;


        if (!paused) {
            super.act(delta);


            if (collision) evadeEnemy();


            updateMovementState();
            updateAnimation();

            if (this.name == "Player") {
                surface();
            }

            previousX = getX();
            previousY = getY();

            deltaTime = delta;
            stateTime += delta;

            // If sunk, gradually fade out the actor
            if (currentHealth == 0 && !isSunk()) {
                stateTime = 0;
                handleSinking();
            }

        }
    }


    private void evadeEnemy() {
        // Move along the X axis and ensure the player stays within the bounds [0, WORLD_WIDTH]
        if (previousX < getX()) {
            setX(Math.max(0, getX() - 2)); // Move left, but not beyond the left boundary
        } else if (previousX > getX()) {
            setX(Math.min(VIRTUAL_WIDTH, getX() + 2)); // Move right, but not beyond the right boundary
        }

        // Move along the Y axis and ensure the player stays within the bounds [0, WORLD_HEIGHT - SKY_SIZE]
        if (previousY > getY()) {
            setY(Math.min(VIRTUAL_HEIGHT - SKY_SIZE, getY() + 2)); // Move up, but not beyond the upper boundary
        } else if (previousY < getY()) {
            setY(Math.max(0, getY() - 2)); // Move down, but not beyond the lower boundary
        }

        collision = false;
    }


    public void setOverrideAnimation(Animation<TextureRegion> animation) {
        overrideAnimation = animation;
        overrideActive = true;
        stateTime = 0;
    }

    private void handleSinking() {
        if (!(name == "torpedo" || name == "depthCharge")) {
            addAction(Actions.sequence(
                Actions.parallel(
                    Actions.fadeOut(1.0f),
                    Actions.moveBy(0, -50f, 4.0f)
                ),
                Actions.removeActor()
            ));
        } else {
            addAction(Actions.sequence(
                Actions.parallel(
                    Actions.delay(4.0f)
                ),
                Actions.removeActor()
            ));
        }
        isSunk(true);
    }


    private void updateMovementState() {
        // Determine the new direction
        newHorizontalDirection = horizontalDirection;

        if (getX() > previousX) {
            newHorizontalDirection = "R";
        } else if (getX() < previousX) {
            newHorizontalDirection = "L";
        }

        previousVerticalDirection = verticalDirection;

        if (getY() > previousY) {
            verticalDirection = "up";
        } else if (getY() < previousY) {
            verticalDirection = "down";
        } else {
            verticalDirection = "idle";
        }

        if (!name.equals("torpedo") && !name.equals("depthCharge")) {
            // Check if the direction has changed
            if (!newHorizontalDirection.equals(previousHorizontalDirection)) {
                // Direction has changed, play turning animation
                isTurning = true;
                horizontalDirection = newHorizontalDirection;

                // Set the turning animation, invert if turning right
                if (horizontalDirection.equals("L")) {
                    tempAnimation = turningAnimationRight;
                    // stateTime = 0;
                } else {
                    tempAnimation = turningAnimationLeft;
                    // stateTime = 0;
                }
                // Set overrideAnimation to tempAnimation
                setOverrideAnimation(tempAnimation);
                previousHorizontalDirection = horizontalDirection; // Update previousDirection
                return; // Exit early to prevent further processing
            }
        }

        if (!isTurning && !isChangingDepth) {
            if (horizontalDirection == "L") {
                if (getX() > previousX || getX() < previousX) {
                    tempAnimation = moveAnimationLeft;
                } else if (getX() == previousX && getY() == previousY) {
                    tempAnimation = idleAnimationLeft;
                }
            } else {
                if (getX() > previousX || getX() < previousX) {
                    tempAnimation = moveAnimationRight;
                } else if (getX() == previousX && getY() == previousY) {
                    tempAnimation = idleAnimationRight;
                }
            }

            if (horizontalDirection == "L") {
                if (verticalDirection.equals("up") && !previousVerticalDirection.equals(verticalDirection)) {
                    tempAnimation = upAnimationLeft;
                } else if (verticalDirection.equals("down") && !previousVerticalDirection.equals(verticalDirection)) {
                    tempAnimation = downAnimationLeft;
                }
            } else {
                if (verticalDirection.equals("up") && !previousVerticalDirection.equals(verticalDirection)) {
                    tempAnimation = upAnimationRight;
                } else if (verticalDirection.equals("down") && !previousVerticalDirection.equals(verticalDirection)) {
                    tempAnimation = downAnimationRight;
                }
            }

            if (horizontalDirection == "L") {
                if (isHit) {
                    tempAnimation = hitAnimationLeft;
                } else if (currentHealth == 0) {
                    tempAnimation = sunkAnimationLeft;
                }
            } else {
                if (isHit) {
                    tempAnimation = hitAnimationRight;
                } else if (currentHealth == 0) {
                    tempAnimation = sunkAnimationRight;
                }
            }

        }
//        }
        previousHorizontalDirection = horizontalDirection; // Update previousDirection
    }


    public void updateAnimationSpeedBasedOnMoveSpeed() {
        if (currentAnimation != null) {
            // Scale the frame duration inversely to the moveSpeed (faster move = shorter frame duration)
            float scaledFrameDuration = 0.2f / (moveSpeed / 10f);
            currentAnimation.setFrameDuration(scaledFrameDuration);
        }
    }

    private void updateAnimation() {
        if (overrideActive) {
            currentAnimation = overrideAnimation;
            updateAnimationSpeedBasedOnMoveSpeed();
            if (reversed) {
                overrideAnimation.setPlayMode(Animation.PlayMode.REVERSED);
            } else {
                overrideAnimation.setPlayMode(Animation.PlayMode.NORMAL);
            }
            if (currentAnimation.isAnimationFinished(stateTime)) {
                overrideActive = false;
                overrideAnimation = null;
                isTurning = false;
                isChangingDepth = false;
                reversed = false;
                if (reversed) {
                    toBack();
                }
            }
        } else {
            if (tempAnimation == null)
                return;
            if (currentAnimation != tempAnimation) {
                currentAnimation = tempAnimation;
                stateTime = 0;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (currentAnimation == null)
            return;

        // If sunk, gradually fade out the actor
        if (currentHealth == 0 && !(name == "torpedo" || name == "depthCharge")) {
            float alpha = Math.max(0, 1 - (stateTime / FADE_DURATION));
            batch.setColor(1, 1, 1, alpha);
        }

        // Define looping animation
        boolean loops;
        if (isSub) {
            loops = (tempAnimation == idleAnimationLeft || tempAnimation == idleAnimationRight
                || tempAnimation == moveAnimationLeft || tempAnimation == moveAnimationRight);
        } else {
            loops = (tempAnimation == idleAnimationLeft || tempAnimation == idleAnimationRight ||
                tempAnimation == moveAnimationLeft || tempAnimation == moveAnimationRight);
        }


        batch.draw(currentAnimation.getKeyFrame(stateTime, loops), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1f, 1f, angle);


        if (name != "torpedo" && name != "depthCharge") {
            drawHealthBar(batch);
            drawReloadBar(batch);
        }

        if (!overrideActive) {
            surfaceAnimationPlayed = false;
        }

        isHit = false;
        // Reset the batch color to fully opaque
        batch.setColor(1, 1, 1, 1);

        // Draw bounding rectangle for debugging
        batch.end();

        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        Rectangle boundingRectangle = getBounding();
        shapeRenderer.rect(boundingRectangle.x, boundingRectangle.y, boundingRectangle.width, boundingRectangle.height);

        shapeRenderer.end();
        batch.begin();
    }

    public void surface() {
        if ((previousY < triggerSurfaceDive && getY() >= triggerSurfaceDive) && !surfaceAnimationPlayed) {
            //surfacingAnimation.setFrameDuration(surfacingAnimation.getAnimationDuration() / (moveSpeed - 5f));
//            if (horizontalDirection == "L") {
//                surfacingAnimationLeft.setPlayMode(Animation.PlayMode.NORMAL);
//            } else {
//                surfacingAnimationRight.setPlayMode(Animation.PlayMode.NORMAL);
//            }
            stateTime = 0;
            setSurfacingAnimation();
            surfaceAnimationPlayed = true; // Ensure the animation only plays once per crossing
            reversed = false;
            toFront();
        } else if ((previousY > triggerSurfaceDive && getY() <= triggerSurfaceDive) && !surfaceAnimationPlayed) {
            stateTime = 0;
            setSurfacingAnimation();
            surfaceAnimationPlayed = true; // Ensure the animation only plays once per crossing
            reversed = true;
            toBack();
        }
    }

    // Method to check collision with another actor
    public boolean collidesWith(AnimatedActor otherActor) {
        Rectangle myRect = getBounding();
        Rectangle otherRect = otherActor.getBounding();
        return myRect.overlaps(otherRect);
    }

    private void drawHealthBar(Batch batch) {
        if (currentHealth > 0) {
            Color healthBarColor = determineHealthBarColor();
            batch.setColor(healthBarColor);

            float healthBarWidth = healthBarTextureRegion.getRegionWidth();
            float healthBarForegroundWidth = (currentHealth / maxHealth) * healthBarWidth;

            batch.draw(healthBarTextureRegion, getX(), getY() + getHeight() + 3, healthBarForegroundWidth, 2);
        }
        batch.setColor(Color.WHITE);
    }

    private void drawReloadBar(Batch batch) {
        if (reload >= 0) {
            Color reloadBarColor = determineReloadBarColor();
            batch.setColor(reloadBarColor);

            float reloadBarWidth = healthBarTextureRegion.getRegionWidth();
            float reloadBarForegroundWidth = reloadBarWidth - (reloadBarWidth / reloadSpeed * reload);

            batch.draw(healthBarTextureRegion, getX(), getY() + getHeight() + 5, reloadBarForegroundWidth, 2);
            // Check if reload is full
            if (reload == 0 && !hasReloadCompleted) {
                onReloadComplete();
                hasReloadCompleted = true;
            }
        }
        batch.setColor(Color.WHITE);
    }

    private void onReloadComplete() {
        SoundManager soundManager = SoundManager.getInstance();
        soundManager.check();
    }

    public void startReload() {
        reload = reloadSpeed; // Or the starting value for reload
        hasReloadCompleted = false; // Reset the flag
    }

    private Color determineReloadBarColor() {
        int reloadBarWidth = healthBarTextureRegion.getRegionWidth();
        float reloadProgress = (reloadBarWidth / reloadSpeed * reload);
        if (reloadProgress <= reloadBarWidth / 2.5) {
            return Color.GREEN;
        } else if (reloadProgress <= reloadBarWidth / 1.5) {
            return Color.GOLD;
        } else {
            return Color.FIREBRICK;
        }
    }

    private Color determineHealthBarColor() {
        if (currentHealth <= maxHealth / 3) {
            return Color.RED;
        } else if (currentHealth <= maxHealth / 2) {
            return Color.YELLOW;
        } else {
            return Color.GREEN;
        }
    }

    public void setHit(boolean hit, float damage) {
        isHit = hit;
        if (!name.equals("Player"))
            moveSpeed = Math.max((moveSpeed - (0.25f * moveSpeed)), 0);

        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }

        if (hitAnimationLeft != null && name != "torpedo") {
            if (horizontalDirection == "R") {
                setOverrideAnimation(hitAnimationRight);
            } else {
                setOverrideAnimation(hitAnimationLeft);
            }
        }
    }

    public void setSurfacingAnimation() {
        if (horizontalDirection == "R") {
            setOverrideAnimation(surfacingAnimationRight);
        } else {
            setOverrideAnimation(surfacingAnimationLeft);
        }
        stateTime = 0;
    }

    public Rectangle getBounding() {
        // Get the actor's position and size
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        rotate();

        // Create and return the bounding rectangle
        if (name == "torpedo") {
            return new Rectangle(x + 5, y, width - 10, height - 1);
        } else if (name.equals("Player")) {
            return new Rectangle(x, y, width, height - 5);
        } else if (isSub) {
            return new Rectangle(x, y + 9, width, height - 20);
        } else if (name == "depthCharge") {
            return new Rectangle(x + 4, y + 5, width - 8 , height - 10 );
        } else {
            return new Rectangle(x, y, width, height - 15);
        }
    }

    public void rotate() {
        float centerX = getX() + getWidth() / 2;
        float centerY = getY() + getHeight() / 2;

        setOriginX(getWidth() / 2);
        setOriginY(getHeight() / 2);

        float x = centerX - getOriginX();
        float y = centerY - getOriginY();

        Vector3 transformedPosition = new Vector3(x, y, 0);

        setX(transformedPosition.x);
        setY(transformedPosition.y);
    }


    private Animation<TextureRegion> invertHorizontal(Animation<TextureRegion> originalAnimation) {
        TextureRegion[] originalFrames = originalAnimation.getKeyFrames();
        int frameCount = originalFrames.length;
        TextureRegion[] flippedFrames = new TextureRegion[frameCount];

        // Clone the original frames and flip each one horizontally
        for (int i = 0; i < frameCount; i++) {
            TextureRegion originalFrame = originalFrames[i];
            TextureRegion flippedFrame = new TextureRegion(originalFrame); // Clone the original frame
            flippedFrame.flip(true, false); // Flip horizontally
            flippedFrames[i] = flippedFrame;
        }
        // Create the inverted animation using the flipped frames
        return new Animation<>(originalAnimation.getFrameDuration(), flippedFrames);
    }

    public void isSunk(boolean sunk) {
        this.sunk = sunk;
    }

    public boolean isSunk() {
        return sunk;
    }

    public void moveLeft(float distance) {
        if (collision) return;
        setX(getX() - distance * deltaTime);
    }

    public void moveRight(float distance) {
        if (collision) return;
        setX(getX() + distance * deltaTime);
    }

    public void moveUp(float distance) {
        if (collision) return;
        setY(getY() + distance * deltaTime);
    }

    public void moveDown(float distance) {
        if (collision) return;
        setY(getY() - distance * deltaTime);
    }

    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setReload(float reload) {
        this.reload = reload;
    }

    public void setReloadSpeed(float reloadSpeed) {
        this.reloadSpeed = reloadSpeed;
    }

    public float getReloadSpeed() {
        return reloadSpeed;
    }

    public Vector2 getPosition() {
        return new Vector2(getX(), getY());
    }

    public void setPosition(Vector2 movementVector) {
        // Apply the movement
        float deltaTime = Gdx.graphics.getDeltaTime();
        setX(getX() + movementVector.x * moveSpeed * deltaTime);
        setY(getY() + movementVector.y * moveSpeed * deltaTime);
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }

    public float getMoveSpeed() {
        return !isParked() ? moveSpeed : 0;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isParked() {
        return parked;
    }

    public void setParked(boolean parked) {
        this.parked = parked;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }

    public String getHorizontalDirection() {
        return horizontalDirection;
    }

    @Override
    public boolean remove() {
        dispose();
        return super.remove();
    }

    public void dispose() {
        if (this.name == "Player")
            return;
        if (idleAnimationLeft != null) disposeAnimation(idleAnimationLeft);
        if (moveAnimationLeft != null) disposeAnimation(moveAnimationLeft);
        if (upAnimationLeft != null) disposeAnimation(upAnimationLeft);
        if (downAnimationLeft != null) disposeAnimation(downAnimationLeft);
        if (hitAnimationLeft != null) disposeAnimation(hitAnimationLeft);
        if (sunkAnimationLeft != null) disposeAnimation(sunkAnimationLeft);

        idleAnimationLeft = null;
        moveAnimationLeft = null;
        upAnimationLeft = null;
        downAnimationLeft = null;
        hitAnimationLeft = null;
        sunkAnimationLeft = null;

        if (healthBarTextureRegion != null && healthBarTextureRegion.getTexture() != null) {
            healthBarTextureRegion.getTexture().dispose();
            healthBarTextureRegion = null;
        }

//        if (shapeRenderer != null) {
//            shapeRenderer.dispose();
//            shapeRenderer = null;
//        }
    }


    public void setCollision(boolean state) {
        collision = state;
    }


    private void disposeAnimation(Animation<TextureRegion> animation) {
        for (TextureRegion frame : animation.getKeyFrames()) {
            if (frame.getTexture() != null) {
                frame.getTexture().dispose();
            }
        }
    }
}

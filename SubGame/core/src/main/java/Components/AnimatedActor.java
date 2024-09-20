package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import utilities.SoundManager;

import java.util.Objects;

import static utilities.Constants.Game.*;

public class AnimatedActor extends Actor implements Pausable {
    private static final float FADE_DURATION = 6f;
    private float moveSpeed = 10.5f;
    private float damage = 0;

    private final String name;
    private Animation<TextureRegion> idleAnimation;
    private Animation<TextureRegion> moveAnimation;
    private Animation<TextureRegion> upAnimation;
    private Animation<TextureRegion> downAnimation;
    private Animation<TextureRegion> hitAnimation;
    private Animation<TextureRegion> sunkAnimation;
    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion> surfacingAnimation;
    private Animation<TextureRegion> turningAnimation;
    private Animation<TextureRegion> tempAnimation;
    private TextureRegion healthBarTextureRegion;
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
    private String horizontalDirection;
    private boolean paused = false;
    private boolean parked = false;
    private float angle;

    private boolean overrideActive = false;
    private Animation<TextureRegion> overrideAnimation = null;
    private final boolean isSub;
    private boolean hasReloadCompleted = false;

    private boolean surfaceAnimationPlayed = false;
    private boolean reversed = false;
    private float triggerUp = VIRTUAL_HEIGHT - SKY_SIZE - 45;
    private float triggerDown = VIRTUAL_HEIGHT - SKY_SIZE - 45;

    private String previousHorizontalDirection;
    private boolean isTurning = false;
    private boolean isChangingDepth = false;

    private String newHorizontalDirection;

    private String previousVerticalDirection;
    private String verticalDir;


    // collision
    private boolean collision;

//    private ShapeRenderer shapeRenderer;

    public AnimatedActor(String name,
                         Animation<TextureRegion> idleAnimation,
                         Animation<TextureRegion> moveAnimation,
                         Animation<TextureRegion> upAnimation,
                         Animation<TextureRegion> downAnimation,
                         Animation<TextureRegion> hitAnimation,
                         Animation<TextureRegion> sunkAnimation,
                         Animation<TextureRegion> surfacingAnimation,
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
        this.idleAnimation = idleAnimation;
        this.moveAnimation = moveAnimation;
        this.upAnimation = upAnimation;
        this.downAnimation = downAnimation;
        this.hitAnimation = hitAnimation;
        this.sunkAnimation = sunkAnimation;
        this.surfacingAnimation = surfacingAnimation;
        this.reload = reload;
        this.maxHealth = maxHealth;
        this.isSub = isSub;
        this.currentHealth = maxHealth;

        this.isHit = false;
        this.sunk = false;

        this.reloadSpeed = 0;

        this.angle = 0;

        setWidth(frameWidth);
        setHeight(frameHeight);

        // Set initial position
        setPosition(spawnPosX, spawnPosY);
        this.previousX = getX();
        this.previousY = getY();
        this.horizontalDirection = "L";
        this.previousHorizontalDirection = this.horizontalDirection;
        this.previousVerticalDirection = "idle";

        // Set the initial animation
        tempAnimation = idleAnimation;
        currentAnimation = idleAnimation;

        // Set collision initial state
        collision = false;
//        shapeRenderer = new ShapeRenderer();
        healthBarTextureRegion = new TextureRegion(new Texture("health_bar.png"));
        healthBarTextureRegion.setRegionWidth((int) frameWidth);
        if (name == "Player") {
            toBack();
        }
    }

    public AnimatedActor(String name,
                         Animation<TextureRegion> idleAnimation,
                         Animation<TextureRegion> moveAnimation,
                         Animation<TextureRegion> upAnimation,
                         Animation<TextureRegion> downAnimation,
                         Animation<TextureRegion> hitAnimation,
                         Animation<TextureRegion> sunkAnimation,
                         Animation<TextureRegion> surfacingAnimation,
                         Animation<TextureRegion> turningAnimation,
                         float reload,
                         float maxHealth,
                         float frameWidth,
                         float frameHeight,
                         float spawnPosX,
                         float spawnPosY,
                         boolean isSub
    ) {
        this(name, idleAnimation, moveAnimation, upAnimation, downAnimation, hitAnimation, sunkAnimation, surfacingAnimation, reload, maxHealth, frameWidth, frameHeight, spawnPosX, spawnPosY, isSub);
        this.turningAnimation = turningAnimation;
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

        previousVerticalDirection = verticalDir;

        if (getY() > previousY) {
            verticalDir = "up";
        } else if (getY() < previousY) {
            verticalDir = "down";
        } else {
            verticalDir = "idle";
        }

        if (!name.equals("torpedo") && !name.equals("depthCharge")) {
            // Check if the direction has changed
            if (!newHorizontalDirection.equals(previousHorizontalDirection)) {
                // Direction has changed, play turning animation
                isTurning = true;
                horizontalDirection = newHorizontalDirection;

                // Set the turning animation, invert if turning right
                if (horizontalDirection.equals("L")) {
                    tempAnimation = invertHorizontal(turningAnimation);
                    stateTime = 0;
                } else {
                    tempAnimation = turningAnimation;
                   stateTime = 0;
                }
                // Set overrideAnimation to tempAnimation
                setOverrideAnimation(tempAnimation);
                previousHorizontalDirection = horizontalDirection; // Update previousDirection
                return; // Exit early to prevent further processing
            }
        }


//        if (!this.name.equals("Player")) {
//            if (getX() > previousX ) {
//                //horizontalDirection = "R";
//                tempAnimation = moveAnimation;
//            } else if (getX() < previousX) {
//                //horizontalDirection = "L";
//                tempAnimation = moveAnimation;
//            } else if (getX() == previousX && getY() == previousY) {
//                tempAnimation = idleAnimation;
//            }
//
//            if (verticalDir.equals("up") && !previousVerticalDirection.equals(verticalDir)) {
//                tempAnimation = upAnimation;
//                stateTime = 0;
//            } else if (verticalDir.equals("down") && !previousVerticalDirection.equals(verticalDir)) {
//                tempAnimation = downAnimation;
//                stateTime = 0;
//            }
//
//            if (isHit) {
//                tempAnimation = hitAnimation;
//            } else if (currentHealth == 0) {
//                tempAnimation = sunkAnimation;
//            }
//        } else {
        if (!isTurning && !isChangingDepth) {
            if (getX() > previousX || getX() < previousX) {
                tempAnimation = moveAnimation;
            } else if (getX() == previousX && getY() == previousY) {
                tempAnimation = idleAnimation;
            }

            if (verticalDir.equals("up") && !previousVerticalDirection.equals(verticalDir)) {
                tempAnimation = upAnimation;
            } else if (verticalDir.equals("down") && !previousVerticalDirection.equals(verticalDir)) {
                tempAnimation = downAnimation;
            }

            if (isHit) {
                tempAnimation = hitAnimation;
            } else if (currentHealth == 0) {
                tempAnimation = sunkAnimation;
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
            }
            if (currentAnimation.isAnimationFinished(stateTime)) {
                overrideActive = false;
                overrideAnimation = null;
                isTurning = false;
                isChangingDepth = false;
                //updateMovementState(); // Ensure the movement state gets reflected correctly after override
                if (reversed) {
                    toBack();
                }
            }
        } else {
            if (tempAnimation == null)
                return;
            if (horizontalDirection.equals("R") && (!name.equals("torpedo") && !name.equals("depthCharge") && (!Objects.equals(currentAnimation, tempAnimation)))) {
                tempAnimation = invertHorizontal(tempAnimation);
                currentAnimation = tempAnimation;
                if (name.equals("Player"))
                    stateTime = 0;
            } else if (currentAnimation != tempAnimation) {
                currentAnimation = tempAnimation;
                if (name.equals("Player"))
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
            loops = (tempAnimation != sunkAnimation && tempAnimation != hitAnimation && tempAnimation != surfacingAnimation && tempAnimation != turningAnimation && tempAnimation != upAnimation && tempAnimation != downAnimation);
        } else {
            loops = (tempAnimation == idleAnimation || tempAnimation == moveAnimation || horizontalDirection == "R");
            if (name.equals("enemy1"))
                System.out.println(loops);
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

//        // Draw bounding rectangle for debugging
//        batch.end();
//
//        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
//        shapeRenderer.setColor(Color.RED);
//
//        Rectangle boundingRectangle = getBounding();
//        shapeRenderer.rect(boundingRectangle.x, boundingRectangle.y, boundingRectangle.width, boundingRectangle.height);
//
//        shapeRenderer.end();
//        batch.begin();
    }


    public void surface() {
        if ((previousY < triggerUp && getY() >= triggerUp) && !surfaceAnimationPlayed) {
            //surfacingAnimation.setFrameDuration(surfacingAnimation.getAnimationDuration() / (moveSpeed - 5f));
            surfacingAnimation.setPlayMode(Animation.PlayMode.NORMAL);
            stateTime = 0;
            setSurfacingAnimation();
            surfaceAnimationPlayed = true; // Ensure the animation only plays once per crossing
            reversed = false;
            toFront();
        } else if ((previousY > triggerDown && getY() <= triggerDown) && !surfaceAnimationPlayed) {
            //surfacingAnimation.setFrameDuration(surfacingAnimation.getAnimationDuration() / (moveSpeed - 5f));
            // surfacingAnimation.setPlayMode(Animation.PlayMode.REVERSED);
            stateTime = 0;
            setSurfacingAnimation();
            surfaceAnimationPlayed = true; // Ensure the animation only plays once per crossing
            reversed = true;
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

        if (hitAnimation != null && name != "torpedo") {
            if (horizontalDirection == "R") {
                setOverrideAnimation(invertHorizontal(hitAnimation));
            } else {
                setOverrideAnimation(hitAnimation);
            }
        }
    }

    public void setSurfacingAnimation() {
        if (horizontalDirection == "R") {
            currentAnimation = invertHorizontal(surfacingAnimation);
            setOverrideAnimation(currentAnimation);
        } else {
            setOverrideAnimation(surfacingAnimation);
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
        } else {
            return new Rectangle(x, y, width, height);
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
        setX(getX() + movementVector.x * moveSpeed * deltaTime);  // Multiply by speed and delta to adjust movement speed
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
        if (idleAnimation != null) disposeAnimation(idleAnimation);
        if (moveAnimation != null) disposeAnimation(moveAnimation);
        if (upAnimation != null) disposeAnimation(upAnimation);
        if (downAnimation != null) disposeAnimation(downAnimation);
        if (hitAnimation != null) disposeAnimation(hitAnimation);
        if (sunkAnimation != null) disposeAnimation(sunkAnimation);

        idleAnimation = null;
        moveAnimation = null;
        upAnimation = null;
        downAnimation = null;
        hitAnimation = null;
        sunkAnimation = null;

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

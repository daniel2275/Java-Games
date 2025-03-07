package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import utilities.SoundManager;

import static utilities.Settings.Game.*;
import static utilities.LoadSave.invertHorizontal;

public abstract class BaseActor extends Actor implements Pausable {
    private static final float FADE_DURATION = 6f;
    private float moveSpeed = 10.5f;
    private float stoppedSpeed = 10.5f;
    private boolean turnStop = false;
    private float damage = 0;
    private String name;
    private boolean aggroed = false;

    // Left-facing animations
    private Animation<TextureRegion> idleAnimationLeft;

    private Animation<TextureRegion> moveAnimationLeft;
    private Animation<TextureRegion> upAnimationLeft;
    private Animation<TextureRegion> downAnimationLeft;
    private Animation<TextureRegion> hitAnimationLeft;
    private Animation<TextureRegion> sunkAnimationLeft;
    private Animation<TextureRegion> surfacingAnimationLeft;
    protected Animation<TextureRegion> turningAnimationLeft;

    // Right-facing animations
    private Animation<TextureRegion> idleAnimationRight;

    private Animation<TextureRegion> moveAnimationRight;
    private Animation<TextureRegion> upAnimationRight;
    private Animation<TextureRegion> downAnimationRight;
    private Animation<TextureRegion> hitAnimationRight;
    private Animation<TextureRegion> sunkAnimationRight;
    private Animation<TextureRegion> surfacingAnimationRight;
    protected Animation<TextureRegion> turningAnimationRight;
    //private Animation<TextureRegion> turningAnimationRightReversed;

    // Current and temporary animations
    private Animation<TextureRegion> currentAnimation;
    protected Animation<TextureRegion> tempAnimation;

    // Health bar texture
    private TextureRegion healthBarTextureRegion;

    // Movement and state tracking
    protected float stateTime;
    private float previousX;
    private float previousY;
    private float reload;
    private float reloadSpeed;
    private float maxHealth;
    private float currentHealth;
    private float deltaTime = 0;
    private boolean isHit;
    private boolean sunk;
    private boolean paused = false;
    private boolean parked = false;
    private float angle;

    // Override animation
    protected boolean overrideActive = false;
    private Animation<TextureRegion> overrideAnimation = null;

    private boolean hasReloadCompleted = false;

    // Surfacing and reversing flags
    private boolean surfaceAnimationPlayed = false;
    private boolean reversed = false;
    private float triggerSurfaceDive = VIRTUAL_HEIGHT - SKY_SIZE - 36;

    // Direction tracking
    protected String previousHorizontalDirection;
    protected boolean isTurning = false;
    private boolean isChangingDepth = false;
    protected String newHorizontalDirection;
    protected String previousVerticalDirection;
    protected String horizontalDirection;
    private String verticalDirection;

    // Collision state
    private boolean collision;

    // Turn buffer value
    protected static final float TURN_BUFFER = 10f;
    protected float firstChangeX = -1;

    // Random Movement
    private boolean randomMovement = false;
    //private ShapeRenderer shapeRenderer;

    // Player Constructor
    public BaseActor(
        String name,
        Animation<TextureRegion> idleAnimationLeft,
        Animation<TextureRegion> moveAnimationLeft,
        Animation<TextureRegion> upAnimationLeft,
        Animation<TextureRegion> downAnimationLeft,
        Animation<TextureRegion> hitAnimationLeft,
        Animation<TextureRegion> sunkAnimationLeft,
        Animation<TextureRegion> surfacingAnimationLeft,
        Animation<TextureRegion> turningAnimationLeft,
        Animation<TextureRegion> idleAnimationRight,
        Animation<TextureRegion> moveAnimationRight,
        Animation<TextureRegion> upAnimationRight,
        Animation<TextureRegion> downAnimationRight,
        Animation<TextureRegion> hitAnimationRight,
        Animation<TextureRegion> sunkAnimationRight,
        Animation<TextureRegion> surfacingAnimationRight,
        Animation<TextureRegion> turningAnimationRight,
        float reload,
        float maxHealth,
        float frameWidth,
        float frameHeight,
        float spawnPosX,
        float spawnPosY
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
        this.turningAnimationLeft = turningAnimationLeft;


        // Assign right-facing animations
        this.idleAnimationRight = idleAnimationRight;
        this.moveAnimationRight = moveAnimationRight;
        this.upAnimationRight = upAnimationRight;
        this.downAnimationRight = downAnimationRight;
        this.hitAnimationRight = hitAnimationRight;
        this.sunkAnimationRight = sunkAnimationRight;
        this.surfacingAnimationRight = surfacingAnimationRight;
        this.turningAnimationRight = turningAnimationRight;


        // Reload and health setup
        this.reload = reload;
        this.maxHealth = maxHealth;
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


        //  shapeRenderer = new ShapeRenderer();
    }

    // Enemies Constructor
    public BaseActor(
        String name,
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
        boolean randomMovement
    ) {
        this.name = name;
        setName(name);

        // Assign left-facing animations
        this.idleAnimationLeft = idleAnimationLeft;
        this.moveAnimationLeft = moveAnimationLeft;
        this.hitAnimationLeft = hitAnimationLeft;
        this.sunkAnimationLeft = sunkAnimationLeft;
        this.turningAnimationLeft = turningAnimationLeft;


        // Assign right-facing animations
        this.idleAnimationRight = idleAnimationRight;
        this.moveAnimationRight = moveAnimationRight;
        this.hitAnimationRight = hitAnimationRight;
        this.sunkAnimationRight = sunkAnimationRight;
        this.turningAnimationRight = turningAnimationRight;


        // Reload and health setup
        this.reload = reload;
        this.maxHealth = maxHealth;
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

        // Set random movement
        this.randomMovement = randomMovement;
        // shapeRenderer = new ShapeRenderer();
    }

    // depth charge constructor
    public BaseActor(
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
        float spawnPosY
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

        //shapeRenderer = new ShapeRenderer();
    }


    @Override
    public void act(float delta) {
        if (currentAnimation == null)
            return;

        if (!paused) {
            super.act(delta);

            if (collision) collisionReaction();


            updateMovementState();
            updateAnimation();

            surface();

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


    @SuppressWarnings("SuspiciousIndentation")
    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (currentAnimation == null)
        return;

        // If sunk, gradually fade out the actor
        if (currentHealth == 0) fadeActor(batch);

        if (aggroed) {
            batch.setColor(1, 0, 0, 1f);
        } else {
            batch.setColor(1, 1, 1, 1);
        }

        // Draw the regular current animation
        batch.draw(currentAnimation.getKeyFrame(stateTime), getX(), getY(),
            getOriginX(), getOriginY(), getWidth(), getHeight(), 1f, 1f, angle);


        drawHealthBar(batch);
        drawReloadBar(batch);

        if (!overrideActive) {
            surfaceAnimationPlayed = false;
        }

        isHit = false;
        // Reset the batch color to fully opaque
        batch.setColor(1, 1, 1, 1);

//        // Draw bounding rectangle for debugging
//        batch.end();
//
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

    private void collisionReaction() {
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
        if (reversed && (overrideAnimation == surfacingAnimationRight || overrideAnimation == surfacingAnimationLeft)) {
            overrideAnimation.setPlayMode(Animation.PlayMode.REVERSED);
            reversed = false;
        } else if (overrideAnimation != null) {
            overrideAnimation.setPlayMode(Animation.PlayMode.NORMAL);
        }

        stateTime = 0;
        overrideActive = true;
    }

    public void handleSinking() {
        addAction(Actions.sequence(
            Actions.parallel(
                Actions.delay(4.0f)
            ),
            Actions.removeActor()
        ));
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

        if (directionChanged()) return;

        if (isTurning && !turnStop) {
            stoppedSpeed = moveSpeed;
            moveSpeed = 0;
            turnStop = true;
        } else if (!isTurning && turnStop) {
            turnStop = false;
            moveSpeed = stoppedSpeed;
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
        previousHorizontalDirection = horizontalDirection; // Update previousDirection
    }

//    public boolean directionChanged() {
//        // Calculate the horizontal movement distance
//        float movementDistance = Math.abs(getX() - previousX);
//        // Check if the movement exceeds the buffer zone
//        if (movementDistance < TURN_BUFFER) {
//            return false; // Don't trigger the turn if the movement is within the buffer
//        }
//
//        // Check if the direction has changed
//        if (!overrideActive) {
//            if (!newHorizontalDirection.equals(previousHorizontalDirection)) {
//                // Direction has changed, play turning animation
//                isTurning = true;
//                horizontalDirection = newHorizontalDirection;
//
//                // Set the turning animation
//                if (horizontalDirection.equals("L")) {
//                    tempAnimation = turningAnimationRight;
//                } else {
//                    tempAnimation = turningAnimationLeft;
//                }
//                // Set overrideAnimation to tempAnimation
//                stateTime = 0;
//                setOverrideAnimation(tempAnimation);
//                previousHorizontalDirection = horizontalDirection; // Update previousDirection
//                return true;
//            }
//        }
//        return false;
//    }


    // To store the initial position where direction changed

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
            // If firstChangeX has been set, trigger the turning animation immediately
            if (firstChangeX != -1) {
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
        return false;
    }


    public void updateAnimationSpeedBasedOnMoveSpeed() {
        if (currentAnimation != null && currentAnimation != turningAnimationLeft && currentAnimation != turningAnimationRight) {
            // Define the target frame duration for max and min speeds
            float minFrameDuration = 0.2f; // 0.2 seconds per frame at 1f speed
            float maxFrameDuration = 0.09f / 9.0f; // ~0.0044 seconds per frame at 60f speed

            // Scale the frame duration based on the move speed but ensure a minimum speed of 1f
            // If moveSpeed is below 1f, we want to treat it as 1f
            float safeMoveSpeed = Math.max(moveSpeed, 1.0f);

            // Calculate the scaled frame duration
            // Interpolate between maxFrameDuration and minFrameDuration based on moveSpeed
            float scaledFrameDuration = maxFrameDuration + (minFrameDuration - maxFrameDuration) * (1f - (safeMoveSpeed - 1f) / (60f - 1f));

            // Ensure the frame duration does not exceed the min frame duration
            scaledFrameDuration = Math.min(scaledFrameDuration, minFrameDuration);

            // Set the frame duration
            currentAnimation.setFrameDuration(scaledFrameDuration);
        }
    }


    private void updateAnimation() {
        if (overrideActive) {
            currentAnimation = overrideAnimation;
            updateAnimationSpeedBasedOnMoveSpeed();


            // Check if the turning animation has finished playing
            if (currentAnimation.isAnimationFinished(stateTime)) {
                if (isTurning) {
                    isTurning = false;  // After the reversed animation is set, stop the turning flag
                } else {
                    // Reset everything after the reversed animation plays
                    overrideActive = false;
                    overrideAnimation = null;
                    isTurning = false;
                    reversed = false;
                    if (reversed) {
                        toBack();
                    }
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


    public void fadeActor(Batch batch) {
        float alpha = Math.max(0, 1 - (stateTime / FADE_DURATION));
        batch.setColor(1, 1, 1, alpha);
    }

    public void surface() {
        if (!overrideActive || isTurning) {
            if ((previousY < triggerSurfaceDive && getY() >= triggerSurfaceDive) && !surfaceAnimationPlayed) {
                stateTime = 0;
                reversed = false;
                setSurfacingAnimation();
                surfaceAnimationPlayed = true; // Ensure the animation only plays once per crossing
                //toFront();
            } else if ((previousY > triggerSurfaceDive && getY() <= triggerSurfaceDive) && !surfaceAnimationPlayed) {
                stateTime = 0;
                reversed = true;
                setSurfacingAnimation();
                surfaceAnimationPlayed = true; // Ensure the animation only plays once per crossing
                //toBack();
            }
        }
    }

    // Method to check collision with another actor
    public boolean collidesWith(Components.BaseActor otherActor) {
        Rectangle myRect = getBounding();
        Rectangle otherRect = otherActor.getBounding();
        return myRect.overlaps(otherRect);
    }

    public void drawHealthBar(Batch batch) {
        if (currentHealth > 0) {
            Color healthBarColor = determineHealthBarColor();
            batch.setColor(healthBarColor);

            float healthBarWidth = healthBarTextureRegion.getRegionWidth();
            float healthBarForegroundWidth = (currentHealth / maxHealth) * healthBarWidth;

            batch.draw(healthBarTextureRegion, getX(), getY() + getHeight() + 3, healthBarForegroundWidth, 2);
        }
        batch.setColor(Color.WHITE);
    }

    public void drawReloadBar(Batch batch) {
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
        reload = reloadSpeed; //  starting value for reload
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

        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }

        hitDirection();
    }

    public void hitDirection() {
        if (horizontalDirection == "R") {
            setOverrideAnimation(hitAnimationRight);
        } else {
            setOverrideAnimation(hitAnimationLeft);
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
        return new Rectangle(x, y, width, height - 15);
    }


//    public Rectangle rotate() {
////        float centerX = getX() + getWidth() / 2;
////        float centerY = getY() + getHeight() / 2;
////
////        setOriginX(getWidth() / 2);
////        setOriginY(getHeight() / 2);
////
////        float x = centerX - getOriginX();
////        float y = centerY - getOriginY();
////
////        Vector3 transformedPosition = new Vector3(x, y, 0);
////
////        setX(transformedPosition.x);
////        setY(transformedPosition.y);
//        setOrigin(getWidth() / 2, getHeight() / 2);
//
//        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);
//        Rectangle boundingRect = new Rectangle(
//            getX(),               // The x-position of the actor
//            getY(),               // The y-position of the actor
//            currentFrame.getRegionWidth(),
//            currentFrame.getRegionHeight()
//        );
//
//        Polygon polygon = new Polygon(new float[]{
//            0, 0,                                            // bottom-left
//            currentFrame.getRegionWidth(), 0,                // bottom-right
//            currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),  // top-right
//            0, currentFrame.getRegionHeight()                // top-left
//        });
//
//// Set the position of the polygon to match the actor's position
//        polygon.setPosition(getX(), getY());
//
//// Set the origin of the polygon (usually the center for rotation)
//        polygon.setOrigin(currentFrame.getRegionWidth() / 2f, currentFrame.getRegionHeight() / 2f);
//
//// Apply rotation in degrees
//        polygon.setRotation(angle); // rotationAngle is in degrees
//
//        return polygon.getBoundingRectangle();
//    }


    public Rectangle rotate() {
        // Set the origin to the center of the sprite for rotation
        setOrigin(getWidth() / 2, getHeight() / 2);

        // Get the current frame of the animation
        TextureRegion currentFrame = currentAnimation.getKeyFrame(stateTime, true);

        // Create a polygon that represents the sprite's current state
        Polygon polygon = new Polygon(new float[]{
            0, 0,                                            // bottom-left
            currentFrame.getRegionWidth(), 0,                // bottom-right
            currentFrame.getRegionWidth(), currentFrame.getRegionHeight(),  // top-right
            0, currentFrame.getRegionHeight()                // top-left
        });

        // Set the position of the polygon to match the actor's position
        polygon.setPosition(getX(), getY());

        // Set the origin of the polygon to its center
        polygon.setOrigin(currentFrame.getRegionWidth() / 2f, currentFrame.getRegionHeight() / 2f);

        // Apply rotation to the polygon
        polygon.setRotation(angle); // angle is in degrees

        // Now calculate the position of the tip of the sprite, considering the rotation.
        // The "tip" is usually at the middle of the top edge of the sprite, or wherever "front" is defined.

        float tipX = getX() + (getWidth() / 2) + (float) Math.cos(Math.toRadians(angle)) * (getWidth() / 2);
        float tipY = getY() + (getHeight() / 2) + (float) Math.sin(Math.toRadians(angle)) * (getHeight() / 2);

        // Create the small 2x2 rectangle at the tip of the sprite, following the rotation
        Rectangle smallRect = new Rectangle(tipX - 1, tipY - 1, 2, 2); // The 2x2 rectangle, centered on the tip

        return smallRect;

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

    public void setStateTime(float stateTime) {
        this.stateTime = stateTime;
    }

    public boolean isRandomMovement() {
        return randomMovement;
    }

    @Override
    public boolean remove() {
        //dispose();
        return super.remove();
    }

    //   public void dispose() {
//        if (idleAnimationLeft != null) disposeAnimation(idleAnimationLeft);
//        if (moveAnimationLeft != null) disposeAnimation(moveAnimationLeft);
//        if (upAnimationLeft != null) disposeAnimation(upAnimationLeft);
//        if (downAnimationLeft != null) disposeAnimation(downAnimationLeft);
//        if (hitAnimationLeft != null) disposeAnimation(hitAnimationLeft);
//        if (sunkAnimationLeft != null) disposeAnimation(sunkAnimationLeft);
//
//        idleAnimationLeft = null;
//        moveAnimationLeft = null;
//        upAnimationLeft = null;
//        downAnimationLeft = null;
//        hitAnimationLeft = null;
//        sunkAnimationLeft = null;
//
//        if (healthBarTextureRegion != null && healthBarTextureRegion.getTexture() != null) {
//            healthBarTextureRegion.getTexture().dispose();
//            healthBarTextureRegion = null;
//        }
//
////        if (shapeRenderer != null) {
////            shapeRenderer.dispose();
////            shapeRenderer = null;
////        }
//    }


    public void setCollision(boolean state) {
        collision = state;
    }

    public void setAggroState(boolean aggroed) {
        this.aggroed = aggroed;
    }

    public boolean isAggroed() {
        return aggroed;
    }

//    private void disposeAnimation(Animation<TextureRegion> animation) {
//        for (TextureRegion frame : animation.getKeyFrames()) {
//            if (frame.getTexture() != null) {
//                frame.getTexture().dispose();
//            }
//        }


}


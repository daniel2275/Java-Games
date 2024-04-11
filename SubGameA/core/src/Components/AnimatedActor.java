package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import utilities.LoadSave;

import java.util.Objects;

public class AnimatedActor extends Actor {
    private static final float FADE_DURATION = 6f;
    private static final float DEFAULT_SPEED = 10.5f;

    private String name;
    private Animation<TextureRegion> idleAnimation, moveAnimation, upAnimation, downAnimation, hitAnimation, sunkAnimation, currentAnimation, tempAnimation;
    private TextureRegion healthBarTextureRegion;
    private float stateTime, previousX, previousY, reload, reloadSpeed, maxHealth, currentHealth, speed = DEFAULT_SPEED, deltaTime = 0;
    private boolean isHit, sunk, loops, killed;
    private String direction;

    private float angle;

    public AnimatedActor(String name,
                         Animation<TextureRegion> idleAnimation,
                         Animation<TextureRegion> moveAnimation,
                         Animation<TextureRegion> upAnimation,
                         Animation<TextureRegion> downAnimation,
                         Animation<TextureRegion> hitAnimation,
                         Animation<TextureRegion> sunkAnimation,
                         float reload,
                         float maxHealth,
                         float frameWidth,
                         float frameHeight,
                         float spawnPosX,
                         float spawnPosY
    ) {
        this.name = name;
        setName(name);
        this.idleAnimation = idleAnimation;
        this.moveAnimation = moveAnimation;
        this.upAnimation = upAnimation;
        this.downAnimation = downAnimation;
        this.hitAnimation = hitAnimation;
        this.sunkAnimation = sunkAnimation;
        this.reload = reload;
        this.maxHealth = maxHealth;

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
        this.direction = "L";

        // Set the initial animation
        tempAnimation = idleAnimation;
        currentAnimation = idleAnimation;

        // In your constructor or initialization method
        healthBarTextureRegion = new TextureRegion(new Texture("health_bar.png"));
        healthBarTextureRegion.setRegionWidth((int) frameWidth);
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        updateMovementState();
        updateAnimation();
        previousX = getX();
        previousY = getY();
        deltaTime = delta;
        stateTime += delta;
    }

    private void updateMovementState() {
        if (getX() > previousX) {
            direction = "R";
            tempAnimation = moveAnimation;
        } else if (getX() < previousX) {
            direction = "L";
            tempAnimation = moveAnimation;
        } else if (getX() == previousX && getY() == previousY) {
            tempAnimation = idleAnimation;
        }

        if (getY() > previousY) {
            tempAnimation = upAnimation; // Override only if needed
        } else if (getY() < previousY) {
            tempAnimation = downAnimation; // Override only if needed
        }

        if (isHit) {
            tempAnimation = hitAnimation;
        } else if (currentHealth == 0) {
            tempAnimation = sunkAnimation;
        }
    }

    private void updateAnimation() {
        if (Objects.equals(direction, "R") && !name.equals("torpedo")) {
            currentAnimation = LoadSave.invertHorizontal(tempAnimation);
        } else {
            currentAnimation = tempAnimation;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        // If sunk, gradually fade out the actor
        if (currentHealth == 0 && !isSunk()) {
            System.out.println("add actions");
            // Make the label fade out and move up
            addAction(Actions.sequence(
                    Actions.parallel(
                            Actions.fadeOut(1.0f),
                            Actions.moveBy(0,  -50f, 4.0f)
                    ),
                    Actions.removeActor()
            ));
            isSunk(true);
//            moveDown(2);
//            float alpha = Math.max(0, 1 - (stateTime / FADE_DURATION));
//            batch.setColor(1, 1, 1, alpha);
        }

        // Define looping animation
        loops = (tempAnimation == idleAnimation || tempAnimation == moveAnimation);

        // Draw the current animation with SpriteBatch
        //batch.draw(currentAnimation.getKeyFrame(stateTime, loops), getX(), getY());

        batch.draw(currentAnimation.getKeyFrame(stateTime, loops), getX(), getY(), 0, 0, getWidth(), getHeight(), 1f, 1f, angle);

        if (!name.equals("torpedo")) {
            drawHealthBar(batch);
            drawReloadBar(batch);
        }

        isHit = false;
    }

    // Method to check collision with another actor
    public boolean collidesWith(AnimatedActor otherActor) {
        Rectangle myRect = getBoundingRectangle();
        Rectangle otherRect = otherActor.getBoundingRectangle();
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

            batch.draw(healthBarTextureRegion, getX(), getY() + getHeight() + 5, reloadBarForegroundWidth, 1);
        }
        batch.setColor(Color.WHITE);
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
        speed = Math.max((speed - (0.25f * speed)),0);

        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
        stateTime = 0; // Reset state time when hit animation starts
    }

    public Rectangle getBoundingRectangle() {
        // Get the actor's position and size
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();

        // Create and return the bounding rectangle
        return new Rectangle(x, y, width, height);
    }

    public boolean isHit() {
        return isHit;
    }

    public void isSunk(boolean sunk) {
        this.sunk = sunk;
    }

    public boolean isSunk() {
        return sunk;
    }

    public void moveLeft(float distance) {
        setX(getX() - distance * deltaTime);
    }

    public void moveRight(float distance) {
        setX(getX() + distance * deltaTime);
    }

    public void moveUp(float distance) {
        setY(getY() + distance * deltaTime);
    }

    public void moveDown(float distance) {
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
        setX(getX() + movementVector.x * speed * deltaTime);  // Multiply by speed and delta to adjust movement speed
        setY(getY() + movementVector.y * speed * deltaTime);
    }

    public boolean killed() {
        return killed;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}

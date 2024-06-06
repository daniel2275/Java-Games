package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import java.util.Objects;

public class AnimatedActor extends Actor implements Pausable {
    private static final float FADE_DURATION = 6f;
    private float moveSpeed = 10.5f;

    private String name;
    private Animation<TextureRegion> idleAnimation, moveAnimation, upAnimation, downAnimation, hitAnimation, sunkAnimation, currentAnimation, tempAnimation;
    private TextureRegion healthBarTextureRegion;
    private float stateTime, previousX, previousY, reload, reloadSpeed, maxHealth, currentHealth, deltaTime = 0;
    private boolean isHit, sunk, loops;
    private String direction;
    private boolean paused = false;

    private float angle;
    private ShapeRenderer shapeRenderer;

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

        shapeRenderer = new ShapeRenderer();

        healthBarTextureRegion = new TextureRegion(new Texture("health_bar.png"));
        healthBarTextureRegion.setRegionWidth((int) frameWidth);
    }

    @Override
    public void act(float delta) {
        if (currentAnimation == null)
            return;

        if (!paused) {
            super.act(delta);

            updateMovementState();
            updateAnimation();
            previousX = getX();
            previousY = getY();
            deltaTime = delta;
            stateTime += delta;

            // If sunk, gradually fade out the actor
            if (currentHealth == 0 && !isSunk()) {
                stateTime = 0;
                // Make the label "?fade out" and move down
                if (!(name.equals("torpedo") || name.equals("depthCharge"))) {
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
        }
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
            tempAnimation = upAnimation;
        } else if (getY() < previousY) {
            tempAnimation = downAnimation;
        }

        if (isHit) {
            tempAnimation = hitAnimation;
        } else if (currentHealth == 0) {
            tempAnimation = sunkAnimation;
        }
    }

    private void updateAnimation() {
        if (tempAnimation == null)
            return;
        if (Objects.equals(direction, "R") && !name.equals("torpedo")) {
            currentAnimation = invertHorizontal(tempAnimation);
        } else {
            currentAnimation = tempAnimation;
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);

        if (currentAnimation == null)
            return;

        // If sunk, gradually fade out the actor
        if (currentHealth == 0 && !(name.equals("torpedo") || name.equals("depthCharge"))) {
            float alpha = Math.max(0, 1 - (stateTime / FADE_DURATION));
            batch.setColor(1, 1, 1, alpha);
        }

        // Define looping animation
        loops = (tempAnimation == idleAnimation || tempAnimation == moveAnimation);

        batch.draw(currentAnimation.getKeyFrame(stateTime, loops), getX(), getY(), getOriginX(), getOriginY(), getWidth(), getHeight(), 1f, 1f, angle);

        if (!name.equals("torpedo") && !name.equals("depthCharge")) {
            drawHealthBar(batch);
            drawReloadBar(batch);
        }

        isHit = false;

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
        moveSpeed = Math.max((moveSpeed - (0.25f * moveSpeed)), 0);

        System.out.println("move updated to:" + moveSpeed);

        currentHealth -= damage;
        if (currentHealth < 0) {
            currentHealth = 0;
        }
        stateTime = 0; // Reset state time when hit animation starts
    }

    public Rectangle getBounding() {
        // Get the actor's position and size
        float x = getX();
        float y = getY();
        float width = getWidth();
        float height = getHeight();
        rotate();

        // Create and return the bounding rectangle
        if (Objects.equals(name, "torpedo")) {
            return new Rectangle(x+5, y, width-10, height-1);
        }else{
            return new Rectangle(x, y, width, height);
        }
    }

//    public Polygon getBounding() {
//        // Get the actor's position and size
//        float x = getX();
//        float y = getY();
//        float width = getWidth();
//        float height = getHeight();
//        if (Objects.equals(name, "torpedo")) {
//            System.out.println("stop");
//        }
//        // Create the polygon with the rectangle's vertices
//        float[] vertices = new float[8];
//        vertices[0] = x;
//        vertices[1] = y;
//        vertices[2] = x + width;
//        vertices[3] = y;
//        vertices[4] = x + width;
//        vertices[5] = y + height;
//        vertices[6] = x;
//        vertices[7] = y + height;
//
//        Polygon polygon = new Polygon(vertices);
//        rotatePolygon(polygon); // Rotate the polygon
//
//        //Rectangle polyRec = polygon.getBoundingRectangle();
//        // Get the bounding rectangle of the rotated polygon
//        return polygon;
//    }

//    private void rotatePolygon(Polygon polygon) {
//        polygon.setOrigin(getOriginX(), getOriginY());
//        polygon.setRotation(angle);
//    }
//
//    public float getPolygonWidth(Polygon polygon) {
//        float[] vertices = polygon.getVertices();
//
//        // Initialize min and max values with the first x-coordinate
//        float minX = vertices[0];
//        float maxX = vertices[0];
//
//        // Iterate through the vertices array (step by 2 since vertices array contains x and y alternately)
//        for (int i = 0; i < vertices.length; i += 2) {
//            float x = vertices[i];
//            if (x < minX) {
//                minX = x;
//            }
//            if (x > maxX) {
//                maxX = x;
//            }
//        }
//
//        // The width of the polygon is the difference between maxX and minX
//        return maxX - minX;
//    }

    public float getPolygonHeight(Polygon polygon) {
        float[] vertices = polygon.getVertices();

        // Initialize min and max values with the first y-coordinate
        float minY = vertices[1];
        float maxY = vertices[1];

        // Iterate through the vertices array (step by 2 since vertices array contains x and y alternately)
        for (int i = 1; i < vertices.length; i += 2) {
            float y = vertices[i];
            if (y < minY) {
                minY = y;
            }
            if (y > maxY) {
                maxY = y;
            }
        }

        // The height of the polygon is the difference between maxY and minY
        return maxY - minY;
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
        return moveSpeed;
    }

    public void setMoveSpeed(float moveSpeed) {
        this.moveSpeed = moveSpeed;
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    @Override
    public boolean remove() {
        dispose();
        return super.remove();
    }


    public void dispose() {
        System.out.println("DISPOSED :" + this.name);
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

        if (shapeRenderer != null) {
            shapeRenderer.dispose();
            shapeRenderer = null;
        }
    }

    private void disposeAnimation(Animation<TextureRegion> animation) {
        for (TextureRegion frame : animation.getKeyFrames()) {
            if (frame.getTexture() != null) {
                frame.getTexture().dispose();
            }
        }
    }
}

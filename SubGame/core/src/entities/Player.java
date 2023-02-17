package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import gamestates.Playing;
import utilz.HelpMethods;

import java.util.Objects;

import static com.danielr.subgame.SubGame.*;
import static utilz.Constants.Game.*;
import static utilz.HelpMethods.drawObject;
import static utilz.LoadSave.boatAnimation;

public class Player {

    public static final int PLAYER_WIDTH = 48;
    public static final int PLAYER_HEIGHT = 16;
    public static final float SPAWN_X = WORLD_WIDTH/2;
    public static final float SPAWN_Y = WORLD_HEIGHT/2;

    private final TextureRegion[][] uBoatSprites =  new TextureRegion[6][6];

    private Animation<TextureRegion> idleAnimations;
    private Animation<TextureRegion> movingAnimations;
    private Animation<TextureRegion> upAnimations;
    private Animation<TextureRegion> downAnimations;
    private Animation<TextureRegion> hitAnimations;
    private Animation<TextureRegion> sunkAnimations;

    private float stateTime;

    private TextureRegion currentFrame;

    private Rectangle hitbox;
    private float playerHealth = 100f;
    private float collisionDamage = 5f;

    private String lastDirection;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean sunk = false;
    private int flipX = 1;

    private int xOffset = 0;

    private Playing playing;

    public Player(Playing playing) {
        this.playing = playing;
        loadAnimations("Uboat-atlas2.png");
        this.hitbox = HelpMethods.initHitBox(SPAWN_X,SPAWN_Y, PLAYER_WIDTH,PLAYER_HEIGHT);
    }

    public void update() {
        if (!pause) {
            checkDirection();
            checkAnimation();
            checkCollision();
        }
        render();
    }

    // load animations (pending: migrate to helper class)
    private void loadAnimations(String sprites) {
        Texture uBoatAtlas = new Texture(sprites);

        for (int i= 0; i <= 5; i++) {
            for (int j= 0; j <= 5; j++) {
                uBoatSprites[i][j] = new TextureRegion(uBoatAtlas, j * 64, i * 16,PLAYER_WIDTH,PLAYER_HEIGHT);
            }
        }

        idleAnimations = boatAnimation(0,5, uBoatSprites, 2.0f);
        movingAnimations = boatAnimation(1,3, uBoatSprites, 0.055f);
        upAnimations = boatAnimation(2,3, uBoatSprites, 0.7f);
        downAnimations = boatAnimation(3,3, uBoatSprites, 0.7f);
        hitAnimations = boatAnimation(4,1, uBoatSprites, 0.7f);
        sunkAnimations = boatAnimation(5,1, uBoatSprites, 0.7f);
    }


    public void render () {
        drawObject(currentFrame, hitbox, -xOffset, 0, flipX, 1, playerHealth);
    }

    // Animate the player character, resets statetime on non-looping animations
    private void checkAnimation() {
        stateTime += Gdx.graphics.getDeltaTime();
        if (sunk) {
            currentFrame = getSunkAnimations().getKeyFrame(stateTime, true);
            return;
        }
        if ((left | right) && !(up || down)) {
            currentFrame = getMovingAnimations().getKeyFrame(stateTime, true);
            lastDirection = "left or right";
        } else if (up) {
            if(!Objects.equals(lastDirection, "up")) {
                stateTime=0;
            }
            currentFrame = getUpAnimations().getKeyFrame(stateTime, false);
            lastDirection = "up";
        } else if (down) {
            if(!Objects.equals(lastDirection, "down")) {
                stateTime=0;
            }
            currentFrame = getDownAnimations().getKeyFrame(stateTime, false);
            lastDirection = "down";
        } else {
            currentFrame = getIdleAnimations().getKeyFrame(stateTime, true);
            lastDirection = "idle";
        }



    }

    private void checkCollision() {
        if (playing.checkCollision(this.hitbox, collisionDamage)) {
            if (playerHealth > 0) {
                playerHealth -= collisionDamage;
            } else {
                sunk = true;
                // game over
            }
        }
    }

    public Animation<TextureRegion> getIdleAnimations() {
        return idleAnimations;
    }

    public void setPlayerHealth(float playerHealth) {
        this.playerHealth = playerHealth;
    }

    public Animation<TextureRegion> getMovingAnimations() {
        return movingAnimations;
    }

    public Animation<TextureRegion> getUpAnimations() {
        return upAnimations;
    }

    public Animation<TextureRegion> getDownAnimations() {
        return downAnimations;
    }

    public float getPlayerHealth() {
        return playerHealth;
    }

    public Animation<TextureRegion> getHitAnimations() {
        return hitAnimations;
    }

    public Animation<TextureRegion> getSunkAnimations() {
        return sunkAnimations;
    }

    public Rectangle getuBoatHitBox() {
        return hitbox;
    }

    public void checkDirection() {
        if (up) {
            if ( hitbox.getY() + PLAYER_HEIGHT < WORLD_HEIGHT - SKY_SIZE + PLAYER_HEIGHT / 2.0f ) {
                hitbox.y++;
            }
        }
        if (down) {
            if ( hitbox.getY() > 1) {
                hitbox.y--;
            }
        }
        if (left) {
            flipX = 1;
            xOffset = 0;
            if (hitbox.getX() > 1) {
                hitbox.x--;
            }
        }
        if (right) {
            flipX = -1;
            xOffset = -PLAYER_WIDTH ;
            if (hitbox.getX() < WORLD_WIDTH + xOffset ) {
                hitbox.x++;
            }
        }

    }

    public int getFlipX() {
        return flipX;
    }

    public void setFlipX(int flipX) {
        this.flipX = flipX;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public ShapeRenderer getShapeRendered() {
        return shapeRendered;
    }

    public String getLastDirection() {
        return lastDirection;
    }

    public String direction() {
        String currentDirection;

        if (left && up) {
            currentDirection = "left&up";
        } else if (left&&down) {
            currentDirection = "left&down";
        } else if (left) {
            currentDirection = "left";
        } else if (right&&up) {
            currentDirection = "right&up";
        } else if (right&&down) {
            currentDirection = "right&down";
        } else if (right) {
            currentDirection = "right";
        } else if (down) {
            currentDirection = "down";
        } else {
            currentDirection = "up";
        }


        return currentDirection;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

}

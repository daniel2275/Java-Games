package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import gamestates.Gamestate;
import gamestates.Playing;
import objects.DepthCharge;
import objects.Torpedo;
import utilz.DrawAsset;
import utilz.HelpMethods;

import java.util.Objects;

import static com.danielr.subgame.SubGame.*;
import static utilz.Constants.Game.*;
import static utilz.LoadSave.boatAnimation;

public class Player {

    public static final int PLAYER_WIDTH = 48;
    public static final int PLAYER_HEIGHT = 16;
    public static float SPAWN_X = WORLD_WIDTH / 2;
    public static float SPAWN_Y = WORLD_HEIGHT / 2;
    private float maxHealth = 100f;
    private float playerHealth = 100f;
    private float collisionDamage = 20f;
    private float playerSpeed = 0.2f;

    private int playerScore = 1000;

    private final TextureRegion[][] uBoatSprites = new TextureRegion[6][6];

    private Animation<TextureRegion> idleAnimations;
    private Animation<TextureRegion> movingAnimations;
    private Animation<TextureRegion> upAnimations;
    private Animation<TextureRegion> downAnimations;
    private Animation<TextureRegion> hitAnimations;
    private Animation<TextureRegion> sunkAnimations;

    private float stateTime;

    private TextureRegion currentFrame;

    private Rectangle hitbox;


    private String lastDirection;
    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private boolean sunk = false;
    private int flipX = 1;

    private float reload = 0;

    public float reloadSpeed = 3f;

    private int xOffset = 0;

    private Playing playing;

//    private Vector2 velocity = new Vector2(0, 0);


    public Player(Playing playing, float delta) {
        stateTime = delta;
        this.playing = playing;
        loadAnimations("Uboat-atlas.png");
        this.hitbox = HelpMethods.initHitBox(SPAWN_X, SPAWN_Y, PLAYER_WIDTH, PLAYER_HEIGHT);
    }

    public void reset() {
        playerHealth = 100f;
        collisionDamage = 20f;
        playerSpeed = 0.2f;
        playerScore = 1000;
        stateTime = 0;
        sunk = false;
        reload = 0;
        reloadSpeed = 3f;
        xOffset = 0;
        flipX = 1;
        hitbox.setPosition(SPAWN_X, SPAWN_Y);
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

        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 5; j++) {
                uBoatSprites[i][j] = new TextureRegion(uBoatAtlas, j * 64, i * 16, PLAYER_WIDTH, PLAYER_HEIGHT);
            }
        }
        idleAnimations = boatAnimation(0, 5, uBoatSprites, 2.0f);
        movingAnimations = boatAnimation(1, 3, uBoatSprites, 0.055f);
        upAnimations = boatAnimation(2, 3, uBoatSprites, 0.7f);
        downAnimations = boatAnimation(3, 3, uBoatSprites, 0.7f);
        hitAnimations = boatAnimation(4, 1, uBoatSprites, 0.7f);
        sunkAnimations = boatAnimation(5, 1, uBoatSprites, 0.7f);
    }

    public void render() {
        DrawAsset drawPlayer = new DrawAsset(currentFrame, hitbox, -xOffset, 0, flipX, 1, maxHealth,playerHealth, reload, Color.WHITE, reloadSpeed);

//        velocity.scl(stateTime);
//        hitbox.x += velocity.x;
//        hitbox.y += velocity.y;

        drawPlayer.draw();
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
            if (!Objects.equals(lastDirection, "up")) {
                stateTime = 0;
            }
            currentFrame = getUpAnimations().getKeyFrame(stateTime, false);
            lastDirection = "up";
        } else if (down) {
            if (!Objects.equals(lastDirection, "down")) {
                stateTime = 0;
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
                if (playerHealth < 0) {
                    playerHealth = 0;
                }
            } else {
                sunk = true;
                System.out.println("Game Over Collided");
                // game over
                Gamestate.state = Gamestate.GAME_OVER;
            }
        }
    }

    public void doHit(Torpedo torpedo) {
        playerHealth = (playerHealth - torpedo.getTorpedoDamage());
        if (playerHealth <= 0) {
            playerHealth = 0;
            sunk = true;
            System.out.println("Game Over Torpedoed");
            // game over
            Gamestate.state = Gamestate.GAME_OVER;
        }
    }

    public void doHit(DepthCharge depthCharge) {
        playerHealth = (playerHealth - depthCharge.getDpcDamage());
        if (playerHealth <= 0) {
            playerHealth = 0;
            sunk = true;
            System.out.println("Game Over Torpedoed");
            // game over
            Gamestate.state = Gamestate.GAME_OVER;
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
            if (hitbox.getY() + PLAYER_HEIGHT < WORLD_HEIGHT - SKY_SIZE + PLAYER_HEIGHT / 2.0f) {
//                velocity.y = playerSpeed;
                hitbox.y += playerSpeed * Gdx.graphics.getDeltaTime();
            }
        }
        if (down) {
            if (hitbox.getY() > 1) {
//                velocity.y = -playerSpeed;a
                hitbox.y -= playerSpeed * Gdx.graphics.getDeltaTime();
            }
        }
        if (left) {
            flipX = 1;
            xOffset = 0;
            if (hitbox.getX() > 1) {
//                velocity.x = -playerSpeed;
                hitbox.x -= playerSpeed * Gdx.graphics.getDeltaTime();
//                hitbox.x -= playerSpeed;
            }
        }
        if (right) {
            flipX = -1;
            xOffset = -PLAYER_WIDTH;
            if (hitbox.getX() < WORLD_WIDTH + xOffset) {
//                velocity.x = playerSpeed;
                hitbox.x += playerSpeed * Gdx.graphics.getDeltaTime();
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

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }


    public float getReloadSpeed() {
        return reloadSpeed;
    }

    public float getReload() {
        return reload;
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public void setReloadSpeed(float reloadSpeed) {

        this.reloadSpeed = reloadSpeed;
    }

    public void setPlayerSpeed(float playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public void setReload(float reload) {
        this.reload = reload;
    }

    public void exit() {
        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 5; j++) {
                uBoatSprites[i][j].getTexture().dispose();
            }
        }
    }
}

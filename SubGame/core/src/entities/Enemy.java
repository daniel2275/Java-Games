package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import utilz.HelpMethods;
import utilz.Timing;

import static com.danielr.subgame.SubGame.batch;
import static com.danielr.subgame.SubGame.pause;
import static utilz.Constants.Game.*;
import static utilz.HelpMethods.*;
import static utilz.LoadSave.boatAnimation;

public class Enemy {


    public static final int ENEMY_WIDTH = 64;
    public static final int ENEMY_HEIGHT = 25;

    private Rectangle hitbox;
    private float enemyHeath = 100f;

    private final TextureRegion[][] boatSprites =  new TextureRegion[2][6];;
    private Animation<TextureRegion> shipIdle;
    private Animation<TextureRegion> shipExplode;
    private Animation<TextureRegion> shipHit;

    private boolean doHitAnimation = false;
    private boolean dying = false;
    private boolean sunk = false;

    private float enemySpeed, speed;
    private final String spriteAtlas;

    private final float delay;

    private int flipX, flipY;
    private String direction;

    private float xOffset = ENEMY_WIDTH;

    private boolean aggro;

    private float stateTime;

    private TextureRegion currentFrame;

    private boolean sub;

    private Timing fadeDelay;

    private HelpMethods.FadingAnimation fadingAnimation;

    public Enemy(float delay, int spawnPosX , int flipX, String spriteAtlas, float speed, boolean aggro) {
        this.flipX = flipX;
        this.delay = delay;
        this.spriteAtlas = spriteAtlas;
        this.enemySpeed = speed;
        this.speed = speed;
        this.aggro = aggro;
        this.fadeDelay = new Timing(7); // seconds dispose delay
        loadAnimations(spriteAtlas);
        this.fadingAnimation = new HelpMethods.FadingAnimation(200); // fade time
        this.hitbox = initHitBox(spawnPosX, WORLD_HEIGHT - SKY_SIZE - ENEMY_HEIGHT / 3f , ENEMY_WIDTH, ENEMY_HEIGHT);
    }


    public Enemy(float delay, int spawnPosX , int spawnPosY, int flipX,  String spriteAtlas, float speed, boolean aggro, boolean sub) {
        this(delay,spawnPosX,flipX,spriteAtlas,speed,aggro);
        this.sub = sub;
        this.direction = "";
        this.flipY = 1;
        this.hitbox = initHitBox(spawnPosX, spawnPosY , ENEMY_WIDTH, ENEMY_HEIGHT);
    }


    public void update(Player player) {
        if ((aggro) && (!dying)) {
            turnTowardsPlayer(player);
        }
        if (!dying) {
            scheduleAnimation(player);
        }
        render();
    }

    // load animations (pending: migrate to helper class)
    private void loadAnimations(String sprites) {
        Texture boatAtlas = new Texture(sprites);

        for (int i= 0; i <= 1; i++) {
            for (int j= 0; j <= 4; j++) {
        boatSprites[i][j] = new TextureRegion(boatAtlas, 64 * j, 32 * i,ENEMY_WIDTH,ENEMY_HEIGHT);
            }
        }

        shipIdle = boatAnimation(0,5, boatSprites, 0.2f);
        shipExplode = boatAnimation(1,5, boatSprites, 1.0f);
        shipHit = boatAnimation(1,3, boatSprites, 0.2f);
    }


    public void scheduleAnimation(final Player player){
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!pause) {
                    direction = "";

                    if (flipX == -1 && hitbox.getX() <= WORLD_WIDTH) {

                        hitbox.x += enemySpeed ;
                        xOffset = ENEMY_WIDTH;
                        direction = "right";
                    } else if (flipX == -1 && hitbox.getX() > WORLD_WIDTH) {
                        flipX = 1;
                    }
                    if (flipX == 1 && hitbox.getX() >= -65 ) {
                        hitbox.x -= enemySpeed ;
                        xOffset = 0;
                        direction = "left";
                    } else if (flipX == 1 && hitbox.getX() <= -65) {
                        flipX = -1;
                    }



                    if (sub && hitbox.getY() > player.getHitbox().getY()) {
                        hitbox.y -= enemySpeed * flipY;
                        if  (direction.equals("") || Math.abs(hitbox.getX() - player.getHitbox().getX()) < 55) {
                            direction = "down";
                        } else if (Math.abs(hitbox.getY() - player.getHitbox().getY()) > 100) {
                            direction += "&down";
                        }
                    } else if (sub && hitbox.getY() < player.getHitbox().getY()) {
                        hitbox.y += enemySpeed * flipY;
                        if  (direction.equals("") || Math.abs(hitbox.getX() - player.getHitbox().getX()) < 55 ) {
                            direction = "up";
                        } else if (Math.abs(hitbox.getY() - player.getHitbox().getY()) > 100){
                            direction += "&up";
                        }
                    }

                    if (direction.equals("")) {
                        if (flipX == -1) {
                            direction  = "left";
                        } else {
                            direction  = "right";
                        }
                    }


                }
            }
        },delay);
    }




    // aggro behavior
    public void turnTowardsPlayer(Player player) {
        float playerX = player.getuBoatHitBox().getX();
        float playerDistX = Math.abs(playerX - this.hitbox.getX());
        float playerDistY = Math.abs(player.getuBoatHitBox().getY() - this.hitbox.getY());
        // check player in radar range
        if ((playerDistX  < 180) && (playerDistY) < 180) {
            // if sub stop near player
            if ((playerDistX  < 80) && (playerDistY) < 60 && (sub)) {
                enemySpeed = 0;
                return;
            } else {
                enemySpeed = speed;
            }
            // patrol above player
            if ((playerDistX) > 70) {
                if (playerX > hitbox.getX()) {
                    flipX = -1;
                    xOffset = ENEMY_WIDTH;
                } else {
                    flipX = 1;
                    xOffset = 0;
                }

                stateTime = 0;
            }
        }
    }

    public boolean deployCharges() {
        java.util.Random rnd = new java.util.Random();
        int launch = rnd.nextInt(3000);
        return launch < 10;
    }


    public void render() {
        Color color = Color.WHITE;

        if (!pause) {

            hitbox = updateHitbox(hitbox, hitbox.getX(), hitbox.getY());

            stateTime += Gdx.graphics.getDeltaTime();

            if (dying) {
                enemySpeed = 0;

                fadeDelay.checkPause(false);
                fadeDelay.update();

                if (stateTime > 3 && stateTime < 6) {  // delay for 3 frames of animation (smoke above water) & 3 to sink
                    hitbox.y -= 0.5;
                } else if (stateTime > 6) {
                    hitbox.y -= 0.5;
                    fadingAnimation.update(stateTime);

                    color = fadingAnimation.color();
                }

                System.out.println("state:" +stateTime + "  fade:" + fadeDelay.getTimeRemaining());

                currentFrame = shipExplode.getKeyFrame(stateTime, false);

                if (hitbox.y <= -16f || fadeDelay.getTimeRemaining() <= 0) {
                    this.sunk = true;
                }
            } else if (doHitAnimation) {
                currentFrame = shipHit.getKeyFrame(stateTime, false);
                if (stateTime > 1) {
                    doHitAnimation = false;
                }
            } else {
                currentFrame = shipIdle.getKeyFrame(stateTime, true);
            }
        } else {
            fadeDelay.checkPause(true);
        }

        drawObject(currentFrame, hitbox, xOffset, 0, flipX, 1, enemyHeath, -1, color);

    }




    public SpriteBatch getBatch() {
        return batch;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public boolean checkHit(Rectangle hitBox, float damage) {
        boolean collision = Intersector.overlaps(hitBox, this.hitbox);
        if(collision && !dying) {
            this.enemyHeath -= damage;
            if (enemySpeed > 0) {
                if (sub) {
                    speed -= 0.05f;
                } else {
                    speed -= 0.2f;
                }
                if (speed < 0) {
                    speed = 0;
                }
                enemySpeed = speed;

                doHitAnimation = true;
                stateTime = 0;
            }
            return true;
        }
        return false;
    }


    public float getEnemyHeath() {
        return enemyHeath;
    }

    public void setEnemyHeath(int enemyHeath) {
        this.enemyHeath = enemyHeath;
    }

    public float getEnemySpeed() {
        return enemySpeed;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
        fadeDelay.init(); // initialize fade timer
        stateTime = 0; // reset animation time
    }

    public boolean isDying() {
        return dying;
    }

    public void setEnemySpeed(float enemySpeed) {
        this.enemySpeed = enemySpeed;
    }

    public boolean isSunk() {
        return sunk;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public boolean isAggro() {
        return aggro;
    }

    public boolean isSub() {
        return sub;
    }

    public String getDirection() {
        return direction;
    }

    public int getFlipY() {
        return flipY;
    }

    public void setFlipY(int flipY) {
        this.flipY = flipY;
    }
}

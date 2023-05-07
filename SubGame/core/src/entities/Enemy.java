package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import utilz.DrawAsset;
import utilz.HelpMethods;
import utilz.Timing;

import java.util.concurrent.ScheduledExecutorService;

import static com.danielr.subgame.SubGame.batch;
import static com.danielr.subgame.SubGame.pause;
import static utilz.Constants.Game.*;
import static utilz.HelpMethods.initHitBox;
import static utilz.HelpMethods.updateHitbox;
import static utilz.LoadSave.boatAnimation;

public class Enemy {
    private int enemyWidth;
    private int enemyHeight; // 25

    private float currentHealth;

    private float maxHealth;

    private int enemyPoints = 10;
    private Rectangle hitbox;
    private float enemySpeed, speed;

    private final TextureRegion[][] boatSprites =  new TextureRegion[2][6];;
    private Animation<TextureRegion> shipIdle;
    private Animation<TextureRegion> shipExplode;
    private Animation<TextureRegion> shipHit;

    private boolean doHitAnimation = false;
    private boolean dying = false;
    private boolean sunk = false;

    private final String spriteAtlas;

    private long delay;
    private int flipX, flipY;
    private String direction;

    private float xOffset = enemyWidth;

    private boolean aggro;

    private float stateTime;

    private TextureRegion currentFrame;

    private boolean sub;

    private Timing fadeDelay;

    private HelpMethods.FadingAnimation fadingAnimation;

    private boolean quit;

    private ScheduledExecutorService ses;

    private boolean delayComplete;

    Sound explodeSound = Gdx.audio.newSound(Gdx.files.internal("audio/exploded1.mp3"));

    public Enemy(long delay, int spawnPosX , int flipX, String spriteAtlas, float speed, boolean aggro, float currentHealth, float maxHealth, int enemyPoints) {
        this.flipX = flipX;
        this.delay = delay;
        this.enemyWidth = 64;
        this.enemyHeight = 32; // 25
        this.spriteAtlas = spriteAtlas;
        this.enemySpeed = speed;
        this.speed = speed;
        this.aggro = aggro;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.enemyPoints = enemyPoints;
        this.fadeDelay = new Timing(7); // seconds dispose delay
        loadAnimations(spriteAtlas);
        this.fadingAnimation = new HelpMethods.FadingAnimation(200); // fade time
        this.hitbox = initHitBox(spawnPosX, WORLD_HEIGHT - SKY_SIZE - enemyHeight / 3f , enemyWidth, enemyHeight);
    }


    public Enemy(long delay, int spawnPosX , int spawnPosY, int flipX, String spriteAtlas, float speed, boolean aggro, float currentHealth, float maxHealth, boolean sub, int enemyPoints) {
        this(delay,spawnPosX,flipX,spriteAtlas,speed,aggro, currentHealth, maxHealth, enemyPoints);
        this.sub = sub;
        this.direction = "";
        this.flipY = 1;
        this.hitbox = initHitBox(spawnPosX, spawnPosY , enemyWidth, enemyHeight);
    }


    public Enemy(long delay, int spawnPosX , int spawnPosY, int flipX, String spriteAtlas, float speed, boolean aggro, float currentHealth, float maxHealth, boolean sub, int enemyPoints, int enemyWidth, int enemyHeight) {
        this(delay,spawnPosX, spawnPosY, flipX, spriteAtlas,speed,aggro, currentHealth, maxHealth, sub, enemyPoints);
        this.sub = sub;
        this.direction = "";
        this.flipY = 1;
        this.enemyWidth = enemyWidth;
        this.enemyHeight = enemyHeight;
        System.out.println("constructor " + this.enemyWidth + " " + this.enemyHeight);
        loadAnimations(spriteAtlas);
        this.fadingAnimation = new HelpMethods.FadingAnimation(200); // fade time
        this.hitbox = initHitBox(spawnPosX, spawnPosY , this.enemyWidth, this.enemyHeight);
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

    // load animations
    private void loadAnimations(String sprites) {
        Texture boatAtlas = new Texture(sprites);

        for (int i= 0; i <= 1; i++) {
            for (int j= 0; j <= 4; j++) {
        boatSprites[i][j] = new TextureRegion(boatAtlas, enemyWidth * j, enemyHeight * i, enemyWidth, enemyHeight);
//                System.out.println(enemyWidth + " " + enemyHeight);
            }
        }

        shipIdle = boatAnimation(0,5, boatSprites, 0.2f);
        shipExplode = boatAnimation(1,5, boatSprites, 1.0f);
        shipHit = boatAnimation(1,3, boatSprites, 0.2f);
    }

    public void scheduleAnimation(Player player) {
        if (!delayComplete) {
            delay();
        } else {
            direction = "";

            if (flipX == -1 && hitbox.getX() <= WORLD_WIDTH) {
                if ((Math.abs(player.getHitbox().getX() - hitbox.x) > 5) || !sub) {
                    hitbox.x += enemySpeed * Gdx.graphics.getDeltaTime();
                }
                xOffset = enemyWidth;
                direction = "right";
            } else if (flipX == -1 && hitbox.getX() > WORLD_WIDTH) {
                flipX = 1;
            }
            if (flipX == 1 && hitbox.getX() >= -65) {
                if ((Math.abs(player.getHitbox().getX() - hitbox.x) > 5) || !sub) {
                    hitbox.x -= enemySpeed * Gdx.graphics.getDeltaTime();
                }
                xOffset = 0;
                direction = "left";
            } else if (flipX == 1 && hitbox.getX() <= -65) {
                if (!aggro) {
                    enemyPoints -= enemyPoints * 0.1;
                }
                flipX = -1;
            }

            if (sub) {
                if (hitbox.getY() > player.getHitbox().getY()) {
                    hitbox.y -= (enemySpeed * flipY) * Gdx.graphics.getDeltaTime();
                    if (direction.equals("") || Math.abs(hitbox.getX() - player.getHitbox().getX()) < 15) {
                        direction = "down";
                    } else if (Math.abs(hitbox.getY() - player.getHitbox().getY()) > 100) {
                        direction += "&down";
                    }
                } else if (hitbox.getY() < player.getHitbox().getY()) {
                    hitbox.y += (enemySpeed * flipY) * Gdx.graphics.getDeltaTime();
                    if (direction.equals("") || Math.abs(hitbox.getX() - player.getHitbox().getX()) < 15) {
                        direction = "up";
                    } else if (Math.abs(hitbox.getY() - player.getHitbox().getY()) > 100) {
                        direction += "&up";
                    }
                }
            }

            if (direction.equals("")) {
                direction = flipX == -1 ? "left" : "right";
            }
        }
    }

    // aggro behavior
    public void turnTowardsPlayer(Player player) {
        float playerX = player.getuBoatHitBox().getX();
        float playerDistX = Math.abs(playerX - this.hitbox.getX());
        float playerDistY = Math.abs(player.getuBoatHitBox().getY() - this.hitbox.getY());

        // check player in radar range
        if ( (((playerDistX  < 180) && (playerDistY < 180))) || sub ) {
            // if sub stop near player
            if ((playerDistX  < 80) && (playerDistY) < 60 && (sub)) {
                enemySpeed = 0;
            } else {
                enemySpeed = speed;
            }
            // patrol above player
            if (playerDistX > 70 || sub && playerDistX > 2) {
                if (playerX > hitbox.getX()) {
                    flipX = -1;
                    xOffset = enemyWidth;
                } else {
                    flipX = 1;
                    xOffset = 0;
                }
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
                fadeDelay.checkPaused(false);
                fadeDelay.update();

                if (stateTime > 3 && stateTime < 6) {  // delay for 3 frames of animation (smoke above water) & 3 to sink
                    hitbox.y -= 0.5;
                } else if (stateTime > 6) {
                    hitbox.y -= 0.5;
                    fadingAnimation.update(stateTime);
                    color = fadingAnimation.color();
                }

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
            fadeDelay.checkPaused(true);
        }

        DrawAsset drawEnemy = new DrawAsset(currentFrame, hitbox, xOffset, 0,  flipX, 1, maxHealth, currentHealth, -1, color);

        drawEnemy.draw();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public boolean checkHit(Rectangle hitBox, float damage) {
        boolean collision = Intersector.overlaps(hitBox, this.hitbox);
        if (collision && !dying) {
            this.currentHealth -= damage;
            float newSpeed = speed - (0.25f * speed);
            if (this.currentHealth <= 0) {
                this.currentHealth = 0;
            }
            if (enemySpeed > 0) {
                speed = Math.max(newSpeed, 0);
                enemySpeed = speed;
                doHitAnimation = true;
                stateTime = 0;
            }
            return true;
        }
        return false;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public float getEnemySpeed() {
        return enemySpeed;
    }

    public void setEnemySpeed(float enemySpeed) {
        this.enemySpeed = enemySpeed;
    }


    public void setDying(boolean dying) {
        this.dying = dying;
        explodeSound.play();
        fadeDelay.init(); // initialize fade timer
        stateTime = 0; // reset animation time
    }

    public boolean isDying() {
        return dying;
    }

    public void setSunk(boolean sunk) {
        this.sunk = sunk;
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

    public int getEnemyPoints() {
        return enemyPoints;
    }

    public boolean isQuit() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }


    public int getEnemyWidth() {
        return enemyWidth;
    }

    public void setEnemyWidth(int enemyWidth) {
        this.enemyWidth = enemyWidth;
    }

    public int getEnemyHeight() {
        return enemyHeight;
    }

    public void setEnemyHeight(int enemyHeight) {
        this.enemyHeight = enemyHeight;
    }

    public boolean delay() {
        float delaySeconds = delay;
        if (!delayComplete) {
            // Start the delay
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    System.out.print(".");
                    delayComplete = true;
                }
            }, (long) delaySeconds);
        } else {
            return true;
        }
        return false;
    }

//    public boolean delay(long delayMillis) {

//        if (delayComplete) {
//            return true; // delay already complete
//        }
//        if (delayMillis <= 0) {
//            return false; // invalid delay time
//        }
//        CompletableFuture<Void> delayFuture = new CompletableFuture<>();
//        Timer.schedule(new Timer.Task() {
//            @Override
//            public void run() {
//                System.out.print(".");
//                delayFuture.complete(null);
//            }
//        }, delayMillis / 1000f);
//        try {
//            delayFuture.get(); // wait for delay to complete
//        } catch (InterruptedException | ExecutionException e) {
//            Thread.currentThread().interrupt();
//            return false; // delay interrupted or failed
//        }
//        delayComplete = true;
//        return true;
//    }


    private static long timerDelay;

    public static void pause() {
        pause = true;
        timerDelay = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
        Timer.instance().stop();
        System.out.print("Timer Stop -");
    }

    public static void resume() {
        pause = false;
        Timer.instance().delay(TimeUtils.nanosToMillis(TimeUtils.nanoTime() - timerDelay));
        Timer.instance().start();
        System.out.print("Timer Start -");
    }

    public TextureRegion[][] getBoatSprites() {
        return boatSprites;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }

    public Animation<TextureRegion> getBoatIdleAnimation() {
        return shipIdle;
    }

    public void setBoatIdleAnimation(Animation<TextureRegion> shipIdle) {
        this.shipIdle = shipIdle;
    }

    public Animation<TextureRegion> getBoatExplodeAnimation() {
        return shipExplode;
    }

    public void setBoatExplodeAnimation(Animation<TextureRegion> shipExplode) {
        this.shipExplode = shipExplode;
    }

    public Animation<TextureRegion> getBoatHitAnimation() {
        return shipHit;
    }

    public void setBoatHitAnimation(Animation<TextureRegion> shipHit) {
        this.shipHit = shipHit;
    }

    public Timing getFadeDelay() {
        return fadeDelay;
    }

    public void setFadeDelay(Timing fadeDelay) {
        this.fadeDelay = fadeDelay;
    }

    public HelpMethods.FadingAnimation getFadingAnimation() {
        return fadingAnimation;
    }

    public void setFadingAnimation(HelpMethods.FadingAnimation fadingAnimation) {
        this.fadingAnimation = fadingAnimation;
    }
//    private Timer timer;
//    private boolean paused;
//
//    public void togglePauseResume() {
//        if (paused) {
//            timer.start();
//            System.out.println("Timer resumed.");
//        } else {
//            timer.stop();
//            System.out.println("Timer paused.");
//        }
//        paused = !paused;
//    }

//    public void setTimer(Timer timer) {
//        this.timer = timer;
//    }
}

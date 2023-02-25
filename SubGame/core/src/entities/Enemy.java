package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;

import static com.danielr.subgame.SubGame.batch;
import static com.danielr.subgame.SubGame.pause;
import static utilz.Constants.Game.SKY_SIZE;
import static utilz.Constants.Game.WORLD_HEIGHT;
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
    private boolean explode = false;
    private boolean sunk = false;

    private float enemySpeed, speed;
    private final String spriteAtlas;

    private final float delay;

    private int flipX;
    private String direction;

    private float xOffset = ENEMY_WIDTH;

    private boolean aggro;

    private float stateTime;

    private TextureRegion currentFrame;

    private boolean sub;

    public Enemy(float delay, int spawnPosX , int flipX, String spriteAtlas, float speed, boolean aggro) {
        this.flipX = flipX;
        this.delay = delay;
        this.spriteAtlas = spriteAtlas;
        this.enemySpeed = speed;
        this.speed = speed;
        this.aggro = aggro;
        loadAnimations(spriteAtlas);
        this.hitbox = initHitBox(spawnPosX, WORLD_HEIGHT - SKY_SIZE - ENEMY_HEIGHT / 3f , ENEMY_WIDTH, ENEMY_HEIGHT);
    }


    public Enemy(float delay, int spawnPosX , int spawnPosY, int flipX, String spriteAtlas, float speed, boolean aggro, boolean sub) {
        this(delay,spawnPosX,flipX,spriteAtlas,speed,aggro);
        this.sub = sub;
        this.direction = "";
        this.hitbox = initHitBox(spawnPosX, spawnPosY , ENEMY_WIDTH, ENEMY_HEIGHT);
    }

    public void update(Player player) {
        if ((aggro) && (!explode)) {
            turnTowardsPlayer(player);
        }
        if (!explode) {
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
                    if (flipX == -1 && hitbox.getX() <= 800) {
                        hitbox.x += enemySpeed ;
                        xOffset = ENEMY_WIDTH;
                        direction = "right";
                    } else if (flipX == -1 && hitbox.getX() > 800) {
                        flipX = 1;
                    }
                    if (flipX == 1 && hitbox.getX() >= -65 ) {
                        hitbox.x -= enemySpeed ;
                        xOffset = 0;
                        direction = "left";
                    } else if (flipX == 1 && hitbox.getX() <= -65) {
                        flipX = -1;
                    }

                    System.out.println(Math.abs(hitbox.getY() - player.getHitbox().getY()));

                    if (sub && hitbox.getY() > player.getHitbox().getY()) {
                        hitbox.y -= enemySpeed;
                        if  (direction.equals("") || Math.abs(hitbox.getX() - player.getHitbox().getX()) < 55) {
                            direction = "down";
                        } else if (Math.abs(hitbox.getY() - player.getHitbox().getY()) > 100) {
                            direction += "&down";
                        }
                    } else if (sub && hitbox.getY() < player.getHitbox().getY()) {
                        hitbox.y += enemySpeed;
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
        int launch = rnd.nextInt(1000);
        return launch < 10;
    }


    public void render () {
    if(!pause) {
        hitbox = updateHitbox(hitbox, hitbox.getX(), hitbox.getY());
        stateTime += Gdx.graphics.getDeltaTime();
        if (explode) {
            enemySpeed = 0;
            if (stateTime > 3) {  // delay for 3 frames of animation (smoke above water)
                hitbox.y -= 0.5;
            }
            currentFrame = shipExplode.getKeyFrame(stateTime, false);
            if (hitbox.y <= -16f) {
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
    }
        drawObject(currentFrame, hitbox, xOffset, 0, flipX,1, enemyHeath);
    }





    public SpriteBatch getBatch() {
        return batch;
    }

    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public boolean checkHit(Rectangle hitBox, float damage) {
        boolean collision = Intersector.overlaps(hitBox, this.hitbox);
        if(collision && !explode) {
            this.enemyHeath -= damage;
            if (enemySpeed > 0) {
                enemySpeed -= 0.1f;
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

    public void setExplode(boolean explode) {
        this.explode = explode;
        stateTime = 0; // reset animation time
    }

    public boolean isExplode() {
        return explode;
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
}

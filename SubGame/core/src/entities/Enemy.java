package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import utilz.HelpMethods;
import utilz.LoadSave;

import static com.danielr.subgame.SubGame.batch;
import static com.danielr.subgame.SubGame.pause;
import static utilz.Constants.Game.SKY_SIZE;
import static utilz.Constants.Game.WORLD_HEIGHT;

public class Enemy {

    public static final int ENEMY_WIDTH = 64;
    public static final int ENEMY_HEIGHT = 25;

    private Rectangle hitbox;
    private float enemyHeath = 100f;

    private final TextureRegion[][] boatSprites =  new TextureRegion[2][6];;
    private Animation<TextureRegion> shipIdle;
    private Animation<TextureRegion> shipExplode;
    private boolean explode = false;
    private boolean sunk = false;

    private float enemySpeed;
    private final String spriteAtlas;

    private final float delay;

    private final int flip;
    private final int spawnPos;
    private float xOffset = ENEMY_WIDTH;

    private float xPos = 0;

    private float stateTime;

    private TextureRegion currentFrame;

    public Enemy(float delay, int spawnPos , int flip, String spriteAtlas, float speed) {
        this.flip = flip;
        this.spawnPos = spawnPos;
        this.delay = delay;
        this.spriteAtlas = spriteAtlas;
        this.enemySpeed = speed;
        loadAnimations(spriteAtlas);
        this.hitbox = HelpMethods.initHitBox(spawnPos, WORLD_HEIGHT - SKY_SIZE - ENEMY_HEIGHT / 3f , ENEMY_WIDTH, ENEMY_HEIGHT);
    }

    public void update() {
        if (!explode) {
            scheduleAnimation();
        }
        render();
    }

    private void loadAnimations(String sprites) {
        Texture boatAtlas = new Texture(sprites);

        for (int i= 0; i <= 1; i++) {
            for (int j= 0; j <= 4; j++) {
        boatSprites[i][j] = new TextureRegion(boatAtlas, 64 * j, 32 * i,ENEMY_WIDTH,ENEMY_HEIGHT);
            }
        }

        shipIdle = LoadSave.boatAnimation(0,5, boatSprites, 0.2f);
        shipExplode = LoadSave.boatAnimation(1,5, boatSprites, 1.0f);
    }


    public String getSpriteAtlas() {
        return spriteAtlas;
    }

    public void scheduleAnimation(){
        Timer timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (!pause) {
//                    if (first) {
//                        startTime = TimeUtils.nanoTime();
//                        first = false;
//                    }
//                    firstPause = true;
//                    long elapsedTime = TimeUtils.nanoTime() - startTime;
//                    float elapsedSeconds = (elapsedTime / 1000000000f) - pauseTime;
                    if (flip == -1 && xPos > -865) {
                        xPos = (xPos - enemySpeed) ;
//                        xPos = enemySpeed * -elapsedSeconds;
                        xOffset = ENEMY_WIDTH;
                    } else if (flip == 1 && xPos < 930 ) {
                        xPos = enemySpeed + xPos;
//                        xPos = enemySpeed * elapsedSeconds;
                        xOffset = 0;
                    }
                }
            }
        },delay);
    }

    public void render () {
    if(!pause) {
        hitbox = HelpMethods.updateHitbox(hitbox, spawnPos - xPos, hitbox.getY());

        stateTime += Gdx.graphics.getDeltaTime();
        if (explode) {
            enemySpeed = 0;
            if (stateTime > 3) {  // delay for 3 frames of animation (smoke above water)
                hitbox.y -= 0.5;
            }
            currentFrame = shipExplode.getKeyFrame(stateTime, false);
            if (hitbox.y <= -16f) {
                sunk = true;
            }
        } else {
            currentFrame = shipIdle.getKeyFrame(stateTime, true);
        }
    }
        HelpMethods.drawObject(currentFrame, hitbox, xOffset, flip, enemyHeath);
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

    public void setEnemySpeed(float enemySpeed) {
        this.enemySpeed = enemySpeed;
    }

    public boolean isSunk() {
        return sunk;
    }
}

package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.danielr.subgame.SubGame;
import utilz.HelpMethods;
import utilz.LoadSave;

import static com.danielr.subgame.SubGame.batch;
import static com.danielr.subgame.SubGame.shapeRendered;
import static utilz.Constants.Game.*;

public class Enemy {

    public static final int ENEMY_WIDTH = 64;
    public static final int ENEMY_HEIGHT = 24;

    private Rectangle hitbox;

    private TextureRegion[][] boatSprites =  new TextureRegion[1][1];;
    private Animation<TextureRegion> shipIdle;
    private static Texture boatAtlas;

//    private SpriteBatch batch;
    private float enemySpeed;
    private String spriteAtlas;
    boolean first = true;

    private long startTime;
    private float delay;

    private int flip;
    private int spawnPos;
    private float xOffset = ENEMY_WIDTH;

    private float xPos = 0;

    private Timer timer;
    float stateTime;

    private TextureRegion currentFrame;

    public Enemy(float delay, int spawnPos , int flip, String spriteAtlas, float speed) {

        this.flip = flip;
        this.spawnPos = spawnPos;
        this.delay = delay;
        this.spriteAtlas = spriteAtlas;
        this.enemySpeed = speed;
        loadAnimations(spriteAtlas);
        this.hitbox = HelpMethods.initHitBox(spawnPos, WORLD_HEIGHT - SKY_SIZE - ENEMY_HEIGHT / 3f , ENEMY_WIDTH, ENEMY_HEIGHT);

        create();
    }

    public void update() {
        scheduleAnimation();
        render();
    }

    private void loadAnimations(String sprites) {
        boatAtlas = new Texture(sprites);

//        for (int i= 0; i <= 5; i++) {
//            for (int j= 0; j <= 5; j++) {
        boatSprites[0][0] = new TextureRegion(boatAtlas, 1, 1,ENEMY_WIDTH,ENEMY_HEIGHT);
//            }
//        }

        shipIdle = LoadSave.boatAnimation(0,1, boatSprites, 2.0f);
    }


    public String getSpriteAtlas() {
        return spriteAtlas;
    }

    public void scheduleAnimation(){
        timer = new Timer();
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                if (first == true) {
                    startTime = TimeUtils.nanoTime();
                    first =  false;
                }

                long elapsedTime = TimeUtils.nanoTime() - startTime;
                float elapsedSeconds = elapsedTime / 1000000000f;
                if (flip == -1 )  {
                    xPos = enemySpeed * -elapsedSeconds;
                    xOffset = ENEMY_WIDTH;
                } else if (flip == 1 ) {
                    xPos = enemySpeed * elapsedSeconds;
                    xOffset = 0;

                }
            }
        },delay);
    }


    public void create() {
//        batch = new SpriteBatch();
    }

    public void render () {
        batch.begin();
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame =  shipIdle.getKeyFrame(stateTime, true);
        hitbox = HelpMethods.updateHitbox(hitbox,spawnPos - xPos + xOffset , hitbox.getY());
        batch.draw(currentFrame, hitbox.getX(), hitbox.getY() , hitbox.getWidth() * flip,hitbox.getHeight());
        batch.end();

        if (VISIBLE_HITBOXES) {
            shapeRendered.setProjectionMatrix(SubGame.camera.combined);
            shapeRendered.begin();
            hitbox = HelpMethods.updateHitbox(hitbox, spawnPos - xPos, hitbox.getY());
            shapeRendered.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
            shapeRendered.end();
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }


    public TextureRegion getCurrentFrame() {
        return currentFrame;
    }

    public boolean checkHit(Rectangle hitBox) {
        boolean collision = Intersector.overlaps(hitBox, this.hitbox);
        if(collision) {
            System.out.println("hit");
            return true;
        }
        return false;
    }


}

package entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.danielr.subgame.SubGame;
import gamestates.Playing;
import utilz.LoadSave;

import static com.danielr.subgame.SubGame.shapeRendered;
import static utilz.Constants.Game.*;

public class Player {

    public static final int PLAYER_WIDTH = 48;
    public static final int PLAYER_HEIGHT = 16;
    public static final float SPAWN_X = WORLD_WIDTH/2;
    public static final float SPAWN_Y = WORLD_HEIGHT/2;

    private TextureRegion[][] uBoatSprites =  new TextureRegion[6][6];

    private Animation<TextureRegion> idleAnimations;
    private Animation<TextureRegion> movingAnimations;
    private Animation<TextureRegion> upAnimations;
    private Animation<TextureRegion> downAnimations;
    private Animation<TextureRegion> hitAnimations;
    private Animation<TextureRegion> sunkAnimations;

    private static Texture uBoatAtlas;
    private Rectangle uBoatHitBox;

//    private OrthographicCamera camera;
    private SpriteBatch batch;
    private Sprite background;


    private float stateTime;

    private TextureRegion currentFrame;

//    private ShapeRenderer shapeRendered;
    private Rectangle hitbox;
//    private int set;

    private boolean left;
    private boolean right;
    private boolean up;
    private boolean down;
    private int flip = 1;

    private int xPos = 0;
    private int xOffset = PLAYER_WIDTH;
    private int yPos = 0;

    private SubGame subGame;

    private Playing playing;

    public Player(Playing playing) {
        this.playing = playing;
//        this.camera = camera;
        loadAnimations("Uboat-atlas2.png");
        this.uBoatHitBox = LoadSave.initHitBox(SPAWN_X,SPAWN_Y, PLAYER_WIDTH,PLAYER_HEIGHT );
        this.hitbox = LoadSave.initHitBox(SPAWN_X,SPAWN_Y, PLAYER_WIDTH,PLAYER_HEIGHT );
        create();
    }

    public void update() {
        checkDirection();
        checkAnimation();
        checkCollision();
        render();
    }

    private void loadAnimations(String sprites) {
        uBoatAtlas = new Texture(sprites);

        for (int i= 0; i <= 5; i++) {
            for (int j= 0; j <= 5; j++) {
                uBoatSprites[i][j] = new TextureRegion(uBoatAtlas, j * 64, i * 16,PLAYER_WIDTH,PLAYER_HEIGHT);
            }
        }

        idleAnimations = LoadSave.boatAnimation(0,5, uBoatSprites, 2.0f);
        movingAnimations = LoadSave.boatAnimation(1,3, uBoatSprites, 0.055f);
        upAnimations = LoadSave.boatAnimation(2,3, uBoatSprites, 0.7f);
        downAnimations = LoadSave.boatAnimation(3,3, uBoatSprites, 0.7f);
    }


//    private Animation<TextureRegion> boatAnimation(int file, int sprites, TextureRegion[][] boatSprites, float frameDuration) {
//        TextureRegion  animation[] = new TextureRegion[sprites];
//        for (int i = 0; i < sprites; i++) {
//            animation[i] = boatSprites[file][i];
//        }
//        Animation animations = new Animation<TextureRegion>(frameDuration, animation);
//        return animations;
//    }

    public void create() {
        background = new Sprite(new Texture(Gdx.files.internal("sea_background.png")));
//        ship = new Texture(Gdx.files.internal("tanker.png"));

        background.setPosition(0,0);
        background.setSize(800f,600f);

//        ship.setPosition(100,450 - 8);

//        shapeRendered = new ShapeRenderer();
////        hitbox = getuBoatHitBox();
//        shapeRendered.setAutoShapeType(true);

        batch = new SpriteBatch();

    }


    public void render () {
//        batch.setProjectionMatrix(camera.combined);

        checkAnimation();

        batch.begin();
        background.draw(batch);

        uBoatHitBox = LoadSave.updateHitbox(uBoatHitBox,SPAWN_X + xPos - xOffset ,  SPAWN_Y + yPos, uBoatHitBox.width, uBoatHitBox.height);
        batch.draw(currentFrame, uBoatHitBox.getX(), uBoatHitBox.getY(), uBoatHitBox.width * flip,uBoatHitBox.height);
        batch.end();

//        shapeRendered.setProjectionMatrix(camera.combined);
        shapeRendered.begin();
        hitbox = LoadSave.updateHitbox(hitbox, SPAWN_X + xPos - PLAYER_WIDTH  ,  SPAWN_Y + yPos, hitbox.width, hitbox.height);
        shapeRendered.rect(hitbox.getX() , hitbox.getY() ,  hitbox.getWidth() , hitbox.getHeight());
        shapeRendered.end();
    }

    private void checkAnimation() {
        stateTime += Gdx.graphics.getDeltaTime();
        if (left | right) {
            currentFrame = getMovingAnimations().getKeyFrame(stateTime, true);
        } else if (up) {
            currentFrame = getUpAnimations().getKeyFrame(stateTime, true);
        } else if (down) {
            currentFrame = getDownAnimations().getKeyFrame(stateTime, true);
        } else {
            currentFrame = getIdleAnimations().getKeyFrame(stateTime, true);
        }
    }

    private void checkCollision() {
        playing.checkCollision(this.hitbox);
    }

    public Animation<TextureRegion> getIdleAnimations() {
        return idleAnimations;
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

    public Animation<TextureRegion> getHitAnimations() {
        return hitAnimations;
    }

    public Animation<TextureRegion> getSunkAnimations() {
        return sunkAnimations;
    }

    public Rectangle getuBoatHitBox() {
        return uBoatHitBox;
    }

    public void checkDirection() {
        if (up) {
            if (WORLD_HEIGHT/2 + yPos + PLAYER_HEIGHT < WORLD_HEIGHT - SKY_SIZE + PLAYER_HEIGHT /2 ) {
                yPos++;
            }
        }
        if (down) {
            if (WORLD_HEIGHT/2 + yPos > 1) {
                yPos--;
            }
        }
        if (left) {
            flip = 1;
            xOffset = PLAYER_WIDTH;
            if (WORLD_WIDTH/2 + xPos - PLAYER_WIDTH > 1) {
                xPos--;
            }
        }
        if (right) {
            flip = -1;
            xOffset = 0 ;
            if (WORLD_WIDTH/2 + xPos < WORLD_WIDTH) {
                xPos++;
            }
        }

    }

    public int getFlip() {
        return flip;
    }

    public void setFlip(int flip) {
        this.flip = flip;
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
}

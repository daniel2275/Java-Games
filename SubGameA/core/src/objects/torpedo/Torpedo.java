package objects.torpedo;

import Components.AnimatedActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import gamestates.GamePlayScreen;
import utilities.HelpMethods;

import static com.mygdx.sub.SubGame.pause;
import static java.lang.Math.atan2;

public class Torpedo {
    // Torpedo default parameters
    private int speed = 1;
    public static final int TORPEDO_WIDTH = 16 ;
    public static final int TORPEDO_HEIGHT = 16 ;
    private int torpedoDamage = 20;
    private String direction;
    private Rectangle hitbox;
    private boolean enemy = false;
    private float stateTime;
    private final TextureRegion[][] torpedoSprites =  new TextureRegion[4][8];;
    private Animation<TextureRegion> torpedoUpAnimation;
    private Animation<TextureRegion> torpedoExplode;
    private boolean explode = false;
    private float targetX;
    private float targetY;
    private boolean atTarget;
    private float angle;
    private boolean calculateSpeed = false;
    private float velocityX;
    private float velocityY;
    private GamePlayScreen gamePlayScreen;

    private TorpedoAnimationManager animationManager;

    private AnimatedActor torpedoActor;

    public Torpedo(GamePlayScreen gamePlayScreen, float x, float y) {
        animationManager = new TorpedoAnimationManager(new Texture("torpedo-atlas.png"));
        //loadAnimations("torpedo-atlas.png");
        this.gamePlayScreen = gamePlayScreen;
        hitbox = HelpMethods.initHitBox(x, y, TORPEDO_WIDTH, TORPEDO_HEIGHT);

        initializeTorpedo(x,y);

        gamePlayScreen.getGmStage().addActor(torpedoActor);
    }

    public void initializeTorpedo(float x,float y) {
        torpedoActor = new AnimatedActor("torpedo",
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoExplodeAnimation(),
                0, 0, TORPEDO_WIDTH, TORPEDO_HEIGHT, x, y);
    }

    // Constructor for Enemy torpedo
    public Torpedo(GamePlayScreen gamePlayScreen, float x, float y, boolean enemy) {
       this(gamePlayScreen,x,y);
       this.enemy = enemy;
    }

    // Constructor for Player torpedo
    public Torpedo(GamePlayScreen gamePlayScreen, float x, float y, boolean enemy, float targetX, float targetY) {
        this(gamePlayScreen,x,y,enemy);
        this.targetX = targetX;
        this.targetY = targetY;

    }

//    public void update() {
//        render();
//    }

    // Torpedo screen coordinates
    public void updatePos() {
        if (!pause) {
            stateTime += Gdx.graphics.getDeltaTime();
        }
        getShotCoordinates();
    }


//    public void render() {
//        TextureRegion currentFrame;
//
//        if (!pause) {
//            stateTime += Gdx.graphics.getDeltaTime();
//            updatePos();
//        }
//
//        if (this.explode) {
//        //    torpedoActor.isSunk(true);
//            currentFrame = animationManager.getTorpedoExplodeAnimation().getKeyFrame(stateTime, false);
//        } else {
//            currentFrame = animationManager.getTorpedoUpAnimation().getKeyFrame(stateTime, true);
//        }
//
//       // DrawAsset drawTorpedo = new DrawAsset(gamePlayScreen, currentFrame, hitbox, 0, 0, 1, 1, -1, -1, -1, Color.WHITE, TORPEDO_WIDTH, TORPEDO_HEIGHT, 1f, 1f, angle);
//  //      drawTorpedo.draw();
//
//    }

    private void getShotCoordinates() {
        if (enemy) {
            targetShot(targetX, targetY);
        } else {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            Vector3 worldCoordinates = gamePlayScreen.getCamera().unproject(new Vector3(x, y, 0));
            targetShot(worldCoordinates.x - TORPEDO_WIDTH , worldCoordinates.y - TORPEDO_HEIGHT);
        }

    }

    private void targetShot(float goalX, float goalY) {
         if(!calculateSpeed) {
            float destX = goalX - hitbox.x;
            float destY = goalY - hitbox.y;

            angle = (float)  atan2(destY,destX);
            angle = MathUtils.radiansToDegrees * angle;

            // center tubes : (player size - torpedo size) / 2 middle of the sub
            //hitbox.x += (48f-16f) /2f;
            //torpedoActor.setX( torpedoActor.getX() +(48f-16f) /2f);


            float dist = (float) Math.sqrt(destX * destX + destY * destY);
            destX = destX / dist;
            destY = destY / dist;

            velocityX = destX * this.speed;
            velocityY = destY * this.speed;

            calculateSpeed = true;
            torpedoActor.setAngle(angle);
        }
        torpedoActor.moveBy(velocityX,velocityY);
        hitbox.x += velocityX;
        hitbox.y += velocityY;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setExplode(boolean explode) {
        if(explode) {
            stateTime = 0;
        }
        this.explode = explode;
    }

    public boolean isExplode() {
        return explode;
    }

    public int getTorpedoDamage() {
        return torpedoDamage;
    }

    public void setTorpedoDamage(int torpedoDamage) {
        this.torpedoDamage = torpedoDamage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public boolean isAtTarget() {
        return atTarget;
    }

    public void setAtTarget(boolean atTarget) {
        this.atTarget = atTarget;
    }

    public float getAngle() {
        return angle;
    }
    public void exit() {
        for (int i= 0; i <= 1; i++) {
            for (int j= 0; j <= 7; j++) {
                torpedoSprites[i][j].getTexture().dispose();
            }
        }
    }

    public AnimatedActor getTorpedoActor() {
        return torpedoActor;
    }
}


package objects.torpedo;

import Components.AnimatedActor;
import UI.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import static com.mygdx.sub.SubGame.pause;
import static java.lang.Math.atan2;

public class Torpedo {
    // Torpedo default parameters
    private int speed = 1;
    public static final int TORPEDO_WIDTH = 16 ;
    public static final int TORPEDO_HEIGHT = 5 ;
    private float damage = 20;
    private boolean enemy = false;
    private float stateTime;
    private boolean explode = false;
    private float targetX;
    private float targetY;
    private boolean atTarget;
    private float angle;
    private boolean calculateVector = false;
    private float velocityX;
    private float velocityY;
    private GameScreen gameScreen;

    private TorpedoAnimationManager animationManager;

    private AnimatedActor torpedoActor;

    public Torpedo(GameScreen gameScreen, float x, float y, float damage) {
        animationManager = new TorpedoAnimationManager(new Texture("torpedo-atlas.png"));
        this.gameScreen = gameScreen;
        initializeTorpedo(x,y);
        this.damage = damage;
        torpedoActor.setDamage(damage);
        gameScreen.getGmStage().addActor(torpedoActor);
    }

    public void initializeTorpedo(float x,float y) {
        torpedoActor = new AnimatedActor("torpedo",
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoExplodeAnimation(),
                0, 1, TORPEDO_WIDTH, TORPEDO_HEIGHT, x, y);
    }

    // Constructor for Enemy torpedo
    public Torpedo(GameScreen gameScreen, float x, float y, boolean enemy, float damage ) {
       this(gameScreen,x,y, damage);
       this.enemy = enemy;
    }

    // Constructor for Player torpedo
    public Torpedo(GameScreen gameScreen, float x, float y, boolean enemy, float targetX, float targetY,float damage) {
        this(gameScreen,x,y,enemy, damage);
        this.targetX = targetX;
        this.targetY = targetY;
        this.damage = damage;
    }

    // Torpedo screen coordinates
    public void updatePos() {
        if (!pause) {
            stateTime += Gdx.graphics.getDeltaTime();
        }
        getShotCoordinates();
    }

    private void getShotCoordinates() {
        if (enemy) {
            targetShot(targetX, targetY);
        } else {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            Vector3 worldCoordinates = gameScreen.getCamera().unproject(new Vector3(x, y, 0));
            targetShot(worldCoordinates.x - TORPEDO_WIDTH , worldCoordinates.y - TORPEDO_HEIGHT);
        }

    }

    private void targetShot(float goalX, float goalY) {
         if(!calculateVector) {
            float destX = goalX - torpedoActor.getX();
            float destY = goalY - torpedoActor.getY();

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

            calculateVector = true;
            torpedoActor.setAngle(angle);
        }
        torpedoActor.moveBy(velocityX,velocityY);
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

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
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

    public AnimatedActor getTorpedoActor() {
        return torpedoActor;
    }
}


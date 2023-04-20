package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import utilz.DrawAsset;
import utilz.HelpMethods;

import static com.danielr.subgame.SubGame.camera;
import static com.danielr.subgame.SubGame.pause;
import static java.lang.Math.atan2;
import static utilz.LoadSave.boatAnimation;

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

    public Torpedo(float x, float y) {
        loadAnimations("torpedo-atlas.png");
        hitbox = HelpMethods.initHitBox(x, y, TORPEDO_WIDTH, TORPEDO_HEIGHT);
    }

    public Torpedo(float x, float y,  boolean enemy) {
       this(x,y);
       this.enemy = enemy;
    }

    public Torpedo(float x, float y,  boolean enemy, float targetX, float targetY) {
        this(x,y,enemy);
        this.targetX = targetX;
        this.targetY = targetY;
    }

    public void update() {
        render();
    }

    // Torpedo direction and translation
    public void updatePos() {
        directionTranslation();
    }


    public void render() {
        TextureRegion currentFrame;

        if (!pause) {
            stateTime += Gdx.graphics.getDeltaTime();
            updatePos();
        }

        if (this.explode) {
            currentFrame = torpedoExplode.getKeyFrame(stateTime, false);
        } else {
            currentFrame = torpedoUpAnimation.getKeyFrame(stateTime, true);
        }

        DrawAsset drawTorpedo = new DrawAsset(currentFrame, hitbox, 0, 0, 1, 1, -1, -1, -1, Color.WHITE, TORPEDO_WIDTH, TORPEDO_HEIGHT, 1f, 1f, angle);

        drawTorpedo.draw();

    }

    private void loadAnimations(String sprites) {
        Texture boatAtlas = new Texture(sprites);

        for (int i= 0; i <= 1; i++) {
            for (int j= 0; j <= 7; j++) {
                torpedoSprites[i][j] = new TextureRegion(boatAtlas, 16 * j , 16 * i ,TORPEDO_WIDTH,TORPEDO_HEIGHT);
            }
        }

        torpedoExplode = boatAnimation(1,1, torpedoSprites, 8.0f);
        torpedoUpAnimation = boatAnimation(0,8, torpedoSprites, 0.03f);
    }

    private void directionTranslation() {
        if (enemy) {
            targetShot(targetX, targetY);
        } else {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            Vector3 worldCoordinates = camera.unproject(new Vector3(x, y, 0));
            targetShot(worldCoordinates.x - TORPEDO_WIDTH , worldCoordinates.y - TORPEDO_HEIGHT);
        }

    }

    private void targetShot(float goalX, float goalY) {
         if(!calculateSpeed) {
            float destX = goalX - hitbox.x;
            float destY = goalY - hitbox.y;

            angle = (float)  atan2(destY,destX);
            angle = MathUtils.radiansToDegrees * angle;

            // different tubes
            if(Math.abs(angle) > 90) {
//                hitbox.x += 24;
                hitbox.y -= 16;
            }

            float dist = (float) Math.sqrt(destX * destX + destY * destY);
            destX = destX / dist;
            destY = destY / dist;

            velocityX = destX * this.speed;
            velocityY = destY * this.speed;
            calculateSpeed = true;
        }

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
}


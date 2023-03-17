package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import utilz.HelpMethods;

import static com.danielr.subgame.SubGame.*;
import static java.lang.Math.atan2;
import static utilz.Constants.Game.VISIBLE_HITBOXES;
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
    private Animation<TextureRegion> torpedoLeftUpAnimation;
    private Animation<TextureRegion> torpedoLeftAnimation;
    private Animation<TextureRegion> torpedoExplode;
    private boolean explode = false;

    private float targetX;
    private float targetY;

    private boolean atTarget;

    private float angle;

    private boolean calculateSpeed = false;

    private float velocityX;
    private float velocityY;

    public Torpedo(float x, float y, String direction) {
        loadAnimations("torpedo-atlas.png");
        hitbox = HelpMethods.initHitBox(x, y, TORPEDO_WIDTH, TORPEDO_HEIGHT);
        this.direction = direction;
    }

    public Torpedo(float x, float y, String direction, boolean enemy) {
       this(x,y,direction);
       this.enemy = enemy;
    }

    public Torpedo(float x, float y, String direction, boolean enemy, float targetX, float targetY) {
        this(x,y,direction,enemy);
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

        int flipX = 1;
        int flipY = 1;
        int xOffset = 0;
        int yOffset = 0;


//        if (this.explode) {
//            currentFrame = torpedoExplode.getKeyFrame(stateTime, false);
//        } else {
//            switch ( direction ) {
//                case "down":
//                    yOffset = TORPEDO_HEIGHT;
//                    flipY = -1;
//                    currentFrame = torpedoUpAnimation.getKeyFrame(stateTime, true);
//                    break;
//                case "left&down":
//                    flipY = -1;
//                    yOffset = TORPEDO_HEIGHT;
//                    currentFrame = torpedoLeftUpAnimation.getKeyFrame(stateTime, true);
//                    break;
//                case "right&down":
//                    flipY = -1;
//                    flipX = -1;
//                    xOffset = TORPEDO_WIDTH;
//                    yOffset = TORPEDO_HEIGHT;
//                    currentFrame = torpedoLeftUpAnimation.getKeyFrame(stateTime, true);
//                    break;
//                case "left&up":
//                    flipX = 1;
//                    currentFrame = torpedoLeftUpAnimation.getKeyFrame(stateTime, true);
//                    break;
//                case "left":
//                    flipX = 1;
//                    currentFrame = torpedoLeftAnimation.getKeyFrame(stateTime, true);
//                    break;
//                case "right&up":
//                    xOffset = TORPEDO_WIDTH;
//                    flipX = -1;
//                    currentFrame = torpedoLeftUpAnimation.getKeyFrame(stateTime, true);
//                    break;
//                case "right":
//                    xOffset = TORPEDO_WIDTH;
//                    flipX = -1;
//                    currentFrame = torpedoLeftAnimation.getKeyFrame(stateTime, true);
//                    break;
//                default:
//                    flipX = 1;
//                    currentFrame = torpedoUpAnimation.getKeyFrame(stateTime, true);
//                    break;
//            }

//            // mouse controls
//            int mouseX = Gdx.input.getX();
//            int mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
//
//            if ((mouseX < hitbox.getX()) && (mouseY > hitbox.getY())) {
//                flipX = 1;
//                currentFrame = torpedoLeftUpAnimation.getKeyFrame(stateTime, true);
//                direction = "left&up";
//            } else if ((mouseX > hitbox.getX()) && (mouseY > hitbox.getY())) {
//                xOffset = TORPEDO_WIDTH;
//                flipX = -1;
//                currentFrame = torpedoLeftUpAnimation.getKeyFrame(stateTime, true);
//                direction = "right&up";
//            }

//        }

//        HelpMethods.drawObject(currentFrame, hitbox, xOffset, yOffset, flipX, flipY,0,-1, Color.WHITE);


        if (this.explode) {
            currentFrame = torpedoExplode.getKeyFrame(stateTime, false);
        } else {
            currentFrame = torpedoUpAnimation.getKeyFrame(stateTime, true);
        }

        if (currentFrame != null) {
            batch.begin();
            batch.setColor(Color.WHITE);
            System.out.println(angle);
            batch.draw(currentFrame, hitbox.getX() , hitbox.getY() , hitbox.getWidth(), hitbox.getHeight() ,  TORPEDO_WIDTH , TORPEDO_HEIGHT,1,1,angle -90 );
            batch.end();
        }

        if (VISIBLE_HITBOXES) {
            shapeRendered.setColor(Color.WHITE);
            shapeRendered.begin();
            shapeRendered.rect(hitbox.getX() , hitbox.getY() , hitbox.getWidth(), hitbox.getHeight(), TORPEDO_WIDTH,TORPEDO_HEIGHT,1,1,angle -90);
            shapeRendered.end();
        }
    }


    private void loadAnimations(String sprites) {
        Texture boatAtlas = new Texture(sprites);

        for (int i= 0; i <= 3; i++) {
            for (int j= 0; j <= 7; j++) {
                torpedoSprites[i][j] = new TextureRegion(boatAtlas, 16 * j , 16 * i ,TORPEDO_WIDTH,TORPEDO_HEIGHT);
            }
        }

        torpedoExplode = boatAnimation(1,1, torpedoSprites, 8.0f);
        torpedoUpAnimation = boatAnimation(0,8, torpedoSprites, 0.03f);
        torpedoLeftAnimation = boatAnimation(2,8, torpedoSprites, 0.03f);
        torpedoLeftUpAnimation = boatAnimation(3,8, torpedoSprites, 0.03f);
    }

    private void directionTranslation() {
        if (enemy) {
            targetShot(targetX, targetY);
        } else {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            Vector3 worldCoordinates = camera.unproject(new Vector3(x, y, 0));
            System.out.println("Mouse position: (" + worldCoordinates.x + ", " + worldCoordinates.y + ")");

            targetShot(worldCoordinates.x -16 , worldCoordinates.y -16);
        }

//            switch ( direction ) {
//                case "up":
//                    hitbox.y += speed;
//                    break;
//                case "left&up":
//                    hitbox.x -= speed / 2f;
//                    hitbox.y += speed / 2f;
//                    break;
//                case "left":
//                    hitbox.x -= speed;
//                    break;
//                case "right":
//                    hitbox.x += speed;
//                    break;
//                case "right&up":
//                    hitbox.x += speed / 2f;
//                    hitbox.y += speed / 2f;
//                    break;
//                case "down":
//                    hitbox.y -= speed;
//                    break;
//                case "left&down":
//                    hitbox.x -= speed / 2f;
//                    hitbox.y -= speed / 2f;
//                    break;
//                case "right&down":
//                    hitbox.x += speed / 2f;
//                    hitbox.y -= speed / 2f;
//                    break;
//            }
//        }

    }

    private void targetShot(float goalX, float goalY) {
         if(!calculateSpeed) {
            float destX = goalX - hitbox.x ;
            float destY = goalY - hitbox.y ;

            angle = (float)  atan2(destY,destX);
            angle = MathUtils.radiansToDegrees * angle;

            float dist = (float) Math.sqrt(destX * destX + destY * destY);
            destX = destX / dist;
            destY = destY / dist;



            velocityX = destX * this.speed;
            velocityY = destY * this.speed;
            calculateSpeed = true;
        }

        hitbox.x += velocityX;
        hitbox.y += velocityY;

//      float distTravel = (float) Math.sqrt(travelX * travelX + travelY * travelY);
//      if (distTravel > dist) {
//                this.atTarget = true;
////            hitbox.x = destX;
////            hitbox.y = destY;
//            } else {
//      }
        }






    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setTorpedoDamage(int torpedoDamage) {
        this.torpedoDamage = torpedoDamage;
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
}


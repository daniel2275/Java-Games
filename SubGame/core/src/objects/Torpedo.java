package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import utilz.HelpMethods;
import utilz.LoadSave;

import static com.danielr.subgame.SubGame.pause;

public class Torpedo {
    // Torpedo default parameters
    public static final int SPEED = 1;
    public static final int TORPEDO_WIDTH = 16 ;
    public static final int TORPEDO_HEIGHT = 16 ;
    private int torpedoDamage = 20;

    private Rectangle hitbox;

    float stateTime;
    private final TextureRegion[][] torpedoSprites =  new TextureRegion[2][8];;
    private Animation<TextureRegion> torpedoAnimation;
    private Animation<TextureRegion> torpedoExplode;
    private boolean explode = false;

    public Torpedo(float x, float y) {
        loadAnimations("torpedo-atlas.png");
        hitbox = HelpMethods.initHitBox(x, y, TORPEDO_WIDTH, TORPEDO_HEIGHT);
    }

    public void update() {
        if (!pause) {
            updatePos();
        }
        render();
    }

    public void updatePos() {
        hitbox.y += SPEED;
    }

    public void render() {
        TextureRegion currentFrame;
        stateTime += Gdx.graphics.getDeltaTime();
        if (explode) {
            currentFrame = torpedoExplode.getKeyFrame(stateTime, false);
        } else {
            currentFrame = torpedoAnimation.getKeyFrame(stateTime, true);
        }

        HelpMethods.drawObject(currentFrame, hitbox, 0, 1, 0);
    }

    private void loadAnimations(String sprites) {
        Texture boatAtlas = new Texture(sprites);

        for (int i= 0; i <= 1; i++) {
            for (int j= 0; j <= 7; j++) {
                torpedoSprites[i][j] = new TextureRegion(boatAtlas, 16 * j , 16 * i ,TORPEDO_WIDTH,TORPEDO_HEIGHT);
            }
        }

        torpedoAnimation = LoadSave.boatAnimation(0,8, torpedoSprites, 0.03f);
        torpedoExplode = LoadSave.boatAnimation(1,1, torpedoSprites, 8.0f);
    }


    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setTorpedoDamage(int torpedoDamage) {
        this.torpedoDamage = torpedoDamage;
    }

    public void setExplode(boolean explode) {
        this.explode = explode;
    }

    public int getTorpedoDamage() {
        return torpedoDamage;
    }


}


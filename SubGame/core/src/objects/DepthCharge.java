package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import utilz.DrawAsset;
import utilz.HelpMethods;

import static com.danielr.subgame.SubGame.pause;
import static utilz.LoadSave.boatAnimation;

public class DepthCharge {

    public static final int DPC_WIDTH = 16 ;
    public static final int DPC_HEIGHT = 16 ;
    private int dpcDamage = 20;
    private int speed = 1;

    private Rectangle hitbox;

    private float stateTime;
    private final TextureRegion[][] dpcSprites =  new TextureRegion[2][8];;
    private Animation<TextureRegion> dpcAnimation;
    private Animation<TextureRegion> dpcExplodeAnimation;

    private boolean explode;

    public DepthCharge(float x, float y) {
        loadAnimations("dephtcharge-atlas.png");
        hitbox = HelpMethods.initHitBox(x, y, DPC_WIDTH, DPC_HEIGHT);
    }

    public void update() {
        render();
    }

    private void render(){
        TextureRegion currentFrame;

        if (!pause) {
            stateTime += Gdx.graphics.getDeltaTime();
            hitbox.y -= speed;
        }

        int flipX = 1;
        int flipY = 1;
        int xOffset = 0;
        int yOffset = 0;

        if (this.explode) {
            currentFrame = dpcExplodeAnimation.getKeyFrame(stateTime, false);
        } else {
            currentFrame = dpcAnimation.getKeyFrame(stateTime, true);
        }

        DrawAsset drawDepthCharge =  new DrawAsset (currentFrame, hitbox, xOffset, yOffset, flipX, flipY,0, 0,-1, Color.WHITE);

        drawDepthCharge.draw();
    }


    private void loadAnimations(String sprites) {
        Texture dpcAtlas = new Texture(sprites);

        for (int i= 0; i <= 1; i++) {
            for (int j= 0; j <= 7; j++) {
                dpcSprites[i][j] = new TextureRegion(dpcAtlas, 16 * j , 16 * i , DPC_WIDTH, DPC_HEIGHT);
            }
        }

        dpcAnimation = boatAnimation(0,2, dpcSprites, 1.0f);
        dpcExplodeAnimation = boatAnimation(1,8, dpcSprites, 0.6f);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public float getDpcDamage() {
        return dpcDamage;
    }

    public void setDpcDamage(int dpcDamage) {
        this.dpcDamage = dpcDamage;
    }

    public void setExplode(boolean explode) {
        stateTime = 0;
        this.explode = explode;
    }

    public boolean isExplode() {
        return explode;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean getAnimationFinished() {
        return dpcExplodeAnimation.isAnimationFinished(stateTime);
    }

    public void exit() {
        for (int i= 0; i <= 1; i++) {
            for (int j= 0; j <= 7; j++) {
                dpcSprites[i][j].getTexture().dispose();
            }
        }
    }
}

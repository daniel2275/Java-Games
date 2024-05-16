package objects.depthChage;

import Components.AnimatedActor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import UI.game.GameScreen;

public class DepthCharge {

    public static final int DPC_WIDTH = 16 ;
    public static final int DPC_HEIGHT = 16 ;
    private int dpcDamage = 20;
    private int speed = 12;

    private Rectangle hitbox;

    private float stateTime;
    private final TextureRegion[][] dpcSprites =  new TextureRegion[2][8];;
//    private Animation<TextureRegion> dpcAnimation;
//    private Animation<TextureRegion> dpcExplodeAnimation;

    private boolean explode;
    private GameScreen gameScreen;
    private DepthChargeAnimationManager animationManager;
    private AnimatedActor depthChargeActor;

    public DepthCharge(GameScreen gameScreen, float x, float y) {
        animationManager = new DepthChargeAnimationManager(new Texture("dephtcharge-atlas.png"));
        //loadAnimations("dephtcharge-atlas.png");
        this.gameScreen = gameScreen;
        //hitbox = HelpMethods.initHitBox(x, y, DPC_WIDTH, DPC_HEIGHT);
        initializeDepthCharge(x,y);

        gameScreen.getGmStage().addActor(depthChargeActor);
    }

    private void initializeDepthCharge(float x, float y) {
        depthChargeActor = new AnimatedActor("depthCharge",
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoExplodeAnimation(),
                0,1,DPC_WIDTH,DPC_HEIGHT,x,y);
    }

    public void update() {
        depthChargeActor.moveDown(speed);
    }
//        render();
//    }

//    private void render(){
//        TextureRegion currentFrame;
//
////        if (!pause) {
////            stateTime += Gdx.graphics.getDeltaTime();
//
////            hitbox.y -= speed;
////        }
//
//        int flipX = 1;
//        int flipY = 1;
//        int xOffset = 0;
//        int yOffset = 0;
//
//        if (this.explode) {
//            currentFrame = dpcExplodeAnimation.getKeyFrame(stateTime, false);
//        } else {
//            currentFrame = dpcAnimation.getKeyFrame(stateTime, true);
//        }
//
//        DrawAsset drawDepthCharge =  new DrawAsset (gamePlayScreen, currentFrame, hitbox, xOffset, yOffset, flipX, flipY,0, 0,-1, Color.WHITE);
//
//        drawDepthCharge.draw();
//    }


//    private void loadAnimations(String sprites) {
//        Texture dpcAtlas = new Texture(sprites);
//
//        for (int i= 0; i <= 1; i++) {
//            for (int j= 0; j <= 7; j++) {
//                dpcSprites[i][j] = new TextureRegion(dpcAtlas, 16 * j , 16 * i , DPC_WIDTH, DPC_HEIGHT);
//            }
//        }

//        dpcAnimation = boatAnimation(0,2, dpcSprites, 1.0f);
//        dpcExplodeAnimation = boatAnimation(1,8, dpcSprites, 0.6f);
//    }

//    public Rectangle getHitbox() {
//        return hitbox;
//    }

    public int getDpcDamage() {
        return dpcDamage;
    }

    public void setDpcDamage(int dpcDamage) {
        this.dpcDamage = dpcDamage;
    }

    public void setExplode(boolean explode) {
        //stateTime = 0;
        this.explode = explode;
    }

    public boolean isExplode() {
        return explode;
    }

    public int getSpeed() {
        return speed;
    }

//    public void setSpeed(int speed) {
//        this.speed = speed;
//        depthChargeActor.setMoveSpeed(1);
//    }

//    public boolean getAnimationFinished() {
//        return dpcExplodeAnimation.isAnimationFinished(stateTime);
//    }

    public void exit() {
        for (int i= 0; i <= 1; i++) {
            for (int j= 0; j <= 7; j++) {
                dpcSprites[i][j].getTexture().dispose();
            }
        }
    }

    public AnimatedActor getDepthChargeActor() {
        return depthChargeActor;
    }
}

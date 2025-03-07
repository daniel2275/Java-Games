package objects.depthChage;

import Components.BaseActor;
import Components.DepthChargeActor;
import UI.game.GameScreen;
import com.badlogic.gdx.graphics.Texture;

public class DepthCharge {
    public static final int DPC_WIDTH = 16 ;
    public static final int DPC_HEIGHT = 16 ;
    private int dpcDamage = 20;
    private int speed = 12;
    private boolean explode;
    private GameScreen gameScreen;
    private DepthChargeAnimationManager animationManager;
    private DepthChargeActor depthChargeActor;

    private float startY;
    private float maxDistance = 150;

    public DepthCharge(GameScreen gameScreen, float x, float y) {
        animationManager = new DepthChargeAnimationManager(new Texture("dephtcharge-atlas.png"));
        this.gameScreen = gameScreen;
        this.startY = y;
        initializeDepthCharge(x,y);
        gameScreen.getGmStage().addActor(depthChargeActor);
    }

    private void initializeDepthCharge(float x, float y) {
        depthChargeActor = new DepthChargeActor("depthCharge",
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoExplodeAnimation(),
                animationManager.getTorpedoUpAnimation(),
            0,
            1,
                DPC_WIDTH,
                DPC_HEIGHT,
                x,
                y);
        depthChargeActor.toBack();
    }

    public void update() {
        depthChargeActor.moveDown(speed);
    }

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


    public BaseActor getDepthChargeActor() {
        return depthChargeActor;
    }

    public float getStartY() {
        return startY;
    }

    public void setMaxDistance(float maxDistance) {
        this.maxDistance = maxDistance;
    }

    public float getMaxDistance() {
        return maxDistance;
    }


}

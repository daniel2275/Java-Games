package objects.depthChage;

import Components.AnimatedActor;
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
    private AnimatedActor depthChargeActor;

    public DepthCharge(GameScreen gameScreen, float x, float y) {
        animationManager = new DepthChargeAnimationManager(new Texture("dephtcharge-atlas.png"));
        this.gameScreen = gameScreen;
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
            0,
            1,
                DPC_WIDTH,
                DPC_HEIGHT,
                x,
                y,
            false);
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


    public AnimatedActor getDepthChargeActor() {
        return depthChargeActor;
    }
}

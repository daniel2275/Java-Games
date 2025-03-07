package objects.mine;

import Components.MineActor;
import UI.game.GameScreen;
import utilities.Timing;


public class Mine {
    public static final int MINE_WIDTH = 16;
    public static final int MINE_HEIGHT = 16;
    private int mineDamage;
    private boolean enemy;
    private boolean explode;
    private GameScreen gameScreen;
    private MineAnimationManager animationManager;
    private MineActor mineActor;
    private float startY;
    private Timing mineDuration;

    public Mine(GameScreen gameScreen, float x, float y, int mineDamage) {
        animationManager = new MineAnimationManager("animations/mine.atlas");
        this.gameScreen = gameScreen;
        this.startY = y;
        this.mineDamage = mineDamage;
        initializeMine(x, y);
        gameScreen.getGmStage().addActor(mineActor);
        //mineDuration = new Timing(30);
        mineActor.toBack();
    }

    // Constructor for Enemy mine
    public Mine(GameScreen gameScreen, float x, float y, boolean enemy, float damage ) {
        this(gameScreen,x,y, (int) damage);
        this.enemy = enemy;
    }

    private void initializeMine(float x, float y) {
        mineActor = new MineActor("mine",
            animationManager.getAnimation("idle"),
            animationManager.getAnimation("idle"),
            animationManager.getAnimation("idle"),
            animationManager.getAnimation("idle"),
            animationManager.getAnimation("idle"),
            animationManager.getAnimation("idle"),
            animationManager.getAnimation("idle"),
            animationManager.getAnimation("idle"),
            animationManager.getAnimation("explode"),
            animationManager.getAnimation("explode"),
            0,
            1,
            MINE_WIDTH,
            MINE_HEIGHT,
            x,
            y,
            false);

    }
    public void updatePos() {

    }

    public int getMineDamage() {
        return mineDamage;
    }

    public void setMineDamage(int mineDamage){
        this.mineDamage = mineDamage;
    }

    public boolean isExplode(){
        return explode;
    }

    public MineActor getMineActor() {
        return mineActor;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public void setExplode(boolean explode) {
        this.explode = explode;
    }


}

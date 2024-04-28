package entities.player;

import Components.AnimatedActor;
import com.badlogic.gdx.math.Rectangle;
import gamestates.GamePlayScreen;
import static com.mygdx.sub.SubGame.pause;
import static utilities.Constants.PlayerConstants.PLAYER_HEIGHT;
import static utilities.Constants.PlayerConstants.PLAYER_WIDTH;

public class Player {
    private float playerScore = 1000;
    private float stateTime;
    private boolean left, right, up, down, sunk = true;
    private float reload = 0;
    private int xOffset = 0;
    private PlayerAnimationManager playerAnimationManager;
    private PlayerCollisionDetector playerCollisionDetector;
    private PlayerHealthManager playerHealthManager;
    private PlayerMovement playerMovement;
    private AnimatedActor playerActor;
    private GamePlayScreen gamePlayScreen;

    public Player(GamePlayScreen gamePlayScreen, float delta) {
        stateTime = delta;
        this.playerAnimationManager = new PlayerAnimationManager("Uboat-atlas.png");
        this.playerHealthManager = new PlayerHealthManager(this);
        this.playerMovement = new PlayerMovement(this);
        this.gamePlayScreen = gamePlayScreen;
        initializePlayerActor();
        this.playerCollisionDetector = new PlayerCollisionDetector(gamePlayScreen, this, playerHealthManager);
    }

    private void initializePlayerActor() {
        playerActor =  new AnimatedActor(
                "Player",
                playerAnimationManager.getIdleAnimations(),
                playerAnimationManager.getMovingAnimations(),
                playerAnimationManager.getUpAnimations(),
                playerAnimationManager.getDownAnimations(),
                playerAnimationManager.getHitAnimations(),
                playerAnimationManager.getSunkAnimations(),
                reload,
                playerHealthManager.getMaxHealth(),
                PLAYER_WIDTH,
                PLAYER_HEIGHT,
                playerMovement.getSPAWN_X(),
                playerMovement.getSPAWN_Y());
        playerActor.setReloadSpeed(3f);
    }

    public void update() {
        if (!pause) {
            playerMovement.checkDirection();
            playerCollisionDetector.checkCollision();
        }
    }

    public void reset() {
        playerHealthManager.setPlayerHealth(100f);
        playerCollisionDetector.setCollisionDamage(5f);
        playerMovement.setPlayerSpeed(0.2f);
        playerScore = 1000;
        stateTime = 0;
        sunk = false;
        reload = 0;
       // reloadSpeed = 3f;
        xOffset = 0;
    }

    public Rectangle getHitbox() {
        return playerActor.getBoundingRectangle();
    }

//    public void setFlipX(int flipX) {
//        this.flipX = flipX;
//    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isLeft() {
        return left;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isUp() {
        return up;
    }

    public boolean isDown() {
        return down;
    }

    public float getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(float playerScore) {
        this.playerScore = playerScore;
    }

    public float getPlayerSpeed() {
        return playerMovement.getPlayerSpeed();
    }

    public void setPlayerSpeed(float playerSpeed) {
        playerMovement.setPlayerSpeed(playerSpeed);
    }

    public void setReload(float reload) {
        playerActor.setReload(reload);
        this.reload = reload;
    }

    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    public int getxOffset() {
        return this.xOffset;
    }

    public void setPlayerHealth(float playerHealthInit) {
        playerHealthManager.setPlayerHealth(playerHealthInit);
    }

    public float getPlayerHealth() {
        return playerHealthManager.getPlayerHealth();
    }

    public PlayerCollisionDetector getPlayerCollisionDetector() {
        return playerCollisionDetector;
    }

    public PlayerMovement getPlayerMovement() {
        return playerMovement;
    }

    public AnimatedActor getPlayerActor() {
        return playerActor;
    }
}


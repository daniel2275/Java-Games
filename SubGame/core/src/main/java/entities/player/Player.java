package entities.player;

import Components.AnimatedActor;
import UI.game.GameScreen;
import com.badlogic.gdx.math.Rectangle;

import static utilities.Constants.PlayerConstants.PLAYER_HEIGHT;
import static utilities.Constants.PlayerConstants.PLAYER_WIDTH;

public class Player {
    private int playerScore = 0;
    private float stateTime;
    private boolean left, right, up, down, sunk = true;
    private float reload = 0;
    private int xOffset = 0;
    private float damage = 5;
    private PlayerAnimationManager playerAnimationManager;
    private PlayerCollisionDetector playerCollisionDetector;
    private PlayerHealthManager playerHealthManager;
    private PlayerMovement playerMovement;
    private AnimatedActor playerActor;
    private GameScreen gameScreen;

    public Player(GameScreen gameScreen, float delta) {
        stateTime = delta;
        this.playerAnimationManager = new PlayerAnimationManager("Uboat-atlas.png");
        this.playerHealthManager = new PlayerHealthManager(this);
        this.playerMovement = new PlayerMovement(this);
        this.gameScreen = gameScreen;
        initializePlayerActor();
        playerActor.setReloadSpeed(3f); // Default reload speed
        playerActor.setDamage(damage); // Default damage
        this.playerCollisionDetector = new PlayerCollisionDetector(gameScreen, this, playerHealthManager);
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
                playerMovement.getSPAWN_Y(),
                false
            );
    }

    public void update() {
          playerMovement.checkDirection();
          playerCollisionDetector.checkCollision();
    }

    public void reset() {
        playerActor.remove();
        initializePlayerActor();
        gameScreen.getGmStage().addActor(playerActor);
    }

    public Rectangle getHitbox() {
        return playerActor.getBounding();
    }


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

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
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

    public void setPlayerHealth(int playerHealthInit) {
        playerHealthManager.setPlayerHealth(playerHealthInit);
    }

    public int getPlayerHealth() {
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

    public void setPlayerDamage(float damage) {
        this.damage = damage;
    }

    public float getDamage() {
        return damage;
    }
}


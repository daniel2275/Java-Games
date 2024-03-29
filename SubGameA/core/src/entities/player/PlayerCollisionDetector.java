package entities.player;

import Components.HitNumberActor;
import com.badlogic.gdx.math.Rectangle;
import gamestates.GamePlayScreen;
import gamestates.Gamestate;
import objects.DepthCharge;
import objects.Torpedo;

public class PlayerCollisionDetector {
    private GamePlayScreen gamePlayScreen;
    private Rectangle hitbox;
    private boolean sunk;
    private PlayerHealthManager playerHealthManager;
    private Player player;

    PlayerCollisionDetector(GamePlayScreen gamePlayScreen, Player player, PlayerHealthManager playerHealthManager) {
        this.playerHealthManager = playerHealthManager;
        this.player = player;
        this.gamePlayScreen = gamePlayScreen;
        this.hitbox = player.getHitbox();
    }

    private float collisionDamage = 20f;
    public void checkCollision() {
        if (gamePlayScreen.checkCollision(this.hitbox, collisionDamage)) {
            if (playerHealthManager.getPlayerHealth() > 0) {
                playerHealthManager.setPlayerHealth(playerHealthManager.getPlayerHealth() - collisionDamage);
                player.getPlayerActor().setHit(true);
                if (playerHealthManager.getPlayerHealth() < 0) {
                    playerHealthManager.setPlayerHealth(0);
                }
            } else {
                player.getPlayerActor().isSunk(true);
                System.out.println("Game Over Collided");
                // game over
                Gamestate.state = Gamestate.GAME_OVER;
            }
        }
    }

    public void doHit(Torpedo torpedo) {
        playerHealthManager.setPlayerHealth(playerHealthManager.getPlayerHealth() - torpedo.getTorpedoDamage());
        player.getPlayerActor().setHit(true);
        // display hit values
        HitNumberActor hitNumberActor = new HitNumberActor(player.getPlayerActor().getX() , player.getPlayerActor().getY(), torpedo.getTorpedoDamage());
        gamePlayScreen.getGameStage().getStage().addActor(hitNumberActor);

        if (playerHealthManager.getPlayerHealth() <= 0) {
            playerHealthManager.setPlayerHealth(0);
            player.getPlayerActor().isSunk(true);
            System.out.println("Game Over Torpedoed");
            // game over
            Gamestate.state = Gamestate.GAME_OVER;
        }
    }

    public void doHit(DepthCharge depthCharge) {
        playerHealthManager.setPlayerHealth(playerHealthManager.getPlayerHealth() - depthCharge.getDpcDamage());
        player.getPlayerActor().setHit(true);
        // display hit values
        HitNumberActor hitNumberActor = new HitNumberActor(player.getPlayerActor().getX()  , player.getPlayerActor().getX(), (int) depthCharge.getDpcDamage());
        gamePlayScreen.getGameStage().getStage().addActor(hitNumberActor);

        if (playerHealthManager.getPlayerHealth() <= 0) {
            playerHealthManager.setPlayerHealth(0);
            player.getPlayerActor().isSunk(true);
            System.out.println("Game Over Torpedoed");
            // game over
            Gamestate.state = Gamestate.GAME_OVER;
        }
    }

    public boolean isSunk() {
        return sunk;
    }

    public void setCollisionDamage(float collisionDamage) {
        this.collisionDamage = collisionDamage;
    }
}

package entities.player;

import Components.HitNumberActor;
import gamestates.GamePlayScreen;
import gamestates.Gamestate;
import objects.depthChage.DepthCharge;
import objects.torpedo.Torpedo;

public class PlayerCollisionDetector {
    private GamePlayScreen gamePlayScreen;
    //private Rectangle hitbox;
    private boolean sunk;
    private PlayerHealthManager playerHealthManager;
    private Player player;

    PlayerCollisionDetector(GamePlayScreen gamePlayScreen, Player player, PlayerHealthManager playerHealthManager) {
        this.playerHealthManager = playerHealthManager;
        this.player = player;
        this.gamePlayScreen = gamePlayScreen;
       // this.hitbox = player.getHitbox();
    }

    private float collisionDamage = 5f;
    public void checkCollision() {
        if (gamePlayScreen.checkCollision(player.getPlayerActor(), collisionDamage)) {
            if (player.getPlayerActor().getCurrentHealth() > 0) {
                player.getPlayerActor().setHit(true,collisionDamage);
            } else {
                System.out.println("Game Over Collided");
                // game over
                Gamestate.state = Gamestate.GAME_OVER;
            }
        }
    }

    public void doHit(Torpedo torpedo) {
        player.getPlayerActor().setHit(true,torpedo.getTorpedoDamage());
        // display hit values
        HitNumberActor hitNumberActor = new HitNumberActor(player.getPlayerActor().getX() , player.getPlayerActor().getY(), torpedo.getTorpedoDamage());
        gamePlayScreen.getGameStage().getStage().addActor(hitNumberActor);

        if (player.getPlayerActor().getCurrentHealth() <= 0) {
            System.out.println("Game Over Torpedoed");
            // game over
            Gamestate.state = Gamestate.GAME_OVER;
        }
    }

    public void doHit(DepthCharge depthCharge) {
        playerHealthManager.setPlayerHealth(playerHealthManager.getPlayerHealth() - depthCharge.getDpcDamage());
        player.getPlayerActor().setHit(true, depthCharge.getDpcDamage());
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

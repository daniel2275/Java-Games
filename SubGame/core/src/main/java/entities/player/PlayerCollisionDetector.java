package entities.player;

import Components.HitNumberActor;
import UI.game.GameScreen;
import objects.depthChage.DepthCharge;
import objects.torpedo.Torpedo;

public class PlayerCollisionDetector {
    private GameScreen gameScreen;
    private boolean sunk;
    private PlayerHealthManager playerHealthManager;
    private Player player;
    private float collisionDamage = 5f;

    PlayerCollisionDetector(GameScreen gameScreen, Player player, PlayerHealthManager playerHealthManager) {
        this.playerHealthManager = playerHealthManager;
        this.player = player;
        this.gameScreen = gameScreen;
    }

    public void checkCollision() {
        if (gameScreen.checkCollision(player.getPlayerActor(), collisionDamage)) {
            if (player.getPlayerActor().getCurrentHealth() > 0) {
                player.getPlayerActor().setHit(true, collisionDamage);
                player.getPlayerActor().setCollision(true);
            } else {
                player.getPlayerActor().setCollision(false);
            }

            if (player.getPlayerActor().getCurrentHealth() <= 0) {
                System.out.println("Game Over Collided");
                // game over
                gameOver();
            }
        }
    }

    public void playerHit(Torpedo torpedo) {
        player.getPlayerActor().setHit(true,torpedo.getTorpedoActor().getDamage());
        // display hit values
        HitNumberActor hitNumberActor = new HitNumberActor(player.getPlayerActor().getX() , player.getPlayerActor().getY(), (int) torpedo.getTorpedoActor().getDamage());
        gameScreen.getGameStage().getStage().addActor(hitNumberActor);

        if (player.getPlayerActor().getCurrentHealth() <= 0) {
            System.out.println("Game Over Torpedoed");
            gameOver();
        }
    }

    public void playerHit(DepthCharge depthCharge) {
        playerHealthManager.setPlayerHealth(playerHealthManager.getPlayerHealth() - depthCharge.getDpcDamage());
        player.getPlayerActor().setHit(true, depthCharge.getDpcDamage());
        // display hit values
        HitNumberActor hitNumberActor = new HitNumberActor(player.getPlayerActor().getX()  , player.getPlayerActor().getY(), depthCharge.getDpcDamage());
        gameScreen.getGameStage().getStage().addActor(hitNumberActor);

        if (player.getPlayerActor().getCurrentHealth() <= 0) {
              player.getPlayerActor().isSunk(true);
            System.out.println("Game Over Torpedoed");
            gameOver();
        }
    }


    public void gameOver()
    {
        gameScreen.pause();
        gameScreen.getSubGame().setScreen(gameScreen.getGameOver());
    }


    public boolean isSunk() {
        return sunk;
    }

    public void setCollisionDamage(float collisionDamage) {
        this.collisionDamage = collisionDamage;
    }
}
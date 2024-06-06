package entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import utilities.Constants;

import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;
import static utilities.Constants.PlayerConstants.PLAYER_HEIGHT;
import static utilities.Constants.PlayerConstants.PLAYER_WIDTH;

public class PlayerMovement {
    private float SPAWN_X ;
    private float SPAWN_Y ;
    private float playerSpeed = 1f;
    private Player player;

    PlayerMovement(Player player){
        this.player = player;
        this.SPAWN_X = WORLD_WIDTH / 2;
        this.SPAWN_Y = WORLD_HEIGHT / 2;
    }

    public void checkDirection() {
        // Getting direction variables from the Player class instance
        boolean left = player.isLeft();
        boolean right = player.isRight();
        boolean up = player.isUp();
        boolean down = player.isDown();

        float deltaTime = Gdx.graphics.getDeltaTime();

        if (up && player.getHitbox().getY() + PLAYER_HEIGHT < WORLD_HEIGHT - Constants.Game.SKY_SIZE + PLAYER_HEIGHT / 2.0f) {
            player.getPlayerActor().moveUp(playerSpeed );
        }

        if (down && player.getHitbox().getY() > 1) {
            player.getPlayerActor().moveDown(playerSpeed );
        }

        if (left && player.getHitbox().getX() > 1) {
            //player.setFlipX(1);
            player.setxOffset(0);
            player.getPlayerActor().moveLeft(playerSpeed );
        }

        if (right && player.getHitbox().getX() < WORLD_WIDTH + player.getxOffset()) {
            //player.setFlipX(-1);
            player.setxOffset(-PLAYER_WIDTH);
            player.getPlayerActor().moveRight(playerSpeed );
        }
    }

    public void calculateDirection(Vector2 clickPosition, Vector2 playerPosition) {
        // Calculate direction vector
        Vector2 direction = new Vector2(clickPosition.x - playerPosition.x, clickPosition.y - playerPosition.y).nor();



        // Determine closest primary direction (up, left, down, right)
        if (Math.abs(direction.angleDeg()) < 45) {
            player.setRight(true);
            player.setLeft(false);
        } else if (direction.angleDeg() < 135) {
            player.setDown(true);
            player.setUp(false);
        } else if (direction.angleDeg() < 225) {
            player.setLeft(true);
            player.setRight(false);
        } else {
            player.setUp(true);
            player.setDown(false);
        }
    }

    public float getPlayerSpeed() {
        return playerSpeed;
    }

    public void setPlayerSpeed(float playerSpeed) {
        this.playerSpeed = playerSpeed;
    }

    public float getSPAWN_X() {
        return SPAWN_X;
    }

    public float getSPAWN_Y() {
        return SPAWN_Y;
    }
}

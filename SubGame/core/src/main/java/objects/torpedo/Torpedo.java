package objects.torpedo;

import Components.AnimatedActor;
import UI.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import static io.github.daniel2275.subgame.SubGame.pause;

public class Torpedo {
    // Torpedo default parameters
    private int speed = 100;
    public static final int TORPEDO_WIDTH = 16 ;
    public static final int TORPEDO_HEIGHT = 5 ;
    private float damage = 20;
    private boolean enemy = false;
    private float stateTime;
    private boolean explode = false;
    private float targetX;
    private float targetY;
    private boolean atTarget;
    private float angle;
    private boolean calculateVector = false;
    private float velocityX;
    private float velocityY;
    private GameScreen gameScreen;
    private float worldX;
    private float worldY;
    private Vector2 velocity = new Vector2();

    private TorpedoAnimationManager animationManager;

    private AnimatedActor torpedoActor;

    public Torpedo(GameScreen gameScreen, float x, float y, float damage) {
        animationManager = new TorpedoAnimationManager(new Texture("torpedo-atlas.png"));
        this.gameScreen = gameScreen;
        initializeTorpedo(x,y);
        this.damage = damage;
        torpedoActor.setDamage(damage);
        gameScreen.getGmStage().addActor(torpedoActor);
    }

    public void initializeTorpedo(float x,float y) {
        torpedoActor = new AnimatedActor("torpedo",
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoUpAnimation(),
                animationManager.getTorpedoExplodeAnimation(),
            0,
            1,
            TORPEDO_WIDTH,
            TORPEDO_HEIGHT,
            x,
            y,
            false);
    }

    public Torpedo(GameScreen gameScreen, float x, float y, float damage, float worldX, float worldY) {
        this(gameScreen,x,y, damage);
        this.worldX = worldX;
        this.worldY = worldY;
    }

    // Constructor for Enemy torpedo
    public Torpedo(GameScreen gameScreen, float x, float y, boolean enemy, float damage ) {
       this(gameScreen,x,y, damage);
       this.enemy = enemy;
    }

    // Constructor for Player torpedo
    public Torpedo(GameScreen gameScreen, float x, float y, boolean enemy, float targetX, float targetY,float damage) {
        this(gameScreen,x,y,enemy, damage);
        this.targetX = targetX;
        this.targetY = targetY;
        this.damage = damage;
    }

    // Torpedo screen coordinates
    public void updatePos() {
        if (!pause) {
            stateTime += Gdx.graphics.getDeltaTime();
        }
        getShotCoordinates();
    }

    private void getShotCoordinates() {
        if (enemy) {
            targetShot(targetX, targetY);
        } else {
            handleTarget();
        }
    }

    private void handleTarget() {
        // Set the target for the torpedo
        targetShot(worldX, worldY);
    }


    private void targetShot(float goalX, float goalY) {
        if (!calculateVector) {
            Vector2 torpedoPosition = new Vector2(torpedoActor.getX(), torpedoActor.getY());
            Vector2 targetPosition = new Vector2(goalX, goalY);

            Vector2 direction = targetPosition.sub(torpedoPosition).nor(); // Calculate direction vector
            if (direction.isZero()) {
                // Avoid division by zero if target and torpedo are at the same position
                direction.set(1, 0); // Default direction
            }
            velocity.set(direction).scl(speed); // Scale the direction to get the velocity

            // Calculate angle
            float angle = direction.angle(); // Angle in degrees

            // Set the angle and mark vector as calculated
            torpedoActor.setAngle(angle);
            calculateVector = true;

            // Log values for debugging
            System.out.println("Normalized Direction: " + direction);
            System.out.println("Velocity: " + velocity);
            System.out.println("Angle: " + angle);
        }

        // Update the position of the torpedo actor
        torpedoActor.moveBy(velocity.x * Gdx.graphics.getDeltaTime(), velocity.y * Gdx.graphics.getDeltaTime());

    }



    public void setExplode(boolean explode) {
        if(explode) {
            stateTime = 0;
        }
        this.explode = explode;
    }

    public boolean isExplode() {
        return explode;
    }

    public float getDamage() {
        return damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public boolean isEnemy() {
        return enemy;
    }

    public boolean isAtTarget() {
        return atTarget;
    }

    public void setAtTarget(boolean atTarget) {
        this.atTarget = atTarget;
    }

    public float getAngle() {
        return angle;
    }

    public AnimatedActor getTorpedoActor() {
        return torpedoActor;
    }
}

package entities.enemies;

import Components.BaseActor;
import Components.EnemyShipActor;
import Components.EnemySubActor;
import Components.HitNumberActor;
import UI.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import entities.player.Player;
import io.github.daniel2275.subgame.SubGame;
import objects.BulletControl;
import utilities.Settings;
import utilities.HelpMethods;
import utilities.SoundManager;
import utilities.Timing;

import static io.github.daniel2275.subgame.SubGame.pause;
import static utilities.Settings.Game.*;

public class Enemy {
    private int enemyWidth;
    private int enemyHeight;
    private int currentHealth;
    private int enemyPoints = 10;
    private float moveSpeed;
    private String name;
    private boolean dying = false;
    private final String spriteAtlas;
    private long delay;
    private int flipX;
    //    private int flipY;
    //private String direction;
    private float xOffset = enemyWidth;
    private boolean aggro;
    private float stateTime;
    private boolean sub;
    private Timing fadeDelay;
    private HelpMethods.FadingAnimation fadingAnimation;
    private boolean quit;
    private boolean delayComplete;
    private SoundManager soundManager;
    private GameScreen gameScreen;
    private EnemyAnimationManager enemyAnimationManager;
    private float spawnPosX;
    private float spawnPosY;
    private BaseActor enemyActor;
    private float enemyDamage;
    BulletControl bulletControl;
    private boolean isActivated = false;

    private boolean isChasingPlayer = false;
    private float randomMoveTimer = 0;
    private float randomMoveInterval = 10.0f;
    private float randomDirectionX = 0;
    private float randomDirectionY = 0;
    private float ordinanceRange = 150;
    private boolean randomMovement;

    public Enemy(GameScreen gameScreen,
                 String name,
                 long delay,
                 int spawnPosX,
                 int spawnPosY,
                 int flipX,
                 String spriteAtlas,
                 float moveSpeed,
                 boolean aggro,
                 int maxHealth,
                 boolean sub,
                 int enemyPoints,
                 int enemyWidth,
                 int enemyHeight,
                 float enemyDamage,
                 float ordinanceRange,
                 SubGame subGame,
                 boolean randomMovement) {

        // Assign basic properties
        this.name = name;
        this.delay = delay;
        this.flipX = flipX;
        this.enemyWidth = enemyWidth;
        this.enemyHeight = enemyHeight;
        this.spriteAtlas = spriteAtlas;
        this.moveSpeed = moveSpeed;
        this.aggro = aggro;
        this.currentHealth = maxHealth;
        this.enemyPoints = enemyPoints;
        this.sub = sub;
        this.enemyDamage = enemyDamage;
        this.ordinanceRange = ordinanceRange;
        this.randomMovement = randomMovement;

        // Initialize SoundManager
        this.soundManager = SoundManager.getInstance(subGame);

        // Set spawn position
        this.spawnPosX = spawnPosX;
        this.spawnPosY = sub ? spawnPosY : VIRTUAL_HEIGHT - Settings.Game.SKY_SIZE - enemyHeight / 3f + spawnPosY;

        // Initialize game screen
        this.gameScreen = gameScreen;

        // Set up animation manager
        this.enemyAnimationManager = new EnemyAnimationManager(this, spriteAtlas);

        // Initialize charge deployer
        this.bulletControl = new BulletControl();

        // Set default values for direction and flipY
//        this.flipY = 1;

        // Initialize enemy actor and add to game stage
        if (sub) {
            initializeEnemySubActor();
        } else {
            initializeEnemyShipActor();
        }
        gameScreen.getGmStage().addActor(enemyActor);
        gameScreen.onActorAddedOrRemoved();

        // Initially, set the enemy to invisible or inactive
        enemyActor.setVisible(false);

        bulletControl.pauseDeployment(true);

        // Use a timer to delay the activation
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                activateEnemy();
                if (sub) {
                    enemyActor.toBack();
                }
            }
        }, delay);
    }

    private void activateEnemy() {
        isActivated = true;
        enemyActor.setVisible(true);
    }

    private void initializeEnemySubActor() {
        enemyActor = new EnemySubActor(
            name,
            enemyAnimationManager.getAnimation("idleLeft"),
            enemyAnimationManager.getAnimation("idleRight"),
            enemyAnimationManager.getAnimation("movLeft"),
            enemyAnimationManager.getAnimation("movRight"),
            enemyAnimationManager.getAnimation("turnLeft"),
            enemyAnimationManager.getAnimation("turnRight"),
            enemyAnimationManager.getAnimation("hitLeft"),
            enemyAnimationManager.getAnimation("hitRight"),
            enemyAnimationManager.getAnimation("sunkLeft"),
            enemyAnimationManager.getAnimation("sunkRight"),
            -1,
            currentHealth,
            enemyWidth,
            enemyHeight,
            spawnPosX,
            spawnPosY,
            randomMovement
        );
        enemyActor.setMoveSpeed(moveSpeed);
    }

    private void initializeEnemyShipActor() {
        enemyActor = new EnemyShipActor(
            name,
            enemyAnimationManager.getAnimation("idleLeft"),
            enemyAnimationManager.getAnimation("idleRight"),
            enemyAnimationManager.getAnimation("movLeft"),
            enemyAnimationManager.getAnimation("movRight"),
            enemyAnimationManager.getAnimation("turnLeft"),
            enemyAnimationManager.getAnimation("turnRight"),
            enemyAnimationManager.getAnimation("hitLeft"),
            enemyAnimationManager.getAnimation("hitRight"),
            enemyAnimationManager.getAnimation("sunkLeft"),
            enemyAnimationManager.getAnimation("sunkRight"),
           -1,
            currentHealth,
            enemyWidth,
            enemyHeight,
            spawnPosX,
            spawnPosY,
            randomMovement
        );
        enemyActor.setMoveSpeed(moveSpeed);
    }

    public void update(Player player) {
        if (!isActivated) return;

        if (!dying) {
            behavior(player);
        }
        if (gameScreen.isPaused()) {
            bulletControl.pauseDeployment(gameScreen.isPaused());
        }
    }

    public void behavior(Player player) {
        if (!delayComplete) {
            delay();
            return; // Exit early if delay is not complete
        }

        // Reset direction
        if (aggro && !dying && playerInRange(player)) {
            if (!isChasingPlayer) {
                enemyActor.setAggroState(true);
                HitNumberActor hitNumberActor = new HitNumberActor(enemyActor.getX() + enemyActor.getWidth() / 2, enemyActor.getY() + 5, "!", Color.RED);
                gameScreen.getGameStage().getStage().addActor(hitNumberActor);
                soundManager.detected();
                float chaseSpeed = enemyActor.getMoveSpeed() + 6;
                enemyActor.setMoveSpeed(chaseSpeed);
            }
            isChasingPlayer = true;
            bulletControl.pauseDeployment(false);
            turnTowardsPlayer(player);  // Chase the player
        } else {
            if (isChasingPlayer) {
                enemyActor.setAggroState(false);
                bulletControl.pauseDeployment(true);
                float normalSpeed = enemyActor.getMoveSpeed() - 6;
                enemyActor.setMoveSpeed(normalSpeed);
            }
            isChasingPlayer = false;
            if (enemyActor.getName() == "mini") {
                currentHealth = 1;
                bulletControl.pauseDeployment(false);
            }
            randomMovement();  // Move randomly
        }
    }




    private boolean playerInRange(Player player) {
        float playerX = player.getPlayerActor().getX();
        float playerY = player.getPlayerActor().getY();
        float enemyX = enemyActor.getX();
        float enemyY = enemyActor.getY();

        // Calculate distances between enemy and player
        float playerDistX = Math.abs(playerX - enemyX);
        float playerDistY = Math.abs(playerY - enemyY);

       // if (playerDistX < AGGRO_RANGE_X && playerDistY < AGGRO_RANGE_Y) System.out.println("X :" +playerDistX +"  Y :" + playerDistY );

        // Check if player is within radar range
        return playerDistX < AGGRO_RANGE_X && playerDistY < AGGRO_RANGE_Y;
    }

    private void randomMovement() {
        // Update the timer for random movement
        randomMoveTimer += Gdx.graphics.getDeltaTime();

        if (enemyActor.isRandomMovement() &&  randomMoveTimer >= randomMoveInterval) {
            randomMoveTimer = 0;

            // Get player's position relative to the enemy
            float playerX = gameScreen.getPlayer().getPlayerActor().getX();
            float playerY = gameScreen.getPlayer().getPlayerActor().getY();
            float enemyX = enemyActor.getX();
            float enemyY = enemyActor.getY();

            // Bias X direction towards the player
            if (enemyActor.getName().equals("mini")) {

                // Bias X direction away from the player
                randomDirectionX = (MathUtils.randomBoolean(0.7f) && Math.abs(playerX - enemyX) > 10)
                    ? (playerX > enemyX ? -1 : 1) // Move away from the player
                    : MathUtils.random(-1, 1);    // -1 for left, 1 for right, 0 for no movement

                // Bias Y direction away from the player
                randomDirectionY = (MathUtils.randomBoolean(0.7f) && Math.abs(playerY - enemyY) > 10)
                    ? (playerY > enemyY ? -1 : 1) // Move away from the player
                    : MathUtils.random(-1, 1);    // -1 for down, 1 for up, 0 for no movement
            } else if (sub) {
                randomDirectionX = (MathUtils.randomBoolean(0.7f) && Math.abs(playerX - enemyX) > 10)
                ? (playerX > enemyX ? 1 : -1)
                : MathUtils.random(-1, 1);  // -1 for left, 1 for right, 0 for no movement

                // Bias Y direction towards the player
                randomDirectionY = (MathUtils.randomBoolean(0.7f) && Math.abs(playerY - enemyY) > 10)
                ? (playerY > enemyY ? 1 : -1)
                : MathUtils.random(-1, 1);  // -1 for down, 1 for up, 0 for no movement
            } else {
                // Non-sub enemies move only horizontally, with a bias towards the player
                randomDirectionX = (MathUtils.randomBoolean(0.7f) && Math.abs(playerX - enemyX) > 10)
                    ? (playerX > enemyX ? 1 : -1)
                    : MathUtils.randomBoolean() ? 1 : -1;  // -1 for left, 1 for right
                randomDirectionY = 0;  // No vertical movement for non-sub enemies
            }

            // Ensure randomDirectionX or randomDirectionY is active if a sub
            if (sub && randomDirectionX == 0 && randomDirectionY == 0) {
                randomDirectionX = MathUtils.random(-1, 1);  // Reassign X direction
                randomDirectionY = MathUtils.random(-1, 1);  // Reassign Y direction
            }
        }

        // Move horizontally based on randomDirectionX
        if (randomDirectionX == -1) {
            enemyActor.moveLeft(enemyActor.getMoveSpeed());
        } else if (randomDirectionX == 1) {
            enemyActor.moveRight(enemyActor.getMoveSpeed());
        }

        // Move vertically based on randomDirectionY (only if sub is true)
        if (sub) {
            if (randomDirectionY == -1) {
                enemyActor.moveDown(enemyActor.getMoveSpeed());
            } else if (randomDirectionY == 1) {
                enemyActor.moveUp(enemyActor.getMoveSpeed());
            }
        }

        // Ensure the enemy doesn't move off-screen horizontally
        if (enemyActor.getX() < 0) {
            randomDirectionX = 1;  // Move right if out of bounds
        } else if (enemyActor.getX() > VIRTUAL_WIDTH - enemyWidth) {
            randomDirectionX = -1; // Move left if out of bounds
        }

        // Ensure the enemy doesn't move off-screen vertically (only if sub is true)
        if (sub) {
            if (enemyActor.getY() < 0) {
                randomDirectionY = 1;  // Move up if out of bounds
            } else if (enemyActor.getY() > VIRTUAL_HEIGHT - enemyHeight - SKY_SIZE) {
                randomDirectionY = -1; // Move down if out of bounds
            }
        }
    }

    public void turnTowardsPlayer(Player player) {
        float playerX = player.getPlayerActor().getX();
        float playerY = player.getPlayerActor().getY();
        float enemyX = enemyActor.getX();
        float enemyY = enemyActor.getY();

        // Calculate distances between enemy and player
        float playerDistX = playerX - enemyX;
        float playerDistY = Math.abs(playerY - enemyY);

        // Define a dead zone to prevent bouncing
        float deadZoneX = 20;  // Horizontal tolerance
        float deadZoneY = 15;  // Vertical tolerance

        // Check if player is within range
        boolean playerInRange = playerInRange(player);

        if (playerInRange || sub) {
            // If the enemy is a submarine and close to the player, stop near the player
            if (sub && Math.abs(playerDistX) < 180 && playerDistY < 160) {
                enemyActor.setParked(true); // Stop close to the player
            } else {
                enemyActor.setParked(false); // Continue moving
            }

            // Move horizontally towards the player (non-submarine movement)
            if (!sub) {
                // If moving right and the distance is less than 180, turn left
                if (enemyActor.getHorizontalDirection().equals("R")) {
                    if (playerDistX <= -180) {
                        enemyActor.moveLeft(enemyActor.getMoveSpeed());  // Turn left when player is to the left
                    } else {  // Avoid bouncing back and forth
                        enemyActor.moveRight(enemyActor.getMoveSpeed()); // Keep moving right
                    }
                }
                // If moving left and the distance is greater than 180, turn right
                else if (enemyActor.getHorizontalDirection().equals("L")) {
                    if (playerDistX >= 180) {
                        enemyActor.moveRight(enemyActor.getMoveSpeed());  // Turn right when player is to the right
                    } else  {  // Avoid bouncing back and forth
                        enemyActor.moveLeft(enemyActor.getMoveSpeed());   // Keep moving left
                    }
                }
            }

            // Horizontal and vertical movement for submarines
            if (sub) {
                // Horizontal movement towards the player
                if (Math.abs(playerDistX) > deadZoneX) {
                    if (playerDistX > 0) {
                        enemyActor.moveRight(enemyActor.getMoveSpeed());
                    } else if (playerDistX < 0) {
                        enemyActor.moveLeft(enemyActor.getMoveSpeed());
                    }
                }

                // Vertical movement towards the player
                if (Math.abs(playerY - enemyY) > deadZoneY) {
                    if (playerY > enemyY) {
                        enemyActor.moveUp(enemyActor.getMoveSpeed());
                    } else if (playerY < enemyY) {
                        enemyActor.moveDown(enemyActor.getMoveSpeed());
                    }
                }
            }
        }
    }

    public boolean checkHit(BaseActor actor, float damage) {
        boolean collision = enemyActor.collidesWith(actor);
        if (collision && !dying &&
            (!"mini".equals(enemyActor.getName()) ||
                ("mini".equals(enemyActor.getName()) && "torpedo".equals(actor.getName())))) { // Disable collision detection for mini sub unless is hit by a torpedo

            // display hit values for enemies
            HitNumberActor hitNumberActor = new HitNumberActor(enemyActor.getX() + actor.getWidth(), enemyActor.getY(), String.valueOf((int) damage));
            enemyActor.setHit(true, damage);

            if (gameScreen != null) {
                gameScreen.getGameStage().getStage().addActor(hitNumberActor);
            }
            return true;
        }
        return false;
    }

    public void setDying(boolean dying) {
        if (enemyActor.isAggroed()) enemyActor.setAggroState(false);
        this.dying = dying;
        soundManager.sunkRnd();
        fadeDelay.start(); // initialize fade timer
        stateTime = 0; // reset animation time
    }

    public boolean isDying() {
        return dying;
    }

    public boolean isAggro() {
        return aggro;
    }

    public boolean isSub() {
        return sub;
    }

//    public void setFlipY(int flipY) {
//        this.flipY = flipY;
//    }


    public String getName(){
        return name;
    }

    public int getEnemyPoints() {
        return enemyPoints;
    }

    public boolean isQuit() {
        return quit;
    }

    public void setQuit(boolean quit) {
        this.quit = quit;
    }

    public int getEnemyWidth() {
        return enemyWidth;
    }

    public int getEnemyHeight() {
        return enemyHeight;
    }


    public boolean delay() {
        float delaySeconds = delay;
        if (!delayComplete) {
            // Start the delay
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    delayComplete = true;
                }
            }, (long) delaySeconds);
        } else {
            return true;
        }
        return false;
    }

    private static long timerDelay;

    public static void pause() {
        pause = true;
        timerDelay = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
        Timer.instance().stop();
    }

    public static void resume() {
        pause = false;
        Timer.instance().delay(TimeUtils.nanosToMillis(TimeUtils.nanoTime() - timerDelay));
        Timer.instance().start();
    }

    public void setFadeDelay(Timing fadeDelay) {
        this.fadeDelay = fadeDelay;
    }

//    public void setFadingAnimation(HelpMethods.FadingAnimation fadingAnimation) {
//        this.fadingAnimation = fadingAnimation;
//    }

    public BaseActor getEnemyActor() {
        return enemyActor;
    }

    public BulletControl getBulletControl() {
        return bulletControl;
    }

    public float getEnemyDamage() {
        return enemyDamage;
    }

    public float getOrdinanceRange() {
        return ordinanceRange;
    }


    public void dispose() {
        // Dispose actor if it exists
        if (enemyActor != null) {
            enemyActor.remove(); // Remove from stage
            enemyActor.clear();  // Clear actions and listeners
            enemyActor = null;
        }

        // Dispose the animation manager
        if (enemyAnimationManager != null) {
            enemyAnimationManager.dispose();
            enemyAnimationManager = null;
        }

        // Dispose BulletControl if it has disposable resources
        if (bulletControl != null) {
            bulletControl.dispose();
            bulletControl = null;
        }

        // SoundManager is a singleton, don't dispose it here
        soundManager = null;

        // Clear any references to large objects
        gameScreen = null;
        fadingAnimation = null;
        fadeDelay = null;
    }
}


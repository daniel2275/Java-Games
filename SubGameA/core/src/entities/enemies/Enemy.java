package entities.enemies;

import Components.AnimatedActor;
import Components.HitNumberActor;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.sub.SubGame;
import entities.player.Player;
import UI.game.GameScreen;
import objects.depthChage.ChargeDeployer;
import utilities.Constants;
import utilities.HelpMethods;
import utilities.SoundManager;
import utilities.Timing;

import static com.mygdx.sub.SubGame.pause;
import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;

public class Enemy {
    private int enemyWidth;
    private int enemyHeight;
    private int currentHealth;
    private int maxHealth;
    private int enemyPoints = 10;
    private float enemySpeed, speed;
    private String name;
    private boolean dying = false;
    private final String spriteAtlas;
    private long delay;
    private int flipX, flipY;
    private String direction;
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
    private AnimatedActor enemyActor;
    ChargeDeployer chargeDeployer;

    public Enemy(GameScreen gameScreen, String name, long delay, int spawnPosX, int flipX, String spriteAtlas, float speed, boolean aggro, int maxHealth, int enemyPoints, int enemyWidth, int enemyHeight, SubGame subGame) {
        this.name = name;
        this.flipX = flipX;
        this.delay = delay;
        this.enemyWidth = enemyWidth;
        this.enemyHeight = enemyHeight; // 25
        this.spriteAtlas = spriteAtlas;
        this.enemySpeed = speed;
        this.speed = speed;
        this.aggro = aggro;
        this.currentHealth = maxHealth;
        this.enemyPoints = enemyPoints;
        this.soundManager = SoundManager.getInstance(subGame);

        this.spawnPosX = spawnPosX;
        this.spawnPosY = WORLD_HEIGHT - Constants.Game.SKY_SIZE - enemyHeight / 3f;

        this.gameScreen = gameScreen;

        this.enemyAnimationManager = new EnemyAnimationManager(this, spriteAtlas);

        this.chargeDeployer = new ChargeDeployer();

        initializeEnemyActor();

        gameScreen.getGmStage().addActor(enemyActor);
    }

    public Enemy(GameScreen gameScreen, String name, long delay, int spawnPosX, int spawnPosY, int flipX, String spriteAtlas, float speed, boolean aggro, int maxHealth, boolean sub, int enemyPoints, int enemyWidth, int enemyHeight, SubGame subGame) {
        this(gameScreen, name,delay, spawnPosX, flipX, spriteAtlas, speed, aggro, maxHealth, enemyPoints, enemyWidth, enemyHeight,  subGame);
        this.sub = sub;
        this.direction = "";
        this.flipY = 1;
        this.spawnPosY = spawnPosY;
    }

    private void initializeEnemyActor() {
        enemyActor = new AnimatedActor(
                name,
                enemyAnimationManager.getIdleAnimations(),
                enemyAnimationManager.getMovingAnimations(),
                enemyAnimationManager.getUpAnimations(),
                enemyAnimationManager.getDownAnimations(),
                enemyAnimationManager.getHitAnimations(),
                enemyAnimationManager.getSunkAnimations(),
                -1,
                currentHealth,
                enemyWidth,
                enemyHeight,
                spawnPosX,
                spawnPosY);
    }

    public void update(Player player) {
        if ((aggro) && (!dying)) {
            turnTowardsPlayer(player);
        }
        if (!dying) {
            scheduleAnimation(player);
        }
    }

    public void scheduleAnimation(Player player) {
        if (!delayComplete) {
            delay();
            return; // Exit early if delay is not complete
        }

        // Reset direction
        direction = "";

        // Handle movement based on direction and position
        if (flipX == -1) {
            if (enemyActor.getX() <= WORLD_WIDTH) {
                handleHorizontalMovement(player, true);
            } else {
                flipX = 1; // Flip direction if out of bounds
            }
        } else if (flipX == 1) {
            if (enemyActor.getX() >= -enemyActor.getWidth()) {
                handleHorizontalMovement(player, false);
            } else {
                handleOutOfBounds();
            }
        }

        // Handle sub-movement
        if (sub) {
            handleSubMovement(player);
        }

        // Default direction if not set
        if (direction.isEmpty()) {
            direction = (flipX == -1) ? "left" : "right";
        }
    }

    private void handleHorizontalMovement(Player player, boolean movingRight) {
        if (Math.abs(player.getPlayerActor().getX() - enemyActor.getX()) > 5 || !sub) {
            if (movingRight) {
                enemyActor.moveRight(enemyActor.getMoveSpeed());
            } else {
                enemyActor.moveLeft(enemyActor.getMoveSpeed());
            }
        }
        xOffset = movingRight ? enemyWidth : 0;
        direction = movingRight ? "right" : "left";
    }

    private void handleOutOfBounds() {
        if (!aggro) {
            enemyPoints *= 0.9; // Decrease points by 10% if not aggressive
        }
        flipX = -1; // Flip direction
    }

    private void handleSubMovement(Player player) {
        float playerY = player.getPlayerActor().getY();
        float enemyY = enemyActor.getY();
        if (enemyY > playerY) {
            enemyActor.moveDown(enemyActor.getMoveSpeed()); // Move down
            if (direction.isEmpty() || Math.abs(enemyActor.getX() - player.getPlayerActor().getX()) < 15) {
                direction = "down";
            } else if (Math.abs(enemyY - playerY) > 100) {
                direction += "&down";
            }
        } else if (enemyY < player.getHitbox().getY()) {
            enemyActor.moveUp(enemyActor.getMoveSpeed()); // Move up
            if (direction.isEmpty() || Math.abs(enemyActor.getX() - player.getPlayerActor().getX()) < 15) {
                direction = "up";
            } else if (Math.abs(enemyY - playerY) > 100) {
                direction += "&up";
            }
        }
    }

    public void turnTowardsPlayer(Player player) {
        float playerX = player.getPlayerActor().getX();
        float playerY = player.getPlayerActor().getY();
        float enemyX = enemyActor.getX();
        float enemyY = enemyActor.getY();

        // Calculate distances between enemy and player
        float playerDistX = Math.abs(playerX - enemyX);
        float playerDistY = Math.abs(playerY - enemyY);

        // Check if player is within radar range
        boolean playerInRange = playerDistX < 180 && playerDistY < 180;

        // Adjust enemy behavior based on player proximity
        if (playerInRange || sub) {
            // Stop near player if submarine and player is close
            if (sub && playerDistX < 80 && playerDistY < 60) {
                enemyActor.setMoveSpeed(0);
            } else {
                enemyActor.setMoveSpeed(enemyActor.getMoveSpeed());
            }

            // Patrol behavior if player is within range, or it's a submarine
            if ((playerDistX > 70 || sub) && playerDistX > 2) {
                adjustPatrolDirection(playerX, enemyX);
            }
        }
    }

    private void adjustPatrolDirection(float playerX, float enemyX) {
        // Adjust enemy direction based on player position
        if (playerX > enemyX) {
            flipX = -1; // Face left
            xOffset = enemyWidth;
        } else {
            flipX = 1; // Face right
            xOffset = 0;
        }
    }

    public boolean chargeDeployer() {
        java.util.Random rnd = new java.util.Random();
        int launch = rnd.nextInt(3000);
        return launch < 10;
    }

    public boolean checkHit(AnimatedActor actor, float damage) {
        boolean collision = enemyActor.collidesWith(actor);
        if (collision && !dying) {
            // display hit values for enemies
            HitNumberActor hitNumberActor = new HitNumberActor(enemyActor.getX() + actor.getWidth(), enemyActor.getY(), (int) damage);
            enemyActor.setHit(true,damage);
            if (gameScreen != null) {
                gameScreen.getGameStage().getStage().addActor(hitNumberActor);
            }
            return true;
        }
        return false;
    }

    public void setDying(boolean dying) {
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

    public void setFlipY(int flipY) {
        this.flipY = flipY;
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

    public void setFadingAnimation(HelpMethods.FadingAnimation fadingAnimation) {
        this.fadingAnimation = fadingAnimation;
    }

    public void exit() {
//        explodeSound.dispose();
    }

    public AnimatedActor getEnemyActor() {
        return enemyActor;
    }

}


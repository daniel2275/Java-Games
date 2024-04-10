package entities.enemies;

import Components.AnimatedActor;
import Components.HitNumberActor;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.mygdx.sub.SubGame;
import entities.player.Player;
import gamestates.GamePlayScreen;
import utilities.Constants;
import utilities.HelpMethods;
import utilities.SoundManager;
import utilities.Timing;

import java.util.concurrent.ScheduledExecutorService;

import static com.mygdx.sub.SubGame.pause;
import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;

public class Enemy {
    private int enemyWidth;
    private int enemyHeight; // 25
    private float currentHealth;
    private float maxHealth;
    private int enemyPoints = 10;
  //  private Rectangle hitbox;
    private float enemySpeed, speed;
    //private final TextureRegion[][] boatSprites =  new TextureRegion[2][6];;

    private String name;
    private boolean doHitAnimation = false;
    private boolean dying = false;
    //private boolean sunk = false;
    private final String spriteAtlas;
    private long delay;
    private int flipX, flipY;
    private String direction;
    private float xOffset = enemyWidth;
    private boolean aggro;
    private float stateTime;
    private TextureRegion currentFrame;
    private boolean sub;
    private Timing fadeDelay;
    private HelpMethods.FadingAnimation fadingAnimation;
    private boolean quit;
    private ScheduledExecutorService ses;
    private boolean delayComplete;
    private SoundManager soundManager;
    private SubGame subGame;
    private GamePlayScreen gamePlayScreen;
    private EnemyAnimationManager enemyAnimationManager;
    private EnemyHealthManager enemyHealthManager;
    private float spawnPosX;
    private float spawnPosY;
    private AnimatedActor enemyActor;

//    Sound explodeSound = Gdx.audio.newSound(Gdx.files.internal("audio/exploded1.mp3"));

    public Enemy(GamePlayScreen gamePlayScreen, String name, long delay, int spawnPosX, int flipX, String spriteAtlas, float speed, boolean aggro, float currentHealth, float maxHealth, int enemyPoints, SubGame subGame) {
        this.name = name;
        this.flipX = flipX;
        this.delay = delay;
        this.enemyWidth = 64;
        this.enemyHeight = 32; // 25
        this.spriteAtlas = spriteAtlas;
        this.enemySpeed = speed;
        this.speed = speed;
        this.aggro = aggro;
        this.currentHealth = currentHealth;
        this.maxHealth = maxHealth;
        this.enemyPoints = enemyPoints;
        this.fadeDelay = new Timing(7); // seconds dispose delay
        //loadAnimations(spriteAtlas);
        this.fadingAnimation = new HelpMethods.FadingAnimation(200); // fade time
        //this.hitbox = HelpMethods.initHitBox(spawnPosX, WORLD_HEIGHT - Constants.Game.SKY_SIZE - enemyHeight / 3f, enemyWidth, enemyHeight);
        this.soundManager = SoundManager.getInstance(subGame);
        this.subGame = subGame;

        this.spawnPosX = spawnPosX;
        this.spawnPosY = WORLD_HEIGHT - Constants.Game.SKY_SIZE - enemyHeight / 3f;

        this.gamePlayScreen = gamePlayScreen;
        this.enemyHealthManager = new EnemyHealthManager(this);

        this.enemyAnimationManager = new EnemyAnimationManager(this, spriteAtlas);

        initializeEnemyActor();

        gamePlayScreen.getGmStage().addActor(enemyActor);

    }

    public Enemy(GamePlayScreen gamePlayScreen,String name, long delay, int spawnPosX, int spawnPosY, int flipX, String spriteAtlas, float speed, boolean aggro, float currentHealth, float maxHealth, boolean sub, int enemyPoints, SubGame subGame) {
        this(gamePlayScreen, name,delay, spawnPosX, flipX, spriteAtlas, speed, aggro, currentHealth, maxHealth, enemyPoints, subGame);
        this.sub = sub;
        this.direction = "";
        this.flipY = 1;
        this.spawnPosY = spawnPosY;

    }

    public Enemy(GamePlayScreen gamePlayScreen,String name, long delay, int spawnPosX, int spawnPosY, int flipX, String spriteAtlas, float speed, boolean aggro, float currentHealth, float maxHealth, boolean sub, int enemyPoints, int enemyWidth, int enemyHeight, SubGame subGame) {
        this(gamePlayScreen, name,delay, spawnPosX, spawnPosY, flipX, spriteAtlas, speed, aggro, currentHealth, maxHealth, sub, enemyPoints, subGame);
        this.sub = sub;
        this.direction = "";
        this.flipY = 1;
        this.enemyWidth = enemyWidth;
        this.enemyHeight = enemyHeight;
        this.fadingAnimation = new HelpMethods.FadingAnimation(200); // fade time
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
                enemyHealthManager.getMaxHealth(),
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
        render();
    }



    public void scheduleAnimation(Player player) {
        if (!delayComplete) {
            delay();
        } else {
            direction = "";

            if (flipX == -1 && enemyActor.getX() <= WORLD_WIDTH) {
                if ((Math.abs(player.getPlayerActor().getX() - enemyActor.getX()) > 5) || !sub) {
                    enemyActor.moveRight(enemySpeed);
                }
                xOffset = enemyWidth;
                direction = "right";
            } else if (flipX == -1 && enemyActor.getX() > WORLD_WIDTH) {
                flipX = 1;
            }
            if (flipX == 1 && enemyActor.getX() >= -65) {
                if ((Math.abs(player.getPlayerActor().getX() - enemyActor.getX()) > 5) || !sub) {
                    enemyActor.moveLeft(enemySpeed);
                }
                xOffset = 0;
                direction = "left";
            } else if (flipX == 1 && enemyActor.getX() <= -65) {
                if (!aggro) {
                    enemyPoints -= enemyPoints * 0.1;
                }
                flipX = -1;
            }

            if (sub) {
                if (enemyActor.getY() > player.getPlayerActor().getY()) {
                    enemyActor.moveDown(enemySpeed);
                    if (direction.equals("") || Math.abs(enemyActor.getX() - player.getPlayerActor().getX()) < 15) {
                        direction = "down";
                    } else if (Math.abs(enemyActor.getY() - player.getPlayerActor().getY()) > 100) {
                        direction += "&down";
                    }
                } else if (enemyActor.getY() < player.getHitbox().getY()) {
                    enemyActor.moveUp(enemySpeed);
                    if (direction.equals("") || Math.abs(enemyActor.getX() - player.getPlayerActor().getX()) < 15) {
                        direction = "up";
                    } else if (Math.abs(enemyActor.getY() - player.getPlayerActor().getY()) > 100) {
                        direction += "&up";
                    }
                }
            }

            if (direction.equals("")) {
                direction = flipX == -1 ? "left" : "right";
            }
        }
    }

    // aggro behavior
    public void turnTowardsPlayer(Player player) {
        float playerX = player.getPlayerActor().getX();
        float playerDistX = Math.abs(playerX - enemyActor.getX());
        float playerDistY = Math.abs(player.getPlayerActor().getY() - enemyActor.getY());

        // check player in radar range
        if ((((playerDistX < 180) && (playerDistY < 180))) || sub) {
            // if sub stop near player
            if ((playerDistX < 80) && (playerDistY) < 60 && (sub)) {
                enemySpeed = 0;
            } else {
                enemySpeed = speed;
            }
            // patrol above player
            if (playerDistX > 70 || sub && playerDistX > 2) {
                if (playerX > enemyActor.getX()) {
                    flipX = -1;
                    xOffset = enemyWidth;
                } else {
                    flipX = 1;
                    xOffset = 0;
                }
            }
        }
    }

    public boolean deployCharges() {
        java.util.Random rnd = new java.util.Random();
        int launch = rnd.nextInt(3000);
        return launch < 10;
    }

    public void render() {
//        Color color = Color.WHITE;
//
//        if (!pause) {
//
//            // hitbox = HelpMethods.updateHitbox(hitbox, hitbox.getX(), hitbox.getY());
//
//            stateTime += Gdx.graphics.getDeltaTime();

//            if (dying) {
//                enemySpeed = 0;
//                //fadeDelay.checkPaused(false);
//                //fadeDelay.update();
//
//                if (stateTime > 3 && stateTime < 6) {  // delay for 3 frames of animation (smoke above water) & 3 to sink
////                     hitbox.y -= 0.5;
//                } else if (stateTime > 6) {
////                    hitbox.y -= 0.5;
////                    fadingAnimation.update(stateTime);
////                    color = fadingAnimation.color(gamePlayScreen);
//                }
//                //enemyActor.isSunk(true);
//
//                //currentFrame = shipExplode.getKeyFrame(stateTime, false);
//
////                if (hitbox.y <= -16f || fadeDelay.getTimeRemaining() <= 0) {
//                //this.sunk = true;
////                }
//            } else if (doHitAnimation) {
//                enemyActor.setHit(true);
////                //currentFrame = shipHit.getKeyFrame(stateTime, false);
////                if (stateTime > 1) {
////                    doHitAnimation = false;
////                }
////            } else {
////                //currentFrame = shipIdle.getKeyFrame(stateTime, true);
////            }
////        } else {
////            fadeDelay.checkPaused(true);
//            }
//        }
    }

        //DrawAsset drawEnemy = new DrawAsset(gamePlayScreen, currentFrame, hitbox, xOffset, 0, flipX, 1, maxHealth, currentHealth, -1, color);

        //drawEnemy.draw();
   // }

//    public SpriteBatch getBatch() {
//        return batch;
//    }

//    public TextureRegion getCurrentFrame() {
//        return currentFrame;
//    }

    public boolean checkHit(AnimatedActor actor, float damage) {
        //boolean collision = Intersector.overlaps(hitBox, enemyActor.getBoundingRectangle());
        boolean collision = enemyActor.collidesWith(actor);
        if (collision && !dying) {
            // display hit values for enemies
            HitNumberActor hitNumberActor = new HitNumberActor(enemyActor.getX() + actor.getWidth(), enemyActor.getY(), (int) damage);
            enemyActor.setHit(true,damage);
            if (gamePlayScreen != null) {
                gamePlayScreen.getGameStage().getStage().addActor(hitNumberActor);
            }
            return true;
        }
        return false;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public float getEnemySpeed() {
        return enemySpeed;
    }

    public void setEnemySpeed(float enemySpeed) {
        this.enemySpeed = enemySpeed;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
        soundManager.sunkRnd();
        fadeDelay.init(); // initialize fade timer
        stateTime = 0; // reset animation time
    }

    public boolean isDying() {
        return dying;
    }

//    public void setSunk(boolean sunk) {
//        this.sunk = sunk;
//    }

//    public boolean isSunk() {
//        return sunk;
//    }

    public boolean isAggro() {
        return aggro;
    }

    public boolean isSub() {
        return sub;
    }

    public String getDirection() {
        return direction;
    }

    public int getFlipY() {
        return flipY;
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

    public void setEnemyWidth(int enemyWidth) {
        this.enemyWidth = enemyWidth;
    }

    public int getEnemyHeight() {
        return enemyHeight;
    }

    public void setEnemyHeight(int enemyHeight) {
        this.enemyHeight = enemyHeight;
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

//    public void setHitbox(Rectangle hitbox) {
//        this.hitbox = hitbox;
//    }

    public Timing getFadeDelay() {
        return fadeDelay;
    }

    public void setFadeDelay(Timing fadeDelay) {
        this.fadeDelay = fadeDelay;
    }

    public HelpMethods.FadingAnimation getFadingAnimation() {
        return fadingAnimation;
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

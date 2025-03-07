
package levels;

import UI.game.GameScreen;
import entities.enemies.Enemy;
import entities.enemies.EnemyBuilder;
import entities.enemies.EnemyManager;
import java.util.Random;

import static utilities.Settings.Game.ATTACK_RANGE;
import static utilities.Settings.Game.VIRTUAL_WIDTH;

public class Level {
    private static final int HEALTH_INCREMENT = 5;
    // level loop
    private int currentScreen;
    private int totalLevels = 0;

    private final EnemyManager enemyManager;
    private int currentHealth = 100;
    private int maxHealth = currentHealth;
    private final GameScreen gameScreen;
    private float previousLevel;
    // damage
    private int levelCounter = 0;
    private final int damageIncrement = 10;
    private float enemyDamage = 10;
    private boolean randomMovement;


    public Level(EnemyManager enemyManager, GameScreen gameScreen) {
        this.enemyManager = enemyManager;
        this.gameScreen = gameScreen;
    }

    /**
     * Selects and initializes the level based on the current screen.
     */
    public void levelSelector() {
        growEnemies();
        currentHealth = maxHealth;

        if (currentScreen > 0 && currentScreen <= LevelType.values().length) {
            LevelType levelType = LevelType.values()[currentScreen - 1];

            // Check if the current level type is RANDOMIZED
            if (levelType == LevelType.RANDOMIZED) {
                // Reset the currentScreen to the RANDOMIZED level index
                currentScreen = LevelType.RANDOMIZED.ordinal() + 1;
            }

            levelType.initializeLevel(this);
        } else {
            currentScreen = 0;
        }
    }

    public void growEnemies() {
        if (totalLevels == 0) {
            previousLevel = totalLevels;
            levelCounter++;
        }

        if (totalLevels > previousLevel && totalLevels % 5 == 0) {
            maxHealth += HEALTH_INCREMENT;
            if (levelCounter % 10 == 0 &&  enemyDamage < 100) {
                enemyDamage += damageIncrement;
            }
        }
        previousLevel = totalLevels;
    }

    private void addEnemyToManager(Enemy enemy) {
        enemyManager.addEnemy(enemy);
    }

    private Enemy createEnemy(String name, int spawnPosX, int spawnPosY, long delay, boolean flipX, String spriteAtlas, float speed, boolean aggro, boolean sub, int healthMultiplier, int points, float ordinanceRange) {
        return new EnemyBuilder(gameScreen)
                .withName(name)
                .withSpawnPosX(spawnPosX)
                .withSpawnPosY(spawnPosY)
                .withDelay(delay)
                .withFlipX(flipX ? -1 : 1)
                .withSpriteAtlas(spriteAtlas)
                .withSpeed(speed)
                .withAggro(aggro)
                .withMaxHealth(currentHealth * healthMultiplier)
                .withSub(sub)
                .withEnemyPoints(points)
                .withEnemyWidth(64)
                .withEnemyHeight(32)
                .withEnemyDamage(enemyDamage)
                .withOrdinanceRange(ordinanceRange)
                .withRandomMovement(randomMovement)
                .build();
    }

    private Enemy createEnemy(String name, int spawnPosX, int spawnPosY, int delay, boolean flipX, String spriteAtlas, float speed, boolean aggro, boolean sub, int healthMultiplier, int points, int width, int height, float ordinanceRange, boolean randomMovement) {
        return new EnemyBuilder(gameScreen)
                .withName(name)
                .withSpawnPosX(spawnPosX)
                .withSpawnPosY(spawnPosY)
                .withDelay(delay)
                .withFlipX(flipX ? -1 : 1)
                .withSpriteAtlas(spriteAtlas)
                .withSpeed(speed)
                .withAggro(aggro)
                .withMaxHealth(currentHealth * healthMultiplier)
                .withSub(sub)
                .withEnemyPoints(points)
                .withEnemyWidth(width)
                .withEnemyHeight(height)
                .withEnemyDamage(enemyDamage)
                .withOrdinanceRange(ordinanceRange)
                .withRandomMovement(randomMovement)
                .build();
    }

    private enum LevelType {
        LEVEL1 {
            @Override
            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy(
//                    "enemy1",
//                    (int) VIRTUAL_WIDTH,
//                    -10,
//                    3,
//                    true,
//                    "animations/tankeratlas.atlas",
//                    15f,
//                    false,
//                    false,
//                    1,
//                    10,
//                    250,
//                    58,
//                    150));
                level.addEnemyToManager(level.createEnemy("enemy2",
                    -250,
                    -10,
                    5,
                    true,
                    "animations/tankeratlas.atlas",
                    10f,
                    false,
                    false,
                    1,
                    10,
                    250,
                    58,
                    ATTACK_RANGE,
                    false));
                level.addEnemyToManager(level.createEnemy(
                    "enemy3",
                    (int) VIRTUAL_WIDTH,
                    0,
                    20,
                    false,
                    "animations/destroyeratlas.atlas",
                    15f,
                    true,
                    false,
                    1,
                    10,
                    200,
                    82,
                    ATTACK_RANGE,
                    true));
              level.addEnemyToManager(level.createEnemy(
                  "enemy4",
                  (int) VIRTUAL_WIDTH + 160,
                  120,
                  10,
                  false,
                  "animations/enemysub.atlas",
                  13f,
                  true,
                  true,
                  1,
                  10,
                  160,
                  32,
                  ATTACK_RANGE,
                  true));

                level.addEnemyToManager(level.createEnemy(
                    "mini",
                    (int) VIRTUAL_WIDTH + 46,
                    120,
                    1,
                    false,
                    "animations/minisub.atlas",
                    40f,
                    false,
                    true,
                    1,
                    10,
                    46,
                    12,
                    ATTACK_RANGE,
                    true));

                level.addEnemyToManager(level.createEnemy(
                    "enemy5",
                    -160,
                    320,
                    10,
                    false,
                    "animations/enemysub.atlas",
                    13f,
                    true,
                    true,
                    1,
                    10,
                    160,
                    32,
                    ATTACK_RANGE,
                    true));

                //  level.addEnemyToManager(level.createEnemy("enemy4", (int) VIRTUAL_WIDTH + 65, 50, 13, false, "enemy-sub1.png", 13f, true, true, 1, 10, 200));
             //   level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 65, 80, 20, false, "enemy-sub1.png", 13f, true, true, 1, 10, 200));
            }

        },
        LEVEL2 {
            @Override
            void initializeLevel(Level level) {
                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker2-atlas.png", 10f, false, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
            }
        },
        LEVEL3 {
            @Override
            void initializeLevel(Level level) {
                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10,150));
                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "destroyer-atlas.png", 15f, true, false, 1, 10,150));
            }
        },
        LEVEL4 {
            @Override
            void initializeLevel(Level level) {
                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "tanker2-atlas.png", 10f, false, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + 64, 0, 15, false, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy4", -65, 64, 20, true, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
            }
        },
        LEVEL5 {
            @Override
            void initializeLevel(Level level) {
                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 65, 30, 10, false, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy3", -65, 40, 14, true, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
            }
        },
        LEVEL6 {
            @Override
            void initializeLevel(Level level) {
                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy3", -65, 60, 15, true, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy4", -65, 0, 25, true, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 65, 20, 30, false, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
            }
        },
        LEVEL7 {
            @Override
            void initializeLevel(Level level) {
                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy2", -65, 10, 10, true, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + 65, 0, 20, false, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy4", -65, 0, 30, true, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 65, 35, 40, false, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
                level.addEnemyToManager(level.createEnemy("enemy6", -65, 0, 45, true, "tanker2-atlas.png", 10f, false, false, 1, 10, 150));
            }
        },
        RANDOMIZED {
            @Override
            void initializeLevel(Level level) {
                Random random = new Random();
                int numberOfEnemies = random.nextInt(5) + 3; // Random number of enemies between 3 and 7

                for (int i = 0; i < numberOfEnemies; i++) {
                    String[] enemyTypes = {"tanker-atlas.png", "destroyer-atlas.png", "enemy-sub1.png"};
                    String enemyType = enemyTypes[random.nextInt(enemyTypes.length)];

                    int spawnPosX = random.nextBoolean() ? -65 : (int) VIRTUAL_WIDTH + 65; // Random left or right spawn
                    int spawnPosY = random.nextInt(100); // Random Y position between 0 and 100
                    long delay = random.nextInt(30); // Random delay between 0 and 30
                    boolean flipX = random.nextBoolean();
                    float speed = 10f + random.nextFloat() * 5f; // Random speed between 10 and 15
                    boolean aggro = (enemyType.equals("enemy-sub1.png") || enemyType.equals("destroyer-atlas.png"));
                    boolean sub = enemyType.equals("enemy-sub1.png");
                    int healthMultiplier = 1 + random.nextInt(3); // Random health multiplier between 1 and 3
                    int points = 10 * healthMultiplier;
                    float ordinanceRange = 150f + random.nextFloat() * 500f;

                    level.addEnemyToManager(level.createEnemy(
                        "enemy" + (i + 1), spawnPosX, spawnPosY, delay, flipX, enemyType, speed, aggro, sub, healthMultiplier, points, ordinanceRange
                    ));
                }
            }
        };

        abstract void initializeLevel(Level level);
    }

    // Getters and Setters
    public int getCurrentScreen() {
        return currentScreen;
    }

    public void setCurrentScreen(int currentScreen) {
        this.currentScreen = currentScreen;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public void setTotalLevels(int totalLevels) {
        this.totalLevels = totalLevels;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public float getEnemyDamage() {
        return enemyDamage;
    }

    public void setEnemyDamage(float enemyDamage) {
        this.enemyDamage = enemyDamage;
    }
}

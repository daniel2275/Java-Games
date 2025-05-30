//package levels;
//
//import UI.game.GameScreen;
//import entities.enemies.Enemy;
//import entities.enemies.EnemyBuilder;
//import entities.enemies.EnemyManager;
//
//import java.util.Random;
//
//import static utilities.Settings.Game.ATTACK_RANGE;
//import static utilities.Settings.Game.VIRTUAL_WIDTH;
//
//public class Level {
//    private static final int HEALTH_INCREMENT = 5;
//    // level loop
//    private int currentScreen;
//    private int totalLevels = 0;
//
//    private final EnemyManager enemyManager;
//    private int currentHealth = 100;
//    private int maxHealth = currentHealth;
//    private final GameScreen gameScreen;
//    private float previousLevel;
//    // damage
//    private int levelCounter = 0;
//    private final int damageIncrement = 10;
//    private float enemyDamage = 10;
//    private boolean randomMovement;
//
//    public Level(EnemyManager enemyManager, GameScreen gameScreen) {
//        this.enemyManager = enemyManager;
//        this.gameScreen = gameScreen;
//    }
//    /**
//     * Selects and initializes the level based on the current screen.
//     */
//    public void levelSelector() {
//        growEnemies();
//        currentHealth = maxHealth;
//
//        if (currentScreen > 0 && currentScreen <= LevelType.values().length) {
//            LevelType levelType = LevelType.values()[currentScreen - 1];
//            gameScreen.getUpgradeStore().getUpgradeManager().advanceLevel("saveGame", 1);
//            // Check if the current level type is RANDOMIZED
//            if (levelType == LevelType.RANDOMIZED) {
//                // Reset the currentScreen to the RANDOMIZED level index
//                currentScreen = LevelType.RANDOMIZED.ordinal() + 1;
//            }
//
//            levelType.initializeLevel(this);
//        } else {
//            currentScreen = 0;
//        }
//    }
//
//    public void growEnemies() {
//        if (totalLevels == 0) {
//            previousLevel = totalLevels;
//            levelCounter++;
//        }
//
//        if (totalLevels > previousLevel && totalLevels % 5 == 0) {
//            maxHealth += HEALTH_INCREMENT;
//            if (levelCounter % 10 == 0 &&  enemyDamage < 100) {
//                enemyDamage += damageIncrement;
//            }
//        }
//        previousLevel = totalLevels;
//    }
//
//    private void addEnemyToManager(Enemy enemy) {
//        enemyManager.addEnemy(enemy);
//    }
//
//    private Enemy createEnemy(String name, int spawnPosX, int spawnPosY, long delay, boolean flipX, String spriteAtlas, float speed, boolean aggro, boolean sub, int healthMultiplier, int points, float ordinanceRange) {
//        return new EnemyBuilder(gameScreen)
//            .withName(name)
//            .withSpawnPosX(spawnPosX)
//            .withSpawnPosY(spawnPosY)
//            .withDelay(delay)
//            .withFlipX(flipX ? -1 : 1)
//            .withSpriteAtlas(spriteAtlas)
//            .withSpeed(speed)
//            .withAggro(aggro)
//            .withMaxHealth(currentHealth * healthMultiplier)
//            .withSub(sub)
//            .withEnemyPoints(points)
//            .withEnemyWidth(64)
//            .withEnemyHeight(32)
//            .withEnemyDamage(enemyDamage)
//            .withOrdinanceRange(ordinanceRange)
//            .withRandomMovement(randomMovement)
//            .build();
//    }
//
//    private Enemy createEnemy(String name, int spawnPosX, int spawnPosY, int delay, boolean flipX, String spriteAtlas, float speed, boolean aggro, boolean sub, int healthMultiplier, int points, int width, int height, float ordinanceRange, boolean randomMovement) {
//        return new EnemyBuilder(gameScreen)
//            .withName(name)
//            .withSpawnPosX(spawnPosX)
//            .withSpawnPosY(spawnPosY)
//            .withDelay(delay)
//            .withFlipX(flipX ? -1 : 1)
//            .withSpriteAtlas(spriteAtlas)
//            .withSpeed(speed)
//            .withAggro(aggro)
//            .withMaxHealth(currentHealth * healthMultiplier)
//            .withSub(sub)
//            .withEnemyPoints(points)
//            .withEnemyWidth(width)
//            .withEnemyHeight(height)
//            .withEnemyDamage(enemyDamage)
//            .withOrdinanceRange(ordinanceRange)
//            .withRandomMovement(randomMovement)
//            .build();
//    }
//
//    private enum LevelType {
//        LEVEL1 {
//            @Override
//            void initializeLevel(Level level) {
////                level.addEnemyToManager(level.createEnemy(
////                    "enemy1",
////                    (int) VIRTUAL_WIDTH,
////                    -10,
////                    3,
////                    true,
////                    "animations/tankeratlas.atlas",
////                    15f,
////                    false,
////                    false,
////                    1,
////                    10,
////                    250,
////                    58,
////                    150));
//                level.addEnemyToManager(level.createEnemy("enemy2",
//                    -250,
//                    -10,
//                    5,
//                    true,
//                    "animations/tankeratlas.atlas",
//                    10f,
//                    false,
//                    false,
//                    1,
//                    10,
//                    250,
//                    58,
//                    ATTACK_RANGE,
//                    false));
//                level.addEnemyToManager(level.createEnemy(
//                    "enemy3",
//                    (int) VIRTUAL_WIDTH,
//                    0,
//                    20,
//                    false,
//                    "animations/destroyeratlas.atlas",
//                    15f,
//                    true,
//                    false,
//                    1,
//                    10,
//                    200,
//                    82,
//                    ATTACK_RANGE,
//                    true));
//                level.addEnemyToManager(level.createEnemy(
//                    "enemy4",
//                    (int) VIRTUAL_WIDTH + 160,
//                    120,
//                    10,
//                    false,
//                    "animations/enemysub.atlas",
//                    13f,
//                    true,
//                    true,
//                    1,
//                    10,
//                    160,
//                    32,
//                    ATTACK_RANGE,
//                    true));
//
//                level.addEnemyToManager(level.createEnemy(
//                    "mini",
//                    (int) VIRTUAL_WIDTH + 46,
//                    120,
//                    1,
//                    false,
//                    "animations/minisub.atlas",
//                    40f,
//                    false,
//                    true,
//                    1,
//                    10,
//                    46,
//                    12,
//                    ATTACK_RANGE,
//                    true));
//
//                level.addEnemyToManager(level.createEnemy(
//                    "enemy5",
//                    -160,
//                    320,
//                    10,
//                    false,
//                    "animations/enemysub.atlas",
//                    13f,
//                    true,
//                    true,
//                    1,
//                    10,
//                    160,
//                    32,
//                    ATTACK_RANGE,
//                    true));
//
//                //  level.addEnemyToManager(level.createEnemy("enemy4", (int) VIRTUAL_WIDTH + 65, 50, 13, false, "enemy-sub1.png", 13f, true, true, 1, 10, 200));
//                //   level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 65, 80, 20, false, "enemy-sub1.png", 13f, true, true, 1, 10, 200));
//            }
//
//        },
//        LEVEL2 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker2-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//            }
//        },
//        LEVEL3 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10,150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "destroyer-atlas.png", 15f, true, false, 1, 10,150));
//            }
//        },
//        LEVEL4 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "tanker2-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + 64, 0, 15, false, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy4", -65, 64, 20, true, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//            }
//        },
//        LEVEL5 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 65, 30, 10, false, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy3", -65, 40, 14, true, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//            }
//        },
//        LEVEL6 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy3", -65, 60, 15, true, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy4", -65, 0, 25, true, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 65, 20, 30, false, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//            }
//        },
//        LEVEL7 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", -65, 10, 10, true, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + 65, 0, 20, false, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy4", -65, 0, 30, true, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 65, 35, 40, false, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy6", -65, 0, 45, true, "tanker2-atlas.png", 10f, false, false, 1, 10, 150));
//            }
//        },
//        RANDOMIZED {
//            @Override
//            void initializeLevel(Level level) {
//                Random random = new Random();
//                int numberOfEnemies = random.nextInt(5) + 3; // Random number of enemies between 3 and 7
//
//                for (int i = 0; i < numberOfEnemies; i++) {
//                    String[] enemyTypes = {"tanker-atlas.png", "destroyer-atlas.png", "enemy-sub1.png"};
//                    String enemyType = enemyTypes[random.nextInt(enemyTypes.length)];
//
//                    int spawnPosX = random.nextBoolean() ? -65 : (int) VIRTUAL_WIDTH + 65; // Random left or right spawn
//                    int spawnPosY = random.nextInt(100); // Random Y position between 0 and 100
//                    long delay = random.nextInt(30); // Random delay between 0 and 30
//                    boolean flipX = random.nextBoolean();
//                    float speed = 10f + random.nextFloat() * 5f; // Random speed between 10 and 15
//                    boolean aggro = (enemyType.equals("enemy-sub1.png") || enemyType.equals("destroyer-atlas.png"));
//                    boolean sub = enemyType.equals("enemy-sub1.png");
//                    int healthMultiplier = 1 + random.nextInt(3); // Random health multiplier between 1 and 3
//                    int points = 10 * healthMultiplier;
//                    float ordinanceRange = 150f + random.nextFloat() * 500f;
//
//                    level.addEnemyToManager(level.createEnemy(
//                        "enemy" + (i + 1), spawnPosX, spawnPosY, delay, flipX, enemyType, speed, aggro, sub, healthMultiplier, points, ordinanceRange
//                    ));
//                }
//            }
//        };
//
//        abstract void initializeLevel(Level level);
//    }
//
//    // Getters and Setters
//    public int getCurrentScreen() {
//        return currentScreen;
//    }
//
//    public void setCurrentScreen(int currentScreen) {
//        this.currentScreen = currentScreen;
//    }
//
//    public int getTotalLevels() {
//        return totalLevels;
//    }
//
//    public void setTotalLevels(int totalLevels) {
//        this.totalLevels = totalLevels;
//    }
//
//    public int getCurrentHealth() {
//        return currentHealth;
//    }
//
//    public void setCurrentHealth(int currentHealth) {
//        this.currentHealth = currentHealth;
//    }
//
//    public int getMaxHealth() {
//        return maxHealth;
//    }
//
//    public void setMaxHealth(int maxHealth) {
//        this.maxHealth = maxHealth;
//    }
//
//    public float getEnemyDamage() {
//        return enemyDamage;
//    }
//
//    public void setEnemyDamage(float enemyDamage) {
//        this.enemyDamage = enemyDamage;
//    }
//}


package levels;

import UI.game.GameScreen;
import entities.enemies.Enemy;
import entities.enemies.EnemyBuilder;
import entities.enemies.EnemyManager;
import utilities.Settings.LevelConstants;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static utilities.Settings.Game.ATTACK_RANGE;
import static utilities.Settings.Game.VIRTUAL_WIDTH;
import static utilities.Settings.LevelConstants.*;

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
    private int stage;

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

        stage = gameScreen.getStage();

        LevelType levelType = getLevelTypeForStage(stage);

        if (levelType != null) {
            List<LevelType.LevelInitializer> selectedLevels = levelType.getLevels();

            if (currentScreen > 0 && currentScreen <= selectedLevels.size()) {
                LevelType.LevelInitializer levelInitializer = selectedLevels.get(currentScreen - 1);
                // gameScreen.getUpgradeStore().getUpgradeManager().advanceLevel("SaveGame", stage);
                //gameScreen.getUpgradeStore().getUpgradeManager().setStageLevel("SaveGame", stage, currentScreen - 1);
                // Check if the current level is RANDOMIZED
                if (levelInitializer == LevelType.DefaultLevels.RANDOMIZED) {
                    currentScreen = levelType.ordinal() + 1;
                }

                levelInitializer.initializeLevel(this);
            } else {
                currentScreen = 0;
            }
        } else {
            currentScreen = 0;
        }
    }

    private LevelType getLevelTypeForStage(int stage) {
        switch ( stage ) {
            case 1:
                return LevelType.STAGE_ONE;
            case 2:
                return LevelType.STAGE_TWO;
            default:
                return LevelType.DEFAULT;
        }
    }

    public enum LevelType {
        STAGE_ONE(Arrays.asList(StageOneLevels.values())),
        STAGE_TWO(Arrays.asList(StageTwoLevels.values())),
        DEFAULT(Arrays.asList(DefaultLevels.values()));

        private final List<LevelInitializer> levels;

        LevelType(List<LevelInitializer> levels) {
            this.levels = levels;
        }

        public List<LevelInitializer> getLevels() {
            return levels;
        }

        public interface LevelInitializer {
            void initializeLevel(Level level);
        }

        public enum StageOneLevels implements LevelInitializer {
            LEVEL1 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 2, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, -10, (int) delays[0], false, LevelConstants.TANKER_ATLAS, 10f, false, false, 1, 10, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, false));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH, -10, (int) delays[1], true, LevelConstants.TANKER_ATLAS, 10f, false, false, 1, 10, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, false));
                }
            },
            LEVEL2 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 2, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], true, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL3 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 3, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[1], false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[2], false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL4 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 3, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[1], false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[2], false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL5 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 4, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[2], false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy4", -LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL6 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy3", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[2], false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy4", -LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL7 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 3, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 16f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 16f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, MINI_SUB_ATLAS, 40f, false, true, 1, 10, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL8 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 4, 0, 2000, 1000, 2000);
                    for (int i = 0; i < 2; i++) {
                        level.addEnemyToManager(level.createEnemy("destroyer" + i, (i % 2 == 0 ? (int) VIRTUAL_WIDTH : -LevelConstants.DESTROYER_WIDTH), 0, (int) delays[i], false, LevelConstants.DESTROYER_ATLAS, 17f, true, false, 1, 12, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    }
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 180, (int) delays[2], false, LevelConstants.ENEMY_SUB_ATLAS, 17f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[3], false, MINI_SUB_ATLAS, 40f, false, true, 1, 10, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL9 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 18f, true, false, 1, 15, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 18f, true, false, 1, 15, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 200, (int) delays[2], false, LevelConstants.ENEMY_SUB_ATLAS, 18f, true, true, 1, 15, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", -LevelConstants.ENEMY_SUB_WIDTH, 200, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 18f, true, true, 1, 15, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[4], false, MINI_SUB_ATLAS, 40f, false, true, 1, 15, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL10 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    for (int i = 0; i < 3; i++) {
                        level.addEnemyToManager(level.createEnemy("destroyer" + i, i % 2 == 0 ? (int) VIRTUAL_WIDTH : -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[i], false, LevelConstants.DESTROYER_ATLAS, 19f, true, false, 1, 18, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    }
                    level.addEnemyToManager(level.createEnemy("sub1", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 19f, true, true, 1, 18, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[4], false, MINI_SUB_ATLAS, 40f, false, true, 1, 18, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL11 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 6, 0, 2000, 1000, 2000);
                    for (int i = 0; i < 4; i++) {
                        level.addEnemyToManager(level.createEnemy("destroyer" + i, (i % 2 == 0 ? -LevelConstants.DESTROYER_WIDTH : (int) VIRTUAL_WIDTH), 0, (int) delays[i], false, LevelConstants.DESTROYER_ATLAS, 20f, true, false, 1, 20, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    }
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 200, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 20f, true, true, 1, 20, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[5], false, MINI_SUB_ATLAS, 40f, false, true, 1, 20, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL12 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    for (int i = 0; i < 3; i++) {
                        level.addEnemyToManager(level.createEnemy("sub" + i, (i % 2 == 0 ? -LevelConstants.ENEMY_SUB_WIDTH : (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH), 150 + i * 50, (int) delays[i], false, LevelConstants.ENEMY_SUB_ATLAS, 21f, true, true, 1, 22, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    }
                    level.addEnemyToManager(level.createEnemy("destroyer", (int) VIRTUAL_WIDTH, 0, (int) delays[3], false, LevelConstants.DESTROYER_ATLAS, 21f, true, false, 1, 22, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[4], false, MINI_SUB_ATLAS, 40f, false, true, 1, 22, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL13 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    for (int i = 0; i < 4; i++) {
                        level.addEnemyToManager(level.createEnemy("sub" + i, (i % 2 == 0 ? -LevelConstants.ENEMY_SUB_WIDTH : (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH), 100 + i * 40, (int) delays[i], false, LevelConstants.ENEMY_SUB_ATLAS, 22f, true, true, 1, 25, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    }
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[4], false, MINI_SUB_ATLAS, 40f, false, true, 1, 25, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL14 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 6, 0, 2000, 1000, 2000);
                    for (int i = 0; i < 4; i++) {
                        level.addEnemyToManager(level.createEnemy("destroyer" + i, (i % 2 == 0 ? -LevelConstants.DESTROYER_WIDTH : (int) VIRTUAL_WIDTH), 0, (int) delays[i], false, LevelConstants.DESTROYER_ATLAS, 23f, true, false, 1, 27, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    }
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 180, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 23f, true, true, 1, 27, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[5], false, MINI_SUB_ATLAS, 40f, false, true, 1, 27, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL15 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 11, 0, 2000, 1000, 2000);
                    int delayIndex = 0;
                    for (int i = 0; i < 3; i++) {
                        level.addEnemyToManager(level.createEnemy("destroyer" + i, -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[delayIndex++], false, LevelConstants.DESTROYER_ATLAS, 24f, true, false, 1, 30, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    }
                    for (int i = 0; i < 3; i++) {
                        level.addEnemyToManager(level.createEnemy("sub" + i, (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150 + i * 50, (int) delays[delayIndex++], false, LevelConstants.ENEMY_SUB_ATLAS, 24f, true, true, 1, 30, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    }
                    for (int i = 0; i < 5; i++) {
                        level.addEnemyToManager(level.createEnemy("mini" + i, (i % 2 == 0 ? -LevelConstants.ENEMY_SUB_WIDTH : (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH), 100 + i * 50, (int) delays[delayIndex++], false, MINI_SUB_ATLAS, 40f, false, true, 1, 35, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    }
                }
            };

            private static float[] generateDelays(Random random, int enemyCount, int firstMinDelay, int firstMaxDelay, int subsequentMinDelay, int subsequentMaxDelay) {
                float[] delays = new float[enemyCount];
                delays[0] = (firstMinDelay + random.nextFloat() * (firstMaxDelay - firstMinDelay)) / 1000.0f; // Convert ms to seconds
                for (int i = 1; i < enemyCount; i++) {
                    delays[i] = delays[i - 1] + (subsequentMinDelay + random.nextFloat() * (subsequentMaxDelay - subsequentMinDelay)) / 1000.0f;
                }
                return delays;
            }
        }

        public enum StageTwoLevels implements LevelInitializer {
            LEVEL1 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 3, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 20f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 20f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 40f, false, true, 1, 10, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL2 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 3, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, (int) delays[0], false, LevelConstants.TANKER_ATLAS, 30f, true, false, 1, 12, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, (int) delays[1], false, LevelConstants.TANKER_ATLAS, 30f, true, false, 1, 12, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 40f, false, true, 1, 12, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL3 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 4, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, (int) delays[0], false, LevelConstants.TANKER_ATLAS, 30f, true, false, 1, 15, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, (int) delays[1], false, LevelConstants.TANKER_ATLAS, 30f, true, false, 1, 15, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[2], false, LevelConstants.ENEMY_SUB_ATLAS, 30f, true, true, 1, 15, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 30f, true, true, 1, 15, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL4 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 4, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, (int) delays[0], false, LevelConstants.TANKER_ATLAS, 35f, true, false, 1, 20, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, (int) delays[1], false, LevelConstants.TANKER_ATLAS, 35f, true, false, 1, 20, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 45f, false, true, 1, 20, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 120, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 35f, true, true, 1, 20, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL5 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 4, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 40f, true, false, 1, 25, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 40f, true, false, 1, 25, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 50f, false, true, 1, 25, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 40f, true, true, 1, 25, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL6 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 45f, true, false, 1, 30, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 45f, true, false, 1, 30, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 55f, false, true, 1, 30, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 45f, true, true, 1, 30, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 45f, true, true, 1, 30, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL7 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 4, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, (int) delays[0], false, LevelConstants.TANKER_ATLAS, 50f, true, false, 1, 35, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, (int) delays[1], false, LevelConstants.TANKER_ATLAS, 50f, true, false, 1, 35, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 60f, false, true, 1, 35, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 50f, true, true, 1, 35, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL8 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 4, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 55f, true, false, 1, 40, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 55f, true, false, 1, 40, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 65f, false, true, 1, 40, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 55f, true, true, 1, 40, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL9 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, (int) delays[0], false, LevelConstants.TANKER_ATLAS, 60f, true, false, 1, 45, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, (int) delays[1], false, LevelConstants.TANKER_ATLAS, 60f, true, false, 1, 45, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 70f, false, true, 1, 45, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 60f, true, true, 1, 45, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 60f, true, true, 1, 45, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL10 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 65f, true, false, 1, 50, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 65f, true, false, 1, 50, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 75f, false, true, 1, 50, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 65f, true, true, 1, 50, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 65f, true, true, 1, 50, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL11 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, (int) delays[0], false, LevelConstants.TANKER_ATLAS, 70f, true, false, 1, 55, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, (int) delays[1], false, LevelConstants.TANKER_ATLAS, 70f, true, false, 1, 55, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 80f, false, true, 1, 55, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 70f, true, true, 1, 55, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 70f, true, true, 1, 55, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL12 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 75f, true, false, 1, 60, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 75f, true, false, 1, 60, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 85f, false, true, 1, 60, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 75f, true, true, 1, 60, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 75f, true, true, 1, 60, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL13 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, (int) delays[0], false, LevelConstants.TANKER_ATLAS, 80f, true, false, 1, 65, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, (int) delays[1], false, LevelConstants.TANKER_ATLAS, 80f, true, false, 1, 65, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 90f, false, true, 1, 65, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 80f, true, true, 1, 65, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 80f, true, true, 1, 65, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL14 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, (int) delays[0], false, LevelConstants.DESTROYER_ATLAS, 85f, true, false, 1, 70, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, (int) delays[1], false, LevelConstants.DESTROYER_ATLAS, 85f, true, false, 1, 70, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 95f, false, true, 1, 70, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 85f, true, true, 1, 70, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 85f, true, true, 1, 70, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            },
            LEVEL15 {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    float[] delays = generateDelays(random, 5, 0, 2000, 1000, 2000);
                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, (int) delays[0], false, LevelConstants.TANKER_ATLAS, 90f, true, false, 1, 75, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, (int) delays[1], false, LevelConstants.TANKER_ATLAS, 90f, true, false, 1, 75, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, (int) delays[2], false, LevelConstants.MINI_SUB_ATLAS, 100f, false, true, 1, 75, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[3], false, LevelConstants.ENEMY_SUB_ATLAS, 90f, true, true, 1, 75, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, (int) delays[4], false, LevelConstants.ENEMY_SUB_ATLAS, 90f, true, true, 1, 75, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
                }
            };

            private static float[] generateDelays(Random random, int enemyCount, int firstMinDelay, int firstMaxDelay, int subsequentMinDelay, int subsequentMaxDelay) {
                float[] delays = new float[enemyCount];
                delays[0] = (firstMinDelay + random.nextFloat() * (firstMaxDelay - firstMinDelay)) / 1000.0f; // Convert ms to seconds
                for (int i = 1; i < enemyCount; i++) {
                    delays[i] = delays[i - 1] + (subsequentMinDelay + random.nextFloat() * (subsequentMaxDelay - subsequentMinDelay)) / 1000.0f;
                }
                return delays;
            }
        }



//        public enum StageOneLevels implements LevelInitializer {
//            LEVEL1 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, -10, 2, false, LevelConstants.TANKER_ATLAS, 10f, false, false, 1, 10, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, false));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH, -10, 7, true, LevelConstants.TANKER_ATLAS, 10f, false, false, 1, 10, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, false));
//                }
//            },
//            LEVEL2 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.DESTROYER_WIDTH, 0, 10, true, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL3 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.ENEMY_SUB_WIDTH, 120, 15, false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, 15, false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL4 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.ENEMY_SUB_WIDTH, 120, 15, false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, 15, false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL5 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.DESTROYER_WIDTH, 0, 5, false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, 15, false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy4", -LevelConstants.ENEMY_SUB_WIDTH, 120, 15, false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL6 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH, 0, 9, false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy3", -LevelConstants.DESTROYER_WIDTH, 0, 11, false, LevelConstants.DESTROYER_ATLAS, 15f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy4", -LevelConstants.ENEMY_SUB_WIDTH, 120, 15, false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, 15, false, LevelConstants.ENEMY_SUB_ATLAS, 15f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL7 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, 6, false, LevelConstants.DESTROYER_ATLAS, 16f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH, 0, 8, false, LevelConstants.DESTROYER_ATLAS, 16f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, MINI_SUB_ATLAS, 40f, false, true, 1, 10, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));  // Mini enemy added
//                }
//            },
//            LEVEL8 {
//                @Override
//                public void initializeLevel(Level level) {
//                    for (int i = 0; i < 2; i++) {
//                        level.addEnemyToManager(level.createEnemy("destroyer" + i, (i % 2 == 0 ? (int) VIRTUAL_WIDTH : -LevelConstants.DESTROYER_WIDTH), 0, 7 + i, false, LevelConstants.DESTROYER_ATLAS, 17f, true, false, 1, 12, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    }
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 180, 15, false, LevelConstants.ENEMY_SUB_ATLAS, 17f, true, true, 1, 10, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, MINI_SUB_ATLAS, 40f, false, true, 1, 10, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL9 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 6, false, LevelConstants.DESTROYER_ATLAS, 18f, true, false, 1, 15, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", -LevelConstants.DESTROYER_WIDTH, 0, 9, false, LevelConstants.DESTROYER_ATLAS, 18f, true, false, 1, 15, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 200, 12, false, LevelConstants.ENEMY_SUB_ATLAS, 18f, true, true, 1, 15, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", -LevelConstants.ENEMY_SUB_WIDTH, 200, 14, false, LevelConstants.ENEMY_SUB_ATLAS, 18f, true, true, 1, 15, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, MINI_SUB_ATLAS, 40f, false, true, 1, 15, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL10 {
//                @Override
//                public void initializeLevel(Level level) {
//                    for (int i = 0; i < 3; i++) {
//                        level.addEnemyToManager(level.createEnemy("destroyer" + i, i % 2 == 0 ? (int) VIRTUAL_WIDTH : -LevelConstants.DESTROYER_WIDTH, 0, 7 + i, false, LevelConstants.DESTROYER_ATLAS, 19f, true, false, 1, 18, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    }
//                    level.addEnemyToManager(level.createEnemy("sub1", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, 14, false, LevelConstants.ENEMY_SUB_ATLAS, 19f, true, true, 1, 18, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, MINI_SUB_ATLAS, 40f, false, true, 1, 18, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL11 {
//                @Override
//                public void initializeLevel(Level level) {
//                    for (int i = 0; i < 4; i++) {
//                        level.addEnemyToManager(level.createEnemy("destroyer" + i, (i % 2 == 0 ? -LevelConstants.DESTROYER_WIDTH : (int) VIRTUAL_WIDTH), 0, 8 + i, false, LevelConstants.DESTROYER_ATLAS, 20f, true, false, 1, 20, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    }
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 200, 16, false, LevelConstants.ENEMY_SUB_ATLAS, 20f, true, true, 1, 20, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, MINI_SUB_ATLAS, 40f, false, true, 1, 20, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL12 {
//                @Override
//                public void initializeLevel(Level level) {
//                    for (int i = 0; i < 3; i++) {
//                        level.addEnemyToManager(level.createEnemy("sub" + i, (i % 2 == 0 ? -LevelConstants.ENEMY_SUB_WIDTH : (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH), 150 + i * 50, 15 + i, false, LevelConstants.ENEMY_SUB_ATLAS, 21f, true, true, 1, 22, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    }
//                    level.addEnemyToManager(level.createEnemy("destroyer", (int) VIRTUAL_WIDTH, 0, 10, false, LevelConstants.DESTROYER_ATLAS, 21f, true, false, 1, 22, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, MINI_SUB_ATLAS, 40f, false, true, 1, 22, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL13 {
//                @Override
//                public void initializeLevel(Level level) {
//                    for (int i = 0; i < 4; i++) {
//                        level.addEnemyToManager(level.createEnemy("sub" + i, (i % 2 == 0 ? -LevelConstants.ENEMY_SUB_WIDTH : (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH), 100 + i * 40, 17 + i, false, LevelConstants.ENEMY_SUB_ATLAS, 22f, true, true, 1, 25, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    }
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, MINI_SUB_ATLAS, 40f, false, true, 1, 25, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL14 {
//                @Override
//                public void initializeLevel(Level level) {
//                    for (int i = 0; i < 4; i++) {
//                        level.addEnemyToManager(level.createEnemy("destroyer" + i, (i % 2 == 0 ? -LevelConstants.DESTROYER_WIDTH : (int) VIRTUAL_WIDTH), 0, 10 + i, false, LevelConstants.DESTROYER_ATLAS, 23f, true, false, 1, 27, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    }
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 180, 16, false, LevelConstants.ENEMY_SUB_ATLAS, 23f, true, true, 1, 27, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, MINI_SUB_ATLAS, 40f, false, true, 1, 27, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL15 {
//                @Override
//                public void initializeLevel(Level level) {
//                    // Final boss-like level: heavy spam of mini enemies
//                    for (int i = 0; i < 3; i++) {
//                        level.addEnemyToManager(level.createEnemy("destroyer" + i, -LevelConstants.DESTROYER_WIDTH, 0, 12 + i, false, LevelConstants.DESTROYER_ATLAS, 24f, true, false, 1, 30, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                        level.addEnemyToManager(level.createEnemy("sub" + i, (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150 + i * 50, 17 + i, false, LevelConstants.ENEMY_SUB_ATLAS, 24f, true, true, 1, 30, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    }
//                    // Adding multiple mini enemies
//                    for (int i = 0; i < 5; i++) {
//                        level.addEnemyToManager(level.createEnemy("mini" + i, (i % 2 == 0 ? -LevelConstants.ENEMY_SUB_WIDTH : (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH), 100 + i * 50, 20, false, MINI_SUB_ATLAS, 40f, false, true, 1, 35, MINI_SUB_WIDTH, MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    }
//                }
//            },
//        }
//
//        public enum StageTwoLevels implements LevelInitializer {
//            LEVEL1 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, 6, false, LevelConstants.DESTROYER_ATLAS, 20f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, 6, false, LevelConstants.DESTROYER_ATLAS, 20f, true, false, 1, 10, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, LevelConstants.MINI_SUB_ATLAS, 40f, false, true, 1, 10, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL2 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, 8, false, LevelConstants.TANKER_ATLAS, 30f, true, false, 1, 12, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, 8, false, LevelConstants.TANKER_ATLAS, 30f, true, false, 1, 12, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, LevelConstants.MINI_SUB_ATLAS, 40f, false, true, 1, 12, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL3 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, 8, false, LevelConstants.TANKER_ATLAS, 30f, true, false, 1, 15, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, 8, false, LevelConstants.TANKER_ATLAS, 30f, true, false, 1, 15, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 120, 18, false, LevelConstants.ENEMY_SUB_ATLAS, 30f, true, true, 1, 15, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 120, 20, false, LevelConstants.ENEMY_SUB_ATLAS, 30f, true, true, 1, 15, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL4 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, 9, false, LevelConstants.TANKER_ATLAS, 35f, true, false, 1, 20, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, 9, false, LevelConstants.TANKER_ATLAS, 35f, true, false, 1, 20, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 6, false, LevelConstants.MINI_SUB_ATLAS, 45f, false, true, 1, 20, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 120, 22, false, LevelConstants.ENEMY_SUB_ATLAS, 35f, true, true, 1, 20, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL5 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, 12, false, LevelConstants.DESTROYER_ATLAS, 40f, true, false, 1, 25, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, 12, false, LevelConstants.DESTROYER_ATLAS, 40f, true, false, 1, 25, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 7, false, LevelConstants.MINI_SUB_ATLAS, 50f, false, true, 1, 25, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 25, false, LevelConstants.ENEMY_SUB_ATLAS, 40f, true, true, 1, 25, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL6 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, 14, false, LevelConstants.DESTROYER_ATLAS, 45f, true, false, 1, 30, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, 14, false, LevelConstants.DESTROYER_ATLAS, 45f, true, false, 1, 30, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 8, false, LevelConstants.MINI_SUB_ATLAS, 55f, false, true, 1, 30, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 28, false, LevelConstants.ENEMY_SUB_ATLAS, 45f, true, true, 1, 30, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, 30, false, LevelConstants.ENEMY_SUB_ATLAS, 45f, true, true, 1, 30, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL7 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, 15, false, LevelConstants.TANKER_ATLAS, 50f, true, false, 1, 35, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, 15, false, LevelConstants.TANKER_ATLAS, 50f, true, false, 1, 35, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 9, false, LevelConstants.MINI_SUB_ATLAS, 60f, false, true, 1, 35, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 32, false, LevelConstants.ENEMY_SUB_ATLAS, 50f, true, true, 1, 35, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL8 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, 18, false, LevelConstants.DESTROYER_ATLAS, 55f, true, false, 1, 40, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, 18, false, LevelConstants.DESTROYER_ATLAS, 55f, true, false, 1, 40, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 10, false, LevelConstants.MINI_SUB_ATLAS, 65f, false, true, 1, 40, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 35, false, LevelConstants.ENEMY_SUB_ATLAS, 55f, true, true, 1, 40, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL9 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, 20, false, LevelConstants.TANKER_ATLAS, 60f, true, false, 1, 45, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, 20, false, LevelConstants.TANKER_ATLAS, 60f, true, false, 1, 45, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 12, false, LevelConstants.MINI_SUB_ATLAS, 70f, false, true, 1, 45, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 38, false, LevelConstants.ENEMY_SUB_ATLAS, 60f, true, true, 1, 45, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, 40, false, LevelConstants.ENEMY_SUB_ATLAS, 60f, true, true, 1, 45, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL10 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, 22, false, LevelConstants.DESTROYER_ATLAS, 65f, true, false, 1, 50, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, 22, false, LevelConstants.DESTROYER_ATLAS, 65f, true, false, 1, 50, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 14, false, LevelConstants.MINI_SUB_ATLAS, 75f, false, true, 1, 50, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 42, false, LevelConstants.ENEMY_SUB_ATLAS, 65f, true, true, 1, 50, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, 45, false, LevelConstants.ENEMY_SUB_ATLAS, 65f, true, true, 1, 50, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL11 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, 25, false, LevelConstants.TANKER_ATLAS, 70f, true, false, 1, 55, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, 25, false, LevelConstants.TANKER_ATLAS, 70f, true, false, 1, 55, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 16, false, LevelConstants.MINI_SUB_ATLAS, 80f, false, true, 1, 55, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 48, false, LevelConstants.ENEMY_SUB_ATLAS, 70f, true, true, 1, 55, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, 50, false, LevelConstants.ENEMY_SUB_ATLAS, 70f, true, true, 1, 55, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL12 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, 28, false, LevelConstants.DESTROYER_ATLAS, 75f, true, false, 1, 60, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, 28, false, LevelConstants.DESTROYER_ATLAS, 75f, true, false, 1, 60, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 18, false, LevelConstants.MINI_SUB_ATLAS, 85f, false, true, 1, 60, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 55, false, LevelConstants.ENEMY_SUB_ATLAS, 75f, true, true, 1, 60, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, 58, false, LevelConstants.ENEMY_SUB_ATLAS, 75f, true, true, 1, 60, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL13 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, 30, false, LevelConstants.TANKER_ATLAS, 80f, true, false, 1, 65, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, 30, false, LevelConstants.TANKER_ATLAS, 80f, true, false, 1, 65, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 20, false, LevelConstants.MINI_SUB_ATLAS, 90f, false, true, 1, 65, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 60, false, LevelConstants.ENEMY_SUB_ATLAS, 80f, true, true, 1, 65, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, 62, false, LevelConstants.ENEMY_SUB_ATLAS, 80f, true, true, 1, 65, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL14 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.DESTROYER_WIDTH, 0, 35, false, LevelConstants.DESTROYER_ATLAS, 85f, true, false, 1, 70, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.DESTROYER_WIDTH, 0, 35, false, LevelConstants.DESTROYER_ATLAS, 85f, true, false, 1, 70, LevelConstants.DESTROYER_WIDTH, LevelConstants.DESTROYER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 22, false, LevelConstants.MINI_SUB_ATLAS, 95f, false, true, 1, 70, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 65, false, LevelConstants.ENEMY_SUB_ATLAS, 85f, true, true, 1, 70, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, 67, false, LevelConstants.ENEMY_SUB_ATLAS, 85f, true, true, 1, 70, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL15 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -LevelConstants.TANKER_WIDTH, 0, 40, false, LevelConstants.TANKER_ATLAS, 90f, true, false, 1, 75, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + LevelConstants.TANKER_WIDTH, 0, 40, false, LevelConstants.TANKER_ATLAS, 90f, true, false, 1, 75, LevelConstants.TANKER_WIDTH, LevelConstants.TANKER_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 25, false, LevelConstants.MINI_SUB_ATLAS, 100f, false, true, 1, 75, LevelConstants.MINI_SUB_WIDTH, LevelConstants.MINI_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub1", -LevelConstants.ENEMY_SUB_WIDTH, 150, 70, false, LevelConstants.ENEMY_SUB_ATLAS, 90f, true, true, 1, 75, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("sub2", (int) VIRTUAL_WIDTH + LevelConstants.ENEMY_SUB_WIDTH, 150, 75, false, LevelConstants.ENEMY_SUB_ATLAS, 90f, true, true, 1, 75, LevelConstants.ENEMY_SUB_WIDTH, LevelConstants.ENEMY_SUB_HEIGHT, ATTACK_RANGE, true));
//                }
//            }
//        }


//        public enum StageOneLevels implements LevelInitializer {
//            LEVEL1 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -250, -10, 2, false, "animations/tankeratlas.atlas", 10f, false, false, 1, 10, 250, 58, ATTACK_RANGE, false));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH, -10, 7, true, "animations/tankeratlas.atlas", 10f, false, false, 1, 10, 250, 58, ATTACK_RANGE, false));
//                }
//            },
//            LEVEL2 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", -250, 0, 10, true, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL3 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", -160, 120, 15, false, "animations/enemysub.atlas", 15f, true, true, 1, 10, 160, 32, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + 160, 120, 15, false, "animations/enemysub.atlas", 15f, true, true, 1, 10, 160, 32, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL4 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", -160, 120, 15, false, "animations/enemysub.atlas", 15f, true, true, 1, 10, 160, 32, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + 160, 120, 15, false, "animations/enemysub.atlas", 15f, true, true, 1, 10, 160, 32, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL5 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", -65, 0, 5, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + 160, 120, 15, false, "animations/enemysub.atlas", 15f, true, true, 1, 10, 160, 32, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy4", -65, 120, 15, false, "animations/enemysub.atlas", 15f, true, true, 1, 10, 160, 32, ATTACK_RANGE, true));
//                }
//            }
//            ,
//            LEVEL6 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 5, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH, 0, 9, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy3", -65, 0, 11, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy4", -160, 120, 15, false, "animations/enemysub.atlas", 15f, true, true, 1, 10, 160, 32, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 160, 120, 15, false, "animations/enemysub.atlas", 15f, true, true, 1, 10, 160, 32, ATTACK_RANGE, true));
//                }
//            }
//        }
//
//        public enum StageTwoLevels implements LevelInitializer {
//            LEVEL1 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", -250, -10, 2, false, "animations/tankeratlas.atlas", 10f, false, false, 1, 10, 250, 58, ATTACK_RANGE, false));
//                    level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH, -10, 5, false, "animations/tankeratlas.atlas", 10f, false, false, 1, 10, 250, 58, ATTACK_RANGE, false));
//                    level.addEnemyToManager(level.createEnemy("enemy4", (int) VIRTUAL_WIDTH, 0, 10, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("enemy5", -250, 0, 10, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL2 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 2, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 5, false, "animations/minisub.atlas", 40f, false, true, 1, 10, 46, 12, ATTACK_RANGE, true));
//                }
//            },
//            LEVEL3 {
//                @Override
//                public void initializeLevel(Level level) {
//                    level.addEnemyToManager(level.createEnemy("enemy1", (int) VIRTUAL_WIDTH, 0, 10, false, "animations/destroyeratlas.atlas", 15f, true, false, 1, 10, 200, 82, ATTACK_RANGE, true));
//                    level.addEnemyToManager(level.createEnemy("mini", (int) VIRTUAL_WIDTH + 46, 120, 1, false, "animations/minisub.atlas", 40f, false, true, 1, 10, 46, 12, ATTACK_RANGE, true));
//                }
//            }
//        }

        public enum DefaultLevels implements LevelInitializer {
            RANDOMIZED {
                @Override
                public void initializeLevel(Level level) {
                    Random random = new Random();
                    int numberOfEnemies = random.nextInt(5) + 3;
                    for (int i = 0; i < numberOfEnemies; i++) {
                        String[] enemyTypes = {"tanker-atlas.png", "destroyer-atlas.png", "enemy-sub1.png"};
                        String enemyType = enemyTypes[random.nextInt(enemyTypes.length)];

                        int spawnPosX = random.nextBoolean() ? -65 : (int) VIRTUAL_WIDTH + 65;
                        int spawnPosY = random.nextInt(100);
                        long delay = random.nextInt(30);
                        boolean flipX = random.nextBoolean();
                        float speed = 10f + random.nextFloat() * 5f;
                        boolean aggro = (enemyType.equals("enemy-sub1.png") || enemyType.equals("destroyer-atlas.png"));
                        boolean sub = enemyType.equals("enemy-sub1.png");
                        int healthMultiplier = 1 + random.nextInt(3);
                        int points = 10 * healthMultiplier;
                        float ordinanceRange = 150f + random.nextFloat() * 500f;

                        level.addEnemyToManager(level.createEnemy("enemy" + (i + 1), spawnPosX, spawnPosY, delay, flipX, enemyType, speed, aggro, sub, healthMultiplier, points, ordinanceRange));
                    }
                }
            }
        }
    }


    public void growEnemies() {
        if (totalLevels == 0) {
            previousLevel = totalLevels;
            levelCounter++;
        }

        if (totalLevels > previousLevel && totalLevels % 5 == 0) {
            maxHealth += HEALTH_INCREMENT;
            if (levelCounter % 10 == 0 && enemyDamage < 100) {
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

//    private enum LevelType {
//        LEVEL1 {
//            @Override
//            void initializeLevel(Level level) {
////                level.addEnemyToManager(level.createEnemy(
////                    "enemy1",
////                    (int) VIRTUAL_WIDTH,
////                    -10,
////                    3,
////                    true,
////                    "animations/tankeratlas.atlas",
////                    15f,
////                    false,
////                    false,
////                    1,
////                    10,
////                    250,
////                    58,
////                    150));
//                level.addEnemyToManager(level.createEnemy("enemy2",
//                    -250,
//                    -10,
//                    5,
//                    true,
//                    "animations/tankeratlas.atlas",
//                    10f,
//                    false,
//                    false,
//                    1,
//                    10,
//                    250,
//                    58,
//                    ATTACK_RANGE,
//                    false));
//                level.addEnemyToManager(level.createEnemy(
//                    "enemy3",
//                    (int) VIRTUAL_WIDTH,
//                    0,
//                    20,
//                    false,
//                    "animations/destroyeratlas.atlas",
//                    15f,
//                    true,
//                    false,
//                    1,
//                    10,
//                    200,
//                    82,
//                    ATTACK_RANGE,
//                    true));
//              level.addEnemyToManager(level.createEnemy(
//                  "enemy4",
//                  (int) VIRTUAL_WIDTH + 160,
//                  120,
//                  10,
//                  false,
//                  "animations/enemysub.atlas",
//                  13f,
//                  true,
//                  true,
//                  1,
//                  10,
//                  160,
//                  32,
//                  ATTACK_RANGE,
//                  true));
//
//                level.addEnemyToManager(level.createEnemy(
//                    "mini",
//                    (int) VIRTUAL_WIDTH + 46,
//                    120,
//                    1,
//                    false,
//                    "animations/minisub.atlas",
//                    40f,
//                    false,
//                    true,
//                    1,
//                    10,
//                    46,
//                    12,
//                    ATTACK_RANGE,
//                    true));
//
//                level.addEnemyToManager(level.createEnemy(
//                    "enemy5",
//                    -160,
//                    320,
//                    10,
//                    false,
//                    "animations/enemysub.atlas",
//                    13f,
//                    true,
//                    true,
//                    1,
//                    10,
//                    160,
//                    32,
//                    ATTACK_RANGE,
//                    true));
//
//                //  level.addEnemyToManager(level.createEnemy("enemy4", (int) VIRTUAL_WIDTH + 65, 50, 13, false, "enemy-sub1.png", 13f, true, true, 1, 10, 200));
//             //   level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 65, 80, 20, false, "enemy-sub1.png", 13f, true, true, 1, 10, 200));
//            }
//
//        },
//        LEVEL2 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker2-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//            }
//        },
//        LEVEL3 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10,150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "destroyer-atlas.png", 15f, true, false, 1, 10,150));
//            }
//        },
//        LEVEL4 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "tanker2-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + 64, 0, 15, false, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy4", -65, 64, 20, true, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//            }
//        },
//        LEVEL5 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 65, 30, 10, false, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy3", -65, 40, 14, true, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//            }
//        },
//        LEVEL6 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", (int) VIRTUAL_WIDTH + 64, 0, 5, false, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy3", -65, 60, 15, true, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy4", -65, 0, 25, true, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 65, 20, 30, false, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//            }
//        },
//        LEVEL7 {
//            @Override
//            void initializeLevel(Level level) {
//                level.addEnemyToManager(level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy2", -65, 10, 10, true, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy3", (int) VIRTUAL_WIDTH + 65, 0, 20, false, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy4", -65, 0, 30, true, "destroyer-atlas.png", 15f, true, false, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy5", (int) VIRTUAL_WIDTH + 65, 35, 40, false, "enemy-sub1.png", 13f, true, true, 1, 10, 150));
//                level.addEnemyToManager(level.createEnemy("enemy6", -65, 0, 45, true, "tanker2-atlas.png", 10f, false, false, 1, 10, 150));
//            }
//        },
//        RANDOMIZED {
//            @Override
//            void initializeLevel(Level level) {
//                Random random = new Random();
//                int numberOfEnemies = random.nextInt(5) + 3; // Random number of enemies between 3 and 7
//
//                for (int i = 0; i < numberOfEnemies; i++) {
//                    String[] enemyTypes = {"tanker-atlas.png", "destroyer-atlas.png", "enemy-sub1.png"};
//                    String enemyType = enemyTypes[random.nextInt(enemyTypes.length)];
//
//                    int spawnPosX = random.nextBoolean() ? -65 : (int) VIRTUAL_WIDTH + 65; // Random left or right spawn
//                    int spawnPosY = random.nextInt(100); // Random Y position between 0 and 100
//                    long delay = random.nextInt(30); // Random delay between 0 and 30
//                    boolean flipX = random.nextBoolean();
//                    float speed = 10f + random.nextFloat() * 5f; // Random speed between 10 and 15
//                    boolean aggro = (enemyType.equals("enemy-sub1.png") || enemyType.equals("destroyer-atlas.png"));
//                    boolean sub = enemyType.equals("enemy-sub1.png");
//                    int healthMultiplier = 1 + random.nextInt(3); // Random health multiplier between 1 and 3
//                    int points = 10 * healthMultiplier;
//                    float ordinanceRange = 150f + random.nextFloat() * 500f;
//
//                    level.addEnemyToManager(level.createEnemy(
//                        "enemy" + (i + 1), spawnPosX, spawnPosY, delay, flipX, enemyType, speed, aggro, sub, healthMultiplier, points, ordinanceRange
//                    ));
//                }
//            }
//        };
//
//        abstract void initializeLevel(Level level);
//    }

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

    public int getStage() {
        return stage;
    }

    public float getEnemyDamage() {
        return enemyDamage;
    }

    public void setEnemyDamage(float enemyDamage) {
        this.enemyDamage = enemyDamage;
    }
}

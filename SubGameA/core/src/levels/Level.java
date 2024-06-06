//package levels;
//
//
//import entities.enemies.Enemy;
//import entities.enemies.EnemyBuilder;
//import entities.enemies.EnemyManager;
//import UI.game.GameScreen;
//
//import static utilities.Constants.Game.WORLD_WIDTH;
//
//
//public class Level {
//
//    private int currentScreen;
//    private int totalLevels = 0;
//    private EnemyManager enemyManager;
//    private int currentHealth = 100;
//    private int maxHealth = currentHealth;
//    private GameScreen gameScreen;
//
//    public Level(EnemyManager enemyManager, GameScreen gameScreen) {
//        this.enemyManager = enemyManager;
//        this.gameScreen = gameScreen;
//    }
//
//    public void levelSelector() {
//        maxHealth += 50;
//        currentHealth = maxHealth;
//
//        switch ( LevelType.values()[currentScreen - 1] ) {
//            case LEVEL1:
//                level1();
//                break;
//            case LEVEL2:
//                level2();
//                break;
//            case LEVEL3:
//                level3();
//                break;
//            case LEVEL4:
//                level4();
//                break;
//            case LEVEL5:
//                level5();
//                break;
//            default:
//                currentScreen = 0;
//        }
//    }
//
//
//    private void level1() {
//        Enemy enemy1 = new EnemyBuilder(gameScreen)
//                .withName("enemy1")
//                .withSpawnPosX(-65)
//                .withFlipX(-1)
//                .withSpriteAtlas("tanker-atlas.png")
//                .withSpeed(10f)
//                .withMaxHealth(currentHealth)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
//        enemyManager.addEnemy(enemy1);
//    }
//
//    private void level2() {
//        Enemy enemy1 = new EnemyBuilder(gameScreen)
//                .withName("enemy1")
//                .withSpawnPosX(-65)
//                .withFlipX(-1)
//                .withSpriteAtlas("tanker-atlas.png")
//                .withSpeed(10f)
//                .withMaxHealth(currentHealth)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
//        Enemy enemy2 = new EnemyBuilder(gameScreen)
//                .withName("enemy2")
//                .withDelay(5)
//                .withSpawnPosX((int) WORLD_WIDTH + 64)
//                .withSpriteAtlas("tanker-atlas.png")
//                .withSpeed(10f)
//                .withMaxHealth(currentHealth)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
//        enemyManager.addEnemy(enemy1);
//        enemyManager.addEnemy(enemy2);
//    }
//
//    private void level3() {
//
//        Enemy enemy1 = new EnemyBuilder(gameScreen)
//                .withName("enemy1")
//                .withDelay(24)
//                .withSpawnPosX(-65)
//                .withSpawnPosY(100)
//                .withSpriteAtlas("enemy-sub1.png")
//                .withSpeed(13f)
//                .withAggro(true)
//                .withMaxHealth(currentHealth)
//                .withSub(true)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
//        Enemy enemy2 = new EnemyBuilder(gameScreen)
//                .withName("enemy2")
//                .withSpawnPosX(-65)
//                .withFlipX(-1)
//                .withSpriteAtlas("tanker-atlas.png")
//                .withSpeed(10f)
//                .withMaxHealth(currentHealth)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
//
////        Enemy enemy2 = new EnemyBuilder(gamePlayScreen)
////                .withDelay(3)
////                .withSpawnPosX((int) WORLD_WIDTH + 65)
////                .withSpriteAtlas("tanker-atlas.png")
////                .withSpeed(11f)
////                .withCurrentHealth(maxHealth)
////                .withMaxHealth(currentHealth)
////                .withEnemyPoints(10)
////                .withEnemyWidth(64)
////                .withEnemyHeight(32)
////                .build();
////
////        Enemy enemy3 = new EnemyBuilder(gamePlayScreen)
////                .withDelay(10)
////                .withSpawnPosX((int) WORLD_WIDTH + 65)
////                .withSpawnPosY(100)
////                .withSpriteAtlas("tanker2-atlas.png")
////                .withSpeed(10f)
////                .withMaxHealth(maxHealth)
////                .withCurrentHealth(currentHealth)
////                .withEnemyPoints(10)
////                .withEnemyWidth(64)
////                .withEnemyHeight(32)
////                .build();
////
////        Enemy enemy4 = new EnemyBuilder(gameScreen)
////                .withName("enemy4")
////                .withDelay(0)
////                .withSpawnPosX((int) WORLD_WIDTH + 64)
////                .withSpriteAtlas("destroyer-atlas.png")
////                .withSpeed(15f)
////                .withAggro(true)
////                .withMaxHealth(maxHealth)
////                .withEnemyPoints(10)
////                .withEnemyWidth(64)
////                .withEnemyHeight(32)
////                .build();
////
////        Enemy enemy5 = new EnemyBuilder(gamePlayScreen)
////                .withDelay(10)
////                .withSpawnPosX((int) WORLD_WIDTH + 64)
////                .withSpriteAtlas("destroyer-atlas.png")
////                .withSpeed(20f)
////                .withAggro(true)
////                .withMaxHealth(maxHealth)
////                .withCurrentHealth(currentHealth)
////                .withEnemyPoints(10)
////                .withEnemyWidth(64)
////                .withEnemyHeight(32)
////                .build();
//
////        Enemy enemy6 = new EnemyBuilder(gamePlayScreen)
////                .withName("Enemy6")
////                .withDelay(0)
////                .withSpawnPosX((int) WORLD_WIDTH + 65)
////                .withSpawnPosY(100)
////                .withSpriteAtlas("enemy-sub1.png")
////                .withSpeed(13f)
////                .withAggro(true)
////                .withMaxHealth(maxHealth)
////                .withCurrentHealth(currentHealth)
////                .withSub(true)
////                .withEnemyPoints(10)
////                .withEnemyWidth(64)
////                .withEnemyHeight(32)
////                .build();
//
//        enemyManager.addEnemy(enemy1);
//        enemyManager.addEnemy(enemy2);
//    }
//
//    private void level4() {
//        Enemy enemy1 = new EnemyBuilder(gameScreen)
//                .withName("enemy1")
//                .withDelay(5)
//                .withSpawnPosX((int) WORLD_WIDTH + 64)
//                .withSpriteAtlas("destroyer-atlas.png")
//                .withSpeed(15f)
//                .withAggro(true)
//                .withMaxHealth(currentHealth)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
//        Enemy enemy2 = new EnemyBuilder(gameScreen)
//                .withName("enemy2")
//                .withDelay(15)
//                .withSpawnPosX((int) WORLD_WIDTH + 65)
//                .withSpawnPosY(100)
//                .withSpriteAtlas("enemy-sub1.png")
//                .withSpeed(13f)
//                .withAggro(true)
//                .withMaxHealth(currentHealth)
//                .withSub(true)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
//        enemyManager.addEnemy(enemy1);
//        enemyManager.addEnemy(enemy2);
//    }
//
//    private void level5() {
//        Enemy enemy1 = new EnemyBuilder(gameScreen)
//                .withName("enemy1")
//                .withSpawnPosX(-125)
//                .withSpriteAtlas("cv6-atlas.png")
//                .withSpeed(13f)
//                .withAggro(true)
//                .withMaxHealth(currentHealth * 3)
//                .withEnemyPoints(50)
//                .withEnemyWidth(128)
//                .withEnemyHeight(32)
//                .build();
//
//        enemyManager.addEnemy(enemy1);
//    }
//
//
//    public void setCurrentScreen(int currentScreen) {
//        this.currentScreen = currentScreen;
//    }
//
//    public int getCurrentScreen() {
//        return currentScreen;
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
//    public float getCurrentHealth() {
//        return currentHealth;
//    }
//
//    public void setCurrentHealth(int currentHealth) {
//        this.currentHealth = currentHealth;
//    }
//
//    public float getMaxHealth() {
//        return maxHealth;
//    }
//
//    public void setMaxHealth(int maxHealth) {
//        this.maxHealth = maxHealth;
//    }
//
//    private enum LevelType {
//        LEVEL1,
//        LEVEL2,
//        LEVEL3,
//        LEVEL4,
//        LEVEL5,
//        LOOP
//    }
//}

package levels;

import entities.enemies.Enemy;
import entities.enemies.EnemyBuilder;
import entities.enemies.EnemyManager;
import UI.game.GameScreen;

import static utilities.Constants.Game.WORLD_WIDTH;

public class Level {

    private int currentScreen;
    private int totalLevels = 0;
    private final EnemyManager enemyManager;
    private int currentHealth = 100;
    private int maxHealth = currentHealth;
    private final GameScreen gameScreen;

    public Level(EnemyManager enemyManager, GameScreen gameScreen) {
        this.enemyManager = enemyManager;
        this.gameScreen = gameScreen;
    }

    /**
     * Selects and initializes the level based on the current screen.
     */
    public void levelSelector() {
        maxHealth += 50;
        currentHealth = maxHealth;

        if (currentScreen > 0 && currentScreen <= LevelType.values().length) {
            LevelType levelType = LevelType.values()[currentScreen - 1];
            levelType.initializeLevel(this);
        } else {
            currentScreen = 0;
        }
    }

    private void addEnemyToManager(Enemy enemy) {
        enemyManager.addEnemy(enemy);
    }

    private Enemy createEnemy(String name, int spawnPosX, int spawnPosY, int delay, boolean flipX, String spriteAtlas, float speed, boolean aggro, boolean sub, int healthMultiplier, int points) {
        return createEnemy(name, spawnPosX, spawnPosY, delay, flipX, spriteAtlas, speed, aggro, sub, healthMultiplier, points, 64, 32);
    }

    private Enemy createEnemy(String name, int spawnPosX, int spawnPosY, int delay, boolean flipX, String spriteAtlas, float speed, boolean aggro, boolean sub, int healthMultiplier, int points, int width, int height) {
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
                .build();
    }


    private enum LevelType {
        LEVEL1 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10);
                level.addEnemyToManager(enemy1);
            }
        },
        LEVEL2 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -65, 0, 0, true, "tanker2-atlas.png", 10f, false, false, 1, 10);
                Enemy enemy2 = level.createEnemy("enemy2", (int) WORLD_WIDTH + 64, 0, 5, false, "tanker-atlas.png", 10f, false, false, 1, 10);
                level.addEnemyToManager(enemy1);
                level.addEnemyToManager(enemy2);
            }
        },
        LEVEL3 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10);
                Enemy enemy2 = level.createEnemy("enemy2", (int) WORLD_WIDTH + 64, 0, 5, false, "destroyer-atlas.png", 15f, true, false, 1, 10);
                level.addEnemyToManager(enemy1);
                level.addEnemyToManager(enemy2);
            }
        },
        LEVEL4 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10);
                Enemy enemy2 = level.createEnemy("enemy2", (int) WORLD_WIDTH + 64, 0, 2, false, "tanker2-atlas.png", 10f, false, false, 1, 10);
                Enemy enemy3 = level.createEnemy("enemy3", (int) WORLD_WIDTH + 64, 0, 5, false, "destroyer-atlas.png", 15f, true, false, 1, 10);
                Enemy enemy4 = level.createEnemy("enemy4", -65, 64, 10, true, "destroyer-atlas.png", 15f, true, false, 1, 10);
                level.addEnemyToManager(enemy1);
                level.addEnemyToManager(enemy2);
                level.addEnemyToManager(enemy3);
                level.addEnemyToManager(enemy4);
            }
        },
        LEVEL5 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10);
                Enemy enemy2 = level.createEnemy("enemy2", (int) WORLD_WIDTH + 65, 100, 10, false, "enemy-sub1.png", 13f, true, true, 1, 10);
                Enemy enemy3 = level.createEnemy("enemy3", -65, 100, 14, true, "enemy-sub1.png", 13f, true, true, 1, 10);
                level.addEnemyToManager(enemy1);
                level.addEnemyToManager(enemy2);
                level.addEnemyToManager(enemy3);
            }
        },
        LEVEL6 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10);
                Enemy enemy2 = level.createEnemy("enemy2", (int) WORLD_WIDTH + 64, 0, 2, false, "destroyer-atlas.png", 15f, true, false, 1, 10);
                Enemy enemy3 = level.createEnemy("enemy3", -65, 60, 5, true, "enemy-sub1.png", 13f, true, true, 1, 10);
                Enemy enemy4 = level.createEnemy("enemy4", -65, 0, 15, true, "destroyer-atlas.png", 15f, true, false, 1, 10);
                Enemy enemy5 = level.createEnemy("enemy5", (int) WORLD_WIDTH + 65, 20, 20, false, "enemy-sub1.png", 13f, true, true, 1, 10);
                level.addEnemyToManager(enemy1);
                level.addEnemyToManager(enemy2);
                level.addEnemyToManager(enemy3);
                level.addEnemyToManager(enemy4);
                level.addEnemyToManager(enemy5);
            }
        },
        LEVEL7 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -65, 0, 0, true, "tanker-atlas.png", 10f, false, false, 1, 10);
                level.addEnemyToManager(enemy1);
            }
        },
        LEVEL8 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -125, 0, 0, false, "cv6-atlas.png", 13f, true, false, 3, 50,128,32);
                level.addEnemyToManager(enemy1);
            }
        },
        LEVEL9 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -125, 0, 0, false, "cv6-atlas.png", 13f, true, false, 3, 50,128,32);
                level.addEnemyToManager(enemy1);
            }
        },
        LEVEL10 {
            @Override
            void initializeLevel(Level level) {
                Enemy enemy1 = level.createEnemy("enemy1", -125, 0, 0, false, "cv6-atlas.png", 13f, true, false, 3, 50,128,32);
                level.addEnemyToManager(enemy1);
            }
        },
        LOOP {
            @Override
            void initializeLevel(Level level) {
                level.setCurrentScreen(1);
                LEVEL1.initializeLevel(level);
            }
        };

        abstract void initializeLevel(Level level);
    }

    public void setCurrentScreen(int currentScreen) {
        this.currentScreen = currentScreen;
    }

    public int getCurrentScreen() {
        return currentScreen;
    }

    public int getTotalLevels() {
        return totalLevels;
    }

    public void setTotalLevels(int totalLevels) {
        this.totalLevels = totalLevels;
    }

    public float getCurrentHealth() {
        return currentHealth;
    }

    public void setCurrentHealth(int currentHealth) {
        this.currentHealth = currentHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }
}

package levels;


import entities.enemies.Enemy;
import entities.enemies.EnemyBuilder;
import entities.enemies.EnemyManager;
import UI.game.GameScreen;

import static utilities.Constants.Game.WORLD_WIDTH;


public class Level {

    private int currentScreen;
    private int totalLevels = 0;
    private EnemyManager enemyManager;
    private int currentHealth = 100;
    private int maxHealth = currentHealth;
    private GameScreen gameScreen;

    public Level(EnemyManager enemyManager, GameScreen gameScreen) {
        this.enemyManager = enemyManager;
        this.gameScreen = gameScreen;
    }

    public void levelSelector() {
        maxHealth += 10;
        currentHealth = maxHealth;

        switch ( LevelType.values()[currentScreen - 1] ) {
            case LEVEL1:
                level1();
                break;
            case LEVEL2:
                level2();
                break;
            case LEVEL3:
                level3();
                break;
            case LEVEL4:
                level4();
                break;
            default:
                currentScreen = 0;
        }
    }



    private void level1() {
        Enemy enemy1 = new EnemyBuilder(gameScreen)
                .withName("enemy1")
                .withSpawnPosX(-65)
                .withFlipX(-1)
                .withSpriteAtlas("tanker-atlas.png")
                .withSpeed(10f)
                .withMaxHealth(currentHealth)
                .withEnemyPoints(10)
                .withEnemyWidth(64)
                .withEnemyHeight(32)
                .build();

        enemyManager.addEnemy(enemy1);
    }

    private void level2() {
        Enemy enemy1 = new EnemyBuilder(gameScreen)
                .withName("enemy1")
                .withSpawnPosX(-65)
                .withFlipX(-1)
                .withSpriteAtlas("tanker-atlas.png")
                .withSpeed(10f)
                .withMaxHealth(currentHealth)
                .withEnemyPoints(10)
                .withEnemyWidth(64)
                .withEnemyHeight(32)
                .build();

        Enemy enemy2 = new EnemyBuilder(gameScreen)
                .withName("enemy2")
                .withDelay(5)
                .withSpawnPosX((int) WORLD_WIDTH + 64)
                .withSpriteAtlas("destroyer-atlas.png")
                .withSpeed(15f)
                .withAggro(true)
                .withMaxHealth(currentHealth)
                .withEnemyPoints(10)
                .withEnemyWidth(64)
                .withEnemyHeight(32)
                .build();

        Enemy enemy3 = new EnemyBuilder(gameScreen)
                .withName("enemy3")
                .withDelay(15)
                .withSpawnPosX((int) WORLD_WIDTH + 65)
                .withSpawnPosY(100)
                .withSpriteAtlas("enemy-sub1.png")
                .withSpeed(13f)
                .withAggro(true)
                .withMaxHealth(currentHealth)
                .withSub(true)
                .withEnemyPoints(10)
                .withEnemyWidth(64)
                .withEnemyHeight(32)
                .build();

        Enemy enemy4 = new EnemyBuilder(gameScreen)
                .withName("enemy4")
                .withDelay(24)
                .withSpawnPosX(-65)
                .withSpawnPosY(100)
                .withSpriteAtlas("enemy-sub1.png")
                .withSpeed(13f)
                .withAggro(true)
                .withMaxHealth(currentHealth)
                .withSub(true)
                .withEnemyPoints(10)
                .withEnemyWidth(64)
                .withEnemyHeight(32)
                .build();

        enemyManager.addEnemy(enemy1);
        enemyManager.addEnemy(enemy2);
        enemyManager.addEnemy(enemy3);
        enemyManager.addEnemy(enemy4);
    }

    private void level3() {
        Enemy enemy1 = new EnemyBuilder(gameScreen)
                .withName("enemy1")
                .withSpawnPosX(-65)
                .withFlipX(-1)
                .withSpriteAtlas("tanker-atlas.png")
                .withSpeed(10f)
                .withMaxHealth(currentHealth)
                .withEnemyPoints(10)
                .withEnemyWidth(64)
                .withEnemyHeight(32)
                .build();


        //        Enemy enemy1 = new EnemyBuilder(gamePlayScreen)
//                .withSpawnPosX(-65)
//                .withFlipX(-1)
//                .withSpriteAtlas("tanker-atlas.png")
//                .withSpeed(10f)
//                .withCurrentHealth(maxHealth)
//                .withMaxHealth(currentHealth)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
//        Enemy enemy2 = new EnemyBuilder(gamePlayScreen)
//                .withDelay(3)
//                .withSpawnPosX((int) WORLD_WIDTH + 65)
//                .withSpriteAtlas("tanker-atlas.png")
//                .withSpeed(11f)
//                .withCurrentHealth(maxHealth)
//                .withMaxHealth(currentHealth)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
//        Enemy enemy3 = new EnemyBuilder(gamePlayScreen)
//                .withDelay(10)
//                .withSpawnPosX((int) WORLD_WIDTH + 65)
//                .withSpawnPosY(100)
//                .withSpriteAtlas("tanker2-atlas.png")
//                .withSpeed(10f)
//                .withMaxHealth(maxHealth)
//                .withCurrentHealth(currentHealth)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();
//
        Enemy enemy4 = new EnemyBuilder(gameScreen)
                .withName("enemy4")
                .withDelay(0)
                .withSpawnPosX((int) WORLD_WIDTH + 64)
                .withSpriteAtlas("destroyer-atlas.png")
                .withSpeed(15f)
                .withAggro(true)
                .withMaxHealth(maxHealth)
                .withEnemyPoints(10)
                .withEnemyWidth(64)
                .withEnemyHeight(32)
                .build();
//
//        Enemy enemy5 = new EnemyBuilder(gamePlayScreen)
//                .withDelay(10)
//                .withSpawnPosX((int) WORLD_WIDTH + 64)
//                .withSpriteAtlas("destroyer-atlas.png")
//                .withSpeed(20f)
//                .withAggro(true)
//                .withMaxHealth(maxHealth)
//                .withCurrentHealth(currentHealth)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();

//        Enemy enemy6 = new EnemyBuilder(gamePlayScreen)
//                .withName("Enemy6")
//                .withDelay(0)
//                .withSpawnPosX((int) WORLD_WIDTH + 65)
//                .withSpawnPosY(100)
//                .withSpriteAtlas("enemy-sub1.png")
//                .withSpeed(13f)
//                .withAggro(true)
//                .withMaxHealth(maxHealth)
//                .withCurrentHealth(currentHealth)
//                .withSub(true)
//                .withEnemyPoints(10)
//                .withEnemyWidth(64)
//                .withEnemyHeight(32)
//                .build();

//        enemyManager.addEnemy(enemy1);
//        enemyManager.addEnemy(enemy2);
//        enemyManager.addEnemy(enemy3);
        enemyManager.addEnemy(enemy4);
//        enemyManager.addEnemy(enemy5);
//        enemyManager.addEnemy(enemy6);
    }

    private void level4() {
        Enemy enemy1 = new EnemyBuilder(gameScreen)
                .withName("enemy1")
                .withSpawnPosX(-125)
                .withSpriteAtlas("cv6-atlas.png")
                .withSpeed(13f)
                .withAggro(true)
                .withMaxHealth(currentHealth * 3)
                .withEnemyPoints(50)
                .withEnemyWidth(128)
                .withEnemyHeight(32)
                .build();

        enemyManager.addEnemy(enemy1);
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

    private enum LevelType {
        LEVEL1,
        LEVEL2,
        LEVEL3,
        LEVEL4,
        LOOP
    }
}
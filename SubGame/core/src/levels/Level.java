package levels;


import entities.Enemy;
import entities.EnemyBuilder;
import entities.EnemyManager;

import static utilz.Constants.Game.*;

public class Level {

    private int currentScreen;

    private int totalLevels  = 0 ;
    private EnemyManager enemyManager;

    private float currentHealth = 100f;

    private float maxHealth = currentHealth;

    public Level(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public void levelSelector() {
        maxHealth += totalLevels;
        currentHealth = maxHealth;
        switch ( currentScreen ) {
            case 1: {
                level1();
                break;
            }
            case 2:{
                level2();
                break;
            }
            case 3:{
                level3();
                break;
            }
            default: {
                currentScreen = 0;
            }
        }
    }

    private void level1() {
        Enemy enemy1 = new EnemyBuilder()
                .withSpawnPosX(-125)
                .withSpriteAtlas("cv6-atlas.png")
                .withSpeed(13f)
                .withAggro(true)
                .withCurrentHealth(maxHealth )
                .withMaxHealth(currentHealth )
                .withEnemyPoints(200)
                .withEnemyWidth(128)
                .build();

        enemyManager.addEnemy(enemy1);
    }

    private void level2() {

        Enemy enemy1 = new EnemyBuilder()
                .withSpawnPosX(-65)
                .withFlipX(-1)
                .withSpriteAtlas("tanker-atlas.png")
                .withSpeed(10f)
                .withCurrentHealth(maxHealth )
                .withMaxHealth(currentHealth )
                .withEnemyPoints(10)
                .build();

        Enemy enemy2 = new EnemyBuilder()
                .withDelay(5)
                .withSpawnPosX((int) WORLD_WIDTH + 64)
                .withSpriteAtlas("destroyer-atlas.png")
                .withSpeed(15f)
                .withAggro(true)
                .withCurrentHealth(maxHealth )
                .withMaxHealth(currentHealth )
                .withEnemyPoints(10)
                .build();

        Enemy enemy3 = new EnemyBuilder()
                .withDelay(15)
                .withSpawnPosX((int) WORLD_WIDTH + 65)
                .withSpawnPosY(100)
                .withSpriteAtlas("enemy-sub1.png")
                .withSpeed(13f)
                .withAggro(true)
                .withMaxHealth(maxHealth)
                .withCurrentHealth(currentHealth)
                .withSub(true)
                .withEnemyPoints(10)
                .build();

        Enemy enemy4 = new EnemyBuilder()
                .withDelay(24)
                .withSpawnPosX(-65)
                .withSpawnPosY(100)
                .withSpriteAtlas("enemy-sub1.png")
                .withSpeed(13f)
                .withAggro(true)
                .withMaxHealth(maxHealth)
                .withCurrentHealth(currentHealth)
                .withSub(true)
                .withEnemyPoints(10)
                .build();

        enemyManager.addEnemy(enemy1);
        enemyManager.addEnemy(enemy2);
        enemyManager.addEnemy(enemy3);
        enemyManager.addEnemy(enemy4);
    }

    private void level3() {
        Enemy enemy1 = new EnemyBuilder()
                .withSpawnPosX(-65)
                .withFlipX(-1)
                .withSpriteAtlas("tanker-atlas.png")
                .withSpeed(10f)
                .withCurrentHealth(maxHealth )
                .withMaxHealth(currentHealth )
                .withEnemyPoints(10)
                .build();

        Enemy enemy2 = new EnemyBuilder()
                .withDelay(3)
                .withSpawnPosX((int) WORLD_WIDTH + 65)
                .withSpriteAtlas("tanker-atlas.png")
                .withSpeed(11f)
                .withCurrentHealth(maxHealth)
                .withMaxHealth(currentHealth)
                .withEnemyPoints(10)
                .build();

        Enemy enemy3 = new EnemyBuilder()
                .withDelay(10)
                .withSpawnPosX((int) WORLD_WIDTH + 65)
                .withSpawnPosY(100)
                .withSpriteAtlas("tanker2-atlas.png")
                .withSpeed(10f)
                .withMaxHealth(maxHealth)
                .withCurrentHealth(currentHealth)
                .withEnemyPoints(10)
                .build();


        Enemy enemy4 = new EnemyBuilder()
                .withDelay(4)
                .withSpawnPosX((int) WORLD_WIDTH + 64)
                .withSpriteAtlas("destroyer-atlas.png")
                .withSpeed(15f)
                .withAggro(true)
                .withMaxHealth(maxHealth)
                .withCurrentHealth(currentHealth)
                .withEnemyPoints(10)
                .build();

        Enemy enemy5 = new EnemyBuilder()
                .withDelay(10)
                .withSpawnPosX((int) WORLD_WIDTH + 64)
                .withSpriteAtlas("destroyer-atlas.png")
                .withSpeed(20f)
                .withAggro(true)
                .withMaxHealth(maxHealth)
                .withCurrentHealth(currentHealth)
                .withEnemyPoints(10)
                .build();

        Enemy enemy6 = new EnemyBuilder()
                .withDelay(12)
                .withSpawnPosX((int) WORLD_WIDTH + 65)
                .withSpawnPosY(100)
                .withSpriteAtlas("enemy-sub1.png")
                .withSpeed(13f)
                .withAggro(true)
                .withMaxHealth(maxHealth)
                .withCurrentHealth(currentHealth)
                .withSub(true)
                .withEnemyPoints(10)
                .build();

        enemyManager.addEnemy(enemy1);
        enemyManager.addEnemy(enemy2);
        enemyManager.addEnemy(enemy3);
        enemyManager.addEnemy(enemy4);
        enemyManager.addEnemy(enemy5);
        enemyManager.addEnemy(enemy6);
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

    public void setCurrentHealth(float currentHealth) {
        this.currentHealth = currentHealth;
    }

    public float getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(float maxHealth) {
        this.maxHealth = maxHealth;
    }
    //    BufferedImage img;

//    public Level(BufferedImage img) {
//        this.img = img;
//        loadLevel();
//    }
//
//    private void loadLevel() {
//        for(int y = 0; y < img.getHeight(); y++) {
//            for (int x = 0; x < img.getWidth(); x++) {
//                Color c = new Color(img.getRGB(x, y));
//                int red = c.getRed();
//
////                System.out.println("at x:" + x + " y:" + y + "  red color:" + red);
//            }
//        }
//
//    }



}

//
//package levels;
//
//import entities.Enemy;
//import entities.EnemyManager;
//
//import static utilz.Constants.Game.*;
//
//public class Level {
//
//    private int currentScreen;
//    private int totalLevels = 0;
//    private EnemyManager enemyManager;
//    private float currentHealth = 100f;
//    private float maxHealth = currentHealth;
//
//    private Enemy enemy1 = new Enemy(0, -65, -1, "tanker-atlas.png", 0.5f, false, maxHealth, currentHealth, 10);
//    private Enemy enemy2 = new Enemy(2, (int) WORLD_WIDTH + 64, 1, "destroyer-atlas.png", 1.5f, true, maxHealth, currentHealth, 10);
//    private Enemy enemy3 = new Enemy(5, (int) WORLD_WIDTH + 65, 100, 1, "enemy-sub1.png", 0.3f, true, maxHealth, currentHealth, true, 10);
//    private Enemy enemy4 = new Enemy(3, (int) WORLD_WIDTH + 65, 1, "tanker-atlas.png", 0.5f, false, maxHealth, currentHealth, 10);
//    private Enemy enemy5 = new Enemy(10, (int) WORLD_WIDTH + 65, 1, "tanker2-atlas.png", 1f, false, maxHealth, currentHealth, 10);
//    private Enemy enemy6 = new Enemy(4, (int) WORLD_WIDTH + 64, 1, "destroyer-atlas.png", 1.5f, true, maxHealth, currentHealth, 10);
//    private Enemy enemy7 = new Enemy(10, (int) WORLD_WIDTH + 64, 1, "destroyer-atlas.png", 1.5f, true, maxHealth, currentHealth, 10);
//    private Enemy enemy8 = new Enemy(12, (int) WORLD_WIDTH + 65, 100, 1, "enemy-sub1.png", 0.3f, true, maxHealth, currentHealth, true, 10);
//
//    public Level(EnemyManager enemyManager) {
//        this.enemyManager = enemyManager;
//    }
//
//    public void levelSelector() {
//        totalLevels++;
//        maxHealth += totalLevels;
//        currentHealth = maxHealth;
//        switch ( currentScreen ) {
//            case 1: {
//                level1();
//                break;
//            }
//            case 2: {
//                level2();
//                break;
//            }
//            case 3: {
//                level3();
//                break;
//            }
//            default: {
//                currentScreen = 0;
//            }
//        }
//    }
//
//    private void level1() {
//        enemy1.reset(0,-65,-1,0.5f,maxHealth,currentHealth,10);
//        enemyManager.addEnemy(enemy1);
//    }
//
//    private void level2() {
//        enemy1.reset(0,-65,-1,0.5f,maxHealth,currentHealth,10);
//        enemy2.reset(2,(int) WORLD_WIDTH + 64,1,1.5f,maxHealth,currentHealth,10);
//        enemy3.reset(5,(int) WORLD_WIDTH + 65, 100,1,0.3f,maxHealth,currentHealth,10);
//
//        enemyManager.addEnemy(enemy1);
//        enemyManager.addEnemy(enemy2);
//        enemyManager.addEnemy(enemy3);
//    }
//
//    private void level3() {
//        enemy1.reset(0,-65,-1,0.5f,maxHealth,currentHealth,10);
//        enemy4.reset(3, (int) WORLD_WIDTH + 65, 1, 0.5f, maxHealth, currentHealth, 10);
//        enemy5.reset(10, (int) WORLD_WIDTH + 65, 1, 1f, maxHealth, currentHealth, 10);
//        enemy6.reset(4, (int) WORLD_WIDTH + 64, 1, 1.5f, maxHealth, currentHealth, 10);
//        enemy7.reset(10, (int) WORLD_WIDTH + 64, 1, 1.5f, maxHealth, currentHealth, 10);
//        enemy8.reset(12, (int) WORLD_WIDTH + 65, 100, 1, 0.3f, maxHealth, currentHealth, 10);
//
//        enemyManager.addEnemy(enemy1);
//        enemyManager.addEnemy(enemy4);
//        enemyManager.addEnemy(enemy5);
//        enemyManager.addEnemy(enemy6);
//        enemyManager.addEnemy(enemy7);
//        enemyManager.addEnemy(enemy8);
//    }
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
//    public void setCurrentHealth(float currentHealth) {
//        this.currentHealth = currentHealth;
//    }
//
//    public float getMaxHealth() {
//        return maxHealth;
//    }
//
//    public void setMaxHealth(float maxHealth) {
//        this.maxHealth = maxHealth;
//    }
//}

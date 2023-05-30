package com.danielr.subgame.levels;


import com.danielr.subgame.SubGame;
import entities.Enemy;
import entities.EnemyBuilder;
import entities.EnemyManager;

import static utilz.Constants.Game.*;

public class Level {

    private int currentScreen;

    private int totalLevels = 0;
    private EnemyManager enemyManager;

    private float currentHealth = 100f;

    private float maxHealth = currentHealth;
    private SubGame subGame;

    public Level(EnemyManager enemyManager, SubGame subGame) {
        this.enemyManager = enemyManager;
        this.subGame = subGame;
    }

    public void levelSelector() {
        maxHealth += totalLevels * (1 + totalLevels) / 2f;
        currentHealth = maxHealth;
        System.out.println("Health"  + maxHealth);
        switch ( currentScreen ) {
            case 1: {
                level1();
                break;
            }
            case 2: {
                level2();
                break;
            }
            case 3: {
                level3();
                break;
            }
            default: {
                currentScreen = 0;
            }
        }
    }

    private void level1() {
        Enemy enemy1 = new EnemyBuilder(subGame)
                .withSpawnPosX(-125)
                .withSpriteAtlas("cv6-atlas.png")
                .withSpeed(13f)
                .withAggro(true)
                .withCurrentHealth(maxHealth * 3)
                .withMaxHealth(currentHealth * 3)
                .withEnemyPoints(200)
                .withEnemyWidth(128)
                .build();

        enemyManager.addEnemy(enemy1);
    }

    private void level2() {

        Enemy enemy1 = new EnemyBuilder(subGame)
                .withSpawnPosX(-65)
                .withFlipX(-1)
                .withSpriteAtlas("tanker-atlas.png")
                .withSpeed(10f)
                .withCurrentHealth(maxHealth)
                .withMaxHealth(currentHealth)
                .withEnemyPoints(10)
                .build();

        Enemy enemy2 = new EnemyBuilder(subGame)
                .withDelay(5)
                .withSpawnPosX((int) WORLD_WIDTH + 64)
                .withSpriteAtlas("destroyer-atlas.png")
                .withSpeed(15f)
                .withAggro(true)
                .withCurrentHealth(maxHealth)
                .withMaxHealth(currentHealth)
                .withEnemyPoints(10)
                .build();

        Enemy enemy3 = new EnemyBuilder(subGame)
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

        Enemy enemy4 = new EnemyBuilder(subGame)
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
        Enemy enemy1 = new EnemyBuilder(subGame)
                .withSpawnPosX(-65)
                .withFlipX(-1)
                .withSpriteAtlas("tanker-atlas.png")
                .withSpeed(10f)
                .withCurrentHealth(maxHealth)
                .withMaxHealth(currentHealth)
                .withEnemyPoints(10)
                .build();

        Enemy enemy2 = new EnemyBuilder(subGame)
                .withDelay(3)
                .withSpawnPosX((int) WORLD_WIDTH + 65)
                .withSpriteAtlas("tanker-atlas.png")
                .withSpeed(11f)
                .withCurrentHealth(maxHealth)
                .withMaxHealth(currentHealth)
                .withEnemyPoints(10)
                .build();

        Enemy enemy3 = new EnemyBuilder(subGame)
                .withDelay(10)
                .withSpawnPosX((int) WORLD_WIDTH + 65)
                .withSpawnPosY(100)
                .withSpriteAtlas("tanker2-atlas.png")
                .withSpeed(10f)
                .withMaxHealth(maxHealth)
                .withCurrentHealth(currentHealth)
                .withEnemyPoints(10)
                .build();


        Enemy enemy4 = new EnemyBuilder(subGame)
                .withDelay(4)
                .withSpawnPosX((int) WORLD_WIDTH + 64)
                .withSpriteAtlas("destroyer-atlas.png")
                .withSpeed(15f)
                .withAggro(true)
                .withMaxHealth(maxHealth)
                .withCurrentHealth(currentHealth)
                .withEnemyPoints(10)
                .build();

        Enemy enemy5 = new EnemyBuilder(subGame)
                .withDelay(10)
                .withSpawnPosX((int) WORLD_WIDTH + 64)
                .withSpriteAtlas("destroyer-atlas.png")
                .withSpeed(20f)
                .withAggro(true)
                .withMaxHealth(maxHealth)
                .withCurrentHealth(currentHealth)
                .withEnemyPoints(10)
                .build();

        Enemy enemy6 = new EnemyBuilder(subGame)
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
}
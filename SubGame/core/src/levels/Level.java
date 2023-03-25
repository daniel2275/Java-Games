package levels;


import entities.Enemy;
import entities.EnemyManager;

import static utilz.Constants.Game.WORLD_WIDTH;

public class Level {

    private int currentScreen;

    private int totalLevels  = 0 ;
    private EnemyManager enemyManager;

    public Level(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public void levelSelector() {
        totalLevels ++;
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
        Enemy enemy1 = new Enemy(0, -65, -1, "tanker-atlas.png", 0.5f, false, 100f, 10);

        enemyManager.addEnemy(enemy1);
    }


    private void level2() {
        Enemy enemy1 = new Enemy(0, -65, -1, "tanker-atlas.png", 0.5f, false, 100f, 10);
        Enemy enemy2 = new Enemy(2, (int) WORLD_WIDTH + Enemy.ENEMY_WIDTH, 1, "destroyer-atlas.png", 1.5f, true, 100f, 10);
        Enemy enemy3 = new Enemy(5, (int) WORLD_WIDTH + 65, 100, 1, "enemy-sub1.png", 0.3f, true, 100f, true, 10);

        enemyManager.addEnemy(enemy1);
        enemyManager.addEnemy(enemy2);
        enemyManager.addEnemy(enemy3);
    }

    private void level3() {
        Enemy enemy1 = new Enemy(0, -65, -1, "tanker-atlas.png", 0.5f, false, 100f, 10);
        Enemy enemy2 = new Enemy(3, (int) WORLD_WIDTH + 65, 1, "tanker-atlas.png", 0.5f,false, 100f, 10);
        Enemy enemy3 = new Enemy(10, (int) WORLD_WIDTH + 65, 1, "tanker2-atlas.png", 1f,false, 100f, 10);
        Enemy enemy4 = new Enemy(4, (int) WORLD_WIDTH + Enemy.ENEMY_WIDTH, 1, "destroyer-atlas.png", 1.5f,true, 100f, 10);
        Enemy enemy5 = new Enemy(10, (int) WORLD_WIDTH + Enemy.ENEMY_WIDTH, 1, "destroyer-atlas.png", 1.5f,true, 100f, 10);
        Enemy enemy6 = new Enemy(12, (int) WORLD_WIDTH + 65, 100, 1, "enemy-sub1.png", 0.3f,true, 100f, true, 10);

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

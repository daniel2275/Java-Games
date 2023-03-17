package entities;

import gamestates.Playing;
import objects.ObjectManager;

import java.util.ArrayList;
import java.util.Iterator;

import static com.danielr.subgame.SubGame.pause;
import static utilz.Constants.Game.WORLD_WIDTH;

public class EnemyManager {

    private ArrayList<Enemy> listOfEnemies = new ArrayList<>();

    public EnemyManager(Playing playing) {
    }

    public void create() {
//        Enemy enemy1 = new Enemy(2, -65, -1, "tanker-atlas.png", 0.5f, false);
//        Enemy enemy2 = new Enemy(20, (int) WORLD_WIDTH + 65, 1, "tanker-atlas.png", 0.5f,false);
//        Enemy enemy3 = new Enemy(10, (int) WORLD_WIDTH + 65, 1, "tanker2-atlas.png", 1f,false);
        Enemy enemy4 = new Enemy(4, (int) WORLD_WIDTH + 65, 1, "destroyer-atlas.png", 1.5f,true);
        Enemy enemy5 = new Enemy(1, (int) WORLD_WIDTH + 65, 100, 1, "enemy-sub1.png", 0.3f,true, true);
//        Enemy enemy6 = new Enemy(8, -65, 400, -1, "enemy-sub1.png", 0.3f,true, true);
//        Enemy enemy7 = new Enemy(25, (int) WORLD_WIDTH + 65, 200, -1, "enemy-sub1.png", 0.3f,true, true);


//        listOfEnemies.add(enemy1);
//        listOfEnemies.add(enemy2);
//        listOfEnemies.add(enemy3);
        listOfEnemies.add(enemy4);
        listOfEnemies.add(enemy5);
//        listOfEnemies.add(enemy6);
//        listOfEnemies.add(enemy7);
    }

    public void update(Player player, ObjectManager objectManager) {
        // update enemies
        Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
        // iterateEnemies(enemyIterator);
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.isSunk()) {
//                System.out.println(enemy.isSunk());
                enemyIterator.remove();
            } else {
                enemy.update(player);
                avoidEnemies();
                objectManager.dropCharge(enemy);
            }
        }

//        System.out.println(listOfEnemies.size());
    }

// avoid overlapping of submarine enemies
    public void avoidEnemies() {
        if (!pause) {
            Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy nextEnemy = enemyIterator.next();
                if (enemyIterator.hasNext()) {
                    Enemy enemy = enemyIterator.next();
                    if (!nextEnemy.isDying()) {
                        if (nextEnemy.getHitbox().overlaps(enemy.getHitbox())) {
                            enemy.setFlipY(-1);
                        } else {
                            enemy.setFlipY(1);
                        }
                    }
                }
            }
        }
    }

    public ArrayList<Enemy> getListOfEnemies() {
        return listOfEnemies;
    }


}

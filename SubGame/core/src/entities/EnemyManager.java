package entities;

import gamestates.Playing;
import objects.ObjectManager;


import com.badlogic.gdx.math.Rectangle;
import java.util.ArrayList;
import java.util.Iterator;

import static com.danielr.subgame.SubGame.pause;

public class EnemyManager {

    private ArrayList<Enemy> listOfEnemies = new ArrayList<>();

    public EnemyManager(Playing playing) {
    }

    public void create() {
        Enemy enemy1 = new Enemy(2, -65, -1, "tanker-atlas.png", 0.5f, false);
        Enemy enemy2 = new Enemy(20, 865, 1, "tanker-atlas.png", 0.5f,false);
        Enemy enemy3 = new Enemy(10, 865, 1, "tanker2-atlas.png", 1f,false);
        Enemy enemy4 = new Enemy(1, 865, 1, "destroyer-atlas.png", 1.5f,true);
        Enemy enemy5 = new Enemy(1, 865, 100, 1, "enemy-sub1.png", 0.3f,true, true);
        Enemy enemy6 = new Enemy(1, -65, 400, 1, "enemy-sub1.png", 0.3f,true, true);



        listOfEnemies.add(enemy1);
        listOfEnemies.add(enemy2);
        listOfEnemies.add(enemy3);
        listOfEnemies.add(enemy4);
        listOfEnemies.add(enemy5);
        listOfEnemies.add(enemy6);
    }

    public void update(Player player, ObjectManager objectManager) {
            // update enemies
        Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
//        iterateEnemies(enemyIterator);
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.isSunk()) {
                enemyIterator.remove();
            } else {
                enemy.update(player);
                avoidEnemies();
                objectManager.dropCharge(enemy);
            }
        }
    }


    public void avoidEnemies() {
        if (!pause) {
            Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
            while (enemyIterator.hasNext()) {
                Enemy nextEnemy = enemyIterator.next();
                if (enemyIterator.hasNext()) {
                    Enemy enemy = enemyIterator.next();
                    if (nextEnemy.getHitbox().overlaps(enemy.getHitbox())) {
                        enemy.setFlipY(-1);
                    } else {
                        enemy.setFlipY(1);
                    }
                }
            }
        }
    }

    public ArrayList<Enemy> getListOfEnemies() {
        return listOfEnemies;
    }

    public void removeEnemy(Enemy enemy) {
        listOfEnemies.remove(enemy);
    }
}

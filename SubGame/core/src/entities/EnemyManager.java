package entities;

import gamestates.Playing;

import java.util.ArrayList;
import java.util.Iterator;

public class EnemyManager {

    private ArrayList<Enemy> listOfEnemies = new ArrayList<>();

    public EnemyManager(Playing playing) {
    }

    public void create() {
        Enemy enemy1 = new Enemy(2, -65, -1, "tanker-atlas.png", 0.5f);
        Enemy enemy2 = new Enemy(20, 865, 1, "tanker-atlas.png", 0.5f);
        Enemy enemy3 = new Enemy(10, 865, 1, "tanker2-atlas.png", 1f);

        listOfEnemies.add(enemy1);
        listOfEnemies.add(enemy2);
        listOfEnemies.add(enemy3);
    }

    public void update() {
        // update enemies
        Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
//        iterateEnemies(enemyIterator);
        while (enemyIterator.hasNext()) {
            Enemy enemy =  enemyIterator.next();
            if (enemy.isSunk()) {
                System.out.println("remove?");
                enemyIterator.remove();
            } else {
                enemy.update();
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

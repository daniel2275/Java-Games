package entities;

import gamestates.Playing;
import objects.ObjectManager;

import java.util.ArrayList;
import java.util.Iterator;

import static com.danielr.subgame.SubGame.pause;

public class EnemyManager {

    private ArrayList<Enemy> listOfEnemies = new ArrayList<>();



    public EnemyManager(Playing playing) {
    }
    public void reset() {
        // clear the list of enemies
        Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            enemy.setQuit(true);
        }
    }

    public void update(Player player, ObjectManager objectManager) {
        // update enemies
        Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();
            if (enemy.isSunk()) {
                player.setPlayerScore(player.getPlayerScore() + enemy.getEnemyPoints());
                enemyIterator.remove();
            } else if (enemy.isQuit()) {
                enemy.setQuit(false);
                enemyIterator.remove();
            }
                enemy.update(player);
                avoidEnemies();
                objectManager.dropCharge(enemy);
            }
        }

// avoid overlapping of submarine enemies
    public void avoidEnemies() {
        if (pause) {
            return;
        }

        listOfEnemies.forEach(enemyA -> {
            if (enemyA.isDying()) {
                return;
            }

            listOfEnemies.forEach(enemyB -> {
                if (enemyA == enemyB || enemyB.isDying()) {
                    return;
                }

                enemyB.setFlipY(enemyA.getHitbox().overlaps(enemyB.getHitbox()) ? -1 : 1);
            });
        });
    }



    public ArrayList<Enemy> getListOfEnemies() {
        return listOfEnemies;
    }

    public void setListOfEnemies(ArrayList<Enemy> listOfEnemies) {
        this.listOfEnemies = listOfEnemies;
    }

    public void addEnemy(Enemy enemy) {
        listOfEnemies.add(enemy);
    }

    public void removeEnemy(Enemy enemy) {
        listOfEnemies.remove(enemy);
    }

    public void pause() {
        Enemy.pause();
//            listOfEnemies.forEach(Enemy::pause);
    }

    public void resume() {
        Enemy.resume();
//        listOfEnemies.forEach(Enemy::resume);
    }

    public void exit() {
        listOfEnemies.forEach(enemyA -> {
            enemyA.exit();
        });
    }

}

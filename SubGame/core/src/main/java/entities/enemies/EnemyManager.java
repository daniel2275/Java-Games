package entities.enemies;

import UI.game.GameScreen;
import entities.player.Player;
import objects.ObjectManager;

import java.util.ArrayList;
import java.util.Iterator;

import static io.github.daniel2275.subgame.SubGame.pause;


public class EnemyManager {

    private ArrayList<Enemy> listOfEnemies = new ArrayList<>();
    private GameScreen gameScreen;


    public EnemyManager(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
    }

    public void reset() {
        // clear the list of enemies
        dispose();
        listOfEnemies.clear();
    }

    public void update(Player player, ObjectManager objectManager) {
        // update enemies
        Iterator<Enemy> enemyIterator = listOfEnemies.iterator();
        while (enemyIterator.hasNext()) {
            Enemy enemy = enemyIterator.next();

            if (enemy.getEnemyActor().getParent() == null) {
                player.setPlayerScore(player.getPlayerScore() + enemy.getEnemyPoints());
                enemyIterator.remove();
            } else if (enemy.isQuit()) {
                enemy.setQuit(false);
                //enemy.getEnemyActor().dispose();
                enemyIterator.remove();
            } else {
                enemy.update(player);
                avoidEnemies();
                objectManager.dropCharge(enemy);
            }
        }

    }

    // avoid overlapping of submarine enemies
    public void avoidEnemies() {
        // Check if the game is paused
        if (pause) {
            return;
        }

        for (Enemy enemyA : listOfEnemies) {
            if (enemyA.isDying() || enemyA.getEnemyActor() == null) {
                continue;
            }

            for (Enemy enemyB : listOfEnemies) {
                if (enemyA == enemyB || enemyB.isDying() || enemyB.getEnemyActor() == null) {
                    continue;
                }

                // Check for overlapping bounding rectangles and flip Y axis if necessary
                enemyB.setFlipY(enemyA.getEnemyActor().getBounding().overlaps(enemyB.getEnemyActor().getBounding()) ? -1 : 1);
            }
        }
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

//    public void exit() {
//        listOfEnemies.forEach(enemyA -> {
//            enemyA.exit();
//        });
//    }

    public void dispose(){
        for (Enemy enemy : listOfEnemies) {
            enemy.getEnemyActor().remove();
        }
    }


    public void toFront() {
        listOfEnemies.forEach(enemyA -> {
            enemyA.getEnemyActor().toFront();
        });

    }

}
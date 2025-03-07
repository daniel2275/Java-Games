package entities.enemies;

import Components.BaseActor;
import UI.game.GameScreen;
import entities.player.Player;
import objects.ObjectManager;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

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
                enemyIterator.remove();
            } else {
                enemy.update(player);
                //avoidEnemies();
                objectManager.enemyAttack(enemy);
            }
        }
    }

    public boolean checkCollision(BaseActor actor, float damage) {
        Optional<Enemy> deadEnemy = getListOfEnemies().stream()
            .filter(enemy -> {
                boolean isHit = enemy.checkHit(actor, damage);
                if (isHit && enemy.getEnemyActor().getCurrentHealth() <= 0) {
                    enemy.setDying(true);
                }
                return isHit;
            })
            .findFirst();

        return deadEnemy.isPresent();
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
                if (enemyA.getEnemyActor().getBounding().overlaps(enemyB.getEnemyActor().getBounding())) {
                    int setFlipY = -1;  // Default to -1 when overlapping

                    // Decide movement based on the overlap result
                    if (setFlipY == 1) {
                        enemyB.getEnemyActor().moveUp(enemyB.getEnemyActor().getMoveSpeed());
                    } else {
                        enemyB.getEnemyActor().moveDown(enemyB.getEnemyActor().getMoveSpeed());
                    }
                }
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






}

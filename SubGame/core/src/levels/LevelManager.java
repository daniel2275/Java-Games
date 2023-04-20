package levels;

import entities.Enemy;
import gamestates.Playing;

import java.util.List;

public class LevelManager {

    private Playing playing;

    private Level level;

    public LevelManager(Playing playing) {
        level = new Level(playing.getEnemyManager());
        this.playing = playing;
    }

    public void update() {
        if (playing.getEnemyManager().getListOfEnemies().size() == 0) {
            level.setCurrentScreen(level.getCurrentScreen() + 1);
            level.levelSelector();
        }
    }

    public Level getLevel() {
        return level;
    }

    public void reset() {
        // reset the current level to the first level
        level.setCurrentScreen(0);
        level.setTotalLevels(0);
        level.setCurrentHealth(100);
        level.setMaxHealth(100);
        // remove all enemies from the enemy manager
        List<Enemy> enemies = playing.getEnemyManager().getListOfEnemies();
        for (Enemy enemy : enemies) {
            playing.getEnemyManager().reset();
        }
    }
}

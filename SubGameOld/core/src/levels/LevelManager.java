package levels;

import entities.Enemy;
import gamestates.Playing;

import java.util.List;

public class LevelManager {
    private Playing playing;
    private Level level;
    private boolean currentRun;

    public LevelManager(Playing playing) {
        level = new Level(playing.getEnemyManager(), playing.getSubGame());
        this.playing = playing;
    }

    public void update() {
        if (!currentRun) {
            level.setCurrentScreen(level.getCurrentScreen() + 1);
            level.levelSelector();
            currentRun = true;
        } else if (playing.getEnemyManager().getListOfEnemies().isEmpty()) {
            level.setCurrentScreen(level.getCurrentScreen() + 1);
            level.setTotalLevels(level.getTotalLevels() + 1);
            level.levelSelector();
        }
    }

    public Level getLevel() {
        return level;
    }

    public void reset() {
        currentRun = false;
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

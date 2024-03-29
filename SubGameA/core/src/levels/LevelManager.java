package levels;

import entities.enemies.Enemy;
import gamestates.GamePlayScreen;

import java.util.List;

public class LevelManager {
    private GamePlayScreen gamePlayScreen;
    private Level level;
    private boolean currentRun;

    public LevelManager(GamePlayScreen gamePlayScreen) {
        level = new Level(gamePlayScreen.getEnemyManager(), gamePlayScreen);
        this.gamePlayScreen = gamePlayScreen;
    }

    public void update() {
        if (!currentRun) {
            level.setCurrentScreen(level.getCurrentScreen() + 1);
            level.levelSelector();
            currentRun = true;
        } else if (gamePlayScreen.getEnemyManager().getListOfEnemies().isEmpty()) {
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
        List<Enemy> enemies = gamePlayScreen.getEnemyManager().getListOfEnemies();
        for (Enemy enemy : enemies) {
            gamePlayScreen.getEnemyManager().reset();
        }
    }
}

package levels;

import UI.game.GameScreen;

public class LevelManager {
    private GameScreen gameScreen;
    private Level level;
    private boolean currentRun;

    public LevelManager(GameScreen gameScreen) {
        level = new Level(gameScreen.getEnemyManager(), gameScreen);
        this.gameScreen = gameScreen;
    }

    public void update() {
        if (!currentRun) {
            level.setCurrentScreen(level.getCurrentScreen() + 1);
            level.levelSelector();
            currentRun = true;
        } else if (gameScreen.getEnemyManager().getListOfEnemies().isEmpty()) {
            level.setCurrentScreen(level.getCurrentScreen() + 1);
            level.setTotalLevels(level.getTotalLevels() + 1);
            gameScreen.getUpgradeStore().saveGame();
            level.levelSelector();
            gameScreen.getObjectManager().reset(); // reset Objects, mines torpedoes, etc
        }
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public void reset() {
        currentRun = false;
        // reset the current level to the first level
        level.setCurrentScreen(0);
        level.setTotalLevels(0);
        level.setCurrentHealth(100);
        level.setMaxHealth(100);
    }
}

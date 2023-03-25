package levels;

import gamestates.Playing;

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
}

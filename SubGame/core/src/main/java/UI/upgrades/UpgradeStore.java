package UI.upgrades;

import UI.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class UpgradeStore implements Screen {
    private GameScreen gameScreen;
    private UpgradeStageManager upgradeStageManager;
    private UpgradeManager upgradeManager ;
    private Stage upStage;
    private float playerScore;
    private String saveName = "SaveGame";

    public UpgradeStore(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.playerScore = gameScreen.getPlayer().getPlayerScore();
        upgradeManager = new UpgradeManager();
        loadInit();
        this.upgradeStageManager = new UpgradeStageManager(gameScreen, this);
        upStage = upgradeStageManager.build();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(upStage);
        upStage.act();
        getUpgradeStageManager().animateButtonsOnShow();
    }

    @Override
    public void render(float delta) {
        upgradeStageManager.getScoreLbl().setText("Score: " + gameScreen.getPlayer().getPlayerScore());
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        upStage.act(delta);
        upStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        updateViewport(width, height);
    }

    public void updateViewport(int width, int height) {
        upStage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        upgradeStageManager.hideEverything();
        upgradeStageManager.hide();
    }

    @Override
    public void dispose() {
    }

    public void setPlayerScore(float playerScore) {
        this.playerScore = playerScore;
    }

    public float getPlayerScore() {
        return playerScore;
    }

    public void loadInit() {
        boolean loadSuccessful = upgradeManager.loadFromPrefs();

        if (!loadSuccessful) {
            setDefaults();
        }
            // Get SaveGame values
            int playerScoreInit = upgradeManager.getSaveGame("SaveGame").getPlayerScore();
            gameScreen.getPlayer().setPlayerScore(playerScoreInit);

            int playerHealthInit = upgradeManager.getSaveGame("SaveGame").getPlayerHealth();
            gameScreen.getPlayer().setPlayerHealth(playerHealthInit);

//            int levelInit = upgradeManager.getSaveGame("SaveGame").getLevel();
//            gameScreen.getLevelManager().getLevel().setTotalLevels(levelInit);
//            int levelInit = upgradeManager.getStageLevel("SaveGame", 1);
//           gameScreen.getLevelManager().getLevel().setTotalLevels(levelInit);

            float volume = upgradeManager.getSaveGame("SaveGame").getVolume();
            gameScreen.getSubGame().getOptions().setVolume(volume);

            // Get Upgraded values
            float reloadSpeed = upgradeManager.getUpgrade("FireRate").getActualValue();
            gameScreen.getPlayer().getPlayerActor().setReloadSpeed(reloadSpeed);

            float playerSpeed = upgradeManager.getUpgrade("Speed").getActualValue();
            gameScreen.getPlayer().setPlayerSpeed(playerSpeed);

            float playerDamage = upgradeManager.getUpgrade("Damage").getActualValue();
            gameScreen.getPlayer().getPlayerActor().setDamage(playerDamage);
            gameScreen.getPlayer().setPlayerDamage(playerDamage);

    }


    public void gameOver() {
        // Load specific game settings
        int playerScoreInit = upgradeManager.getSaveGame("SaveGame").getPlayerScore();
        gameScreen.getPlayer().setPlayerScore(playerScoreInit);

        int playerHealthInit = upgradeManager.getSaveGame("SaveGame").getPlayerHealth();
        gameScreen.getPlayer().setPlayerHealth(playerHealthInit);


        int stageId = gameScreen.getLevelManager().getLevel().getStage();
        //int level = upgradeManager.getStageLevel("SaveGame", stageId);
        int currentLevel =  gameScreen.getLevelManager().getLevel().getTotalLevels();
        upgradeManager.getSaveGame("SaveGame").setStageLevel( stageId, currentLevel);
        //gameScreen.getLevelManager().getLevel().setTotalLevels(levelInit);

        float volume = upgradeManager.getSaveGame("SaveGame").getVolume();
        gameScreen.getSubGame().getOptions().setVolume(volume);

        // Get Upgraded values
        float reloadSpeed = upgradeManager.getUpgrade("FireRate").getActualValue();
        gameScreen.getPlayer().getPlayerActor().setReloadSpeed(reloadSpeed);

        float playerSpeed = upgradeManager.getUpgrade("Speed").getActualValue();
        gameScreen.getPlayer().setPlayerSpeed(playerSpeed);

        float playerDamage = upgradeManager.getUpgrade("Damage").getActualValue();
        gameScreen.getPlayer().getPlayerActor().setDamage(playerDamage);
        gameScreen.getPlayer().setPlayerDamage(playerDamage);
    }


    public void saveGame() {
        // Update player score, health, level, reload speed, player speed, and volume
        int playerScore = gameScreen.getPlayer().getPlayerScore();
        int playerHealth = gameScreen.getPlayer().getPlayerHealth();

        int stageId = gameScreen.getLevelManager().getLevel().getStage();
        int currentLevel =  gameScreen.getLevelManager().getLevel().getTotalLevels();
        //int maxLevel = upgradeManager.getStageLevel("SaveGame", stageId);
        upgradeManager.getSaveGame("SaveGame").setStageLevel( stageId, currentLevel);


        float volume = gameScreen.getSubGame().getOptions().getVolume();
        upgradeManager.addOrUpdateSaveGame("SaveGame", playerScore, playerHealth, volume);

        upgradeManager.saveToPrefs();
   }


    public void setDefaults() {
        // Set default values for upgrades
        upgradeManager.addOrUpdateUpgrade("Speed", 10, 10f, 60f, 10f, 15, 0);
        upgradeManager.addOrUpdateUpgrade("FireRate", 10, 3f, 1f, 3f, 15, 0);
        upgradeManager.addOrUpdateUpgrade("Damage", 10, 25f, 150f, 25f, 15, 0);


        // Set default values for player score, health, total levels, reload speed, player speed
        upgradeManager.addOrUpdateSaveGame("SaveGame", 0, 100,0.5f);
        upgradeManager.saveToPrefs();

        int playerScoreInit = upgradeManager.getSaveGame("SaveGame").getPlayerScore();
        gameScreen.getPlayer().setPlayerScore(playerScoreInit);


        int playerHealthInit = upgradeManager.getSaveGame("SaveGame").getPlayerHealth();
        gameScreen.getPlayer().setPlayerHealth(playerHealthInit);

//        int levelInit = upgradeManager.getSaveGame("SaveGame").getLevel();
//        gameScreen.getLevelManager().getLevel().setTotalLevels(levelInit);
//        int levelInit = upgradeManager.getStageLevel ("SaveGame",1);
//        gameScreen.getLevelManager().getLevel().setTotalLevels(levelInit);
        upgradeManager.resetAllStages("SaveGame");

        float volume = upgradeManager.getSaveGame("SaveGame").getVolume();
        gameScreen.getSubGame().getOptions().setVolume(volume);

        // upgrades
        float reloadSpeed = upgradeManager.getUpgrade("FireRate").getActualValue();
        gameScreen.getPlayer().getPlayerActor().setReloadSpeed(reloadSpeed);

        float playerSpeed = upgradeManager.getUpgrade("Speed").getActualValue();
        gameScreen.getPlayer().setPlayerSpeed(playerSpeed);

        float PlayerDamage = upgradeManager.getUpgrade("Damage").getActualValue();
        gameScreen.getPlayer().setPlayerDamage(PlayerDamage);
        gameScreen.getPlayer().getPlayerActor().setDamage(PlayerDamage);

     }

    public UpgradeManager getUpgradeManager() {
        return upgradeManager;
    }

    public UpgradeStageManager getUpgradeStageManager() {
        return upgradeStageManager;
    }

    public String getSaveName() {
        return saveName;
    }

    //    public void advanceLevel(){
//        upgradeManager.advanceLevel("SaveGame", 1);
//    }
}

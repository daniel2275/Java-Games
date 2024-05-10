package UI.upgrades;

import UI.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gamestates.Gamestate;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class UpgradeStore implements Screen {
    private Map<String, Upgrade> upgrades;

    private GameScreen gameScreen;
    private UpgradeStageManager upgradeStageManager;

    private Upgrade upgradeSpeed;
    private Upgrade upgradeFireRate;

    private Stage upStage;

    private float playerScore;

    public UpgradeStore(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        this.playerScore = gameScreen.getPlayer().getPlayerScore();
        loadDefaults();
        loadInit();
        this.upgradeStageManager = new UpgradeStageManager(gameScreen, this);
        upStage =  upgradeStageManager.build();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(upStage);
    }

    @Override
    public void render(float delta) {
        upgradeStageManager.getScoreLbl().setText("Score: " + gameScreen.getPlayer().getPlayerScore());
        //Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
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
        Preferences prefs = Gdx.app.getPreferences("my_prefs");

        String jsonStringDefault = prefs.getString("upgrades", null);
        if (jsonStringDefault != null) {
            Map<String, Upgrade> upgrades = new Gson().fromJson(jsonStringDefault, new TypeToken<Map<String, Upgrade>>(){}.getType());
        } else {
            // Handle the case where upgrades are not set yet
            upgrades = new HashMap<>();
        }

        float playerScoreInit = prefs.getFloat("playerScore", 0);
        gameScreen.getPlayer().setPlayerScore(playerScoreInit);

        float playerHealthInit = prefs.getFloat("playerHealth", 100); // Default health if not set
        gameScreen.getPlayer().setPlayerHealth(playerHealthInit);

        int levelInit = prefs.getInteger("level", 1); // Default level if not set
        gameScreen.getLevelManager().getLevel().setTotalLevels(levelInit);

        float reloadSpeed = prefs.getFloat("reloadSpeed", 1.0f); // Default reload speed if not set
        gameScreen.getPlayer().getPlayerActor().setReloadSpeed(reloadSpeed);

        float playerSpeed = prefs.getFloat("playerSpeed", 1.0f); // Default player speed if not set
        gameScreen.getPlayer().setPlayerSpeed(playerSpeed);

        float volume = prefs.getFloat("volume", 0.5f); // Default volume if not set
        gameScreen.getSubGame().getOptions().setVolume(volume);
    }



    public void load() {
        Preferences prefs = Gdx.app.getPreferences("my_prefs");

        String jsonStringDefault = prefs.getString("upgrades", null);
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Upgrade>>(){}.getType();
        HashMap<String, Upgrade> upgrades = gson.fromJson(jsonStringDefault, type);


        float reloadSpeed = prefs.getFloat("reloadSpeed");
        gameScreen.getPlayer().getPlayerActor().setReloadSpeed(reloadSpeed);

        float playerSpeed = prefs.getFloat("playerSpeed");
        gameScreen.getPlayer().setPlayerSpeed(playerSpeed);

        float volume = prefs.getFloat("volume");
        gameScreen.getSubGame().getOptions().setVolume(volume);
    }


    public void saveGame() {
        Preferences prefs = Gdx.app.getPreferences("my_prefs");
        String jsonString = new Json().toJson(upgrades);
        prefs.putString("upgrades", jsonString);

        prefs.putFloat("playerScore", gameScreen.getPlayer().getPlayerScore());
        prefs.putFloat("playerHealth", gameScreen.getPlayer().getPlayerHealth());

        int level = gameScreen.getLevelManager().getLevel().getTotalLevels();
        System.out.println(Gamestate.state + " " + level);
        prefs.putInteger("level", level);

        prefs.putFloat("reloadSpeed", gameScreen.getPlayer().getPlayerActor().getReloadSpeed());
        prefs.putFloat("playerSpeed", gameScreen.getPlayer().getPlayerSpeed());

        prefs.putFloat("volume", gameScreen.getSubGame().getOptions().getVolume());

        prefs.flush();
    }

    public void loadDefaults() {
        upgradeSpeed = new Upgrade("Speed", 10f, 2f, 0f, 60f, 40, 0);
        upgradeFireRate = new Upgrade("FireRate", 10f, 2f, 3f, 0f, 40, 0);
        int playerScore = 1000;
        float playerHealth = 100f;

        int totalLevels = 0;

        float reloadSpeed = 3f;
        float playerSpeed = 10f;

        upgrades = new HashMap<>();
        upgrades.put("Speed",upgradeSpeed);
        upgrades.put("FireRate",upgradeFireRate);

        Preferences prefs = Gdx.app.getPreferences("my_prefs");
        String jsonString = new Json().toJson(upgrades);
        prefs.putString("default_upgrades", jsonString);
        prefs.putInteger("default_playerScore", playerScore);
        prefs.putFloat("default_playerHealth", playerHealth);
        prefs.putInteger("default_level", totalLevels);

        prefs.putFloat("default_reloadSpeed", reloadSpeed);
        prefs.putFloat("default_playerSpeed", playerSpeed);

//        prefs.putFloat("volume", 1.0f);

        prefs.flush();
    }

    public void resetUpgrades() {
        Preferences prefs = Gdx.app.getPreferences("my_prefs");

        String jsonStringDefault = prefs.getString("default_upgrades", null);

        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Upgrade>>(){}.getType();
        HashMap<String, Upgrade> upgrades = gson.fromJson(jsonStringDefault, type);

        int playerScoreInit = prefs.getInteger("default_playerScore");
        gameScreen.getPlayer().setPlayerScore(playerScoreInit);

        float playerHealthInit = prefs.getFloat("default_playerHealth");
        gameScreen.getPlayer().setPlayerHealth(playerHealthInit);

        int levelInit = prefs.getInteger("default_level");
        gameScreen.getLevelManager().getLevel().setTotalLevels(levelInit);

        float reloadSpeed = prefs.getFloat("default_reloadSpeed");
        gameScreen.getPlayer().getPlayerActor().setReloadSpeed(reloadSpeed);

        float playerSpeed = prefs.getFloat("default_playerSpeed");
        gameScreen.getPlayer().setPlayerSpeed(playerSpeed);

        float volume = prefs.getFloat("volume");
        gameScreen.getSubGame().getOptions().setVolume(volume);

        saveGame();

        upgradeStageManager.resetUpgrades();
    }

    public Map<String, Upgrade> getUpgrades() {
        return upgrades;
    }


}

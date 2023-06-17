package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Json;
import entities.Enemy;
import utilz.Constants;

import java.util.HashMap;
import java.util.Map;

import static com.danielr.subgame.SubGame.pause;

public class UpgradeStore implements Screen {
    private Stage stage;
    private Map<String, Upgrade> upgrades;

    private Playing playing;
    private Label scoreLbl;

    private Upgrade upgradeSpeed;
    private Upgrade upgradeFireRate;

    private Label speedCost;
    private Label fireRateCost;

    private ProgressBar playerSpeedDisplay;
    private ProgressBar playerFireRateDisplay;

    private int playerScore;

    public UpgradeStore(Playing playing) {
        this.playerScore = playing.getPlayer().getPlayerScore();
        this.playing = playing;
        loadDefaults();
        loadInit();
        show();
    }

    public void loadInit() {
        Preferences prefs = Gdx.app.getPreferences("my_prefs");

        String jsonStringDefault = prefs.getString("upgrades", null);
        upgrades = new Json().fromJson(HashMap.class, jsonStringDefault);

        int playerScoreInit = prefs.getInteger("playerScore");
        playing.getPlayer().setPlayerScore(playerScoreInit);

        float playerHealthInit = prefs.getFloat("playerHealth");
        playing.getPlayer().setPlayerHealth(playerHealthInit);

        int levelInit = prefs.getInteger("level");
        playing.getLevelManager().getLevel().setTotalLevels(levelInit);

        float reloadSpeed = prefs.getFloat("reloadSpeed");
        playing.getPlayer().setReloadSpeed(reloadSpeed);

        float playerSpeed = prefs.getFloat("playerSpeed");
        playing.getPlayer().setPlayerSpeed(playerSpeed);

        float volume = prefs.getFloat("volume");
        playing.getSubGame().getOptions().setVolume(volume);
    }


    public void load() {
        Preferences prefs = Gdx.app.getPreferences("my_prefs");

        String jsonStringDefault = prefs.getString("upgrades", null);
        upgrades = new Json().fromJson(HashMap.class, jsonStringDefault);

        float reloadSpeed = prefs.getFloat("reloadSpeed");
        playing.getPlayer().setReloadSpeed(reloadSpeed);

        float playerSpeed = prefs.getFloat("playerSpeed");
        playing.getPlayer().setPlayerSpeed(playerSpeed);

        float volume = prefs.getFloat("volume");
        playing.getSubGame().getOptions().setVolume(volume);
    }


    public void saveGame() {
        Preferences prefs = Gdx.app.getPreferences("my_prefs");
        String jsonString = new Json().toJson(upgrades);
        prefs.putString("upgrades", jsonString);

        prefs.putInteger("playerScore", playing.getPlayer().getPlayerScore());
        prefs.putFloat("playerHealth", playing.getPlayer().getPlayerHealth());

        int level = playing.getLevelManager().getLevel().getTotalLevels();
        System.out.println(Gamestate.state + " " + level);
//        prefs.putInteger("level", ((Gamestate.state == Gamestate.MENU) ? level - 1 : level ) );
        prefs.putInteger("level", level);

        prefs.putFloat("reloadSpeed", playing.getPlayer().getReloadSpeed());
        prefs.putFloat("playerSpeed", playing.getPlayer().getPlayerSpeed());

        prefs.putFloat("volume", playing.getSubGame().getOptions().getVolume());

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

        upgrades = new Json().fromJson(HashMap.class, jsonStringDefault);

        int playerScoreInit = prefs.getInteger("default_playerScore");
        playing.getPlayer().setPlayerScore(playerScoreInit);

        float playerHealthInit = prefs.getFloat("default_playerHealth");
        playing.getPlayer().setPlayerHealth(playerHealthInit);

        int levelInit = prefs.getInteger("default_level");
        playing.getLevelManager().getLevel().setTotalLevels(levelInit);

        float reloadSpeed = prefs.getFloat("default_reloadSpeed");
        playing.getPlayer().setReloadSpeed(reloadSpeed);

        float playerSpeed = prefs.getFloat("default_playerSpeed");
        playing.getPlayer().setPlayerSpeed(playerSpeed);

        float volume = prefs.getFloat("volume");
        playing.getSubGame().getOptions().setVolume(volume);

        saveGame();

        show();

        int percent;
        percent = (upgrades.get("Speed").getLevel() * 100) / upgrades.get("Speed").getUpgTicks();
        playerSpeedDisplay.setValue(percent);

        percent = (upgrades.get("FireRate").getLevel() * 100) / upgrades.get("FireRate").getUpgTicks();
        playerFireRateDisplay.setValue(percent);

        speedCost.setText(" " + upgrades.get("Speed").getCost());
        fireRateCost.setText(" " + upgrades.get("FireRate").getCost());

    }

    @Override
    public void show() {

        // create a skin object
        Skin skin = new Skin(Gdx.files.internal("glassyui/glassy-ui.json"));
        stage = new Stage();

        speedCost = new Label("00", skin);
        fireRateCost = new Label("00", skin);

        // Score label creation
        scoreLbl = new Label("(Score:)" + playing.getPlayer().getPlayerScore(), skin);
        scoreLbl.setPosition(5, Constants.Game.WORLD_HEIGHT - 25);
        stage.addActor(scoreLbl);

        TextButton playerSpeedUpBtn      = new TextButton(" Sub Speed + ", skin, "small");
        TextButton playerSpeedDownBtn    = new TextButton(" Sub Speed - ", skin);
        playerSpeedUpBtn.setPosition(10,10);

        TextButton playerFireRateUpBtn   = new TextButton("Sub FireRate +", skin, "small");
        TextButton playerFireRateDownBtn = new TextButton("Sub FireRate -", skin);

        playerSpeedDisplay = new ProgressBar(2, 100, 1, false, skin);
        int percent = 0;
        playerSpeedDisplay.setValue(percent);

        playerFireRateDisplay = new ProgressBar(2, 100, 1, false, skin);
        int percentFireRate = 0;
        playerFireRateDisplay.setValue(percentFireRate);

        TextButton exitBtn = new TextButton("Exit", skin, "small");

        //Speed Up
        stage.addActor(playerSpeedUpBtn);
        playerSpeedUpBtn.setPosition(10, Constants.Game.WORLD_HEIGHT * 0.8f);

        stage.addActor(playerSpeedDisplay);
        playerSpeedDisplay.setPosition(200, Constants.Game.WORLD_HEIGHT * 0.820f);
        playerSpeedDisplay.setWidth(200f);

        stage.addActor(speedCost);
        speedCost.setColor(Color.BLACK);
        speedCost.setPosition(275, Constants.Game.WORLD_HEIGHT * 0.826f);


        //Fire Rate
        stage.addActor(playerFireRateUpBtn);
        playerFireRateUpBtn.setPosition(10, Constants.Game.WORLD_HEIGHT * 0.65f);

        stage.addActor(playerFireRateDisplay);
        playerFireRateDisplay.setPosition(200, Constants.Game.WORLD_HEIGHT * 0.67f);
        playerFireRateDisplay.setWidth(200f);

        stage.addActor(fireRateCost);
        fireRateCost.setColor(Color.BLACK);
        fireRateCost.setPosition(275, Constants.Game.WORLD_HEIGHT * 0.676f);


        //Exit
        stage.addActor(exitBtn);
        exitBtn.setPosition(875,20);

        String property = "Speed";
        addListeners(playerSpeedUpBtn, property, -1, speedCost, playerSpeedDisplay);
        addListeners(playerSpeedDownBtn, property, 1, speedCost, playerSpeedDisplay);

        property = "FireRate";
        addListeners(playerFireRateUpBtn, property, -1, fireRateCost, playerFireRateDisplay);
        addListeners(playerFireRateDownBtn, property, 1, fireRateCost, playerFireRateDisplay);

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Enemy.resume();
                Gamestate.state = Gamestate.PLAYING;
                pause = false;
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch ( keycode ) {
                    case Input.Keys.ESCAPE: {
                        Gamestate.state = Gamestate.PLAYING;
                        pause = false;
                    }
                    break;
                }
                return super.keyDown(event, keycode);
            }
        });

        percent = (upgrades.get("Speed").getLevel() * 100) / upgrades.get("Speed").getUpgTicks();
        playerSpeedDisplay.setValue(percent);

        percent = (upgrades.get("FireRate").getLevel() * 100) / upgrades.get("FireRate").getUpgTicks();
        playerFireRateDisplay.setValue(percent);

        speedCost.setText(" " + upgrades.get("Speed").getCost());
        fireRateCost.setText(" " + upgrades.get("FireRate").getCost());

    }

    private void addListeners(TextButton textButton, final String property, final int behavior, final Label value, final ProgressBar outputLbl) {
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                labelBehavior(property, behavior, value, outputLbl);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void labelBehavior(String property, int behavior, Label value, ProgressBar outputLbl) {
        load();
        float costIncrements = upgrades.get(property).getCostIncrements();
        float minUpg = upgrades.get(property).getMinUpg();
        float maxUpg = upgrades.get(property).getMaxUpg();
        float upgTicks = upgrades.get(property).getUpgTicks();
        float speed = getProperty(property);
        float upgAmount = -((maxUpg - minUpg) / upgTicks) * behavior;
        int level = upgrades.get(property).getLevel();
        int playerScore = playing.getPlayer().getPlayerScore();
        float baseCost;
        if (behavior == 1) {
            baseCost = (upgrades.get(property).getCost() - costIncrements) * behavior;
            level--;
        } else {
            baseCost = (upgrades.get(property).getCost()) * behavior;
            level++;
        }
        if ((playerScore + baseCost >= 0) && (playerScore + baseCost <= this.playerScore) && (level >= 0) && (level <= upgTicks)) {
            if (Math.abs(speed - maxUpg) > 0) {
                if (behavior == -1) {
                    playerScore += baseCost;
                    upgrades.get(property).upgrade();
                } else {
                    playerScore += baseCost;
                    upgrades.get(property).downgrade();
                }
                float newSpeed = speed + upgAmount;
                setProperty(property, newSpeed);

                scoreLbl.setText("(Score:)" + playing.getPlayer().getPlayerScore());
                float percent = (upgrades.get(property).getLevel() * 100) / upgTicks;
                outputLbl.setValue(percent);
                baseCost = upgrades.get(property).getCost();
                value.setText(" " + baseCost);
                playing.getPlayer().setPlayerScore(playerScore);

                saveGame();
            }
        }
    }

    public float getProperty(String property) {
        switch ( property ) {
            case "Speed": {
                return playing.getPlayer().getPlayerSpeed();
            }
            case "FireRate": {
                return playing.getPlayer().getReloadSpeed();
            }
        }
        return 0;
    }

    public void setProperty(String property, float speed) {
        switch ( property ) {
            case "Speed": {
                playing.getPlayer().setPlayerSpeed(speed);
                break;
            }
            case "FireRate": {
                playing.getPlayer().setReloadSpeed(speed);
                break;
            }
        }
    }

    @Override
    public void render(float delta) {
        scoreLbl.setText("Score: " + playing.getPlayer().getPlayerScore());
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void resize(int i, int i1) {

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

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }
}

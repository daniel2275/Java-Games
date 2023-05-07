package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Json;
import entities.Enemy;

import java.util.HashMap;
import java.util.Map;

import static com.danielr.subgame.SubGame.pause;
import static utilz.Constants.Game.WORLD_HEIGHT;
import static utilz.Constants.Game.WORLD_WIDTH;

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
    }


    public void load() {
        Preferences prefs = Gdx.app.getPreferences("my_prefs");

        String jsonStringDefault = prefs.getString("upgrades", null);
        upgrades = new Json().fromJson(HashMap.class, jsonStringDefault);

        float reloadSpeed = prefs.getFloat("reloadSpeed");
        playing.getPlayer().setReloadSpeed(reloadSpeed);

        float playerSpeed = prefs.getFloat("playerSpeed");
        playing.getPlayer().setPlayerSpeed(playerSpeed);
    }


    public void saveGame() {
        Preferences prefs = Gdx.app.getPreferences("my_prefs");
        String jsonString = new Json().toJson(upgrades);
        prefs.putString("upgrades", jsonString);

        prefs.putInteger("playerScore", playing.getPlayer().getPlayerScore());
        prefs.putFloat("playerHealth", playing.getPlayer().getPlayerHealth());

        int level = playing.getLevelManager().getLevel().getTotalLevels();
        System.out.println(Gamestate.state + " " + level);
        prefs.putInteger("level", ((Gamestate.state == Gamestate.MENU) ? level - 1 : level ) );

        prefs.putFloat("reloadSpeed", playing.getPlayer().getReloadSpeed());
        prefs.putFloat("playerSpeed", playing.getPlayer().getPlayerSpeed());

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

    public void show() {

        // create a skin object
        Skin skin = new Skin(Gdx.files.internal("clean-crispy/skin/clean-crispy-ui.json"));

        stage = new Stage();

        Table table;
        table = new Table();
        table.setFillParent(true);
        table.defaults().align(Align.left).pad(15);
        table.align(Align.left);
        table.setSkin(skin);

        speedCost = new Label("00", skin);
        fireRateCost = new Label("00", skin);

        Window window = new Window("Upgrades", skin);
        Label titleLabel = window.getTitleLabel();
        titleLabel.setAlignment(Align.center);
        window.align(Align.center);
        window.add(table);
        window.setWidth(WORLD_WIDTH);
        window.setHeight(WORLD_HEIGHT - 25);
        stage.addActor(window);

        // Score label creation
        scoreLbl = new Label("(Score:)" + playing.getPlayer().getPlayerScore(), skin);
        scoreLbl.setPosition(5, WORLD_HEIGHT - 25);
        stage.addActor(scoreLbl);

        TextButton playerSpeedUpBtn = new TextButton("(Sub Speed + )", skin);
        TextButton playerSpeedDownBtn = new TextButton("(Sub Speed -)", skin);

        TextButton playerFireRateUpBtn = new TextButton("(Sub FireRate + )", skin);
        TextButton playerFireRateDownBtn = new TextButton("(Sub FireRate -)", skin);

        playerSpeedDisplay = new ProgressBar(2, 100, 1, false, skin);
        int percent = 0;
        playerSpeedDisplay.setValue(percent);

        playerFireRateDisplay = new ProgressBar(2, 100, 1, false, skin);
        int percentFireRate = 0;
        playerFireRateDisplay.setValue(percentFireRate);

        TextButton exitBtn = new TextButton("Exit", skin);

        table.add(playerSpeedUpBtn);
        table.add(playerSpeedDownBtn);
        table.add(speedCost);
        table.add(playerSpeedDisplay);
        table.row();
        table.add(playerFireRateUpBtn);
        table.add(playerFireRateDownBtn);
        table.add(fireRateCost);
        table.add(playerFireRateDisplay);
        table.add(exitBtn);

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
        scoreLbl.setText("(Score:)" + playing.getPlayer().getPlayerScore());
        Gdx.gl.glClearColor(1, 0, 0, 1);
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

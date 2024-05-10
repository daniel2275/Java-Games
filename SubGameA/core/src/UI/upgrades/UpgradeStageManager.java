package UI.upgrades;

import UI.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static com.mygdx.sub.SubGame.pause;
import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;
import static utilities.Constants.UIConstants.*;


public class UpgradeStageManager {
    private GameScreen gameScreen;
    private UpgradeStore upgradeStore;

    private Stage upStage;
    private Skin upSkin;

    private Label scoreLbl;
    private ProgressBar playerSpeedDisplay;
    private ProgressBar playerFireRateDisplay;
    private Label speedCost;
    private Label fireRateCost;

    public UpgradeStageManager(GameScreen gameScreen, UpgradeStore upgradeStore) {
        upStage = new Stage(new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT));
        this.gameScreen = gameScreen;
        this.upgradeStore = upgradeStore;
    }

    public Stage build() {

        loadSkin();

        createUIElements();
        return upStage;
    }

    private void createUIElements() {
        speedCost = new Label("00", upSkin);
        fireRateCost = new Label("00", upSkin);

        // Score label creation
        scoreLbl = new Label("(Score:)" + gameScreen.getPlayer().getPlayerScore(), upSkin);

        //upTable.add(scoreLbl);

        TextButton playerSpeedUpBtn = new TextButton(" Sub Speed + ", upSkin);
        TextButton playerSpeedDownBtn = new TextButton(" Sub Speed - ", upSkin);
        //playerSpeedUpBtn.setPosition(10,10);

        TextButton playerFireRateUpBtn = new TextButton("Sub FireRate +", upSkin);
        TextButton playerFireRateDownBtn = new TextButton("Sub FireRate -", upSkin);

        playerSpeedDisplay = new ProgressBar(2, 100, 1, false, upSkin);
        int percent = 0;
        playerSpeedDisplay.setValue(percent);


        playerFireRateDisplay = new ProgressBar(2, 100, 1, false, upSkin);
        int percentFireRate = 0;
        playerFireRateDisplay.setValue(percentFireRate);


        TextButton exitBtn = new TextButton("Continue", upSkin);

        String property = "Speed";
        addListeners(playerSpeedUpBtn, property, -1, speedCost, playerSpeedDisplay);
        addListeners(playerSpeedDownBtn, property, 1, speedCost, playerSpeedDisplay);

        property = "FireRate";
        addListeners(playerFireRateUpBtn, property, -1, fireRateCost, playerFireRateDisplay);
        addListeners(playerFireRateDownBtn, property, 1, fireRateCost, playerFireRateDisplay);


        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause = false;
                gameScreen.resume();
                gameScreen.getSubGame().setScreen(gameScreen.getSubGame().gamePlayScreen());
            }
        });



        upStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    pause = false;
                    gameScreen.resume();
                    gameScreen.getSubGame().setScreen(gameScreen.getSubGame().gamePlayScreen());
                }
                return super.keyDown(event, keycode);
            }
        });

        if (upgradeStore.getUpgrades().get("Speed") != null) {
            percent = (upgradeStore.getUpgrades().get("Speed").getLevel() * 100) / upgradeStore.getUpgrades().get("Speed").getUpgTicks();
            playerSpeedDisplay.setValue(percent);

            percent = (upgradeStore.getUpgrades().get("FireRate").getLevel() * 100) / upgradeStore.getUpgrades().get("FireRate").getUpgTicks();
            playerFireRateDisplay.setValue(percent);

            speedCost.setText(" " + upgradeStore.getUpgrades().get("Speed").getCost());
            fireRateCost.setText(" " + upgradeStore.getUpgrades().get("FireRate").getCost());
        }

        Table upTable = new Table();
        upTable.setFillParent(true);
        upTable.defaults().space(5);

        upTable.add(scoreLbl).colspan(1);
        scoreLbl.setColor(TITLE_COLOR);
        // Player Speed Controls
        playerSpeedUpBtn.setColor(BUTTON_OPTION_COLOR);
        playerSpeedDownBtn.setColor(BUTTON_OPTION_COLOR);
        upTable.row().padTop(20);
        upTable.add(playerSpeedUpBtn).size(115, 28);
        Stack stackSpeed = new Stack();
        stackSpeed.add(playerSpeedDisplay);
        stackSpeed.add(speedCost);
        speedCost.setAlignment(0,0);
        upTable.add(stackSpeed).size(115,28);
        upTable.add(playerSpeedDownBtn).size(115, 28);

        // Player Fire Rate Controls
        playerFireRateDownBtn.setColor(BUTTON_OPTION_COLOR);
        playerFireRateUpBtn.setColor(BUTTON_OPTION_COLOR);

        upTable.row().padTop(20);
        upTable.add(playerFireRateUpBtn).size(115, 28);
        Stack stackFireRate = new Stack();
        stackFireRate.add(playerFireRateDisplay);
        stackFireRate.add(fireRateCost);
        fireRateCost.setAlignment(0,0);
        upTable.add(stackFireRate).size(115,28);
        upTable.add(playerFireRateDownBtn).size(115, 28);

        // Exit Button
        exitBtn.setColor(BUTTON_QUIT_COLOR);
        upTable.row().padTop(50);
        upTable.add(exitBtn).size(345,50).colspan(3).pad(10);

        upStage.addActor(upTable);

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
        upgradeStore.load();
        System.out.println("property " + property + " " + " behavior " + behavior + " value " + value);
        float costIncrements = upgradeStore.getUpgrades().get(property).getCostIncrements();
        float minUpg = upgradeStore.getUpgrades().get(property).getMinUpg();
        float maxUpg = upgradeStore.getUpgrades().get(property).getMaxUpg();
        float upgTicks = upgradeStore.getUpgrades().get(property).getUpgTicks();
        float speed = getProperty(property);
        float upgAmount = -((maxUpg - minUpg) / upgTicks) * behavior;
        int level = upgradeStore.getUpgrades().get(property).getLevel();
        float playerScore = gameScreen.getPlayer().getPlayerScore();
        float baseCost;
        if (behavior == 1) {
            baseCost = (upgradeStore.getUpgrades().get(property).getCost() - costIncrements) * behavior;
            level--;
        } else {
            baseCost = (upgradeStore.getUpgrades().get(property).getCost()) * behavior;
            level++;
        }
        if ((playerScore + baseCost >= 0) && (playerScore + baseCost <= upgradeStore.getPlayerScore()) && (level >= 0) && (level <= upgTicks)) {
            if (Math.abs(speed - maxUpg) > 0) {
                if (behavior == -1) {
                    playerScore += baseCost;
                    upgradeStore.getUpgrades().get(property).upgrade();
                } else {
                    playerScore += baseCost;
                    upgradeStore.getUpgrades().get(property).downgrade();
                }
                float newSpeed = speed + upgAmount;
                setProperty(property, newSpeed);

                scoreLbl.setText("(Score:)" + gameScreen.getPlayer().getPlayerScore());
                float percent = (upgradeStore.getUpgrades().get(property).getLevel() * 100) / upgTicks;
                outputLbl.setValue(percent);
                baseCost = upgradeStore.getUpgrades().get(property).getCost();
                value.setText(" " + baseCost);
                gameScreen.getPlayer().setPlayerScore(playerScore);

                upgradeStore.saveGame();
            }
        }
    }

    public float getProperty(String property) {
        switch ( property ) {
            case "Speed": {
                return gameScreen.getPlayer().getPlayerSpeed();
            }
            case "FireRate": {
                return gameScreen.getPlayer().getPlayerActor().getReloadSpeed();
            }
        }
        return 0;
    }

    public void setProperty(String property, float speed) {
        switch ( property ) {
            case "Speed": {
                gameScreen.getPlayer().setPlayerSpeed(speed);
                break;
            }
            case "FireRate": {
                gameScreen.getPlayer().getPlayerActor().setReloadSpeed(speed);
                break;
            }
        }
    }

    private void loadSkin() {
        // create a skin object
        upSkin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
    }

    public Label getScoreLbl() {
        return scoreLbl;
    }

    public void resetUpgrades() {
        int percent;

        percent = (upgradeStore.getUpgrades().get("Speed").getLevel() * 100) / upgradeStore.getUpgrades().get("Speed").getUpgTicks();
        playerSpeedDisplay.setValue(percent);

        percent = (upgradeStore.getUpgrades().get("FireRate").getLevel() * 100) / upgradeStore.getUpgrades().get("FireRate").getUpgTicks();
        playerFireRateDisplay.setValue(percent);

        speedCost.setText(" " + upgradeStore.getUpgrades().get("Speed").getCost());
        fireRateCost.setText(" " + upgradeStore.getUpgrades().get("FireRate").getCost());
    }
}


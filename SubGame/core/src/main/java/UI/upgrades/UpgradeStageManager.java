package UI.upgrades;

import UI.game.FontManager;
import UI.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import static io.github.daniel2275.subgame.SubGame.pause;
import static utilities.Settings.Game.VIRTUAL_HEIGHT;
import static utilities.Settings.Game.VIRTUAL_WIDTH;
import static utilities.Settings.UIConstants.*;


public class UpgradeStageManager {
    private GameScreen gameScreen;
    private UpgradeStore upgradeStore;

    private Stage upStage;
    private Skin upSkin;

    private Label scoreLbl;
    private ProgressBar playerSpeedDisplay;
    private ProgressBar playerFireRateDisplay;
    private ProgressBar playerDamageDisplay;
    private float percentSpeed;
    private float percentFireRate;
    private float percentDamage;
    private Label speedCost;
    private Label fireRateCost;
    private Label damageCost;
    private Table upTable;
    private boolean alternate = false;

    public UpgradeStageManager(GameScreen gameScreen, UpgradeStore upgradeStore) {
        upStage = new Stage(new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT));
        this.gameScreen = gameScreen;
        this.upgradeStore = upgradeStore;
    }

    public Stage build() {
        upTable = new Table();
        loadSkin();
        createUIElements();
        upTable.setFillParent(true);
        upTable.defaults().space(5);
        upStage.addActor(upTable);
        return upStage;
    }

    public void animateButtonsOnShow() {
        final float initialDelay = 0.1f; // Fixed delay
        final float delayIncrement = 0.1f; // Fixed delay increment
        final float animationDuration = 0.1f; // Fixed animation duration

        upTable.setVisible(true);

        // Schedule layout force with a delay
        upStage.addAction(Actions.sequence(
            Actions.delay(initialDelay), // Delay before forcing layout
            Actions.run(() -> {
                upTable.layout(); // Force layout recalculation
                upTable.setLayoutEnabled(false);

                    float delay = 0f; // Initialize local variable for delay

                    for (Actor actor : upTable.getChildren()) {
                        if (actor instanceof TextButton || actor instanceof Slider || actor instanceof Label || actor instanceof Stack) {
                            // Get the button's final position as set by the table's layout
                            float finalX = actor.getX();
                            float finalY = actor.getY();

                            // Move the button off-screen initially (e.g., above the screen)
                            if (alternate) {
                                actor.setPosition(0, -actor.getHeight());
                            } else {
                                actor.setPosition(Gdx.graphics.getWidth(), -actor.getHeight());
                            }
                            alternate = !alternate;

                            actor.setVisible(true);

                            Action moveIn = Actions.moveTo(finalX, finalY, animationDuration, Interpolation.smooth);
                            actor.addAction(Actions.sequence(Actions.delay(delay), moveIn));

                            delay += delayIncrement;
                        }
                    }

                    // Re-enable layout and force an update after the animations
                    upStage.addAction(Actions.sequence(
                        Actions.delay(delay + 0.2f),
                        Actions.run(() -> {
                            upTable.setLayoutEnabled(true);
                            upTable.layout();    // Force a final layout update
                        })
                    ));
                })

        ));
    }


    private void createUIElements() {
        upTable.setVisible(false);
        speedCost = new Label("00", upSkin);
        fireRateCost = new Label("00", upSkin);
        damageCost = new Label("00", upSkin);

        speedCost.setColor(Color.BLACK);
        fireRateCost.setColor(Color.BLACK);
        damageCost.setColor(Color.BLACK);

        // Score label creation
        scoreLbl = new Label("(Score:)" + gameScreen.getPlayer().getPlayerScore(), upSkin);

        //upTable.add(scoreLbl);

        TextButton playerSpeedUpBtn = new TextButton(" Sub Speed + ", upSkin);
        TextButton playerSpeedDownBtn = new TextButton(" Sub Speed - ", upSkin);

        TextButton playerFireRateUpBtn = new TextButton("Sub FireRate +", upSkin);
        TextButton playerFireRateDownBtn = new TextButton("Sub FireRate -", upSkin);

        TextButton playerDamageUpBtn = new TextButton("Sub Damage +", upSkin);
        TextButton playerDamageDownBtn = new TextButton("Sub Damage -", upSkin);

        playerSpeedDisplay = new ProgressBar(2, 100, 1, false, upSkin);
        percentSpeed = 0;
        playerSpeedDisplay.setValue(percentSpeed);

        playerFireRateDisplay = new ProgressBar(2, 100, 1, false, upSkin);
        percentFireRate = 0;
        playerFireRateDisplay.setValue(percentFireRate);

        playerDamageDisplay = new ProgressBar(2, 100, 1, false, upSkin);
        percentDamage = 0;
        playerDamageDisplay.setValue(percentDamage);


        TextButton exitBtn = new TextButton("Continue", upSkin);

        String property = "Speed";
        addListeners(playerSpeedDownBtn, property, 1, speedCost, playerSpeedDisplay);
        addListeners(playerSpeedUpBtn, property, -1, speedCost, playerSpeedDisplay);

        property = "FireRate";
        addListeners(playerFireRateDownBtn, property, 1, fireRateCost, playerFireRateDisplay);
        addListeners(playerFireRateUpBtn, property, -1, fireRateCost, playerFireRateDisplay);

        property = "Damage";
        addListeners(playerDamageDownBtn, property, 1, damageCost, playerDamageDisplay);
        addListeners(playerDamageUpBtn, property, -1, damageCost, playerDamageDisplay);

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause = false;
                gameScreen.resume();
                gameScreen.getSubGame().setScreen(gameScreen.getSubGame().gameScreen());
            }
        });

        upStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    pause = false;
                    gameScreen.resume();
                    gameScreen.getSubGame().setScreen(gameScreen.getSubGame().gameScreen());
                }
                return super.keyDown(event, keycode);
            }
        });

        if (upgradeStore.getUpgradeManager().getUpgrade("Speed") != null) {

            percentSpeed = ((float) (upgradeStore.getUpgradeManager().getUpgrade("Speed").getUpgradeLevel() * 100) / upgradeStore.getUpgradeManager().getUpgrade("Speed").getTicks());
            if (upgradeStore.getUpgradeManager().getUpgrade("Speed").getUpgradeLevel() == upgradeStore.getUpgradeManager().getUpgrade("Speed").getTicks()) {
                speedCost.setText("MAX");
            }
            playerSpeedDisplay.setValue(percentSpeed);

            percentFireRate = ((float) (upgradeStore.getUpgradeManager().getUpgrade("FireRate").getUpgradeLevel() * 100) / upgradeStore.getUpgradeManager().getUpgrade("FireRate").getTicks());
            playerFireRateDisplay.setValue(percentFireRate);

            percentDamage = ((float) (upgradeStore.getUpgradeManager().getUpgrade("Damage").getUpgradeLevel() * 100) / upgradeStore.getUpgradeManager().getUpgrade("Damage").getTicks());
            playerDamageDisplay.setValue(percentDamage);

            speedCost.setText(" " + upgradeStore.getUpgradeManager().getUpgrade("Speed").getCost());
            fireRateCost.setText(" " + upgradeStore.getUpgradeManager().getUpgrade("FireRate").getCost());
            damageCost.setText(" " + upgradeStore.getUpgradeManager().getUpgrade("Damage").getCost());
        }


        upTable.add(scoreLbl).colspan(1);
        scoreLbl.setColor(TITLE_COLOR);
        // Player Speed Controls
        playerSpeedUpBtn.setColor(BUTTON_OPTION_COLOR);
        playerSpeedDownBtn.setColor(BUTTON_OPTION_COLOR);
        upTable.row().padTop(20);
        upTable.add(playerSpeedDownBtn).size(115, 28);
        Stack stackSpeed = new Stack();
        stackSpeed.add(playerSpeedDisplay);
        stackSpeed.add(speedCost);
        speedCost.setAlignment(0, 0);
        upTable.add(stackSpeed).size(115, 28);
        upTable.add(playerSpeedUpBtn).size(115, 28);

        // Player Fire Rate Controls
        playerFireRateDownBtn.setColor(BUTTON_OPTION_COLOR);
        playerFireRateUpBtn.setColor(BUTTON_OPTION_COLOR);
        upTable.row().padTop(20);
        upTable.add(playerFireRateDownBtn).size(115, 28);
        Stack stackFireRate = new Stack();
        stackFireRate.add(playerFireRateDisplay);
        stackFireRate.add(fireRateCost);
        fireRateCost.setAlignment(0, 0);
        upTable.add(stackFireRate).size(115, 28);
        upTable.add(playerFireRateUpBtn).size(115, 28);

        // Player Damage Controls
        playerDamageDownBtn.setColor(BUTTON_OPTION_COLOR);
        playerDamageUpBtn.setColor(BUTTON_OPTION_COLOR);
        upTable.row().padTop(20);
        upTable.add(playerDamageDownBtn).size(115, 28);
        Stack stackDamage = new Stack();
        stackDamage.add(playerDamageDisplay);
        stackDamage.add(damageCost);
        damageCost.setAlignment(0, 0);
        upTable.add(stackDamage).size(115, 28);
        upTable.add(playerDamageUpBtn).size(115, 28);

        // Exit Button
        exitBtn.setColor(BUTTON_QUIT_COLOR);
        upTable.row().padTop(50);
        upTable.add(exitBtn).size(345, 50).colspan(3).pad(10);

        hideEverything();
    }


    public void hideEverything() {
        for (Actor actor : upTable.getChildren()) {
            actor.setVisible(false);
        }
        upTable.setVisible(false); // Optionally hide the entire table
    }

    public void hide() {
        // Clear the input processor when the stage is hidden
        Gdx.input.setInputProcessor(null);
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
        UpgradeManager upgradeManager = upgradeStore.getUpgradeManager();
        Upgrade upgrade = upgradeManager.getUpgrade(property);

        float minUpgrade = upgrade.getMinUpgrade();
        float maxUpgrade = upgrade.getMaxUpgrade();
        int upgradeTicks = upgrade.getTicks();
        float currentProperty = getProperty(property);
        float upgradeAmount = calculateUpgradeAmount(minUpgrade, maxUpgrade, upgradeTicks, behavior);

        int currentLevel = upgrade.getUpgradeLevel();
        int playerScore = upgradeManager.getSaveGame("SaveGame").getPlayerScore();
        int baseCost = upgrade.getCost() * behavior;

        if (isValidUpgrade(playerScore, baseCost, currentLevel, upgradeTicks, behavior)) {
            if (behavior == -1) {
                processUpgradePurchase(upgradeManager, property);
                currentLevel++;
            } else {
                processUpgradeSale(upgradeManager, property);
                baseCost = upgrade.getCost(); // Refresh cost for selling
                currentLevel--;
            }

            playerScore += baseCost;

            // Update game parameters
            float newPropertyValue = currentProperty + upgradeAmount;
            setProperty(property, newPropertyValue);

            float progressPercent = (float) (currentLevel * 100) / upgradeTicks;
            outputLbl.setValue(progressPercent);

            baseCost = upgrade.getCost();

            if (currentLevel == upgradeTicks) {
                value.setText("MAX");
            } else {
                value.setText(" " + Math.abs(baseCost));
            }

            gameScreen.getPlayer().setPlayerScore(playerScore);
            upgradeStore.saveGame();
        }
    }

    private float calculateUpgradeAmount(float minUpgrade, float maxUpgrade, int upgradeTicks, int behavior) {
        if (minUpgrade > maxUpgrade) {
            return ((minUpgrade - maxUpgrade) / upgradeTicks) * behavior;
        } else {
            return -((maxUpgrade - minUpgrade) / upgradeTicks) * behavior;
        }
    }

    private boolean isValidUpgrade(int playerScore, int baseCost, int currentLevel, int upgradeTicks, int behavior) {
        return (playerScore + baseCost >= 0) && ((currentLevel - behavior) <= upgradeTicks) && ((currentLevel - behavior) >= 0);
    }

    private void processUpgradePurchase(UpgradeManager upgradeManager, String property) {
        upgradeManager.buyUpgrade(property);
    }

    private void processUpgradeSale(UpgradeManager upgradeManager, String property) {
        upgradeManager.sellUpgrade(property);
    }


    public float getProperty(String property) {
        switch ( property ) {
            case "Speed": {
                return gameScreen.getPlayer().getPlayerSpeed();
            }
            case "FireRate": {
                return gameScreen.getPlayer().getPlayerActor().getReloadSpeed();
            }
            case "Damage": {
                return gameScreen.getPlayer().getPlayerActor().getDamage();
            }
        }
        return 0;
    }

    public void setProperty(String property, float value) {
        switch ( property ) {
            case "Speed": {
                gameScreen.getPlayer().setPlayerSpeed(value);
                upgradeStore.getUpgradeManager().getUpgrade("Speed").setActualValue(value);
                break;
            }
            case "FireRate": {
                gameScreen.getPlayer().getPlayerActor().setReloadSpeed(value);
                upgradeStore.getUpgradeManager().getUpgrade("FireRate").setActualValue(value);
                break;
            }
            case "Damage": {
                gameScreen.getPlayer().getPlayerActor().setDamage(value);
                upgradeStore.getUpgradeManager().getUpgrade("Damage").setActualValue(value);
                break;
            }
        }
    }

//    private void loadSkin() {
//        // create a skin object
//        upSkin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
//    }

    private void loadSkin() {
        try {
            upSkin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));

            // Load the custom font
            FontManager.loadFont(12);

            // Add the custom font to the skin and replace the default font
            upSkin.add("default-font", FontManager.getFont());

            // Modify existing styles to use the new font
            Label.LabelStyle labelStyle = upSkin.get(Label.LabelStyle.class);
            labelStyle.font = FontManager.getFont();
            upSkin.add("default", labelStyle);

            TextButton.TextButtonStyle buttonStyle = upSkin.get(TextButton.TextButtonStyle.class);
            buttonStyle.font = FontManager.getFont();
            upSkin.add("default", buttonStyle);

        } catch (Exception e) {
            Gdx.app.error("MainMenu", "Error loading skin file", e);
        }
    }

    public Label getScoreLbl() {
        return scoreLbl;
    }

    public void resetUpgrades() {
        percentSpeed = 0;
        percentFireRate = 0;
        percentDamage = 0;
        playerSpeedDisplay.setValue(percentSpeed);
        playerFireRateDisplay.setValue(percentFireRate);
        playerDamageDisplay.setValue(percentDamage);

        speedCost.setText(" " + upgradeStore.getUpgradeManager().getUpgrade("Speed").getCost());
        fireRateCost.setText(" " + upgradeStore.getUpgradeManager().getUpgrade("FireRate").getCost());
        damageCost.setText("" + upgradeStore.getUpgradeManager().getUpgrade("Damage").getCost());

    }
}

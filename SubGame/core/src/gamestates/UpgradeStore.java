package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import java.util.HashMap;
import java.util.Map;

import static utilz.Constants.Game.WORLD_HEIGHT;
import static utilz.Constants.Game.WORLD_WIDTH;

public class UpgradeStore implements Screen, InputProcessor {
private Stage stage;
private Map<String, Upgrade> upgrades;

private Playing playing;
private Label scoreLbl;

private Upgrade upgradeSpeed;
private Upgrade upgradeFireRate;

private float playerScore;

// Score label creation font name
private BitmapFont font = loadFont("fonts/SmallTypeWriting.ttf");

    public UpgradeStore(Playing playing) {
        this.playerScore = playing.getPlayer().getPlayerScore();
        this.playing = playing;
        loadInit();
        show();
    }

    private  void loadInit() {
        upgrades = new HashMap<>();
        upgradeSpeed = new Upgrade("Speed", 10f,2f, 0f,2f,40,0);
        upgradeFireRate =  new Upgrade("FireRate", 10f,2f, 3f,0f, 40,0);
        upgrades.put("Speed", upgradeSpeed);
        upgrades.put("FireRate", upgradeFireRate);
    }

    // Score label creation (load font)
    private BitmapFont loadFont(String fontName){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16; // font size
        BitmapFont font = generator.generateFont(parameter); // generate the BitmapFont
        generator.dispose(); // dispose the generator when you're done
        return font;
    }


//    public void addUpgrade(String name, Upgrade upgrade) {
//        upgrades.put(name, upgrade);
//    }
//
//    public Upgrade getUpgrade(String name) {
//        return upgrades.get(name);
//    }
//
//    public void upgrade(String name) {
//        Upgrade upgrade = upgrades.get(name);
//        if (upgrade != null) {
//            upgrade.upgrade();
//        }
//    }
//
//    public void downgrade(String name) {
//        Upgrade upgrade = upgrades.get(name);
//        if (upgrade != null) {
//            upgrade.downgrade();
//        }
//    }

    @Override
    public void show() {

        // create a skin object
        Skin skin = new Skin(Gdx.files.internal("assets/clean-crispy/skin/clean-crispy-ui.json"));

        stage = new Stage();

        Table table;
        table = new Table();
        table.setFillParent(true);
        table.defaults().align(Align.left).pad(15);
        table.align(Align.left);
        table.setSkin(skin);

        Label speedCost = new Label("00", skin);
        Label fireRateCost = new Label("00", skin);

        Window window =  new Window("Upgrades", skin);
        Label titleLabel = window.getTitleLabel();
        titleLabel.setAlignment(Align.center);
        window.align(Align.center);
        window.add(table);
        window.setWidth(WORLD_WIDTH);
        window.setHeight(WORLD_HEIGHT - 25);
        stage.addActor(window);

        // Score label creation
        scoreLbl = new Label("(Score:)" + playing.getPlayer().getPlayerScore(), skin);
        scoreLbl.setPosition(5,WORLD_HEIGHT - 25);
        stage.addActor(scoreLbl);

        TextButton playerSpeedUpBtn = new TextButton("(Sub Speed + )", skin);
        TextButton playerSpeedDownBtn = new TextButton("(Sub Speed -)", skin);

        TextButton playerFireRateUpBtn = new TextButton("(Sub FireRate + )", skin);
        TextButton playerFireRateDownBtn = new TextButton("(Sub FireRate -)", skin);

        ProgressBar playerSpeedDisplay = new ProgressBar(2,100, 1,false, skin);
        float percent = (playing.getPlayer().getPlayerSpeed() * 100) / 2;
        playerSpeedDisplay.setValue(percent);

        ProgressBar playerFireRateDisplay = new ProgressBar(2,100, 1,false, skin);
        float percentFireRate = (playing.getPlayer().getReloadSpeed() + (float) (upgrades.get("FireRate").getMaxUpg()) * 100) / 2;
        playerFireRateDisplay.setValue(percentFireRate);

        TextButton exitBtn = new TextButton("Exit", skin);

        table.add(playerSpeedUpBtn);
        table.add(playerSpeedDownBtn);;
        table.add(speedCost);
        table.add(playerSpeedDisplay);
        table.row();
        table.add(playerFireRateUpBtn);
        table.add(playerFireRateDownBtn);
        table.add(fireRateCost);
        table.add(playerFireRateDisplay);
        table.add(exitBtn);

        String property = "Speed";
        addListeners(playerSpeedUpBtn, property, upgradeSpeed, -1, speedCost, playerSpeedDisplay);
        addListeners(playerSpeedDownBtn, property, upgradeSpeed, 1, speedCost, playerSpeedDisplay);

        property = "FireRate";
        addListeners(playerFireRateUpBtn, property, upgradeFireRate,-1, fireRateCost, playerFireRateDisplay);
        addListeners(playerFireRateDownBtn, property, upgradeFireRate, 1, fireRateCost, playerFireRateDisplay);



        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gamestate.state = Gamestate.PLAYING;
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                switch ( keycode ) {
                    case Input.Keys.ESCAPE : {
                        Gamestate.state = Gamestate.PLAYING;
                    }
                    break;
                }
                return super.keyDown(event, keycode);
            }
        });

        speedCost.setText(" "  + upgrades.get("Speed").getCost());
        fireRateCost.setText(" "  + upgrades.get("Speed").getCost());
    }


    private void addListeners(final TextButton textButton,final String property, final Upgrade upgrade, final int behavior, final Label value, final ProgressBar outputLbl) {
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                labelBehavior(property, upgrade, behavior, value, outputLbl);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void labelBehavior(String property, Upgrade upgrade, int behavior, Label value, ProgressBar outputLbl) {
        float costIncrements = upgrades.get(property).getCostIncrements();
        float minUpg = upgrades.get(property).getMinUpg();
        float maxUpg = upgrades.get(property).getMaxUpg();
        float upgTicks = upgrades.get(property).getUpgTicks();

        float speed = getProperty(property);
        float upgAmount = -((maxUpg - minUpg) / upgTicks) * behavior;

        int level = upgrade.getLevel();
        int playerScore = playing.getPlayer().getPlayerScore();

        float baseCost ;
        if (behavior == 1) {
            baseCost = (upgrades.get(property).getCost() - costIncrements) * behavior ;
            level --;
        } else {
            baseCost = (upgrades.get(property).getCost()) * behavior;
            level ++;
        }

        System.out.println("speed " + speed + "  local " + this.playerScore + "  var " + playerScore + "  basecost " + baseCost + " ticks " + upgTicks
                + " maxupg " + maxUpg + " upgamo " + upgAmount + " lvl " + level + " upg level " + upgrade.getLevel());

                if ((playerScore + baseCost >= 0) && (playerScore + baseCost <= this.playerScore)  && (level >= 0) && (level <= upgTicks)) {

                    if (Math.abs(speed - maxUpg) > 0 ) {

                        if (behavior == -1) {
                            playerScore += baseCost;
                            upgrade.upgrade();
                            System.out.println("up");
                        } else {
                            playerScore += baseCost;
                            upgrade.downgrade();
                            System.out.println("down");
                        }

                        float newSpeed = speed + upgAmount;
                        setProperty(property, newSpeed);

                        System.out.println(upgrade.getName() + " LEVEL:" + upgrade.getLevel());

                        scoreLbl.setText("(Score:)" + playing.getPlayer().getPlayerScore());

                        float percent = (getProperty(property) * 100) / maxUpg;

                        outputLbl.setValue(percent);

                        baseCost = upgrade.getCost();

                        value.setText(" " + baseCost);

                        playing.getPlayer().setPlayerScore(playerScore);
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
//                upgradeSpeed.upgrade();
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

    @Override
    public boolean keyDown(int i) {
        return false;
    }

    @Override
    public boolean keyUp(int i) {
        return false;
    }

    @Override
    public boolean keyTyped(char c) {
        return false;
    }

    @Override
    public boolean touchDown(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchUp(int i, int i1, int i2, int i3) {
        return false;
    }

    @Override
    public boolean touchDragged(int i, int i1, int i2) {
        return false;
    }

    @Override
    public boolean mouseMoved(int i, int i1) {
        return false;
    }

    @Override
    public boolean scrolled(float v, float v1) {
        return false;
    }

    public void setPlayerScore(float playerScore) {
        this.playerScore = playerScore;
    }
}

package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;

import static utilz.Constants.Game.WORLD_HEIGHT;

public class Upgrades implements Screen, InputProcessor {

private Table table = new Table();;
private Stage stage;

private Label scoreLbl;

private Playing playing;

private int playerScore;

private BitmapFont font = loadFont("fonts/SmallTypeWriting.ttf");

    public Upgrades(Playing playing) {
        this.playing = playing;
        show();
    }


    @Override
    public void show() {
        stage = new Stage();
        table = new Table();

        table.setFillParent(true);

        table.defaults().align(Align.left).pad(10);

        // create a skin object
        Skin skin = new Skin(Gdx.files.internal("assets/clean-crispy/skin/clean-crispy-ui.json"));

        table.setSkin(skin);
        stage.addActor(table);

//        table.setX(0);
//        table.setY(0);
        scoreLbl = new Label("(Score:)" + playing.getPlayer().getPlayerScore(), new Label.LabelStyle(font, Color.WHITE));
        scoreLbl.setPosition(5,WORLD_HEIGHT - 25);
        stage.addActor(scoreLbl);

        TextButton playerSpeedUpBtn = new TextButton("(Sub Speed + cost:)" + (int) (((2 - playing.getPlayer().getPlayerSpeed() + 0.05) / 40) * 3 + 10), skin);
        TextButton playerSpeedDownBtn = new TextButton("(Sub Speed -)", skin);

//        final Label playerSpeedDisplay = new Label("(" +  playing.getPlayer().getPlayerSpeed() + ")" , skin);

        ProgressBar playerSpeedDisplay = new ProgressBar(2,100, 1,false, skin);
        float percent = (playing.getPlayer().getPlayerSpeed() * 100) / 2;
        playerSpeedDisplay.setValue(percent);

        TextButton exitBtn = new TextButton("Exit", skin);

        table.add(playerSpeedUpBtn);
        table.add(playerSpeedDownBtn);;
        table.add(playerSpeedDisplay);
        table.row();
        table.add(exitBtn);

        addListeners(playerSpeedUpBtn, 1, playerSpeedDisplay);
        addListeners(playerSpeedDownBtn, 2, playerSpeedDisplay);

        exitBtn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Item3 was clicked.");
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

    }

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


    private BitmapFont loadFont(String fontName){
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal(fontName));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 16; // font size
        BitmapFont font = generator.generateFont(parameter); // generate the BitmapFont
        generator.dispose(); // dispose the generator when you're done
        return font;
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


    private void addListeners(final TextButton textButton, final int behavior, final ProgressBar outputLbl) {
        textButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                labelBehavior(behavior, outputLbl);
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }


    private void labelBehavior(int behavior, ProgressBar outputLbl) {

            float baseCost = 10;
            float increments = 3;
            float maxUpg = 2;
            float upgTicks = 40;
            float upgAmount = (maxUpg / upgTicks);

            switch ( behavior ) {
                case 1: {
                    int playerScore = playing.getPlayer().getPlayerScore();
                    if (playerScore - 10 >= 0) {
                        double speed = playing.getPlayer().getPlayerSpeed();
                        if (speed < 2) {
                            double newSpeed = speed + upgAmount;
                            playing.getPlayer().setPlayerSpeed((float) newSpeed);
                            scoreLbl.setText("(Score:)" + playing.getPlayer().getPlayerScore());
                            float percent = (playing.getPlayer().getPlayerSpeed() * 100) / maxUpg;
                            outputLbl.setValue(percent);
                            playerScore -= (int) (((maxUpg - newSpeed) / upgTicks) * increments + baseCost);
                            playing.getPlayer().setPlayerScore(playerScore);
                            System.out.println(newSpeed);
                        }
                    }
                    break;
                }
                case 2: {
                    int playerScore = playing.getPlayer().getPlayerScore();
                    if (this.playerScore + 10 > playerScore) {
                        double speed = playing.getPlayer().getPlayerSpeed();
                        if (speed > 0) {
                            double newSpeed = speed - upgAmount;
                            playing.getPlayer().setPlayerSpeed((float) newSpeed);
                            float percent = (playing.getPlayer().getPlayerSpeed() * 100) / 2;
                            outputLbl.setValue(percent);
                            playerScore += (int) (((maxUpg - newSpeed) / upgTicks) * increments + baseCost);
                            playing.getPlayer().setPlayerScore(playerScore);
                            System.out.println(newSpeed);
                        }
                    }
                    break;
                }

            }


    }
}




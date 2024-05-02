package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.sub.SubGame;
import utilities.SoundManager;

import java.util.Random;

import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;
import static utilities.Constants.UIConstants.*;

public class OptionsStageManager {
    private Stage opStage;
    private Table opTable;
    private Skin opSkin;
    private Screen OptionScreen;
    private Slider slider;
    private float volume;
    private SubGame subGame;
    private SoundManager soundManager;
    private AudioDevice audioDevice = Gdx.audio.newAudioDevice(44100, true);

    public OptionsStageManager(SubGame subGame) {
        this.subGame = subGame;
        this.soundManager = SoundManager.getInstance(subGame);
        opStage = new Stage(new ScreenViewport());
    }

    public Stage build() {
        opTable = new Table();
        loadSkin();
        createOpElements();
        opTable.setFillParent(true); // Makes the table take the whole stage
        opStage.addActor(opTable);
        return opStage;
    }

    private void loadSkin() {
        try {
            opSkin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
        } catch (Exception e) {
            Gdx.app.error("Options", "Error loading skin file", e);
        }
    }

    private void createOpElements() {
        opTable.setFillParent(true);
        opStage.addActor(opTable);

        Label optionsTitle = new Label(OPTIONS_TITLE_TEXT, opSkin);
        opTable.add(optionsTitle).padBottom(BUTTON_PADDING).row();

        Label optionsVolume = new Label(OPTIONS_BUTTON_VOLUME_TEXT, opSkin);

        float minValue = 0.0f;
        float maxValue = 1.0f;
        float stepSize = 0.01f;
        Slider.SliderStyle sliderStyle = opSkin.get(SLIDER_TYPE, Slider.SliderStyle.class);
        slider = new Slider(minValue, maxValue, stepSize, false, sliderStyle);
        slider.setBounds(WORLD_WIDTH / 2 - 265, WORLD_HEIGHT * 0.78f, 250, 40);

        TextButton quit = new TextButton("Back", opSkin, "default");
        quit.setPosition(WORLD_WIDTH - 150f, WORLD_HEIGHT * 0.1f);

        opStage.addActor(optionsTitle);
        opStage.addActor(optionsVolume);
        opStage.addActor(slider);
        opStage.addActor(quit);

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (volume != slider.getValue()) {
                    volume = slider.getValue();
                    audioDevice.setVolume(volume);
                    randomSound();
                }
            }
        });

        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                subGame.setScreen(subGame.getMenuRenderer());
            }
        });


    }


    private void randomSound() {
        Random rnd = new Random();
        int rndSoundNo = rnd.nextInt(4);

        switch ( rndSoundNo ) {
            case 0:
                soundManager.playDepthChargeFar();
                break;
            case 1:
                soundManager.playDepthChargeHit();
                break;
            case 2:
                soundManager.playTorpedoHit();
                break;
            case 3:
                soundManager.playExplode();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + rndSoundNo);
        }
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        this.slider.setValue(volume);
    }

    public void hide() {
        // Clear the input processor when the stage is hidden
        Gdx.input.setInputProcessor(null);
    }

    public void dispose() {
        // Dispose of resources such as the stage and skin
        opStage.dispose();
        opSkin.dispose();
        audioDevice.dispose();
    }

}

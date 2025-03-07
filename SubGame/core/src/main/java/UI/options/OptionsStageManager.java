package UI.options;

import UI.game.FontManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.daniel2275.subgame.SubGame;
import utilities.SoundManager;

import java.util.Random;

import static utilities.Settings.UIConstants.*;

public class OptionsStageManager {
    private Stage opStage;
    private Table opTable;
    private Skin opSkin;
    private Slider slider;
    private float volume;
    private SubGame subGame;
    private SoundManager soundManager;
    private AudioDevice audioDevice = Gdx.audio.newAudioDevice(44100, true);
    private boolean alternate = true;
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

    public void animateButtonsOnShow() {
        final float initialDelay = 0.1f; // Fixed delay
        final float delayIncrement = 0.1f; // Fixed delay increment
        final float animationDuration = 0.2f; // Fixed animation duration
        opTable.setVisible(true);

        // Schedule layout force with a delay
        opStage.addAction(Actions.sequence(
            Actions.delay(initialDelay), // Delay before forcing layout
            Actions.run(() -> {
                opTable.layout(); // Force layout recalculation
                opTable.setLayoutEnabled(false);

                float delay = 0f; // Initialize local variable for delay

                for (Actor actor : opTable.getChildren()) {
                    actor.setVisible(true);
                    if (actor instanceof TextButton || actor instanceof Slider || actor instanceof Label) {
                        float finalX = actor.getX();
                        float finalY = actor.getY();

                        if (alternate) {
                            actor.setPosition(0, -actor.getHeight());
                        } else {
                            actor.setPosition(Gdx.graphics.getWidth(), -actor.getHeight());
                        }
                        alternate = !alternate;

                        Action moveIn = Actions.moveTo(finalX, finalY, animationDuration, Interpolation.smooth);
                        actor.addAction(Actions.sequence(Actions.delay(delay), moveIn));

                        delay += delayIncrement;
                    }
                }

                // Re-enable layout and force an update after the animations
                opStage.addAction(Actions.sequence(
                    Actions.delay(delay + 0.2f), // Adjust delay if needed
                    Actions.run(() -> {
                        opTable.setLayoutEnabled(true);
                        opTable.layout(); // Force a final layout update
                    })
                ));
            })
        ));
    }


    private void loadSkin() {
        try {
            opSkin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));

            // Load the custom font
            FontManager.loadFont(12);

            // Add the custom font to the skin and replace the default font
            opSkin.add("default-font", FontManager.getFont());

            // Modify existing styles to use the new font
            Label.LabelStyle labelStyle = opSkin.get(Label.LabelStyle.class);
            labelStyle.font = FontManager.getFont();
            opSkin.add("default", labelStyle);

            TextButton.TextButtonStyle buttonStyle = opSkin.get(TextButton.TextButtonStyle.class);
            buttonStyle.font = FontManager.getFont();
            opSkin.add("default", buttonStyle);

        } catch (Exception e) {
            Gdx.app.error("MainMenu", "Error loading skin file", e);
        }
    }

    private void createOpElements() {
        float scale = Gdx.graphics.getWidth() / 800.0f; // Adjust the scale factor based on a reference width (800 in this case)

        // Positioning optionsTitle label at the top of the screen
        Label optionsTitle = createTitle();
        optionsTitle.setFontScale(2 * scale);
        opTable.add(optionsTitle).padBottom(BUTTON_PADDING * scale).colspan(3).row();

        // Creating optionsVolume label and positioning it beside the slider
        Label optionsVolume = new Label(OPTIONS_BUTTON_VOLUME_TEXT, opSkin);
        optionsVolume.setFontScale(scale);
        optionsVolume.setColor(BUTTON_OPTION_COLOR);
        opTable.add(optionsVolume).colspan(1).row();

        float minValue = 0.0f;
        float maxValue = 1.0f;
        float stepSize = 0.01f;
        Slider.SliderStyle sliderStyle = opSkin.get(SLIDER_TYPE, Slider.SliderStyle.class);
        slider = new Slider(minValue, maxValue, stepSize, false, sliderStyle);
        slider.setColor(BUTTON_OPTION_COLOR);

        opTable.add(optionsVolume); // Add label Volume

        opTable.add(slider).width(300f * scale).height(100f * scale).row(); // Add the slider to the table

        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle(opSkin.get(TextButton.TextButtonStyle.class));
        textButtonStyle.font.getData().setScale(scale); // Adjust the font scale
        TextButton quit = new TextButton("Back", opSkin, "default");
        quit.setColor(BUTTON_QUIT_COLOR);
        opTable.add(quit).colspan(4).padTop(100 * scale).width(150 * scale).height(50 * scale).row();

        // Set visibility to false to start hidden
        opTable.setVisible(false);
        optionsTitle.setVisible(false);
        optionsVolume.setVisible(false);
        slider.setVisible(false);
        quit.setVisible(false);

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

    private Label createTitle() {
        Label menuTitle = new Label(OPTIONS_TITLE_TEXT, opSkin);
        menuTitle.setColor(TITLE_COLOR);
        return menuTitle;
    }



    private void randomSound() {
        Random rnd = new Random();
        int rndSoundNo = rnd.nextInt(4);

        switch (rndSoundNo) {
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

    public void hideEverything() {
        for (Actor actor : opTable.getChildren()) {
            actor.setVisible(false);
        }
        opTable.setVisible(false); // Optionally hide the entire table
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

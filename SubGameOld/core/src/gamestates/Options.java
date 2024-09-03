package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.danielr.subgame.SubGame;
import utilz.Constants;
import utilz.SoundManager;

import java.util.Random;

public class Options extends State {
    AudioDevice audioDevice = Gdx.audio.newAudioDevice(44100,true);
    private Stage stage;
    private TextButton volumeUp;
    private TextButton volumeDown;
    private Slider slider;
    private TextButton quit;
    private ProgressBar displayVolume;
    private Skin skin;
    private SoundManager soundManager;
    private float volume;

    public Options(SubGame subGame) {
        super(subGame);
        this.soundManager = SoundManager.getInstance(subGame);
        create();
    }

    private void create() {
        skin = new Skin(Gdx.files.internal("glassyui/glassy-ui.json"));
        stage = new Stage();

        //title
        Label title = new Label("Options", skin);
        title.setColor(Color.WHITE);
        title.setFontScale(3);
        title.setPosition((Constants.Game.WORLD_WIDTH / 2f) - 150, Constants.Game.WORLD_HEIGHT * 0.9f);

        Label titleVolume = new Label("Volume", skin);
        titleVolume.setColor(Color.WHITE);
        titleVolume.setFontScale(3);
        titleVolume.setPosition(10 , Constants.Game.WORLD_HEIGHT * 0.8f);

        // slider
        float minValue = 0.0f;
        float maxValue = 1.0f;
        float stepSize = 0.01f;
        Slider.SliderStyle sliderStyle = skin.get("default-horizontal", Slider.SliderStyle.class);
        slider = new Slider(minValue, maxValue, stepSize, false, sliderStyle);
        slider.setBounds(Constants.Game.WORLD_WIDTH/2 - 265, Constants.Game.WORLD_HEIGHT * 0.78f, 250, 40);

//        // buttons
//
//        volumeUp = new TextButton("+", skin, "small");
//        volumeUp.setPosition(WORLD_WIDTH/2f - 264, WORLD_HEIGHT * 0.7f);
//        volumeDown = new TextButton("-", skin, "small");
//        volumeDown.setPosition(WORLD_WIDTH/2f - 132 , WORLD_HEIGHT * 0.7f);
//        displayVolume = new ProgressBar(0f, 1f, 0.1f, false, skin);
//        displayVolume.setWidth(262);
//        displayVolume.setPosition(WORLD_WIDTH/2 - 265, WORLD_HEIGHT * 0.8f);

        quit = new TextButton("Back", skin, "small");
        quit.setPosition(Constants.Game.WORLD_WIDTH - 150f, Constants.Game.WORLD_HEIGHT * 0.1f);

        stage.addActor(title);
        stage.addActor(titleVolume);
        stage.addActor(slider);
//        stage.addActor(volumeUp);
//        stage.addActor(volumeDown);
//        stage.addActor(displayVolume);
        stage.addActor(quit);

//        displayVolume.setValue(getSubGame().getVolume());
//        slider.setValue(getSubGame().getVolume());

        slider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                if (volume != slider.getValue()) {
                    volume =slider.getValue();
                    audioDevice.setVolume(volume);
//                    subGame.setVolume(volume);
//                    soundManager.playTorpedoHit();
                    randomSound();
//                    System.out.println(getSubGame().getVolume());
                }
            }
        });

//        volumeUp.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                    float volume = subGame.getVolume();
//                    if (volume < 1.0f) {
//                        volume += 0.1f;
//                        audioDevice.setVolume(volume);
//                        subGame.setVolume(volume);
//                        System.out.println(getSubGame().getVolume());
//                        displayVolume.setValue(getSubGame().getVolume());
//                        slider.setValue(getSubGame().getVolume());
//                    }
//            }
//        });
//
//        volumeDown.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                float volume = subGame.getVolume();
//                if (volume > 0.0f) {
//                    volume -= 0.1f;
//                    if (volume < 0.0f) {
//                        volume = 0.0f;
//                    }
//                    audioDevice.setVolume(volume);
//                    subGame.setVolume(volume);
//                    System.out.println(getSubGame().getVolume());
//                    displayVolume.setValue(getSubGame().getVolume());
//                    slider.setValue(getSubGame().getVolume());
//                }
//            }
//        });

        quit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getSubGame().getPlaying().getEnemyManager().resume();
                setGameState(Gamestate.MENU);
            }
        });

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        stage.act(delta);
        stage.draw();

        Gdx.input.setInputProcessor(stage);
    }

    public void exit() {
        soundManager.exit();
        audioDevice.dispose();
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
        this.slider.setValue(volume);
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

}

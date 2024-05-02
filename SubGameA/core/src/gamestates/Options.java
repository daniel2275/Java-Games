package gamestates;

import UI.OptionsStageManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.sub.SubGame;
import utilities.Constants;

public class Options implements Screen {
    private Stage opStage;
    private OptionsStageManager optionsStageManager;

    public Options(SubGame subGame) {
        this.optionsStageManager = new OptionsStageManager(subGame);
        opStage =  optionsStageManager.build();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(opStage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Constants.UIConstants.BACKGROUND_COLOR);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        opStage.act(delta);
        opStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        updateViewport(width,height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        optionsStageManager.hide();
    }

    @Override
    public void dispose() {
        optionsStageManager.dispose();
    }

    public void updateViewport(int width, int height) {
        opStage.getViewport().update(width, height, true);
    }

    public float getVolume() {
        return optionsStageManager.getVolume();
    }

    public void setVolume(float volume) {
        optionsStageManager.setVolume(volume);
    }

}

//    public Options(SubGame subGame) {
//        super(subGame);
//        this.soundManager = SoundManager.getInstance(subGame);
//        create();
//    }
//
//    private void create() {
//        skin = new Skin(Gdx.files.internal("glassyui/glassy-ui.json"));
//        stage = new Stage();
//
//        //title
//        Label title = new Label("Options", skin);
//        title.setColor(Color.WHITE);
//        title.setFontScale(3);
//        title.setPosition((WORLD_WIDTH / 2f) - 150, WORLD_HEIGHT * 0.9f);
//
//        Label titleVolume = new Label("Volume", skin);
//        titleVolume.setColor(Color.WHITE);
//        titleVolume.setFontScale(3);
//        titleVolume.setPosition(10 , WORLD_HEIGHT * 0.8f);
//
//        // slider
//        float minValue = 0.0f;
//        float maxValue = 1.0f;
//        float stepSize = 0.01f;
//        Slider.SliderStyle sliderStyle = skin.get("default-horizontal", Slider.SliderStyle.class);
//        slider = new Slider(minValue, maxValue, stepSize, false, sliderStyle);
//        slider.setBounds(WORLD_WIDTH/2 - 265, WORLD_HEIGHT * 0.78f, 250, 40);
//
////        // buttons
////
////        volumeUp = new TextButton("+", skin, "small");
////        volumeUp.setPosition(WORLD_WIDTH/2f - 264, WORLD_HEIGHT * 0.7f);
////        volumeDown = new TextButton("-", skin, "small");
////        volumeDown.setPosition(WORLD_WIDTH/2f - 132 , WORLD_HEIGHT * 0.7f);
////        displayVolume = new ProgressBar(0f, 1f, 0.1f, false, skin);
////        displayVolume.setWidth(262);
////        displayVolume.setPosition(WORLD_WIDTH/2 - 265, WORLD_HEIGHT * 0.8f);
//
//        quit = new TextButton("Back", skin, "small");
//        quit.setPosition(WORLD_WIDTH - 150f, WORLD_HEIGHT * 0.1f);
//
//        stage.addActor(title);
//        stage.addActor(titleVolume);
//        stage.addActor(slider);
////        stage.addActor(volumeUp);
////        stage.addActor(volumeDown);
////        stage.addActor(displayVolume);
//        stage.addActor(quit);
//
////        displayVolume.setValue(getSubGame().getVolume());
////        slider.setValue(getSubGame().getVolume());
//
//        slider.addListener(new ChangeListener() {
//            @Override
//            public void changed(ChangeEvent event, Actor actor) {
//
//                if (volume != slider.getValue()) {
//                    volume =slider.getValue();
//                    audioDevice.setVolume(volume);
////                    subGame.setVolume(volume);
////                    soundManager.playTorpedoHit();
//                    randomSound();
////                    System.out.println(getSubGame().getVolume());
//                }
//            }
//        });
//
////        volumeUp.addListener(new ClickListener() {
////            @Override
////            public void clicked(InputEvent event, float x, float y) {
////                    float volume = subGame.getVolume();
////                    if (volume < 1.0f) {
////                        volume += 0.1f;
////                        audioDevice.setVolume(volume);
////                        subGame.setVolume(volume);
////                        System.out.println(getSubGame().getVolume());
////                        displayVolume.setValue(getSubGame().getVolume());
////                        slider.setValue(getSubGame().getVolume());
////                    }
////            }
////        });
////
////        volumeDown.addListener(new ClickListener() {
////            @Override
////            public void clicked(InputEvent event, float x, float y) {
////                float volume = subGame.getVolume();
////                if (volume > 0.0f) {
////                    volume -= 0.1f;
////                    if (volume < 0.0f) {
////                        volume = 0.0f;
////                    }
////                    audioDevice.setVolume(volume);
////                    subGame.setVolume(volume);
////                    System.out.println(getSubGame().getVolume());
////                    displayVolume.setValue(getSubGame().getVolume());
////                    slider.setValue(getSubGame().getVolume());
////                }
////            }
////        });
//
//        quit.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                getSubGame().gamePlayScreen().getEnemyManager().resume();
//                setGameState(Gamestate.MENU);
//            }
//        });
//
//    }
//
//    @Override
//    public void show() {
//
//    }
//
//    public void render(float delta) {
//        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//        stage.act(delta);
//        stage.draw();
//
//        Gdx.input.setInputProcessor(stage);
//    }
//
//    @Override
//    public void resize(int width, int height) {
//
//    }
//
//    @Override
//    public void pause() {
//
//    }
//
//    @Override
//    public void resume() {
//
//    }
//
//    @Override
//    public void hide() {
//
//    }
//
//    @Override
//    public void dispose() {
//
//    }
//
//    public void exit() {
//        soundManager.exit();
//        audioDevice.dispose();
//    }
//

//
//    private void randomSound() {
//        Random rnd = new Random();
//        int rndSoundNo = rnd.nextInt(4);
//
//        switch ( rndSoundNo ) {
//            case 0:
//                soundManager.playDepthChargeFar();
//                break;
//            case 1:
//                soundManager.playDepthChargeHit();
//                break;
//            case 2:
//                soundManager.playTorpedoHit();
//                break;
//            case 3:
//                soundManager.playExplode();
//                break;
//            default:
//                throw new IllegalStateException("Unexpected value: " + rndSoundNo);
//        }
//    }
//
//}

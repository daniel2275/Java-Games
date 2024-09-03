package UI.options;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.daniel2275.subgame.SubGame;
import utilities.Constants;

public class Options implements Screen {
    private Stage opStage;
    private OptionsStageManager optionsStageManager;

    public Options(SubGame subGame) {
        this.optionsStageManager = new OptionsStageManager(subGame);
        opStage = optionsStageManager.build();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(opStage);
        opStage.act();
        getOptionsStageManager().animateButtonsOnShow();
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
        updateViewport(width, height);
    }

    @Override
    public void pause() {
        // Handle the pause state if needed
    }

    @Override
    public void resume() {
        // Handle the resume state if needed
    }

    @Override
    public void hide() {
        optionsStageManager.hideEverything();
        optionsStageManager.hide();
    }

    @Override
    public void dispose() {
        optionsStageManager.dispose();
    }

    private void updateViewport(int width, int height) {
        opStage.getViewport().update(width, height, true);
    }

    public float getVolume() {
        return optionsStageManager.getVolume();
    }

    public void setVolume(float volume) {
        optionsStageManager.setVolume(volume);
    }

    public OptionsStageManager getOptionsStageManager() {
        return optionsStageManager;
    }
}

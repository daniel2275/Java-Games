package UI.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.daniel2275.subgame.SubGame;
import utilities.Settings;

public class MapScreen implements Screen {
    private Stage uiStage;
    private MapStageManager mapStage;
    private SubGame subGame;

    public MapScreen(SubGame subGame) {
        this.subGame = subGame;
        this.mapStage = new MapStageManager(subGame);
        uiStage = mapStage.build();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(uiStage);

        uiStage.act();
       // getMapStage().animateButtonsOnShow();
    }

    @Override
    public void render(float delta) {
        //   Gdx.gl.glClearColor(0f, 105f / 255f, 148f / 255f, 1f);
        ScreenUtils.clear(Settings.UIConstants.BACKGROUND_COLOR);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        uiStage.act(delta);
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        updateViewport(width, height);
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

    public void updateViewport(int width, int height) {
        uiStage.getViewport().update(width, height, true);
    }

    public MapStageManager getMapStage(){
        return mapStage;
    }
}

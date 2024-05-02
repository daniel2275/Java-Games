package gamestates;

import UI.MenuStageManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.sub.SubGame;
import utilities.Constants.UIConstants;

public class MenuRenderer implements Screen {
    private Stage uiStage;
    private MenuStageManager menuStage;
    private SubGame subGame;

    public MenuRenderer(SubGame subGame) {
        this.subGame = subGame;
        this.menuStage = new MenuStageManager(subGame);
        uiStage = menuStage.build();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(uiStage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(UIConstants.BACKGROUND_COLOR);
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

    public MenuStageManager getMenuStage(){
        return menuStage;
    }

}

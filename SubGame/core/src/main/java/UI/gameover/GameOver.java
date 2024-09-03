package UI.gameover;

import UI.game.GameScreen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class GameOver implements Screen {
    private GameScreen gameScreen;
    private OrthographicCamera camera;
    private SpriteBatch batch;
    private BitmapFont font;
    private GlyphLayout layout;
    private Texture background;

    public GameOver(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch = new SpriteBatch();
        background = new Texture(Gdx.files.internal("gameover.png"));
        font = new BitmapFont(Gdx.files.internal("clean-crispy/skin/font-export.fnt"), Gdx.files.internal("clean-crispy/raw/font-export.png"), false);
        layout = new GlyphLayout();
    }

    public void show() {
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                gameScreen.getPlayer().setPlayerHealth(100);
                gameScreen.getPlayer().getPlayerActor().setCurrentHealth(100);
                gameScreen.getUpgradeStore().saveGame();
                gameScreen.reset();

                gameScreen.getUpgradeStore().gameOver();

                gameScreen.getSubGame().setScreen(gameScreen.getSubGame().getMenuRenderer());
                return true;
            }
        });
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(camera.combined);
        batch.begin();

        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        font.getData().setScale(2);
        layout.setText(font, "Game Over!");
        font.draw(batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2, (Gdx.graphics.getHeight() + layout.height) / 2);

        layout.setText(font, "Tap to return to Main Menu");
        font.draw(batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2, (Gdx.graphics.getHeight() - layout.height) / 2);

        batch.end();
        show();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}

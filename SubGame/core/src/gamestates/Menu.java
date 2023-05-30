package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.danielr.subgame.SubGame;

import static com.danielr.subgame.SubGame.upgradeStore;
import static utilz.Constants.Game.WORLD_HEIGHT;
import static utilz.Constants.Game.WORLD_WIDTH;

public class Menu extends State {

    private Stage stage;
    private TextButton menuPlay;
    private TextButton menuReset;
    private TextButton menuOption;
    private TextButton menuQuit;
    private Skin skin;

    public Menu(SubGame subGame) {
        super(subGame);
        create();
    }

    public void update() {
        float stateTime = Gdx.graphics.getDeltaTime();
        render(stateTime);
    }

    public void create() {

        skin = new Skin(Gdx.files.internal("glassyui/glassy-ui.json"));
        stage = new Stage();

        Label menuTitle = new Label("Menu", skin);
        menuTitle.setColor(Color.WHITE);
        menuTitle.setFontScale(3);

        menuTitle.setPosition((WORLD_WIDTH/2f) - 40, WORLD_HEIGHT * 0.9f);

        menuPlay = new TextButton(" Play ", skin);
        menuPlay.setPosition(10, WORLD_HEIGHT * 0.7f);
        menuReset = new TextButton(" Reset ", skin);
        menuReset.setPosition(10,WORLD_HEIGHT * 0.5f);
        menuOption = new TextButton(" Options ", skin);
        menuOption.setPosition(10,WORLD_HEIGHT * 0.3f);
        menuQuit = new TextButton(" Quit ", skin);
        menuQuit.setPosition(10,WORLD_HEIGHT * 0.1f);

        stage.addActor(menuTitle);
        stage.addActor(menuPlay);
        stage.addActor(menuReset);
        stage.addActor(menuOption);
        stage.addActor(menuQuit);

        menuPlay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                getSubGame().getPlaying().getEnemyManager().resume();
                setGameState(Gamestate.PLAYING);
            }
        });

        menuReset.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                subGame.getPlaying().reset();
            }
        });

        menuOption.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setGameState(Gamestate.OPTIONS);
            }
        });

        menuQuit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                upgradeStore.saveGame();
                Gdx.app.exit();
            }
        });

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

//        menuPlay.setChecked(false);
        stage.act(delta);
        stage.draw();

        Gdx.input.setInputProcessor(stage);
    }


}



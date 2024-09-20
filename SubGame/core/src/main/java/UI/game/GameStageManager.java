package UI.game;

import Components.CrossHairActor;
import Components.BackgroundActor;
import Components.ParallaxLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import io.github.daniel2275.subgame.SubGame;

import static io.github.daniel2275.subgame.SubGame.pause;
import static utilities.Constants.Game.SKY_SIZE;
import static utilities.Constants.Game.VIRTUAL_HEIGHT;
import static utilities.Constants.UIConstants.FONT_GAME_SIZE;
import static utilities.Constants.UIConstants.SKIN_FILE_PATH;
import static utilities.LoadSave.boatAnimation;

public class GameStageManager {
    private Stage gmStage;
    private Skin gameSkin;
    private Label scoreLabel;
    private Label scoreLabel1;
    private Label scoreLabel2;
    private Label scoreLabel3;
    private BackgroundActor underSea;
    private BackgroundActor skyLine;
    private Sprite pauseSprite;
    private SubGame subGame;
    private GameScreen gameScreen;
    private Table tile;
    private Button upgradesButton;
    private Button pauseButton;
    private Button menuButton;
    private Actor androidCrossHair;

    public GameStageManager(GameScreen gameScreen) {
        // Initialize the stage with the same viewport as GameScreen
        this.gameScreen = gameScreen;

        gmStage = new Stage(gameScreen.viewport);

        // Ensure the stage's viewport matches GameScreen's viewport
        gmStage.getViewport().update((int) gameScreen.viewport.getWorldWidth(), (int) gameScreen.viewport.getWorldHeight(), true);
    }

    public Stage build() {
        loadSkin();
        createUIElements();
        return gmStage;
    }


    private void loadSkin() {
        try {
            gameSkin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));

            // Load the custom font
            FontManager.loadFont(16);

            // Add the custom font to the skin and replace the default font
            gameSkin.add("default-font", FontManager.getFont());

            // Modify existing styles to use the new font
            Label.LabelStyle labelStyle = gameSkin.get(Label.LabelStyle.class);
            labelStyle.font = FontManager.getFont();
            gameSkin.add("default", labelStyle);

            TextButton.TextButtonStyle buttonStyle = gameSkin.get(TextButton.TextButtonStyle.class);
            buttonStyle.font = FontManager.getFont();
            gameSkin.add("default", buttonStyle);

        } catch (Exception e) {
            Gdx.app.error("MainMenu", "Error loading skin file", e);
        }
    }

    private void createUIElements() {
        setupButtons();
        setupAnimations();
        setupBackground();
        setupScoreboard();
        setupParallaxLayers();

        gmStage.addActor(underSea);
        gmStage.addActor(skyLine);
        gmStage.addActor(upgradesButton);
        gmStage.addActor(pauseButton);
        gmStage.addActor(menuButton);
        gmStage.addActor(tile);
        gmStage.addActor(androidCrossHair);
        androidCrossHair.setVisible(false);
    }

    private void setupButtons() {
        upgradesButton = createTextButton("Upgrades", 712, 445);
        pauseButton = createTextButton("Pause", 651, 445);
        menuButton = createTextButton("Menu", 595, 445);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.getUpgradeStore().saveGame();
                gameScreen.pause();
                gameScreen.getSubGame().setScreen(gameScreen.getSubGame().getMenuRenderer());
            }
        });

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pause = !pause;
                if (pause) {
                    gameScreen.pause();
                } else {
                    gameScreen.resume();
                }
            }
        });

        upgradesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.pause();
                gameScreen.getUpgradeStore().saveGame();
                gameScreen.getSubGame().setScreen(gameScreen.getUpgradeStore());
            }
        });
    }

    private TextButton createTextButton(String text, float x, float y) {
        TextButton button = new TextButton(text, gameSkin);
        button.getLabel().setFontScale(FONT_GAME_SIZE);
        button.getLabel().setColor(0f, 0f, 0f, 0.5f);
        button.setPosition(x, y);
        Color color = button.getColor();
        button.setColor(color.r, color.g, color.b, 0.5f);
        return button;
    }

    private void setupAnimations() {
        TextureAtlas seaAtlas = new TextureAtlas(Gdx.files.internal("seanim/seanim.atlas"));
        Animation<TextureRegion> seaAnimation = createAnimation(seaAtlas, "Wateranim", 4, 0.6f);

        TextureAtlas crossHairAtlas = new TextureAtlas(Gdx.files.internal("androidcrosshair/acrosshair.atlas"));
        Animation<TextureRegion> crossHairAnimation = createAnimation(crossHairAtlas, "acrosshair", 5, 0.1f);

        Texture skyBackgroundAtlas = new Texture("skyline.png");
        Animation<TextureRegion> skyAnimation = boatAnimation(0, 1, new TextureRegion[][]{{new TextureRegion(skyBackgroundAtlas, 770, 290)}}, 0.2f);

        androidCrossHair =  new CrossHairActor(crossHairAnimation);
        skyLine = new BackgroundActor(skyAnimation, 0, VIRTUAL_HEIGHT - SKY_SIZE + 15);
        underSea = new BackgroundActor(seaAnimation, 0, -SKY_SIZE + 45);
    }

    private Animation<TextureRegion> createAnimation(TextureAtlas atlas, String regionName, int frames, float frameDuration) {
        Array<TextureAtlas.AtlasRegion> regions = new Array<>();
        for (int i = 1; i <= frames; i++) {
            TextureAtlas.AtlasRegion region = atlas.findRegion(regionName + i);
            if (region != null) {
                regions.add(region);
            }
        }
        return new Animation<>(frameDuration, regions, Animation.PlayMode.LOOP);
    }

    private void setupBackground() {
        pauseSprite = new Sprite(new Texture(Gdx.files.internal("paused.png")));
        pauseSprite.setPosition(Gdx.graphics.getWidth() / 2f - 100f, 500);
        pauseSprite.setSize(200f, 80f);
    }

    private void setupScoreboard() {
        tile = new Table(gameSkin);
        tile.setSize(224, 70);
        tile.setPosition(10, 410);
        Color color = tile.getColor();
        tile.setColor(color.r, color.g, color.b, 0.5f);
        tile.setBackground("tooltip");

        scoreLabel = createLabel("Enemies remaining:");
        scoreLabel1 = createLabel("Score:");
        scoreLabel2 = createLabel("Level:");
        scoreLabel3 = createLabel("Health:");

        tile.add(scoreLabel).row();
        tile.add(scoreLabel1).row();
        tile.add(scoreLabel2).row();
        tile.add(scoreLabel3).row();
    }

    private Label createLabel(String text) {
        Label label = new Label(text, gameSkin);
        label.setColor(Color.BLACK);
        return label;
    }

    private void setupParallaxLayers() {
        ParallaxLayer layer1 = createParallaxLayer("clouds.png", 8, 150, 415);
        ParallaxLayer layer2 = createParallaxLayer("clouds2.png", 11, 500, 415);
        ParallaxLayer layer3 = createParallaxLayer("clouds3.png", 15, 800, 400);

        gmStage.addActor(layer1);
        gmStage.addActor(layer2);
        gmStage.addActor(layer3);
    }

    private ParallaxLayer createParallaxLayer(String texturePath, float speed, float initialX, float initialY) {
        Texture texture = new Texture(Gdx.files.internal(texturePath));
        return new ParallaxLayer(texture, speed, initialX, initialY);
    }

    public void dispose() {
        if (gameSkin != null) {
            gameSkin.dispose();
        }
        if (gmStage != null) {
            gmStage.dispose();
        }
    }


    public Stage getStage() {
        return gmStage;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getScoreLabel1() {
        return scoreLabel1;
    }

    public Label getScoreLabel2() {
        return scoreLabel2;
    }

    public Label getScoreLabel3() {
        return scoreLabel3;
    }

    public BackgroundActor getSkyLine() {
        return skyLine;
    }

    public BackgroundActor getUndersea() {
        return underSea;
    }

    public Actor getAndroidCrossHairActor() {
        return androidCrossHair;
    }
}





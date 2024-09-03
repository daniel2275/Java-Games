package UI.game;

import Components.CustomActor;
import Components.ParallaxLayer;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
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
    private CustomActor underSea;
    private CustomActor skyLine;
    private Sprite pauseSprite;
    private SubGame subGame;
    private GameScreen gameScreen;
    private Animation<TextureRegion> seaAnimation;
    private Animation<TextureRegion> skyAnimation;
    private TextureAtlas atlas;

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
        //set up UI display
        TextButton upgradesButton = new TextButton("Upgrades", gameSkin);
        TextButton pauseButton = new TextButton("Pause", gameSkin);
        TextButton menuButton = new TextButton("Menu", gameSkin);
        upgradesButton.getLabel().setFontScale(FONT_GAME_SIZE);
        menuButton.getLabel().setFontScale(FONT_GAME_SIZE);
        pauseButton.getLabel().setFontScale(FONT_GAME_SIZE);

        menuButton.setPosition(595,445);
        pauseButton.setPosition(651,445);
        upgradesButton.setPosition(712,445);

        Color color = menuButton.getColor();
        menuButton.setColor(color.r, color.g, color.b, 0.5f);
        pauseButton.setColor(color.r, color.g, color.b, 0.5f);
        upgradesButton.setColor(color.r, color.g, color.b, 0.5f);

        menuButton.addListener(new ClickListener(){
           @Override
           public void clicked(InputEvent event, float x, float y) {
               gameScreen.getUpgradeStore().saveGame();
               gameScreen.pause();
               gameScreen.getSubGame().setScreen(gameScreen.getSubGame().getMenuRenderer());
           }
        });

        pauseButton.addListener(new ClickListener(){
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

        upgradesButton.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y) {
                gameScreen.pause();
                gameScreen.getUpgradeStore().saveGame();
                gameScreen.getSubGame().setScreen(gameScreen.getUpgradeStore());
            }
        });


        atlas = new TextureAtlas(Gdx.files.internal("seanim/seanim.atlas"));

        Array<TextureAtlas.AtlasRegion> regions = new Array<>();
        for (int i = 1; i <= 4; i++) {
            String regionName = "Wateranim" + i;//String.format("%04d", i);
            TextureAtlas.AtlasRegion region = atlas.findRegion(regionName);
            if (region != null) {
                regions.add(region);
            }
        }

        seaAnimation = new Animation<>(0.6f, regions, Animation.PlayMode.LOOP);

        TextureRegion[][] skyBackgroundSprites = new TextureRegion[1][1];
        Texture skyBackgroundAtlas = new Texture("skyline.png");
        skyBackgroundSprites[0][0] = new TextureRegion(skyBackgroundAtlas, 770, 290);

        skyAnimation = boatAnimation(0, 1,skyBackgroundSprites, 0.2f);

        skyLine = new CustomActor(skyAnimation, 0, VIRTUAL_HEIGHT - SKY_SIZE + 15);
        underSea = new CustomActor(seaAnimation, 0, -SKY_SIZE + 45);

        Table tile = new Table(gameSkin);
        tile.setSize(224,70);
        tile.setPosition(10,410);
        color = tile.getColor();
        tile.setColor(color.r, color.g, color.b, 0.5f);

        tile.setBackground("tooltip");

        scoreLabel = new Label("Enemies remaining:", gameSkin);
        scoreLabel1 = new Label("Score:", gameSkin);
        scoreLabel2 = new Label("Level:", gameSkin);
        scoreLabel3 = new Label("Health:", gameSkin);
        scoreLabel.setColor(Color.BLACK);
        scoreLabel1.setColor(Color.BLACK);
        scoreLabel2.setColor(Color.BLACK);
        scoreLabel3.setColor(Color.BLACK);

        tile.add(scoreLabel).row();
        tile.add(scoreLabel1).row();
        tile.add(scoreLabel2).row();
        tile.add(scoreLabel3).row();

        //background images
        pauseSprite = new Sprite(new Texture(Gdx.files.internal("paused.png")));
        pauseSprite.setPosition((float) Gdx.graphics.getWidth() / 2 - 100f, 500);
        pauseSprite.setSize(200f, 80f);

        gmStage.addActor(underSea);
        gmStage.addActor(skyLine);

        Texture texture1 = new Texture(Gdx.files.internal("clouds.png"));
        Texture texture2 = new Texture(Gdx.files.internal("clouds2.png"));
        Texture texture3 = new Texture(Gdx.files.internal("clouds3.png"));
        float speed1 = 8;
        float initialX1 = 150;
        float initialY1 = 415;

        float speed2 = 11;
        float initialX2 = 500;
        float initialY2 = 415;

        float speed3 = 15;
        float initialX3 = 800;
        float initialY3 = 400;

        ParallaxLayer layer1 = new ParallaxLayer(texture1, speed1, initialX1, initialY1);
        ParallaxLayer layer2 = new ParallaxLayer(texture2, speed2, initialX2, initialY2);
        ParallaxLayer layer3 = new ParallaxLayer(texture3, speed3, initialX3, initialY3);

        gmStage.addActor(layer1);
        gmStage.addActor(layer2);
        gmStage.addActor(layer3);

        gmStage.addActor(upgradesButton);
        gmStage.addActor(pauseButton);
        gmStage.addActor(menuButton);

        gmStage.addActor(tile);
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

    public CustomActor getSkyLine() {
        return skyLine;
    }


    public CustomActor getUndersea() {
        return underSea;
    }
}





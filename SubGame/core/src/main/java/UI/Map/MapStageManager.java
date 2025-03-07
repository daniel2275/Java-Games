////package UI.Map;
////
////import UI.game.FontManager;
////import com.badlogic.gdx.Gdx;
////import com.badlogic.gdx.graphics.Color;
////import com.badlogic.gdx.graphics.Texture;
////import com.badlogic.gdx.graphics.g2d.Batch;
////import com.badlogic.gdx.scenes.scene2d.Actor;
////import com.badlogic.gdx.scenes.scene2d.InputEvent;
////import com.badlogic.gdx.scenes.scene2d.Stage;
////import com.badlogic.gdx.scenes.scene2d.ui.Label;
////import com.badlogic.gdx.scenes.scene2d.ui.Skin;
////import com.badlogic.gdx.scenes.scene2d.ui.Table;
////import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
////import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
////import com.badlogic.gdx.utils.viewport.ScreenViewport;
////import io.github.daniel2275.subgame.SubGame;
////import utilities.Settings;
////
////import static utilities.Settings.UIConstants.*;
////
////public class MapStageManager {
////        private Stage mapStage;
////        private Table uiTable;
////        private Skin uiSkin;
////        private SubGame subGame;
////        private boolean alternate = false;
////        private Actor mapBackground;
////
////        private Texture texture;
////
////        public MapStageManager(SubGame subGame) {
////            mapStage = new Stage(new ScreenViewport());
////            this.subGame = subGame;
////            loadTextures();
////        }
////
////        private void loadTextures() {
////            texture = new Texture(Gdx.files.internal("map.png"), true);
////            texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
////        }
////
////        public Stage build() {
////            uiTable = new Table();
////            loadSkin();
////            createUIElements();
////            uiTable.setFillParent(true); // Makes the table take the whole stage
////            mapStage.addActor(mapBackground);
////            mapStage.addActor(uiTable);
////            return mapStage;
////        }
////
////        private void loadSkin() {
////            try {
////                uiSkin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));
////
////                // Load the custom font
////                FontManager.loadFont(24);
////
////                // Add the custom font to the skin and replace the default font
////                uiSkin.add("default-font", FontManager.getFont());
////
////                // Modify existing styles to use the new font
////                Label.LabelStyle labelStyle = uiSkin.get(Label.LabelStyle.class);
////                labelStyle.font = FontManager.getFont();
////                uiSkin.add("default", labelStyle);
////
////                TextButton.TextButtonStyle buttonStyle = uiSkin.get(TextButton.TextButtonStyle.class);
////                buttonStyle.font = FontManager.getFont();
////                uiSkin.add("default", buttonStyle);
////
////                TextButton.TextButtonStyle arcadeStyle = uiSkin.get("arcade", TextButton.TextButtonStyle.class);
////                arcadeStyle.font = FontManager.getFont();
////                uiSkin.add("default", arcadeStyle);
////
////            } catch (Exception e) {
////                Gdx.app.error("Map", "Error loading skin file", e);
////            }
////        }
////
////        public void hide() {
////            // Clear the input processor when the stage is hidden
////            Gdx.input.setInputProcessor(null);
////        }
////
////    private void createUIElements() {
////        mapBackground = new Actor() {
////            @Override
////            public void draw(Batch batch, float parentAlpha) {
////                super.draw(batch, parentAlpha);
////                batch.draw(texture, getX(), getY(), getWidth(), getHeight());
////            }
////        };
////
////        float originalWidth = texture.getWidth();
////        float originalHeight = texture.getHeight();
////
////        // Target dimensions
////        float targetWidth = 1280f;
////        float targetHeight = 720f;
////
////        float scaleFactor = Math.min(targetWidth / originalWidth, targetHeight / originalHeight);
////        mapBackground.setSize(originalWidth * scaleFactor, originalHeight * scaleFactor);
////
////        mapStage.addActor(mapBackground);
////
////        Label menuTitle = createMenuTitle();
////        menuTitle.setPosition(540, 650);
////        mapStage.addActor(menuTitle);
////
////        // Button setup
////        String[] buttonLabels = {
////            MAP_BUTTON_PLAY_TEXT,
////            MAP_BUTTON_OPTION_TEXT,
////            MAP_BUTTON_RESET_TEXT,
////            MAP_BUTTON_QUIT_TEXT
////        };
////
////        Color[] buttonColors = {
////            BUTTON_PLAY_COLOR,
////            BUTTON_OPTION_COLOR,
////            BUTTON_RESET_COLOR,
////            BUTTON_QUIT_COLOR
////        };
////
////        float[][] buttonPositions = {
////            {540, 500}, // Play Button
////            {540, 400}, // Options Button
////            {540, 300}, // Reset Button
////            {540, 200}  // Quit Button
////        };
////
////        setupButtons(buttonLabels, buttonColors, buttonPositions);
////    }
////
////    private void setupButtons(String[] buttonLabels, Color[] buttonColors, float[][] positions) {
////        for (int i = 0; i < buttonLabels.length; i++) {
////            String label = buttonLabels[i];
////            Color color = buttonColors[i];
////            float x = positions[i][0];
////            float y = positions[i][1];
////
////            TextButton button = createButton(label, color);
////            button.setPosition(x, y);
////            button.addListener(new ButtonClickListener(label));
////
////            mapStage.addActor(button);
////        }
////    }
////
////
////    private Label createMenuTitle() {
////            Label menuTitle = new Label(MENU_TITLE_TEXT, uiSkin);
////            menuTitle.setColor(TITLE_COLOR);
////            menuTitle.setFontScale(FONT_MENU_SIZE);
////            return menuTitle;
////        }
////
////        private TextButton createButton(String text, Color color) {
////            TextButton button = new TextButton(text, uiSkin);
////            if (text.equals(MENU_BUTTON_PLAY_TEXT)) {
////                TextButton.TextButtonStyle arcadeStyle = uiSkin.get("arcade", TextButton.TextButtonStyle.class);
////                button.setStyle(arcadeStyle);
////            }
////            button.getLabel().setFontScale(Settings.UIConstants.FONT_MENU_SIZE);
////            button.setColor(color);
////            return button;
////        }
////
////        private void setupButtons(Table uiTable, String[] buttonLabels, Color[] buttonColors) {
////            for (int i = 0; i < buttonLabels.length; i++) {
////                String label = buttonLabels[i];
////                Color color = buttonColors[i];
////                TextButton button = createButton(label, color);
////                // Add a listener specific to each button
////                button.addListener(new UI.Map.MapStageManager.ButtonClickListener(label));
////                //button.setVisible(false);
////
////                uiTable.add(button).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(BUTTON_PADDING).row();
////            }
////        }
////
////
////        public void dispose() {
////            if (uiSkin != null) {
////                uiSkin.dispose();
////            }
////            if (texture != null) {
////                texture.dispose();
////            }
////            if (mapStage != null) {
////                mapStage.dispose();
////            }
////        }
////
////        // Inner class to define a separate ClickListener for each button
////        private class ButtonClickListener extends ClickListener {
////            private String buttonLabel;
////
////            public ButtonClickListener(String buttonLabel) {
////                this.buttonLabel = buttonLabel;
////            }
////
////            @Override
////            public void clicked(InputEvent event, float x, float y) {
////                // Perform different actions based on the button clicked
////                switch ( buttonLabel ) {
////                    case MENU_BUTTON_PLAY_TEXT:
////                        // Handle play button click
////                        subGame.setScreen(subGame.gameScreen());
////                        if (subGame.gameScreen().isPaused()) {
////                            subGame.gameScreen().resume();
////                        }
////                        break;
////                    case MENU_BUTTON_OPTION_TEXT:
////                        subGame.setScreen(subGame.getOptions());
////                        break;
////                    case MENU_BUTTON_RESET_TEXT:
////                        subGame.gameScreen().reset();
////                        subGame.gameScreen().resetUpgrades();
////                        //subGame.getUpgradeStore().setDefaults();
////                        // Handle reset button click
////                        break;
////                    case MENU_BUTTON_QUIT_TEXT:
////                        subGame.gameScreen().getUpgradeStore().saveGame();
////                        Gdx.app.exit();
////                        break;
////                    default:
////                        // Handle default case
////                        break;
////                }
////            }
////        }
////
////    }
////
////
////
////
////
////
//
//
//package UI.Map;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.TextureAtlas;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.badlogic.gdx.scenes.scene2d.InputEvent;
//import com.badlogic.gdx.scenes.scene2d.InputListener;
//import com.badlogic.gdx.scenes.scene2d.Stage;
//import com.badlogic.gdx.scenes.scene2d.actions.Actions;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
//import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
//import com.badlogic.gdx.utils.Array;
//import com.badlogic.gdx.utils.viewport.ScreenViewport;
//import com.badlogic.gdx.utils.viewport.Viewport;
//import io.github.daniel2275.subgame.SubGame;
//
//public class MapStageManager {
//    private Stage mapStage;
//    private SubGame subGame;
//    private Texture mapTexture;
//    private Texture[] hoverAnimationFrames;
//    private float animationTime = 0;
//    private int currentFrame = 0;
//    private boolean hovering = false;
//
//    private TextureAtlas buttonAtlas;
//
//    public MapStageManager(SubGame subGame) {
//        mapStage = new Stage(new ScreenViewport());
//        this.subGame = subGame;
//        loadTextures();
//    }
//
//    private void loadTextures() {
//        mapTexture = new Texture(Gdx.files.internal("map.png"));
//        buttonAtlas = new TextureAtlas(Gdx.files.internal("animations/radanim.atlas"));
//    }
//
//    public Stage build() {
//        Viewport viewport = mapStage.getViewport();
//        float screenWidth = viewport.getWorldWidth();
//        float screenHeight = viewport.getWorldHeight();
//
//        Image mapBackground = new Image(mapTexture);
//        mapBackground.setSize(screenWidth, screenHeight);
//        mapBackground.setPosition(0, 0);
//        mapStage.addActor(mapBackground);
//
//        // Create and add buttons
//        createImageButton("animations/marker.png", screenWidth * 0.109f, screenHeight * 0.764f, "play");   // 140/1280, 550/720
//        createImageButton("animations/marker.png", screenWidth * 0.289f, screenHeight * 0.556f, "options"); // 370/1280, 400/720
//        createImageButton("animations/marker.png", screenWidth * 0.508f, screenHeight * 0.528f, "reset");   // 650/1280, 380/720
//        createImageButton("animations/marker.png", screenWidth * 0.625f, screenHeight * 0.208f, "quit");    // 800/1280, 150/720
//
//        return mapStage;
//    }
//
//
//    private void createImageButton(String buttonTexturePath,  float x, float y, final String action) {
//        final Texture buttonTexture = new Texture(Gdx.files.internal(buttonTexturePath));
//
//        final Image buttonImage = new Image(buttonTexture);
//        buttonImage.setPosition(x, y);
//        buttonImage.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float xx, float yy) {
//                handleButtonAction(action);
//            }
//
//            @Override
//            public boolean mouseMoved(InputEvent event, float xx, float yy) {
//                hovering = true;
//                return super.mouseMoved(event, xx, yy);
//            }
//
//            @Override
//            public void exit(InputEvent event, float xx, float yy, int pointer, Actor toActor) {
//                hovering = false;
//                buttonImage.setDrawable(new Image(buttonTexture).getDrawable());
//            }
//        });
//
//        mapStage.addActor(buttonImage);
//        animateHover(buttonImage, buttonAtlas);
//    }
//
//
//    private void animateHover(final Image button, final TextureAtlas atlas) {
//        final float frameDuration = 1f / 16f; // 16 FPS
//        final float[] animationTime = {0};
//        final int[] currentFrame = {0};
//        final boolean[] hovering = {false};
//
//        // Load animation frames from the atlas
//        Array<TextureAtlas.AtlasRegion> frames = new Array<>();
//        for (int i = 1; i <= 16; i++) {
//            frames.add(atlas.findRegion("radanim" + i));
//        }
//
//        button.addListener(new InputListener() {
//            @Override
//            public boolean mouseMoved(InputEvent event, float x, float y) {
//                hovering[0] = true;
//                return true;
//            }
//
//            @Override
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                hovering[0] = false;
//                // Reset to static button image (first frame)
//                //button.setDrawable(new TextureRegionDrawable(frames.first()));
//                Texture buttonTexture = new Texture(Gdx.files.internal("animations/marker.png"));
//                button.setDrawable(new TextureRegionDrawable(buttonTexture));
//
//            }
//        });
//
//        button.addAction(Actions.forever(Actions.run(() -> {
//            if (hovering[0]) {
//                animationTime[0] += Gdx.graphics.getDeltaTime();
//                if (animationTime[0] >= frameDuration) {
//                    animationTime[0] -= frameDuration;
//                    currentFrame[0] = (currentFrame[0] + 1) % frames.size;
//                    button.setDrawable(new TextureRegionDrawable(frames.get(currentFrame[0])));
//                }
//            }
//        })));
//    }
//
//
//
//
//
//    private void handleButtonAction(String action) {
//        switch (action) {
//            case "play":
//                subGame.setScreen(subGame.gameScreen());
//                if (subGame.gameScreen().isPaused()) {
//                    subGame.gameScreen().resume();
//                }
//                break;
//            case "options":
//                subGame.setScreen(subGame.getOptions());
//                break;
//            case "reset":
//                subGame.gameScreen().reset();
//                subGame.gameScreen().resetUpgrades();
//                break;
//            case "quit":
//                subGame.gameScreen().getUpgradeStore().saveGame();
//                Gdx.app.exit();
//                break;
//        }
//    }
//
//    public void dispose() {
//        mapTexture.dispose();
//        for (Texture texture : hoverAnimationFrames) {
//            texture.dispose();
//        }
//        mapStage.dispose();
//    }
//}


package UI.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.daniel2275.subgame.SubGame;

public class MapStageManager {
    private Stage mapStage;
    private SubGame subGame;
    private Texture mapTexture;
    private Texture buttonTexture;
    private TextureAtlas buttonAtlas;
    private float frameDuration = 1f / 16f; // 16 FPS for animation

    public MapStageManager(SubGame subGame) {
        mapStage = new Stage(new ScreenViewport());
        this.subGame = subGame;
        loadTextures();
    }

    private void loadTextures() {
        mapTexture = new Texture(Gdx.files.internal("map.png"), true);
        mapTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        buttonTexture = new Texture(Gdx.files.internal("animations/marker.png"), true);
        buttonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        buttonAtlas = new TextureAtlas(Gdx.files.internal("animations/radanim.atlas"));
    }

    public Stage build() {
        float screenWidth = mapStage.getViewport().getWorldWidth();
        float screenHeight = mapStage.getViewport().getWorldHeight();

        Image mapBackground = new Image(mapTexture);
        mapBackground.setSize(screenWidth, screenHeight);
        mapBackground.setPosition(0, 0);
        mapStage.addActor(mapBackground);

        // Create and add buttons with their actions
        createImageButton(screenWidth * 0.109f, screenHeight * 0.764f, "play");   // 140/1280, 550/720
        createImageButton(screenWidth * 0.289f, screenHeight * 0.556f, "options"); // 370/1280, 400/720
        createImageButton(screenWidth * 0.508f, screenHeight * 0.528f, "reset");   // 650/1280, 380/720
        createImageButton(screenWidth * 0.625f, screenHeight * 0.208f, "quit");    // 800/1280, 150/720

        return mapStage;
    }

    private void createImageButton(float x, float y, final String action) {
        final Image buttonImage = new Image(buttonTexture);
        buttonImage.setPosition(x, y);

        // Load animation frames from the atlas
        final Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        for (int i = 1; i <= 16; i++) {
            frames.add(buttonAtlas.findRegion("radanim" + i));
        }

        // Animation state
        final float[] animationTime = {0};
        final int[] currentFrame = {0};
        final boolean[] isActive = {false}; // Tracks hover or touch hold state

        // ClickListener for both mouse and touch events
        buttonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float xx, float yy) {
                // Trigger action only on release (touch up or mouse click)
                handleButtonAction(action);
            }

            @Override
            public boolean touchDown(InputEvent event, float xx, float yy, int pointer, int button) {
                // Start animation on touch down (Android)
                isActive[0] = true;
                return true; // Consume the event
            }

            //@Override
            public void touchUp(InputEvent event, float xx, float yy, int pointer, int button) {
                // Stop animation and reset to static image on touch up
                isActive[0] = false;
                buttonImage.setDrawable(new TextureRegionDrawable(buttonTexture));

                clicked(event, xx, yy);
                //super.touchUp(event, xx, yy, pointer, button); // Calls clicked()
            }

            @Override
            public void enter(InputEvent event, float xx, float yy, int pointer, Actor fromActor) {
                // Start animation on mouse hover (desktop)
                if (pointer == -1) { // -1 means mouse, not touch
                    isActive[0] = true;
                }
            }

            @Override
            public void exit(InputEvent event, float xx, float yy, int pointer, Actor toActor) {
                // Stop animation and reset on mouse exit (desktop)
                if (pointer == -1) { // Only reset for mouse, not touch
                    isActive[0] = false;
                    buttonImage.setDrawable(new TextureRegionDrawable(buttonTexture));
                }
            }
        });

        // Animation loop
        buttonImage.addAction(Actions.forever(Actions.run(() -> {
            if (isActive[0]) {
                animationTime[0] += Gdx.graphics.getDeltaTime();
                if (animationTime[0] >= frameDuration) {
                    animationTime[0] -= frameDuration;
                    currentFrame[0] = (currentFrame[0] + 1) % frames.size;
                    buttonImage.setDrawable(new TextureRegionDrawable(frames.get(currentFrame[0])));
                }
            }
        })));

        mapStage.addActor(buttonImage);
    }

    private void handleButtonAction(String action) {
        switch (action) {
            case "play":
                subGame.setScreen(subGame.gameScreen());
                if (subGame.gameScreen().isPaused()) {
                    subGame.gameScreen().resume();
                }
                break;
            case "options":
                subGame.setScreen(subGame.getOptions());
                break;
            case "reset":
                subGame.gameScreen().reset();
                subGame.gameScreen().resetUpgrades();
                break;
            case "quit":
                subGame.gameScreen().getUpgradeStore().saveGame();
                Gdx.app.exit();
                break;
        }
    }

    public void dispose() {
        if (mapTexture != null) mapTexture.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
        if (buttonAtlas != null) buttonAtlas.dispose();
        if (mapStage != null) mapStage.dispose();
    }
}

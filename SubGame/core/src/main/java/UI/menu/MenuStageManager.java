package UI.menu;

import UI.game.FontManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.daniel2275.subgame.SubGame;
import utilities.Settings;

import static utilities.Settings.UIConstants.*;

public class MenuStageManager {
    private Stage uiStage;
    private Table uiTable;
    private Skin uiSkin;
    private SubGame subGame;
    private boolean alternate = false;

    private Texture texture;

    public MenuStageManager(SubGame subGame) {
        uiStage = new Stage(new ScreenViewport());
        this.subGame = subGame;
        loadTextures();
    }

    private void loadTextures() {
        texture = new Texture(Gdx.files.internal("hugeship.png"), true);
        texture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
    }

    public Stage build() {
        uiTable = new Table();
        loadSkin();
        createUIElements();
        uiTable.setFillParent(true); // Makes the table take the whole stage
        uiStage.addActor(uiTable);
        return uiStage;
    }

    private void loadSkin() {
        try {
            uiSkin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));

            // Load the custom font
            FontManager.loadFont(24);

            // Add the custom font to the skin and replace the default font
            uiSkin.add("default-font", FontManager.getFont());

            // Modify existing styles to use the new font
            Label.LabelStyle labelStyle = uiSkin.get(Label.LabelStyle.class);
            labelStyle.font = FontManager.getFont();
            uiSkin.add("default", labelStyle);

            TextButton.TextButtonStyle buttonStyle = uiSkin.get(TextButton.TextButtonStyle.class);
            buttonStyle.font = FontManager.getFont();
            uiSkin.add("default", buttonStyle);

            TextButton.TextButtonStyle arcadeStyle = uiSkin.get("arcade", TextButton.TextButtonStyle.class);
            arcadeStyle.font = FontManager.getFont();
            uiSkin.add("default", arcadeStyle);

        } catch (Exception e) {
            Gdx.app.error("MainMenu", "Error loading skin file", e);
        }
    }

    public void hideEverything() {
        for (Actor subTitleActor : uiTable.getChildren()) {
            subTitleActor.setVisible(false);
        }
        uiTable.setVisible(false); // Optionally hide the entire table
    }

    public void hide() {
        // Clear the input processor when the stage is hidden
        Gdx.input.setInputProcessor(null);
    }

    private void createUIElements() {
//        uiTable.setFillParent(true); // Makes the table take the whole stage
//        uiStage.addActor(uiTable);


        Actor subTitle = new Actor() {
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                batch.draw(texture, getX(), getY(), getWidth(), getHeight());
            }
        };

        float originalWidth = texture.getWidth();
        float originalHeight = texture.getHeight();

        // Target dimensions
        float targetWidth = 128f;
        float targetHeight = 64f;

        float scaleFactor = Math.min(targetWidth / originalWidth, targetHeight / originalHeight);

        subTitle.setSize(originalWidth * scaleFactor, originalHeight * scaleFactor);

        // SubTexture
        uiTable.add(subTitle).padBottom(BUTTON_PADDING).row();

        Label menuTitle = createMenuTitle();
        uiTable.add(menuTitle).padBottom(BUTTON_PADDING).row();

        String[] buttonLabels = {
            MENU_BUTTON_PLAY_TEXT,
            MENU_BUTTON_OPTION_TEXT,
            MENU_BUTTON_RESET_TEXT,
            MENU_BUTTON_QUIT_TEXT
        };

        Color[] buttonColors = {
            BUTTON_PLAY_COLOR,
            BUTTON_OPTION_COLOR,
            BUTTON_RESET_COLOR,
            BUTTON_QUIT_COLOR
        };
        setupButtons(uiTable, buttonLabels, buttonColors);
    }

    private Label createMenuTitle() {
        Label menuTitle = new Label(MENU_TITLE_TEXT, uiSkin);
        menuTitle.setColor(TITLE_COLOR);
        menuTitle.setFontScale(FONT_MENU_SIZE);
        return menuTitle;
    }

    private TextButton createButton(String text, Color color) {
        TextButton button = new TextButton(text, uiSkin);
        if (text.equals(MENU_BUTTON_PLAY_TEXT)) {
            TextButton.TextButtonStyle arcadeStyle = uiSkin.get("arcade", TextButton.TextButtonStyle.class);
            button.setStyle(arcadeStyle);
        }
        button.getLabel().setFontScale(Settings.UIConstants.FONT_MENU_SIZE);
        button.setColor(color);
        return button;
    }

    private void setupButtons(Table uiTable, String[] buttonLabels, Color[] buttonColors) {
        for (int i = 0; i < buttonLabels.length; i++) {
            String label = buttonLabels[i];
            Color color = buttonColors[i];
            TextButton button = createButton(label, color);
            // Add a listener specific to each button
            button.addListener(new ButtonClickListener(label));
            button.setVisible(false);

            uiTable.add(button).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(BUTTON_PADDING).row();
        }
    }

    public void animateButtonsOnShow() {
        final float initialDelay = 0.1f; // Fixed delay
        final float delayIncrement = 0.1f; // Fixed delay increment
        final float animationDuration = 0.2f; // Fixed animation duration
        uiTable.setVisible(true);

        // Schedule layout force with a delay
        uiStage.addAction(Actions.sequence(
            Actions.delay(initialDelay), // Delay before forcing layout
            Actions.run(() -> {
                uiTable.layout(); // Force layout recalculation
                uiTable.setLayoutEnabled(false);

                float delay = 0f; // Initialize local variable for delay

                for (Actor actor : uiTable.getChildren()) {
                    //if (actor instanceof TextButton || actor instanceof Slider || actor instanceof Label || actor instanceof Actor) {

                        float finalX = actor.getX();
                        float finalY = actor.getY();

                        if (alternate) {
                            actor.setPosition(0, -actor.getHeight());
                        } else {
                            actor.setPosition(Gdx.graphics.getWidth(), -actor.getHeight());
                        }
                        alternate = !alternate;

                        actor.setVisible(true);

                        Action moveIn = Actions.moveTo(finalX, finalY, animationDuration, Interpolation.smooth);
                        actor.addAction(Actions.sequence(Actions.delay(delay), moveIn));

                        delay += delayIncrement;
                    }
              //  }

                // Re-enable layout and force an update after the animations
                uiStage.addAction(Actions.sequence(
                    Actions.delay(delay + 0.2f), // Adjust delay if needed
                    Actions.run(() -> {
                        uiTable.setLayoutEnabled(true);
                        uiTable.layout(); // Force a final layout update
                    })
                ));
            })
        ));
    }


    public void dispose() {
        if (uiSkin != null) {
            uiSkin.dispose();
        }
        if (texture != null) {
            texture.dispose();
        }
        if (uiStage != null) {
            uiStage.dispose();
        }
    }

    // Inner class to define a separate ClickListener for each button
    private class ButtonClickListener extends ClickListener {
        private String buttonLabel;

        public ButtonClickListener(String buttonLabel) {
            this.buttonLabel = buttonLabel;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            // Perform different actions based on the button clicked
            switch ( buttonLabel ) {
                case MENU_BUTTON_PLAY_TEXT:
                    // Handle play button click
                    subGame.setScreen(subGame.mapScreen());
   //                 subGame.setScreen(subGame.gameScreen());
//                    if (subGame.gameScreen().isPaused()) {
//                        subGame.gameScreen().resume();
//                    }
                    break;
                case MENU_BUTTON_OPTION_TEXT:
                    subGame.setScreen(subGame.getOptions());
                    break;
                case MENU_BUTTON_RESET_TEXT:
                    subGame.gameScreen().reset();
                    subGame.gameScreen().resetUpgrades();
                    //subGame.getUpgradeStore().setDefaults();
                    // Handle reset button click
                    break;
                case MENU_BUTTON_QUIT_TEXT:
                    subGame.gameScreen().getUpgradeStore().saveGame();
                    Gdx.app.exit();
                    break;
                default:
                    // Handle default case
                    break;
            }
        }
    }

}

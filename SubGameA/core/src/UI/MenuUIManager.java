package UI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.sub.SubGame;
import utilities.Constants;

import static utilities.Constants.UIConstants.*;

public class MenuUIManager {
    private Stage uiStage;
    private Table uiTable;
    private Skin uiSkin;
    private String selectedOption;
    private SubGame subGame;
    private Screen GameScreen;
    public MenuUIManager(SubGame subGame){
        this.selectedOption = "Menu";
        uiStage = new Stage(new ScreenViewport());
        this.subGame = subGame;
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
        } catch (Exception e) {
            Gdx.app.error("MainMenu", "Error loading skin file", e);
        }
    }

    private void createUIElements() {
        uiTable.setFillParent(true); // Makes the table take the whole stage
        uiStage.addActor(uiTable);

        Label menuTitle = createMenuTitle();
        uiTable.add(menuTitle).padBottom(BUTTON_PADDING).row();

        String[] buttonLabels = {
                BUTTON_PLAY_TEXT,
                BUTTON_OPTION_TEXT,
                BUTTON_RESET_TEXT,
                BUTTON_QUIT_TEXT
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
        Label menuTitle = new Label(TITLE_TEXT, uiSkin);
        menuTitle.setColor(TITLE_COLOR);
        menuTitle.setFontScale(FONT_MENU_SIZE);
        return menuTitle;
    }
    private TextButton createButton(String text, Color color) {
        TextButton button = new TextButton(text, uiSkin);
        if (text.equals(BUTTON_PLAY_TEXT)) {
            TextButton.TextButtonStyle arcadeStyle = uiSkin.get("arcade", TextButton.TextButtonStyle.class);
            button.setStyle(arcadeStyle);
        }
        button.getLabel().setFontScale(Constants.UIConstants.FONT_MENU_SIZE);
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

            uiTable.add(button).width(BUTTON_WIDTH).height(BUTTON_HEIGHT).padBottom(BUTTON_PADDING).row();
        }
    }

    public void dispose() {
        if (uiSkin != null) {
            uiSkin.dispose();
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
            switch (buttonLabel) {
                case BUTTON_PLAY_TEXT:
                    // Handle play button click
                    subGame.setScreen(subGame.gamePlayScreen());
                    break;
                case BUTTON_OPTION_TEXT:
                    // Handle option button click
                    break;
                case BUTTON_RESET_TEXT:
                    // Handle reset button click
                    break;
                case BUTTON_QUIT_TEXT:
                    Gdx.app.exit();
                    break;
                default:
                    // Handle default case
                    break;
            }
        }
    }

}
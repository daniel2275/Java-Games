package utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;


public class Constants {

    public static class UIConstants {
    //Constants for percentage-based layout
    public static final float BUTTON_WIDTH_PERCENT = 0.3f;
    public static final float BUTTON_HEIGHT_PERCENT = 0.12f;
    public static final float FONT_PERCENT = 0.001f;

    // UI-related constants
    public static final float BUTTON_WIDTH = Gdx.graphics.getWidth() * BUTTON_WIDTH_PERCENT;
    public static final float BUTTON_HEIGHT = Gdx.graphics.getHeight() * BUTTON_HEIGHT_PERCENT;
    public static final float FONT_MENU_SIZE = (Gdx.graphics.getWidth() * FONT_PERCENT);
    public static final float FONT_GAME_SIZE = (Game.VIRTUAL_WIDTH * FONT_PERCENT);
    public static final float BUTTON_PADDING = 20f;

    // Colors
    //private static Color newTeal = new Color(97,152,190,255);
    public static final Color BACKGROUND_COLOR = Color.valueOf("241424");
    public static final Color TITLE_COLOR = Color.WHITE;
    public static final Color BUTTON_PLAY_COLOR = Color.valueOf("6198be");;
    public static final Color BUTTON_OPTION_COLOR = Color.valueOf("6198be");
    public static final Color BUTTON_RESET_COLOR = Color.ORANGE;
    public static final Color BUTTON_QUIT_COLOR =  Color.valueOf("6198be");

    // Texts
    // Main-menu
    public static final String MENU_TITLE_TEXT = "U-BOAT 1.0";
    public static final String MENU_BUTTON_PLAY_TEXT = "Play";
    public static final String MENU_BUTTON_OPTION_TEXT = "Options";
    public static final String MENU_BUTTON_RESET_TEXT = "Reset";
    public static final String MENU_BUTTON_QUIT_TEXT = "Quit";
    // Options
    public static final String OPTIONS_TITLE_TEXT = "Options";
    public static final String OPTIONS_BUTTON_VOLUME_TEXT = "Volume ";
    public static final String OPTIONS_BUTTON_BACK_TEXT = "Back";

    // Skin file path
    public static final String SKIN_FILE_PATH = "clean-crispy/skin/clean-crispy-ui.json";

    // Misc
    public static final String SLIDER_TYPE = "default-horizontal";
}

public class PlayerConstants {
    // Constants for Player
    public static final int PLAYER_WIDTH = 48;
    public static final int PLAYER_HEIGHT = 16;
}

    public static class Game {
        public static final boolean VISIBLE_HITBOXES = true ;

        public static final float VIRTUAL_WIDTH = 800;
        public static final float VIRTUAL_HEIGHT = 480;

        public static final float SKY_SIZE = 90f; //150f - 800x600
        public static final int SCALE = 1;
    }


    private Constants() {
    }
}

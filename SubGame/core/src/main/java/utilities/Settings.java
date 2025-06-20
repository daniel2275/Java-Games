package utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;


public class Settings {

    public static class UIConstants {
    //Constants for percentage-based layout
    public static final float BUTTON_WIDTH_PERCENT = 0.3f;
    public static final float BUTTON_HEIGHT_PERCENT = 0.12f;
    public static final float FONT_PERCENT_MENU = 0.001f;
    public static final float FONT_PERCENT_GAME = 0.002f;


    // UI-related constants
    public static final float BUTTON_WIDTH = Gdx.graphics.getWidth() * BUTTON_WIDTH_PERCENT;
    public static final float BUTTON_HEIGHT = Gdx.graphics.getHeight() * BUTTON_HEIGHT_PERCENT;
    public static final float FONT_MENU_SIZE = (Gdx.graphics.getWidth() * FONT_PERCENT_MENU);
    public static final float FONT_GAME_SIZE = (Game.VIRTUAL_WIDTH * FONT_PERCENT_MENU);
    public static final float BUTTON_PADDING = 20f;

    // Colors
    //private static Color newTeal = new Color(97,152,190,255);
    //public static final Color BACKGROUND_COLOR = Color.valueOf("241424");
    public static final Color BACKGROUND_COLOR = Color.valueOf("6198be");
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
    // Map-menu
    public static final String MAP_TITLE_TEXT = "U-BOAT 1.0";
    public static final String MAP_BUTTON_PLAY_TEXT = "OP 1";
    public static final String MAP_BUTTON_OPTION_TEXT = "OP 2";
    public static final String MAP_BUTTON_RESET_TEXT = "OP 3";
    public static final String MAP_BUTTON_QUIT_TEXT = "OP 4";
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
//    public static final int PLAYER_WIDTH = 64;
//    public static final int PLAYER_HEIGHT = 16;
    public static final int PLAYER_WIDTH = 128;
    public static final int PLAYER_HEIGHT = 23;

}

    public static class Game {
        public static final boolean VISIBLE_HITBOXES = true ;

//        public static final float VIRTUAL_WIDTH = 800;
//        public static final float VIRTUAL_HEIGHT = 480;
//
//        public static final float SKY_SIZE = 90f; //150f - 800x600
        public static final int SCALE = 1;

        public static final float VIRTUAL_WIDTH = 1280;
        public static final float VIRTUAL_HEIGHT = 720;


        public static final float SKY_SIZE = 115f; //90f; //150f - 800x600

        public static final float SURFACE_OFFSET = 10;

        public static final float AGGRO_RANGE_X = 450;
        public static final float AGGRO_RANGE_Y = 350;

        public static final float ATTACK_RANGE = 400;
    }

    public class LevelConstants {
        public static final int DESTROYER_WIDTH = 200;
        public static final int DESTROYER_HEIGHT = 82;
        public static final int TANKER_WIDTH = 250;
        public static final int TANKER_HEIGHT = 58;
        public static final int ENEMY_SUB_WIDTH = 160;
        public static final int ENEMY_SUB_HEIGHT = 32;
        public static final int MINI_SUB_WIDTH = 46;
        public static final int MINI_SUB_HEIGHT = 12;

        public static final String DESTROYER_ATLAS = "animations/destroyeratlas.atlas";
        public static final String TANKER_ATLAS = "animations/tankeratlas.atlas";
        public static final String ENEMY_SUB_ATLAS = "animations/enemysub.atlas";
        public static final String MINI_SUB_ATLAS = "animations/minisub.atlas";
    }

    private Settings() {
    }
}

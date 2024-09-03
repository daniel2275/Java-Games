package gamestates;

public enum Gamestate {
    PLAYING, MENU, STORE, OPTIONS, PAUSE, GAME_OVER, QUIT;

    public static Gamestate state = MENU;
}

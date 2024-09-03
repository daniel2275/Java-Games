package UI.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

public class FontManager {
    private static BitmapFont myFont;
    private int size;
    public static void loadFont(int size) {
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Roboto-Black.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = size; // Set the desired font size
        myFont = generator.generateFont(parameter);
        generator.dispose(); // Don't forget to dispose the generator after use
    }

    public static BitmapFont getFont() {
        if (myFont == null) {
            loadFont(24);
        }
        return myFont;
    }

    public static void dispose() {
        if (myFont != null) {
            myFont.dispose();
        }
    }
}

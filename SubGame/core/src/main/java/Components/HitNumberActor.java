package Components;

import UI.game.FontManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

import static utilities.Settings.UIConstants.SKIN_FILE_PATH;

public class HitNumberActor extends Group {
        private Label label;
        private Skin skin; // = new Skin(Gdx.files.internal("clean-crispy/skin/clean-crispy-ui.json"));
        private  Color color = Color.WHITE;


    private void loadSkin() {
        try {
            skin = new Skin(Gdx.files.internal(SKIN_FILE_PATH));

            // Load the custom font
            FontManager.loadFont(24);

            // Add the custom font to the skin and replace the default font
            skin.add("default-font", FontManager.getFont());

            // Modify existing styles to use the new font
            Label.LabelStyle labelStyle = skin.get(Label.LabelStyle.class);
            labelStyle.font = FontManager.getFont();
            labelStyle.fontColor = color;
            skin.add("default", labelStyle);
        } catch (Exception e) {
            Gdx.app.error("HitNumberActor", "Error loading skin file", e);
        }
    }

        public HitNumberActor(float x, float y , String value, Color color) {
            this.color = color;
            loadSkin();
            // Set the position of the hit number
            setPosition(x, y);

            label = new Label(String.valueOf(value), skin);
            label.setColor(Color.WHITE);
            label.setAlignment(Align.center);
            label.setText(value);

            // Add the label to the HitNumber actor
            this.addActor(label);

            // Make the label fade out and move up
            addAction(Actions.sequence(
                    Actions.parallel(
                            Actions.fadeOut(1.0f),
                            Actions.moveBy(0, 50, 3.0f)
                    ),
                    Actions.removeActor()
            ));
        }

    public HitNumberActor(float x, float y , String value) {
        this(x,y,value, Color.WHITE);
    }

    @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
        }

}



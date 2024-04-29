package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;

public class HitNumberActor extends Group {
        private Label label;
        private Skin skin = new Skin(Gdx.files.internal("glassyui/glassy-ui.json"));

        public HitNumberActor(float x, float y , int value) {
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

    @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
        }

}



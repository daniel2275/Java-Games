package utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import gamestates.GamePlayScreen;


public class HelpMethods {

    public static Rectangle initHitBox(float x, float y, int width, int height) {
        return new Rectangle(x,y,width,height);
    }

    public static Rectangle updateHitbox(Rectangle hitbox, float x,float y) {
        hitbox.x = x;
        hitbox.y = y;
        return hitbox;
    }

    public static class FadingAnimation {
        private float stateTime;
        private float alpha;
        private float duration;

        public FadingAnimation(float duration) {
            this.duration = duration;
            stateTime = 0f;
            alpha = 1f;
        }

        public void update(float delta) {
            stateTime += delta;
            alpha = MathUtils.clamp(alpha - delta/duration, 0f, 1f); // Decrease alpha over time
        }

        public float getAlpha() {
            return alpha;
        }

        public Color color(GamePlayScreen gamePlayScreen) {
            Color color = gamePlayScreen.getBatch().getColor();
            color.a = getAlpha(); // Set alpha value of color
            return color;
        }
    }


}



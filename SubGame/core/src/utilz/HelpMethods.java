package utilz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

import static com.danielr.subgame.SubGame.batch;

public class HelpMethods {

    public static Rectangle initHitBox(float x, float y, int width, int height) {
        return new Rectangle(x,y,width,height);
    }

    public static Rectangle updateHitbox(Rectangle hitbox, float x,float y) {
        hitbox.x = x;
        hitbox.y = y;
        return hitbox;
    }


//    public static Rectangle drawObject(TextureRegion currentFrame, Rectangle hitbox, float xOffset, float yOffset, int flipX, int flipY, float health, float reload, Color color) {
////        hitbox = HelpMethods.updateHitbox(hitbox, hitbox.getX(), hitbox.getY());
//
//        if(!pause) {
//            batch.begin();
//            batch.setColor(color);
//            if (currentFrame != null) {
//                batch.draw(currentFrame, hitbox.getX() + xOffset, hitbox.getY() + yOffset, hitbox.getWidth() * flipX, hitbox.getHeight() * flipY);
//            }
//            batch.end();
//
//            if (health > 0) {
//                shapeRendered.begin(ShapeRenderer.ShapeType.Filled);
//                if (((int) (hitbox.getWidth() / 100 * health)) <= (int) hitbox.getWidth() / 3) {
//                    shapeRendered.setColor(Color.RED);
//                } else if (((int) (hitbox.getWidth() / 100 * health)) <= (int) hitbox.getWidth() / 2) {
//                    shapeRendered.setColor(Color.YELLOW);
//                } else {
//                    shapeRendered.setColor(Color.GREEN);
//                }
//
//                shapeRendered.rect(hitbox.getX(), hitbox.getY() + hitbox.getHeight() + 5, (hitbox.getWidth() / 100 * health), 2);
//                shapeRendered.end();
//            }
//
//            if (reload >= 0) {
//                shapeRendered.begin(ShapeRenderer.ShapeType.Filled);
//                if (((int) (hitbox.getWidth() / reloadSpeed * reload)) <= (int) hitbox.getWidth() / 2.5) {
//                    shapeRendered.setColor(Color.GREEN);
//                } else if (((int) (hitbox.getWidth() / reloadSpeed * reload)) <= (int) hitbox.getWidth() / 1.5) {
//                    shapeRendered.setColor(Color.GOLD);
//                } else {
//                    shapeRendered.setColor(Color.FIREBRICK);
//                }
//                float width = hitbox.getWidth() - (hitbox.getWidth() / reloadSpeed * reload);
////                System.out.println(width);
//                shapeRendered.rect(hitbox.getX(), hitbox.getY() + hitbox.getHeight() + 8, width, 1);
//                shapeRendered.end();
//            }
//
//
//            if (VISIBLE_HITBOXES) {
//                shapeRendered.setColor(Color.WHITE);
//                shapeRendered.begin();
//                shapeRendered.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
//                shapeRendered.end();
//            }
//
//        }
//        return hitbox;
//    }

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

        public Color color() {
            Color color = batch.getColor();
            color.a = getAlpha(); // Set alpha value of color
            return color;
        }
    }


}



package utilz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.danielr.subgame.SubGame;

import static com.danielr.subgame.SubGame.shapeRendered;
import static utilz.Constants.Game.VISIBLE_HITBOXES;

public class HelpMethods {

    public static Rectangle initHitBox(float x, float y, int width, int height) {
        return new Rectangle(x,y,width,height);
    }

    public static Rectangle updateHitbox(Rectangle hitbox, float x,float y) {
        hitbox.x = x;
        hitbox.y = y;
        return hitbox;
    }


    public static Rectangle drawObject(TextureRegion currentFrame, Rectangle hitbox, float xOffset, int flip, float health) {
//        hitbox = HelpMethods.updateHitbox(hitbox, hitbox.getX(), hitbox.getY());

        SubGame.batch.begin();
        SubGame.batch.draw(currentFrame, hitbox.getX() + xOffset, hitbox.getY() , hitbox.getWidth() * flip ,hitbox.getHeight());
        SubGame.batch.end();

        if (health > 0) {
            shapeRendered.begin(ShapeRenderer.ShapeType.Filled);
            if (((int) (hitbox.getWidth() / 100 * health)) <= (int) hitbox.getWidth() / 3) {
                shapeRendered.setColor(Color.RED);
            } else if (((int) (hitbox.getWidth() / 100 * health)) <= (int) hitbox.getWidth() / 2) {
                shapeRendered.setColor(Color.YELLOW);
            } else {
                shapeRendered.setColor(Color.GREEN);
            }

            shapeRendered.rect(hitbox.getX(), hitbox.getY() + hitbox.getHeight() + 5, (hitbox.getWidth() / 100 * health), 2);
            shapeRendered.end();
        }

        if (VISIBLE_HITBOXES) {
            shapeRendered.setColor(Color.WHITE);
            shapeRendered.begin();
            shapeRendered.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
            shapeRendered.end();
        }
        return hitbox;
    }



}

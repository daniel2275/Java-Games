package utilz;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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


    public static Rectangle drawObject( float stateTime, Animation<TextureRegion> shipIdle, Rectangle hitbox) {
        TextureRegion currentFrame;
        SubGame.batch.begin();
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame =  shipIdle.getKeyFrame(stateTime, true);
        hitbox = HelpMethods.updateHitbox(hitbox, hitbox.getX() , hitbox.getY());
        SubGame.batch.draw(currentFrame, hitbox.getX(), hitbox.getY() , hitbox.getWidth() ,hitbox.getHeight());
        SubGame.batch.end();


        if (VISIBLE_HITBOXES) {
            shapeRendered.begin();
            hitbox = HelpMethods.updateHitbox(hitbox, hitbox.getX(), hitbox.getY());
            shapeRendered.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
            shapeRendered.end();
        }
        return hitbox;
    }
}

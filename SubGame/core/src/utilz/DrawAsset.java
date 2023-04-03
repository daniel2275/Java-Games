package utilz;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;

import static com.danielr.subgame.SubGame.*;
import static com.danielr.subgame.SubGame.shapeRendered;
import static utilz.Constants.Game.VISIBLE_HITBOXES;

public class DrawAsset {
    private TextureRegion currentFrame;
    private Rectangle hitbox;
    private float xOffset;
    private float yOffset;
    private int flipX;
    private int flipY;
    private float health = -1;
    private float reload;
    private Color color;
    //--- Overloaded ex: torpedo
    private float width = 0;
    private float height = 0;
    private float scaleX = 1f;
    private float scaleY = 1f;
    private float angle = 0;

    private float reloadSpeed;

//
//

    public DrawAsset(TextureRegion currentFrame, Rectangle hitbox, float xOffset, float yOffset, int flipX, int flipY, float health, float reload, Color color) {
        this.currentFrame = currentFrame;
        this.hitbox = hitbox;
        this.width = hitbox.width;
        this.height = hitbox.height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.flipX = flipX;
        this.flipY = flipY;
        this.health = health;
        this.reload = reload;
        this.color = color;
    }

    public DrawAsset(TextureRegion currentFrame, Rectangle hitbox, float xOffset, float yOffset, int flipX, int flipY, float health, float reload, Color color, float width,float height,float scaleX ,float scaleY,float angle) {
        this(currentFrame,hitbox,xOffset,yOffset,flipX,flipY,health,reload,color);
        this.width = width;
        this.height = height;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.angle= angle;
    }

    public DrawAsset(TextureRegion currentFrame, Rectangle hitbox, float xOffset, float yOffset, int flipX, int flipY, float health, float reload, Color color,float reloadSpeed) {
        this(currentFrame,hitbox,xOffset,yOffset,flipX,flipY,health,reload, color);
        this.reloadSpeed = reloadSpeed;
    }





    public void draw() {
//        hitbox = HelpMethods.updateHitbox(hitbox, hitbox.getX(), hitbox.getY());

        if(!pause) {
            batch.begin();
            batch.setColor(color);
            if (currentFrame != null) {
                if (health == -1) {
                    batch.draw(currentFrame, hitbox.getX() + xOffset, hitbox.getY() + yOffset, hitbox.getWidth() * flipX, hitbox.getHeight() * flipY, width, height, scaleX, scaleY, angle);

//                      batch.draw(currentFrame, hitbox.getX() , hitbox.getY() , hitbox.getWidth() , hitbox.getHeight(), width, height, scaleX, scaleY, angle);

                } else {
                    batch.draw(currentFrame, hitbox.getX() + xOffset, hitbox.getY() + yOffset, hitbox.getWidth() * flipX, hitbox.getHeight() * flipY);
                }
            }
            batch.end();

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

            if (reload >= 0) {
                shapeRendered.begin(ShapeRenderer.ShapeType.Filled);
                if (((int) (hitbox.getWidth() / reloadSpeed * reload)) <= (int) hitbox.getWidth() / 2.5) {
                    shapeRendered.setColor(Color.GREEN);
                } else if (((int) (hitbox.getWidth() / reloadSpeed * reload)) <= (int) hitbox.getWidth() / 1.5) {
                    shapeRendered.setColor(Color.GOLD);
                } else {
                    shapeRendered.setColor(Color.FIREBRICK);
                }
                float width = hitbox.getWidth() - (hitbox.getWidth() / reloadSpeed * reload);

                shapeRendered.rect(hitbox.getX(), hitbox.getY() + hitbox.getHeight() + 8, width, 1);
                shapeRendered.end();
            }


            if (VISIBLE_HITBOXES) {
                shapeRendered.setColor(Color.WHITE);
                shapeRendered.begin();
                shapeRendered.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight(), width, height, scaleX, scaleY, angle);
                shapeRendered.end();
            }

        }
    }

//    public void draw(TextureRegion currentFrame, Rectangle hitbox, float xOffset, float yOffset, int flipX, int flipY, float health, float reload, Color color, float width,float height,float scaleX ,float scaleY,float angle) {
//        this.width = width;
//        this.height = height;
//        this.scaleX = scaleX;
//        this.scaleY = scaleY;
//        this.angle= angle;
//        draw(currentFrame,hitbox,xOffset,yOffset,flipX,flipY,health,reload,color);
//    }
}

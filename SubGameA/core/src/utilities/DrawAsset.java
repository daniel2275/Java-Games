package utilities;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import UI.game.GameScreen;

public class DrawAsset {
    private TextureRegion currentFrame;
    private Rectangle hitbox;
    private float xOffset;
    private float yOffset;
    private int flipX;
    private int flipY;
    private float maxHealth = -1;
    private float currentHealth;
    private float reload;
    private Color color;
    //--- Overloaded ex: torpedo
    private float width = 0;
    private float height = 0;
    private float scaleX = 1f;
    private float scaleY = 1f;
    private float angle = 0;
    private float originX = 0;
    private float originY = 0;
    private float transformedX = 0;
    private float transformedY = 0;
    private float reloadSpeed;
    private GameScreen gameScreen;

    public DrawAsset(GameScreen gameScreen, TextureRegion currentFrame, Rectangle hitbox, float xOffset, float yOffset, int flipX, int flipY, float maxHealth, float currentHealth, float reload, Color color) {
        this.gameScreen = gameScreen;
        this.currentFrame = currentFrame;
        this.hitbox = hitbox;
        this.width = hitbox.width;
        this.height = hitbox.height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.flipX = flipX;
        this.flipY = flipY;
        this.maxHealth = maxHealth;
        this.currentHealth = currentHealth;
        this.reload = reload;
        this.color = color;
    }

    public DrawAsset(GameScreen gameScreen, TextureRegion currentFrame, Rectangle hitbox, float xOffset, float yOffset, int flipX, int flipY, float maxHealth, float currentHealth, float reload, Color color, float width, float height, float scaleX , float scaleY, float angle) {
        this(gameScreen, currentFrame,hitbox,xOffset,yOffset,flipX,flipY, maxHealth, currentHealth,reload,color);
        this.width = width;
        this.height = height;
        this.scaleX = scaleX;
        this.scaleY = scaleY;
        this.angle= angle;
    }

    public DrawAsset(GameScreen gameScreen, TextureRegion currentFrame, Rectangle hitbox, float xOffset, float yOffset, int flipX, int flipY, float maxHealth, float currentHealth, float reload, Color color, float reloadSpeed) {
        this(gameScreen, currentFrame,hitbox,xOffset,yOffset,flipX,flipY, maxHealth,currentHealth, reload, color);
        this.reloadSpeed = reloadSpeed;
    }

//    public void draw() {
//
//        if(!pause) {
//            gamePlayScreen.getBatch().setProjectionMatrix(gamePlayScreen.camera.combined);
//            gamePlayScreen.getBatch().begin();
//            gamePlayScreen.getBatch().setColor(color);
//            if (currentFrame != null) {
//                if (maxHealth == -1) {
//                    rotate();
//                    gamePlayScreen.getBatch().draw(currentFrame, transformedX, transformedY, originX, originY, width, height, scaleX * SCALE, scaleY * SCALE, angle);
//                } else {
//                    gamePlayScreen.getBatch().draw(currentFrame, hitbox.getX() + xOffset, hitbox.getY() + yOffset, hitbox.getWidth() * flipX , hitbox.getHeight() * flipY );
//                }
//            }
//            gamePlayScreen.getBatch().end();
//
//            gamePlayScreen.getShapeRenderer().setProjectionMatrix(gamePlayScreen.camera.combined);
//
//            gamePlayScreen.getShapeRenderer().setAutoShapeType(true);
//            if (maxHealth > 0) {
//                gamePlayScreen.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
//                if ((int) (hitbox.getWidth() / 100 * ((currentHealth * 100) / maxHealth)) <= (int) hitbox.getWidth() / 3) {
//                    gamePlayScreen.getShapeRenderer().setColor(Color.RED);
//                } else if ((int) (hitbox.getWidth() / 100 * ((currentHealth * 100) / maxHealth)) <= (int) hitbox.getWidth() / 2) {
//                    gamePlayScreen.getShapeRenderer().setColor(Color.YELLOW);
//                } else {
//                    gamePlayScreen.getShapeRenderer().setColor(Color.GREEN);
//                }
//                gamePlayScreen.getShapeRenderer().rect(hitbox.getX(), hitbox.getY() + hitbox.getHeight() + 5, (int) (hitbox.getWidth() / 100 * ((currentHealth * 100) / maxHealth)) , 2 );
//                gamePlayScreen.getShapeRenderer().end();
//            }
//
//            if (reload >= 0) {
//                gamePlayScreen.getShapeRenderer().begin(ShapeRenderer.ShapeType.Filled);
//                if (((int) (hitbox.getWidth() / reloadSpeed * reload)) <= (int) hitbox.getWidth() / 2.5) {
//                    gamePlayScreen.getShapeRenderer().setColor(Color.GREEN);
//                } else if (((int) (hitbox.getWidth() / reloadSpeed * reload)) <= (int) hitbox.getWidth() / 1.5) {
//                    gamePlayScreen.getShapeRenderer().setColor(Color.GOLD);
//                } else {
//                    gamePlayScreen.getShapeRenderer().setColor(Color.FIREBRICK);
//                }
//                float width = hitbox.getWidth() - (hitbox.getWidth() / reloadSpeed * reload);
//                gamePlayScreen.getShapeRenderer().rect(hitbox.getX(), hitbox.getY() + hitbox.getHeight() + 8, width , 1 );
//                gamePlayScreen.getShapeRenderer().end();
//            }
//
//            if (Constants.Game.VISIBLE_HITBOXES) {
//                gamePlayScreen.getShapeRenderer().setColor(Color.WHITE);
//                gamePlayScreen.getShapeRenderer().begin();
//                rotate();
//                gamePlayScreen.getShapeRenderer().rect(transformedX, transformedY, originX, originY, width, height, scaleX * SCALE, scaleY * SCALE, angle);
//                gamePlayScreen.getShapeRenderer().end();
//            }
//        }
//    }

    public void rotate() {
        float centerX = hitbox.getX() + hitbox.getWidth() / 2;
        float centerY = hitbox.getY() + hitbox.getHeight() / 2;

        originX = width / 2;
        originY = height / 2;

        float x = centerX - originX;
        float y = centerY - originY;

        Vector3 transformedPosition = new Vector3(x, y, 0);

        transformedX = transformedPosition.x;
        transformedY = transformedPosition.y;
    }

}

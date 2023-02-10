package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import utilz.LoadSave;

public class Torpedo {
    public static final int SPEED = 1;
    public static final int TORPEDO_WIDTH = 3 ;
    public static final int TORPEDO_HEIGHT = 12 ;

    private SpriteBatch batch;
    float stateTime;
    private TextureRegion currentFrame;

    private Rectangle hitbox;
    private ShapeRenderer shapeRendered;

    private TextureRegion[][] boatSprites =  new TextureRegion[1][1];;
    private Animation<TextureRegion> shipIdle;
    private static Texture boatAtlas;


    public Torpedo(float x, float y) {
        loadAnimations("torpedo.png");
        create();

        hitbox = LoadSave.initHitBox(x, y, TORPEDO_WIDTH, TORPEDO_HEIGHT);

//        render();
    }

    public void update() {
        updatePos();
        render();
    }

    public void updatePos() {
        hitbox.y += SPEED;
    }

    public void create(){
        batch = new SpriteBatch();
        shapeRendered = new ShapeRenderer();
        shapeRendered.setAutoShapeType(true);
    }

    public void render() {
        batch.begin();
        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame =  shipIdle.getKeyFrame(stateTime, true);
        hitbox = LoadSave.updateHitbox(hitbox, hitbox.getX() , hitbox.getY(), hitbox.getWidth() , hitbox.getHeight() );
        batch.draw(currentFrame, hitbox.getX(), hitbox.getY() , hitbox.getWidth() ,hitbox.getHeight());
        batch.end();



        shapeRendered.begin();
        hitbox = LoadSave.updateHitbox(hitbox, hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        shapeRendered.rect(hitbox.getX(), hitbox.getY(), hitbox.getWidth(), hitbox.getHeight());
        shapeRendered.end();
    }

    private void loadAnimations(String sprites) {
        boatAtlas = new Texture(sprites);

//        for (int i= 0; i <= 5; i++) {
//            for (int j= 0; j <= 5; j++) {
        boatSprites[0][0] = new TextureRegion(boatAtlas, 1, 1,TORPEDO_WIDTH,TORPEDO_HEIGHT);
//            }
//        }

        shipIdle = LoadSave.boatAnimation(0,1, boatSprites, 2.0f);
    }


    public Rectangle getHitbox() {
        return hitbox;
    }
}


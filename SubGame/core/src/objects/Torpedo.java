package objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import utilz.HelpMethods;
import utilz.LoadSave;

public class Torpedo {
    public static final int SPEED = 1;
    public static final int TORPEDO_WIDTH = 3 ;
    public static final int TORPEDO_HEIGHT = 12 ;


    float stateTime;


    private Rectangle hitbox;


    private TextureRegion[][] boatSprites =  new TextureRegion[1][1];;
    private Animation<TextureRegion> shipIdle;
    private static Texture boatAtlas;


    public Torpedo(float x, float y) {
        loadAnimations("torpedo.png");

        hitbox = HelpMethods.initHitBox(x, y, TORPEDO_WIDTH, TORPEDO_HEIGHT);

    }

    public void update() {
        updatePos();
        render();
    }

    public void updatePos() {
        hitbox.y += SPEED;
    }



    public void render() {
        hitbox = HelpMethods.drawObject(stateTime, shipIdle, hitbox);
    }

    private void loadAnimations(String sprites) {
        boatAtlas = new Texture(sprites);

//        for (int i= 0; i <= 5; i++) {
//            for (int j= 0; j <= 5; j++) {
        boatSprites[0][0] = new TextureRegion(boatAtlas, 0, 0,TORPEDO_WIDTH,TORPEDO_HEIGHT);
//            }
//        }

        shipIdle = LoadSave.boatAnimation(0,1, boatSprites, 2.0f);
    }


    public Rectangle getHitbox() {
        return hitbox;
    }
}


package Components;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class TorpedoActor extends BaseActor{
    private Animation<TextureRegion> hitAnimationLeft;

    public TorpedoActor(String name,
                        Animation<TextureRegion> idleAnimationLeft,
                        Animation<TextureRegion> moveAnimationLeft,
                        Animation<TextureRegion> upAnimationLeft,
                        Animation<TextureRegion> downAnimationLeft,
                        Animation<TextureRegion> hitAnimationLeft,
                        Animation<TextureRegion> sunkAnimationLeft,
                        Animation<TextureRegion> surfacingAnimationLeft,
                        float reload,
                        float maxHealth,
                        float frameWidth,
                        float frameHeight,
                        float spawnPosX,
                        float spawnPosY) {
        super(name,
            idleAnimationLeft,
            moveAnimationLeft,
            upAnimationLeft,
            downAnimationLeft,
            hitAnimationLeft,
            sunkAnimationLeft,
            surfacingAnimationLeft,
            reload,
            maxHealth,
            frameWidth,
            frameHeight,
            spawnPosX,
            spawnPosY);
    }


    @Override
    public void handleSinking() {
        addAction(Actions.sequence(
            Actions.parallel(
                Actions.fadeOut(1.0f)
            ),
            Actions.removeActor()
        ));
    }

    @Override
    public boolean directionChanged(){
        return false;
    }

    @Override
    public void fadeActor(Batch batch){
    }

    @Override
    public void surface(){
    }

    @Override
    public void drawHealthBar(Batch batch){
    }

    @Override
    public void drawReloadBar(Batch batch){
    }

    @Override
    public void hitDirection() {
        setOverrideAnimation(hitAnimationLeft);
        setStateTime(0);
    }

    @Override
    public Rectangle getBounding() {

        //return new Rectangle(getX() , getY(), getWidth() , getHeight() );

        return rotate();
    }




}

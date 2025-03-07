package objects.mine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class MineAnimationManager  {
    private Animation<TextureRegion> mineAnimation;
    private Animation<TextureRegion> mineExplode;
    private HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();


    public MineAnimationManager(String sprites) {
        loadAnimations(sprites);
    }

    private void loadAnimations(String sprites) {
        TextureAtlas animationAtlas = new TextureAtlas(Gdx.files.internal(sprites));

        mineAnimation = createAnimation(animationAtlas, "idle", 8, 0.3f, true, "LOOP");
        mineExplode = createAnimation(animationAtlas, "explode", 6, 0.5f, false, "NORMAL");
        addAnimations();
    }

    private Animation<TextureRegion> createAnimation(TextureAtlas atlas, String regionName, int frames, float frameDuration, boolean flipHorizontally, String playMode) {
        Array<TextureAtlas.AtlasRegion> regions = new Array<>();
        for (int i = 1; i <= frames; i++) {
            TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(atlas.findRegion(regionName + i));
            if (flipHorizontally) {
                region.flip(true, false);
            }
            regions.add(region);
        }
        if (playMode.equals("LOOP")) {
            return new Animation<>(frameDuration, regions, Animation.PlayMode.LOOP);
        } else if (playMode.equals("REVERSED")) {
            return new Animation<>(frameDuration, regions, Animation.PlayMode.REVERSED);
        } else {
            return new Animation<>(frameDuration, regions, Animation.PlayMode.NORMAL);
        }


    }

    private void addAnimations() {
        animations.put("idle", mineAnimation);
        animations.put("explode", mineExplode);
    }


    public Animation<TextureRegion> getAnimation(String name) {
        return animations.get(name);
    }

}

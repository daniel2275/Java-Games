package objects.torpedo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import utilities.LoadSave;

public class TorpedoAnimationManager {
    private Animation<TextureRegion> torpedoUpAnimation;
    private Animation<TextureRegion> torpedoExplode;

//    private Animation<TextureRegion> torpedoAnimations;
//    private Animation<TextureRegion> torpedoAnimationsExplode;

    public TorpedoAnimationManager(Texture texture) {
        loadAnimations(texture);
    }

//    public TorpedoAnimationManager(String sprites) {
//        loadAnimations(sprites);
//    }


    private void loadAnimations(Texture texture) {
        TextureRegion[][] torpedoSprites = new TextureRegion[4][8];
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 7; j++) {
                torpedoSprites[i][j] = new TextureRegion(texture, 32 * j, 16 * i, Torpedo.TORPEDO_WIDTH, Torpedo.TORPEDO_HEIGHT);
            }
        }
        torpedoUpAnimation = LoadSave.boatAnimation(0, 8, torpedoSprites, 0.3f);
        torpedoExplode = LoadSave.boatAnimation(1, 4, torpedoSprites, 0.01f);
}

//    private void loadAnimations(String sprites) {
//        TextureAtlas animationAtlas = new TextureAtlas(Gdx.files.internal(sprites));
//
//        torpedoAnimations = createAnimation(animationAtlas, "torpedo", 8, 0.5f, false, "LOOP");
//    }

//    private Animation<TextureRegion> createAnimation(TextureAtlas atlas, String regionName, int frames, float frameDuration, boolean flipHorizontally, String playMode) {
//        Array<TextureAtlas.AtlasRegion> regions = new Array<>();
//        for (int i = 1; i <= frames; i++) {
//            TextureAtlas.AtlasRegion region = new TextureAtlas.AtlasRegion(atlas.findRegion(regionName + i));
//            if (flipHorizontally) {
//                region.flip(true, false);
//            }
//            regions.add(region);
//        }
//        if (playMode.equals("LOOP")) {
//            return new Animation<>(frameDuration, regions, Animation.PlayMode.LOOP);
//        } else if (playMode.equals("REVERSED")) {
//            return new Animation<>(frameDuration, regions, Animation.PlayMode.REVERSED);
//        } else {
//            return new Animation<>(frameDuration, regions, Animation.PlayMode.NORMAL);
//        }
//
//
//    }

//    public Animation<TextureRegion> getTorpedoUpAnimation() {
//        return torpedoAnimations;
//    }
//
//    public Animation<TextureRegion> getTorpedoExplodeAnimation() {
//        return torpedoAnimationsExplode;
//    }

        public Animation<TextureRegion> getTorpedoUpAnimation() {
        return torpedoUpAnimation;
    }

    public Animation<TextureRegion> getTorpedoExplodeAnimation() {
        return torpedoExplode;
    }
}

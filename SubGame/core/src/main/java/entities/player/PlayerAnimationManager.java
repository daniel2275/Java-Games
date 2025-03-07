package entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import utilities.LoadSave;

import java.util.HashMap;

import static utilities.Settings.PlayerConstants.PLAYER_HEIGHT;
import static utilities.Settings.PlayerConstants.PLAYER_WIDTH;

public class PlayerAnimationManager {
    private final TextureRegion[][] uBoatSprites = new TextureRegion[8][8];
    private Animation<TextureRegion> idleAnimationsLeft;
    private Animation<TextureRegion> idleAnimationsRight;
    private Animation<TextureRegion> movingAnimationsRight;
    private Animation<TextureRegion> movingAnimationsLeft;
    private Animation<TextureRegion> upAnimationsRight;
    private Animation<TextureRegion> upAnimationsLeft;
    private Animation<TextureRegion> downAnimationsRight;
    private Animation<TextureRegion> downAnimationsLeft;
    private Animation<TextureRegion> hitAnimationsRight;
    private Animation<TextureRegion> hitAnimationsLeft;
    private Animation<TextureRegion> sunkAnimationsRight;
    private Animation<TextureRegion> sunkAnimationsLeft;
    private Animation<TextureRegion> surfacingAnimationsRight;
    private Animation<TextureRegion> surfacingAnimationsLeft;
    private Animation<TextureRegion> turningAnimationsRight;
    private Animation<TextureRegion> turningAnimationsLeft;


    private HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();

    public PlayerAnimationManager(String sprites) {
        loadAnimations(sprites);
    }

    private void loadAnimations(String sprites) {
        Texture uBoatAtlas = new Texture(sprites);

        for (int i = 0; i <= 7; i++) {
            for (int j = 0; j <= 7; j++) {
                uBoatSprites[i][j] = new TextureRegion(uBoatAtlas, j * 64, i * 16, PLAYER_WIDTH, PLAYER_HEIGHT);
            }
        }
        //   idleAnimations = LoadSave.boatAnimation(0, 5, uBoatSprites, 2.0f);
        //   movingAnimationsRight = LoadSave.boatAnimation(1, 3, uBoatSprites, 0.055f);
        //upAnimationsRight = LoadSave.boatAnimation(2, 3, uBoatSprites, 0.5f);
        //downAnimationsRight = LoadSave.boatAnimation(3, 3, uBoatSprites, 0.5f);
//        hitAnimationsRight = LoadSave.boatAnimation(4, 2, uBoatSprites, 0.3f);
        sunkAnimationsRight = LoadSave.boatAnimation(5, 1, uBoatSprites, 0.7f);
        //   surfacingAnimationsRight = LoadSave.boatAnimation(6, 5, uBoatSprites, 0.2f);
        //    turningAnimationsRight = LoadSave.boatAnimation(7, 5, uBoatSprites, 0.2f);

        // testing atlas
        TextureAtlas playerAtlas = new TextureAtlas(Gdx.files.internal("playeranimations/playeratlas.atlas"));
        idleAnimationsRight = createAnimation(playerAtlas, "playeridle", 6, 0.5f, true, "LOOP");
        idleAnimationsLeft = createAnimation(playerAtlas, "playeridle", 6, 0.5f, false, "LOOP");

        movingAnimationsRight = createAnimation(playerAtlas, "playermove", 6, 0.5f, true, "LOOP");
        movingAnimationsLeft = createAnimation(playerAtlas, "playermove", 6, 0.5f, false, "LOOP");

        turningAnimationsRight = createAnimation(playerAtlas, "playerturn", 17, 0.2f, true, "NORMAL");
        turningAnimationsLeft = createAnimation(playerAtlas, "playerturn", 17, 0.2f, false, "NORMAL");

//        turningAnimationsRightReversed = createAnimation(playerAtlas, "playerturn", 9, 0.2f, true, "REVERSED");
//        turningAnimationsLeftReversed = createAnimation(playerAtlas, "playerturn", 9, 0.2f, false, "REVERSED");

        upAnimationsRight = createAnimation(playerAtlas, "playerup", 3, 0.5f, true, "NORMAL");
        upAnimationsLeft = createAnimation(playerAtlas, "playerup", 3, 0.5f, false, "NORMAL");

        downAnimationsRight = createAnimation(playerAtlas, "playerdown", 3, 0.5f, true, "NORMAL");
        downAnimationsLeft = createAnimation(playerAtlas, "playerdown", 3, 0.5f, false, "NORMAL");

        surfacingAnimationsRight = createAnimation(playerAtlas, "playerdive", 6, 0.2f, true, "NORMAL");
        surfacingAnimationsLeft = createAnimation(playerAtlas, "playerdive", 6, 0.2f, false, "NORMAL");

        hitAnimationsRight = createAnimation(playerAtlas, "playerhit", 4, 0.01f, true, "NORMAL");
        hitAnimationsLeft = createAnimation(playerAtlas, "playerhit", 4, 0.01f, false, "NORMAL");

//        sunkAnimationsRight = createAnimation(playerAtlas, "playerhit", 4, 0.7f, true);
//        sunkAnimationsRight = createAnimation(playerAtlas, "playerhit", 4, 0.7f, false);

        addAnimations();
    }

    private void addAnimations() {

        animations.put("idleLeft", idleAnimationsLeft);
        animations.put("movingLeft", movingAnimationsLeft);
        animations.put("upLeft", upAnimationsLeft);
        animations.put("downLeft", downAnimationsLeft);
        animations.put("hitLeft", hitAnimationsLeft);
        animations.put("sunkLeft", sunkAnimationsRight);
        animations.put("surfacingLeft", surfacingAnimationsLeft);
        animations.put("turningLeft", turningAnimationsLeft);
//        animations.put("turningLeftReversed", turningAnimationsLeftReversed);

        animations.put("idleRight", idleAnimationsRight);
        animations.put("movingRight", movingAnimationsRight);
        animations.put("upRight", upAnimationsRight);
        animations.put("downRight", downAnimationsRight);
        animations.put("hitRight", hitAnimationsRight);
        animations.put("sunkRight", invertHorizontal(sunkAnimationsRight));
        animations.put("surfacingRight", surfacingAnimationsRight);
        animations.put("turningRight", turningAnimationsRight);
//        animations.put("turningRightReversed", turningAnimationsRightReversed);
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


//    private Texture resizeTexture(Texture originalTexture, int srcX, int srcY, int srcWidth, int srcHeight, int newWidth, int newHeight) {
//        // Prepare the texture data if not already prepared
//        TextureData textureData = originalTexture.getTextureData();
//        if (!textureData.isPrepared()) {
//            textureData.prepare();
//        }
//
//        // Create a Pixmap from the original texture region
//        Pixmap originalPixmap = new Pixmap(srcWidth, srcHeight, textureData.getFormat());
//        originalPixmap.drawPixmap(textureData.consumePixmap(), 0, 0, srcX, srcY, srcWidth, srcHeight);
//
//        // Create a new Pixmap with the desired size
//        Pixmap resizedPixmap = new Pixmap(newWidth, newHeight, originalPixmap.getFormat());
//        resizedPixmap.drawPixmap(originalPixmap, 0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(), 0, 0, newWidth, newHeight);
//
//        // Create a new texture from the resized pixmap
//        Texture resizedTexture = new Texture(resizedPixmap, true); // 'true' enables mipmaps
//        resizedTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
//
//        // Dispose of the pixmaps to free resources
//        originalPixmap.dispose();
//        resizedPixmap.dispose();
//
//        return resizedTexture;
//    }


    private Animation<TextureRegion> invertHorizontal(Animation<TextureRegion> originalAnimation) {
        TextureRegion[] originalFrames = originalAnimation.getKeyFrames();
        int frameCount = originalFrames.length;
        TextureRegion[] flippedFrames = new TextureRegion[frameCount];

        // Clone the original frames and flip each one horizontally
        for (int i = 0; i < frameCount; i++) {
            TextureRegion originalFrame = originalFrames[i];
            TextureRegion flippedFrame = new TextureRegion(originalFrame); // Clone the original frame
            flippedFrame.flip(true, false); // Flip horizontally
            flippedFrames[i] = flippedFrame;
        }
        // Create the inverted animation using the flipped frames
        return new Animation<>(originalAnimation.getFrameDuration(), flippedFrames);
    }


    public Animation<TextureRegion> getAnimation(String name) {
        return animations.get(name);
    }

    public HashMap<String, Animation<TextureRegion>> getAllAnimations() {
        return animations;
    }


}


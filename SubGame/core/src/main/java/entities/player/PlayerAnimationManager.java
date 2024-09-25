package entities.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import utilities.LoadSave;

import static utilities.Constants.PlayerConstants.PLAYER_HEIGHT;
import static utilities.Constants.PlayerConstants.PLAYER_WIDTH;

public class PlayerAnimationManager  {
    private final TextureRegion[][] uBoatSprites = new TextureRegion[8][8];
    private Animation<TextureRegion> idleAnimations;
    private Animation<TextureRegion> movingAnimations;
    private Animation<TextureRegion> upAnimations;
    private Animation<TextureRegion> downAnimations;
    private Animation<TextureRegion> hitAnimations;
    private Animation<TextureRegion> sunkAnimations;
    private Animation<TextureRegion> surfacingAnimation;
    private Animation<TextureRegion> turningAnimation;

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
        idleAnimations = LoadSave.boatAnimation(0, 5, uBoatSprites, 2.0f);
        movingAnimations = LoadSave.boatAnimation(1, 3, uBoatSprites, 0.055f);
        upAnimations = LoadSave.boatAnimation(2, 3, uBoatSprites, 0.5f);
        downAnimations = LoadSave.boatAnimation(3, 3, uBoatSprites, 0.5f);
        hitAnimations = LoadSave.boatAnimation(4, 2, uBoatSprites, 0.3f);
        sunkAnimations = LoadSave.boatAnimation(5, 1, uBoatSprites, 0.7f);
        surfacingAnimation = LoadSave.boatAnimation(6, 5, uBoatSprites, 0.2f);
        turningAnimation = LoadSave.boatAnimation(7, 5, uBoatSprites,0.2f);

        // Testing MipMap rescale
        Texture testAtlas = new Texture(Gdx.files.internal("u3atlas.png"), true);
        testAtlas.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        TextureRegion[] animation = new TextureRegion[5];

        int targetWidth = 128;
        int targetHeight = 64;

        int scaleFactor = Math.min(targetWidth / PLAYER_WIDTH, targetHeight / PLAYER_HEIGHT);

        for (int i = 0; i < 5; i++) {
            TextureRegion region = new TextureRegion(testAtlas, 953 * i, 0, 953, 159);

            Texture resizedTexture = resizeTexture(region.getTexture(), region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), region.getRegionWidth() * scaleFactor, region.getRegionHeight() * scaleFactor);

            animation[i] = new TextureRegion(resizedTexture);
        }

        idleAnimations = new Animation<>(0.5f, animation);
    }


    private Texture resizeTexture(Texture originalTexture, int srcX, int srcY, int srcWidth, int srcHeight, int newWidth, int newHeight) {
        // Prepare the texture data if not already prepared
        TextureData textureData = originalTexture.getTextureData();
        if (!textureData.isPrepared()) {
            textureData.prepare();
        }

        // Create a Pixmap from the original texture region
        Pixmap originalPixmap = new Pixmap(srcWidth, srcHeight, textureData.getFormat());
        originalPixmap.drawPixmap(textureData.consumePixmap(), 0, 0, srcX, srcY, srcWidth, srcHeight);

        // Create a new Pixmap with the desired size
        Pixmap resizedPixmap = new Pixmap(newWidth, newHeight, originalPixmap.getFormat());
        resizedPixmap.drawPixmap(originalPixmap, 0, 0, originalPixmap.getWidth(), originalPixmap.getHeight(), 0, 0, newWidth, newHeight);

        // Create a new texture from the resized pixmap
        Texture resizedTexture = new Texture(resizedPixmap, true); // 'true' enables mipmaps
        resizedTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);

        // Dispose of the pixmaps to free resources
        originalPixmap.dispose();
        resizedPixmap.dispose();

        return resizedTexture;
    }


    public Animation<TextureRegion> getIdleAnimations() {
        return idleAnimations;
    }

    public Animation<TextureRegion> getMovingAnimations() {
        return movingAnimations;
    }

    public Animation<TextureRegion> getUpAnimations() {
        return upAnimations;
    }

    public Animation<TextureRegion> getDownAnimations() {
        return downAnimations;
    }

    public Animation<TextureRegion> getHitAnimations() {
        return hitAnimations;
    }

    public Animation<TextureRegion> getSunkAnimations() {
        return sunkAnimations;
    }

    public Animation<TextureRegion> getSurfacingAnimation() {
        return surfacingAnimation;
    }

    public Animation<TextureRegion> getTurningAnimation() {
        return turningAnimation;
    }
}


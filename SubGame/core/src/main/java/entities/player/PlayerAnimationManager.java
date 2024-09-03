package entities.player;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import utilities.LoadSave;

import static utilities.Constants.PlayerConstants.PLAYER_HEIGHT;
import static utilities.Constants.PlayerConstants.PLAYER_WIDTH;

public class PlayerAnimationManager  {
    private final TextureRegion[][] uBoatSprites = new TextureRegion[6][6];
    private Animation<TextureRegion> idleAnimations;
    private Animation<TextureRegion> movingAnimations;
    private Animation<TextureRegion> upAnimations;
    private Animation<TextureRegion> downAnimations;
    private Animation<TextureRegion> hitAnimations;
    private Animation<TextureRegion> sunkAnimations;

    public PlayerAnimationManager(String sprites) {
        loadAnimations(sprites);
    }

    private void loadAnimations(String sprites) {
        Texture uBoatAtlas = new Texture(sprites);

        for (int i = 0; i <= 5; i++) {
            for (int j = 0; j <= 5; j++) {
                uBoatSprites[i][j] = new TextureRegion(uBoatAtlas, j * 64, i * 16, PLAYER_WIDTH, PLAYER_HEIGHT);
            }
        }
        idleAnimations = LoadSave.boatAnimation(0, 5, uBoatSprites, 2.0f);
        movingAnimations = LoadSave.boatAnimation(1, 3, uBoatSprites, 0.055f);
        upAnimations = LoadSave.boatAnimation(2, 3, uBoatSprites, 0.7f);
        downAnimations = LoadSave.boatAnimation(3, 3, uBoatSprites, 0.7f);
        hitAnimations = LoadSave.boatAnimation(4, 1, uBoatSprites, 0.3f);
        sunkAnimations = LoadSave.boatAnimation(5, 1, uBoatSprites, 0.7f);
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
}


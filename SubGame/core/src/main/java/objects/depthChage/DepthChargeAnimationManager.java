package objects.depthChage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import utilities.LoadSave;

public class DepthChargeAnimationManager {
    private Animation<TextureRegion> depthChargeUpAnimation;
    private Animation<TextureRegion> depthChargeExplode;

    public DepthChargeAnimationManager(Texture texture) {
        loadAnimations(texture);
    }

    private void loadAnimations(Texture texture) {
        TextureRegion[][] depthChargeSprites = new TextureRegion[4][8];
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 7; j++) {
                depthChargeSprites[i][j] = new TextureRegion(texture, 16 * j, 16 * i, DepthCharge.DPC_WIDTH, DepthCharge.DPC_HEIGHT);
            }
        }
        depthChargeUpAnimation = LoadSave.boatAnimation(0, 8, depthChargeSprites, 0.03f);
        depthChargeExplode = LoadSave.boatAnimation(1, 8, depthChargeSprites, 0.5f);
    }

    public Animation<TextureRegion> getTorpedoUpAnimation() {
        return depthChargeUpAnimation;
    }

    public Animation<TextureRegion> getTorpedoExplodeAnimation() {
        return depthChargeExplode;
    }
}

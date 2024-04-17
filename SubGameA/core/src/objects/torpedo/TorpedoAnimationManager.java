package objects.torpedo;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import utilities.LoadSave;

public class TorpedoAnimationManager {
    private Animation<TextureRegion> torpedoUpAnimation;
    private Animation<TextureRegion> torpedoExplode;

    public TorpedoAnimationManager(Texture texture) {
        loadAnimations(texture);
    }

    private void loadAnimations(Texture texture) {
        TextureRegion[][] torpedoSprites = new TextureRegion[4][8];
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 7; j++) {
                torpedoSprites[i][j] = new TextureRegion(texture, 16 * j, 5 * i, Torpedo.TORPEDO_WIDTH, Torpedo.TORPEDO_HEIGHT);
            }
        }
        torpedoUpAnimation = LoadSave.boatAnimation(0, 8, torpedoSprites, 0.03f);
        torpedoExplode = LoadSave.boatAnimation(1, 1, torpedoSprites, 8.0f);
    }

    public Animation<TextureRegion> getTorpedoUpAnimation() {
        return torpedoUpAnimation;
    }

    public Animation<TextureRegion> getTorpedoExplodeAnimation() {
        return torpedoExplode;
    }
}

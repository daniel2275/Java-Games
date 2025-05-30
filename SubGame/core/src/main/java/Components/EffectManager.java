package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;

public class EffectManager {
    public static ParticleEffect baseHitEffect;
    public static ParticleEffect backgroundBubbles;

    public static void load() {
        baseHitEffect = new ParticleEffect();
        baseHitEffect.load(Gdx.files.internal("particles/smoke.p"), Gdx.files.internal("particles"));
        baseHitEffect.scaleEffect(0.5f);

        backgroundBubbles = new ParticleEffect();
        backgroundBubbles.load(Gdx.files.internal("particles/bubbles.p"), Gdx.files.internal("particles"));
        //backgroundBubbles.scaleEffect(0.5f);
    }
}

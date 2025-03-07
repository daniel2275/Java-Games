package entities.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;

public class EnemyAnimationManager {
    private final TextureRegion[][] boatSprites = new TextureRegion[5][6];
    private Animation<TextureRegion> idleAnimations;
    private Animation<TextureRegion> movingAnimations;
    private Animation<TextureRegion> upAnimations;
    private Animation<TextureRegion> downAnimations;
    private Animation<TextureRegion> hitAnimations;
    private Animation<TextureRegion> sunkAnimations;
    private Animation<TextureRegion> turnAnimations;

    private Animation<TextureRegion> idleAnimationsRight;
    private Animation<TextureRegion> idleAnimationsLeft;
    private Animation<TextureRegion> movingAnimationsRight;

    private Animation<TextureRegion> upAnimationsRight;
    private Animation<TextureRegion> upAnimationsLeft;

    private Animation<TextureRegion> downAnimationsRight;
    private Animation<TextureRegion> downAnimationsLeft;

    private Animation<TextureRegion> movingAnimationsLeft;
    private Animation<TextureRegion> turningAnimationsRight;
    private Animation<TextureRegion> turningAnimationsLeft;
    private Animation<TextureRegion> sunkAnimationsLeft;
    private Animation<TextureRegion> sunkAnimationsRight;
    private Animation<TextureRegion> hitAnimationsRight;
    private Animation<TextureRegion> hitAnimationsLeft;

    private HashMap<String, Animation<TextureRegion>> animations = new HashMap<>();

    private int enemyWidth;
    private int enemyHeight;
    private Enemy enemy;

    public EnemyAnimationManager(Enemy enemy, String sprites) {
        this.enemy = enemy;
        this.enemyWidth = enemy.getEnemyWidth();
        this.enemyHeight = enemy.getEnemyHeight();
        loadAnimations(sprites);
    }

    private void loadAnimations(String sprites) {
  //      Texture boatAtlas = new Texture(sprites);
//
//        for (int i = 0; i <= 4; i++) {
//            for (int j = 0; j <= 4; j++) {
//                boatSprites[i][j] = new TextureRegion(boatAtlas, enemyWidth * j, enemyHeight * i, enemyWidth, enemyHeight);
//            }
//        }
//
//        idleAnimations = boatAnimation(0, 1, boatSprites, 0.2f);
//        movingAnimationsLeft = boatAnimation(0, 5, boatSprites, 0.4f);
//        movingAnimationsRight = boatAnimation(0, 5, boatSprites, 0.4f);
//        upAnimations = boatAnimation(3, 3, boatSprites, 0.7f);
//        downAnimations = boatAnimation(4, 3, boatSprites, 0.7f);
//        hitAnimations = boatAnimation(1, 3, boatSprites, 0.3f);
//        sunkAnimations = boatAnimation(1, 5, boatSprites, 0.5f);
//        turnAnimations = boatAnimation(2, 5, boatSprites, 0.2f);
//
//        if (sprites == "tanker-atlas.png") {
//            TextureAtlas tankerAtlas = new TextureAtlas(Gdx.files.internal("animations/tankeratlas.atlas"));

        TextureAtlas animationAtlas = new TextureAtlas(Gdx.files.internal(sprites));

        idleAnimationsRight = createAnimation(animationAtlas, "idle", 6, 0.5f, true, "LOOP");
        idleAnimationsLeft = createAnimation(animationAtlas, "idle", 6, 0.5f, false, "LOOP");

        movingAnimationsRight = createAnimation(animationAtlas, "mov", 6, 0.5f, true, "LOOP");
        movingAnimationsLeft = createAnimation(animationAtlas, "mov", 6, 0.5f, false, "LOOP");

        upAnimationsRight = createAnimation(animationAtlas, "mov", 6, 0.5f, true, "LOOP");
        upAnimationsLeft = createAnimation(animationAtlas, "mov", 6, 0.5f, false, "LOOP");

        downAnimationsRight = createAnimation(animationAtlas, "mov", 6, 0.5f, true, "LOOP");
        downAnimationsLeft = createAnimation(animationAtlas, "mov", 6, 0.5f, false, "LOOP");

        hitAnimationsRight = createAnimation(animationAtlas, "hit", 1, 0.3f, true, "NORMAL");
        hitAnimationsLeft = createAnimation(animationAtlas, "hit", 1, 0.3f, false, "NORMAL");

        sunkAnimationsRight = createAnimation(animationAtlas, "sunk", 6, 0.5f, true, "NORMAL");
        sunkAnimationsLeft = createAnimation(animationAtlas, "sunk", 6, 0.5f, false, "NORMAL");

        turningAnimationsRight = createAnimation(animationAtlas, "turn", 31, 0.07f, true, "NORMAL");
        turningAnimationsLeft = createAnimation(animationAtlas, "turn", 31, 0.07f, false, "NORMAL");

        addAnimations();
//        }
    }

    private void addAnimations() {
        animations.put("idleRight", idleAnimationsRight);
        animations.put("idleLeft", idleAnimationsLeft);

        animations.put("upRight", upAnimationsRight);
        animations.put("upLeft", upAnimationsLeft);

        animations.put("downRight", upAnimationsRight);
        animations.put("downLeft", idleAnimationsLeft);

        animations.put("hitRight", hitAnimationsRight);
        animations.put("hitLeft", hitAnimationsLeft);

        animations.put("movRight", movingAnimationsRight);
        animations.put("movLeft", movingAnimationsLeft);

        animations.put("turnRight", turningAnimationsRight);
        animations.put("turnLeft", turningAnimationsLeft);

        animations.put("sunkRight", sunkAnimationsRight);
        animations.put("sunkLeft", sunkAnimationsLeft);
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

    public Animation<TextureRegion> getTurnAnimations() {
        return turnAnimations;
    }

    public Animation<TextureRegion> getAnimation(String name) {
        return animations.get(name);
    }
}


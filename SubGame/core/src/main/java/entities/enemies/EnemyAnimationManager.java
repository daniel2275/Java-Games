package entities.enemies;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static utilities.LoadSave.boatAnimation;

public class EnemyAnimationManager {
    private final TextureRegion[][] boatSprites = new TextureRegion[5][6];
    private Animation<TextureRegion> idleAnimations;
    private Animation<TextureRegion> movingAnimations;
    private Animation<TextureRegion> upAnimations;
    private Animation<TextureRegion> downAnimations;
    private Animation<TextureRegion> hitAnimations;
    private Animation<TextureRegion> sunkAnimations;
    private Animation<TextureRegion> turnAnimations;
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
        Texture boatAtlas = new Texture(sprites);

        for (int i = 0; i <= 4; i++) {
            for (int j = 0; j <= 4; j++) {
                boatSprites[i][j] = new TextureRegion(boatAtlas, enemyWidth * j, enemyHeight * i, enemyWidth, enemyHeight);
            }
        }

        idleAnimations = boatAnimation(0, 1, boatSprites, 0.2f);
        movingAnimations = boatAnimation(0, 5, boatSprites, 0.2f);
        upAnimations = boatAnimation(3, 3, boatSprites, 0.7f);
        downAnimations = boatAnimation(4, 3, boatSprites, 0.7f);
        hitAnimations = boatAnimation(1, 3, boatSprites, 0.2f);
        sunkAnimations = boatAnimation(1, 5, boatSprites, 0.5f);
        turnAnimations = boatAnimation(2, 5, boatSprites, 0.2f);
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
}


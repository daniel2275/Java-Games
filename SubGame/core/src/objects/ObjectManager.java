package objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import entities.Enemy;
import gamestates.Playing;
import utilz.Timing;

import java.util.ArrayList;
import java.util.Iterator;

import static com.danielr.subgame.SubGame.pause;
import static objects.Torpedo.TORPEDO_HEIGHT;
import static utilz.Constants.Game.*;

public class ObjectManager {
    private final Playing playing;
    private final ArrayList<Torpedo> torpedoes;
    private ArrayList<DepthCharge> depthCharges;

    private Timing torpedoLoading;

    Sound torpedoHitSound = Gdx.audio.newSound(Gdx.files.internal("audio/BoatTorpHit.mp3"));
    Sound torpedoHitSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/BoatTorpHit1.mp3"));
    Sound depthChargeHit = Gdx.audio.newSound(Gdx.files.internal("audio/depthchargeHit1.mp3"));
    Sound depthChargeFar = Gdx.audio.newSound(Gdx.files.internal("audio/depthChargeFar1.mp3"));

    public ObjectManager(Playing playing) {
        torpedoes = new ArrayList<>();
        depthCharges = new ArrayList<>();
        this.playing = playing;
        torpedoLoading = new Timing(playing.getPlayer().getReloadSpeed());
    }

    public void reset() {
        torpedoes.clear();
        depthCharges.clear();
        torpedoLoading.reset();
        // Reset any other variables or objects that need to be reset to their default values.
    }

    public void fireProjectile() {
        if (!pause && (torpedoLoading.getStartTime() == 0) || !pause && (torpedoLoading.getTimeRemaining() <= 0)) {
            torpedoLoading.init();
            // update timing with reloadSpeed updates
            torpedoLoading.setDuration(playing.getPlayer().getReloadSpeed());
            torpedoes.add(new Torpedo(playing.getPlayer().getuBoatHitBox().getX(), playing.getPlayer().getuBoatHitBox().getY()));
        }
    }

    public void dropCharge(Enemy enemy) {
        if (enemy.deployCharges() && enemy.isAggro() && !enemy.isDying() && !enemy.isSub()) {
            depthCharges.add(new DepthCharge(enemy.getHitbox().getX() + (enemy.getEnemyWidth()/2f), enemy.getHitbox().getY()));
        } else if (enemy.deployCharges() && enemy.isAggro() && !enemy.isDying() && enemy.isSub()) {
            if (checkBounds(enemy)) {
                torpedoes.add(new Torpedo(enemy.getHitbox().getX(), enemy.getHitbox().getY(), true, playing.getPlayer().getHitbox().getX(), playing.getPlayer().getHitbox().getY()));
            }
        }
    }

    public void update() {
        render();
    }

    private void handleTorpedo() {
        Iterator<Torpedo> torpedoIterator = torpedoes.iterator();
        while (torpedoIterator.hasNext()) {
            Torpedo torpedo = torpedoIterator.next();
            if (!checkProjectileLimit(torpedoIterator, torpedo)) {
                if (torpedo.isEnemy()) {
                    if (playing.getPlayer().getHitbox().overlaps(torpedo.getHitbox())) {
                        playing.getPlayer().doHit(torpedo);
                        torpedo.setExplode(true);
                        boolean randomBit = MathUtils.randomBoolean() ? true : false;
                        if (randomBit) {
                            torpedoHitSound.play();
                        } else {
                            torpedoHitSound1.play();
                        }
                        torpedo.update();
                        torpedoIterator.remove();
                    }
                } else {
                    if (playing.checkCollision(torpedo.getHitbox(), torpedo.getTorpedoDamage())) {
                        torpedo.setExplode(true);
                        boolean randomBit = MathUtils.randomBoolean() ? true : false;
                        if (randomBit) {
                            torpedoHitSound.play();
                        } else {
                            torpedoHitSound1.play();
                        }
                        torpedo.update();
                        torpedoIterator.remove();
                    }
                }
            }
            torpedo.update();
        }
    }


    private void handleDeepCharges() {
        Iterator<DepthCharge> depthChargeIterator = depthCharges.iterator();
        while (depthChargeIterator.hasNext()) {
            DepthCharge depthCharge = depthChargeIterator.next();
            if (!checkDpcLimit(depthChargeIterator, depthCharge)) {
                if (playing.getPlayer().getHitbox().overlaps(depthCharge.getHitbox())) {
                    if (!depthCharge.isExplode()) {
                        playing.getPlayer().doHit(depthCharge);
                        depthCharge.setExplode(true);
                        depthChargeHit.play();
                        depthCharge.setSpeed(0);
                    }
                }
            }
            if (depthCharge.getAnimationFinished() && depthCharge.isExplode()) {
                depthChargeIterator.remove();
            }
            depthCharge.update();
        }
    }

    // sets up an iterator with the list of torpedoes, call to check boundaries and manages explosion/removal
    public void render() {
        if (!pause) {
            playing.getPlayer().setReload(torpedoLoading.getTimeRemaining());
            torpedoLoading.checkPaused(false);
            torpedoLoading.update();

            if (torpedoes.size() > 0) {
                handleTorpedo();
            }

            if (depthCharges.size() > 0) {
                handleDeepCharges();
            }
        } else {
            torpedoLoading.checkPaused(true);
        }
    }

    // Check projectile reached the skyline and remove it from the iterator for de-spawn
    public boolean checkProjectileLimit(Iterator<Torpedo> torpedoIterator, Torpedo torpedo) {
        if (torpedo.getHitbox().getY() >= WORLD_HEIGHT - SKY_SIZE - TORPEDO_HEIGHT || torpedo.isAtTarget() || !checkBoundsT(torpedo)) {
            torpedoIterator.remove();
            return true;
        }
        return false;
    }

    public boolean checkDpcLimit(Iterator<DepthCharge> depthChargeIterator, DepthCharge depthCharge) {
        if (depthCharge.getHitbox().getY() <= 0) {
            depthChargeIterator.remove();
            depthChargeFar.play();
            return true;
        }
        return false;
    }

    public ArrayList<Torpedo> getTorpedoes() {
        return torpedoes;
    }

    public ArrayList<DepthCharge> getDepthCharges() {
        return depthCharges;
    }

    private boolean checkBounds(Enemy enemy) {
        return ((enemy.getHitbox().getX() > 0 && enemy.getHitbox().getX() < WORLD_WIDTH) && (enemy.getHitbox().getY() > 0 && enemy.getHitbox().getY() < WORLD_HEIGHT - SKY_SIZE));
    }

    private boolean checkBoundsT(Torpedo enemy) {
        return ((enemy.getHitbox().getX() > 0 && enemy.getHitbox().getX() < WORLD_WIDTH) && (enemy.getHitbox().getY() > 0 && enemy.getHitbox().getY() < WORLD_HEIGHT - SKY_SIZE));
    }

    public void exit () {
        torpedoHitSound.dispose();
        torpedoHitSound1.dispose();
        depthChargeHit.dispose();
        depthChargeFar.dispose();
        for (Torpedo torpedo : torpedoes) {
            torpedo.exit();
        }
        for (DepthCharge depthCharge : depthCharges) {
            depthCharge.exit();
        }
    }

}






package objects;

import com.badlogic.gdx.math.Intersector;
import entities.enemies.Enemy;
import gamestates.GamePlayScreen;
import utilities.Constants;
import utilities.SoundManager;
import utilities.Timing;

import java.util.ArrayList;
import java.util.Iterator;

import static com.mygdx.sub.SubGame.pause;
import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;

public class ObjectManager {
    private final GamePlayScreen gamePlayScreen;
    private final ArrayList<Torpedo> torpedoes;
    private ArrayList<DepthCharge> depthCharges;

    private Timing torpedoLoading;

    private SoundManager soundManager;

    public ObjectManager(GamePlayScreen gamePlayScreen) {
        torpedoes = new ArrayList<>();
        depthCharges = new ArrayList<>();
        this.gamePlayScreen = gamePlayScreen;
        this.soundManager = SoundManager.getInstance(gamePlayScreen.getSubGame());
        torpedoLoading = new Timing(gamePlayScreen.getPlayer().getPlayerActor().getReloadSpeed());
    }

    public void reset() {
        // Reset any other variables or objects that need to be reset to their default values.
        torpedoes.clear();
        depthCharges.clear();
        torpedoLoading.reset();
    }

    public void fireProjectile() {
        if (!pause && (torpedoLoading.getStartTime() == 0) || !pause && (torpedoLoading.getTimeRemaining() <= 0)) {
            torpedoLoading.init();
            // update timing with reloadSpeed updates
            torpedoLoading.setDuration(gamePlayScreen.getPlayer().getPlayerActor().getReloadSpeed());
            torpedoes.add(new Torpedo(gamePlayScreen, gamePlayScreen.getPlayer().getHitbox().getX(), gamePlayScreen.getPlayer().getHitbox().getY()));
            soundManager.playLaunchTorpedoRnd();
         }
    }

    public void dropCharge(Enemy enemy) {
        if (enemy.deployCharges() && enemy.isAggro() && !enemy.isDying() && !enemy.isSub()) {
            depthCharges.add(new DepthCharge(gamePlayScreen,enemy.getEnemyActor().getX() + (enemy.getEnemyWidth()/2f), enemy.getEnemyActor().getY()));
        } else if (enemy.deployCharges() && enemy.isAggro() && !enemy.isDying() && enemy.isSub()) {
            if (checkBounds(enemy)) {
                torpedoes.add(new Torpedo(gamePlayScreen, enemy.getEnemyActor().getX(), enemy.getEnemyActor().getY(), true, gamePlayScreen.getPlayer().getHitbox().getX(), gamePlayScreen.getPlayer().getHitbox().getY()));

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
                    if (Intersector.overlaps(gamePlayScreen.getPlayer().getPlayerActor().getBoundingRectangle(), torpedo.getHitbox())) {
                        gamePlayScreen.getPlayer().getPlayerCollisionDetector().doHit(torpedo);
                        torpedo.setExplode(true);
                        soundManager.playTorpedoHitRnd();
                        torpedo.update();
                        torpedoIterator.remove();
                    }
                } else {
                    if (gamePlayScreen.checkCollision(torpedo.getHitbox(), torpedo.getTorpedoDamage())) {
                        torpedo.setExplode(true);
                        soundManager.playTorpedoHitRnd();
                        torpedo.update();
                        torpedoIterator.remove();
                    }
                }
            }
            torpedo.render();
        }
    }

    private void handleDeepCharges() {
        Iterator<DepthCharge> depthChargeIterator = depthCharges.iterator();
        while (depthChargeIterator.hasNext()) {
            DepthCharge depthCharge = depthChargeIterator.next();
            if (!checkDpcLimit(depthChargeIterator, depthCharge)) {
                if (Intersector.overlaps(gamePlayScreen.getPlayer().getPlayerActor().getBoundingRectangle(), depthCharge.getHitbox())) {
                    if (!depthCharge.isExplode()) {
                        gamePlayScreen.getPlayer().getPlayerCollisionDetector().doHit(depthCharge);
                        depthCharge.setExplode(true);
                        soundManager.playDepthChargeHit();
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
            gamePlayScreen.getPlayer().setReload(torpedoLoading.getTimeRemaining());
            torpedoLoading.checkPaused(false);
            torpedoLoading.update();

            if (!torpedoes.isEmpty()) {
                handleTorpedo();
            }

            if (!depthCharges.isEmpty()) {
                handleDeepCharges();
            }
        } else {
            torpedoLoading.checkPaused(true);
        }
    }

    // Check projectile reached the skyline and remove it from the iterator for de-spawn
    public boolean checkProjectileLimit(Iterator<Torpedo> torpedoIterator, Torpedo torpedo) {
        if (torpedo.getHitbox().getY() >= WORLD_HEIGHT - Constants.Game.SKY_SIZE - Torpedo.TORPEDO_HEIGHT || torpedo.isAtTarget() || !checkBoundsT(torpedo)) {
                torpedoIterator.remove();
                return true;
            }

        return false;
    }

    public boolean checkDpcLimit(Iterator<DepthCharge> depthChargeIterator, DepthCharge depthCharge) {
        if (depthCharge.getHitbox().getY() <= 0) {
            depthChargeIterator.remove();
            soundManager.playDepthChargeFar();
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
        return ((enemy.getEnemyActor().getX() > 0 && enemy.getEnemyActor().getX() < WORLD_WIDTH) && (enemy.getEnemyActor().getY() > 0 && enemy.getEnemyActor().getY() < WORLD_HEIGHT - Constants.Game.SKY_SIZE));
    }

    private boolean checkBoundsT(Torpedo enemy) {
        return ((enemy.getHitbox().getX() > 0 && enemy.getHitbox().getX() < WORLD_WIDTH) && (enemy.getHitbox().getY() > 0 && enemy.getHitbox().getY() < WORLD_HEIGHT - Constants.Game.SKY_SIZE ));
    }

    public void exit () {
        for (Torpedo torpedo : torpedoes) {
            torpedo.exit();
        }
        for (DepthCharge depthCharge : depthCharges) {
            depthCharge.exit();
        }
    }
}
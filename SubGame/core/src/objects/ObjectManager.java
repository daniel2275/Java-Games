package objects;

import entities.Enemy;
import gamestates.Playing;
import utilz.Timing;

import java.util.ArrayList;
import java.util.Iterator;

import static com.danielr.subgame.SubGame.pause;
import static entities.Player.PLAYER_WIDTH;
import static objects.Torpedo.TORPEDO_HEIGHT;
import static utilz.Constants.Game.SKY_SIZE;
import static utilz.Constants.Game.WORLD_HEIGHT;

public class ObjectManager {
    private final Playing playing;
    private final ArrayList<Torpedo> torpedoes;
    private ArrayList<DepthCharge> depthCharges;

    private Timing torpedoLoading;

    public ObjectManager(Playing playing) {
        torpedoes = new ArrayList<>();
        depthCharges = new ArrayList<>();
        this.playing = playing;
        torpedoLoading = new Timing(playing.getPlayer().getReloadSpeed());
    }

    public void fireProjectile() {
        if (!pause && (torpedoLoading.getStartTime() == 0) || !pause && (torpedoLoading.getTimeRemaining() <= 0)) {
                torpedoLoading.init();
                torpedoes.add(new Torpedo(playing.getPlayer().getuBoatHitBox().getX() + PLAYER_WIDTH / 2.0f, playing.getPlayer().getuBoatHitBox().getY(), playing.getPlayer().direction()));
        }
    }

    public void dropCharge(Enemy enemy) {
        if (enemy.deployCharges() && enemy.isAggro() && !enemy.isDying() && !enemy.isSub()) {
            depthCharges.add(new DepthCharge(enemy.getHitbox().getX(), enemy.getHitbox().getY()));
        } else if (enemy.deployCharges() && enemy.isAggro() && !enemy.isDying() && enemy.isSub()) {
           torpedoes.add(new Torpedo(enemy.getHitbox().getX() , enemy.getHitbox().getY(), enemy.getDirection(), true));
        }
    }

    public void update() {
        render();
    }

    // sets up an iterator with the list of torpedoes, call to check boundaries and manages explosion/removal
    public void render() {
        if (!pause) {
            playing.getPlayer().setReload(torpedoLoading.getTimeRemaining());
            torpedoLoading.checkPause(false);
            torpedoLoading.update();

            if (torpedoes.size() > 0) {
                Iterator<Torpedo> torpedoIterator = torpedoes.iterator();
                while (torpedoIterator.hasNext()) {
                    Torpedo torpedo = torpedoIterator.next();
                    if (!checkProjectileLimit(torpedoIterator, torpedo)) {
                        if (torpedo.isEnemy()) {
                            if (playing.getPlayer().getHitbox().overlaps(torpedo.getHitbox())) {
                                playing.getPlayer().setPlayerHealth(playing.getPlayer().getPlayerHealth() - torpedo.getTorpedoDamage());
                                torpedo.setExplode(true);
                                torpedo.update();
                                torpedoIterator.remove();
                            }
                        } else {
                            if (playing.checkCollision(torpedo.getHitbox(), torpedo.getTorpedoDamage())) {
                                torpedo.setExplode(true);
                                torpedo.update();
                                torpedoIterator.remove();
                            }
                        }
                    }
                    torpedo.update();
                }
            }

            if (depthCharges.size() > 0) {
                Iterator<DepthCharge> depthChargeIterator = depthCharges.iterator();
                while (depthChargeIterator.hasNext()) {
                    DepthCharge depthCharge = depthChargeIterator.next();
                    if (!checkDpcLimit(depthChargeIterator, depthCharge)) {
                        if (playing.getPlayer().getHitbox().overlaps(depthCharge.getHitbox())) {
                            if (!depthCharge.isExplode()) {
                                playing.getPlayer().setPlayerHealth(playing.getPlayer().getPlayerHealth() - depthCharge.getDpcDamage());
                            }
                            depthCharge.setExplode(true);
                            depthCharge.setSpeed(0);
                        }
                    }
                    if (depthCharge.getAnimationFinished() && depthCharge.isExplode()) {
                        depthChargeIterator.remove();
                    }
                    depthCharge.update();
                }

            }
        } else {
            torpedoLoading.checkPause(true);
        }
    }

    // Check projectile reached the skyline and remove it from the iterator for de-spawn
    public boolean checkProjectileLimit(Iterator<Torpedo> torpedoIterator, Torpedo torpedo) {
        if (torpedo.getHitbox().getY() >= WORLD_HEIGHT - SKY_SIZE - TORPEDO_HEIGHT) {
            torpedoIterator.remove();
            return true;
        }
        return false;
    }

    public boolean checkDpcLimit(Iterator<DepthCharge> depthChargeIterator, DepthCharge depthCharge) {
        if (depthCharge.getHitbox().getY() <= 0) {
            depthChargeIterator.remove();
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
}






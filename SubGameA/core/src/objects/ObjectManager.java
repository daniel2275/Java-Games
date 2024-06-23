package objects;

import Components.Pausable;
import com.badlogic.gdx.math.Intersector;
import entities.enemies.Enemy;
import UI.game.GameScreen;
import objects.depthChage.DepthCharge;
import objects.torpedo.Torpedo;
import utilities.Constants;
import utilities.SoundManager;
import utilities.Timing;

import java.util.ArrayList;
import java.util.Iterator;

import static com.mygdx.sub.SubGame.pause;
import static utilities.Constants.Game.WORLD_HEIGHT;
import static utilities.Constants.Game.WORLD_WIDTH;

public class ObjectManager implements Pausable {
    private final GameScreen gameScreen;
    private final ArrayList<Torpedo> torpedoes;
    private ArrayList<DepthCharge> depthCharges;

    private Timing torpedoLoading;

    private SoundManager soundManager;

    public ObjectManager(GameScreen gameScreen) {
        torpedoes = new ArrayList<>();
        depthCharges = new ArrayList<>();
        this.gameScreen = gameScreen;
        this.soundManager = SoundManager.getInstance(gameScreen.getSubGame());
        torpedoLoading = new Timing(gameScreen.getPlayer().getPlayerActor().getReloadSpeed());
    }

    public void reset() {
        // Reset any other variables or objects that need to be reset to their default values.
        dispose();
        torpedoes.clear();
        depthCharges.clear();
        torpedoLoading.reset();
    }

    public void fireProjectile() {
        if (!pause && (torpedoLoading.getStartTime() == 0) || !pause && (torpedoLoading.getTimeRemaining() <= 0)) {
            torpedoLoading.start();

            // Get the center coordinates of the PlayerActor's hitbox
            float playerX = gameScreen.getPlayer().getHitbox().getX();
            float playerY = gameScreen.getPlayer().getHitbox().getY();
            float playerWidth = gameScreen.getPlayer().getHitbox().getWidth();
            float playerHeight = gameScreen.getPlayer().getHitbox().getHeight();

            // Calculate the center coordinates
            float playerCenterX = playerX + (playerWidth / 2);
            float playerCenterY = playerY + (playerHeight / 2);


            // update timing with reloadSpeed updates
            torpedoLoading.setDuration(gameScreen.getPlayer().getPlayerActor().getReloadSpeed());

            // Create and add the torpedo using the center coordinates
            torpedoes.add(new Torpedo(gameScreen, playerCenterX, playerCenterY, gameScreen.getPlayer().getPlayerActor().getDamage()));


            soundManager.playLaunchTorpedoRnd();
         }
    }

    public void dropCharge(Enemy enemy) {
        if (enemy.getChargeDeployer().deployAttack() && enemy.isAggro() && !enemy.isDying()) {
            float enemyX = enemy.getEnemyActor().getX();
            float enemyY = enemy.getEnemyActor().getY();

            if (enemy.isSub()) {
                if (checkBounds(enemy)) {
                    float targetX = gameScreen.getPlayer().getPlayerActor().getX();
                    float targetY = gameScreen.getPlayer().getPlayerActor().getY();
                    float torpedoX = enemyX + (enemy.getEnemyWidth() / 2f);
                    float torpedoY = enemyY + (enemy.getEnemyHeight() / 2f);
                    torpedoes.add(new Torpedo(gameScreen, torpedoX, torpedoY, true, targetX, targetY, enemy.getDamage()));
                }
            } else {
                depthCharges.add(new DepthCharge(gameScreen, enemyX, enemyY));
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
                    if (gameScreen.getPlayer().getPlayerActor().collidesWith(torpedo.getTorpedoActor())) {
//                    if (Intersector.overlaps(gameScreen.getPlayer().getPlayerActor().getBoundingRectangle(), torpedo.getTorpedoActor().getBoundingRectangle())) {
                        gameScreen.getPlayer().getPlayerCollisionDetector().doHit(torpedo);
                        torpedo.setExplode(true);
                        soundManager.playTorpedoHitRnd();
                        torpedo.updatePos();
                        torpedo.getTorpedoActor().setCurrentHealth(0);
                        torpedoIterator.remove();
                    }
                } else {
                    if (gameScreen.checkCollision(torpedo.getTorpedoActor(), torpedo.getTorpedoActor().getDamage())) {
                        torpedo.setExplode(true);
                        soundManager.playTorpedoHitRnd();
                        torpedo.updatePos();
                        torpedo.getTorpedoActor().setCurrentHealth(0);
                        torpedoIterator.remove();
                    }
                }
            }
            torpedo.updatePos();
        }
    }

    private void handleDeepCharges() {
        Iterator<DepthCharge> depthChargeIterator = depthCharges.iterator();
        while (depthChargeIterator.hasNext()) {
            DepthCharge depthCharge = depthChargeIterator.next();
            if (!checkDpcLimit(depthChargeIterator, depthCharge)) {
             //   if (Intersector.overlapConvexPolygons(gameScreen.getPlayer().getPlayerActor().getBounding(), depthCharge.getDepthChargeActor().getBounding())) {
                if (Intersector.overlaps(gameScreen.getPlayer().getPlayerActor().getBounding(), depthCharge.getDepthChargeActor().getBounding())) {
                        gameScreen.getPlayer().getPlayerCollisionDetector().doHit(depthCharge);
                        depthCharge.setExplode(true);
                        soundManager.playDepthChargeHit();
                        depthCharge.getDepthChargeActor().setCurrentHealth(0);
                        depthChargeIterator.remove();
                }
            }
            depthCharge.update();
        }
    }

    // sets up an iterator with the list of torpedoes, call to check boundaries and manages explosion/removal
    public void render() {
        if (!pause) {
            gameScreen.getPlayer().setReload(torpedoLoading.getTimeRemaining());
            torpedoLoading.pause(false);
            torpedoLoading.update();

            if (!torpedoes.isEmpty()) {
                handleTorpedo();
            }

            if (!depthCharges.isEmpty()) {
                handleDeepCharges();
            }
        } else {
            torpedoLoading.pause(true);
        }
    }

    // Check projectile reached the skyline and remove it from the iterator for de-spawn
    public boolean checkProjectileLimit(Iterator<Torpedo> torpedoIterator, Torpedo torpedo) {
        if (torpedo.getTorpedoActor().getY() >= WORLD_HEIGHT - Constants.Game.SKY_SIZE - (Torpedo.TORPEDO_HEIGHT - 3) || torpedo.isAtTarget() || !checkBoundsT(torpedo)) {
                torpedo.getTorpedoActor().remove();
                torpedoIterator.remove();
                return true;
            }

        return false;
    }

    public boolean checkDpcLimit(Iterator<DepthCharge> depthChargeIterator, DepthCharge depthCharge) {
        if (depthCharge.getDepthChargeActor().getY() <= 0) {
            soundManager.playDepthChargeFar();
            depthCharge.getDepthChargeActor().remove();
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

    private boolean checkBounds(Enemy enemy) {
        return ((enemy.getEnemyActor().getX() > 0 && enemy.getEnemyActor().getX() < WORLD_WIDTH) && (enemy.getEnemyActor().getY() > 0 && enemy.getEnemyActor().getY() < WORLD_HEIGHT - Constants.Game.SKY_SIZE));
    }

    private boolean checkBoundsT(Torpedo enemyTorpedo) {
        return ((enemyTorpedo.getTorpedoActor().getX() > 0 && enemyTorpedo.getTorpedoActor().getX() < WORLD_WIDTH) && (enemyTorpedo.getTorpedoActor().getY() > 0 && enemyTorpedo.getTorpedoActor().getY() < WORLD_HEIGHT - Constants.Game.SKY_SIZE ));
    }

    public void dispose() {
        for (DepthCharge depthCharge : depthCharges) {
            depthCharge.getDepthChargeActor().remove();
        }
        for (Torpedo torpedo : torpedoes) {
            torpedo.getTorpedoActor().remove();
        }
    }


    @Override
    public void setPaused(boolean paused) {
        this.torpedoLoading.pause(paused);
    }



}
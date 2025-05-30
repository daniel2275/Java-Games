package objects;

import Components.Pausable;
import UI.game.GameScreen;
import com.badlogic.gdx.math.Intersector;
import entities.enemies.Enemy;
import objects.depthChage.DepthCharge;
import objects.mine.Mine;
import objects.torpedo.Torpedo;
import utilities.Settings;
import utilities.SoundManager;
import utilities.Timing;

import java.util.ArrayList;
import java.util.Iterator;

import static io.github.daniel2275.subgame.SubGame.pause;
import static utilities.Settings.Game.VIRTUAL_HEIGHT;
import static utilities.Settings.Game.VIRTUAL_WIDTH;

public class ObjectManager implements Pausable {
    private final GameScreen gameScreen;
    private final ArrayList<Torpedo> torpedoes;
    private ArrayList<DepthCharge> depthCharges;
    private ArrayList<Mine> mines;

    private Timing torpedoLoading;

    private SoundManager soundManager;

    public ObjectManager(GameScreen gameScreen) {
        torpedoes = new ArrayList<>();
        depthCharges = new ArrayList<>();
        mines = new ArrayList<>();
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
        mines.clear();
    }

    public void fireProjectile(float x, float y) {
        if (!pause && (torpedoLoading.getStartTime() == 0) || !pause && (torpedoLoading.getTimeRemaining() <= 0)) {
            torpedoLoading.start();
            // reset reload flag for complete sound
            gameScreen.getPlayer().getPlayerActor().startReload();

            // Get the center coordinates of the PlayerActor's hitbox
            float playerX = gameScreen.getPlayer().getPlayerActor().getX();
            float playerY = gameScreen.getPlayer().getPlayerActor().getY();
            float playerWidth = gameScreen.getPlayer().getPlayerActor().getWidth();
            float playerHeight = gameScreen.getPlayer().getPlayerActor().getHeight();

            // Calculate the center coordinates
            float playerCenterX = playerX + (playerWidth / 2f);
            float playerCenterY = playerY + (playerHeight / 2f);


            // update timing with reloadSpeed updates
            torpedoLoading.setDuration(gameScreen.getPlayer().getPlayerActor().getReloadSpeed());

            // Create and add the torpedo using the center coordinates
            torpedoes.add(new Torpedo(gameScreen, playerCenterX, playerCenterY, gameScreen.getPlayer().getPlayerActor().getDamage(), x, y));

            soundManager.playLaunchTorpedoRnd();
        }
    }

    // handles bullet creation for all entities
    public void enemyAttack(Enemy enemy) {
        if (enemy.getBulletControl().deployAttack() && (enemy.isAggro() || enemy.getName() == "mini") && !enemy.isDying()) {
            float enemyX = enemy.getEnemyActor().getX();
            float enemyY = enemy.getEnemyActor().getY();

            if (enemy.isSub()) {
                if (checkBounds(enemy)) {
                    if (enemy.getName() == "mini") {

                        System.out.println("new mine " + mines.size());
                       Mine newMine = new Mine(gameScreen, enemyX, enemyY, true, (int) enemy.getEnemyDamage());
                        mines.add(newMine);
                    } else {
                        Torpedo newTorpedo = createEnemyTorpedo(enemy, enemyX, enemyY);
                        torpedoes.add(newTorpedo);
                    }
                }
            } else {
                DepthCharge newDepthCharge = new DepthCharge(gameScreen, enemyX, enemyY);
                newDepthCharge.setMaxDistance(enemy.getOrdinanceRange());
                depthCharges.add(newDepthCharge);
            }
        }
    }

    private Torpedo createEnemyTorpedo(Enemy enemy, float enemyX, float enemyY) {
        float targetX = gameScreen.getPlayer().getPlayerActor().getX();
        float targetY = gameScreen.getPlayer().getPlayerActor().getY();
        float torpedoX = enemyX + (enemy.getEnemyWidth() / 2f);
        float torpedoY = enemyY + (enemy.getEnemyHeight() / 2f);
        Torpedo newTorpedo = new Torpedo(gameScreen, torpedoX, torpedoY, true, targetX, targetY, enemy.getEnemyDamage());
        newTorpedo.setMaxDistance(enemy.getOrdinanceRange());
        return newTorpedo;
    }

    public void update() {
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

            if (!mines.isEmpty()) {
                handleMines();
            }

        } else {
            torpedoLoading.pause(true);
        }
    }

    private void handleTorpedo() {
        Iterator<Torpedo> torpedoIterator = torpedoes.iterator();
        while (torpedoIterator.hasNext()) {
            Torpedo torpedo = torpedoIterator.next();
            if (!checkProjectileLimit(torpedoIterator, torpedo)) {
                if (torpedo.isEnemy()) {
                    if (gameScreen.getPlayer().getPlayerActor().collidesWith(torpedo.getTorpedoActor())) {
                        gameScreen.getPlayer().getPlayerCollisionDetector().playerHit(torpedo);
                        torpedo.setExplode(true);
                        soundManager.playTorpedoHitRnd();
                        torpedo.updatePos();
                        torpedo.getTorpedoActor().setCurrentHealth(0);
                        torpedoIterator.remove();
                    }
                } else {
                    if (gameScreen.getEnemyManager().checkCollision(torpedo.getTorpedoActor(), torpedo.getTorpedoActor().getDamage())) {
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
                if (Intersector.overlaps(gameScreen.getPlayer().getPlayerActor().getBounding(), depthCharge.getDepthChargeActor().getBounding())) {
                    gameScreen.getPlayer().getPlayerCollisionDetector().playerHit(depthCharge);
                    depthCharge.setExplode(true);
                    soundManager.playDepthChargeHit();
                    depthCharge.getDepthChargeActor().setCurrentHealth(0);
                    depthChargeIterator.remove();
                }
            }
            depthCharge.update();
        }
    }

    private void handleMines() {
        Iterator<Mine> mineIterator = mines.iterator();
        while (mineIterator.hasNext()) {
            Mine mine = mineIterator.next();
            if (mine.isEnemy()) {
                if (gameScreen.getPlayer().getPlayerActor().collidesWith(mine.getMineActor())) {
                    gameScreen.getPlayer().getPlayerCollisionDetector().playerHit(mine);
                    mine.setExplode(true);
                    soundManager.playTorpedoHitRnd();
                    mine.updatePos();
                    mine.getMineActor().setCurrentHealth(0);
                    mineIterator.remove();
                }
            } else {
                if (gameScreen.getEnemyManager().checkCollision(mine.getMineActor(), mine.getMineActor().getDamage())) {
                    mine.setExplode(true);
                    soundManager.playTorpedoHitRnd();
                    mine.updatePos();
                    mine.getMineActor().setCurrentHealth(0);
                    mineIterator.remove();
                }
            }
            mine.updatePos();
        }
    }


    // Check projectile reached the skyline and remove it from the iterator for de-spawn
    public boolean checkProjectileLimit(Iterator<Torpedo> torpedoIterator, Torpedo torpedo) {
        if (torpedo == null || torpedoIterator == null || torpedo.getTorpedoActor() == null) {
            return false;
        }

        float distanceTraveled = (float) Math.sqrt(Math.pow(torpedo.getTorpedoActor().getX() - torpedo.getStartX(), 2) +
            Math.pow(torpedo.getTorpedoActor().getY() - torpedo.getStartY(), 2));

        if (distanceTraveled >= torpedo.getMaxDistance() || torpedo.getTorpedoActor().getY() >= VIRTUAL_HEIGHT - Settings.Game.SKY_SIZE - (Torpedo.TORPEDO_HEIGHT - 3) || torpedo.isAtTarget() || !checkBoundsT(torpedo)) {
            soundManager.playDepthChargeFar();
            torpedo.setExplode(true);
            torpedo.updatePos();
            torpedo.getTorpedoActor().setCurrentHealth(0);
            torpedoIterator.remove();

            //torpedo.getTorpedoActor().remove();
            //torpedoIterator.remove();
            return true;
        }

        return false;
    }

    public boolean checkDpcLimit(Iterator<DepthCharge> depthChargeIterator, DepthCharge depthCharge) {
        float distanceTraveled = Math.abs(depthCharge.getDepthChargeActor().getY() - depthCharge.getStartY());

        if (distanceTraveled >= depthCharge.getMaxDistance() || depthCharge.getDepthChargeActor().getY() <= 0) {

            depthCharge.setExplode(true);
            soundManager.playDepthChargeFar();
            depthCharge.getDepthChargeActor().setCurrentHealth(0);
            depthChargeIterator.remove();

//            depthCharge.getDepthChargeActor().remove();
            //          depthChargeIterator.remove();
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
        return ((enemy.getEnemyActor().getX() > 0 && enemy.getEnemyActor().getX() < VIRTUAL_WIDTH) && (enemy.getEnemyActor().getY() > 0 && enemy.getEnemyActor().getY() < VIRTUAL_HEIGHT - Settings.Game.SKY_SIZE));
    }

    private boolean checkBoundsT(Torpedo enemyTorpedo) {
        return ((enemyTorpedo.getTorpedoActor().getX() > 0 && enemyTorpedo.getTorpedoActor().getX() < VIRTUAL_WIDTH) && (enemyTorpedo.getTorpedoActor().getY() > 0 && enemyTorpedo.getTorpedoActor().getY() < VIRTUAL_HEIGHT - Settings.Game.SKY_SIZE));
    }

    public void dispose() {
        for (DepthCharge depthCharge : depthCharges) {
            if (depthCharge != null) {
                depthCharge.dispose();
            }
        }

        for (Torpedo torpedo : torpedoes) {
            if (torpedo != null) {
                torpedo.dispose();
            }
        }

        for (Mine mine : mines) {
            if (mine != null) {
                mine.dispose();
            }
        }

        torpedoes.clear();
        depthCharges.clear();
        mines.clear();
    }



    @Override
    public void setPaused(boolean paused) {
        this.torpedoLoading.pause(paused);
    }


}

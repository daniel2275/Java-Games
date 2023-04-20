package objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Rectangle;
import entities.Enemy;
import gamestates.Playing;
import utilz.Timing;

import java.util.ArrayList;
import java.util.Iterator;

import static com.danielr.subgame.SubGame.pause;
import static com.danielr.subgame.SubGame.shapeRendered;
import static objects.Torpedo.TORPEDO_HEIGHT;
import static utilz.Constants.Game.*;

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

            torpedoes.add(new Torpedo(playing.getPlayer().getuBoatHitBox().getX() , playing.getPlayer().getuBoatHitBox().getY() ));
        }
    }

    public void dropCharge(Enemy enemy) {
        if (enemy.deployCharges() && enemy.isAggro() && !enemy.isDying() && !enemy.isSub()) {
            depthCharges.add(new DepthCharge(enemy.getHitbox().getX(), enemy.getHitbox().getY()));
        } else if (enemy.deployCharges() && enemy.isAggro() && !enemy.isDying() && enemy.isSub()) {
            if (checkBounds(enemy)) {
                torpedoes.add(new Torpedo(enemy.getHitbox().getX(), enemy.getHitbox().getY(), true, playing.getPlayer().getHitbox().getX(), playing.getPlayer().getHitbox().getY()));
            }
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
                                playing.getPlayer().doHit(torpedo);
//                                System.out.println("player health "  + playing.getPlayer().getPlayerHealth());
//                                System.out.println("Torpedo damage " + torpedo.getTorpedoDamage());
//                                playing.getPlayer().setPlayerHealth(playing.getPlayer().getPlayerHealth() - torpedo.getTorpedoDamage());
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
                                playing.getPlayer().doHit(depthCharge);
//                                playing.getPlayer().setPlayerHealth(playing.getPlayer().getPlayerHealth() - depthCharge.getDpcDamage());
                                depthCharge.setExplode(true);
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
        } else {
            torpedoLoading.checkPause(true);
        }
    }

    // Check projectile reached the skyline and remove it from the iterator for de-spawn
    public boolean checkProjectileLimit(Iterator<Torpedo> torpedoIterator, Torpedo torpedo) {
//        System.out.println(torpedoes.size());
        if (torpedo.getHitbox().getY() >= WORLD_HEIGHT - SKY_SIZE - TORPEDO_HEIGHT || torpedo.isAtTarget() || !checkBoundsT(torpedo)) {
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

    private boolean checkBounds(Enemy enemy) {
        return ((enemy.getHitbox().getX() > 0 && enemy.getHitbox().getX() < WORLD_WIDTH) && (enemy.getHitbox().getY() > 0 && enemy.getHitbox().getY() < WORLD_HEIGHT - SKY_SIZE));
    }

    private boolean checkBoundsT(Torpedo enemy) {
        return ((enemy.getHitbox().getX() > 0 && enemy.getHitbox().getX() < WORLD_WIDTH) && (enemy.getHitbox().getY() > 0 && enemy.getHitbox().getY() < WORLD_HEIGHT - SKY_SIZE));
    }


    public boolean hitDetect(Rectangle rect1, float angle1, Rectangle rect2, float angle2) {
        System.out.println(angle2);
        // calculate vertices of rect1
        float[] vertices1 = new float[]{
                rect1.x, rect1.y,
                rect1.x + rect1.width, rect1.y,
                rect1.x + rect1.width, rect1.y + rect1.height,
                rect1.x, rect1.y + rect1.height
        };
        Polygon poly1 = new Polygon(vertices1);
        poly1.setOrigin(rect1.width / 2, rect1.height / 2);
//        poly1.setRotation(angle1);

        // calculate vertices of rect2
        float[] vertices2 = new float[]{
                rect2.x, rect2.y,
                rect2.x + rect2.width, rect2.y,
                rect2.x + rect2.width, rect2.y + rect2.height,
                rect2.x, rect2.y + rect2.height
        };
        Polygon poly2 = new Polygon(vertices2);
        poly2.setOrigin(rect2.width / 2, rect2.height / 2);
//        poly2.setRotation(angle2);

        shapeRendered.begin(ShapeRenderer.ShapeType.Filled);
        shapeRendered.setColor(Color.RED);
        shapeRendered.polygon(poly1.getVertices());
        shapeRendered.setColor(Color.BLUE);
        shapeRendered.polygon(poly2.getVertices());
        shapeRendered.end();
// check for collision
        if (Intersector.overlapConvexPolygons(poly1, poly2)) {
            // handle collision
            System.out.println( " Intersect polys ");
            return true;
        }
        System.out.println(" NO HIT polys ");
        return false;
    }


    public static boolean isSkewedRectangleColliding(Rectangle r2, float skewAngle, Rectangle r1) {
        // Transform r1 into an axis-aligned rectangle
        float[] cornerX = {r1.x, r1.x + r1.width, r1.x, r1.x + r1.width};
        float[] cornerY = {r1.y, r1.y, r1.y + r1.height, r1.y + r1.height};
        float centerX = r1.x + r1.width / 2;
        float centerY = r1.y + r1.height / 2;
        for (int i = 0; i < 4; i++) {
            float tempX = cornerX[i] - centerX;
            float tempY = cornerY[i] - centerY;
            float cos = (float) Math.cos(skewAngle);
            float sin = (float) Math.sin(skewAngle);
            cornerX[i] = tempX * cos - tempY * sin + centerX;
            cornerY[i] = tempX * sin + tempY * cos + centerY;
        }
        float minX = cornerX[0];
        float maxX = cornerX[0];
        float minY = cornerY[0];
        float maxY = cornerY[0];
        for (int i = 1; i < 4; i++) {
            if (cornerX[i] < minX) {
                minX = cornerX[i];
            }
            if (cornerX[i] > maxX) {
                maxX = cornerX[i];
            }
            if (cornerY[i] < minY) {
                minY = cornerY[i];
            }
            if (cornerY[i] > maxY) {
                maxY = cornerY[i];
            }
        }
        Rectangle axisAlignedRect = new Rectangle(minX, minY, maxX - minX, maxY - minY);

        shapeRendered.begin(ShapeRenderer.ShapeType.Filled);
        shapeRendered.setColor(Color.RED);
        shapeRendered.rect(axisAlignedRect.getX(),axisAlignedRect.getY(),axisAlignedRect.getWidth(),axisAlignedRect.getHeight());
        shapeRendered.end();


        // Check for collision with r2
        if (axisAlignedRect.overlaps(r2)) {
            System.out.println(axisAlignedRect.overlaps(r2));
        }
        return axisAlignedRect.overlaps(r2);
    }
}






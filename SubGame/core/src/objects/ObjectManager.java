package objects;

import gamestates.Playing;

import java.util.ArrayList;
import java.util.Iterator;

import static utilz.Constants.Game.*;

public class ObjectManager {
    private Playing playing;
    private ArrayList<Torpedo> torpedoes;

    public ObjectManager(Playing playing ) {
        torpedoes = new ArrayList<>();
        this.playing = playing;
    }

    public void fireProjectile() {
        torpedoes.add(new Torpedo(playing.getPlayer().getuBoatHitBox().getX() , playing.getPlayer().getuBoatHitBox().getY()));
    }

    public void update() {
        render();
    }


    public void render() {
        if (torpedoes != null) {
            if (torpedoes.size() > 0) {
                Iterator<Torpedo> torpedoIterator = torpedoes.iterator();
                while (torpedoIterator.hasNext()) {
                    Torpedo torpedo = torpedoIterator.next();
                    torpedo.update();
                    checkProjectileLimit(torpedoIterator, torpedo);
                    playing.checkCollision(torpedo.getHitbox());
                }
            }
        }
    }

    public void checkProjectileLimit(Iterator<Torpedo> torpedoIterator, Torpedo torpedo) {
        System.out.println(torpedo.getHitbox().getY());

        if (torpedo.getHitbox().getY() >= WORLD_HEIGHT - SKY_SIZE) {
            torpedoIterator.remove();
        }
    }
}





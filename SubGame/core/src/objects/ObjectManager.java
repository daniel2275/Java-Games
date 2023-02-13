package objects;

import gamestates.Playing;

import java.util.ArrayList;
import java.util.Iterator;

import static com.danielr.subgame.SubGame.pause;
import static entities.Player.PLAYER_WIDTH;
import static objects.Torpedo.TORPEDO_HEIGHT;
import static utilz.Constants.Game.*;

public class ObjectManager {
    private final Playing playing;
    private final ArrayList<Torpedo> torpedoes;

    public ObjectManager(Playing playing) {
        torpedoes = new ArrayList<>();
        this.playing = playing;
    }

    public void fireProjectile() {
        if (!pause) {
            torpedoes.add(new Torpedo(playing.getPlayer().getuBoatHitBox().getX() + PLAYER_WIDTH / 2.0f, playing.getPlayer().getuBoatHitBox().getY()));
        }
    }

    public void update() {
        render();

    }

    // sets up an iterator with the list of torpedoes, call to check boundaries and manages explosion/removal
    public void render() {
        if (torpedoes.size() > 0) {
            Iterator<Torpedo> torpedoIterator = torpedoes.iterator();
            while (torpedoIterator.hasNext()) {
                Torpedo torpedo = torpedoIterator.next();
                torpedo.update();
                if (!checkProjectileLimit(torpedoIterator, torpedo)) {
                    if (playing.checkCollision(torpedo.getHitbox(), torpedo.getTorpedoDamage())) {
                        torpedo.setExplode(true);
                        torpedo.update();
                        torpedoIterator.remove();
                    }
                }
            }
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



}





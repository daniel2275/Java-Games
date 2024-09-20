package utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.MathUtils;
import io.github.daniel2275.subgame.SubGame;

import java.util.Random;

public class SoundManager {
    private static SoundManager instance;

    private SubGame subGame;

    private Sound torpedoHitSound;
    private Sound torpedoHitSound1;
    private Sound depthChargeHit;
    private Sound depthChargeFar;
    private Sound explodedShort;
    private Sound launchTorpedo;
    private Sound launchTorpedo1;
    private Sound explosion;
    private Sound ping;
    private Sound ping2;
    private Sound torpedoMiss;
    private Sound detected;
    private Sound check;

    private float volume;

    private SoundManager(SubGame subGame) {
    torpedoHitSound = Gdx.audio.newSound(Gdx.files.internal("audio/BoatTorpHit.mp3"));
    torpedoHitSound1 = Gdx.audio.newSound(Gdx.files.internal("audio/BoatTorpHit1.mp3"));
    depthChargeHit = Gdx.audio.newSound(Gdx.files.internal("audio/depthchargehit1.mp3"));
    depthChargeFar = Gdx.audio.newSound(Gdx.files.internal("audio/depthchargefar1.mp3"));
    explodedShort = Gdx.audio.newSound(Gdx.files.internal("audio/explodedshort.mp3"));
    launchTorpedo = Gdx.audio.newSound(Gdx.files.internal("audio/launchtorpedo.mp3"));
    launchTorpedo1 = Gdx.audio.newSound(Gdx.files.internal("audio/launchtorpedo1.mp3"));
    explosion = Gdx.audio.newSound(Gdx.files.internal("audio/explosion.mp3"));
    //ping = Gdx.audio.newSound(Gdx.files.internal("audio/ping.mp3"));
    //ping2 = Gdx.audio.newSound(Gdx.files.internal("audio/ping2.mp3"));
    torpedoMiss = Gdx.audio.newSound(Gdx.files.internal("audio/torpedomiss.mp3"));
    check = Gdx.audio.newSound(Gdx.files.internal("audio/check.mp3"));
    detected = Gdx.audio.newSound(Gdx.files.internal("audio/detected.mp3"));

    this.subGame = subGame;


}

//public static synchronized SoundManager getInstance(SubGame subGame) {
//        if (instance == null) {
//            instance = new SoundManager(subGame);
//        }
//        return instance;
//}

    public static synchronized SoundManager getInstance(SubGame subGame) {
        if (instance == null) {
            instance = new SoundManager(subGame);
        }
        return instance;
    }

    public static SoundManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("SoundManager not initialized. Call getInstance(SubGame subGame) first.");
        }
        return instance;
    }


    public void playTorpedoHit() {
        torpedoHitSound.play(subGame.getOptions().getVolume());
    }

    public void playTorpedoHit1() {
        torpedoHitSound1.play(subGame.getOptions().getVolume());
    }

    public void playDepthChargeHit() {
        depthChargeHit.play(subGame.getOptions().getVolume());
    }

    public void playDepthChargeFar() {
        depthChargeFar.play(subGame.getOptions().getVolume());
    }

    public void playExplode() {
        explodedShort.play(subGame.getOptions().getVolume());
    }

    public void playExplode1(){
        explosion.play(subGame.getOptions().getVolume());
    }

    public void detected(){
        detected.play(subGame.getOptions().getVolume());
    }


    public void launchTorpedo(){
        launchTorpedo.play(subGame.getOptions().getVolume());
    }

    public void launchTorpedo1(){
        launchTorpedo1.play(subGame.getOptions().getVolume());
    }

    public void ping(){
        ping.play(subGame.getOptions().getVolume());
    }

    public void ping2(){
        ping2.play(subGame.getOptions().getVolume());
    }

    public void torpedoMiss(){
        torpedoMiss.play(subGame.getOptions().getVolume());
    }

    public void check(){
        check.play(subGame.getOptions().getVolume());
    }



    public void playTorpedoHitRnd() {
        boolean randomBit = MathUtils.randomBoolean() ? true : false;
        if (randomBit) {
            playTorpedoHit();
        } else {
            playTorpedoHit1();
        }
    }

    public void playLaunchTorpedoRnd() {
        Random rnd = new Random();
        int rndSoundNo = rnd.nextInt(3);

        switch ( rndSoundNo ) {
            case 0:
                launchTorpedo();
                break;
            case 1:
                launchTorpedo1();
                break;
            case 2:
                torpedoMiss();
                break;
        }
    }

    public void sunkRnd() {
        boolean randomBit = MathUtils.randomBoolean() ? true : false;
        if (randomBit) {
            playExplode();
        } else {
            playExplode1();
        }
    }

    public void exit() {
        torpedoHitSound.dispose();
        torpedoHitSound1.dispose();
        depthChargeHit.dispose();
        depthChargeFar.dispose();
        explodedShort.dispose();
        explosion.dispose();
        launchTorpedo.dispose();
        launchTorpedo1.dispose();
        ping.dispose();
        ping2.dispose();
        torpedoMiss.dispose();
    }
}

package utilities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class LoadSave {


    public static void loadBinary() {

        Pixmap pixmap = new Pixmap(Gdx.files.internal("lvls/level1.png"));

        int color = pixmap.getPixel(0, 0);

        int red = color >>> 24;
        int green = (color & 0xFF0000) >>> 16;
        int blue = (color & 0xFF00) >>> 8;
        int alpha = color & 0xFF;

    }


//    public static BufferedImage[] GetAllLevels(){
//        URL url = LoadSave.class.getResource("/lvls");
//        File file = null;
//
//        try {
//            file = new File(url.toURI());
//        } catch (URISyntaxException e) {
//            e.printStackTrace();
//        }
//
//        File[] files = file.listFiles();
//        File[] filesSorted = new File[files.length];
//
//        for(int i=0; i < filesSorted.length; i++) {
//            for (int j=0; j < files.length; j++) {
//                if (files[j].getName().equals("level" + (i + 1) + ".png")){
//                    filesSorted[i] = files[j];
//                }
//
//            }
//        }
//
//        BufferedImage[] imgs = new BufferedImage[filesSorted.length];
//
//        for (int i = 0; i < imgs.length; i++ ) {
//            try {
//
//                imgs[i] = ImageIO.read(filesSorted[i]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
//        return imgs;
//    }


    public static Animation<TextureRegion> boatAnimation(int file, int sprites, TextureRegion[][] boatSprites, float frameDuration) {
        TextureRegion animation[] = new TextureRegion[sprites];
        for (int i = 0; i < sprites; i++) {
            animation[i] = boatSprites[file][i];
        }
        Animation animations = new Animation<TextureRegion>(frameDuration, animation);
        return animations;
    }

    public static Animation<TextureRegion> invertHorizontal(Animation<TextureRegion> originalAnimation) {
        TextureRegion[] originalFrames = originalAnimation.getKeyFrames();
        int frameCount = originalFrames.length;
        TextureRegion[] flippedFrames = new TextureRegion[frameCount];

        // Clone the original frames and flip each one horizontally
        for (int i = 0; i < frameCount; i++) {
            TextureRegion originalFrame = originalFrames[i];
            TextureRegion flippedFrame = new TextureRegion(originalFrame); // Clone the original frame
            flippedFrame.flip(true, false); // Flip horizontally
            flippedFrames[i] = flippedFrame;
        }
        // Create the inverted animation using the flipped frames
        return new Animation<>(originalAnimation.getFrameDuration(), flippedFrames);
    }

//    public static void iterateEnemies(Iterator<Enemy> enemyIterator, Player player) {
//        while (enemyIterator.hasNext()) {
//            Enemy enemy = enemyIterator.next();
//
//            enemy.update(player);
//
//        }
//    }

//    public static void removeEnemy(Iterator<Enemy> enemyIterator, Enemy enemyToRemove) {
//        while (enemyIterator.hasNext()) {
//            Enemy enemy =  enemyIterator.next();
//            if (enemy.equals(enemyToRemove)){
//                enemyIterator.remove();
//            }
//        }
//    }

//    public static Rectangle initHitBox(float x,float y,int width,int height) {
//        return new Rectangle(x,y,width,height);
//    }
//
//    public static Rectangle updateHitbox(Rectangle hitbox, float x,float y) {
//        hitbox.x = x;
//        hitbox.y = y;
//        return hitbox;
//    }


}

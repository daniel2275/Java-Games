
package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class BackgroundActor extends Actor implements Pausable {
    private Animation<TextureRegion> animation;
    private float stateTime;
    private float playerX;
    private boolean paused = false;
    //private EnemyManager enemyManager;

    private FrameBuffer fbo;
    private String vertexShader;
    private String fragmentShader;
    private ShaderProgram shaderProgram;
    private boolean waves;
    private TextureRegion fboRegion;

    public BackgroundActor(Animation<TextureRegion> animation, float x, float y, boolean waves) {
        //this.enemyManager = enemyManager;
        this.animation = animation;
        this.stateTime = 0f;
        this.waves = waves;

        // Set initial position
        // setPosition(x, y);

        if (waves) {
            initializeWaveEffect();
        }
    }

    private void initializeWaveEffect() {
        vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
        fragmentShader = Gdx.files.internal("shaders/wavey.glsl").readString();
        shaderProgram = new ShaderProgram(vertexShader, fragmentShader);

        shaderProgram.begin();
        shaderProgram.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shaderProgram.setUniformf("u_alphaValue", 0.4f);
        shaderProgram.end();

        if (fbo == null) {
            fbo = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
            fboRegion = new TextureRegion(fbo.getColorBufferTexture()); // Reuse this TextureRegion
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Get the current frame of the animation
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        batch.end();
        batch.flush();

        if (waves) {
            // Ensure the framebuffer and shader are initialized
            initializeWaveEffect();

            fbo.begin();
            batch.begin();
            super.draw(batch, parentAlpha);
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
            batch.end();
            batch.flush();
            fbo.end();

            // Update the framebuffer texture in case it changed
            fboRegion.setTexture(fbo.getColorBufferTexture());

            // Apply shader and draw the framebuffer texture with the shader effect
            batch.begin();
            batch.setShader(shaderProgram);
            shaderProgram.setUniformf("u_time", stateTime);

            // Draw the texture region from the frame buffer (with the shader effect applied)
            batch.draw(fboRegion, getX(), getY(), getWidth(), getHeight());

            batch.setShader(null);  // Reset shader
        } else {
            batch.begin();
            super.draw(batch, parentAlpha);
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public void act(float delta) {
        if (paused) return;
        stateTime += delta;
        super.act(delta);
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void resize(int width, int height) {
        if (fbo != null && (fbo.getWidth() != width || fbo.getHeight() != height)) {
            fbo.dispose();
            fbo = new FrameBuffer(Pixmap.Format.RGB888, width, height, true);
        }

        // Only update the shader resolution if the size has changed
        if (waves && shaderProgram != null) {
            shaderProgram.begin();
            shaderProgram.setUniformf("u_resolution", width, height);
            shaderProgram.end();
        }
    }

}



package Components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
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

    public BackgroundActor(Animation<TextureRegion> animation, float x, float y, boolean waves) {
        //this.enemyManager = enemyManager;
        this.animation = animation;
        this.stateTime = 0f;
        this.waves = waves;

        // Set initial position
       // setPosition(x, y);

        if (waves) {
            vertexShader = Gdx.files.internal("shaders/vertex.glsl").readString();
            fragmentShader = Gdx.files.internal("shaders/wavey.glsl").readString();
            shaderProgram = new ShaderProgram(vertexShader, fragmentShader);

            fbo = new FrameBuffer(Pixmap.Format.RGB888, Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        // Get the current frame of the animation
        TextureRegion currentFrame = animation.getKeyFrame(stateTime, true);

        batch.end();
        batch.flush();

        if (waves) {
            fbo.begin();
            batch.begin();
            super.draw(batch, parentAlpha);
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
            batch.end();
            batch.flush();
            fbo.end();

            batch.begin();
            batch.setShader(shaderProgram);

            shaderProgram.setUniformf("u_time", stateTime);
            shaderProgram.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shaderProgram.setUniformf("u_alphaValue", 0.5f);

            Texture texture = fbo.getColorBufferTexture();
            TextureRegion textureRegion = new TextureRegion(texture);

            // Draw the texture region from the frame buffer (with the shader effect applied)
            batch.draw(textureRegion, getX(), getY(), getWidth(), getHeight());

            // Reset the shader
            batch.setShader(null);
        } else {
            batch.begin();
            super.draw(batch, parentAlpha);
            batch.draw(currentFrame, getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public void act(float delta) {
        if (!paused) {
            super.act(delta);
            stateTime += delta;
        }
    }

    @Override
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public Animation<TextureRegion> getAnimation() {
        return animation;
    }

    public void resize(int width, int height) {
        // Dispose of the old frame buffer to prevent memory leaks
        if (fbo != null) {
            fbo.dispose();
        }

        // Recreate the frame buffer with the new screen dimensions
        fbo = new FrameBuffer(Pixmap.Format.RGB888, width, height, true);

        // Update the shader resolution uniform with the new screen dimensions
        if (waves && shaderProgram != null) {
            shaderProgram.begin();
            shaderProgram.setUniformf("u_resolution", width, height);
            shaderProgram.end();
        }
    }


}


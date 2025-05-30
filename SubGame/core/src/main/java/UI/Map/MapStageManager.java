package UI.Map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.daniel2275.subgame.SubGame;

import java.util.Map;

public class MapStageManager {
    private Stage mapStage;
    private SubGame subGame;
    private Texture mapTexture;
    private Texture buttonTexture;
    private TextureAtlas buttonAtlas;
    private float frameDuration = 1f / 16f; // 16 FPS for animation
    private Map<Integer, Integer> stageLevels;
    private Label.LabelStyle labelStyle;

    public MapStageManager(SubGame subGame) {
        mapStage = new Stage(new ScreenViewport());
        this.subGame = subGame;
        String saveName = subGame.gameScreen().getUpgradeStore().getSaveName();
        while (this.stageLevels == null) {
            this.stageLevels = subGame.gameScreen().getUpgradeStore().getUpgradeManager().getAllStageLevels(saveName);
        }
        loadTextures();
        initLabelStyle();
    }

    private void loadTextures() {
        mapTexture = new Texture(Gdx.files.internal("map.png"), true);
        mapTexture.setFilter(Texture.TextureFilter.MipMapLinearLinear, Texture.TextureFilter.Linear);
        buttonTexture = new Texture(Gdx.files.internal("animations/marker.png"), true);
        buttonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        buttonAtlas = new TextureAtlas(Gdx.files.internal("animations/radanim.atlas"));
    }

    private void initLabelStyle() {
        BitmapFont font = new BitmapFont();
        labelStyle = new Label.LabelStyle(font, Color.WHITE);
    }

    public Stage build() {
        float screenWidth = mapStage.getViewport().getWorldWidth();
        float screenHeight = mapStage.getViewport().getWorldHeight();

        Image mapBackground = new Image(mapTexture);
        mapBackground.setSize(screenWidth, screenHeight);
        mapBackground.setPosition(0, 0);
        mapStage.addActor(mapBackground);

        // Create and add buttons with their actions
        createImageButton(screenWidth * 0.109f, screenHeight * 0.764f, 1);   // 140/1280, 550/720
        createImageButton(screenWidth * 0.289f, screenHeight * 0.556f, 2); // 370/1280, 400/720
        createImageButton(screenWidth * 0.508f, screenHeight * 0.528f, 3);   // 650/1280, 380/720
        createImageButton(screenWidth * 0.625f, screenHeight * 0.208f, 4);    // 800/1280, 150/720

        return mapStage;
    }

    private void createImageButton(float x, float y, final int action) {
        final boolean isEnabled = isButtonEnabled(action);

        // Don't show the button at all if not enabled (except for stage 1)
        if (!isEnabled && action != 1) return;

        final Image buttonImage = new Image(buttonTexture);
        buttonImage.setPosition(x, y);

        // Load animation frames
        final Array<TextureAtlas.AtlasRegion> frames = new Array<>();
        for (int i = 1; i <= 16; i++) {
            frames.add(buttonAtlas.findRegion("radanim" + i));
        }

        // Animation state
        final float[] animationTime = {0};
        final int[] currentFrame = {0};
        final boolean[] isActive = {false};

        if (!isEnabled) {
            buttonImage.getColor().a = 0.4f;
        }

        buttonImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float xx, float yy) {
                if (!isEnabled) return;
                handleButtonAction(action);
            }

            @Override
            public boolean touchDown(InputEvent event, float xx, float yy, int pointer, int button) {
                if (!isEnabled) return false;
                isActive[0] = true;
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float xx, float yy, int pointer, int button) {
                if (!isEnabled) return;
                isActive[0] = false;
                buttonImage.setDrawable(new TextureRegionDrawable(buttonTexture));
                clicked(event, xx, yy);
            }

            @Override
            public void enter(InputEvent event, float xx, float yy, int pointer, Actor fromActor) {
                if (!isEnabled) return;
                if (pointer == -1) isActive[0] = true;
            }

            @Override
            public void exit(InputEvent event, float xx, float yy, int pointer, Actor toActor) {
                if (!isEnabled) return;
                if (pointer == -1) {
                    isActive[0] = false;
                    buttonImage.setDrawable(new TextureRegionDrawable(buttonTexture));
                }
            }
        });

        // Animation loop
        buttonImage.addAction(Actions.forever(Actions.run(() -> {
            if (isActive[0]) {
                animationTime[0] += Gdx.graphics.getDeltaTime();
                if (animationTime[0] >= frameDuration) {
                    animationTime[0] -= frameDuration;
                    currentFrame[0] = (currentFrame[0] + 1) % frames.size;
                    buttonImage.setDrawable(new TextureRegionDrawable(frames.get(currentFrame[0])));
                }
            }
        })));

        mapStage.addActor(buttonImage);

        // === Add Progress Label ===
        if (action >= 1 && action < 4) {
            int completed = 0;
            Integer prev = stageLevels.get(action);
            if (prev != null) {
                completed = prev;
            }

            Label progressLabel = new Label(completed + "/10", labelStyle);
            progressLabel.setColor(completed >= 10 ? Color.GREEN : Color.RED);
            progressLabel.setPosition(
                x + buttonImage.getWidth() / 2f - progressLabel.getWidth() / 2f,
                y + buttonImage.getHeight() + 5
            );
            mapStage.addActor(progressLabel);
        }
    }

//    private void createImageButton(float x, float y, final int action) {
//        final Image buttonImage = new Image(buttonTexture);
//        buttonImage.setPosition(x, y);
//
//        // Load animation frames from the atlas
//        final Array<TextureAtlas.AtlasRegion> frames = new Array<>();
//        for (int i = 1; i <= 16; i++) {
//            frames.add(buttonAtlas.findRegion("radanim" + i));
//        }
//
//        // Animation state
//        final float[] animationTime = {0};
//        final int[] currentFrame = {0};
//        final boolean[] isActive = {false}; // Tracks hover or touch hold state
//        final boolean isEnabled = isButtonEnabled(action);
//
//        if (!isEnabled) {
//            buttonImage.getColor().a = 0.4f; // Make it semi-transparent
//        }
//
//        buttonImage.addListener(new ClickListener() {
//            @Override
//            public void clicked(InputEvent event, float xx, float yy) {
//                if (!isEnabled) return; // Ignore clicks if disabled
//                handleButtonAction(action);
//            }
//
//            @Override
//            public boolean touchDown(InputEvent event, float xx, float yy, int pointer, int button) {
//                if (!isEnabled) return false;
//                isActive[0] = true;
//                return true;
//            }
//
//            @Override
//            public void touchUp(InputEvent event, float xx, float yy, int pointer, int button) {
//                if (!isEnabled) return;
//                isActive[0] = false;
//                buttonImage.setDrawable(new TextureRegionDrawable(buttonTexture));
//                clicked(event, xx, yy);
//            }
//
//            @Override
//            public void enter(InputEvent event, float xx, float yy, int pointer, Actor fromActor) {
//                if (!isEnabled) return;
//                if (pointer == -1) isActive[0] = true;
//            }
//
//            @Override
//            public void exit(InputEvent event, float xx, float yy, int pointer, Actor toActor) {
//                if (!isEnabled) return;
//                if (pointer == -1) {
//                    isActive[0] = false;
//                    buttonImage.setDrawable(new TextureRegionDrawable(buttonTexture));
//                }
//            }
//        });
//
//        // Animation loop
//        buttonImage.addAction(Actions.forever(Actions.run(() -> {
//            if (isActive[0]) {
//                animationTime[0] += Gdx.graphics.getDeltaTime();
//                if (animationTime[0] >= frameDuration) {
//                    animationTime[0] -= frameDuration;
//                    currentFrame[0] = (currentFrame[0] + 1) % frames.size;
//                    buttonImage.setDrawable(new TextureRegionDrawable(frames.get(currentFrame[0])));
//                }
//            }
//        })));
//
//        mapStage.addActor(buttonImage);
//    }

    private boolean isButtonEnabled(int stage) {
        if (stage == 1) return true;
        Integer prevLevel =  stageLevels.get(stage - 1);
        return prevLevel != null && prevLevel >= 10;
    }

    private void handleButtonAction(int action) {
        switch (action) {
            case 1:
            case 2:
            case 3:
            case 4:
                subGame.gameScreen().setStage(action);
                subGame.setScreen(subGame.gameScreen());
                if (subGame.gameScreen().isPaused()) {
                    subGame.gameScreen().resume();
                }
                break;
        }
    }

    public void dispose() {
        if (mapTexture != null) mapTexture.dispose();
        if (buttonTexture != null) buttonTexture.dispose();
        if (buttonAtlas != null) buttonAtlas.dispose();
        if (mapStage != null) mapStage.dispose();
    }
}

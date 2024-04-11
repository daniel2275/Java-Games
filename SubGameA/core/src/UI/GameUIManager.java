package UI;

import Components.CustomActor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.mygdx.sub.SubGame;
import gamestates.GameOver;

import static utilities.Constants.Game.SKY_SIZE;
import static utilities.Constants.Game.WORLD_HEIGHT;

public class GameUIManager {
    private Stage gmStage;
    private Skin gameSkin;
    private Label scoreLabel;
    private Label scoreLabel1;
    private Label scoreLabel2;
    private Label scoreLabel3;
    private Stage uiStage;
    private CustomActor underSea;
    private CustomActor skyLine;
    private Sprite pauseSprite;
    private SubGame subGame;
    private GameOver gameOver;

    public GameUIManager(SubGame subGame){
            gmStage = new Stage(new FitViewport(640,480));
            this.subGame = subGame;
    }
        public Stage build() {
            loadSkin();
            createUIElements();
            return gmStage;
        }

        private void loadSkin() {
            try {
                gameSkin = new Skin(Gdx.files.internal("glassyui/glassy-ui.json"));
            } catch (Exception e) {
                Gdx.app.error("MainMenu", "Error loading skin file", e);
            }
        }

        private void createUIElements() {
            //set up UI display
            //background images
            skyLine = new CustomActor(new Texture(Gdx.files.internal("skyline.png")), 109f, 45f,0 , WORLD_HEIGHT - SKY_SIZE  + 15 );
            underSea = new CustomActor(new Texture(Gdx.files.internal("sea1.png")), 109f , 45f, 0,- SKY_SIZE + 45 );

            scoreLabel = new Label("Enemies remaining:", gameSkin);
            scoreLabel1 = new Label("Score:", gameSkin);
            scoreLabel2 = new Label("Level:", gameSkin);
            scoreLabel3 = new Label("Health:", gameSkin);
            scoreLabel.setColor(Color.YELLOW);
            scoreLabel1.setColor(Color.GOLD);
            scoreLabel2.setColor(Color.YELLOW);
            scoreLabel3.setColor(Color.GREEN);

            scoreLabel.setPosition(5, WORLD_HEIGHT - 20);
            scoreLabel1.setPosition(5, WORLD_HEIGHT - 35);
            scoreLabel2.setPosition(5, WORLD_HEIGHT - 50);
            scoreLabel3.setPosition(5, WORLD_HEIGHT - 65);

            //background images
            pauseSprite = new Sprite(new Texture(Gdx.files.internal("paused.png")));
            pauseSprite.setPosition((float) Gdx.graphics.getWidth() /2 - 100f, 500);
            pauseSprite.setSize(200f, 80f);

            gmStage.addActor(underSea);
            gmStage.addActor(skyLine);

            gmStage.addActor(scoreLabel);
            gmStage.addActor(scoreLabel1);
            gmStage.addActor(scoreLabel2);
            gmStage.addActor(scoreLabel3);
       }


        public void dispose() {
            if (gameSkin != null) {
                gameSkin.dispose();
            }
            if (gmStage != null) {
                gmStage.dispose();
            }
        }


    public Stage getStage(){
        return gmStage;
    }

    public Label getScoreLabel() {
        return scoreLabel;
    }

    public Label getScoreLabel1() {
        return scoreLabel1;
    }
    public Label getScoreLabel2() {
        return scoreLabel2;
    }

    public Label getScoreLabel3() {
        return scoreLabel3;
    }



}





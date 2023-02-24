package gamestates;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.danielr.subgame.SubGame;

import static com.danielr.subgame.SubGame.*;

public class Menu extends State implements InputProcessor {

    private Sprite menu;


    public Menu(SubGame subGame) {
        super(subGame);
        create();
    }

    public void update() {
        render();
    }


    public void create() {
        menu = new Sprite(new Texture(Gdx.files.internal("menu.png")));
        menu.setPosition(0,0);
    }

    public void render() {
        batch.begin();
        menu.draw(batch);
        batch.end();

        if (Gdx.input.isTouched()) {
            float x = Gdx.input.getX();
            float y = Gdx.input.getY();
            Vector3 worldCoordinates = camera.unproject(new Vector3(x, y, 0));
            System.out.println("Mouse position: (" + worldCoordinates.x + ", " + worldCoordinates.y + ")");
            if(worldCoordinates.y > 342 && worldCoordinates.y < 416) {
                setGameState(Gamestate.PLAYING);
                pause = false;
            } else if (worldCoordinates.y > 14 && worldCoordinates.y < 88) {
                Gdx.app.exit();
            } else if (worldCoordinates.y > 231 && worldCoordinates.y < 303) {
                subGame.getPlaying().reset();
            }
        }
    }

    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}

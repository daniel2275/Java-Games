package gamestates;


import com.mygdx.sub.SubGame;

public class State {

    protected SubGame subGame;

    public State(SubGame subGame) {
        this.subGame = subGame;
    }

    public SubGame getSubGame() {
        return subGame;
    }

    public void setGameState(Gamestate state) {
        Gamestate.state = state;
    }
}

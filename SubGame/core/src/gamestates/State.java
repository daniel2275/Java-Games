package gamestates;

import com.danielr.subgame.SubGame;

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

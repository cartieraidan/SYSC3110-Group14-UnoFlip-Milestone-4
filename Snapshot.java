import java.io.Serializable;

/**
 * Class made for taking snapshot of current game state.
 * @author Mark Bowerman
 * @version December 5, 2025
 */
public class Snapshot implements Serializable {

    private final GameManager gameManagerCopy;
    private final GameState gameStateCopy;

    /**
     * Creates a deep copy of the games state and stores it in the Snapshot.
     * @param gm the game manager to store
     */
    public Snapshot(GameManager gm, GameState state) {
        this.gameManagerCopy = gm.deepCopy();
        this.gameStateCopy = state;
    }

    /**
     * Executes the current snapshot game logic to continue the game.
     */
    public void executeState() {
        gameStateCopy.runState(gameManagerCopy);
    }

    /**
     * Gets copy of current snapshot game state.
     *
     * @return GameState of snapshot to be executed.
     */
    public GameState getGameState() {
        return gameStateCopy;
    }

    /**
     * Returns the GameManager stored in the current Snapshot.
     * @return the game manager from this snapshot
     */
    public GameManager getGameManagerCopy(){
        return gameManagerCopy;
    }
}

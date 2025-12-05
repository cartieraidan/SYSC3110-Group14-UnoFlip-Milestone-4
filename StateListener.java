/**
 * Interface for listener for game state changes since game manager does not hold a reference to controller
 *
 * @author Aidan Cartier
 * @version December 5, 2025
 */
public interface StateListener {
    void saveSnapshotForUndo(GameState state);
}

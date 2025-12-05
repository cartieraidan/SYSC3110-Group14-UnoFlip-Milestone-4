import java.io.Serializable;

/**
 * Class made for taking snapshot of current game state.
 * @author Mark Bowerman
 * @version December 5, 2025
 */
public class Snapshot implements Serializable {
    /* Add all objects needed for game snapshot
    deck
    discard pile
    list players
    round counter
    game counter
    direction
    playerindex
    drawCard
    wildDraw
    wildDrawColour

    What I (mark) think the plan is/ should be here:

    This will store a snapshot of the game state including all things above. When saving, we will serialize the current
    snapshot and save it to a file. Loading will load the file and deserialize it and run that game.

    Undo and redo will work in a somewhat similar way. Every time a player makes a move we copy the game state into a
    snapshot and push it onto a undo stack. We clear the redo stack as we have made a new move.

    When we want to undo a move we push the current game into redoStack and pop it from undoStack and restore that state
    When we want to redo a move we push the current game into undoStack and pop it from redoStack and restore that state

    So we will need to make functions to copy all the changeable game states. These need to be deep copies so we do not
    modify other games through reference.

     */

    private final GameManager gameManagerCopy;

    /**
     * Creates a deep copy of the games state and stores it in the Snapshot.
     * @param gm the game manager to store
     */
    public Snapshot(GameManager gm){
        this.gameManagerCopy =gm.deepCopy();
    }

    /**
     * Returns the GameManager stored in the current Snapshot.
     * @return the game manager from this snapshot
     */
    public GameManager getGameManagerCopy(){
        return gameManagerCopy;
    }
}

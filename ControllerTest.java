import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class ControllerTest {
    private Controller controller;
    private GameManager gameManager;

    @BeforeEach
    void setup() {
        controller = new Controller();
        gameManager = controller.getGameManager();
    }

    @Test
    void testUndoRedo() {
        Player currentPlayer = gameManager.getCurrentPlayer();
        controller.draw();
        Player undoPlayer = gameManager.getCurrentPlayer();
        controller.undo();
        assertEquals(currentPlayer, gameManager.getCurrentPlayer());
        controller.redo();
        assertEquals(undoPlayer, gameManager.getCurrentPlayer());
    }

    @Test
    void testSaveLoad() {
        controller.saveGame("testSave");
        Controller controller2 = new Controller();
        controller2.loadGame("testSave");
        assertEquals(gameManager.getCurrentPlayer().gethand(), controller2.getGameManager().getCurrentPlayer().gethand());
    }
}

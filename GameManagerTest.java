import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.*;

public class GameManagerTest {
    private Player p1, p2;
    private GameManager gm;

    @BeforeEach
    void setUp(){
        p1 = new Player("Bob");
        p2 = new Player("Jill");
        gm = new GameManager(Arrays.asList(p1, p2));
    }

    @Test
    void testStartGameDealsHands(){
        gm.startgame();
        for (Player p: gm.getPlayers()){
            assertEquals(7, p.gethand().size(), "Each player should start with 7 cards.");
        }
        assertNotNull(gm.getDiscardPile().peek(), "Top discard should not be null.");
    }

    @Test
    void testNextTurnCycles() {
        gm.nextTurn();
        assertEquals(1, gm.getCurrentPlayerIndex());
        gm.nextTurn();
        assertEquals(0, gm.getCurrentPlayerIndex(), "Should wrap back to first player.");
    }


    @Test
    void testCheckEmptyHandTrue() {
        gm.startgame();
        p1.clearHand();
        assertTrue(gm.checkEmptyHand());
    }

    @Test
    void testGetRoundWinner() {
        gm.startgame();
        p2.clearHand();
        assertEquals(p2, gm.getRoundWinner());
    }
}

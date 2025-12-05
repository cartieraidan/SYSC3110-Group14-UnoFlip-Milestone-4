import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class PlayerTest {
    private Player player;
    private Deck deck;

    @BeforeEach
    void setUp() {
        player = new Player("Mark");
        deck = new Deck();
    }

    @Test
    void testGetName() {
        assertEquals("Mark", player.getName());
    }

    @Test
    void testDrawCardAddsToHand() {
        int initialSize = player.gethand().size();
        player.drawCard(deck);
        assertEquals(initialSize + 1, player.gethand().size());
    }

    @Test
    void testPlayCardRemovesCard() {
        player.drawCard(deck);
        Card firstCard = player.gethand().get(0);
        Card played = player.playCard(0);

        assertEquals(firstCard, played);
        assertTrue(player.gethand().isEmpty(), "Hand should be empty after playing only card.");
    }

    @Test
    void testPlayCardInvalidIndex() {
        assertNull(player.playCard(99), "Invalid index should return null.");
    }

    @Test
    void testClearHand() {
        player.drawCard(deck);
        player.clearHand();
        assertTrue(player.gethand().isEmpty());
    }

    @Test
    void testAddScore() {
        player.addScore(50);
        assertEquals(50, player.getScore());
        player.addScore(25);
        assertEquals(75, player.getScore());
    }

    @Test
    void testHandValueCalculation() {
        player.gethand().add(new Card(CardColour.RED, CardColour.BROWN, CardType.NUMBER, CardType.NUMBER, 5, 5));
        player.gethand().add(new Card(CardColour.BLUE, CardColour.PURPLE, CardType.DRAW_ONE, CardType.DRAW_ONE, 10, 10));
        assertEquals(15, player.gethandValue());
    }

    @Test
    void testHasPlayableCardTrue() {
        Card top = new Card(CardColour.RED, CardColour.BROWN, CardType.NUMBER, CardType.NUMBER, 3, 3);
        player.gethand().add(new Card(CardColour.RED, CardColour.BROWN, CardType.NUMBER, CardType.NUMBER, 7, 7));
        assertTrue(player.hasPlayableCard(top));
    }

    @Test
    void testHasPlayableCardFalse() {
        Card top = new Card(CardColour.GREEN, CardColour.TEAL, CardType.NUMBER, CardType.NUMBER, 3, 3);
        player.gethand().add(new Card(CardColour.RED, CardColour.BROWN, CardType.SKIP, CardType.SKIP_EVERYONE, 20, 30));
        assertFalse(player.hasPlayableCard(top));
    }

    @Test
    void testToStringFormat() {
        player.drawCard(deck);
        String output = player.toString();
        assertTrue(output.contains("Mark"));
        assertTrue(output.contains("handSize="));
    }
}

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp(){
        deck = new Deck();
    }

    @Test
    void testDeckInitialization_NotEmpty(){
        assertFalse(deck.isEmpty(), "Deck should not be empty after initialization.");
    }

    @Test
    void testDeckInitialization_CorrectSize(){
        assertEquals(60, deck.size(), "Deck should contain 60 cards after initialization.");
    }

    @Test
    void testDrawCard_RemovesTopCard(){
        int initialSize = deck.size();
        Card top = deck.peekCard();
        Card drawn = deck.drawCard();

        assertEquals(top, drawn, "Drawn card should be the same as the top card.");
        assertEquals(initialSize - 1, deck.size(), "Deck size should decrease by one after drawing.");
    }

    @Test
    void testPeekCard_DoesNotRemoveCard() {
        int initialSize = deck.size();
        Card top = deck.peekCard();
        Card again = deck.peekCard();

        assertEquals(top, again, "Peek should return the same card each time.");
        assertEquals(initialSize, deck.size(), "Peeking should not remove a card.");
    }

    @Test
    void testAddCard_IncreasesDeckSize() {
        Card card = new Card(CardColour.RED, CardColour.BROWN, CardType.NUMBER, CardType.NUMBER, 5, 5);
        int initialSize = deck.size();
        deck.addCard(card);
        assertEquals(initialSize + 1, deck.size(), "Adding a card should increase deck size by one.");
        assertEquals(card, deck.peekCard(), "Newly added card should be on top of the deck.");
    }

    @Test
    void testAddAll_AddsMultipleCards() {
        List<Card> newCards = new ArrayList<>();
        newCards.add(new Card(CardColour.BLUE, CardColour.PURPLE, CardType.SKIP, CardType.SKIP_EVERYONE, 20, 30));
        newCards.add(new Card(CardColour.GREEN, CardColour.TEAL, CardType.REVERSE, CardType.REVERSE, 20, 20));

        int initialSize = deck.size();
        deck.addAll(newCards);

        assertEquals(initialSize + 2, deck.size(), "Adding multiple cards should increase deck size accordingly.");
    }

    @Test
    void testShuffle_ChangesOrder() {
        // Copy current deck order
        List<Card> beforeShuffle = new ArrayList<>(deck.getCards());
        deck.shuffle();
        List<Card> afterShuffle = new ArrayList<>(deck.getCards());

        // They shouldn't be in exactly the same order (possible, but extremely rare)
        assertNotEquals(beforeShuffle, afterShuffle, "Shuffling should change card order most of the time.");
    }

    @Test
    void testIsEmpty_OnEmptyDeck() {
        while (!deck.isEmpty()) {
            deck.drawCard();
        }
        assertTrue(deck.isEmpty(), "Deck should be empty after drawing all cards.");
    }

    @Test
    void testGetCards_ReturnsStackReference() {
        Stack<Card> cards = deck.getCards();
        assertNotNull(cards, "getCards() should never return null.");
        assertEquals(deck.size(), cards.size(), "Returned stack size should match deck size.");
    }
}

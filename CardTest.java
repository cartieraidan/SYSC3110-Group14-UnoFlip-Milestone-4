import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CardTest {
    private  Card red5;
    private  Card redSkip;
    private  Card blue5;
    private  Card wild;
    private  Card wildDrawTwo;
    private  Card nullCard;

    @BeforeEach
    void setUp(){
        red5 = new Card(CardColour.RED, CardColour.BROWN, CardType.NUMBER, CardType.NUMBER, 5, 5);
        redSkip = new Card(CardColour.RED, CardColour.BROWN, CardType.SKIP, CardType.SKIP_EVERYONE, 20, 30);
        blue5 = new Card(CardColour.BLUE, CardColour.PURPLE, CardType.NUMBER, CardType.NUMBER, 5, 5);
        wild = new Card(CardColour.WILD, CardColour.WILD, CardType.WILD, CardType.WILD, 40, 40);
        wildDrawTwo = new Card(CardColour.WILD, CardColour.WILD, CardType.WILD_DRAW_TWO, CardType.WILD_DRAW_COLOR, 50, 60);
        nullCard = null;
    }

    @Test
    void testConstructorAndGetters(){
        assertEquals(CardColour.RED, red5.getColour());
        assertEquals(CardType.NUMBER, red5.getType());
        assertEquals(5, red5.getValue());
    }

    @Test
    void testSetColour(){
        red5.setColour(CardColour.GREEN);
        assertEquals(CardColour.GREEN, red5.getColour());
    }

    @Test
    void testMatches_NullOtherCard(){
        assertTrue(red5.matches(nullCard));
    }

    @Test
    void testMatches_SameColour(){
        assertTrue(redSkip.matches(red5), "Cards of same colour should match.");
    }

    @Test
    void testMatches_SameType(){
       Card greenSkip = new Card(CardColour.GREEN, CardColour.ORANGE, CardType.SKIP, CardType.SKIP_EVERYONE, 20, 30);
       assertTrue(redSkip.matches(greenSkip));
    }

    @Test
    void testMatches_SameValue(){
        assertTrue(red5.matches(blue5), "Number cards with same value should match.");
    }

    @Test
    void testMatches_WildAlwaysMatches(){
        assertTrue(wild.matches(red5));
        assertTrue(wildDrawTwo.matches(blue5));
    }

    @Test
    void testMatches_NoMatch(){
       Card green9 =new Card(CardColour.GREEN, CardColour.TEAL, CardType.NUMBER, CardType.NUMBER, 9, 9);
       assertFalse(red5.matches(green9));
    }

    @Test
    void testToString_NumberCard(){
        assertEquals("RED 5", red5.toString());
    }

    @Test
    void testToString_ActionCard(){
        assertEquals("RED SKIP", redSkip.toString());
    }

    @Test
    void testToString_WildCard(){
        assertEquals("WILD WILD", wild.toString());
    }

}

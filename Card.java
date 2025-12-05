import java.io.Serializable;

/**
 * Represents a single card in the Uno Flip game.
 * A card has a colour, a type, and if it is a number card, a value.
 * Cards can be compared to other cards to determine if they can be legally played.
 *
 * @author Mark Bowerman
 * @version 1.0
 */
public class Card implements Serializable {
    private CardColour lightColour;
    private CardColour darkColour;
    private CardType lightType;
    private CardType darkType;
    private CardSide side;
    private int lightValue;
    private int darkValue;


    /**
     * Constructs a new Card with the specified colour, type, and value.
     *
     * @param lightColour the light colour of card
     * @param darkColour the dark colour of card
     * @param lightType the light type of card
     * @param darkType the dark type of card
     * @param lightValue the numeric value of the card. -1 for non-number cards.
     * @param darkValue the numeric value of the card. -1 for non-number cards.
     */
    public Card(CardColour lightColour, CardColour darkColour, CardType lightType, CardType darkType, int lightValue, int darkValue) {
        this.lightColour = lightColour;
        this.darkColour = darkColour;
        this.lightType = lightType;
        this.darkType = darkType;
        this.lightValue = lightValue;
        this.darkValue = darkValue;
        this.side = CardSide.LIGHT;
    }

    /**
     * Returns a deep copy of card to be used in a snapshot.
     * @return the copied card
     */
    public Card deepCopy() {
        Card copy = new Card(lightColour, darkColour, lightType, darkType, lightValue, darkValue);
        copy.side = this.side;
        return copy;
    }

    /**
     * flips colour and type of card.
     */
    public void flipCard() {
        this.side = (this.side == CardSide.LIGHT) ? CardSide.DARK : CardSide.LIGHT;
    }

    /**
     * Returns card current side, used for UI card styling function.
     *
     * @return LIGHT or DARK.
     */
    public CardSide getSide() {
        return side;
    }
    
    /** The colour of this card (RED, BLUE, GREEN, YELLOW, or WILD). */
    public CardColour getColour(){
        return (this.side == CardSide.LIGHT) ? lightColour : darkColour;
    }

    /**
     * Sets the colour of the card when using a wild card.
     * @param colour the colour to set the card to.
     */
    public void setColour(CardColour colour) {
        if (this.side == CardSide.LIGHT){
            this.lightColour = colour;
        } else {
            this.darkColour = colour;
        }
    }
    
    /** The type of this card (NUMBER, REVERSE, SKIP, DRAW_ONE, WILD, WILD_DRAW_TWO).*/
    public CardType getType(){
        return (this.side == CardSide.LIGHT) ?  lightType : darkType;
    }
    
    /** The numeric value of this card, returns -1 for non-number cards. */
    public int getValue(){
        return (this.side == CardSide.LIGHT) ? lightValue : darkValue;
    }

    /**
     * Determines whether this card can be played on top of another card.
     * A card can be played if it matches the colour, type or value of the top card on the discard pile, or if it is a wild card.
     * @param other the top card on the discard pile. May be null of no card has been played yet.
     * @return true if this card is a legal play on the other card, false otherwise.
     */
    public boolean matches(Card other){
        //If no previous card it can be played
        if (other == null){
            return true;
        }

        CardType type = this.getType();
        CardColour colour = this.getColour();
        int value = this.getValue();

        //flip must match color
        if (type == CardType.FLIP && colour == other.getColour()) {
            return true;
        }
        //Wild always matches
        if (type == CardType.WILD || type == CardType.WILD_DRAW_TWO || type == CardType.WILD_DRAW_COLOR) {
            return true;
        }
        //Match by colour
        if (colour == other.getColour()) {
            return true;
        }
        //if both are number compare value
        if (type == CardType.NUMBER && other.getType() == CardType.NUMBER){
            return value == other.getValue();
        }

        //Match by type
        if (type == other.getType()){
            return true;
        }

        return false;
    }

    /**
     * Returns a string representation of the card for display purposes.
     * Number cards include their numeric value, other cards show their type.
     * @return a string describing the card ("RED 5" or "BLUE SKIP")
     */
    @Override
    public String toString(){
        CardType type = this.getType();
        CardColour colour = this.getColour();
        int value = this.getValue();

        if (type == CardType.NUMBER){
            return colour + " " + value;
        }else{
            return colour + " " + type;
        }
    }
}

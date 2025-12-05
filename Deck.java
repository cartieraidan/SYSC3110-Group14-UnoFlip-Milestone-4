import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

/**
 * Class represents a standard UNO deck used in the game.
 * It manages card creation, shuffling, drawing and general deck operations.
 *
 * @author Joshua Heinze
 * @version 1.0
 */
public class Deck implements Serializable {
    private final Stack<Card> cards;

    /**
     * Initialize standard deck.
     */
    public Deck(){
        this.cards = new Stack<>();
        initializeStandardUnoDeck();
    }

    /**
     * Returns a deep copy of deck to be used in a snapshot.
     * @return the copied deck
     */
    public Deck deepCopy(){
        Deck copy = new Deck();
        copy.cards.clear(); //remove cards added by constructor
        for (Card card : this.cards) { //copy over cards from deck to copied deck
            copy.cards.push(card.deepCopy());
        }
        return copy;
    }

    /**
     * Clears deck and re-initialize it for a new game.
     */
    public void newDeck() {
        this.cards.clear();
        initializeStandardUnoDeck();
    }

    /**
     * Create a standard UNO deck.
     */
    private void initializeStandardUnoDeck(){
        // create cards of each colour
        for (int j = 0 ; j <= 3 ; j++) {
            //create coloured cards of each value and type
            for (int i = 0 ; i <= 12 ; i++) {
                int type;
                int value;
                if (i <= 9) {
                    //set type to number
                    type = 0;
                    //set value to 0-9
                    value = i;
                    //for numbers only
                    cards.add(new Card(CardColour.values()[j], CardColour.values()[j + 5], CardType.values()[type], CardType.values()[type],value, value));
                }
                else {
                    //set type to reverse, skip, or draw one
                    type = i - 9;
                    //set value to 10 for draw one and 20 for reverse and skip
                    if(type == 3) { //draw one
                        value = 10;
                        cards.add(new Card(CardColour.values()[j], CardColour.values()[j + 5], CardType.values()[type], CardType.DRAW_FIVE, value, 20));
                    }
                    else { //reverse and skip
                        value = 20;
                        if (type == 1) { //reverse
                            cards.add(new Card(CardColour.values()[j], CardColour.values()[j + 5], CardType.values()[type], CardType.values()[type], value, value));
                        } else { //skip
                            cards.add(new Card(CardColour.values()[j], CardColour.values()[j + 5], CardType.values()[type], CardType.SKIP_EVERYONE, value, 30));
                        }
                    }
                }

                //cards.add(new Card(CardColour.values()[j], CardType.values()[type], value));
            }
        }

        //create four of each wild card
        for (int i = 0 ; i <= 3 ; i++) {
            cards.add(new Card(CardColour.WILD, CardColour.WILD, CardType.WILD, CardType.WILD, 40, 40));
            cards.add(new Card(CardColour.WILD, CardColour.WILD, CardType.WILD_DRAW_TWO, CardType.WILD_DRAW_COLOR, 50, 60));
        }

        //adding flip cards
        for (int i = 0; i < 2; i++) {
            cards.add(new Card(CardColour.RED, CardColour.BROWN, CardType.FLIP, CardType.FLIP, 20, 20));
            cards.add(new Card(CardColour.BLUE, CardColour.PURPLE, CardType.FLIP, CardType.FLIP, 20, 20));
            cards.add(new Card(CardColour.GREEN, CardColour.TEAL, CardType.FLIP, CardType.FLIP, 20, 20));
            cards.add(new Card(CardColour.YELLOW, CardColour.ORANGE, CardType.FLIP, CardType.FLIP, 20, 20));
        }

        //shuffle the deck
        shuffle();
    }

    /**
     * Shuffles the deck.
     */
    public void shuffle(){
        Collections.shuffle(cards);
    }

    /**
     * Takes the top card off of the deck and returns it.
     *
     * @return The top card of the deck
     */
    public Card drawCard(){
        return cards.pop();
    }

    /**
     * Gets the top card of the deck without removing it.
     *
     * @return The top card of the deck
     */
    public Card peekCard(){
        return cards.peek();
    }

    /**
     * Adds a card to the top of the deck.
     *
     * @param card the card to be added to the deck
     */
    public void addCard(Card card){
        cards.push(card);
    }

    /**
     * Returns whether the deck is empty or not.
     *
     * @return true if the deck is empty
     */
    public boolean isEmpty(){
        return cards.isEmpty();
    }

    /**
     * Returns the size of the deck
     *
     * @return the size of the deck
     */
    public int size(){
        return cards.size();
    }

    /**
     * Utility to add multiple cards such as when recycling discard pile.
     *
     * @param moreCards the cards to add
     */
    public void addAll(List<Card> moreCards){
        cards.addAll(moreCards);
    }

    /**
     * Returns the full stack of cards in the deck.
     *
     * @return the stack of cards
     */
    public Stack<Card> getCards(){
        return cards;
    }
}

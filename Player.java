import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a player in the UNO game.
 * Each player has a name, hand of cards, and a score that accumulates across rounds.
 * Players can draw cards, play cards, clear their hand, and calculate the total points of the remaining cards
 *
 * @author Mark Bowerman
 * @version December 5, 2025
 */
public class Player implements Serializable {
    private final String name;
    private final List<Card> hand;
    private int score;


    /**
     * Initializes a player with a given name.
     *
     * @param name the name of the player
     */
    public Player(String name){
        this.name = name;
        this.hand = new ArrayList<>();
        this.score = 0;

    }

    /**
     * Returns a deep copy of player to be used in a snapshot.
     * @return the copied player
     */
    public Player deepCopy(){
        Player copy = new Player(this.name);
        copy.score = this.score;
        for (Card card : this.hand) { //copy over hand
            copy.hand.add(card.deepCopy());
        }
        return copy;
    }

    /**
     * Returns the name of the player.
     *
     * @return the name of the player
     */
    public String getName(){
        return name;
    }

    /**
     * Draw one card from the given deck and add to players hand.
     *
     * @param deck the deck to draw from
     */
    public void drawCard(Deck deck) {
        Card c = deck.drawCard();
        if (c != null){
            hand.add(c);
        }
    }

    /**
     * Lets you add Card to player hand manually.
     *
     * @param card Card drawn from deck.
     */
    public void addCardtoHand(Card card) {
        if (card != null){
            hand.add(card);
        }
    }

    /**
     * Play the card at given index in the hand.
     *
     * @param index the index of the card to play
     * @return the played card or null if index invalid
     */
    public Card playCard(int index){
        Card card;
        if(hand.size() > index - 1) {
            card = hand.get(index);
            hand.remove(card);
            return card;
        }
        return null; //The index is invalid
    }

    /**
     * Flips the side of cards in players hand.
     */
    public void flipHand() {
        for (Card card : hand) {
            card.flipCard();
        }
    }

    /**
     * Clears all the cards in the players hand.
     */
    public void clearHand() {
        hand.clear();
    }

    /**
     * Adds a number of points to the players score.
     *
     * @param points the number of points to be added to the players score
     */
    public void addScore(int points) {
        score += points;
    }

    /**
     * Gets the score of the player
     * @return score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Reset player score after game.
     */
    public void resetScore() {
        score = 0;
    }

    /**
     * Returns the players hand.
     *
     * @return the players hand
     */
    public List<Card> gethand() {
        return hand;
    }

    /**
     * Calculates and returns the point value of the players hand.
     *
     * @return the point value fo the players hand
     */
    public int gethandValue() {
        int value = 0;
        for (Card i : hand) {
            value += i.getValue();
        }
        return value;
    }

    /**
     * Checks if the player has any playable card against topCard.
     *
     * @param topCard the top card on the discard pile
     * @return true if there is at least one playable card
     */
    public boolean hasPlayableCard(Card topCard){
        for (Card c: hand){
            if (c.matches(topCard)){
                return true;
            }
        }
        return false;
    }

    /**
     * Returns a string representation of the player.
     *
     * @return a string representation of the player
     */
    @Override
    public String toString(){
        return name + "(score=" + score + ", handSize=" + hand.size() + ")";
    }

}

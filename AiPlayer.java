import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Child of Player which implements more functions imitating a Player.
 * And still use the same methods as Player so don't have to change GameManager too much.
 *
 * @author Aidan Cartier
 * @version November 25, 2025
 */
public class AiPlayer extends Player {

    private ArrayList<JButton> uiHand;
    private ArrayList<Integer> playableCards;
    private int bestCard;

    /**
     * Constructs an AI player to play UNO.
     *
     * @param name name of player
     */
    public AiPlayer(String name) {
        super(name);
        uiHand = new ArrayList<>();
        playableCards = new ArrayList<>();
        bestCard = -1;
    }

    /**
     * Constructor chaining if AI created without name.
     */
    public AiPlayer() {
        this("AI Player");
    }

    /**
     * updateUIHand() must be called before this function in order
     * to get an update AI hand of all cards playable.
     * Returns JButton emulating if a player selected a card to play.
     *
     * @return JButton of card it wishes to play.
     */
    public JButton tryToPlay() {
        return uiHand.get(playableCards.get(bestCard));
    }

    /**
     * Method clears its previous hand and updates it from getting the JPanel in view
     * and getting all the components (JButtons).
     * Can get actual card class from using .getClientProperty("index") which is the int index
     * in the player hand Card list.
     *
     * @param view Current game view.
     */
    public void updateUIHand(UnoView view, Card card) {
        uiHand.clear(); //clear current hand
        playableCards.clear(); //clear playable cards

        this.playableCards(card);
        if (!(playableCards.isEmpty())) { //finding best card if you have cards to play
            this.findBestCard();
        }

        JPanel cardContainer = view.getPlayerCards(); //where UI buttons stored in view
        Component[] comp = cardContainer.getComponents(); //get all buttons

        for (Component component : comp) {
            if (component instanceof JButton) {
                uiHand.add((JButton) component);
            }
        }
    }

    /**
     * Gets the best hand out of players hand
     *
     * @return Card it will play.
     */
    public Card getBestCard() {
        if (bestCard == -1) {
            return null;
        } else {
            return gethand().get(playableCards.get(bestCard));
        }
    }

    /**
     * Finds the best card value in playable cards. Updates an int value, which is the card index
     * in players hand.
     */
    private void findBestCard() {
        if (playableCards.size() == 1) {
            bestCard = 0;
        } else {
            bestCard = 0;

            for (int i = 1; i < playableCards.size(); i++) {
                int bestCardIndex = playableCards.get(bestCard);
                int currentCardIndex = playableCards.get(i);

                //can swap value to play big cards last by swapping "<" with ">"
                if (gethand().get(bestCardIndex).getValue() < gethand().get(currentCardIndex).getValue()) {
                    bestCard = i;
                }
            }
        }
    }

    /**
     * Update the Arraylist to specify which cards are playable to AI player.
     *
     * @param discardedCard Top card on the discard pile.
     */
    private void playableCards(Card discardedCard) {
        for (Card card : gethand()) {
            if (card.matches(discardedCard)) {
                playableCards.add(gethand().indexOf(card)); //adding index of card
            }
        }

    }

    /**
     * Get the colour the AI chooses when it plays a wild card.
     *
     * @return CardColour for card played.
     */
    public CardColour getWildColour() {
        return calculateBestColour();
    }


    /**
     * Finds which colour is the highest and chooses it
     * for the wild card.
     *
     * @return CardColour for card played.
     */
    private CardColour calculateBestColour() {
        Map<CardColour, Integer> colourMap = new HashMap<>(); //CardColour as key and int for tracking

        for (Card card : gethand()) { //adding colours to hashMap
            if (card.getColour() != CardColour.WILD) { //don't want the colour WILD
                if (colourMap.containsKey(card.getColour())) { //if already in Map
                    colourMap.put(card.getColour(), colourMap.get(card.getColour()) + 1);

                } else { //if not in Map already
                    colourMap.put(card.getColour(), 1);

                }
            }
        }

        System.out.println("colourMap: " + colourMap);

        //Choosing colour from HashMap
        CardColour chosenColour = null;
        for (CardColour colour : colourMap.keySet()) {
            if (chosenColour == null) { //for initial pick
                chosenColour = colour;

            } else { //comparing counts
                if (colourMap.get(chosenColour) < colourMap.get(colour)) {
                    chosenColour = colour;
                }
            }
        }

        return chosenColour;
    }

    public void testHand() {
        for (JButton button : uiHand) {
            System.out.println(button.getText() + " and card object toString: " + gethand().get((Integer)button.getClientProperty("index")).toString());
        }
    }

    public void testPlayableCards() {
        for (int i : playableCards) {
            System.out.println("playble cards: " + gethand().get(i).toString());
        }
    }

    public void testBestCard() {
        System.out.println("best card to play: " + gethand().get(playableCards.get(bestCard)).toString());
    }
}

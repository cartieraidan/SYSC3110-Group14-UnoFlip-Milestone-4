import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

/**
 *
 * The GameManager class manages the state and flow of the UI-based UNO game.
 * Implemented to support up to 0 AI players.
 *
 * @author Aidan Cartier, Mark Bowerman
 * @version December 5, 2025
 */
public class GameManager implements Serializable {
    private final List<Player> players;
    private int playerCount;
    private final Deck deck;
    private final Stack<Card> discardPile;
    private int currentPlayerIndex;
    private int direction;
    private UnoView view;
    private boolean drawCard = false;

    private final Map<JButton, Integer> prevCardZ; //used for tracking button prev z
    private boolean wildDraw = false; //Used for draw colour loop, true if card played
    private CardColour wildDrawColour; //for colour of card in draw loop

    private JButton hoveredButton = null;
    private JButton selectedCard = null;
    private JButton play;
    private JButton draw;

    private int sequence = 0; //for debugging
    private int roundCounter = 0;
    private int gameCounter = 1;

    //all new variables for new code
    private boolean gameStarted = false;

    /**
     * Constructs a GameManager with a list of players
     *
     * @param players the players that are participating in the game
     */
    public GameManager(List<Player> players) {
        this.players = new ArrayList<>(players);
        this.deck = new Deck();
        this.discardPile = new Stack<>();
        this.currentPlayerIndex = 0;
        this.direction = 1;
        prevCardZ = new HashMap<>();

        initialGame(); //getting players
    }

    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //Master code.

    /**
     * Methods that initializes all players for the game.
     */
    private void initialGame() {

        if (!gameStarted) { //
            //get number of players and if they're AI or not
            getNumberPlayers(); //number of players
            getPlayerSettings(); //get name and if AI

            gameStarted = true; //game started no longer need to initialize
        }
    }

    /**
     * Logic for sequence 2, player start of turn, what can they do?
     */
    private void handleInitialHand() {
        Player player = getCurrentPlayer();

        if (canPlay() && player instanceof AiPlayer) {
            //go to seq 3
            //UI play will initialize seq 3 independently

            aiPlay(player);

        } else if (!canPlay() && player instanceof AiPlayer) { //AI player must draw
            //got to seq 5 for AI
            drawCard();
            handleAfterDraw();

        } else if (!canPlay() && !(player instanceof AiPlayer)) { //player can't play
            //go to seq 5
            JOptionPane.showMessageDialog(null, "No playable cards. You must draw.");
            draw.setEnabled(true); //for player to press
        }

        //else player can play and will activate once card selected
    }

    /**
     * AI player plays card
     *
     * @param player current player playing, would be AI.
     */
    private void aiPlay(Player player) {
        //updates AI hand and which card it wants to play
        ((AiPlayer) player).updateUIHand(view, this.topDiscard()); //create separate method

        selectedCard = ((AiPlayer) player).tryToPlay(); //returns JButton/card AI wants to play

        System.out.println("card played by ai: " + player.gethand().get((int)selectedCard.getClientProperty("index")).toString());

        playCard(); //play card AI selected
    }

    /**
     * Logic for sequence 5, drawing a card then what?
     */
    public void handleAfterDraw() {
        displayHand(); //updates player UI hand
        Player player = getCurrentPlayer();

        if (canPlay() && player instanceof AiPlayer) {
            //go to seq 3
            //same as handleInitialHand where AI tries to play

            aiPlay(player);

        } else if (canPlay() && !(player instanceof AiPlayer)) {
            //really goes to seq 3 but User goes independently
            JOptionPane.showMessageDialog(null, "Player can play card.");
        } else {
            //go to seq 4 for both AI and User where it is nextTurn
            JOptionPane.showMessageDialog(null, "No playable cards. skipTurn()");
            nextTurn();
        }
    }

    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //Milestone 4 implementation

    /**
     * Returns a deep copy of deck to be used in a snapshot.
     * @return the copied GameManager
     */
    public GameManager deepCopy() {

        //create new players list
        List<Player> playersCopy = new ArrayList<>();
        for (Player p : this.players) {
            playersCopy.add(p.deepCopy()); //deep copy each player
        }

        GameManager copy = new GameManager(playersCopy);

        //copy the deck manually as deck is final
        copy.deck.newDeck(); //reset deck first
        copy.deck.getCards().clear(); // remove default cards
        for (Card c : this.deck.getCards()) {
            copy.deck.getCards().add(c.deepCopy()); //deep copy of each card
        }

        //copy discard pile
        copy.discardPile.clear();
        for (Card c : this.discardPile) {
            copy.discardPile.push(c.deepCopy()); //deep copy of each card
        }

        //copy other fields
        copy.currentPlayerIndex = this.currentPlayerIndex;
        copy.direction = this.direction;
        copy.drawCard = this.drawCard;
        copy.wildDraw = this.wildDraw;
        copy.wildDrawColour = this.wildDrawColour;
        copy.sequence = this.sequence;
        copy.roundCounter = this.roundCounter;
        copy.gameCounter = this.gameCounter;

        // UI elements are left null
        copy.view = null;
        copy.play = null;
        copy.draw = null;
        copy.hoveredButton = null;
        copy.selectedCard = null;
        copy.prevCardZ.clear();

        return copy;
    }

    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //Sequence 1: Initialize of game, deal hands, display first player hands in UI.

    /**
     * Initialize and start game (shuffle, deal initial hands, set up discard)
     */
    public void startGame() {
        deck.shuffle();
        dealInitialHands();
        Card firstCard = deck.drawCard();
        //no wild cards as first card
        while (firstCard.getType() != CardType.NUMBER) {
            deck.addCard(firstCard);
            deck.shuffle();
            firstCard = deck.drawCard();
        }
        discardPile.push(firstCard);

        updateAll();

        handleInitialHand(); //for start of players hand
    }

    /**
     * Initializes all the buttons at the top right of the screen
     * quit, draw, play
     */
    public void initializeControls() {
        view.addControlButtons();

        play = view.getPlayButton();

        draw = view.getDrawButton();

        view.repaint();
    }

    /**
     * Deals 7 cards from the deck to each player
     */
    private void dealInitialHands() {
        //deal all players their cards
        for (Player p : players) {
            for (int i = 0; i < 7; i++) {
                p.drawCard(deck);
            }
        }


    }

    /**
     *Set view.
     *
     * @param view View instance.
     */
    public void setView(UnoView view) {
        this.view = view;
    }

    /**
     * Initializes player classes for the GameManager, requiring player name, and whether they are AI.
     */
    private void getPlayerSettings() {
        for(int i = 0; i < playerCount; i++){
            String name = JOptionPane.showInputDialog("Enter the name of the player " + (i + 1) + ": ");
            //Player p = new Player(name);

            Object[] options = {"YES", "NO"};
            JPanel promptAi = new JPanel();
            promptAi.add(new JLabel("Is this player AI?"));

            int result = JOptionPane.showOptionDialog(null, promptAi, "Is This Player AI?", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
            if (result == JOptionPane.YES_OPTION) {
                players.add(new AiPlayer(name));
                System.out.println("added AI player" + " state: " + getSeq());
            } else {
                players.add(new Player(name));
                System.out.println("added normal player" + " state: " + getSeq());
            }

        }
    }

    /**
     * To get initialize list of players for GameManager, require number of players before names.
     */
    private void getNumberPlayers() {
        playerCount = Integer.parseInt(JOptionPane.showInputDialog("Input number of players(2-4): "));

        //error checking
        while (playerCount < 2 || playerCount > 4) {
            JOptionPane.showMessageDialog(null,"Invalid number. We have 2-4 players.");
            playerCount = Integer.parseInt(JOptionPane.showInputDialog("Input number of players(2-4): "));
        }
    }

    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //Sequence 2: Handles initial turn logic, determines if you can play.

    /**
     * Method to determine if player can play on their turn.
     *
     * @return True is player has a playable card.
     */
    private boolean canPlay() {
        return getCurrentPlayer().hasPlayableCard(topDiscard());
    }


    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //Sequence 3: Can play card, handles logic.

    /**
     * Set play button in UI
     *
     * @param bool True to enable button.
     */
    public void setPlayButton(boolean bool) {
        play.setEnabled(bool);
    }

    /**
     * Puts card at top of stack in discard.
     *
     * @param card Card player played.
     */
    public void pushToDiscardPile(Card card) {
        discardPile.push(card);
    }

    /**
     * Attempt to play a card on behalf of the current player
     *
     */
    public void playCard() {
        if (selectedCard == null) {
            JOptionPane.showMessageDialog(null, "No card selected.");
        } else {
            Player player = getCurrentPlayer();
            Card card = player.gethand().get((int) selectedCard.getClientProperty("index")); //get card from button hidden index

            if (card.getType() == CardType.FLIP) { //player plays flip card
                player.gethand().remove(card); //remove card played from list
                if (!checkWinner()) { //don't want to go through extra logic if winner
                    handleFlipCard(card);
                }

            } else if (card.getType() == CardType.WILD || card.getType() == CardType.WILD_DRAW_TWO || card.getType() == CardType.WILD_DRAW_COLOR) {
                //wild requires an input so controller has to take care of it
                player.gethand().remove(card);
                if (!checkWinner()) { //don't want to go through extra logic if winner
                    handleWildCard(card);
                }

            } else {
                //check if the move is valid
                if (checkValidMove(card)) {
                    player.gethand().remove(card); //remove card played from list, never implemented in game manager?
                    pushToDiscardPile(card);
                    if (!checkWinner()) { //don't want to go through extra logic if winner
                        handleActionCard(card);
                    }


                }
            }
        }


    }

    /**
     * Flips deck, discard pile and all player hands.
     */
    private void handleFlipCard(Card cardHanded) {
        if (checkValidMove(cardHanded)) {

            pushToDiscardPile(cardHanded);

            //flip deck
            for (Card card : deck.getCards()) {
                card.flipCard();
            }

            //flip discard pile
            for (Card card : discardPile) {
                card.flipCard();
            }

            //flip player hands
            for (Player p : players) {
                p.flipHand();
            }

            nextTurn();

        }


    }

    /**
     * Checks if card can be played on top of discard top card
     *
     * @param card to attempt match
     * @return true if the card can be played, false otherwise
     */
    public boolean checkValidMove(Card card) {
        return card.matches(topDiscard());

    }

    /**
     * Handles special action cards(Reverse, Skip, Draw One, Wild, Wild Draw Two) after card has been played.
     *
     * @param card the action card played
     */
    private void handleActionCard(Card card) {
        switch (card.getType()) {
            //case for regular card
            case NUMBER -> nextTurn();
            //case for reverse card
            case REVERSE -> {
                direction *= -1;
                nextTurn();
            }
            //case for skip card
            case SKIP -> {
                skipTurn(1); //finishes current player turn?
                nextTurn();
            }
            //case for draw_one card
            case DRAW_ONE -> {
                skipTurn(1);
                getCurrentPlayer().drawCard(deck);
                nextTurn();
            }
            //case for draw_five card
            case DRAW_FIVE -> {
                skipTurn(1);
                getCurrentPlayer().drawCard(deck);
                getCurrentPlayer().drawCard(deck);
                getCurrentPlayer().drawCard(deck);
                getCurrentPlayer().drawCard(deck);
                getCurrentPlayer().drawCard(deck);
                nextTurn();
            }
            //case for skipping everyone
            case SKIP_EVERYONE -> {
                for (int i = 0; i < players.size() - 1; i++) {
                    skipTurn(1);
                }
                nextTurn();
            }
            default -> {
            }
        }
    }

    /**
     * Handles logic for when a wild card is played. If wild card is played check if valid move
     * then ask user for input on colour then push card to discard pile.
     *
     * @param card is the current card the player is trying to play.
     */
    public void handleWildCard(Card card) {
        if (checkValidMove(card)) { //check if valid move

            String input;
            CardColour colour;

            if (getCurrentPlayer() instanceof AiPlayer) { //AI choosing color for wild card
                colour = ((AiPlayer) getCurrentPlayer()).getWildColour();
                System.out.println("AI chose color: " + colour.toString());

            } else {
                //loop to try until get a correct color
                while (true) {
                    //8 different colors between both sides
                    if (card.getSide().equals(CardSide.LIGHT)) {
                        input = JOptionPane.showInputDialog("choose a color (RED, BLUE, GREEN, YELLOW): ");
                    } else {
                        input = JOptionPane.showInputDialog("choose a color (BROWN, PURPLE, TEAL, ORANGE): ");
                    }
                    try {
                        colour = CardColour.valueOf(input.trim().toUpperCase()); //attempt to get object

                        //error checking correct colour selected based on which side it is.
                        if (card.getSide() == CardSide.LIGHT && colour.ordinal() < 4) {
                            break;
                        } else if (card.getSide() == CardSide.DARK && colour.ordinal() > 4) {
                            break;
                        } else {
                            JOptionPane.showMessageDialog(null, "Invalid color.");
                        }

                    } catch (IllegalArgumentException e) {
                        JOptionPane.showMessageDialog(null, "Invalid color");
                    }
                }
            }

            card.setColour(colour); //set colour of wild card
            pushToDiscardPile(card); //add to top of discard pile

            //nextTurn();

            //case for wild draw two cards
            if (card.getType() == CardType.WILD_DRAW_TWO) {
                //System.out.println("draw two happened");
                skipTurn(1);
                getCurrentPlayer().drawCard(getDeck());
                getCurrentPlayer().drawCard(getDeck());
                nextTurn();
            } else if (card.getType() == CardType.WILD_DRAW_COLOR) {
                //Extra logic for draw color card
                skipTurn(1);

                //emulating nextTurn
                updateAll();

                wildDrawColour = card.getColour(); //get root colour
                wildDraw = true; //flag for loop
                if (!(getCurrentPlayer() instanceof AiPlayer)) {
                    play.setEnabled(false); //disabling play button
                    draw.setEnabled(true); //allow user to draw card
                    JOptionPane.showMessageDialog(null, "Player must draw color to continue");

                } else { //AI handle draw colour
                    handleAiDrawColour();

                }

            } else {
                nextTurn();
            }
        }

    }

    /**
     * Method to handle the AI while drawing for a colour when a draw colour card is played.
     */
    private void handleAiDrawColour() {
        while (wildDraw) {
            Card card = deck.drawCard(); //draw card

            getCurrentPlayer().addCardtoHand(card); //add card to hand

            System.out.println("AI added card from draw colour loop");

            if (card.getColour() == wildDrawColour) { //if equal to colour must draw
                wildDraw = false;

                System.out.println("AI drew colour");

                handleAfterDraw(); //continue game logic

            }
        }
    }

    /**
     * Get boolean if we're in a draw colour loop.
     *
     * @return True if draw colour card played.
     */
    public boolean getWildDrawLoop() {
        return wildDraw;
    }

    /**
     * Let you set if we're in draw colour loop or not, controlled by logic and Controller.
     *
     * @param bool False if exiting draw colour loop.
     */
    public void setWildDrawLoop(boolean bool) {
        wildDraw = bool;
    }

    /**
     * Get the current colour of the card in the draw colour loop.
     *
     * @return CardColour of the draw colour card.
     */
    public CardColour getDrawLoopColour() {
        return wildDrawColour;
    }

    /**
     * Skip the player turn without initializing any turn logic.
     *
     * @param skip int for how many players you wish to skip.
     */
    public void skipTurn(int skip) {
        System.out.println("skipTurn() called");

        drawCard = false;

        for (int i = 0; i < skip; i++) {
            currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        }

        updateDiscardPile(); //for UI
        updateCurrentPlayer(); //for UI

    }


    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //Sequence 4: NextTurn logic and display new player hand.

    /**
     * Method that changes game state to go to the next player. Initializes game logic.
     */
    public void nextTurn() {
        System.out.println("nextTurn() called");

        currentPlayerIndex = (currentPlayerIndex + direction + players.size()) % players.size();
        drawCard = false;

        updateAll();
        handleInitialHand(); //going back to seq 2

    }

    /**
     * Everytime updatePlayerCards() is called these variables need to be reset to avoid null pointers.
     */
    private void resetVars() {
        prevCardZ.clear(); //reset map for updated hand
        hoveredButton = null; //reset variable
        selectedCard = null; //reset variable
    }

    /**
     * Updates discard pile View by creating new card UI/button from top of stack then adding to JPanel in View.
     */
    private void updateDiscardPile() {
        Card topCard = topDiscard(); //get top card

        //create JButton emulating player hand
        JButton discard = new JButton();
        discard.setEnabled(false);
        discard.setFocusPainted(false);

        //set style
        setCardStyle(discard, topCard);

        discard.setBounds(
                300,
                100,
                130,
                200
        );

        //add to JFrame in view
        view.addCenterCard(discard);
    }

    /**
     * Updates the JLabel in view for which player is playing
     */
    private void updateCurrentPlayer() {
        String name = getCurrentPlayer().getName(); //gets current player name
        view.currentPlayerDisplay(name);
        view.addUpdateScore(getCurrentPlayer().getScore());
        view.addGameStats(roundCounter, gameCounter); //for round and game out for player to see
    }


    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //Sequence 5: Can't play card, must draw.

    /**
     * Sets enable for draw button based on logic, changed by controller
     * @param bool boolean set enable or disable
     */
    public void setButtonBool(boolean bool) {
        draw.setEnabled(bool);
    }

    /**
     * Current player can draw one card from the deck
     */
    public void drawCard() {
        Player p = getCurrentPlayer();
        if (wildDraw) { //required for draw colour loop
            displayHand(); //only need to update UI hand

            //regular draw logic, only need to draw card and that's it
        } else if (!drawCard && (!(getCurrentPlayer().hasPlayableCard(topDiscard())))) {
            drawCard = true;
            p.drawCard(deck);

        }

    }


    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //Universal methods used throughout

    /**
     * Gets the current player whose turn it is
     *
     * @return the current player
     */
    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    /**
     * Gets the top card of the discard pile
     *
     * @return the top discard card
     */
    public Card topDiscard() {
        return discardPile.peek();
    }

    /**
     * Gets the list of all players in the game.
     *
     * @return list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Gets the deck
     *
     * @return deck
     */
    public Deck getDeck() {
        return deck;
    }

    /**
     * Gets the discard pile we are working with
     *
     * @return the discard pile
     */
    public Stack<Card> getDiscardPile() {
        return discardPile;
    }

    /**
     * Gets the current player index
     *
     * @return the index of the current player
     */
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    /**
     * Gets the player JPanel for the interactive card/JButton view.
     *
     * @return JPanel to edit/remove/add components.
     */
    private JPanel getPlayerCards() {
        return view.getPlayerCards(); //JPanel for player cards
    }

    /**
     * Used for debugging.
     *
     * @return int of current game state.
     */
    public int getSeq() {
        sequence = sequence + 1;
        return sequence;
    }

    /**
     * Used to update discard pile, player name and score, player cards in UI.
     */
    private void updateAll() {
        updateDiscardPile(); //for UI
        updateCurrentPlayer(); //for UI
        displayHand(); //display new player cards
    }

    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //Logic for round winner or game winner

    /**
     * Gets the round winner at the end of the round.
     * @return player with zero cards
     */
    public Player getRoundWinner() {
        for (Player p : players) {
            if (p.gethand().isEmpty()) {
                return p;
            }
        }
        return null;
    }

    /**
     * Updates the winner's score by adding the total points
     * from all other players' hands
     */
    public void updatePlayerScore() {
        Player winner = getRoundWinner();
        if (winner == null) return;

        int totalPoints = 0;
        //add up total points value
        for (Player p : players) {
            if (p != winner) {
                totalPoints += p.gethandValue();
            }
        }

        winner.addScore(totalPoints);
    }

    /**
     * Check if there is a winner and starts next round.
     *
     * @return True if there is a winner.
     */
    public boolean checkWinner() {

        if (checkEmptyHand()) { //verifies if a player has an empty hand
            roundCounter++; //round over so increment
            updatePlayerScore();

            JOptionPane.showMessageDialog(null, "Game Over " + getRoundWinner().getName() + " Won");
            restartGame();



            return true;
        }

        return false;
    }

    /**
     * Checks if the player's hands are empty
     * @return true or false depending on if hand is empty
     */
    public boolean checkEmptyHand() {
        //check every player's hand
        for (Player p : players) {
            if (p.gethand().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Reset all player scores.
     */
    private void resetPlayerScores() {
        for (Player p : players) {
            p.resetScore();
        }

    }

    /**
     * Handles resetting everything needed to restart a new round.
     */
    private void restartGame() {
        if(getRoundWinner().getScore() >= 50) {

            Object[] options = {"YES", "NO"};
            JPanel endPrompt = new JPanel();
            endPrompt.add(new JLabel("Do you wish to start a new Game?"));

            int result = JOptionPane.showOptionDialog(null, endPrompt, "Game System Administrator", JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, null);
            if (result == JOptionPane.YES_OPTION) {
                resetPlayerScores();

                gameCounter++; //new game so increment
                roundCounter = 0; //restart round counter

            } else { //no option
                JOptionPane.showMessageDialog(null, "Game Over");
                System.exit(0);
            }
        }

        //refresh deck
        Deck deck = getDeck();
        deck.newDeck();

        //new flags
        resetVars();

        //clear player hands
        for (Player player : getPlayers()) {
            player.clearHand();
        }

        startGame();
        System.out.println("updateView() called by: " + getCurrentPlayer().getName() + " in restartGame(); state: " + getSeq());
    }


    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    /* *************************************************************** */
    //All UI functionality methods

    /**
     * Removes all components from UI view and adds updated hand. Player hand is JButtons with a dynamic space
     * loop for the cards to never be overlapping.
     */
    public void displayHand() {
        Player currentPlayer = getCurrentPlayer(); //get current player

        JPanel playerCards = getPlayerCards(); //get player card/JButton JPanel

        playerCards.removeAll(); //clear old components
        resetVars(); //clears z map, sets hoveredButton and selectedCard null so not referencing old cards

        //offset for handling multiple cards on a fixed JFrame
        int offset = 50;
        if (currentPlayer.gethand().isEmpty()) { //error out if hand empty
            if (checkWinner()) { //don't want to go through extra logic if winner
                return;
            }
        } else {
            offset = (playerCards.getPreferredSize().width - 180) / getCurrentPlayer().gethand().size();
        }

        //main loop creates buttons and adds to JPanel
        for (int i = 0; i < getCurrentPlayer().gethand().size(); i++) {
            JButton buttonCard = new JButton();

            buttonCard.setBounds(
                    (i == 0) ? 50 : 50 + offset * i,
                    30,
                    130,
                    200
            );
            buttonCard.setFocusPainted(false); //disable focus indicator

            buttonCard.putClientProperty("index", i); //hidden button index that is parallel with player hand list

            //adding mouse listeners
            view.addButtonCard(buttonCard);

            //setting style of card/JButton
            setCardStyle(buttonCard, getCurrentPlayer().gethand().get(i));

            playerCards.add(buttonCard); //add to JPanel
            playerCards.setComponentZOrder(buttonCard, i); //set z layer order by index
            addButtonZ(buttonCard, i); //add to Map for dynamically changing z layer and reverting


        }

        //add panel to view
        view.addPanel(playerCards, BorderLayout.SOUTH);
        view.repaint(); //just in case missed something
    }

    /**
     * setCardStyle determines what the card will look like in the UI based on CardColour and CardType and value.
     * Sets colour, sets numerical or special card, adjusts font, set styling visible
     *
     * @param buttonCard is the button we're styling based on card
     * @param card used to grab styling attributes
     *
     */
    private void setCardStyle(JButton buttonCard, Card card) {
        //set color
        switch (card.getColour()) {
            case RED -> buttonCard.setBackground(new Color(156, 24, 9));
            case BLUE -> buttonCard.setBackground(new Color(80, 139, 235));
            case GREEN -> buttonCard.setBackground(new Color(29, 161, 31));
            case YELLOW ->  buttonCard.setBackground(new Color(201, 196, 26));
            case BROWN ->  buttonCard.setBackground(new Color(99, 49, 11));
            case PURPLE -> buttonCard.setBackground(new Color(93, 2, 163));
            case TEAL -> buttonCard.setBackground(new Color(28, 201, 196));
            case ORANGE -> buttonCard.setBackground(new Color(199, 85, 14));
        }

        if (card.getColour().equals(CardColour.WILD)) { //wild cards
            if (card.getSide().equals(CardSide.LIGHT)) {
                buttonCard.setBackground(Color.WHITE); //set background
                buttonCard.setForeground(Color.BLACK); //setting style for text
                buttonCard.setBorder(new LineBorder(Color.BLACK, 6)); //set border
            } else {
                buttonCard.setBackground(Color.BLACK); //set background
                buttonCard.setForeground(Color.WHITE); //setting style for text
                buttonCard.setBorder(new LineBorder(Color.WHITE, 6)); //set border
            }
        } else { //anything that not wild cards
            buttonCard.setForeground(Color.WHITE); //setting style for text

            if (card.getSide().equals(CardSide.LIGHT)) {
                buttonCard.setBorder(new LineBorder(Color.BLACK, 6)); //set border
            } else {
                buttonCard.setBorder(new LineBorder(Color.WHITE, 6)); //set border
            }
        }

        //set card type/value
        if (card.getType() == CardType.NUMBER) {
            buttonCard.setText(String.valueOf(card.getValue()));
        } else { //everything that's not a number
            buttonCard.setText(card.getType().toString().replace('_', ' '));
        }

        //set font based on length
        String text = card.getType().toString().replace('_', ' ');
        buttonCard.setFont(
                (text.length() > 8) ? new Font("Arial", Font.BOLD, 12) : new Font("Arial", Font.BOLD, 18)
        );

        //ensure button style is visible
        buttonCard.setOpaque(true);
        buttonCard.setContentAreaFilled(true);
        buttonCard.setBorderPainted(true);

        //return buttonCard;
    }

    /**
     * method to add the UNO card/JButton to a map with its z index.
     * Z index for keeping track of its layer order in view as it dynamically changes off of cursor position.
     * Use of Map to recall and needs to be cleared before every updatePlayerCards().
     *
     * @param button is the card/key
     * @param z the z index for the view layer order
     */
    public void addButtonZ(JButton button, int z) {
        prevCardZ.put(button, z); //add to Map
    }

    /**
     * Handles when cursor is above card/JButton, called when mouse listener.
     * Will reset previous card/JButton to original state then update the newest one
     * with the highest z order for layers and shift it up.
     *
     * @param buttonCard the player will see interacting with.
     */
    public void handleHover(JButton buttonCard) {
        if (hoveredButton != buttonCard) { //button not already hovered on
            resetHover(); //set back down previous
            hoveredButton = buttonCard;

            JPanel playerCards = getPlayerCards(); //get player panel
            playerCards.setComponentZOrder(buttonCard, 0);

            //make hover
            buttonCard.setLocation(buttonCard.getX(), buttonCard.getY() - 10);
            playerCards.repaint();
        }
    }

    /**
     * Opposite of handleHover method. Put previous hovered card back to original state and z order for the
     * component layer.
     */
    public void resetHover() {
        if (hoveredButton != null) {
            JPanel playerCards = getPlayerCards();
            hoveredButton.setLocation(hoveredButton.getX(), hoveredButton.getY() + 10); //set back down

            Integer z = prevCardZ.get(hoveredButton);
            if (z != null) { //set previous card z index layer
                playerCards.setComponentZOrder(hoveredButton, z);
            }

            hoveredButton = null;
            playerCards.repaint();
        }
    }

    /**
     * Allows player to select a card and attempt to play it. Card will be automatically assessed.
     * If card selected is not a valid move the play button will not be enabled.
     * As well as makes the card more visible and stand out from the interactive GUI.
     *
     * @param button event handler passed from event, i.e. card/JButton pressed.
     */
    public void handleCardPressed(JButton button) {
        JPanel playerCards = getPlayerCards(); //get player panel

        if (selectedCard == null) { //no card select (keep raised)
            selectedCard = button;
            playerCards.setComponentZOrder(selectedCard, 0);
            button.setLocation(button.getX(), button.getY() - 20);

        } else if (selectedCard == button) { //same card (deselect)
            playerCards.setComponentZOrder(selectedCard, prevCardZ.get(selectedCard));
            selectedCard.setLocation(selectedCard.getX(), selectedCard.getY() + 20);

            selectedCard = null;

        } else { //another card selected (deselect current then select other)
            playerCards.setComponentZOrder(button, prevCardZ.get(selectedCard));
            selectedCard.setLocation(selectedCard.getX(), selectedCard.getY() + 20);

            selectedCard = button;

            playerCards.setComponentZOrder(selectedCard, 0);
            button.setLocation(button.getX(), button.getY() - 20);
        }

        if (selectedCard != null) { //method to disable or enable play button (play card)
            playableCard(selectedCard);
        }


        playerCards.repaint(); //changing card coords and z index
    }

    /**
     * Extra logic for handleCardPressed method for enabling or disabling play button if card is playable.
     *
     * @param selectedCard selected button from event.
     */
    private void playableCard(JButton selectedCard) {
        Card card = getCurrentPlayer().gethand().get((int) selectedCard.getClientProperty("index")); //get card from button hidden index
        play.setEnabled(checkValidMove(card));
        view.repaint();
    }

}


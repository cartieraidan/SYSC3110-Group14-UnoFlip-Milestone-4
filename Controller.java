import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * The Controller class manages the state and flow of the GUI UNO game.
 * Controller most of the game flow from interactions with the GUI and handle the logic to handle.
 *
 *
 * @author Aidan Cartier
 * @version November 10, 2025
 */
public class Controller implements MouseListener, MouseMotionListener, ActionListener {

    private UnoView view;
    private ArrayList<Player> players;
    private GameManager gameManager;

    private boolean gameOver;

    /**
     * Constructs a Controller that handles a view and model of UNO
     *
     */
    public Controller() {
        view = new UnoView();
        view.setController(this);
        players = new ArrayList<>();

        //setup game
        gameManager = new GameManager(players);
        gameManager.setView(view);
        gameManager.initializeControls();
        view.subscribe(gameManager);
        gameManager.startGame();

        view.setVisible(true);

        gameOver = false;


    }

    /**
     * Returns the controllers view
     *
     * @return the controllers view
     */
    public UnoView getView() {
        return view;
    }

    /**
     * When cursor leaves area over card/JButton.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseExited(MouseEvent event) {
        gameManager.resetHover();
    }

    /**
     * Implemented for whenever a UNO/JButton Card is pressed, sends logic to handleCardPressed method.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseClicked(MouseEvent event) {
        JButton buttonCard = (JButton) event.getSource(); //get button source

        gameManager.handleCardPressed(buttonCard);

    }

    /**
     * Mouse event for when cursor goes over the button.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseMoved(MouseEvent event) {
        //get current card hovering over
        JButton buttonCard = (JButton) event.getSource();

        gameManager.handleHover(buttonCard);
    }

    /**
     * Not required so not implemented.
     *
     * @param event the event to be processed
     */
    @Override
    public void mousePressed(MouseEvent event) {

    }

    /**
     * Not required so not implemented.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseReleased(MouseEvent event) {

    }

    /**
     * Not required so not implemented.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseEntered(MouseEvent event) {

    }

    /**
     * Not required so not implemented.
     *
     * @param event the event to be processed
     */
    @Override
    public void mouseDragged(MouseEvent event) {

    }

    /**
     * Action listener for the game control buttons like quit, play, draw
     *
     * @param event the event to be processed
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        JButton button = (JButton) event.getSource();

        if (button.getText().equals("Quit")) {
            System.exit(0);

        } else if (button.getText().equals("Play")) { //play card
            System.out.println("play called by: " + gameManager.getCurrentPlayer().getName() + " state: " + gameManager.getSeq());
            gameManager.playCard();

        } else if (button.getText().equals("Draw")) { //draw card
            System.out.println("draw called by: " + gameManager.getCurrentPlayer().getName() + " state: " + gameManager.getSeq());
            Player player = gameManager.getCurrentPlayer();

            if (gameManager.getWildDrawLoop()) { //if player plays draw colour card
                CardColour loopColour = gameManager.getDrawLoopColour(); //colour must draw

                Deck deck = gameManager.getDeck(); //getting deck
                Card drawCard = deck.drawCard(); //drawing card from deck

                player.addCardtoHand(drawCard); //adding to player hand
                if (drawCard.getColour() == loopColour) {
                    gameManager.setWildDrawLoop(false); //exit draw loop
                    gameManager.setPlayButton(true); //enable play card button
                    gameManager.setButtonBool(false); //disable draw button

                    System.out.println("player drew colour");
                } else {
                    JOptionPane.showMessageDialog(null, "Keep drawing");
                }

                gameManager.displayHand(); //update view?

            } else {
                // Draw one card
                gameManager.drawCard();
                gameManager.setButtonBool(false); //disable draw for user after draw card
                gameManager.handleAfterDraw(); //continues game logic after draw card

            }



            System.out.println("updateView() called by: " + gameManager.getCurrentPlayer().getName() + " in Controller draw; state: " + gameManager.getSeq());
            //gameManager.updateView();
        }
    }

    public static void main(String[] args) {
        UnoView unoView = new UnoView();
        Controller controller = new Controller();
    }
}

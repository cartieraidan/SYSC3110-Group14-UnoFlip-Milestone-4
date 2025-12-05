import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Stack;

/**
 * The Controller class manages the state and flow of the GUI UNO game.
 * Controller most of the game flow from interactions with the GUI and handle the logic to handle.
 *
 *
 * @author Aidan Cartier, Mark Bowerman
 * @version December 5, 2025
 */
public class Controller implements MouseListener, MouseMotionListener, ActionListener, StateListener {

    private UnoView view;
    private ArrayList<Player> players;
    private GameManager gameManager;

    private boolean gameOver;

    //undo and redo stacks
    private Stack<Snapshot> undoStack = new Stack<>();
    private Stack<Snapshot> redoStack = new Stack<>();

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
        gameManager.setListener(this);
        gameManager.initializeControls();
        gameManager.initialPlayers();
        view.subscribe(gameManager);

        gameManager.setGameState(GameState.NEW_ROUND); //saving initial game snapshot
        gameManager.startGame();

        view.setVisible(true);

        gameOver = false;


    }

    /**
     * For enabling and disabling the undo and redo buttons for when the user can use them.
     */
    private void updateStackButtons() {
        view.updateRedoButton(!redoStack.isEmpty());
        view.updateUndoButton(!undoStack.isEmpty());
    }

    /**
     * Push a snapshot to the undo stack. Should be called before any player action that changes the game.
     * Interface implementation for StateListener, called everything state change.
     */
    @Override
    public void saveSnapshotForUndo(GameState state) {
        undoStack.push(new Snapshot(gameManager, state)); //create copy and add to stack
        redoStack.clear(); //clear redo when a new move is made

        this.updateStackButtons(); //updates view of buttons
    }

    /**
     * Undo a move. Saves the current game state to the redo stack and pops from undo stack. Then reattaches and updates
     * the GUI.
     */
    public void undo() {
        if(!undoStack.isEmpty()){
            redoStack.push(new Snapshot(gameManager, gameManager.getGameState())); //save current state for redo
            Snapshot prev = undoStack.pop();
            gameManager = prev.getGameManagerCopy();
            gameManager.setView(view); //reattach GUI
            gameManager.displayHand(); //update GUI

            this.updateStackButtons(); //updates view of buttons

            prev.executeState(); //executes game logic
        }
        //should probably add an else in case there is nothing to undo
    }

    /**
     * Redo a move. Saves the current game state to the undo stack and pops from redo stack. Then reattaches and updates
     * the GUI.
     */
    public void redo() {
        if(!redoStack.isEmpty()){
            undoStack.push(new Snapshot(gameManager, gameManager.getGameState())); //save current state for undo
            Snapshot prev = redoStack.pop();
            gameManager = prev.getGameManagerCopy();
            gameManager.setView(view); //reattach GUI
            gameManager.displayHand(); //update GUI

            this.updateStackButtons(); //updates view of buttons

            prev.executeState(); //executes game logic
        }
        //should probably add an else in case there is nothing to redo
    }

    /**
     * Saves the current game state as a snapshot to a file in the saves folder
     * @param filename the name of the file to be saved
     */
    public void saveGame(String filename){

        File saveDir = new File("saves");
        if(!saveDir.exists()){ //make sure saves folder exists otherwise create it
            saveDir.mkdir();
        }

        Snapshot snap = new Snapshot(gameManager,  gameManager.getGameState());
        try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("saves/" + filename))){
            out.writeObject(snap);
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * Loads the game state from the chosen file in saves folder
     * @param filename the name of the file to be loaded from
     */
    public void loadGame(String filename){
        try(ObjectInputStream in = new ObjectInputStream(new FileInputStream("saves/" + filename))){
            Snapshot snap = (Snapshot) in.readObject();
            gameManager = snap.getGameManagerCopy();
            gameManager.setView(view); //reattach GUI
            gameManager.displayHand(); //update GUI

            //clear stacks after loading
            undoStack.clear();
            redoStack.clear();

            this.updateStackButtons(); //updates view of buttons, might or might not need this....

            snap.executeState(); //executes game logic

        } catch (IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
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

        } else if (button.getText().equals("Undo")) { //User presses undo button
            this.undo();

        } else if (button.getText().equals("Redo")) { //User press redo button
            this.redo();

        }
    }

    public static void main(String[] args) {
        UnoView unoView = new UnoView();
        Controller controller = new Controller();
    }
}

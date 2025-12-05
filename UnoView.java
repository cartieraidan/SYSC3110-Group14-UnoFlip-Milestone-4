

import javax.swing.*;
import java.awt.*;

public class UnoView extends JFrame {

    private JPanel playerCards;
    private JPanel topPanel;

    private JPanel leftPanel;
    private JPanel rightPanel;
    private JPanel centerPanel;

    private JPanel player2;

    private JButton play;
    private JButton draw;

    GameManager gameManager;
    Controller controller;

    /**
     * Constructor that initializes most of all game panel.
     */
    public UnoView() {

        setTitle("Uno");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
        setSize(1000, 800);


        //only for testing purposes, will separate
        playerCards = new JPanel();
        playerCards.setBackground(Color.LIGHT_GRAY);
        playerCards.setPreferredSize(new Dimension(800, 300));
        playerCards.setLayout(null);

        topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setBackground(Color.lightGray);
        topPanel.setPreferredSize(new Dimension(800, 200));

        centerPanel = new JPanel(null);
        centerPanel.setPreferredSize(new Dimension(400, 400));
        add(centerPanel, BorderLayout.CENTER);

        leftPanel = new JPanel(new GridLayout(2, 2)); //first row is player name and score, second round and game
        rightPanel = new JPanel();

        topPanel.add(leftPanel);
        topPanel.add(rightPanel);

        add(topPanel,  BorderLayout.NORTH);


        player2 =  new JPanel();
        player2.setBackground(Color.GREEN);
        player2.setPreferredSize(new Dimension(800, 100));



    }

    /**
     * Game operation buttons.
     */
    public void addControlButtons() {
        JPanel panel = getRightPanel(); //JPanel for game controls

        //quit JButton
        JButton quit = new JButton("Quit");
        quit.addActionListener(controller);
        panel.add(quit);

        //play JButton
        play = new JButton("Play");
        play.addActionListener(controller);
        panel.add(play);

        //draw JButton
        draw = new JButton("Draw");
        draw.addActionListener(controller);
        draw.setEnabled(false);
        panel.add(draw);
    }

    /**
     * Method for adding discard card on pile.
     *
     * @param button Card player played.
     */
    public void addCenterCard(JButton button) {
        centerPanel.removeAll();
        centerPanel.add(button);
        revalidate();
        repaint();
    }

    /**
     * Method for adding panel to JFrame.
     *
     * @param panel JPanel adding to frame.
     * @param layout Location of frame.
     */
    public void addPanel(JPanel panel, String layout) {
        add(panel, layout);
        pack();
    }

    /**
     * Panel that is the main player cards container.
     *
     * @return JPanel of current player container.
     */
    public JPanel getPlayerCards() {
        return playerCards;
    }

    /**
     * Get Panel for game controls.
     *
     * @return JPanel.
     */
    public JPanel getRightPanel() {
        return rightPanel;
    }

    /**
     * Get Play button.
     *
     * @return JButton of play.
     */
    public JButton getPlayButton() {
        return play;
    }

    /**
     * Get Draw button.
     *
     * @return JButton of draw.
     */
    public JButton getDrawButton() {
        return draw;
    }

    /**
     * Updates the banner for player name.
     *
     * @param name String name of character.
     */
    public void currentPlayerDisplay(String name) {
        JLabel playerName = new JLabel("Player: " + name);
        playerName.setFont(new Font("Arial", Font.BOLD, 14));
        playerName.setForeground(Color.black);

        leftPanel.removeAll(); //clear JLabel
        leftPanel.add(playerName);

        repaint();
    }

    /**
     * Updates the banner for player score.
     *
     * @param score Int of score player.
     */
    public void addUpdateScore(int score) {
        JLabel scoreLabel = new JLabel("Score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 14));
        scoreLabel.setForeground(Color.black);

        leftPanel.add(scoreLabel);

        repaint();
    }

    public void addGameStats(int round, int game) {
        JLabel roundLabel = new JLabel("Round: " + round);
        JLabel gameLabel = new JLabel("Game: " + game);
        roundLabel.setFont(new Font("Arial", Font.BOLD, 14));
        gameLabel.setFont(new Font("Arial", Font.BOLD, 14));

        leftPanel.add(roundLabel);
        leftPanel.add(gameLabel);

        repaint();
    }

    public void subscribe (GameManager gameManager) {

        this.gameManager = gameManager;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void addButtonCard(JButton buttonCard) {
        buttonCard.addMouseMotionListener(controller);
        buttonCard.addMouseListener(controller);
    }
}

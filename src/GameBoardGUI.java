import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

/**
 * Class to create GUI.
 * @author Michael Servilla
 * @version date 2017-04-30
 */
class GameBoardGUI extends JFrame {
    private static JFrame mainFrame = new JFrame("COLUMNS");
    private static JLabel scoreLabel = new JLabel("Score: ");
    private JToggleButton toggleStart = new JToggleButton("START", true);
    static Grid grid = new Grid();
    private BlockManager game = new BlockManager();
    private Timer timer;


    /**
     * Constructor for GUI class using 1 JFrame, 4 JPanels, 1 JLabel,
     * 1 JToggleButton, and 1 timer.
     */
    GameBoardGUI() {
        mainFrame.setFocusable(false);
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setFocusable(false);
        JPanel topPanel = new JPanel();
        topPanel.setFocusable(false);
        JPanel bottomPanel = new JPanel();
        bottomPanel.setFocusable(false);
        JPanel gameBoard = new JPanel(new BorderLayout());
        gameBoard.setFocusable(false);
        scoreLabel.setFocusable(false);
        grid.setFocusable(false);

        toggleStart.addItemListener(e -> handleToggle(e));
        toggleStart.setForeground(Color.GREEN.darker().darker());

        mainPanel.setBackground(Color.black);
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(gameBoard, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        gameBoard.setBackground(Color.white);
        gameBoard.setLocation(0, 0);
        gameBoard.setSize(600, 800);
        gameBoard.add(grid);


        bottomPanel.add(toggleStart);
        topPanel.add(scoreLabel);

        grid.setBackground(Color.LIGHT_GRAY);
        mainFrame.isFocusable();
        mainFrame.requestFocus();

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(525, 907);
        mainFrame.setForeground(Color.BLACK);
        mainFrame.setContentPane(mainPanel);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        grid.fillCell(game.getBoardList());

        timer = new Timer(500, e -> game.runGame());
    }
    /**
     * Method to handle JToggleButton for start and pause.
     * @param e Event parameter.
     */
    private void handleToggle(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            timer.stop();
            toggleStart.setForeground(Color.GREEN.darker().darker());
            toggleStart.setText("START");
            toggleStart.transferFocus();
        } else {
            timer.start();
            toggleStart.setForeground(Color.RED);
            toggleStart.setText("PAUSE");
            toggleStart.setFocusable(true);
            toggleStart.requestFocusInWindow();
            toggleStart.requestFocus();
            toggleStart.hasFocus();
            toggleStart.addKeyListener(new KeyListener() {
                /**
                 * Overidden method from KeyListener class, not used.
                 * @param e Event parameter.
                 */
                @Override
                public void keyTyped(KeyEvent e) {
                }
                /**
                 * KeyPressed method to listen for arrow keys.
                 * @param e Event parameter.
                 */
                @Override
                public void keyPressed(KeyEvent e) {
                }
                /**
                 * Overidden method from KeyListener class, not used.
                 * @param e Event parameter.
                 */
                @Override
                public void keyReleased(KeyEvent e) {
                    int code = e.getKeyCode();
                    if (code == KeyEvent.VK_RIGHT && game.getJ() < BlockManager.COL - 1) {
                        BlockManager.columnOffset = 1;
//            System.out.println("Right arrow key pressed");
                    }
                    if (code == KeyEvent.VK_LEFT && game.getJ() > 2) {
                        BlockManager.columnOffset = -1;
//            System.out.println("Left arrow key pressed");
                    }
                    if (code == KeyEvent.VK_UP) {
                        BlockManager.rotatePieceFlag = 1;
//            System.out.println("Up arrow key pressed");
                    }
                    if (code == KeyEvent.VK_DOWN) {
                        BlockManager.dropDownFlag = 1;
//            System.out.println("Down arrow key pressed");
                    }
                }
            });
        }
    }
    /**
     * Method to set scoreLabel to current score.
     * @param scoreLabelText Text value of game score.
     */
    static void setScoreLabel(String scoreLabelText) {
        scoreLabel.setText(scoreLabelText);
    }
    /**
     * End of game dialog box.
     */
    static void endOfGameDialog() {
        JOptionPane.showMessageDialog(mainFrame, "GAME OVER!  Final " +
                "Score: " + Search.getScore());
    }
    /**
     * Inner class that is responsible for painting grid and blocks.
     */
    public static class Grid extends JPanel {
        private static ArrayList<String> fillCells;

        /**
         * Grid construct that uses paintComponent method.
         */
        Grid() {
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            for (String fillCell : fillCells) {
                String[] stringBlock = fillCell.split("\\s+");
                int x = Integer.parseInt(stringBlock[1]);
                int y = Integer.parseInt(stringBlock[0]);
                String charColor = stringBlock[2];


                int cellX = 10 + (x * 10);
                int cellY = 10 + (y * 10);
                g.setColor(getColor(charColor));
                g.fillRect(cellX, cellY, 50, 50);//Sets fill cell size.
            }
            g.setColor(Color.WHITE);
            g.drawRect(10, 10, 500, 800);

            for (int i = 10; i <= 500; i += 50) {
                g.drawLine(i, 10, i, 810);
            }

            for (int i = 10; i <= 800; i += 50) {
                g.drawLine(10, i, 510, i);
            }
        }

        /**
         * Method to take ArrayList of current board status and calls repaint.
         * @param gridList ArrayList with grid data.
         */
        void fillCell(ArrayList gridList) {
            fillCells = new ArrayList(gridList);
            repaint();
        }

        /**
         * Method that uses switch statement to determine block color.
         * @param colorCode Letters that correspond to colors.
         * @return Returns color for each block.
         */
        Color getColor( String colorCode) {
            Color cellColor = Color.LIGHT_GRAY;
            switch (colorCode) {
                case "R": cellColor = Color.RED;
                    break;
                case "B": cellColor = Color.BLUE;
                    break;
                case "Y": cellColor = Color.YELLOW;
                    break;
                case "K": cellColor = Color.BLACK;
                    break;
                case "G": cellColor = Color.LIGHT_GRAY;
            }
            return cellColor;
        }

    }
}

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

/**
 * @author Michael Servilla
 * @version date 2017-04-30
 */
public class GameBoardGUI extends JFrame {
    private JFrame mainFrame = new JFrame("COLUMNS");
    private JPanel mainPanel = new JPanel(new BorderLayout());
    private JPanel topPanel = new JPanel();
    private JPanel bottomPanel = new JPanel();
    private JPanel gameBoard = new JPanel(new BorderLayout());
    private static JLabel scoreLabel = new JLabel("Score: ");
    private JToggleButton toggleStart = new JToggleButton("START", true);
    public static Grid grid = new Grid();
    private BlockManager game = new BlockManager();
    private Timer timer;
    int colOffset;
    int dropDown;
    int pieceRotate;



    public GameBoardGUI() {

        mainFrame.setFocusable(false);
        mainPanel.setFocusable(false);
        topPanel.setFocusable(false);
        bottomPanel.setFocusable(false);
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

        timer = new Timer(5000, e -> game.runGame());
    }

    public void handleToggle(ItemEvent e) {
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
                @Override
                public void keyTyped(KeyEvent e) {

                }

                @Override
                public void keyPressed(KeyEvent e) {
                    int code = e.getKeyCode();
                    if (code == KeyEvent.VK_RIGHT) {
                        if (BlockManager.col < BlockManager.COL - 1) {
                            BlockManager.col = BlockManager.col + 1;
                        }
                        System.out.println("Right arrow key pressed");
                    }
                    if (code == KeyEvent.VK_LEFT) {
                        if (BlockManager.col > 0) {
                            BlockManager.col = BlockManager.col - 1;
                        }
                        System.out.println("Left arrow key pressed");
                    }
                    if (code == KeyEvent.VK_UP) {
                        pieceRotate = 1;
                        System.out.println("Up arrow key pressed");
                    }
                    if (code == KeyEvent.VK_DOWN) {
                        BlockManager.row = game.lastEmptyBlock(BlockManager.col);
                        System.out.println("Down arrow key pressed");
                    }
                }

                @Override
                public void keyReleased(KeyEvent e) {

                }
            });

        }
    }


    public static void setScoreLabel(String scoreLabelText) {
        scoreLabel.setText(scoreLabelText);
    }

    public void endOfGameDialog() {
        JOptionPane.showMessageDialog(mainFrame, "GAME OVER!  Final " +
                "Score: " + Search.getScore());
    }

    public int getColOffset() {
        return colOffset;
    }
    public int getDropDown() {
        return dropDown;
    }
    public int getPieceRotate() {
        return pieceRotate;
    }



    public static class Grid extends JPanel {
        private static ArrayList<String> fillCells;

        public Grid() {
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
        public void fillCell(ArrayList gridList) {
            fillCells = new ArrayList(gridList);
            repaint();
        }

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


import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
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
    private JLabel scoreLabel = new JLabel("Score: ");
    private JButton startButton = new JButton("Start");
    private JButton pauseButton = new JButton("Pause");
    public static Grid grid = new Grid();
    private BlockManager game = new BlockManager();
    private Timer timer;
    private int colOffset;
    private int dropDown;
    private int pieceRotate;



    public GameBoardGUI() {

        startButton.setBackground(Color.GREEN);

        mainPanel.setBackground(Color.black);
        mainPanel.add(topPanel, BorderLayout.PAGE_START);
        mainPanel.add(gameBoard, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.PAGE_END);

        gameBoard.setBackground(Color.white);
        gameBoard.setLocation(0, 0);
        gameBoard.setSize(600, 800);
        gameBoard.add(grid);


        bottomPanel.add(startButton);
        bottomPanel.add(pauseButton);
        topPanel.add(scoreLabel);

        grid.setBackground(Color.LIGHT_GRAY);
        grid.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (code == KeyEvent.VK_RIGHT) {
                    colOffset = 1;
                }
                if (code == KeyEvent.VK_LEFT) {
                    colOffset = -1;
                }
                if (code == KeyEvent.VK_UP) {
                    pieceRotate = 1;
                }
                if (code == KeyEvent.VK_DOWN) {
                    dropDown = 1;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        grid.requestFocus();

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(525, 907);
        mainFrame.setForeground(Color.BLACK);
        mainFrame.setContentPane(mainPanel);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        grid.fillCell(game.getBoardList());

        startButton.addActionListener(e -> startTimer());

        pauseButton.addActionListener(e -> stopTimer());

        timer = new Timer(100, e -> game.runGame());
    }


    public void setScoreLabel(String scoreLabelText) {
        scoreLabel.setText(scoreLabelText);
    }

    public void endOfGameDialog() {
        JOptionPane.showMessageDialog(mainFrame, "GAME OVER!  Final " +
                "Score: " + Search.getScore());
    }

    public void startTimer() {
        timer.start();
        startButton.setBackground(Color.LIGHT_GRAY);
        pauseButton.setBackground(Color.RED);
    }

    public void stopTimer() {
        timer.stop();
        startButton.setBackground(Color.GREEN);
        pauseButton.setBackground(Color.LIGHT_GRAY);
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


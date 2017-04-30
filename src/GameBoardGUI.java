import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

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
    private Grid grid = new Grid();


    public GameBoardGUI() {
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

        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(525, 907);
        mainFrame.setForeground(Color.BLACK);
        mainFrame.setContentPane(mainPanel);
        mainFrame.setResizable(false);
        mainFrame.setVisible(true);

        BlockManager game = new BlockManager();
        game.dropPiece(game);

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
        public static void fillCell(ArrayList gridList) {
            fillCells = new ArrayList(gridList);
//            repaint();
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * @author Michael Servilla
 * @version date 2017-04-30
 */
public class BlockManager {

    private char[][] board; //2D array for board.
    private static Random num = new Random(); //Number generator for piece making and column drop.
    static final int ROW = 16;
    static final int COL = 10;
    private ArrayList<String> boardList;
    private static boolean advanceFlag = false;
    static int row;
    static int col;
    private static char[] piece = new char[3];
    boolean isSettled = false;
    boolean redraw = false;
//    GameBoardGUI newGUI = new GameBoardGUI();




    /**
     * Constructor to build board with designated row and column size. Fills in
     * each 2D array location with '-'.
     */
    BlockManager() {

        board = new char[ROW][COL];
        for (int i = 0; i < ROW; i++) {
            for (int j = 0; j < COL; j++) {
                board[i][j] = '-';
            }
        }
    }

    public void runGame() {
        if (advanceFlag) {
            redraw = false;
            advanceOne();
        } else {
            newPiece();
        }
    }


    public void newPiece() {
        col = num.nextInt(COL);
        System.out.println("Next column is " + col + ".");
        if (!isFull(col)) {
            piece = PieceMaker.getPiece();
            for (int i = 0; i < 3; i++) {
                board[i][col] = piece[i];
            }
            System.out.println(toString());
            GameBoardGUI.grid.fillCell(getBoardList());
            System.out.println("Current score " + Search.getScore() + ".");
            GameBoardGUI.setScoreLabel("Current score: " + Search.getScore());
            advanceFlag = true;
            isSettled = false;
        } else {
            System.out.println("Column is full!");
//            gameGUI.endOfGameDialog();
            System.exit(0);
        }
    }

    public void advanceOne() {
        if (!isSettled) {
            for (col = 0; col < COL; col++) {
                if (redraw) {
                    break;
                }
                for (row = 0; row <= ROW - 2; row++) {
                    if (board[row][col] != '-' && board[row + 1][col] == '-') {
                        for (int i = row; i > row - 3 ; i--) {
                            board[i + 1][col] = board[i][col];
                        }
                        board[row - 2][col] = '-';
                        System.out.println(toString());
                        System.out.println("Current score " + Search.getScore() + ".");
                        System.out.println("Current column: " + col);
                        GameBoardGUI.setScoreLabel("Current score: " + Search.getScore());
                        isSettled = false;
                        redraw = true;
                        GameBoardGUI.grid.fillCell(getBoardList());
                        GameBoardGUI.grid.requestFocus();
                        break;
                    }
                }
            }
            if (col == COL && redraw == false) {
                isSettled = true;
                advanceFlag = false;
                Search.search(board);
                System.out.println("Number of blocks removed " +
                Search.getBlocksRemoved() + ".");
                Search.setBlocksRemoved();
                if (Search.repeat) {
                    System.out.println(toString());
                    System.out.println("Current score " + Search.getScore() + ".");
                    GameBoardGUI.setScoreLabel("Current score: " + Search.getScore());
                    isSettled = false;
                    redraw = true;
                    GameBoardGUI.grid.fillCell(getBoardList());
                    advanceFlag = true;
                    Search.bigCoordinates.clear();
                    Search.search(board);
        }
            }
        }
    }

    /**
     * Overridden method of toString to output board to console.
     * @return Returns string value of each line of the board.
     */
    @Override
    public String toString(){
        StringBuilder boardLine = new StringBuilder();
        for (int i = 0; i < ROW; i++) {
            boardLine.append("\n");
            for (int j = 0; j < COL; j++) {
                boardLine.append(board[i][j]);
            }
        }
        return boardLine.toString();
    }

    /**
     * Method to create ArrayList of Strings recreating String representation of board.
     * @return Returns ArrayList of Strings.
     */
    public ArrayList<String> getBoardList() {
        boardList = new ArrayList<>();
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                String blockLine = row * 5 + " " + col * 5 + " " + board[row][col];
                boardList.add(blockLine);
            }
        }
        return boardList;
    }

    /**
     * Method to return random column number for next piece drop.
     * @return Returns int of next column for drop.
     */
    public int getCOL() {
        int nextCOL = num.nextInt(COL);
        return nextCOL;
    }

    /**
     * Method to test whether chosen column can accept any further pieces (3
     * empty blocks starting from the top are required).
     * @param col Randomly chosen column.
     * @return True if full, false otherwise.
     */
    public boolean isFull(int col) {
        int a = 0;
        for (int i = 0; i < ROW; i++) {
            if (board[i][col] == '-') {
                a++;
            }
        }
        return (a < 3);
    }

    public int lastEmptyBlock(int col){
        int a = 0;
        for (int i = 0; i < ROW; i++) {
            if (board[i][col] == '-')
                a = i;
        }
        return a;
    }



    /**
     * Inner class to create new piece (3 blocks of randomly chosen colors from
     * 4 available colors (Red 'R", Yellow 'Y', Blue 'B', and Black 'K').
     */
    private static class PieceMaker {
        private static char[] piece = new char[3]; //3 blocks make a piece to be dropped.
        private static char[] block = {'R', 'Y', 'B', 'K'}; //4 different color choices for block color.

        /**
         * Static method to randomly create a piece object.
         * @return Returns a char array of size 3.
         */
       public static char[] getPiece() {
            for (int i = 0; i < 3; i++) {
                int n = num.nextInt(4);
                piece[i] = block[n];
            }
            return piece;
        }

        /**
         * Method to rotate blocks in piece.
         * @param piece
         * @return
         */
        public static char[] rotatePiece(char[] piece) {
           int a = 0;
           ArrayList<Character> tmpList = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                tmpList.add(piece[i]);
            }
            Collections.rotate(tmpList, 1);
            for (char b :
                    tmpList) {
                piece[a] = b;
                a++;
            }
           return piece;
        }
        /**
         *Static method that prints the piece object to the console.
         */
       public static void printPiece() {
            for (char a :
                    piece) {
                System.out.println(a);
            }
        }
    }
}

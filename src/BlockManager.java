import java.util.ArrayList;
import java.util.Random;

/**
 * BlockManager class that runs the logic of creating 2D array for the board,
 * the logic of piece position manipulation, and piece creation.
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
    static int columnOffset = 0;
    static int dropDownFlag = 0;
    static int rotatePieceFlag = 0;
    static boolean pieceDropFlag = false;

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
    /**
     * Method to that takes timer tick, and determines if new piece is needed to
     * be created, or current piece needs to advance down the board.
     */
    public void runGame() {
        if (advanceFlag) {
            redraw = false;
            advanceOne();
        } else {
            newPiece();
        }
    }
    /**
     * Method that requests piece to be made, and then determines what column
     * to drop it in. Also checks to see if column is full, and ends game if so.
     */
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
            pieceDropFlag = true;
        } else {
            System.out.println("Column is full!");
            GameBoardGUI.endOfGameDialog();
        }
    }
    /**
     * Method to to test board for empty spaces below pieces, advance the piece,
     * and respond to arrow keys.
     */
    public void advanceOne() {
        int i; // "i" is for piece advance loop.
        int j; //"j" is for column loop.
        int k; //"k" is for row loop.
        if (!isSettled) {
            for (j = 0; j < COL; j++) {
                if (redraw) {
                    break;
                }
                for (k = 0; k <= ROW - 2; k++) {
                    if (board[k][j] != '-' && board[k + 1][j] == '-') { //Checks to see if OK to advance.
                        if (board[k][j + columnOffset] == '-' && !Search.repeat && j + columnOffset >= 0 &&
                                j + columnOffset < COL && pieceDropFlag) {
                            moveHorizontal(k, j);
                        }
                        if (rotatePieceFlag == 1 && pieceDropFlag) {
                            rotatePiece(k, j);
                        }
                        if (dropDownFlag == 1 && pieceDropFlag) {
                            dropDown(k, j);
                        }
                        if (dropDownFlag == 0) {
                            for (i = k; i > k - 3; i--) {
                                board[i + 1][j] = board[i][j];
                            }
                            board[k - 2][j] = '-';
                        }
                        dropDownFlag = 0;
                        System.out.println(toString());
                        System.out.println("Current score " + Search.getScore() + ".");
                        System.out.println("Current column: " + j);
                        GameBoardGUI.setScoreLabel("Current score: " + Search.getScore());
                        isSettled = false;
                        redraw = true;
                        GameBoardGUI.grid.fillCell(getBoardList());
                        GameBoardGUI.grid.requestFocus();
                        break;
                    }
                }
            }
            if (j == COL && !redraw) {
                isSettled = true;
                advanceFlag = false;
                Search.search(board);
                System.out.println("Number of blocks removed " +
                Search.getBlocksRemoved() + ".");
                Search.setBlocksRemoved();
                pieceDropFlag = false;
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
    /**
     * Method to determine bottom of column.
     * @param col Column to be checked.
     * @return Returns int of bottom row number.
     */
    public int getLastEmptyBlock(int col){
        int a = 0;
        for (int i = 0; i < ROW; i++) {
            if (board[i][col] == '-')
                a = i;
        }
        return a;
    }
    /**
     * Method to move piece horizontally either left or right.
     * @param row Row number of lowest block.
     * @param col Column of current piece.
     */
    public void moveHorizontal(int row, int col) {
        for (int i = 0; i > -3; i--) {
            board[row + i][col + columnOffset] = board[row + i][col];
            board[row + i][col] = '-';
        }
        columnOffset = 0;
    }
    /**
     * Method to rotate piece.
     * @param row Row number of the lowest block.
     * @param col Column of the current piece.
     */
    public void rotatePiece(int row, int col) {
        char tmpBlock = board[row][col];
        board[row][col] = board[row - 1][col];
        board[row - 1][col] = board[row - 2][col];
        board[row - 2][col] = tmpBlock;
        rotatePieceFlag = 0;
    }
    /**
     * Method to drop piece to bottom of the column.
     * @param row Row number of the lowest block.
     * @param col Column of the current piece.
     */
    public void dropDown(int row, int col) {
        int lastEmptyBlock = getLastEmptyBlock(col);
        for (int i = 0; i > -3 ; i--) {
            board[lastEmptyBlock + i][col] = board[row + i][col];
            board[row + i][col] = '-';
        }
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

    }
}

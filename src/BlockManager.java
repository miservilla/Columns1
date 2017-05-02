import javax.swing.*;
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
    private static final int ROW = 16;
    private static final int COL = 10;
    private ArrayList<String> boardList;
    private static boolean advanceFlag = false;
    private Timer timer;
    private static int row;
    private static int col;
    private static char[] piece = new char[3];
    private static int tmpRow;



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
     * Method to get get representation of game board.
     * @return Char 2D array of board.
     */
    public char[][] getBoard() {
        return board;
    }

    /**
     * Method to get number of rows.
     * @return Int number of rows.
     */
    public static int getRow() {
        return ROW;
    }

    /**
     * Method to get number of columns.
     * @return Int number of columns.
     */
    public static int getCol() {
        return COL;
    }

    public void nextAction() {
        if (advanceFlag) {
            blockAdvanceByOne();
        } else {
            newPiece();
        }
    }


//TODO Need to build offset limits to prevent off board or into full column.
    public void newPiece() {
        col = num.nextInt(COL);
        if (!isFull(col)) { //This while loop initiates dropping piece sequence.
            tmpRow = 0;
            piece = PieceMaker.getPiece();
            row = 0;
            advanceFlag = true;
        } else {
            System.out.println("Column is full!");
            GameBoardGUI.endOfGameDialog();
            System.exit(0);
        }
    }


    public void blockAdvanceByOne() {
        if (row < lastEmptyBlock(col) && row + 2 < ROW) { //This while loop designates one block down movement.
            if (col + GameBoardGUI.getColOffset() >= 0 ||
                    col + GameBoardGUI.getColOffset() < ROW ||
                    board[row][col + GameBoardGUI.getColOffset()] == '-') {
                col = col + GameBoardGUI.getColOffset();
            }
            for (int i = 2; i >= 0; i--) {
                if (GameBoardGUI.getPieceRotate() == 1) {
                    piece = PieceMaker.rotatePiece(piece);
                }
                board[row + i][col] = piece[i];
                if (GameBoardGUI.getDropDown() == 1) {
                    board[lastEmptyBlock(col)][col] = piece[i];
                }
            }
            System.out.println(toString());
            GameBoardGUI.grid.fillCell(getBoardList());
            GameBoardGUI.setScoreLabel("Current score: " + Search.getScore());
            board[row][col] = '-';
            tmpRow = row;
            row++;
        }
        goSearch();
        advanceFlag = false;
    }

    public void goSearch() {
        board[tmpRow][col] = piece[0];
        Search.search(board);
        System.out.println("\nAfter blocks fall...");
        System.out.println(toString());
        System.out.println("Number of blocks removed " +
                Search.getBlocksRemoved() + ".");
        Search.setBlocksRemoved();
        System.out.println("Current score " + Search.getScore() + ".");
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
    public static int getCOL() {
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

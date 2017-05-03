import java.util.HashSet;
/**
 * Search class algorithm that looks for 3 or more matches of blocks in all 8
 * directions.
 * @author Michael Servilla
 * @version date 2017-04-30
 */
class Search {
    static boolean repeat;
    private static int score;
    private static int blocksRemoved;
    private static int[] x = {-1, -1, 0, 1, 1, 1, 0, -1};
    private static int[] y = {0, 1, 1, 1, 0, -1, -1, -1};
    static HashSet<String> bigCoordinates = new HashSet<>();
    private static HashSet<String> coordinates = new HashSet<>();

    /**
     * Search method that finds all 3 or more in a row matches.
     * @param board Takes instance of BlockManager to search through.
     */
    static void search(char[][] board) {
        repeat = false;
        int count = 0;
        int r = BlockManager.ROW;
        for (int row = 0; row < r; row++) {
            int c = BlockManager.COL;
            for (int col = 0; col < c; col++) {
                char test = board[row][col];
                if (test == '-') {
                    continue;
                }
                for (int dir = 0; dir < 8; dir++) {
                    if (count > 2) {
                        bigCoordinates.addAll(coordinates);
                        repeat = true;
                    }
                    coordinates.clear();
                    count = 0;
                    int rd = row;
                    int cd = col;
                    while (!(rd >= r || rd < 0 || cd >= c || cd < 0)) {
                        if (board[rd][cd] == test) {
                            count++;
                            String location = rd + " " + cd;
                            coordinates.add(location);
                            rd = rd + x[dir];
                            cd = cd + y[dir];
                        } else {
                            break;
                        }
                    }
                }
            }
        }
        if (repeat) {
            destroy(board);
        }
    }
    /**
     * Method to destroy (replace) alike pieces by replacing them with an empty
     * place holder, '-'.
     * @param board Takes an instance of BlockManager to do destroying.
     */
    private static void destroy(char[][] board){
        for (String a :
                bigCoordinates) {
            String [] numbers = a.split("\\s+");
            int r = Integer.parseInt(numbers[0]);
            int c = Integer.parseInt(numbers[1]);
            board[r][c] = '-';
        }
        score = score + bigCoordinates.size();
        blocksRemoved = blocksRemoved + bigCoordinates.size();
    }

    /**
     * Getter method to return current game score.
     * @return Returns int value of current game score.
     */
    static int getScore() {
        return score;
    }

    /**
     * Getter method to return current number of blocks removed.
     * @return Returns int value of current number of blocks removed.
     */
    static int getBlocksRemoved() {
        return blocksRemoved;
    }


    /**
     * Setter method to set score back to 0 for second instance of game being
     * played.
     */

    static void setBlocksRemoved() {
        blocksRemoved = 0;
    }
}



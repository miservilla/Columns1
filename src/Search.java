import java.util.ArrayList;
import java.util.HashSet;
/**
 * @author Michael Servilla
 * @version date 2017-04-30
 */
class Search {
    private static boolean repeat;
    private static int score;
    private static int blocksRemoved;
    //Offsets to allow 8 directional searches.
    private static int[] x = {-1, -1, 0, 1, 1, 1, 0, -1};
    private static int[] y = {0, 1, 1, 1, 0, -1, -1, -1};
    private static ArrayList <Character> tmpColOccupied = new ArrayList<>();
    private static ArrayList <Character> tmpColVacant = new ArrayList<>();
    private static HashSet<String> bigCoordinates = new HashSet<>(); //HashSet to hold
    // one of a kind coordinates for removal.
    private static HashSet<String> coordinates = new HashSet<>(); //Temporary HashSet
    // to hold coordinates during single directional search.
    static int R = BlockManager.ROW;
    static int C = BlockManager.COL;

    /**
     * Search method that finds all 3 or more in a row matches.
     * @param board Takes instance of BlockManager to search through.
     */
    static void search(char[][] board) {
        repeat = false; //Flag to repeat search if previous search found items
        //to be destroyed.
        int count = 0;

        for (int row = 0; row < R; row++) {
            for (int col = 0; col < C; col++) {
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
                    while (!(rd >= R || rd < 0 || cd >= C || cd < 0)) {
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
        if (repeat)
        destroy(board);
    }

    /**
     * Method to destroy (replace) alike pieces by replacing them with an empty
     * place holder, '-'.
     * @param board Takes an instance of BlockManager to do destroying.
     */
    private static void destroy(char[][] board){
        //Used String value to hold coordinates as this was best way I could use
        // HashSet to allow only single values.
        for (String a :
                bigCoordinates) {
            String [] numbers = a.split("\\s+"); //Regex to split String
            // value into two row and column coordinates.
            int r = Integer.parseInt(numbers[0]);
            int c = Integer.parseInt(numbers[1]);
            board[r][c] = '-';
        }
        score = score + bigCoordinates.size();
        blocksRemoved = blocksRemoved + bigCoordinates.size();
        gravity(board);
    }

    /**
     * Method to allow gravity to drop pieces hovering above empty spaces after
     * destroying to fall into place. Moves column by column, rebuilding them
     * using gravity as my guide.
     * @param board Takes an instance of BlockManager to do gravity.
     */
    private static void gravity(char[][] board) {
        for (int col = 0; col < C; col++) {
            tmpColOccupied.clear();
            tmpColVacant.clear();

            //Takes all the empty spaces ('-') and stores in ArrayList.
            for (int row = 0; row < R; row++) {
                if (board[row][col] == '-') {
                    tmpColVacant.add(board[row][col]);
                    //Takes all non-empty items and stores in ArrayList.
                } else {
                    tmpColOccupied.add(board[row][col]);
                }
            }
            //Reloads empty spaces ('-') into column of board.
            for (int row = 0; row < tmpColVacant.size(); row++) {
                board[row][col] = tmpColVacant.get(row);
            }
            //Reloads all non-empty items into column of board.
            int a = 0;
            int b = tmpColVacant.size();
            for (int i = 0; i < tmpColOccupied.size(); i++) {
                board[b++][col] = tmpColOccupied.get(a++);
            }
        }
        if (repeat) {
            bigCoordinates.clear();
            search(board);
        }
    }

    /**
     * Method to repeat search if appropriate flag indicates need.
     * @param board Accept board object to pass on to search method.
     */
    static void startRepeat(char[][] board) {
        bigCoordinates.clear();
        search(board);
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
     * Method to check if need to repeat search after immediate prior removal of
     * block.
     * @return Returns boolean value of true if need to repeat search, false if
     * not needed to repeat search.
     */
   static boolean isRepeat() {
        return repeat;
    }

    /**
     * Setter method to set score back to 0 for second instance of game being
     * played.
     */
    static void setScore () {
        score = 0;
    }

    static void setBlocksRemoved() {
        blocksRemoved = 0;
    }
}



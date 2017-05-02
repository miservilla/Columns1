import javax.swing.*;

/**
 * This is the class to initiate game.
 * @author Michael Servilla
 * @version date 2017-04-30
 */
public class Columns {


    /**
     * Main method that uses anonymous class to allow JComponents to run EDT.
     * @param args Takes no command line arguments.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameBoardGUI columns = new GameBoardGUI();
            }
        });
    }
}

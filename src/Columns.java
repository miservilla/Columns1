import javax.swing.*;

/**
 * @author Michael Servilla
 * @version date 2017-04-30
 */
public class Columns {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                GameBoardGUI columns = new GameBoardGUI();
            }
        });
    }
}

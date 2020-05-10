import distributedwhiteboard.gui.WhiteboardGUI;

import javax.swing.*;

/**
 * Haichao Song
 * Description:
 */
public class Client {

    public static void main(String[] args) {

        if (args[0].equals("CreateWhiteBoard")) {
            SwingUtilities.invokeLater(() -> WhiteboardGUI.getInstance());
        } else if (args[0].equals("JoinWhiteBoard")) {
            SwingUtilities.invokeLater(() -> WhiteboardGUI.getInstance());

        } else {
            System.out.println("Invalid Operation");
            System.exit(-1);
        }

    }



}

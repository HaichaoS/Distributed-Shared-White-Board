import javax.swing.*;
import java.awt.*;

/**
 * Team: Haichao Song
 * Description:
 */
public class ClientGUI {

    private JFrame frame;
    private SpringLayout layout;
    private 


    public JFrame getFrame() {
        return frame;
    }

    public ClientGUI () {
        run();
    }

    public void run() {

        frame = new JFrame();
        frame.setMinimumSize(new Dimension(450, 300));
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

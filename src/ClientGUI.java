import javax.swing.*;
import java.awt.*;

/**
 * Haichao Song
 * Description:
 */
public class ClientGUI extends JFrame{

    private JFrame frame;
    private SpringLayout layout;
    private Canvas canvas;
    private JScrollPane scrollPane;
    private Thread thread;


    public JFrame getFrame() {
        return frame;
    }

    public ClientGUI () {
        run();
    }

    public void run() {

        // Set the look and feel to the system style if possible.
        String sysFeel = UIManager.getSystemLookAndFeelClassName();
        String crossFeel = UIManager.getSystemLookAndFeelClassName();
        try {
            UIManager.setLookAndFeel(sysFeel);
        } catch (ClassNotFoundException
                | InstantiationException
                | IllegalAccessException
                | UnsupportedLookAndFeelException ex) {
            System.err.println("Couldn't load system look and feel. "
                    + "Reverting to cross-platform.");
            try {
                UIManager.setLookAndFeel(crossFeel);
            } catch (ClassNotFoundException
                    | InstantiationException
                    | IllegalAccessException
                    | UnsupportedLookAndFeelException innerEx) {
                System.err.println("Couldn't load cross-platform look and "
                        + "feel.");
            }
        }

        this.layout = new SpringLayout();
        Container contentPane = this.getContentPane();
        contentPane.setLayout(layout);

        this.scrollPane = new JScrollPane(canvas);
        this.scrollPane.setBackground(Color.LIGHT_GRAY);


    }


}

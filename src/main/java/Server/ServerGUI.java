package Server;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Haichao Song
 * Description:
 * The GUI shows the address and port number to users
 */
public class ServerGUI {

    private JFrame frame;
    private String address, port;

    public JFrame getFrame() {
        return frame;
    }

    public ServerGUI (String address, String port){
        this.address = address;
        this.port = port;
        create();
    }

    public void create() {

        frame = new JFrame();
        frame.setMinimumSize(new Dimension(450, 300));
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel addressLabel = new JLabel("Address: " + address);
        JLabel portLabel = new JLabel("Port: " + port);

        JPanel panel = new JPanel();
        JLabel jlabel = new JLabel("WELCOME");
        jlabel.setFont(new Font("Verdana",1,20));
        panel.add(jlabel);
        panel.setBorder(new LineBorder(Color.BLACK));


        // Render the server GUI
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(addressLabel, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                                .addGap(5)
                                .addComponent(portLabel, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                        .addGap(5))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(addressLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(portLabel, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE))
                                .addGap(5)
                                .addComponent(panel, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                        .addGap(5))
        );
        frame.getContentPane().setLayout(groupLayout);
    }
}

package Server;

import javax.swing.*;
import java.awt.*;

/**
 * Haichao Song
 * Description:
 */
public class ServerGUI {

    private JFrame frame;
    private JTextArea textArea;
    private String serverName, serviceName;

    public JFrame getFrame() {
        return frame;
    }

    public ServerGUI (String serverName, String serviceName) {
        this.serverName = serverName;
        this.serviceName = serviceName;
        create();
    }

    public void create() {

        frame = new JFrame();
        frame.setMinimumSize(new Dimension(450, 300));
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel port = new JLabel("Server Name: " + serverName);
        JLabel path = new JLabel("Service Name: " + serviceName);

        JLabel log = new JLabel("Log:");
        JScrollPane scrollPane = new JScrollPane();

        textArea = new JTextArea();
        textArea.setLineWrap(true);
        textArea.setEditable(false);
        scrollPane.setViewportView(textArea);

        // Render the server GUI
        GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(port, GroupLayout.DEFAULT_SIZE, 450, Short.MAX_VALUE)
                                .addGap(5)
                                .addComponent(path, GroupLayout.DEFAULT_SIZE, 300, Short.MAX_VALUE))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addComponent(log, GroupLayout.PREFERRED_SIZE, 250, GroupLayout.PREFERRED_SIZE))
                        .addGroup(groupLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE))
        );
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(groupLayout.createSequentialGroup()
                                .addGap(5)
                                .addGroup(groupLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(port, GroupLayout.PREFERRED_SIZE,
                                                30, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(path, GroupLayout.PREFERRED_SIZE,
                                                30, GroupLayout.PREFERRED_SIZE))
                                .addGap(10)
                                .addComponent(log, GroupLayout.PREFERRED_SIZE, 15, GroupLayout.PREFERRED_SIZE)
                                .addGap(5)
                                .addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
        );
        frame.getContentPane().setLayout(groupLayout);

    }

    public JTextArea getTextArea() {
        return textArea;
    }
}

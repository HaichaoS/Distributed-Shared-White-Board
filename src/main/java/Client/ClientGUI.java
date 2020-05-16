package main.java.Client;

import main.java.Server.Event;
import main.java.Server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

import static main.java.Client.Client.userID;

/**
 * Haichao Song
 * Description:
 */
public class ClientGUI {

    private boolean isManger;
    private JFrame frame;
    public static Server server;

    public ClientGUI (boolean isManager) {
        this.isManger = isManager;
        create();
    }

    public JFrame getFrame() {
        return frame;
    }

    private void create() {

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                if (!isManger) {
                    int choice = JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to exit the application?",
                            "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        try {
                            server.kickUser(userID);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    } else {
                        frame.setVisible(true);
                    }
                } else {
                    int choice = JOptionPane.showConfirmDialog(frame,
                            "Are you sure you want to end the current Whiteboard Session?",
                            "Exit Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        if (server != null) {
                            Event event = new Event("Terminate");
                            try {
                                server.addEvent(event);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                        //System.exit(0);
                    } else {
                        frame.setVisible(true);
                    }
                }
            }
        });

        frame.getContentPane().setLayout(new BorderLayout());

        sharedPanel = new SharedPanel(server, userID, frame);
        frame.getContentPane().add(sharedPanel, BorderLayout.CENTER);

        JScrollPane scrollPane = new JScrollPane();
        userPanel = new UserPanel(server, userID, scrollPane);
        scrollPane.setPreferredSize(new Dimension(110, 700));
        scrollPane.getViewport().add(userPanel);
        frame.getContentPane().add(scrollPane, BorderLayout.EAST);


        frame.setTitle("Distributed Whiteboard");
        frame.setSize(828, 893);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(false);

        frame.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                sharedPanel.requestFocusInWindow();
            }
        });

        new Thread(new EventDispatcher(sharedPanel, userPanel), "EventDispatcher").start();

    }


}

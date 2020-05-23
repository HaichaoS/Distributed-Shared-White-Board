package Client;


import Server.BoardEvent;
import Server.Server;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Haichao Song
 * Description:
 */
public class Client {

    public static Server server;
    public static String userID;
    public static String managerID;
    public static boolean isManager;
    public static BoardPanel boardPanel;
    private static String serverName = "localhost";
    private static String serviceName = "Server";
    private static UserPanel userPanel;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("Please Enter Server");
            System.exit(0);
        }

        serverName = args[0];
        serviceName = args[1];

        try {
            server = (Server) Naming.lookup("rmi://" + serverName + "/" + serviceName);
            String candidateID = null;
            while ((candidateID == null)) {
                candidateID = JOptionPane.showInputDialog(null, "Enter User Name", "Login", JOptionPane.INFORMATION_MESSAGE);
            }
            userID = server.joinBoard(candidateID);
            managerID = server.getManager();
            isManager = userID.equalsIgnoreCase(managerID);
            System.out.println("Assigned userID " + userID);
        } catch (MalformedURLException | RemoteException e) {
            e.printStackTrace();
            System.exit(-1);
        } catch (NotBoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        javax.swing.SwingUtilities.invokeLater(() -> initialize());

    }

    private static void initialize() {

        JFrame frame = new JFrame("White Board");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                if (!isManager) {
                    int choice = JOptionPane.showConfirmDialog(frame,
                            "Exit the Whiteboard?",
                            "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        try {
                            server.kickUser(Client.userID);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        System.exit(0);
                    } else {
                        frame.setVisible(true);
                    }
                } else {
                    int choice = JOptionPane.showConfirmDialog(frame,
                            "End the Whiteboard?",
                            "Confirmation", JOptionPane.YES_NO_OPTION);
                    if (choice == JOptionPane.YES_OPTION) {
                        if (server != null) {
                            BoardEvent event = new BoardEvent("Terminate");
                            try {
                                server.addBoardEvent(event);
                            } catch (RemoteException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        frame.setVisible(true);
                    }
                }
            }
        });

        frame.getContentPane().setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane();

        boardPanel = new BoardPanel(server, frame);
        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);


        userPanel = new UserPanel(server, userID, scrollPane);
        scrollPane.setPreferredSize(new Dimension(100, 800));
        scrollPane.getViewport().add(userPanel);
        frame.getContentPane().add(scrollPane, BorderLayout.WEST);


        //frame.pack();
        frame.setSize(1100, 800);
        frame.setResizable(false);
        frame.setTitle("Distributed Shared Whiteboard");
        frame.getContentPane().setBackground(Color.WHITE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(false);
        frame.addWindowFocusListener(new WindowAdapter() {
            public void windowGainedFocus(WindowEvent e) {
                boardPanel.requestFocusInWindow();
            }
        });

        new Thread(new EventHandler(boardPanel, userPanel), "EventHandler").start();

    }

}
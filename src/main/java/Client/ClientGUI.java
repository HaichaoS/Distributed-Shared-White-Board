package Client;

import Remote.IServer;
import Server.BoardHandler;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RemoteException;

/**
 * Haichao Song
 * Description:
 * allows users to draw on the whiteboard and
 * contains board and user panel to allow users perform actions on them.
 */
public class ClientGUI {

    private IServer IServer;
    private boolean isManager;
    private static BoardPanel boardPanel;
    private UserPanel userPanel;

    public ClientGUI(IServer IServer, boolean isManager) {
        this.isManager = isManager;
        this.IServer = IServer;
        javax.swing.SwingUtilities.invokeLater(() -> initialize());
    }

    public void initialize() {

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
                            IServer.kickUser(Client.userID);
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
                        if (IServer != null) {
                            BoardHandler event = new BoardHandler("end");
                            try {
                                IServer.addBoardEvent(event);
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

        boardPanel = new BoardPanel(IServer, frame);
        frame.getContentPane().add(boardPanel, BorderLayout.CENTER);


        userPanel = new UserPanel(IServer, scrollPane);
        scrollPane.setPreferredSize(new Dimension(100, 800));
        scrollPane.getViewport().add(userPanel);
        frame.getContentPane().add(scrollPane, BorderLayout.WEST);

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

    public static BoardPanel getBoardPanel() {return boardPanel;}
}

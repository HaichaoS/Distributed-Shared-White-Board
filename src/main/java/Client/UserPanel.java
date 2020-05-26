package Client;

/**
 * Haichao Song
 * Description:
 */

import Remote.IServer;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.Vector;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class UserPanel extends JPanel implements ActionListener, ListSelectionListener, KeyListener {

    private final IServer IServer;
    private final JScrollPane scrollPane;
    private final JList<String> usersList;
    private JButton kickButton;
    private final Vector<String> userIDs = new Vector<>();
    private String selectedUser;

    public UserPanel(IServer IServer, JScrollPane sPane) {
        this.IServer = IServer;
        this.scrollPane = sPane;
        setBorder(BorderFactory.createTitledBorder("Users"));
        setLayout(new BorderLayout());

        if (Client.isManager) {
            kickButton = new JButton("Kick");
            add(kickButton, BorderLayout.SOUTH);
            kickButton.addActionListener(this);
        }

        usersList = new JList<>(userIDs);
        UserList renderer = new UserList();
        usersList.setCellRenderer(renderer);
        add(usersList, BorderLayout.CENTER);
        usersList.addListSelectionListener(this);
        addKeyListener(this);
    }

    public synchronized void refresh(Vector<String> users) {
        userIDs.removeAllElements();
        for (String user : users) {
            if (user.charAt(0) != '#') {
                userIDs.add(user);
            }
        }
        usersList.setListData(userIDs);
        if (userIDs.size() >= 2) {
            usersList.setSelectedIndex(1);
        } else {
            usersList.setSelectedIndex(0);
        }
        scrollPane.revalidate();
        scrollPane.repaint();
    }

    @Override
    public void valueChanged(ListSelectionEvent event) {
        if (event.getSource() == usersList && !event.getValueIsAdjusting()) {
            String stringValue = usersList.getSelectedValue();
            if (stringValue != null) {
                selectedUser = stringValue;
                if (kickButton != null) {
                    if (selectedUser.equalsIgnoreCase(Client.managerID)) {
                        kickButton.setEnabled(false);
                    } else {
                        kickButton.setEnabled(true);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == kickButton) {
            int selection = usersList.getSelectedIndex();
            if (selection >= 0) {
                try {
                    IServer.kickUser(selectedUser);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        ClientGUI.getBoardPanel().keyTyping(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

}

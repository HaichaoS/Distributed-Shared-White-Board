package main.java.Client;

import main.java.Server.Server;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.rmi.RemoteException;
import java.util.Vector;

/**
 * Haichao Song
 * Description:
 */
public class UserPanel extends JPanel implements ActionListener, ListSelectionListener, KeyListener {

    private static final long serialVersionUID = 1L;
    private Server server;
    private String id;
    private JScrollPane scrollPane;
    private JList<String> usersList;
    private JButton bounceButton;
    private Vector<String> userIDs = new Vector<>();
    private String selectedUser;

    public UserPanel(Server server, String id, JScrollPane scrollPane) {
        this.server = server;
        this.id = id;
        this.scrollPane = scrollPane;
        setBorder(BorderFactory.createTitledBorder("Board Users"));
        setLayout(new BorderLayout());

        if (Client.isManager) {
            bounceButton = new JButton("Bounce");
            add(bounceButton, BorderLayout.SOUTH);
            bounceButton.addActionListener(this);
        }

        usersList = new JList<>(userIDs);
        UserList userList = new UserList();
        usersList.setCellRenderer(userList);
        add(usersList, BorderLayout.CENTER);
        usersList.addListSelectionListener(this);
        addKeyListener(this);
    }

    public synchronized void refresh(Vector<String> uIDs) {
        userIDs.removeAllElements();
        for (String auser : uIDs) {
            if (auser.charAt(0) != '#') {
                userIDs.add(auser);
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
                if (bounceButton != null) {
                    if (selectedUser.equalsIgnoreCase(Client.managerID)) {
                        bounceButton.setEnabled(false);
                    } else {
                        bounceButton.setEnabled(true);
                    }
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == bounceButton) {
            int selection = usersList.getSelectedIndex();
            if (selection >= 0) {
                try {
                    server.kickUser(selectedUser);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        Client.boardPanel.keyTyping(e);
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }


}

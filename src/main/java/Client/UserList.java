package main.java.Client;

import javax.swing.*;
import java.awt.*;

/**
 * Haichao Song
 * Description:
 */
public class UserList extends DefaultListCellRenderer {

    @Override
    public Component getListCellRendererComponent(JList<?> list,
                                                  Object value, int index, boolean isSelected, boolean cellHasFocus) {

        String currentUser = value.toString();
        setText(currentUser);

        Color background;
        Color foreground;

        if (currentUser.equals(Client.userID)) {
            background = Color.WHITE;
            foreground = Color.BLUE;
        } else if (currentUser.equals(Client.managerID)) {
            background = Color.WHITE;
            foreground = Color.RED;
        } else {
            if (Client.isManager) {
                if (isSelected) {
                    background = Color.RED;
                    foreground = Color.WHITE;
                } else {
                    background = Color.WHITE;
                    foreground = Color.BLACK;
                }
            } else {
                background = Color.WHITE;
                foreground = Color.BLACK;
            }
        }

        setBackground(background);
        setForeground(foreground);

        return this;

    }
}

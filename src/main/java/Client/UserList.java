package Client;

/**
 * Haichao Song
 * Description:
 */

import java.awt.Color;
import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

class UserList extends DefaultListCellRenderer {

    private static final long serialVersionUID = 1L;

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
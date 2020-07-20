package Client;

import java.awt.Color;
import java.awt.Component;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 * Haichao Song
 * Description:
 * responsible for defining the basic color setting of the user list
 */
class UserList extends DefaultListCellRenderer {

    private Color background;
    private Color letter;

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                                                  boolean cellHasFocus) {

        String currentUser = value.toString();
        setText(currentUser);

        if (currentUser.equals(Client.userID)) {
            background = Color.WHITE;
            letter = Color.BLUE;
        } else if (currentUser.equals(Client.managerID)) {
            background = Color.WHITE;
            letter = Color.RED;
        } else {
            if (Client.isManager) {
                if (isSelected) {
                    background = Color.RED;
                    letter = Color.WHITE;
                } else {
                    background = Color.WHITE;
                    letter = Color.BLACK;
                }
            } else {
                background = Color.WHITE;
                letter = Color.BLACK;
            }
        }

        setBackground(background);
        setForeground(letter);

        return this;

    }
}
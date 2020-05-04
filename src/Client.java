/**
 * Team: Haichao Song
 * Description:
 */
public class Client {

    private ClientGUI clientGUI;

    public static void main() {
        Client client = new Client();
        client.create();
    }

    public Client() {
        this.clientGUI = null;

    }

    public void create() {
        try {
            this.clientGUI = new ClientGUI();
            clientGUI.getFrame().setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
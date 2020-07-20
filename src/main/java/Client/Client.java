package Client;

import Remote.IServer;
import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Haichao Song
 * Description:
 * connecting the IServer and create the client GUI
 */
public class Client {

    public static IServer IServer;
    public static ClientGUI clientGUI;
    public static String userID;
    public static String managerID;
    public static boolean isManager;

    public static void main(String[] args) {

        if (args.length != 2) {
            System.out.println("Invalid Input: Address Port");
            System.exit(-1);
        }

        // check port number
        if (Integer.parseInt(args[1]) <= 1024 || Integer.parseInt(args[1]) >= 49151) {
            System.out.println("Invalid Port Number");
            System.exit(-1);
        } else {

            String address = args[0];
            String port = args[1];

            try {
                IServer = (IServer) Naming.lookup("rmi://" + address + "/" + port);
                String candidateID = null;
                while ((candidateID == null)) {
                    candidateID = JOptionPane.showInputDialog(null, "Enter User Name",
                            "Login", JOptionPane.INFORMATION_MESSAGE);
                }
                userID = IServer.joinBoard(candidateID);
                managerID = IServer.getManager();
                isManager = userID.equalsIgnoreCase(managerID);
                System.out.println("Assigned userID " + userID);
            } catch (MalformedURLException | RemoteException e) {
                e.printStackTrace();
                System.exit(-1);
            } catch (NotBoundException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }

        clientGUI = new ClientGUI(IServer, isManager);

    }

}

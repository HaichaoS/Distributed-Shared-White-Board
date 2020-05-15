package main.java.Client;

import main.java.Server.Server;

import javax.swing.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

/**
 * Haichao Song
 * Description:
 */
public class Client {

    private static String serverName = "localhost";
    private static String serviceName = "Server";
    public static Server server;
    public static String userID;
    public static String managerID;
    public static boolean isManager;


    public static void main(String[] args) {

        if (args.length != 2) {
            System.err.println("usage: ServerName ServiceName");
            System.exit(0);
        }

        serverName = args[0];
        serviceName = args[1];

        try {
            server = (Server) Naming.lookup("rmi://" + serverName + "/" + serviceName);
            String candidateID = null;
            while ((candidateID == null)) {
                candidateID = JOptionPane.showInputDialog(null, "Enter user name", "Login"
                        , JOptionPane.INFORMATION_MESSAGE);
            }
            userID = server.joinBoard(candidateID);
            managerID = server.getManagerID();
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

    public static void initialize() {

    }

}

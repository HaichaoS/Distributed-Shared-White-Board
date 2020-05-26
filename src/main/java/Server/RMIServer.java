package Server;

import Remote.IServer;

import java.net.InetAddress;
import java.rmi.Naming;

/**
 * Haichao Song
 * Description: Creates an instance of the RemoteServer class and
 * publishes it in the rmiregistry
 */
public class RMIServer{

    private static ServerGUI serverGUI;

    private RMIServer() {
        super();
    }

    public static void main(String args[]) {

        if (args.length != 1) {
            System.out.println("Invalid Input: Port");
            System.exit(-1);
        }

        // check port number
        if (Integer.parseInt(args[0]) <= 1024 || Integer.parseInt(args[0]) >= 49151) {
            System.out.println("Invalid Port Number");
            System.exit(-1);
        } else {
            try {
                String address = InetAddress.getLocalHost().getHostAddress();
                String port = args[0];
                IServer remoteServer = new RemoteServer();

                // Bind this object instance to the name "IServer"
                Naming.rebind("rmi://" + address + "/" + port, remoteServer);

                System.out.println("IServer Start Succeed");

                create(address, port);

            } catch (Exception e) {
                System.out.println("IServer Error: " + e.getMessage());
            }
        }
    }

    public static void create(String address, String port) {

        try {

            serverGUI = new ServerGUI(address, port);
            serverGUI.getFrame().setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
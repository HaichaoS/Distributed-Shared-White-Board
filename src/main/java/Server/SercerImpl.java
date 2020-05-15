package main.java.Server;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class ServerImpl extends UnicastRemoteObject implements Server {

    private ArrayList<String> users = new ArrayList<>();
    private ArrayList<Event> masterEvents;
    private int usersSequence;
    private int eventSequence;
    private String adminID;

    private ServerImpl() throws RemoteException {
        super();
    }

    public static void main(String args[]) {
        try {
            String serverName = "localhost";
            String serviceName = "BoardServer";

            //System.setSecurityManager(new RMISecurityManager());

            Server server = new ServerImpl();
            // Bind this object instance to the name "BoardServer"
            Naming.rebind("rmi://" + serverName + "/" + serviceName, server);

            System.out.println("Successful rebind service");

        } catch (Exception e) {
            System.out.println("ServerImpl err: " + e.getMessage());
        }
    }

    @Override
    public String joinBoard(String id) {
        return id;
    }

    @Override
    public String getManage() {
        return "";
    }

    @Override
    public void approveUser(String userID) {

    }

    @Override
    public void bounceUser(String userID) {

    }

    @Override
    public synchronized void addEvent(Event event) {

    }

    @Override
    public synchronized ArrayList<Event> getEvents(int startFrom) {
        return new ArrayList<>(
                masterEvents.subList(startFrom, masterEvents.size()));
    }

}

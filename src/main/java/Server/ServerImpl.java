package main.java.Server;

import java.lang.reflect.Array;
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
    private ArrayList<Event> events;
    private int usersSequence;
    private int eventSequence;
    private String managerID;

    private ServerImpl() throws RemoteException {
        super();
    }

    public static void main(String args[]) {
        try {
            String serverName = "localhost";
            String serviceName = "Server";

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
        String userID;
        ArrayList<String> usersList;
        synchronized (users) {
            usersList = new ArrayList<>(users);
        }

        boolean hasUser = false;
        if (usersList != null) {
            for (int i = 0; i < usersList.size(); i++) {
                if (usersList.get(i).charAt(0) != '#') {
                    hasUser = true;
                    break;
                }
            }
        }

        if ((usersList == null) || (!hasUser)) {
            users = new ArrayList<>();
            events = new ArrayList<>();
            managerID = id;
            usersSequence = 0;
            eventSequence = 0;
            userID = id;
            approveUser(userID);
        } else {
            for (int i = 0; i < usersList.size(); i++) {
                String user = usersList.get(i);
                if (user.charAt(0) == '#') {
                    user = user.substring(1);
                }
                if (user.equals(id)) {
                    id = id + usersSequence;
                    break;
                }
            }
            userID = id;
            Event event = new Event("Join");
            event.userID = userID;
            addEvent(event);

        }

        usersSequence += 1;
        return userID;
    }

    @Override
    public String getManagerID() {
        return managerID;
    }

    @Override
    public void approveUser(String id) {
        Event userEvent = new Event("userList");
        synchronized (users) {
            userEvent.userList = new ArrayList<>(users);
        }
        addUserListEvent();
    }

    @Override
    public void kickUser(String id) {
        boolean hasUser = false;
        ArrayList<String> usersList;
        synchronized (users) {
            usersList = new ArrayList<>(users);
        }
        for (int i = 0; i < usersList.size(); i++) {
            String user = usersList.get(i);
            if (user.equals('#' + id)) {
                hasUser = true;
            } else if (user.equals(id)) {
                hasUser = true;
                synchronized (users) {
                    users.set(i, '#' + id);
                }
                break;
            }
        }
        if (!hasUser) {
            synchronized (users) {
                users.add("#" + id);
            }
        }
        addUserListEvent();
    }

    private void addUserListEvent() {
        Event event = new Event("userList");
        synchronized (users) {
            event.userList = new ArrayList<>(users);
        }
        addEvent(event);
    }

    @Override
    public synchronized void addEvent(Event event) {
        event.eventID = eventSequence ++;
        events.add(event);
    }

    @Override
    public synchronized ArrayList<Event> getEvents(int startFrom) {
        return new ArrayList<>(
                events.subList(startFrom, events.size()));
    }

}

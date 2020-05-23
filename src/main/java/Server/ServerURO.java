package Server;

import javax.swing.*;
import java.rmi.server.UnicastRemoteObject;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class ServerURO extends UnicastRemoteObject implements Server {

    private static JFrame frame;
    private ArrayList<String> users = new ArrayList<>();
    private ArrayList<BoardEvent> boardEvents;
    private int usersNum, eventsNum;
    private String managerID;
    private static ServerGUI serverGUI;

    private ServerURO() throws RemoteException {
        super();
    }

    public static void main(String args[]) {
        try {
            String serverName = "localhost";
            String serviceName = "Server";

            Server obj = new ServerURO();

            // Bind this object instance to the name "Server"
            Naming.rebind("rmi://" + serverName + "/" + serviceName, obj);

            System.out.println("Server Start Succeed");

            create(serverName, serviceName);

        } catch (Exception e) {
            System.out.println("Server Error: " + e.getMessage());
        }
    }

    public static void create(String ServerName, String ServiceName) {

        try {

            serverGUI = new ServerGUI(ServerName, ServiceName);
            serverGUI.getFrame().setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printLog(String s) {
        System.out.println(s);
        if (serverGUI != null) serverGUI.getTextArea().append(s + '\n');
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
            for (String user : usersList) {
                if (user.charAt(0) != '#') {
                    hasUser = true;
                    break;
                }
            }
        }

        if ((usersList == null) || (!hasUser)) {
            users = new ArrayList<>();
            boardEvents = new ArrayList<>();
            managerID = id;
            usersNum = 0;
            eventsNum = 0;
            userID = id;
            approveUser(userID);
        } else {
            for (String user : usersList) {
                if (user.charAt(0) == '#') {
                    user = user.substring(1);
                }
                if (user.equals(id)) {
                    id = id + usersNum;
                    break;
                }
            }
            userID = id;
            BoardEvent serverEvent = new BoardEvent("joinRequest");
            serverEvent.userID = userID;
            addBoardEvent(serverEvent);

        }

        usersNum += 1;
        return userID;
    }

    @Override
    public String getManager() {
        return managerID;
    }

    @Override
    public void approveUser(String userID) {
        synchronized (users) {
            users.add(userID);
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
        BoardEvent event = new BoardEvent("userList");
        synchronized (users) {
            event.userList = new ArrayList<>(users);
        }
        addBoardEvent(event);
    }

    @Override
    public synchronized void addBoardEvent(BoardEvent event) {
        event.eventID = eventsNum++;
        boardEvents.add(event);
    }

    @Override
    public synchronized ArrayList<BoardEvent> getBoardEvents(int startFrom) {
        return new ArrayList<>(boardEvents.subList(startFrom, boardEvents.size()));
    }
}
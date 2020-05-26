package Server;

import Remote.IServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 * Server side implementation of the remote interface.
 * Must extend UnicastRemoteObject, to allow the JVM to create a
 * remote proxy/stub.
 */
public class RemoteServer extends UnicastRemoteObject implements IServer {

    private ArrayList<String> users = new ArrayList<>();
    private ArrayList<BoardHandler> boardHandlers;
    private int usersNum, eventsNum;
    private String managerID;

    public RemoteServer() throws RemoteException {
        super();
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
            boardHandlers = new ArrayList<>();
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
            BoardHandler serverEvent = new BoardHandler("joinRequest");
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
    public void kickUser(String userID) {
        boolean hasUser = false;
        ArrayList<String> usersList;
        synchronized (users) {
            usersList = new ArrayList<>(users);
        }
        for (int i = 0; i < usersList.size(); i++) {
            String user = usersList.get(i);
            if (user.equals('#' + userID)) {
                hasUser = true;
            } else if (user.equals(userID)) {
                hasUser = true;
                synchronized (users) {
                    users.set(i, '#' + userID);
                }
                break;
            }
        }
        if (!hasUser) {
            synchronized (users) {
                users.add("#" + userID);
            }
        }
        addUserListEvent();
    }

    private void addUserListEvent() {
        BoardHandler event = new BoardHandler("userList");
        synchronized (users) {
            event.userList = new ArrayList<>(users);
        }
        addBoardEvent(event);
    }

    @Override
    public void addBoardEvent(BoardHandler event) {
        event.eventID = eventsNum++;
        boardHandlers.add(event);
    }

    @Override
    public ArrayList<BoardHandler> getBoardEvents(int startFrom){
        return new ArrayList<>(boardHandlers.subList(startFrom, boardHandlers.size()));
    }
}

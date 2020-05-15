package main.java.Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public interface Server extends Remote {

    String joinBoard(String candidateID) throws RemoteException;

    String getManagerID() throws RemoteException;

    void approveUser(String userID) throws RemoteException;

    void kickUser(String userID) throws RemoteException;

    void addEvent(Event event) throws RemoteException;

    ArrayList<Event> getEvents(int startFrom) throws RemoteException;

}

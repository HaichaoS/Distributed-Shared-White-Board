package Server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public interface Server extends Remote {

    String joinBoard(String candidateID) throws RemoteException;

    String getManager() throws RemoteException;

    void approveUser(String userID) throws RemoteException;

    void kickUser(String userID) throws RemoteException;

    void addBoardEvent(BoardEvent event) throws RemoteException;

    ArrayList<BoardEvent> getBoardEvents(int startFrom) throws RemoteException;

}


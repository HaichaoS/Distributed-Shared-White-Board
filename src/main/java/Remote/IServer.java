package Remote;

import Server.BoardHandler;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 * Remote contains the IServer Interface which is shared between the client and the server.
 * It includes all the functions provided by the system to the client.
 */
public interface IServer extends Remote {

    String joinBoard(String id) throws RemoteException;
    String getManager() throws RemoteException;
    void approveUser(String userID) throws RemoteException;
    void kickUser(String userID) throws RemoteException;
    void addBoardEvent(BoardHandler event) throws RemoteException;
    ArrayList<BoardHandler> getBoardEvents(int startFrom) throws RemoteException;

}


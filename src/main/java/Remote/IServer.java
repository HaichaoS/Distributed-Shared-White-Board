package Remote;

import Server.BoardHandler;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 * RMI Remote interface - must be shared between client and server.
 * All methods must throw RemoteException.
 * All parameters and return types must be either primitives or Serializable.
 * Any object that is a remote object must implement this interface.
 * Only those methods specified in a "remote interface" are available remotely.
 */
public interface IServer extends Remote {

    String joinBoard(String id) throws RemoteException;
    String getManager() throws RemoteException;
    void approveUser(String userID) throws RemoteException;
    void kickUser(String userID) throws RemoteException;
    void addBoardEvent(BoardHandler event) throws RemoteException;
    ArrayList<BoardHandler> getBoardEvents(int startFrom) throws RemoteException;

}


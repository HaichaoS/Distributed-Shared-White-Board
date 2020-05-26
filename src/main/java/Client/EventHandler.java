package Client;


import Client.Modes.*;
import Server.BoardHandler;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Haichao Song
 * Description:
 */
public class EventHandler implements Runnable {

    private BoardPanel boardPanel;
    private UserPanel userPanel;
    private boolean joinRequestReplied = false;
    private boolean authorized = false;
    private int nextEventID = 0;

    public EventHandler(BoardPanel boardPanel, UserPanel userPanel) {
        this.boardPanel = boardPanel;
        this.userPanel = userPanel;
    }

    private void dispatch(BoardHandler event) {
        switch (event.eventType) {
            case "end":
                JOptionPane.showMessageDialog(boardPanel.frame, "Whiteboard sharing is ended by the manager",
                        "Terminated", JOptionPane.INFORMATION_MESSAGE);
                try {
                    boardPanel.IServer.kickUser(Client.userID);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            case "joinRequest":
                int choice = JOptionPane.showConfirmDialog(boardPanel.frame,
                        event.userID + " wants to share your whiteboard",
                        "Join Request", JOptionPane.YES_NO_OPTION);
                try {
                    if (choice == JOptionPane.YES_OPTION) {
                        Client.IServer.approveUser(event.userID);
                    } else {
                        Client.IServer.kickUser(event.userID);
                    }
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                break;
            case "userList":
                authorized = false;
                for (String user : event.userList) {
                    if (user.equals(Client.userID)) {
                        joinRequestReplied = true;
                        authorized = true;
                        break;
                    } else if (user.equals("#" + Client.userID)) {
                        joinRequestReplied = true;
                        authorized = false;
                        break;
                    }
                }
                if (joinRequestReplied) {
                    if (authorized) {
                        boardPanel.requestFocusInWindow();
                        boardPanel.frame.setVisible(true);
                        Vector<String> uIDs = new Vector<>(event.userList);
                        userPanel.refresh(uIDs);
                    } else {
                        JOptionPane.showMessageDialog(boardPanel.frame, "Access to whiteboard " +
                                        "was denied by the manager", "Denied", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }
                break;
            case "load":
                boardPanel.shapes = event.shapes;
                boardPanel.frame.repaint();
                break;
            case "keyTyped":
                Text.drawText(boardPanel.getGraphics(), event.startPoint, event.textInput, event.currentColor);
                break;
            case "mouseMoved":
                boardPanel.addShape(new Text(event.startPoint, event.textInput, event.currentColor));
                break;
            case "mouseDragged":
                if (event.currentEraser) {
                    Eraser.drawEraser(boardPanel.getGraphics(), event.eraserSize, event.points);
                } else {
                    switch (event.currentMode) {
                        case LINE:
                            Line.draw(boardPanel.getGraphics(), event.startPoint, event.endPoint, event.currentColor);
                            break;
                        case RECT:
                            Rect.drawRect(boardPanel.getGraphics(), event.startPoint, event.endPoint, event.currentFill, event.currentColor);
                            break;
                        case OVAL:
                            Oval.drawOval(boardPanel.getGraphics(), event.startPoint, event.endPoint, event.currentFill, event.currentColor);
                            break;
                        case FREEFORM_LINE:
                            FreeLine.drawFreeLine(boardPanel.getGraphics(), event.points, event.currentColor);
                            break;
                        case TEXT:
                            break;
                    }
                }
                boardPanel.frame.repaint();
                break;
            case "mouseReleased":
                if (event.currentEraser) {
                    boardPanel.addShape(new Eraser(event.points, event.eraserSize));
                } else {
                    switch (event.currentMode) {
                        case LINE:
                            boardPanel.addShape(new Line(event.startPoint, event.endPoint, event.currentColor));
                            break;
                        case RECT:
                            boardPanel.addShape(new Rect(event.startPoint, event.endPoint, event.currentFill, event.currentColor));
                            break;
                        case OVAL:
                            boardPanel.addShape(new Oval(event.startPoint, event.endPoint, event.currentFill, event.currentColor));
                            break;
                        case FREEFORM_LINE:
                            boardPanel.addShape(new FreeLine(event.points, event.currentColor));
                            break;
                        case TEXT:
                            break;
                    }
                }
                boardPanel.frame.repaint();
                break;
        }
    }

    @Override
    public void run() {
        while (true) {
            ArrayList<BoardHandler> boardHandlers;
            try {
                boardHandlers = boardPanel.IServer.getBoardEvents(nextEventID);
                for (BoardHandler event : boardHandlers) {
                    if (event.eventType.equals("joinRequest") && Client.isManager) {
                        dispatch(event);
                    }
                }
                BoardHandler latestUserList = null;
                for (BoardHandler event : boardHandlers) {
                    if (event.eventType.equals("userList")) {
                        latestUserList = event;
                    }
                }
                if (latestUserList != null) {
                    dispatch(latestUserList);
                }
                if (!joinRequestReplied) {
                    continue;
                }
                for (BoardHandler event : boardHandlers) {
                    if (!(event.eventType.equals("joinRequest") || event.eventType.equals("userList"))) {
                        dispatch(event);
                    }
                }
                if ((boardHandlers).size() > 0) {
                    nextEventID = boardHandlers.get(boardHandlers.size() - 1).eventID + 1;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

}


package main.java.Client;

import main.java.Server.ServerEvent;

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

    private void dispatch(ServerEvent event) {
        switch (event.eventType) {
            case "Terminate":
                JOptionPane.showMessageDialog(boardPanel.frame, "Whiteboard Session was terminated by Admin",
                        "Session Terminated", JOptionPane.INFORMATION_MESSAGE);
                try {
                    boardPanel.server.kickUser(Client.userID);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            case "joinRequest":
                int choice = JOptionPane.showConfirmDialog(boardPanel.frame,
                        event.userID + " wants to join. Allow user?",
                        "Join Request", JOptionPane.YES_NO_OPTION);
                try {
                    if (choice == JOptionPane.YES_OPTION) {
                        Client.server.approveUser(event.userID);
                    } else {
                        Client.server.kickUser(event.userID);
                    }
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                break;
            case "userList":
                authorized = false;
                for (String auser : event.userList) {
                    if (auser.equals(Client.userID)) {
                        joinRequestReplied = true;
                        authorized = true;
                        break;
                    } else if (auser.equals("#" + Client.userID)) {
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
                        JOptionPane.showMessageDialog(boardPanel.frame, "Access to Board was denied by Admin",
                                "Access Denied", JOptionPane.INFORMATION_MESSAGE);
                        System.exit(0);
                    }
                }
                break;
            case "loadBoard":
                boardPanel.shapes = event.shapes;
                boardPanel.frame.repaint();
                break;
            case "keyTyped":
                Text.draw(boardPanel.getGraphics(), event.startPoint, event.textInput);
                break;
            case "mouseMoved":
                boardPanel.addShape(new Text(event.startPoint, event.textInput));
                break;
            case "mouseDragged":
                if (event.currentErase) {
                    Eraser.draw(boardPanel.getGraphics(), event.eraserSize, event.points);
                } else {
                    switch (event.currentMode) {
                        case LINE:
                            Line.draw(boardPanel.getGraphics(), event.startPoint, event.endPoint);
                            break;
                        case RECT:
                            Rect.draw(boardPanel.getGraphics(), event.startPoint, event.endPoint, event.currentMode, event.currentColor);
                            break;
                        case OVAL:
                            Oval.draw(boardPanel.getGraphics(), event.startPoint, event.endPoint, event.currentMode, event.currentColor);
                            break;
                        case FREEFORM_LINE:
                            FreeHand.draw(boardPanel.getGraphics(), event.points);
                            break;
                        case TEXT:
                            break;
                        default:
                            System.err.println("Unsupported shape type");
                            break;
                    }
                }
                boardPanel.frame.repaint();
                break;
            case "mouseReleased":
                if (event.currentErase) {
                    boardPanel.addShape(new Eraser(event.points, event.eraserSize));
                } else {
                    switch (event.currentMode) {
                        case LINE:
                            boardPanel.addShape(new Line(event.startPoint, event.endPoint));
                            break;
                        case RECT:
                            boardPanel.addShape(new Rect(event.startPoint, event.endPoint, event.currentMode, event.currentColor));
                            break;
                        case OVAL:
                            boardPanel.addShape(new Oval(event.startPoint, event.endPoint, event.currentMode, event.currentColor));
                            break;
                        case FREEFORM_LINE:
                            boardPanel.addShape(new FreeHand(event.points));
                            break;
                        case TEXT:
                            break;
                        default:
                            System.err.println("Unsupported shape type");
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
            ArrayList<ServerEvent> boardEvents;
            try {
                boardEvents = boardPanel.server.getEvents(nextEventID);

                for (ServerEvent event : boardEvents) {
                    if (event.eventType.equals("joinRequest") && Client.isManager) {
                        dispatch(event);
                    }
                }

                ServerEvent latestUserList = null;
                for (ServerEvent event : boardEvents) {
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

                for (ServerEvent event : boardEvents) {
                    if (!(event.eventType.equals("joinRequest") || event.eventType.equals("userList"))) {
                        dispatch(event);
                    }
                }

                if (boardEvents.size() > 0) {
                    nextEventID = boardEvents.get(boardEvents.size() - 1).eventID + 1;
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
}

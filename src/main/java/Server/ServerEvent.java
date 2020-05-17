package main.java.Server;

import main.java.Client.Mode;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class ServerEvent implements Serializable {

    private static final long serialVersionUID = 1L;
    public final String eventType;
    public int eventID;
    public String userID;
    public ArrayList<String> userList;
    public Mode currentMode;
    public boolean currentFill;
    public Color currentColor;
    public boolean currentErase;
    public int eraserSize;
    public Point startPoint;
    public Point endPoint;
    public ArrayList<Point> points;
    public ArrayList<String> textInput;
    public ArrayList<Shape> shapes;

    public ServerEvent(String eType) {
        eventType = eType;
    }

}

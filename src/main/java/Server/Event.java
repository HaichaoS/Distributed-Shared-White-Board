package main.java.Server;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    public final String eventType;
    public int eventID;
    public String userID;
    public ArrayList<String> userList;
    public int currentShape;
    public int currentMode;
    public Color currentColor;
    public boolean erasing;
    public int eraserSize;
    public Point startPoint;
    public Point endPoint;
    public ArrayList<Point> points;
    public ArrayList<String> textInput;
    public ArrayList<Shape> shapes;

    public Event(String eType) {
        eventType = eType;
    }

}

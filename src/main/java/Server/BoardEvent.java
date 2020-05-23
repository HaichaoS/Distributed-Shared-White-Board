package Server;

import Client.Mode;
import Client.Shape;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class BoardEvent implements Serializable {

    public int eventID, eraserSize;
    public String eventType, userID;
    public boolean currentFill, currentEraser;
    public Mode currentMode;
    public Color currentColor;
    public Point startPoint, endPoint;
    public ArrayList<Point> points;
    public ArrayList<String> userList, textInput;
    public ArrayList<Shape> shapes;

    public BoardEvent(String event) {
        this.eventType = event;
    }

}

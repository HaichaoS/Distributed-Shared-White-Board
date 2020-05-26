package Server;

import Client.Modes.Mode;
import Client.Modes.Shape;
import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class BoardHandler implements Serializable {

    public int eventID, eraserSize;
    public String eventType, userID;
    public boolean currentFill, currentEraser;
    public Mode currentMode;
    public Color currentColor;
    public Point startPoint, endPoint;
    public ArrayList<Point> points;
    public ArrayList<String> userList, textInput;
    public ArrayList<Shape> shapes;

    public BoardHandler(String event) {
        this.eventType = event;
    }

}

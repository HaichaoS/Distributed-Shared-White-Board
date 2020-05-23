package Client;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class FreeLine implements Shape, Serializable {

    private ArrayList<Point> points;

    public FreeLine(ArrayList<Point> pts) {
        points = pts;
    }

    public static void drawFreeLine(Graphics gfx, ArrayList<Point> points) {
        for (int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1);
            Point p2 = points.get(i);
            gfx.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    public void draw(Graphics gfx) {
        drawFreeLine(gfx, points);
    }
}
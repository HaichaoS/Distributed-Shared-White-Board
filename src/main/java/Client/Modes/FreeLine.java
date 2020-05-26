package Client.Modes;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class FreeLine implements Shape, Serializable {

    private ArrayList<Point> points;
    private Color color;

    public FreeLine(ArrayList<Point> pts, Color currentColor) {
        this.points = pts;
        this.color = currentColor;
    }

    public static void drawFreeLine(Graphics gfx, ArrayList<Point> points, Color currentColor) {
        for (int i = 1; i < points.size(); i++) {
            Point p1 = points.get(i - 1);
            Point p2 = points.get(i);
            gfx.setColor(currentColor);
            gfx.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    public void draw(Graphics gfx) {
        drawFreeLine(gfx, points, color);
    }
}
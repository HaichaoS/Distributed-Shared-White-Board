package Client.Modes;

import Client.Client;
import Client.Modes.Shape;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class Eraser implements Shape, Serializable {

    private static final int seed = 3;
    private int size;
    private ArrayList<Point> points;

    public Eraser(ArrayList<Point> pts, int size) {
        this.size = size;
        this.points = pts;
    }

    private static void erasePoint(Graphics gfx, int size, Point point) {
        size = size * seed;
        int x = (point.x - size) > 0 ? (point.x - size) : 0;
        int y = (point.y - size) > 0 ? (point.y - size) : 0;
        int width = size * 2;
        int height = size * 2;

        gfx.fillRect(x, y, width, height);
    }

    public static void drawEraser(Graphics gfx, int size, ArrayList<Point> points) {
        Color clr = gfx.getColor();
        gfx.setColor(new Color(240, 240, 240));
        for (Point point : points) {
            erasePoint(gfx, size, point);
        }
        gfx.setColor(clr);
    }

    @Override
    public void draw(Graphics gfx) {
        drawEraser(gfx, size, points);
    }
}

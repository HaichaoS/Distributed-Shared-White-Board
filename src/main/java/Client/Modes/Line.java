package Client.Modes;

import java.awt.*;
import java.io.Serializable;

/**
 * Haichao Song
 * Description:
 * responsible for defining the way line draws on the whiteboard
 */
public class Line implements Shape, Serializable {

    private final Point startPoint;
    private final Point endPoint;
    private final Color color;

    public Line(Point sPoint, Point ePoint, Color currentColor) {
        this.startPoint = sPoint;
        this.endPoint = ePoint;
        this.color = currentColor;

    }

    public static void draw(Graphics gfx, Point sPoint, Point ePoint, Color currentColor) {
        gfx.setColor(currentColor);
        gfx.drawLine(sPoint.x, sPoint.y, ePoint.x, ePoint.y);
    }

    @Override
    public void draw(Graphics gfx) {
        draw(gfx, startPoint, endPoint, color);
    }
}


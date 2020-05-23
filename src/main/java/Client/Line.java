package Client;

/**
 * Haichao Song
 * Description:
 */
import java.awt.*;
import java.io.Serializable;

public class Line implements Shape, Serializable {

    private final Point startPoint;
    private final Point endPoint;

    public Line(Point sPoint, Point ePoint) {
        startPoint = sPoint;
        endPoint = ePoint;
    }

    public static void draw(Graphics gfx, Point sPoint, Point ePoint) {
        gfx.drawLine(sPoint.x, sPoint.y, ePoint.x, ePoint.y);
    }

    public void draw(Graphics gfx) {
        gfx.drawLine(startPoint.x, startPoint.y, endPoint.x, endPoint.y);
    }
}


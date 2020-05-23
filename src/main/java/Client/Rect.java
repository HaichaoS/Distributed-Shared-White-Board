package Client;

/**
 * Haichao Song
 * Description:
 */
import java.awt.*;
import java.io.Serializable;

public class Rect implements Shape, Serializable {
    private static final int UNFILLED = 0;
    private static final long serialVersionUID = 1L;
    private final Point startPoint;
    private final Point endPoint;
    private final boolean fill;
    private final Color color;


    public Rect(Point sPoint, Point ePoint, boolean currentFill, Color currentColor) {
        startPoint = sPoint;
        endPoint = ePoint;
        fill = currentFill;
        color = currentColor;
    }

    public static void drawRect(Graphics gfx, Point sPoint, Point ePoint, boolean currentFill, Color currentColor) {
        int x, y, width, height;

        if (sPoint.x < ePoint.x) {
            x = sPoint.x;
            width = ePoint.x - sPoint.x;
        } else {
            x = ePoint.x;
            width = sPoint.x - ePoint.x;
        }

        if (sPoint.y < ePoint.y) {
            y = sPoint.y;
            height = ePoint.y - sPoint.y;
        } else {
            y = ePoint.y;
            height = sPoint.y - ePoint.y;
        }

        if (currentFill == false) {
            gfx.drawRect(x, y, width, height);
        } else {
            gfx.setColor(currentColor);
            gfx.fillRect(x, y, width, height);
        }
    }

    @Override
    public void draw(Graphics gfx) {
        drawRect(gfx, startPoint, endPoint, fill, color);
    }
}

package Client.Modes;

import java.awt.*;
import java.io.Serializable;

/**
 * Haichao Song
 * Description:
 * responsible for defining the way rectangular draws on the whiteboard
 */
public class Rect implements Shape, Serializable {

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
            gfx.setColor(currentColor);
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

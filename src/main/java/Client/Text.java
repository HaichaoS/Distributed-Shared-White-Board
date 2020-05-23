package Client;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 */
public class Text implements Shape, Serializable {

    private static final long serialVersionUID = 1L;
    private final Point startPoint;
    private ArrayList<String> lines;

    public Text(Point sPoint, ArrayList<String> input) {
        startPoint = sPoint;
        lines = input;
    }

    public static void drawText(Graphics gfx, Point sPoint, ArrayList<String> lines) {
        int spacing = gfx.getFontMetrics().getHeight();
        int n = 0;
        for (String line : lines) {
            gfx.drawString(line, sPoint.x, sPoint.y + (n * spacing));
            n++;
        }
    }

    @Override
    public void draw(Graphics gfx) {
        drawText(gfx, startPoint, lines);
    }
}


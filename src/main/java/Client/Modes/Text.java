package Client.Modes;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Haichao Song
 * Description:
 * responsible for defining the way text draws on the whiteboard
 */
public class Text implements Shape, Serializable {

    private final Point startPoint;
    private ArrayList<String> lines;
    private Color color;

    public Text(Point sPoint, ArrayList<String> input, Color currentColor) {
        this.startPoint = sPoint;
        this.lines = input;
        this.color = currentColor;

    }

    public static void drawText(Graphics gfx, Point sPoint, ArrayList<String> lines, Color currentColor) {
        int spacing = gfx.getFontMetrics().getHeight();
        int n = 0;
        for (String line : lines) {
            gfx.setColor(currentColor);
            gfx.drawString(line, sPoint.x, sPoint.y + (n * spacing));
            n++;
        }
    }

    @Override
    public void draw(Graphics gfx) {
        drawText(gfx, startPoint, lines, color);
    }
}


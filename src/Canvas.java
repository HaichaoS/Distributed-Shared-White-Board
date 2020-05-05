import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Team: Haichao Song
 * Description:
 */
public class Canvas extends JPanel {

    private BufferedImage canvas;

    public Canvas(int height, int width) {
        super();
        this.canvas = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_ARGB);
        this.setFocusable(true);
    }

    public Point drawLine(Point start, Point end, Color colour, int size) {
        Graphics2D g = (Graphics2D)canvas.getGraphics();
        Color col = Color.BLACK;
        int weight = 1;
        if (colour != null)
            col = colour;
        if (size > 0)
            weight = size;

        g.setColor(col);
        g.setStroke(new BasicStroke(weight));
        synchronized (Canvas.class) {
            g.drawLine(start.x, start.y, end.x, end.y);
        }
        this.repaint();
        return end;
    }
    

}

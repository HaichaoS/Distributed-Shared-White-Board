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

    public Point drawLine(Point start, Point end, Color color, int size) {
        Graphics2D g = (Graphics2D)canvas.getGraphics();
        Color col = Color.BLACK;
        int weight = 1;
        if (color != null)
            col = color;
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

    public Point drawText(Point start, char letter, Font font, Color color) {
        Graphics2D g = (Graphics2D)canvas.getGraphics();
        Font f = new Font("Serif", Font.PLAIN, 12);
        Color col = Color.BLACK;
        if (color != null)
            col = color;
        if (font != null)
            f = font;
        String text = String.valueOf(letter);
        g.setColor(col);
        g.setFont(f);
        synchronized (Canvas.class) {
            g.drawString(text, start.x, start.y);
        }
        FontMetrics metrics = g.getFontMetrics();

        Point nextPoint = new Point(start);
        nextPoint.x += metrics.stringWidth(text);

        this.repaint();
        return nextPoint;
    }

    public void clearCanvas() {
        Graphics2D g = (Graphics2D)canvas.createGraphics();
        g.setBackground(new Color(255, 255, 255, 0));
        synchronized (Canvas.class) {
            g.clearRect(0, 0, this.getWidth(), this.getHeight());
        }
        this.repaint();
    }

    public BufferedImage getBufferedImage() {return canvas;}

}

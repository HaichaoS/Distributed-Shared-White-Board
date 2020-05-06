import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * Haichao Song
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

    public void drawShape(Shape s, Color colour, boolean fillShape,
                          boolean border, int weight, Color borderColour) {

        Graphics2D g = (Graphics2D)canvas.getGraphics();

        Color col = Color.BLACK;
        Color borderCol = Color.LIGHT_GRAY;

        int borderWeight = 1;
        if (colour != null)
            col = colour;
        if (borderColour != null)
            borderCol = borderColour;
        if (weight > 0)
            borderWeight = weight;

        g.setColor(col);
        synchronized (Canvas.class) {
            if (fillShape)
                g.fill(s);
            else
                g.draw(s);

            if (border) {
                g.setColor(borderCol);
                g.setStroke(new BasicStroke(borderWeight));
                g.draw(s);
            }
            this.repaint();
        }
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

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(canvas.getWidth(), canvas.getHeight());
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D graphics = (Graphics2D)g.create();
        Map<RenderingHints.Key, Object> hintMap = new HashMap<>();
        // Set any rendering hints.
        hintMap.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        hintMap.put(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        RenderingHints hints = new RenderingHints(hintMap);
        graphics.setRenderingHints(hints);
        graphics.drawImage(canvas, 0, 0, Color.WHITE, null);
    }

}

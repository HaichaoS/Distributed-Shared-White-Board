import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;

/**
 * Haichao Song
 * Description:
 */
public class CanvasController extends JPanel {

    private Canvas canvas;
    private FlowLayout layout, toolInnerLayout;
    private CardLayout toolLayout;
    private MatteBorder toolBorder;
    private JLabel modeLabel, fontPreview, weightLabel;
    private JComboBox modeSelect, weightSelect;
    private JButton colourPicker;
    private JPanel colPreview, toolBox, fontTools, lineTools;

    private Point lastPoint, firstPoint;
    private Mode mode;
    private Color colour;
    private Font font;
    private int lineWeight;
    private long lastUpdateTime;


    public CanvasController(Canvas canvas) {
        super();
        this.canvas = canvas;
        this.layout = new FlowLayout();
        this.toolLayout = new CardLayout();
        this.toolInnerLayout = new FlowLayout(FlowLayout.CENTER, 5, 2);
        this.toolBorder = new MatteBorder(0, 1, 0, 0, Color.LIGHT_GRAY);
        this.setLayout(layout);

        this.modeLabel = new JLabel("Tool:");
        this.modeSelect = new JComboBox<>(Mode.values());
        this.modeSelect.setEditable(false);

        this.colourPicker = new JButton("Colour");
        this.colPreview = new JPanel();
        this.colPreview.setBorder(new LineBorder(Color.BLACK));

        this.toolBox = new JPanel(toolLayout);

        this.fontTools = new JPanel(toolInnerLayout);
        this.fontTools.setBorder(toolBorder);

        this.fontPreview = new JLabel("Text");
        this.fontPreview.setBorder(new EmptyBorder(0, 5, 0, 5));

        this.lineTools = new JPanel(toolInnerLayout);
        this.lineTools.setBorder(toolBorder);

        this.weightLabel = new JLabel("Weight:");
        this.weightSelect = new JComboBox<>(new Integer[]{1, 2, 4, 8, 16});
        this.weightSelect.setEditable(false);

        this.mode = Mode.FREE;
        this.font = new Font("Arial", Font.PLAIN, 12);
        this.colour = Color.BLACK;
        this.lineWeight = 1;
        this.lastPoint = null;
        this.firstPoint = null;
        this.lastUpdateTime = 0;

        setupLayout();
    }

    public void setupLayout() {
        
    }

}

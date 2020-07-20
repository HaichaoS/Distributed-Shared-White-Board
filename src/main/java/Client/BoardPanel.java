package Client;

import Client.Modes.Mode;
import Client.Modes.Shape;
import Remote.IServer;
import Server.BoardHandler;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Haichao Song
 * Description:
 * responsible for 1) defining the menu on the white board
 * 2) handling functions on the menu
 * 3) call add board events to send the event to the server.
 */
public class BoardPanel extends JPanel implements ActionListener, KeyListener {

    protected IServer IServer;
    protected JFrame frame;
    protected ArrayList<Shape> shapes = new ArrayList<>();
    private Point startPoint = new Point(5, 10);
    private Mode currentMode = Mode.LINE;
    private boolean currentFill = false;
    private Color currentColor = getForeground();
    private boolean currentEraser = false;
    private int eraserSize = 1;
    private ArrayList<Point> points;
    private ArrayList<String> text;
    private MouseAdapter mouseAdapter;
    private JMenuBar menuBar;
    private JMenu menu, menuMode, menuEraser;
    private JMenuItem menuItem;
    private JRadioButtonMenuItem rbMenuItem;
    private ButtonGroup group;
    private JCheckBoxMenuItem cbMenuItem;

    public BoardPanel(IServer IServer, JFrame frame) {
        this.IServer = IServer;
        this.frame = frame;
        mouseAdapter = new MouseAdapter();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addKeyListener(this);
        createMenuBar();
    }

    private void createMenuBar() {

        menuBar = new JMenuBar();
        menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        if (Client.isManager) {

            menuItem = new JMenuItem("Open");
            menuItem.setActionCommand("OpenFile");
            menuItem.addActionListener(this);
            menuItem.setMnemonic(KeyEvent.VK_O);
            menu.add(menuItem);

            menuItem = new JMenuItem("Save");
            menuItem.setActionCommand("SaveAs");
            menuItem.addActionListener(this);
            menuItem.setMnemonic(KeyEvent.VK_S);
            menu.add(menuItem);

            menu.addSeparator();

            menuItem = new JMenuItem("Reset");
            menuItem.setActionCommand("Reset");
            menuItem.addActionListener(this);
            menuItem.setMnemonic(KeyEvent.VK_N);
            menu.add(menuItem);

            menu.addSeparator();
        }

        menu.add(new AbstractAction("Exit") {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (frame.isActive()) {
                    WindowEvent windowClosing = new WindowEvent(frame,
                            WindowEvent.WINDOW_CLOSING);
                    frame.dispatchEvent(windowClosing);
                }
            }
        });

        menuBar.add(menu);

        menu = new JMenu("Modes");
        menu.setMnemonic(KeyEvent.VK_M);

        group = new ButtonGroup();

        rbMenuItem = new JRadioButtonMenuItem("Line");
        rbMenuItem.setActionCommand("Line");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_L);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Rectangle");
        rbMenuItem.setActionCommand("Rectangle");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setMnemonic(KeyEvent.VK_R);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Oval");
        rbMenuItem.setActionCommand("Oval");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setMnemonic(KeyEvent.VK_O);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Free Line");
        rbMenuItem.setActionCommand("Free Line");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setMnemonic(KeyEvent.VK_F);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Text");
        rbMenuItem.setActionCommand("Text");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setMnemonic(KeyEvent.VK_T);
        group.add(rbMenuItem);
        menu.add(rbMenuItem);

        menuBar.add(menu);

        menuMode = new JMenu("Option");
        menuMode.setMnemonic(KeyEvent.VK_P);

        group = new ButtonGroup();

        rbMenuItem = new JRadioButtonMenuItem("Unfilled");
        rbMenuItem.setActionCommand("Unfilled");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_U);
        group.add(rbMenuItem);
        menuMode.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Filled");
        rbMenuItem.setActionCommand("Filled");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setMnemonic(KeyEvent.VK_F);
        group.add(rbMenuItem);
        menuMode.add(rbMenuItem);

        menuMode.addSeparator();

        menuItem = new JMenuItem("Colors");
        menuItem.setActionCommand("Colors");
        menuItem.addActionListener(this);
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuMode.add(menuItem);

        menuBar.add(menuMode);

        menuEraser = new JMenu("Eraser");
        menuEraser.setMnemonic(KeyEvent.VK_E);

        cbMenuItem = new JCheckBoxMenuItem("Erase");
        cbMenuItem.setActionCommand("ToggleErase");
        cbMenuItem.addActionListener(this);
        cbMenuItem.setMnemonic(KeyEvent.VK_E);
        menuEraser.add(cbMenuItem);

        menuEraser.addSeparator();

        group = new ButtonGroup();

        rbMenuItem = new JRadioButtonMenuItem("Small");
        rbMenuItem.setActionCommand("Small");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_S);
        group.add(rbMenuItem);
        menuEraser.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Medium");
        rbMenuItem.setActionCommand("Medium");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_M);
        group.add(rbMenuItem);
        menuEraser.add(rbMenuItem);

        rbMenuItem = new JRadioButtonMenuItem("Large");
        rbMenuItem.setActionCommand("Large");
        rbMenuItem.addActionListener(this);
        rbMenuItem.setSelected(true);
        rbMenuItem.setMnemonic(KeyEvent.VK_L);
        group.add(rbMenuItem);
        menuEraser.add(rbMenuItem);

        menuBar.add(menuEraser);

        frame.setJMenuBar(menuBar);
    }

    public synchronized void addShape(Shape shape) {
        shapes.add(shape);
    }

    @Override
    public synchronized void paintComponent(Graphics gfx) {
        for (Shape shape : shapes) {
            shape.draw(gfx);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action) {
            case "Reset":
                reset();
                break;
            case "OpenFile":
                load();
                break;
            case "SaveAs":
                save();
                break;
            case "Line":
                currentMode = Mode.LINE;
                break;
            case "Rectangle":
                currentMode = Mode.RECT;
                break;
            case "Oval":
                currentMode = Mode.OVAL;
                break;
            case "Free Line":
                currentMode = Mode.FREEFORM_LINE;
                break;
            case "Text":
                currentMode = Mode.TEXT;
                break;
            case "Colors":
                currentColor = JColorChooser.showDialog(this, "Fill Color", getForeground());
                break;
            case "Unfilled":
                currentFill = false;
                break;
            case "Filled":
                currentFill = true;
                break;
            case "ToggleErase":
                currentEraser = !currentEraser;
                break;
            case "Small":
                eraserSize = 1;
                break;
            case "Medium":
                eraserSize = 2;
                break;
            case "Large":
                eraserSize = 3;
                break;
        }
    }

    private void reset() {
        BoardHandler event = new BoardHandler("load");
        event.shapes = new ArrayList<>();
        try {
            IServer.addBoardEvent(event);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    private void load() {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter[] fileFilters = fileChooser.getChoosableFileFilters();
        for (FileFilter filter : fileFilters) {
            fileChooser.removeChoosableFileFilter(filter);
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Board Files (*.draw)", "draw");
        fileChooser.setFileFilter(filter);

        int value = fileChooser.showOpenDialog(frame);
        if (value == JFileChooser.APPROVE_OPTION) {
            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(
                        fileChooser.getSelectedFile()));
                Object board = ois.readObject();
                if (board instanceof ArrayList<?>) {
                    BoardHandler event = new BoardHandler("load");
                    event.shapes = (ArrayList<Shape>) board;
                    try {
                        IServer.addBoardEvent(event);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Corrupted file contents",
                            "Corrupted File", JOptionPane.INFORMATION_MESSAGE);
                }
                ois.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void save() {
        JFileChooser fileChooser = new JFileChooser();
        FileFilter[] fileFilters = fileChooser.getChoosableFileFilters();
        for (FileFilter filter : fileFilters) {
            fileChooser.removeChoosableFileFilter(filter);
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Board Files (*.draw)", "draw");
        fileChooser.setFileFilter(filter);

        int value = fileChooser.showSaveDialog(frame);
        if (value == JFileChooser.APPROVE_OPTION) {
            try {
                ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(
                        fileChooser.getSelectedFile()));
                oos.writeObject(shapes);
                oos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        keyTyping(e);
    }

    public void keyTyping(KeyEvent e) {
        if (currentMode == Mode.TEXT) {
            if (text == null) {
                text = new ArrayList<>();
                text.add("");
            }

            char c = e.getKeyChar();
            if (c == '\n') {
                text.add("");
            } else {
                int lastLine = text.size() - 1;
                String lastStr = text.get(lastLine);
                text.set(lastLine, lastStr + c);
            }
            BoardHandler event = new BoardHandler("keyTyped");
            event.startPoint = startPoint;
            event.textInput = text;
            try {
                IServer.addBoardEvent(event);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    class MouseAdapter extends MouseInputAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            if ((text != null) && (!text.isEmpty())) {
                BoardHandler event = new BoardHandler("mouseMoved");
                event.startPoint = startPoint;
                event.textInput = text;
                try {
                    IServer.addBoardEvent(event);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                int spacing = getGraphics().getFontMetrics().getHeight();
                startPoint = new Point(startPoint.x, startPoint.y + spacing);
                text = null;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            startPoint = e.getPoint();
            if (currentEraser || (currentMode == Mode.FREEFORM_LINE)) {
                points = new ArrayList<>();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point endPoint = e.getPoint();

            if (currentEraser || (currentMode == Mode.FREEFORM_LINE)) {
                points.add(new Point(e.getX(), e.getY()));
            }
            BoardHandler event = new BoardHandler("mouseDragged");
            sendServer(event, endPoint);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point endPoint = e.getPoint();
            BoardHandler event = new BoardHandler("mouseReleased");
            sendServer(event, endPoint);
        }

        public void sendServer(BoardHandler event, Point endPoint) {
            event.currentMode = currentMode;
            event.currentFill = currentFill;
            event.currentColor = currentColor;
            event.currentEraser = currentEraser;
            event.eraserSize = eraserSize;
            event.startPoint = startPoint;
            event.endPoint = endPoint;
            event.points = points;
            try {
                IServer.addBoardEvent(event);
            } catch (RemoteException e1) {
                e1.printStackTrace();
            }
        }
    }
}
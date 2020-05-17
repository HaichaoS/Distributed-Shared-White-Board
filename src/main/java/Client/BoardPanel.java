package main.java.Client;

import main.java.Server.Server;
import main.java.Server.ServerEvent;

import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.RemoteException;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * Haichao Song
 * Description:
 */
public class BoardPanel extends JPanel implements ActionListener, KeyListener {

    private Server server;
    private String id;
    private JFrame frame;
    private MouseAdapter mouseAdapter;
    private Mode currentMode;
    private Color currentColor = getForeground();
    private boolean currentFill = false;
    private boolean currentErase = false;
    private int eraserSize = 1;
    private ArrayList<Shape> shapes = new ArrayList<>();
    private ArrayList<String> textInput;
    private Point startPoint = new Point(5, 10);
    private ArrayList<Point> points;



    public BoardPanel(Server server, String id, JFrame frame) {
        this.server = server;
        this.id = id;
        this.frame = frame;
        mouseAdapter = new MouseAdapter();
        addMouseListener(mouseAdapter);
        addMouseMotionListener(mouseAdapter);
        addKeyListener(this);
        createMenu();
    }

    public void createMenu() {
        JMenuBar menuBar = new JMenuBar();


        JMenu menu = new JMenu("File");
        menu.setMnemonic(KeyEvent.VK_F);

        if (Client.isManager) {
            JMenuItem menuItem = new JMenuItem("New Board");
            menuItem.setActionCommand("NewBoard");
            menuItem.addActionListener(this);
            menuItem.setMnemonic(KeyEvent.VK_N);
            menu.add(menuItem);

            menuItem = new JMenuItem("Open File...");
            menuItem.setActionCommand("OpenFile");
            menuItem.addActionListener(this);
            menuItem.setMnemonic(KeyEvent.VK_O);
            menu.add(menuItem);

            menu.addSeparator();

            menuItem = new JMenuItem("Save As...");
            menuItem.setActionCommand("SaveAs");
            menuItem.addActionListener(this);
            menuItem.setMnemonic(KeyEvent.VK_S);
            menu.add(menuItem);

            menu.addSeparator();
        }

        menu.add(new AbstractAction("Exit") {
            private static final long serialVersionUID = 1L;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (frame.isActive()) {
                    WindowEvent windowClosing = new WindowEvent(frame, WindowEvent.WINDOW_CLOSING);
                    frame.dispatchEvent(windowClosing);
                }
            }
        });

        menuBar.add(menu);

        menu = new JMenu("Shapes");
        menu.setMnemonic(KeyEvent.VK_S);
        JRadioButtonMenuItem rbMenuItem;

        ButtonGroup group = new ButtonGroup();

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
        rbMenuItem.setActionCommand("Free Hand");
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

        JMenu menuMode = new JMenu("Mode");
        menuMode.setMnemonic(KeyEvent.VK_M);

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

        JMenuItem menuItem = new JMenuItem("Colors");
        menuItem.setActionCommand("Colors");
        menuItem.addActionListener(this);
        menuItem.setMnemonic(KeyEvent.VK_C);
        menuMode.add(menuItem);

        menuBar.add(menuMode);

        JMenu menuEraser = new JMenu("Eraser");
        menuEraser.setMnemonic(KeyEvent.VK_E);
        JCheckBoxMenuItem cbMenuItem;

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
        rbMenuItem.setMnemonic(KeyEvent.VK_M);
        group.add(rbMenuItem);
        menuEraser.add(rbMenuItem);

        menuBar.add(menuEraser);

        menu = new JMenu("Help");
        menu.setMnemonic(KeyEvent.VK_H);

        menuItem = new JMenuItem("About...");
        menuItem.setActionCommand("About");
        menuItem.addActionListener(this);
        menuItem.setMnemonic(KeyEvent.VK_A);
        menu.add(menuItem);

        menuBar.add(menu);

        frame.setJMenuBar(menuBar);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String action = e.getActionCommand();
        switch (action) {
            case "NewBoard":
                newBoard();
                break;
            case "OpenFile":
                loadBoard();
                break;
            case "SaveAs":
                saveBoard();
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
                currentColor = JColorChooser.showDialog(this, "Fill Color...", getForeground());
                break;
            case "Unfilled":
                currentFill = false;
                break;
            case "Filled":
                currentFill = true;
                break;
            case "ToggleErase":
                currentErase = !currentErase;
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
            case "About":
                aboutApp();
                break;
        }
    }

    private void newBoard() {
        ServerEvent event = new ServerEvent("loadBoard");
        event.shapes = new ArrayList<>();
        try {
            server.addEvent(event);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void loadBoard() {
        JFileChooser chooser = new JFileChooser();
        FileFilter[] filefilters = chooser.getChoosableFileFilters();
        for (FileFilter filter : filefilters) {
            chooser.removeChoosableFileFilter(filter);
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Board Files (*.brd)", "brd");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showOpenDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                ObjectInputStream is = new ObjectInputStream(new FileInputStream(
                        chooser.getSelectedFile()));
                Object board = is.readObject();
                if (board instanceof ArrayList<?>) {
                    ServerEvent event = new ServerEvent("loadBoard");
                    event.shapes = (ArrayList<Shape>) board;
                    try {
                        server.addEvent(event);
                    } catch (RemoteException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Corrupted file contents",
                            "Corrupted File", JOptionPane.INFORMATION_MESSAGE);
                }
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void saveBoard() {
        JFileChooser chooser = new JFileChooser();
        FileFilter[] filefilters = chooser.getChoosableFileFilters();
        for (FileFilter filter : filefilters) {
            chooser.removeChoosableFileFilter(filter);
        }
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Board Files (*.brd)", "brd");
        chooser.setFileFilter(filter);

        int returnVal = chooser.showSaveDialog(frame);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(
                        chooser.getSelectedFile()));
                os.writeObject(shapes);
                os.close();
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
            if (textInput == null) {
                textInput = new ArrayList<>();
                textInput.add("");
            }

            char c = e.getKeyChar();
            if (c == '\n') {
                textInput.add("");
            } else {
                int lastLine = textInput.size() - 1;
                String lastStr = textInput.get(lastLine);
                textInput.set(lastLine, lastStr + c);
            }
            //send to server
            ServerEvent event = new ServerEvent("keyTyped");
            event.startPoint = startPoint;
            event.textInput = textInput;
            try {
                server.addEvent(event);
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

    private void aboutApp() {
        JOptionPane.showMessageDialog(frame, "Distributed Board\nBy: Walid Moustafa\nMIT\nMelbourne University",
                "About Shared Board", JOptionPane.INFORMATION_MESSAGE);
    }

    class MouseAdapter extends MouseInputAdapter {

        @Override
        public void mouseMoved(MouseEvent e) {
            if ((textInput != null) && (!textInput.isEmpty())) {
                //send to server
                ServerEvent event = new ServerEvent("mouseMoved");
                event.startPoint = startPoint;
                event.textInput = textInput;
                try {
                    server.addEvent(event);
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }
                //
                int spacing = getGraphics().getFontMetrics().getHeight();
                startPoint = new Point(startPoint.x, startPoint.y + spacing);
                textInput = null;
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            startPoint = e.getPoint();
            if (currentErase || (currentMode == Mode.FREEFORM_LINE)) {
                points = new ArrayList<>();
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            Point endPoint = e.getPoint();

            if (currentErase || (currentMode == Mode.FREEFORM_LINE)) {
                points.add(new Point(e.getX(), e.getY()));
            }

            //send to server
            ServerEvent event = new ServerEvent("mouseDragged");
            sendServer(event, endPoint);
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            Point endPoint = e.getPoint();

            //send to server
            ServerEvent event = new ServerEvent("mouseReleased");
            sendServer(event, endPoint);
        }
    }

    public void sendServer(ServerEvent event, Point endPoint) {
        event.currentMode = currentMode;
        event.currentFill = currentFill;
        event.currentColor = currentColor;
        event.currentErase = currentErase;
        event.eraserSize = eraserSize;
        event.startPoint = startPoint;
        event.endPoint = endPoint;
        event.points = points;
        try {
            server.addEvent(event);
        } catch (RemoteException e1) {
            e1.printStackTrace();
        }
    }


}

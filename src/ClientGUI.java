import distributedwhiteboard.Client;
import distributedwhiteboard.Server;
import distributedwhiteboard.Triple;
import distributedwhiteboard.gui.WhiteboardCanvas;
import distributedwhiteboard.gui.WhiteboardControls;
import distributedwhiteboard.gui.WhiteboardGUI;
import distributedwhiteboard.gui.WhiteboardMenu;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Haichao Song
 * Description:
 */
public class ClientGUI extends JFrame{

    private SpringLayout layout;
    private WhiteboardMenu menu;
    private WhiteboardCanvas canvas;
    private WhiteboardControls controls;
    private JScrollPane scroller;
    private Thread repainter;
    private Boolean manage;

    public ClientGUI (boolean manage) {
        create(manage);
    }

    public void create(boolean manage) {

        this.layout = new SpringLayout();
        Container contentPane = this.getContentPane();
        contentPane.setLayout(layout);

        this.menu = new WhiteboardMenu(this);
        this.setJMenuBar(this.menu);

        this.canvas = new WhiteboardCanvas(800, 800);
        this.controls = new WhiteboardControls(canvas);
        this.scroller = new JScrollPane(canvas);
        this.scroller.setBackground(Color.LIGHT_GRAY);

        // Add the canvas and controls to the main GUI. Canvas above controls.
        contentPane.add(controls);
        this.layout.putConstraint(SpringLayout.WEST, controls, 5,
                SpringLayout.WEST, contentPane);
        this.layout.putConstraint(SpringLayout.SOUTH, controls, -5,
                SpringLayout.SOUTH, contentPane);
        this.layout.putConstraint(SpringLayout.EAST, controls, -5,
                SpringLayout.EAST, contentPane);

        contentPane.add(scroller);
        this.layout.putConstraint(SpringLayout.NORTH, scroller, 5,
                SpringLayout.NORTH, contentPane);
        this.layout.putConstraint(SpringLayout.WEST, scroller, 5,
                SpringLayout.WEST, contentPane);
        this.layout.putConstraint(SpringLayout.SOUTH, scroller, -5,
                SpringLayout.NORTH, controls);
        this.layout.putConstraint(SpringLayout.EAST, scroller, -5,
                SpringLayout.EAST, contentPane);

        // Set up the rest of the JFrame.
        this.setContentPane(contentPane);
        this.setTitle("Distributed Whiteboard");
        this.setSize(828, 893);
        this.setLocationByPlatform(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setVisible(true);

        String answ = JOptionPane.showInputDialog(this,
                "Enter display name:",
                "Set Name",
                JOptionPane.QUESTION_MESSAGE);
        String title = String.format("Distributed Whiteboard - %s", answ);
        ((JFrame)this).setTitle(title);
        Client.getInstance().setClientName(answ);

    }
//    public static ImageIcon createIcon(String path) {
//        return WhiteboardGUI.createIcon(path, "");
//    }

    public static ImageIcon createIcon(String path) {
        java.net.URL imgURL = WhiteboardGUI.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL, "");
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    public boolean saveCanvas(File file, WhiteboardMenu.SaveType type) {
        if (type == WhiteboardMenu.SaveType.UNSUPPORTED)
            return false;

        BufferedImage img = canvas.getBufferedImage();
        try {
            ImageIO.write(img, type.name().toLowerCase(), file);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
            return false;
        }
        return true;
    }


    public WhiteboardCanvas getCanvas() { return this.canvas; }

    public void setManage(boolean manage) {this.manage = manage; }

    public void updateClientList() {
        Client client = Client.getInstance();
        Set<Triple<String, String, Integer>> clients = client.getKnownHosts();

        Set<String> names = new HashSet<>();
        names.add(client.getClientName());
        for (Triple<String, String, Integer> host : clients) {
            int dupCount = 0;
            String name = host.One;
            while (true) {
                if (dupCount > 0) {
                    name = String.format("%s (%d)", host.One, dupCount);
                }
                if (names.add(name)) break;
                dupCount++;
            }
        }
        String[] nameArray = new String[names.size()];
        nameArray = names.toArray(nameArray);
        menu.setClientList(nameArray);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            Server server = Server.getInstance();
            Client client = Client.getInstance();
            if (server != null)
                server.stopServer();
            if (client != null)
                client.stopClient();
            if (this.repainter != null) {
                try {
                    ClientGUI.this.repainter.interrupt();
                    ClientGUI.this.repainter.join();
                    System.out.println("Repainter stopped.");
                } catch (InterruptedException ex) {
                    System.err.println("Failed to stop repainter gracefully.");
                }
            }
            dispose();
        }
    }

}

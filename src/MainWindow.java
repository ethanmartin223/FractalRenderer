import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");

        new MainWindow();
    }

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //setAlwaysOnTop(true);
        setSize(800, 800);
        setTitle("Triangles!");
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);

        FractalRenderer triangle= new FractalRenderer(getWidth(),getHeight(), 2, 3);
        add(triangle,BorderLayout.CENTER);

        JPanel sidePanel = new JPanel();
        sidePanel.setSize(300,getHeight());
        add(sidePanel,BorderLayout.EAST);

        sidePanel.setLayout(new GridLayout(8,1));

        setVisible(true);

        Timer timer = new Timer(0, e->triangle.repaint());
        timer.setRepeats(true);
        timer.start();
    }
}
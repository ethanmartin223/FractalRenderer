import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    boolean[][] pixels;
    float scale = 1;

    public static void main(String[] args) {
        System.setProperty("sun.java2d.opengl", "true");

        new MainWindow();
    }

    public MainWindow() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setAlwaysOnTop(true);
        setSize(800, 800);
        setTitle("Triangles!");
        setLocationRelativeTo(null);
        setLayout(new GridLayout());
        setResizable(false);

        FractalRenderer triangle= new FractalRenderer(800,800, 1);
        add(triangle);
        setVisible(true);

        Timer timer = new Timer(0, e->triangle.repaint());
        timer.setRepeats(true);
        timer.start();
    }
}
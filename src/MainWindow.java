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

        FractalRenderer triangle= new FractalRenderer(getWidth(),getHeight(), 2, 4);
        add(triangle,BorderLayout.CENTER);
        setVisible(true);

        Timer timer = new Timer(0,e->triangle.repaint());
        timer.setRepeats(true);
        timer.start();
    }
}
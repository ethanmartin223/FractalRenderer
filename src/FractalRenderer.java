import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;

public class FractalRenderer extends JPanel {
    float framesPerSecond;

    double[][][] pixels;
    double scale;
    int pixelScale;
    long renderScale;
    long timeStamp;
    long deltaTime;
    int xDrag, yDrag, xPress, yPress;
    double xPosition, yPosition, lastReleasedPositionY, lastReleasedPositionX;
    int numberOfThreads;
    NumberCruncher[] threads;

    public FractalRenderer(int w, int h, int resolution, int numberOfThreads) {
        framesPerSecond = 0;
        this.numberOfThreads = numberOfThreads;
        timeStamp = System.currentTimeMillis();
        pixelScale = resolution;
        scale = 80;
        pixels = new double[h/resolution][w/resolution][2];
        renderScale = (int) scale;
        deltaTime = System.currentTimeMillis();
        xPosition = 0;
        yPosition = 0;
        lastReleasedPositionX = 0;
        lastReleasedPositionY = 0;
        threads = new NumberCruncher[numberOfThreads];

        for (int i=0;i<numberOfThreads; i++) {
            threads[i] = new NumberCruncher(this, i);
            threads[i].start();
        }

        this.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {}
            @Override
            public void mousePressed(MouseEvent e) {}
            @Override
            public void mouseEntered(MouseEvent e) {}
            @Override
            public void mouseExited(MouseEvent e) {}

            @Override
            public void mouseReleased(MouseEvent e) {
                lastReleasedPositionX = xPosition;
                lastReleasedPositionY = yPosition;
            }
        });

        this.addMouseMotionListener(new MouseMotionListener() {
            @Override
            public void mouseDragged(MouseEvent e) {
                xDrag = (int) (xPosition+e.getX());
                yDrag = (int) (yPosition+e.getY());
                xPosition = lastReleasedPositionX-(xDrag-xPress)/scale;
                yPosition = lastReleasedPositionY-(yDrag-yPress)/scale;
                //e.getComponent().repaint();
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                xPress = e.getX();
                yPress = e.getY();
            }
        });

        this.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    scale+= (1+scale*.1);
                    renderScale = (int) (255+scale*.005);
                } else {
                    scale-= (1+scale*.1);
                    renderScale = (int) (255+scale*.005);
                }
                //e.getComponent().repaint();

            }
        });
    }


    @Override
    public void paintComponent(Graphics g) {
        if ((timeStamp+1000)<System.currentTimeMillis()) {
            System.out.println("FPS: "+framesPerSecond+" Scale: "+scale+" RenderingScaling: "+renderScale);
            timeStamp = System.currentTimeMillis();
            framesPerSecond = 0;
        }
        super.paintComponent(g);

        double xOffset = xPosition; //higher goes right
        double yOffset = yPosition; //higher goes down
        int spinner = 0;

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                if (spinner>=numberOfThreads)spinner=0;
                threads[spinner++].addPoint(x,y,new ComplexNumber((((x+scale*xOffset) - (pixels.length/2d))) / (scale),
                        (((y+scale*yOffset) - (pixels.length / 2d))) / (scale), x,y));
            }
        }
//        System.out.println("[Debug] - Waiting for all threads to finish");
        while (!NumberCruncher.finishedRendering());
//        System.out.println("[Debug] - Rendered Frame");
        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                if ((int) (pixels[y][x][1]) != (int) renderScale) {
                    int c = (int) ((int) pixels[y][x][0] + 1 - Math.log(Math.log(Math.abs(pixels[y][x][1])) / Math.log(2))) % 255;
                    g.setColor(new Color(c, c, c));
                    g.fillRect((x * pixelScale), (y * pixelScale), pixelScale, pixelScale);
                }
            }
        }
        framesPerSecond +=1;
    }
}

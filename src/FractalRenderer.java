import javax.swing.*;
import java.awt.*;

public class FractalRenderer extends JPanel {
    float framesPerSecond;

    boolean[][] pixels;
    double scale;
    int pixelScale, renderScale;
    long timeStamp;
    long deltaTime;


    public void setPixels(boolean[][] pixels) {
        this.pixels = pixels;
    }

    public FractalRenderer(int w, int h, int resolution) {
        framesPerSecond = 0;
        timeStamp = System.currentTimeMillis();
        pixelScale = resolution;
        scale = 80;
        pixels = new boolean[h][w];
        renderScale = (int) scale;
        deltaTime = System.currentTimeMillis();
    }

    @Override
    public void paintComponent(Graphics g) {
        if ((timeStamp+1000)<System.currentTimeMillis()) {
            System.out.println("FPS: "+framesPerSecond+" Scale: "+scale+" RenderingScaling: "+renderScale);
            timeStamp = System.currentTimeMillis();
            framesPerSecond = 0;
        }
        super.paintComponent(g);

        double xOffset = .4436; //higher goes right
        double yOffset = .3755; //higher goes down
        boolean showGuides = false; // show guidelines

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                //boolean condition = ((((int)(Math.abs(y-(pixels.length/2))*scale)
                //        &(int)(Math.abs(x-(pixels[0].length/2))*scale)))==0);

                double condition = ComplexNumber.mandelbrotSequence(new ComplexNumber((((x+scale*xOffset) - (pixels.length/2d))) / (scale),
                        (((y+scale*yOffset) - (pixels.length / 2d))) / (scale)), renderScale, 2);
                int c = (int) (255%condition);
                if (showGuides && (x==pixels.length / 2 || y==pixels.length / 2)) g.setColor(Color.red);
                else g.setColor(new Color(c, c, c));
                g.fillRect((x*pixelScale), (y*pixelScale), pixelScale, pixelScale);
            }
        }
        scale+= (1+scale*.1);
        renderScale = (int) (255+scale*.005);
        //if (scale>=1000)scale = 80;
        framesPerSecond +=1;
    }

    public boolean pointIsInMandlebrotSet(float x, float y, int iterations, double threshold) {
        return ComplexNumber.mandelbrotSequence(new ComplexNumber(x,y),
                iterations, threshold)<=threshold;
    }


}

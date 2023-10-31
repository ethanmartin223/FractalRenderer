import javax.swing.*;
import java.awt.*;

public class FractalRenderer extends JPanel {
    float framesPerSecond;

    boolean[][] pixels;
    float scale;
    final int scaleFactor;
    long timeStamp;


    public void setPixels(boolean[][] pixels) {
        this.pixels = pixels;
    }

    public FractalRenderer(int w, int h) {
        framesPerSecond = 0;
        timeStamp = System.currentTimeMillis();
        scaleFactor = 1;
        scale = 1;
        pixels = new boolean[h][w];
    }

    @Override
    public void paintComponent(Graphics g) {
        if ((timeStamp+1000)<System.currentTimeMillis()) {
            System.out.println("FPS: "+framesPerSecond);
            timeStamp = System.currentTimeMillis();
            framesPerSecond = 0;
        }

        super.paintComponent(g);

        for (int y = 0; y < pixels.length; y++) {
            for (int x = 0; x < pixels[0].length; x++) {
                //boolean condition = ((((int)(Math.abs(y-(pixels.length/2))*scale)
                //        &(int)(Math.abs(x-(pixels[0].length/2))*scale)))==0);

                boolean condition = pointIsInMandlebrotSet((int)((x-(pixels.length/2))*scale),(int)((y-(pixels.length/2))*scale),
                        28, 2);
                if (condition) {
                    g.setColor(Color.BLACK);
                    g.fillRect((x),(y),scaleFactor,scaleFactor);
                } if ( x-(pixels.length/2)==0 || y-(pixels.length/2)==0)  {
                    g.setColor(Color.RED);
                    g.fillRect((x),(y),scaleFactor,scaleFactor);
                }
            }
        }
        scale-=.01;
        //if (scale>=2)scale = 1;
        framesPerSecond +=1;
    }

   public boolean pointIsInMandlebrotSet(int x, int y, int iterations, double threshold) {
        return ComplexNumber.mandelbrotSequence(new ComplexNumber(x/80f,y/80f),
                iterations, threshold)<=threshold;
    }


}

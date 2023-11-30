import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class NumberCruncher extends Thread{

    private int id;
    private ConcurrentLinkedQueue<ComplexNumber> renderQueue;
    private FractalRenderer parent;
    private static ArrayList<NumberCruncher> instances= new ArrayList<NumberCruncher>();


    @Override
    public void run() {
        ComplexNumber currentData;
        System.out.println("[Debug] - Worker"+id+ " started");
        while (true) {
            currentData = renderQueue.poll();
            if (currentData!=null) {
                double[] condition = ComplexNumber.mandelbrotSequence(currentData, parent.renderScale, 2);
                double c = (condition[0] + (2f - (Math.log(Math.log(Math.abs(condition[1])) / Math.log(2)))));
                double r = ((1-Math.cos((1*(1/Math.log(2))) *c))/2);
                double g = ((1-Math.cos((1/(3*Math.sqrt(2))*(1/Math.log(2))) *c))/2);
                double b = ((1-Math.cos((1/(7*Math.pow(3,1/8d))*(1/Math.log(2))) *c))/2);
                //https://rubenvannieuwpoort.nl/posts/smooth-iteration-count-for-the-mandelbrot-set

                parent.pixels[currentData.y][currentData.x] =
                        new Color((int) (r*255), (int) (g*255),(int) (b*255));
            }
        }
    }

    public synchronized static boolean finishedRendering() {
        for (NumberCruncher nC : instances)
            if (!nC.renderQueue.isEmpty()) return false;

        return true;
    }

    public NumberCruncher(FractalRenderer renderer, int ID) {
        setDaemon(true);
        instances.add(this);
        parent = renderer;
        id = ID;
        renderQueue = new ConcurrentLinkedQueue<>();
    }

    public void addPoint(int x, int y, ComplexNumber dataPoint) {
        dataPoint.x = x; dataPoint.y = y;
        renderQueue.add(dataPoint);
    }
}

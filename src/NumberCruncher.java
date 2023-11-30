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
                float c = (float) (condition[0] + 1 - Math.log(Math.log(Math.abs(condition[1])))/Math.log(2))/parent.renderScale;
//                double r = ((1-Math.cos((1*(1/Math.log(2))) *c))/2);
//                double g = ((1-Math.cos((1/(3*Math.sqrt(2))*(1/Math.log(2))) *c))/2);
//                double b = ((1-Math.cos((1/(7*Math.pow(3,1/8d))*(1/Math.log(2))) *c))/2);

                //https://rubenvannieuwpoort.nl/posts/smooth-iteration-count-for-the-mandelbrot-set

                parent.pixels[currentData.y][currentData.x] = Color.getHSBColor(0.95f + 10 * (c) ,0.6f,1.0f);;
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

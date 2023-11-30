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
                int c = (int) ((int) condition[0] + 1 - Math.log(Math.log(Math.abs(condition[1])) / Math.log(2))) % 255;
                parent.pixels[currentData.y][currentData.x] = new Color(c,c,c);
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

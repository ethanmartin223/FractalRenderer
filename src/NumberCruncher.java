import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class NumberCruncher extends Thread{

    private int id;
    private Queue<ComplexNumber> renderQueue;
    private FractalRenderer parent;
    private static ArrayList<NumberCruncher> instances= new ArrayList<NumberCruncher>();

    public static boolean g = true;

    @Override
    public void run() {
        ComplexNumber currentData;
        System.out.println("[Debug] - Worker"+id+ " started");
        while (g) {
            currentData = renderQueue.poll();
            System.out.print("");
            if (currentData!=null) {
                double[] condition = ComplexNumber.mandelbrotSequence(currentData, parent.renderScale, 2);
                parent.pixels[currentData.y][currentData.x] = condition;
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
        renderQueue = new LinkedList<>();
    }

    public void addPoint(int x, int y, ComplexNumber dataPoint) {
        dataPoint.x = x; dataPoint.y = y;
        renderQueue.add(dataPoint);
    }
}

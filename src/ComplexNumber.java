public class ComplexNumber {

    double realValue, imagValue;

    public ComplexNumber(double real, double imaginary) {
        realValue = real;
        imagValue = imaginary;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj.getClass().equals(ComplexNumber.class)) {
            ComplexNumber n = (ComplexNumber)obj;
            return n.realValue == realValue && n.imagValue == imagValue;
        }
        return false;
    }

    public ComplexNumber add(ComplexNumber c) {
        return new ComplexNumber(c.realValue + realValue, c.imagValue+imagValue);
    }

    public ComplexNumber multiply(ComplexNumber c) {
        double tempImagValue = (imagValue*c.realValue)+(c.imagValue*realValue);
        double tempRealValue = (c.realValue*realValue)+((c.imagValue*imagValue)*-1);
        return new ComplexNumber(tempRealValue, tempImagValue);
    }


    public double getNorm() {
        return Math.sqrt(realValue*realValue + imagValue*imagValue);
    }

    @Override
    public String toString() {
        return realValue+(imagValue<0?"":"+")+imagValue+"i";
    }


//    public static double mandelbrotSequence(ComplexNumber c, int nOfIterations, double threshold) {
//        return mandelbrotSequence(new ComplexNumber(c.realValue, c.imagValue), c, nOfIterations, threshold);
//    }
//
//    public static double mandelbrotSequence(ComplexNumber original, ComplexNumber c, int nOfIterations, double threshold) {
//        if (nOfIterations == 0 || c.getNorm() > threshold) return c.getNorm();
//        return mandelbrotSequence(original, (c.multiply(c)).add(original), nOfIterations - 1, threshold);
//    }

    public static double[] mandelbrotSequence(ComplexNumber c, long nOfIterations, double threshold) {
        ComplexNumber z = new ComplexNumber(0,0);
        int n=0;
        while  (z.getNorm() <= threshold && n< nOfIterations) {
            z = (z.multiply(z).add(c));
            n += 1;
        }
        return new double[]{n, z.getNorm()};
    }

    public static boolean probableThatPointIsInMandelbrotSequence(int x, int y) {
        System.out.println(mandelbrotSequence(new ComplexNumber(x,y), 4, 2)[1]);
        return 2<=mandelbrotSequence(new ComplexNumber(x,y), 2, 2)[1];
    }
}

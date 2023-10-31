public class ComplexNumber {

    double realValue, imagValue;

    public ComplexNumber(double real, double imaginary) {
        realValue = real;
        imagValue = imaginary;
    }

    @Override
    public boolean equals(Object obj) {
        System.out.println("UNTESTED DON'T USE");
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

    public static double _mandelbrotSequence(ComplexNumber c, int nOfIterations, double threshold) {
        if (nOfIterations==0) return c.getNorm();
        ComplexNumber next = (c.multiply(c)).add(c);
        if (c.getNorm()>=threshold) return next.getNorm();
        return mandelbrotSequence(next, nOfIterations-1, threshold);
    }

    public static double mandelbrotSequence(ComplexNumber c, int nOfIterations, double threshold) {
        ComplexNumber z = new ComplexNumber(0,0);
        int n=0;
        while  (z.getNorm() <= threshold && n< nOfIterations) {
            z = (z.multiply(z).add(c));
            n += 1;
        }
        return n;
    }
}

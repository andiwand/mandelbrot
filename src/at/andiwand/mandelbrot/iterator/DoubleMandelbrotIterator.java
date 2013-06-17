package at.andiwand.mandelbrot.iterator;

import java.math.BigDecimal;
import java.math.MathContext;

import at.andiwand.mandelbrot.math.BigComplex;

public class DoubleMandelbrotIterator extends MandelbrotIterator {

    private static final long serialVersionUID = 3461032337137035084L;

    public DoubleMandelbrotIterator(MathContext context, int maxIterationCount,
	    BigDecimal maxNorm2) {
	super(context, maxIterationCount, maxNorm2);
    }

    public DoubleMandelbrotIterator(MathContext context, int maxIterationCount,
	    double maxNorm2) {
	super(context, maxIterationCount, maxNorm2);
    }

    @Override
    public int iteratePoint(BigComplex c) {
	double tmpx;
	double tmpy;
	double cx = c.getReal().doubleValue();
	double cy = c.getImaginary().doubleValue();
	double zx = 0;
	double zy = 0;
	double maxNorm2 = getMaxNorm2().doubleValue();
	double norm2 = 0;
	int maxIterationCount = getMaxIterationCount();
	int iteration = 0;

	while (iteration < maxIterationCount) {
	    tmpx = zx * zx - zy * zy + cx;
	    tmpy = 2 * zx * zy + cy;
	    zx = tmpx;
	    zy = tmpy;
	    norm2 = zx * zx + zy * zy;

	    if (norm2 > maxNorm2)
		return iteration;

	    iteration++;
	}

	return -1;
    }

}
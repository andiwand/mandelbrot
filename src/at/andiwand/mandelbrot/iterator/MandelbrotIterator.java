package at.andiwand.mandelbrot.iterator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

import at.andiwand.mandelbrot.math.BigComplex;

public abstract class MandelbrotIterator implements Serializable {

    private static final long serialVersionUID = -3810408805009419683L;

    private MathContext context;

    private int maxIterationCount;
    private BigDecimal maxNorm2;

    public MandelbrotIterator(MathContext context, int maxIterationCount,
	    BigDecimal maxNorm2) {
	this.context = context;
	this.maxIterationCount = maxIterationCount;
	this.maxNorm2 = maxNorm2;
    }

    public MandelbrotIterator(MathContext context, int maxIterationCount,
	    double maxNorm2) {
	this(context, maxIterationCount, BigDecimal.valueOf(maxNorm2));
    }

    public MathContext getContext() {
	return context;
    }

    public int getMaxIterationCount() {
	return maxIterationCount;
    }

    public BigDecimal getMaxNorm2() {
	return maxNorm2;
    }

    public void setContext(MathContext context) {
	this.context = context;
    }

    public void setMaxIterationCount(int maxIterationCount) {
	this.maxIterationCount = maxIterationCount;
    }

    public void setMaxNorm2(BigDecimal maxNorm) {
	this.maxNorm2 = maxNorm;
    }

    public abstract int iteratePoint(BigComplex c);

}
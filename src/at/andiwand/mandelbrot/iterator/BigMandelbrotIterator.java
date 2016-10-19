package at.andiwand.mandelbrot.iterator;

import java.math.BigDecimal;
import java.math.MathContext;

import at.andiwand.mandelbrot.math.BigComplex;

public class BigMandelbrotIterator extends MandelbrotIterator {

    private static final long serialVersionUID = 6880814442608911370L;

    public BigMandelbrotIterator(MathContext context, int maxIterationCount,
	    BigDecimal maxNorm2) {
	super(context, maxIterationCount, maxNorm2);
    }

    public BigMandelbrotIterator(MathContext context, int maxIterationCount,
	    double maxNorm2) {
	super(context, maxIterationCount, maxNorm2);
    }
    
    @Override
    public BigMandelbrotIterator clone() {
    	return new BigMandelbrotIterator(context, maxIterationCount, maxNorm2);
    }

    @Override
    public void iteratePoint(BigComplex c) {
	MathContext context = getContext();
	int maxIterationCount = getMaxIterationCount();
	BigDecimal maxNorm2 = getMaxNorm2();
	BigComplex z = new BigComplex();
	int i;

	this.convergent = true;
	
	for (i = 0; i < maxIterationCount; i++) {
	    z = z.pow2(context).add(c, context);
	    if (z.pow2(context).norm2(context).compareTo(maxNorm2) > 0) {
	    	break;
	    }
	}

	this.convergent = false;
	this.iterations = i;
	this.z = z;
    }

}
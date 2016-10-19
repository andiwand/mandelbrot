package at.andiwand.mandelbrot.iterator;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;

import at.andiwand.mandelbrot.math.BigComplex;

public abstract class MandelbrotIterator implements Serializable {

	private static final long serialVersionUID = -3810408805009419683L;

	protected MathContext context;

	protected int maxIterationCount;
	protected BigDecimal maxNorm2;

	protected int iterations;
	protected BigComplex z;
	protected boolean convergent;

	public MandelbrotIterator(MathContext context, int maxIterationCount, BigDecimal maxNorm2) {
		this.context = context;
		this.maxIterationCount = maxIterationCount;
		this.maxNorm2 = maxNorm2;
	}

	public MandelbrotIterator(MathContext context, int maxIterationCount, double maxNorm2) {
		this(context, maxIterationCount, BigDecimal.valueOf(maxNorm2));
	}
	
	@Override
	public abstract MandelbrotIterator clone();

	public MathContext getContext() {
		return context;
	}

	public int getMaxIterationCount() {
		return maxIterationCount;
	}

	public BigDecimal getMaxNorm2() {
		return maxNorm2;
	}

	public BigComplex getZ() {
		return z;
	}

	public int getIterations() {
		return iterations;
	}

	public boolean isConvergent() {
		return convergent;
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

	public abstract void iteratePoint(BigComplex c);

}
package at.andiwand.mandelbrot.painter;

import java.awt.Color;

public class SimpleBlueMandelbrotPainter extends MandelbrotPainter {

    private int maxIterationCount;

    public SimpleBlueMandelbrotPainter(int maxIterationCount) {
	this.maxIterationCount = maxIterationCount;
    }

    @Override
    public Color paintPoint(int iterationCount) {
	if (iterationCount >= 0) {
	    float b = (float) iterationCount / maxIterationCount;
	    float rg = (float) Math.sqrt(iterationCount) / maxIterationCount;

	    if (b > 1)
		b = 1;
	    if (rg > 1)
		rg = 1;

	    return new Color(rg, rg, b);
	} else {
	    return Color.BLACK;
	}
    }

}
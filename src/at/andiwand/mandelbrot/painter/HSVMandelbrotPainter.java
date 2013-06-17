package at.andiwand.mandelbrot.painter;

import java.awt.Color;

public class HSVMandelbrotPainter implements MandelbrotPainter {

    private int maxIterationCount;

    public HSVMandelbrotPainter(int maxIterationCount) {
	this.maxIterationCount = maxIterationCount;
    }

    @Override
    public Color paintPoint(int iterationCount) {
	if (iterationCount >= 0) {
	    double h = 0.55 + 10 * ((double) iterationCount / maxIterationCount);
	    double s = 0.6;
	    double v = 1.0; // Math.pow((double) iterationCount /
			    // maxIterationCount, 0.4);

	    return new Color(Color.HSBtoRGB((float) h, (float) s, (float) v));
	} else if (iterationCount == -1) {
	    return Color.BLACK;
	}

	return Color.WHITE;
    }
}
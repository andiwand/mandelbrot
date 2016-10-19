package at.andiwand.mandelbrot.painter;

import java.awt.Color;

public class SimpleRGBMandelbrotPainter extends MandelbrotPainter {

    private int factor;

    public SimpleRGBMandelbrotPainter(int factor) {
	this.factor = factor;
    }

    public int getFactor() {
	return factor;
    }

    public void setFactor(int factor) {
	this.factor = factor;
    }

    @Override
    public Color paintPoint(int iterationCount) {
	if (iterationCount >= 0) {
	    return new Color(iterationCount * factor);
	} else {
	    return Color.BLACK;
	}
    }

}
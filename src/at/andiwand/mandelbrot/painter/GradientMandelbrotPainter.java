package at.andiwand.mandelbrot.painter;

import java.awt.Color;
import java.math.MathContext;

import at.andiwand.mandelbrot.math.BigComplex;
import at.stefl.commons.math.vector.Vector3d;

public class GradientMandelbrotPainter extends MandelbrotPainter {

	private final MathContext context;
    private Vector3d[] table;

    public GradientMandelbrotPainter(MathContext context, Color... colors) {
    	this.context = context;
    	table = new Vector3d[colors.length];

	for (int i = 0; i < colors.length; i++) {
	    double r = colors[i].getRed() / 255d;
	    double g = colors[i].getGreen() / 255d;
	    double b = colors[i].getBlue() / 255d;
	    table[i] = new Vector3d(r, g, b);
	}
    }
    
    public Color interpolate(int iterationCount, double smooth) {
    	Vector3d a = table[iterationCount % table.length];
    	Vector3d b = table[(iterationCount + 1) % table.length];
    	Vector3d c = a.mul(1 - smooth).add(b.mul(smooth));
    	return new Color((float) c.getX(), (float) c.getY(), (float) c.getZ());
    }
    
    @Override
    public Color paintPoint(int iterationCount) {
    	return paintPoint(iterationCount == -1, iterationCount, BigComplex.ZERO);
    }

    @Override
    public Color paintPoint(boolean convergent, int iterationCount, BigComplex z) {
    	if (convergent) return Color.BLACK;
    	
    	double nsmooth = iterationCount + 1 - Math.log(Math.log(z.norm2(context).doubleValue()))/Math.log(2);
    	iterationCount = (int) nsmooth;
    	double smooth = nsmooth - iterationCount;
    	//double smooth = Math.exp(-z.norm2(context).doubleValue());
		return interpolate(iterationCount, smooth);
    }

}
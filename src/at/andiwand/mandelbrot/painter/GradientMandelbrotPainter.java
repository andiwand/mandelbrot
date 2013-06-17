package at.andiwand.mandelbrot.painter;

import java.awt.Color;

import at.andiwand.commons.math.vector.Vector3d;

public class GradientMandelbrotPainter implements MandelbrotPainter {

    private Color[] table;

    public GradientMandelbrotPainter(int partResolution,
	    Color... gradientColors) {
	Vector3d[] gradients = new Vector3d[gradientColors.length];

	for (int i = 0; i < gradientColors.length; i++) {
	    double r = gradientColors[i].getRed() / 255d;
	    double g = gradientColors[i].getGreen() / 255d;
	    double b = gradientColors[i].getBlue() / 255d;
	    gradients[i] = new Vector3d(r, g, b);
	}

	table = new Color[partResolution * gradientColors.length];

	for (int i = 0, j = 0; i < gradients.length; i++) {
	    Vector3d start = gradients[i];
	    Vector3d end = gradients[(i + 1) % gradients.length];
	    Vector3d step = end.sub(start).div(partResolution);

	    for (int k = 0; k < partResolution; k++, j++) {
		Vector3d color = start.add(step.mul(k));
		table[j] = new Color((float) color.getX(),
			(float) color.getY(), (float) color.getZ());
	    }
	}
    }

    @Override
    public Color paintPoint(int iterationCount) {
	if (iterationCount >= 0) {
	    return table[iterationCount % table.length];
	} else if (iterationCount == -1) {
	    return Color.BLACK;
	}

	return Color.WHITE;
    }

}
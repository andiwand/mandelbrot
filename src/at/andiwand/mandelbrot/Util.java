package at.andiwand.mandelbrot;

import java.awt.Color;

import at.stefl.commons.math.vector.Vector3d;

public class Util {
	
	public static Color[] gradient(int length, Color... colors) {
		Vector3d[] gradients = new Vector3d[colors.length];

		for (int i = 0; i < colors.length; i++) {
		    double r = colors[i].getRed() / 255d;
		    double g = colors[i].getGreen() / 255d;
		    double b = colors[i].getBlue() / 255d;
		    gradients[i] = new Vector3d(r, g, b);
		}

		Color[] result = new Color[length];

		for (int i = 0; i < result.length; i++) {
			double p = (double) i / length * gradients.length;
			int j = (int) p;
			p -= j;
		    Vector3d start = gradients[j];
		    Vector3d end = gradients[(j + 1) % gradients.length];
		    Vector3d diff = end.sub(start);

		    Vector3d color = start.add(diff.mul(p));
		    result[i] = new Color((float) color.getX(),
					(float) color.getY(), (float) color.getZ());
		}
		
		return result;
	}

	private Util() {
	}
	
}

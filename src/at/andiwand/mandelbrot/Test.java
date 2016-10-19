package at.andiwand.mandelbrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.math.MathContext;

import javax.imageio.ImageIO;

import at.andiwand.mandelbrot.iterator.DoubleMandelbrotIterator;
import at.andiwand.mandelbrot.iterator.MandelbrotIterator;
import at.andiwand.mandelbrot.math.BigRectangle;
import at.andiwand.mandelbrot.painter.GradientMandelbrotPainter;
import at.andiwand.mandelbrot.painter.MandelbrotPainter;

public class Test {

	public static void main(String[] args) throws Throwable {
		int maxIterationCount = 10000;
		double maxNorm2 = 36;
		MathContext context = new MathContext(100);
		BigRectangle viewport = new BigRectangle(-2, 1, 3, -1);
		Dimension dimension = new Dimension(3000, 1000);
		Color[] gradient = Util.gradient(100, new Color(1, 5, 72), new Color(242, 254, 242), new Color(255,
				170, 1), new Color(21, 3, 63), new Color(1, 5, 92));

		MandelbrotIterator iterator = new DoubleMandelbrotIterator(context, maxIterationCount, maxNorm2);
		MandelbrotPainter painter = new GradientMandelbrotPainter(context, gradient);
		MandelbrotPlotter plotter = new MultithreadedMandelbrotPlotter();

		MandelbrotMap map = new MandelbrotMap(dimension);
		plotter.generate(iterator, viewport, map, map.getSize());
		BufferedImage image = new BufferedImage(map.getWidth(), map.getHeight(), BufferedImage.TYPE_INT_RGB);
		new MandelbrotDrawer(painter).draw(image.getGraphics(), map);

		ImageIO.write(image, "png", new File("/home/andreas/mandelbrot.png"));
	}

}

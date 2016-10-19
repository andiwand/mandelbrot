package at.andiwand.mandelbrot;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.math.MathContext;

import at.andiwand.mandelbrot.iterator.MandelbrotIterator;
import at.andiwand.mandelbrot.math.BigComplex;
import at.andiwand.mandelbrot.math.BigRectangle;

public class SimpleMandelbrotPlotter extends MandelbrotPlotter {

    public void generate(MandelbrotIterator iterator, BigRectangle viewport,
	    MandelbrotMap map, Dimension size) {
	MathContext context = iterator.getContext();

	BigDecimal stepX = viewport.width.divide(
		BigDecimal.valueOf(size.width), context);
	BigDecimal stepY = viewport.height.divide(
		BigDecimal.valueOf(size.height), context);

	for (int x = 0; x < size.width; x++) {
	    BigDecimal bigX = viewport.x.add(stepX.multiply(BigDecimal
		    .valueOf(x)));

	    for (int y = 0; y < size.height; y++) {
		BigDecimal bigY = viewport.y.add(stepY.multiply(BigDecimal
			.valueOf(y)));

		iterator.iteratePoint(new BigComplex(bigX, bigY));
		map.set(x, y, iterator);
	    }
	}
    }

}
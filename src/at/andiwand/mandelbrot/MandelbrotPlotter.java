package at.andiwand.mandelbrot;

import java.awt.Dimension;

import at.andiwand.mandelbrot.iterator.MandelbrotIterator;
import at.andiwand.mandelbrot.math.BigRectangle;

public abstract class MandelbrotPlotter implements MandelbrotGenerator {

    public MandelbrotMap generate(MandelbrotIterator iterator,
	    BigRectangle viewport, Dimension size) {
	MandelbrotMap result = new MandelbrotMap(size);
	generate(iterator, viewport, result, size);
	return result;
    }

    public abstract void generate(MandelbrotIterator iterator,
	    BigRectangle viewport, MandelbrotMap map, Dimension size);

}
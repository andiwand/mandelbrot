package at.andiwand.mandelbrot;

import java.awt.Dimension;

import at.andiwand.mandelbrot.iterator.MandelbrotIterator;
import at.andiwand.mandelbrot.math.BigRectangle;

public interface MandelbrotGenerator {

    public MandelbrotMap generate(MandelbrotIterator iterator,
	    BigRectangle viewport, Dimension size);

}
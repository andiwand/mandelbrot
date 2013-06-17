package at.andiwand.mandelbrot;

import java.awt.Dimension;
import java.io.Serializable;

import at.andiwand.mandelbrot.math.BigRectangle;

public class MandelbrotRectangle implements Serializable {

    private static final long serialVersionUID = -914595914246228272L;

    private final BigRectangle viewport;
    private final Dimension size;

    public MandelbrotRectangle(BigRectangle viewport, Dimension size) {
	this.viewport = viewport;
	this.size = size;
    }

    public BigRectangle getViewport() {
	return viewport;
    }

    public Dimension getSize() {
	return size;
    }

}
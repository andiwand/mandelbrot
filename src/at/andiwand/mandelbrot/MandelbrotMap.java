package at.andiwand.mandelbrot;

import java.awt.Dimension;
import java.io.Serializable;

import at.andiwand.mandelbrot.iterator.MandelbrotIterator;
import at.andiwand.mandelbrot.math.BigComplex;

public class MandelbrotMap implements Serializable {

    private static final long serialVersionUID = -8006850334872479308L;

    public static final int INFINITY = -1;
    public static final int UNDEFINED = -2;

    private final int width;
    private final int height;

    private final int[][] iterations;
    private final BigComplex[][] zs;

    public MandelbrotMap(int width, int height) {
	this.width = width;
	this.height = height;

	iterations = new int[width][height];
	zs = new BigComplex[width][height];

	clear();
    }

    public MandelbrotMap(Dimension size) {
	this(size.width, size.height);
    }

    public Dimension getSize() {
	return new Dimension(width, height);
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }

    public boolean isDefined(int x, int y) {
    	return iterations[x][y] != UNDEFINED;
    }
    
    public boolean getConvergent(int x, int y) {
    	return iterations[x][y] == INFINITY;
    }
    
    public int getIterations(int x, int y) {
	return iterations[x][y];
    }
    
    public BigComplex getZ(int x, int y) {
    	if (zs[x][y] == null) return BigComplex.ZERO;
    	return zs[x][y];
    }
    
    public void set(int x, int y, MandelbrotIterator iterator) {
    	if (iterator.isConvergent()) this.iterations[x][y] = INFINITY;
    	else this.iterations[x][y] = iterator.getIterations();
    	this.zs[x][y] = iterator.getZ();
    }
    
    public void setConvergent(int x, int y, boolean convergent) {
    	if (convergent) this.iterations[x][y] = INFINITY;
    }

    public void setIterations(int x, int y, int iterations) {
	if (iterations < -2)
	    throw new IllegalArgumentException();
	this.iterations[x][y] = iterations;
    }
    
    public void setZ(int x, int y, BigComplex z) {
    	this.zs[x][y] = z;
    }

    public void clear() {
	for (int x = 0; x < width; x++) {
	    for (int y = 0; y < height; y++) {
		iterations[x][y] = UNDEFINED;
		zs[x][y] = null;
	    }
	}
    }

}
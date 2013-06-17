package at.andiwand.mandelbrot;

import java.awt.Dimension;
import java.io.Serializable;

public class MandelbrotMap implements Serializable {

    private static final long serialVersionUID = -8006850334872479308L;

    public static final int INFINITY = -1;
    public static final int UNDEFINDED = -2;

    private final int width;
    private final int height;

    private final int[][] map;

    public MandelbrotMap(int width, int height) {
	this.width = width;
	this.height = height;

	map = new int[width][height];

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

    public int getIterations(int x, int y) {
	return map[x][y];
    }

    public void setIterations(int x, int y, int iterations) {
	if (iterations < -2)
	    throw new IllegalArgumentException();
	map[x][y] = iterations;
    }

    public void clear() {
	for (int x = 0; x < width; x++)
	    for (int y = 0; y < height; y++)
		map[x][y] = UNDEFINDED;
    }

}
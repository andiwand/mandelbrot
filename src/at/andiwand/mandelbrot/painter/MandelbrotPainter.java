package at.andiwand.mandelbrot.painter;

import java.awt.Color;

public interface MandelbrotPainter {

    public Color paintPoint(int iterationCount);

}
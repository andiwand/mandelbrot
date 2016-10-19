package at.andiwand.mandelbrot.painter;

import java.awt.Color;

import at.andiwand.mandelbrot.math.BigComplex;

public abstract class MandelbrotPainter {

    public abstract Color paintPoint(int iterationCount);
    
    public Color paintPoint(boolean convergent, int iterationCount, BigComplex z) {
    	return paintPoint(iterationCount);
    }

}
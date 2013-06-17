package at.andiwand.mandelbrot;

import java.awt.Color;
import java.awt.Graphics;

import at.andiwand.mandelbrot.painter.MandelbrotPainter;

public class MandelbrotDrawer {

    private MandelbrotPainter plotter;

    public MandelbrotDrawer(MandelbrotPainter plotter) {
	this.plotter = plotter;
    }

    public void draw(Graphics g, MandelbrotMap map) {
	for (int x = 0; x < map.getWidth(); x++) {
	    for (int y = 0; y < map.getHeight(); y++) {
		Color color = plotter.paintPoint(map.getIterations(x, y));
		g.setColor(color);
		g.drawLine(x, y, x, y);
	    }
	}
    }

}
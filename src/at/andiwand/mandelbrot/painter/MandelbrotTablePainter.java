package at.andiwand.mandelbrot.painter;

import java.awt.Color;
import java.util.Arrays;
import java.util.List;

public class MandelbrotTablePainter implements MandelbrotPainter {

    private final Color[] table;

    public MandelbrotTablePainter(Color... table) {
	this.table = Arrays.copyOf(table, table.length);
    }

    public MandelbrotTablePainter(List<Color> table) {
	this.table = new Color[table.size()];
	table.toArray(this.table);
    }

    @Override
    public Color paintPoint(int iterationCount) {
	if (iterationCount >= 0) {
	    return table[iterationCount % table.length];
	} else if (iterationCount == -1) {
	    return Color.BLACK;
	}

	return Color.WHITE;
    }

}
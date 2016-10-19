package at.andiwand.mandelbrot.math;

import java.math.BigDecimal;

public class BigRectangle {

    public BigDecimal x;
    public BigDecimal y;
    public BigDecimal width;
    public BigDecimal height;

    public BigRectangle(BigDecimal x, BigDecimal y, BigDecimal width,
	    BigDecimal height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }

	public BigRectangle(double x, double y, double width, double height) {
	this.x = new BigDecimal(x);
	this.y = new BigDecimal(y);
	this.width = new BigDecimal(width);
	this.height = new BigDecimal(height);
    }
	
	@Override
	public String toString() {
	return "[x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}

}
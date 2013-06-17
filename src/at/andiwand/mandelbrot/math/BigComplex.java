package at.andiwand.mandelbrot.math;

import java.math.BigDecimal;
import java.math.MathContext;

public class BigComplex {

    private static final BigDecimal TWO = BigDecimal.valueOf(2);

    private BigDecimal real;
    private BigDecimal imaginary;

    public BigComplex() {
	this(BigDecimal.ZERO, BigDecimal.ZERO);
    }

    public BigComplex(BigDecimal real, BigDecimal imaginary) {
	this.real = real;
	this.imaginary = imaginary;
    }

    public BigComplex(double real, double imaginary) {
	this(new BigDecimal(real), new BigDecimal(imaginary));
    }

    public BigComplex(String real, String imaginary) {
	this(new BigDecimal(real), new BigDecimal(imaginary));
    }

    @Override
    public String toString() {
	return real.toEngineeringString() + " + ("
		+ imaginary.toEngineeringString() + ")i";
    }

    public BigDecimal getReal() {
	return real;
    }

    public BigDecimal getImaginary() {
	return imaginary;
    }

    public BigComplex add(BigComplex b, MathContext context) {
	BigComplex result = new BigComplex();

	result.real = real.add(b.real, context);
	result.imaginary = imaginary.add(b.imaginary, context);

	return result;
    }

    public BigComplex pow2(MathContext context) {
	BigComplex result = new BigComplex();

	result.real = real.pow(2, context).subtract(imaginary.pow(2, context),
		context);
	result.imaginary = TWO.multiply(real.multiply(imaginary, context));

	return result;
    }

    public BigDecimal norm2(MathContext context) {
	return real.pow(2, context).add(imaginary.pow(2, context));
    }

}
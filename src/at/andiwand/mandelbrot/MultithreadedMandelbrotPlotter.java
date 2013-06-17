package at.andiwand.mandelbrot;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import at.andiwand.mandelbrot.iterator.MandelbrotIterator;
import at.andiwand.mandelbrot.math.BigComplex;
import at.andiwand.mandelbrot.math.BigRectangle;

public class MultithreadedMandelbrotPlotter extends MandelbrotPlotter {

    private static final int THREAD_PART_SIZE = 64;

    private class PartGenerator implements Runnable {
	private final MandelbrotIterator iterator;
	private final MandelbrotMap map;
	private final int offsetX;
	private final int offsetY;
	private final BigDecimal stepX;
	private final BigDecimal stepY;
	private final BigRectangle viewport;
	private final Dimension size;

	public PartGenerator(MandelbrotIterator iterator, MandelbrotMap map,
		int offsetX, int offsetY, BigDecimal stepX, BigDecimal stepY,
		BigRectangle viewport, Dimension size) {
	    this.iterator = iterator;
	    this.map = map;
	    this.offsetX = offsetX;
	    this.offsetY = offsetY;
	    this.stepX = stepX;
	    this.stepY = stepY;
	    this.viewport = viewport;
	    this.size = size;
	}

	@Override
	public void run() {
	    for (int x = 0; x < size.width; x++) {
		if ((offsetX + x) >= map.getWidth())
		    break;
		BigDecimal bigX = viewport.x.add(stepX.multiply(BigDecimal
			.valueOf(x)));

		for (int y = 0; y < size.height; y++) {
		    if ((offsetY + y) >= map.getHeight())
			break;
		    BigDecimal bigY = viewport.y.add(stepY.multiply(BigDecimal
			    .valueOf(y)));

		    int iterations = iterator.iteratePoint(new BigComplex(bigX,
			    bigY));
		    map.setIterations(offsetX + x, offsetY + y, iterations);
		}
	    }
	}
    }

    private int threadCount;
    private ExecutorService service;

    public MultithreadedMandelbrotPlotter(int threadCount) {
	this.threadCount = threadCount;
    }

    public MultithreadedMandelbrotPlotter() {
	this(Runtime.getRuntime().availableProcessors());
    }

    public int getThreadCount() {
	return threadCount;
    }

    public void setThreadCount(int threadCount) {
	this.threadCount = threadCount;
    }

    @Override
    public void generate(MandelbrotIterator iterator, BigRectangle viewport,
	    MandelbrotMap map, Dimension size) {
	if ((service != null) && !service.isShutdown())
	    service.shutdownNow();
	service = Executors.newFixedThreadPool(threadCount);

	MathContext context = iterator.getContext();

	BigDecimal stepX = viewport.width.divide(
		BigDecimal.valueOf(size.width), context);
	BigDecimal stepY = viewport.height.divide(
		BigDecimal.valueOf(size.height), context);

	BigDecimal partViewportSize = stepX.multiply(BigDecimal
		.valueOf(THREAD_PART_SIZE));
	Dimension partSize = new Dimension(THREAD_PART_SIZE, THREAD_PART_SIZE);

	for (int x = 0; x < size.width; x += THREAD_PART_SIZE) {
	    BigDecimal bigX = viewport.x.add(stepX.multiply(BigDecimal
		    .valueOf(x)));

	    for (int y = 0; y < size.height; y += THREAD_PART_SIZE) {
		BigDecimal bigY = viewport.y.add(stepY.multiply(BigDecimal
			.valueOf(y)));

		BigRectangle partRectangle = new BigRectangle(bigX, bigY,
			partViewportSize, partViewportSize);
		PartGenerator drawer = new PartGenerator(iterator, map, x, y,
			stepX, stepY, partRectangle, partSize);
		service.execute(drawer);
	    }
	}

	try {
	    service.shutdown();
	    service.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
	} catch (InterruptedException e) {
	}
    }

}
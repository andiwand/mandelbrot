package at.andiwand.mandelbrot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.LinkedList;

import javax.swing.JComponent;
import javax.swing.JFrame;

import at.andiwand.mandelbrot.iterator.DoubleMandelbrotIterator;
import at.andiwand.mandelbrot.iterator.MandelbrotIterator;
import at.andiwand.mandelbrot.math.BigRectangle;
import at.andiwand.mandelbrot.painter.GradientMandelbrotPainter;
import at.andiwand.mandelbrot.painter.MandelbrotPainter;

public class MandelbrotView extends JComponent {

    private static final long serialVersionUID = -3353950082567503576L;

    private static Rectangle normalize(Rectangle rectangle) {
	Rectangle result = new Rectangle(rectangle);

	if (result.width < 0) {
	    result.x += result.width;
	    result.width = -result.width;
	}

	if (result.height < 0) {
	    result.y += result.height;
	    result.height = -result.height;
	}

	return result;
    }

    private class ResizeHandler extends ComponentAdapter {
	public void componentResized(ComponentEvent e) {
	    recalc();
	}
    }

    private class MouseHandler extends MouseAdapter {
	public void mousePressed(MouseEvent e) {
	    if (e.getButton() != MouseEvent.BUTTON1)
		return;

	    selection = new Rectangle(e.getPoint(), new Dimension());
	}

	public void mouseDragged(MouseEvent e) {
	    if (selection == null)
		return;

	    selection.width = e.getPoint().x - selection.x;
	    selection.height = e.getPoint().y - selection.y;

	    double ratio = (double) getHeight() / getWidth();
	    selection.height = (int) (Math.abs(selection.width) * ratio)
		    * Integer.signum(selection.height);

	    repaint();
	}

	public void mouseReleased(MouseEvent e) {
	    switch (e.getButton()) {
	    case MouseEvent.BUTTON1:
		BigDecimal stepX = viewport.width.divide(
			BigDecimal.valueOf(getWidth()), new MathContext(10));
		BigDecimal stepY = viewport.height.divide(
			BigDecimal.valueOf(getHeight()), new MathContext(10));

		Rectangle rectangle = normalize(selection);
		BigDecimal x = BigDecimal.valueOf(rectangle.getX())
			.multiply(stepX).add(viewport.x);
		BigDecimal y = BigDecimal.valueOf(rectangle.getY())
			.multiply(stepY).add(viewport.y);
		BigDecimal width = BigDecimal.valueOf(rectangle.getWidth())
			.multiply(stepX);
		BigDecimal height = BigDecimal.valueOf(rectangle.getHeight())
			.multiply(stepY);
		lastViewports.add(viewport);
		viewport = new BigRectangle(x, y, width, height);
		recalc();
	    case MouseEvent.BUTTON3:
		selection = null;
		repaint();
		break;
	    }
	}

	public void mouseClicked(MouseEvent e) {
	    if (e.getButton() != MouseEvent.BUTTON3)
		return;

	    if (lastViewports.isEmpty())
		return;
	    viewport = lastViewports.removeLast();

	    recalc();
	}
    }

    private final MandelbrotGenerator generator;
    private final MandelbrotIterator iterator;
    private final MandelbrotDrawer drawer;
    private LinkedList<BigRectangle> lastViewports = new LinkedList<BigRectangle>();
    private BigRectangle viewport;
    private MandelbrotMap map;
    private Image image;

    private Rectangle selection;

    public MandelbrotView(MandelbrotGenerator generator,
	    MandelbrotIterator iterator, MandelbrotPainter painter,
	    BigRectangle viewport) {
	this.generator = generator;
	this.iterator = iterator;
	this.drawer = new MandelbrotDrawer(painter);
	this.viewport = viewport;

	addComponentListener(new ResizeHandler());

	MouseHandler mouseHandler = new MouseHandler();
	addMouseListener(mouseHandler);
	addMouseMotionListener(mouseHandler);
	addMouseWheelListener(mouseHandler);
    }

    private void recalc() {
	new Thread() {
	    public void run() {
		image = null;

		if (generator instanceof MandelbrotPlotter) {
		    MandelbrotPlotter plotter = (MandelbrotPlotter) generator;

		    map = new MandelbrotMap(getSize());
		    plotter.generate(iterator, viewport, map, map.getSize());
		} else {
		    map = generator.generate(iterator, viewport, getSize());
		}

		image = new BufferedImage(map.getWidth(), map.getHeight(),
			BufferedImage.TYPE_INT_RGB);
		drawer.draw(image.getGraphics(), map);

		repaint();
	    }
	}.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
	if (map != null) {
	    if (image != null)
		g.drawImage(image, 0, 0, this);
	    else
		drawer.draw(g, map);
	}

	if (selection != null) {
	    g.setXORMode(Color.BLACK);
	    Rectangle selection = normalize(this.selection);
	    g.setColor(Color.WHITE);
	    g.drawRect(selection.x, selection.y, selection.width,
		    selection.height);

	    return;
	}

	if (image != null)
	    return;

	try {
	    Thread.sleep(100);
	    repaint();
	} catch (InterruptedException e) {
	}
    }

    public static void main(String[] args) throws Throwable {
	int maxIterationCount = 10000;
	double maxNorm2 = 36;
	MathContext context = new MathContext(100);

	// MandelbrotIterator iterator = new BigMandelbrotIterator(context,
	// maxIterationCount, maxNorm2);
	MandelbrotIterator iterator = new DoubleMandelbrotIterator(context,
		maxIterationCount, maxNorm2);
	// MandelbrotPlotter plotter = new SimpleBlueMandelbrotPlotter(
	// maxIterationCount);
	// MandelbrotPainter painter = new
	// HSVMandelbrotPainter(maxIterationCount);
	MandelbrotPainter painter = new GradientMandelbrotPainter(100,
		new Color(1, 5, 72), new Color(242, 254, 242), new Color(255,
			170, 1), new Color(21, 3, 63), new Color(1, 5, 92));
	// MandelbrotPainter painter = new SimpleBlueMandelbrotPainter(
	// maxIterationCount);
	MandelbrotGenerator generator = new MultithreadedMandelbrotPlotter();
	// MandelbrotGenerator generator = new
	// SimpleMandelbrotGenerator(iterator, context);
	BigRectangle rectangle = new BigRectangle(-2, 1, 3, -2);

	JFrame frame = new JFrame();
	frame.add(new MandelbrotView(generator, iterator, painter, rectangle));
	frame.setSize(600, 400);
	frame.setLocationRelativeTo(null);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setVisible(true);
    }

}
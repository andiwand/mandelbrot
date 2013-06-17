package at.andiwand.mandelbrot.opengl;

import java.awt.Dimension;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.security.CodeSource;
import java.text.DecimalFormat;

import org.lwjgl.BufferUtils;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.ARBTextureFloat;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;

import at.andiwand.mandelbrot.math.RectangleF;

public class GLMandelbrot extends GLBase {

    private static final String TITLE = "GL Mandelbrot";
    private static final DecimalFormat DECIMAL_FORMAT = new DecimalFormat(
	    "#.##");

    static {
	String platformName = LWJGLUtil.getPlatformName();
	String librarySubPath = File.separator + "lwjgl" + File.separator
		+ "native" + File.separator + platformName;

	CodeSource classCodeSource = GLMandelbrot.class.getProtectionDomain()
		.getCodeSource();
	URL classLocation = classCodeSource.getLocation();
	File classLocationFile = new File(classLocation.getPath());
	String libraryPath;

	if (classLocationFile.isDirectory()) {
	    libraryPath = classLocationFile + librarySubPath;
	} else {
	    libraryPath = classLocationFile.getParent() + librarySubPath;
	}

	System.setProperty("org.lwjgl.librarypath", libraryPath);
    }

    private static ByteBuffer readShaderSource(InputStream inputStream)
	    throws IOException {
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

	while (true) {
	    int b = inputStream.read();

	    if (b < 0)
		break;

	    outputStream.write(b);
	}

	byte[] array = outputStream.toByteArray();
	ByteBuffer result = ByteBuffer.allocateDirect(array.length);
	result.put(array);
	result.rewind();
	return result;
    }

    private static void validateShader(int shader) {
	int statusInt = GL20.glGetShader(shader, GL20.GL_COMPILE_STATUS);
	boolean status = statusInt != 0;

	if (!status) {
	    int length = GL20.glGetShader(shader, GL20.GL_INFO_LOG_LENGTH);

	    String infoLog = GL20.glGetShaderInfoLog(shader, length);

	    System.out.println(infoLog.trim());
	    System.exit(1);
	}
    }

    private static void validateProgram(int program) {
	int statusInt = GL20.glGetProgram(program, GL20.GL_LINK_STATUS);
	boolean status = statusInt != 0;

	if (!status) {
	    int length = GL20.glGetProgram(program, GL20.GL_INFO_LOG_LENGTH);

	    String infoLog = GL20.glGetProgramInfoLog(program, length);

	    System.out.println(infoLog.trim());
	    System.exit(1);
	}
    }

    private static void programUniform1i(int program, String name, int value) {
	int location = GL20.glGetUniformLocation(program, name);
	GL20.glUniform1i(location, value);
    }

    private static void programUniform1f(int program, String name, float value) {
	int location = GL20.glGetUniformLocation(program, name);
	GL20.glUniform1f(location, value);
    }

    private static void programUniform2f(int program, String name,
	    float value1, float value2) {
	int location = GL20.glGetUniformLocation(program, name);
	GL20.glUniform2f(location, value1, value2);
    }

    private DisplayMode displayMode;
    private boolean fullscreen;
    private boolean vSync;
    private boolean useFbo;

    private float ratio;

    private String title;
    private boolean titleChanged;

    private int renderVbo;

    private Dimension viewportDimension;
    private Dimension renderViewportDimension;

    private int framebuffer;
    private int framebufferTexture;

    private int renderProgram;
    private int renderFragmentShader;
    private int renderVertexShader;

    private RectangleF viewRect;
    private int iterationNumber;
    private float maxNorm;

    public GLMandelbrot(DisplayMode displayMode, boolean fullscreen,
	    boolean vSync, int fboFactor) {
	this.displayMode = displayMode;
	this.fullscreen = fullscreen;
	this.vSync = vSync;

	viewportDimension = new Dimension(displayMode.getWidth(),
		displayMode.getHeight());
	renderViewportDimension = new Dimension(viewportDimension);

	if (fboFactor > 1) {
	    useFbo = true;
	    renderViewportDimension.width *= fboFactor;
	    renderViewportDimension.height *= fboFactor;
	}

	ratio = (float) displayMode.getWidth() / displayMode.getHeight();

	viewRect = new RectangleF(-2, 1.5f, 3, -3);
	iterationNumber = 500;
	maxNorm = 2;

	setTitle(TITLE);

	super.start();
    }

    public void setTitle(String title) {
	this.title = title;
	titleChanged = true;
    }

    private void initDisplay() throws LWJGLException {
	Display.setDisplayMode(displayMode);
	Display.setFullscreen(fullscreen);
	Display.setIcon(null);
	Display.setVSyncEnabled(vSync);
	Display.setTitle(title);
	Display.create();
    }

    private void initMouse() throws LWJGLException {
	Mouse.create();
    }

    private void initViewport() {
	GL11.glViewport(0, 0, displayMode.getWidth(), displayMode.getHeight());
    }

    private void initProjection() {
	GL11.glDisable(GL11.GL_DEPTH_TEST);

	GL11.glMatrixMode(GL11.GL_PROJECTION);
	GL11.glLoadIdentity();
	GL11.glOrtho(0, ratio, 1, 0, -1, 1);
    }

    private void initVbo() {
	renderVbo = GL15.glGenBuffers();

	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderVbo);

	FloatBuffer data = BufferUtils.createFloatBuffer(20);

	// left top
	data.put(1);
	data.put(1);
	data.put(0);
	data.put(0);
	data.put(0);
	// left bottom
	data.put(1);
	data.put(0);
	data.put(0);
	data.put(1);
	data.put(0);
	// right bottom
	data.put(0);
	data.put(0);
	data.put(ratio);
	data.put(1);
	data.put(0);
	// right top
	data.put(0);
	data.put(1);
	data.put(ratio);
	data.put(0);
	data.put(0);

	data.rewind();

	GL15.glBufferData(GL15.GL_ARRAY_BUFFER, data, GL15.GL_STATIC_DRAW);

	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void initFbo() {
	GL11.glEnable(GL11.GL_TEXTURE_2D);

	framebuffer = ARBFramebufferObject.glGenFramebuffers();
	ARBFramebufferObject.glBindFramebuffer(
		ARBFramebufferObject.GL_FRAMEBUFFER, framebuffer);

	framebufferTexture = GL11.glGenTextures();
	GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebufferTexture);

	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER,
		GL11.GL_LINEAR);
	GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER,
		GL11.GL_LINEAR);

	GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, ARBTextureFloat.GL_RGB32F_ARB,
		renderViewportDimension.width, renderViewportDimension.height,
		0, GL11.GL_RGBA, GL11.GL_FLOAT, (FloatBuffer) null);
	ARBFramebufferObject.glFramebufferTexture2D(
		ARBFramebufferObject.GL_FRAMEBUFFER,
		ARBFramebufferObject.GL_COLOR_ATTACHMENT0, GL11.GL_TEXTURE_2D,
		framebufferTexture, 0);

	ARBFramebufferObject.glBindFramebuffer(
		ARBFramebufferObject.GL_FRAMEBUFFER, 0);
    }

    private void initRenderProgram() throws IOException {
	renderProgram = GL20.glCreateProgram();

	renderFragmentShader = GL20.glCreateShader(GL20.GL_FRAGMENT_SHADER);
	renderVertexShader = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);

	GL20.glShaderSource(renderFragmentShader,
		readShaderSource(GLMandelbrot.class
			.getResourceAsStream("render_fragmentshader.txt")));
	GL20.glShaderSource(renderVertexShader,
		readShaderSource(GLMandelbrot.class
			.getResourceAsStream("render_vertexshader.txt")));

	GL20.glCompileShader(renderFragmentShader);
	GL20.glCompileShader(renderVertexShader);

	validateShader(renderFragmentShader);
	validateShader(renderVertexShader);

	GL20.glAttachShader(renderProgram, renderFragmentShader);
	GL20.glAttachShader(renderProgram, renderVertexShader);

	GL20.glLinkProgram(renderProgram);

	validateProgram(renderProgram);
    }

    @Override
    protected void initImpl() {
	try {
	    initDisplay();
	    initMouse();
	} catch (LWJGLException e) {
	    e.printStackTrace();
	    System.exit(1);
	}

	initViewport();
	initProjection();
	initVbo();
	if (useFbo)
	    initFbo();

	try {
	    initRenderProgram();
	} catch (IOException e) {
	    e.printStackTrace();
	    System.exit(2);
	}
    }

    private void render() {
	GL20.glUseProgram(renderProgram);

	if (Mouse.isButtonDown(0) && Mouse.isInsideWindow()) {
	    float mDX = -Mouse.getDX() * ratio / viewportDimension.width;
	    float mDY = (float) -Mouse.getDY() / viewportDimension.height;

	    float realDX = mDX * viewRect.width;
	    float realDY = mDY * viewRect.height;

	    viewRect.x += realDX;
	    viewRect.y += realDY;
	}

	while (Mouse.next()) {
	    if ((Mouse.getEventButton() == 1) && Mouse.getEventButtonState()) {
		float mX = Mouse.getX() * ratio / viewportDimension.width;
		float mY = (float) Mouse.getY() / viewportDimension.height;

		float realX = viewRect.x + mX * viewRect.width;
		float realY = viewRect.y + mY * viewRect.height;

		float sizeFactor = 0.5f;

		viewRect.width *= sizeFactor;
		viewRect.height *= sizeFactor;
		viewRect.x = realX - viewRect.width / 2;
		viewRect.y = realY - viewRect.height / 2;

		System.out.println(viewRect.x + ", " + viewRect.y + ", "
			+ viewRect.width + ", " + viewRect.height);
	    }
	}

	int dWheel = Mouse.getDWheel();
	if (dWheel != 0) {
	    dWheel /= 120;

	    // float mX = (float) Mouse.getX() * ratio /
	    // viewportDimension.width;
	    // float mY = (float) Mouse.getY() / viewportDimension.height;

	    float realX = viewRect.x + 0.5f * viewRect.width;
	    float realY = viewRect.y + 0.5f * viewRect.height;

	    float sizeFactor = 0.9f;

	    if (dWheel < 0)
		sizeFactor = 2 - sizeFactor;

	    int dWheelAbs = Math.abs(dWheel);

	    sizeFactor = (float) Math.pow(sizeFactor, dWheelAbs);

	    viewRect.width *= sizeFactor;
	    viewRect.height *= sizeFactor;
	    viewRect.x = realX - viewRect.width / 2;
	    viewRect.y = realY - viewRect.height / 2;
	}

	programUniform2f(renderProgram, "start", viewRect.x, viewRect.y);
	float stepSizeX = viewRect.width / renderViewportDimension.width;
	float stepSizeY = viewRect.height / renderViewportDimension.width;
	programUniform2f(renderProgram, "stepSize", stepSizeX, stepSizeY);

	iterationNumber = 100 + (int) (10 / viewRect.width);
	iterationNumber = (int) (Math.abs(Math.log(viewRect.width)) * 100);
	iterationNumber = 100;
	programUniform1i(renderProgram, "iterationNumber", iterationNumber);
	programUniform1f(renderProgram, "maxNorm", maxNorm);

	if (useFbo) {
	    ARBFramebufferObject.glBindFramebuffer(
		    ARBFramebufferObject.GL_FRAMEBUFFER, framebuffer);
	    GL11.glPushAttrib(GL11.GL_VIEWPORT_BIT);
	    GL11.glViewport(0, 0, renderViewportDimension.width,
		    renderViewportDimension.height);
	}

	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderVbo);

	GL11.glInterleavedArrays(GL11.GL_T2F_V3F, 0, 0);
	GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);

	GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);

	GL20.glUseProgram(0);

	if (useFbo) {
	    GL11.glPopAttrib();
	    ARBFramebufferObject.glBindFramebuffer(
		    ARBFramebufferObject.GL_FRAMEBUFFER, 0);

	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, framebufferTexture);
	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, renderVbo);

	    GL11.glInterleavedArrays(GL11.GL_T2F_V3F, 0, 0);
	    GL11.glDrawArrays(GL11.GL_QUADS, 0, 4);

	    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
	}
    }

    @Override
    protected void drawImpl() {
	render();

	Display.update();
	setRunning(!Display.isCloseRequested());
	if (titleChanged)
	    Display.setTitle(title);
    }

    private void destroyMouse() {
	Mouse.destroy();
    }

    @Override
    protected void destroyImpl() {
	destroyMouse();
    }

    @Override
    protected void fpsCallback(double fps) {
	setTitle(TITLE + " @" + DECIMAL_FORMAT.format(fps) + "fps");
    }

    public static void main(String[] args) {
	new GLMandelbrot(new DisplayMode(800, 800), false, true, 1);
    }

}
package at.andiwand.mandelbrot.rmi;

import java.io.Serializable;
import java.rmi.Remote;


public interface MandelbrotWorker extends Remote, Serializable {
	
	public static final String NAME = "MandelbrotWorker";
	
	public void calculate(Object id);
	
}
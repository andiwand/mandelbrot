package at.andiwand.mandelbrot.rmi;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;


public class MandelbrotWorkerImpl extends UnicastRemoteObject implements
		MandelbrotWorker {
	
	private static final long serialVersionUID = 5378721496442468816L;
	
	public MandelbrotWorkerImpl() throws IOException {}
	
	public static void main(String[] args) throws Throwable {
		LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
		Naming.rebind(NAME, new MandelbrotWorkerImpl());
	}
	
}
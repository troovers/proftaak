/*
 */
package edu.avans.ivh5.server.model.main;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.avans.ivh5.api.RemoteMemberAdminClientIF;
import edu.avans.ivh5.shared.util.Settings;

/**
 * Creates the stub, which will be remote accessible by the client, and
 * registers it at the rmiregistry by a servicename. Clients can retrieve the
 * remote stub by this servicename.
 * 
 * @author Robin Schellius
 */
public class LibraryServer {

	// Implements the specific DAO functionality (MySQL, XML).
	// static public String daofactoryclassname;
	// Access to remote manager
	static private RemoteMemberAdminClientIF	stub; 

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryServer.class);

	/**
	 * Empty constructor
	 * 
	 * @throws RemoteException
	 */
	public LibraryServer() throws RemoteException {
		logger.debug("Constructor");
	}

	/**
	 * Initialize the server, register it in the RMI registry, and
	 * (automatically) start listening for incoming client calls.
	 * 
	 * @param args
	 *            Command line arguments indicating the servicename for this
	 *            server.
	 */
	public static void main(String args[]) {

		// Get the properties file name from the command line, and load the
		// properties.
		if (args.length == 2) {
			String propertiesfile = parseCommandLine(args);
			Settings.loadProperties(propertiesfile);
		} else {
			System.out.println("No properties file was found. Provide a properties file name.");
			System.out.println("Program is exiting.");
			return;
		}
		
		/**
		 *  Install the security manager. The SecurityManager looks for the system property
		 *  java.security.policy, which points to the se
		 */
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// Configure logging using the given log config file.
		PropertyConfigurator.configure(Settings.props.getProperty(Settings.propLogConfigFile));
		logger.info("Starting application");

		try {
			String service = Settings.props.getProperty(Settings.propRmiServiceGroup) + 
					Settings.props.getProperty(Settings.propRmiServiceName);
			String hostname = Settings.props.getProperty(Settings.propRmiHostName);
			
			// ShutdownHook handles cleaning up the registry when this
			// application exits.
			ShutdownHook shutdownHook = new ShutdownHook(service);
			Runtime.getRuntime().addShutdownHook(shutdownHook);

			logger.debug("Creating stub");
			MemberAdminManagerImpl obj = new MemberAdminManagerImpl(service);
			stub = (RemoteMemberAdminClientIF) UnicastRemoteObject.exportObject(obj, 0);

			logger.debug("Locating registry on '" + hostname + "'");
			Registry registry = LocateRegistry.getRegistry(hostname);
			logger.debug("Registering stub using name \"" + service + "\"");
			registry.rebind(service, stub);

			logger.info("Server ready");
		} catch (java.rmi.ConnectException e) {
			logger.fatal("Could not connect: " + e.getMessage());
			logger.fatal("(is rmiregistry running?)");
		} catch (java.security.AccessControlException e) {
			logger.fatal("No access: " + e.getMessage());
			logger.fatal("(is the HTTP webserver running?)");
		} catch (Exception e) {
			logger.fatal(e.toString());
		}
	}

	/**
	 * When the server is stopped, by Ctrl-C or closing the window that it is
	 * running in, we want to unregister ourselves from the registry and
	 * decouple the stub, so that clients cannot find the remote stub without a
	 * running server.
	 * 
	 * @throws RemoteException
	 */
	public static void exit(String service) throws RemoteException {
		logger.info("Server is exiting, cleaning up registry.");
		try {
			logger.debug("Unbind servicename " + service);
			Naming.unbind(service);
		} catch (java.net.MalformedURLException e) {
			logger.error("Servicename not found in registry.");
		} catch (java.rmi.NoSuchObjectException e) {
			logger.error("Server not found in registry.");
		} catch (Exception e) {
			logger.error("Could not contact registry.");
		} finally {
			logger.info("Bye.");
		}
	}

	/**
	 * Read the command line and parse the name of the properties file. The properties file
	 * contains all required properties for running this application.
	 * <p>
	 * Options: -properties [filename]
	 * </p>
	 * 
	 * @param args
	 *            The string of options given to this application via the
	 *            command line.
	 */
	private static String parseCommandLine(String[] args) {
		boolean errorFound = false;
		String propertiesfilename = null;

		if (args.length != 2) {
			System.out.println("Error reading options; expected 2 but found "
					+ args.length + ".");
			errorFound = true;
		} else {
			if (args[0].equalsIgnoreCase("-properties")) {
				propertiesfilename = args[1];
			} else
				errorFound = true;
		}
		if (errorFound) {
			System.out.println("Use: -properties [filename or URL]");
			System.out.println("     -properties \"http://localhost/path/to/file.props\"");
			System.out.println("     -properties \"file:/C:/path/to/server.cnf\"");
		}
		return propertiesfilename;
	}
}

/**
 * ShutdownHook is a way to handle application cleanup in case the process is
 * stopped by an external event, such as the user stopping the program.
 * 
 * @see http
 *      ://docs.oracle.com/javase/7/docs/api/java/lang/Runtime.html#addShutdownHook
 *      (java.lang.Thread)
 * @see http://www.onjava.com/pub/a/onjava/2003/03/26/shutdownhook.html
 * 
 * @author Robin Schellius
 */
class ShutdownHook extends Thread {

	// The service that we will clean up.
	private String service;
	
	// Constructor
	public ShutdownHook(String svc) { service = svc;}
	
	public void run() {
		try {
			LibraryServer.exit(service);
		} catch (RemoteException e) {
			System.out.println("Error exiting: could not contact remote server or registry.");
		}
	}
}
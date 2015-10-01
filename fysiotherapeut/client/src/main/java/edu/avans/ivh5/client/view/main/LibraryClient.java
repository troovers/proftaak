/*
 * 
 */
package edu.avans.ivh5.client.view.main;

import java.awt.EventQueue;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import edu.avans.ivh5.api.RemoteMemberAdminClientIF;
import edu.avans.ivh5.client.control.Controller;
import edu.avans.ivh5.client.view.ui.UserInterface;
import edu.avans.ivh5.shared.util.Settings;

/**
 * <p>
 * Client that connects to the given RMI service from the registry and opens a
 * GUI. The RMI registry must be running and the server class must be available
 * in order for this client to work properly.
 * </p>
 * <p>
 * This application requires the properties that were specified in the
 * accompanying properties file. Adjust the properties in the file to change the
 * settings and behavior of this client.
 * </p>
 * 
 * @author Robin Schellius
 */
public class LibraryClient {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(LibraryClient.class);

	/**
	 * Constructor. Locate the registry, lookup the service name that was provided via
	 * the properties file, and instantiate a RemoteMemberAdminManagerClient stub.
	 * 
	 * @param hostname The host where the server is running.
	 * @see RemoteMemberAdminClientIF
	 */
	public LibraryClient(String hostname) {

		logger.debug("Constructor using " + hostname);

		// The service name consists of a group name and a service name.
		// The group name enables finding all services within a given group.
		String service = Settings.props.getProperty(Settings.propRmiServiceGroup) + 
				Settings.props.getProperty(Settings.propRmiServiceName);

		RemoteMemberAdminClientIF manager = null;
		try {
			logger.debug("Locate registry on " + hostname);		
			Registry registry = LocateRegistry.getRegistry(hostname);
			logger.debug("Found registry");
			
			logger.debug("Connecting to remote service" + service);
            manager = (RemoteMemberAdminClientIF) registry.lookup(service);
			logger.debug("Connected");	            
        } 
		catch (java.security.AccessControlException e) {
			logger.error("Could not access registry: " + e.getMessage());			
		} catch (RemoteException e) {
			logger.error("RemoteException: " + e.getMessage());			
		} catch (NotBoundException e) {
			logger.error("Service not found: " + e.getMessage());			
		}
		
		// Create controller.
		final Controller controller = new Controller(manager);
		
		/**
		 *  Build the UI. Note that, since the Controller handles all UI events, it
		 *  must be constructed and available before the UI gets created.
		 */
	    EventQueue.invokeLater(new Runnable() {

            public void run() {
            	new UserInterface(controller).display();
            }
        });
	}

	/**
	 * Main method setting up this client application.
	 * 
	 * @param args
	 *            Command line argument identifying the property file. <br/>
	 *            Format: -properties [filename].
	 */
	public static void main(String[] args) {

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
		
		if (System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// Configure logging using the given log config file.
		PropertyConfigurator.configure(Settings.props.getProperty(Settings.propLogConfigFile));
		logger.info("Starting application");

		// Create the client, and connect it to the given (server) hostname.
		String hostname = System.getProperty(Settings.propRmiHostName);
		new LibraryClient(hostname);
	}

	/**
	 * Read the command line and parse the name of the properties file. The
	 * properties file contains all required properties for running this
	 * application.
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
			System.out.println("Error reading command line parameters.");
			System.out.println("Usage: -properties [filename or URL]");
			System.out.println("       -properties client.properties");
			System.exit(0);
		}
		return propertiesfilename;
	}
	
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.server.model.dao.rmi;

import java.rmi.AccessException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * Handles the connection to a remote service via Java Remote Method Invocation (RMI).
 * 
 * @author Robin Schellius
 */
public class RmiConnection {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(RmiConnection.class);
	
	// 
	static Object remoteService = null;
	
	// The name of the host to connect to
	public static String hostname = null;
	// The name of the service to connect to
	public static String servicename = null;
	
	// A list of available registries, possibly on other remote hosts.
	private static HashMap<String, Registry> registries = new HashMap<String, Registry>();
	
	// The list of services that we can connect to
	private static ArrayList<String> availableServices = null;
	
	/**
	 * Method to get and return a reference to the RMI registry on the given server.
	 * 
	 * @param hostname
	 * @return
	 */
	public static Registry getRegistry(String hostname) {
		
		logger.debug("getRegistry on " + hostname);
		
		try {
			// Check if we have previously found the requested registry
			if (registries.containsKey(hostname) && (registries.get(hostname) != null)) {
				logger.debug("Found registry in cache.");
			} else {
				Registry registry = LocateRegistry.getRegistry(hostname);
				logger.debug("Found registry, storing it in cache.");
				registries.put(hostname, registry);
			}
		} catch (RemoteException e) {
			logger.fatal("Could not access the registry: " + e.getMessage());
		} catch (java.security.AccessControlException e) {
			logger.fatal("No access: " + e.getMessage());
		} catch (Exception e) {
			logger.fatal("Exception: " + e.getMessage());
			// e.printStackTrace();
		}
		return registries.get(hostname);
	}

	/**
	 * Find all available services that can be contacted from this service. This includes
	 * all services that are listed in the RMI registry on the host that this service is 
	 * running on, and could include more remote registries and services if available.
	 * 
	 * @return
	 */
	public static ArrayList<String> findAvailableServices(String hostname, String serviceGroup) {
		
		logger.debug("findAvailableServices at " + hostname + " using " + serviceGroup);
	
		// Make sure we start with a fresh list.
		availableServices = null;
		String[] listOfServices = null;
		Registry registry;
	
		try {
			registry = LocateRegistry.getRegistry(hostname);
			listOfServices = registry.list();
	
			if(listOfServices != null) {
				availableServices = new ArrayList<String>();
				for(String servicename: listOfServices) {
					// Return only services that share the given service group name
					if(servicename.startsWith(serviceGroup)) {
						availableServices.add(servicename);
					}
				}
				logger.debug("Found " + availableServices.size() + " services: " + availableServices.toString());
			}
		} catch (AccessException e) {
			logger.error("AccessException: " + e.getMessage());
		} catch (RemoteException e) {
			logger.error("RemoteException: Could not contact host " + hostname);
		}
	
		return availableServices;
	}

	/**
	 * Internal class to get a service using a separate thread.
	 * 
	 * @author Robin Schellius
	 */
    public static class getService implements Runnable {
	
    	private String hostname;
    	private String service;
    	
	    /**
	     * Creates a RMI connection to the given service on the given hostname.
	     * 
	     * @param host Hostname or IP-address of remote server.
	     * @param service The name of the service as it is registered in the registry.
	     */
	    public getService(String host, String svc) {
	    	hostname = host;
	    	service = svc;
			logger.debug(Thread.currentThread().getName() + " Constructor");
	    }

	    /**
	     * The thread's main method that runs and does the work.
	     */
		public void run() {
	    	
			logger.debug(Thread.currentThread().getName() + 
					"Connecting to " + service + " on host "  + hostname);
			
	        try {
	            Registry registry = getRegistry(hostname);
	            remoteService = (Object) registry.lookup(service);
				logger.debug(Thread.currentThread().getName() + 
						" Connected to " + service);	            
	        } 
			catch (java.security.AccessControlException e) {
				logger.error("Could not connect to registry: " + e.getMessage());			
			}
	        catch (java.rmi.NotBoundException e) {
	            logger.error("Service " + service + " not found!");
	        }
	        catch (Exception e) {
	            logger.error(e.getMessage());
	            logger.error("(Are the webserver, the registry and the server running?)");
	        }
	    }
    }

    /**
     * Threaded class to get a list of services from a given host.
     * 
     * @author Robin Schellius
     */
    public static class listServices implements Runnable {

    	private static String hostname;
    	
    	/**
    	 * Constructor
    	 * 
    	 * @param host
    	 */
    	public listServices(String host) {
    		hostname = host;
    	}
    	
    	/**
	     * The thread's main method that runs and does the work.
    	 */
	    public void run() {
	        try {	        	
	        	String[] list = getRegistry(hostname).list();
	        	
	        	if(list != null) {
	        		logger.debug("Found services: " + Arrays.toString(list));
	        	}
	        } catch (RemoteException e) {
				logger.error(e.getMessage());
			}
	    }
	}

    
    
}

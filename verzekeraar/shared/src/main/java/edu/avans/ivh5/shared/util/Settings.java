package edu.avans.ivh5.shared.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides general utility functions for handling properties and settings.
 *  
 * @author Robin Schellius
 *
 */
public class Settings {

	/**
	 *  Define constants for property values.
	 */
	public static final String propRmiServiceName = "service.servicename";
	public static final String propRmiServiceGroup = "service.servicegroup";
	public static final String propLogConfigFile = "logconfigfile";
	public static final String propRmiHostName = "java.rmi.server.hostname";
	public static final String propRmiCodebase = "java.rmi.server.codebase";
	public static final String propSecurityPolicy = "java.security.policy";
	public static final String propDataStore = "daoclassname";
	public static final String propDataComm = "rmiclassname";
	public static final String propDbUser = "mysql.username";
	public static final String propDbPassword = "mysql.password";
	public static final String propDbConnectionString = "mysql.connectionstring";
	public static final String propDbDriver = "mysql.drivername";
	public static final String propXmlFileName = "xml.filename";
	public static final String propXmlSchema = "xml.schema";
	public static final String propRmiServiceHosts = "service.availableservers";

	public static Properties props = new Properties();

	/**
	 * <p>Load the properties for this application from the given properties file.
	 * The properties are used to initialize variables that are important in the
	 * correct initialization of the application.</p>
	 * 
	 * <p>
	 * The properties file allows the use of placeholders. A placeholder is a property
	 * between double brackets ({{ and }}). Method loadProperties replaces these placeholders
	 * with their correct value. This is not standard functionality and must be 
	 * implemented per use.
	 * </p>
	 * 
	 * @param propertiesFileName
	 *            The name of the file containing the system properties.
	 */
	public static void loadProperties(String propertiesFileName) {

		InputStream inputFile = null;

		try {
			inputFile = new FileInputStream(propertiesFileName);
			if (inputFile != null) {
				props.load(inputFile);

				/**
				 * Replace placeholders.
				 */
				String rmiHostName = props.getProperty(propRmiHostName);
				String rmiCodeBase = props.getProperty(propRmiCodebase);
				rmiCodeBase = rmiCodeBase.replace("{{"+propRmiHostName+"}}", rmiHostName);				
				String rmiSecurity = props.getProperty(propSecurityPolicy);		
				rmiSecurity = rmiSecurity.replace("{{"+propRmiHostName+"}}", rmiHostName);

				// Set System properties.
				System.setProperty(propRmiHostName, rmiHostName);
				System.setProperty(propRmiCodebase, rmiCodeBase);
				System.setProperty(propSecurityPolicy, rmiSecurity);

				// List all properties
				// props.list(System.out);
			}
		} catch (IOException  e) {
			System.out.println("Error reading file: " + e.getMessage() + " - aborting.");
			System.exit(0);
		} catch (Error e) {
			System.out.println(e.getMessage());
		} finally {
			if (inputFile != null) {
				try {
					inputFile.close();
				} catch (IOException e) {
					System.out.println("Error closing file: "
							+ e.getMessage());
				}
			}
		}
		
	}

	public void robin() {

		Properties p = System.getProperties();
		
		System.out.println("In Test:");
		System.out.println(Settings.propRmiCodebase + " = " + p.getProperty(Settings.propRmiCodebase));
	}
}

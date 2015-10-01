package edu.avans.ivh5.server.model.dao.rdbms.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

import org.apache.log4j.Logger;

import edu.avans.ivh5.shared.util.Settings;

/**
 * This class manages the connection to the MySQL database. In the current implementation
 * the connection to the database is created before each use, and closed directly after it.
 * This is an implementation choice; a different approach could have been to have a single 
 * database connection that is reused throughout the entire application.
 * 
 * @author ppthgast, rschelli
 */
public class MySqlConnection {
	
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(MySqlConnection.class);

	// Username to connect to the database
	private String username;
	// password to connect to the database
	private String password;
	// drivername used to connect to the MySQL database server
	private String drivername;	
	// connectionstring to connect to the database
	private String connectionstring;
	// the connection that is created.
    private Connection connection;
    
    // The Statement object has been defined as a field because some methods
    // may return a ResultSet object. If so, the statement object may not
    // be closed as you would do when it was a local variable in the query
    // execution method.
    private Statement statement;
    
    /**
     * Constructor. Initialize the database settings and create the database 
     * driver class. We do not make a connection yet.
     */
    public MySqlConnection()
    {
		logger.debug("Constructor");

		connection = null;
        statement = null;
		
        // Initialize using previously read properties; invalid otherwise.
        username = Settings.props.getProperty(Settings.propDbUser);
        password = Settings.props.getProperty(Settings.propDbPassword);
        drivername = Settings.props.getProperty(Settings.propDbDriver);
        connectionstring = Settings.props.getProperty(Settings.propDbConnectionString);

		try {
			logger.debug("Initializing driver " + drivername);
            Class.forName(drivername).newInstance();
        } catch (Exception e) {
                logger.fatal("Error initializing driver: " + e.getMessage());
        }
    }
        
    /**
     * Open the connection to the database.
     * 
     * @return True when the connection is open, false otherwise.
     */
    public boolean openConnection()
    {
		logger.debug("openConnection");
		
        boolean result = false;

        if(connection == null)
        {
            try
            {   
        		logger.debug("Connecting using " + connectionstring + ", " + username);
                connection = DriverManager.getConnection(
                    connectionstring , username, password);

                if(connection != null)
                {
                    statement = connection.createStatement();
                }
                
                result = true;
            }
            catch(SQLException e)
            {
                logger.fatal(e.getMessage());
                logger.fatal("(Did you start mysql and import the sql script?)");
                result = false;
            }
        }
        else
        {
    		logger.debug("Using existing statement");
            result = true;
        }
        
        return result;
    }
    
    /**
     * Check whether an open connection is available.
     * 
     * @return True when the connection is open, false otherwise.
     */
    public boolean connectionIsOpen()
    {
        boolean open = false;
        
		logger.debug("connectionIsOpen");

		if(connection != null && statement != null)
        {
            try
            {
                open = !connection.isClosed() && !statement.isClosed();
            }
            catch(SQLException e)
            {
                logger.error(e.getMessage());
                open = false;
            }
        }
        // Else, at least one of the connection or statement fields is null, so
        // no valid connection.
        
        return open;
    }
    
    /**
     * Close the current connection.
     */
    public void closeConnection()
    {
		logger.debug("closeConnection");
        try
        {
            statement.close();
            
            // Close the connection
            connection.close();
        }
        catch(Exception e) {
            logger.error(e.getMessage());
        }
    }
    
    /**
     * Execute the given SQL query.
     * 
     * @param query The query to be executed
     * @return ResultSet if results were found, <code>null</code> otherwise.
     */
    public ResultSet executeSQLStatement(String query)
    {
		logger.debug("executeSQLStatement(" + query + ")");

		ResultSet resultset = null;
        
        // First, check whether a some query was passed and the connection with
        // the database.
        if(query != null && connectionIsOpen())
        {
            // Then, if succeeded, execute the query.
            try
            {
                resultset = statement.executeQuery(query);
            }
            catch(SQLException e)
            {
                logger.error(e.getMessage());
                resultset = null;
            }
        }
        
        return resultset;
    }    
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.server.model.dao.rdbms.mysql;

import org.apache.log4j.Logger;

import edu.avans.ivh5.server.model.dao.DAOFactory;
import edu.avans.ivh5.server.model.dao.api.LoanDAOInf;
import edu.avans.ivh5.server.model.dao.api.MemberDAOInf;
import edu.avans.ivh5.server.model.dao.api.ReservationDAOInf;

/**
 * This factory provides the MySQL data source implementation. Clients can
 * ask this factory to create specific MySQL data access objects for the 
 * domain classes in this system.
 *
 * @author Robin Schellius
 */
public class MySqlDAOFactory extends DAOFactory {
    
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(MySqlDAOFactory.class);

	/**
	 * Empty constructor.
	 */
	public MySqlDAOFactory() {
		logger.debug("Factory constructed.");
    }
    
    /**
     * Return the MySQL implementation for the MemberDAO.
     */
	public MemberDAOInf getMemberDAO() {
		return new MySqlMemberDAO();
	}
	
    /**
     * Return the MySQL implementation for the LoanDAO.
     */
	public LoanDAOInf getLoanDAO() {
		return new MySqlLoanDAO();
	}
	
    /**
     * Return the MySQL implementation for the ReservationDAO.
     */
	public ReservationDAOInf getReservationDAO() {
		return new MySqlReservationDAO();
	}
    
}

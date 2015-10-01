/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.server.model.dao.xml.dom;

import org.apache.log4j.Logger;

import edu.avans.ivh5.server.model.dao.DAOFactory;
import edu.avans.ivh5.server.model.dao.api.LoanDAOInf;
import edu.avans.ivh5.server.model.dao.api.MemberDAOInf;
import edu.avans.ivh5.server.model.dao.api.ReservationDAOInf;

/**
 * The XML Document Object Model (DOM) implementation of the DAOFactory.
 * This factory provides XML DOM implementations for the domain classes
 * in this system.
 *
 * @author Robin Schellius
 */
public class XmlDOMDAOFactory extends DAOFactory {
    
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(XmlDOMDAOFactory.class);

	/**
	 * Constructor.
	 */
	public XmlDOMDAOFactory() {
		logger.debug("Factory constructed.");
    }
    
	/**
	 * Return the XML DOM implementation of the MemberDAO interface.
	 */
	public MemberDAOInf getMemberDAO() {
		return new XmlDOMMemberDAO();
	}
	
	/**
	 * Return the XML DOM implementation of the LoanDAO interface.
	 */
	public LoanDAOInf getLoanDAO() {
		return new XmlDOMLoanDAO();
	}
	
	/**
	 * Return the XML DOM implementation of the ReservationDAO interface.
	 */
	public ReservationDAOInf getReservationDAO() {
		return new XmlDOMReservationDAO();
	}
}

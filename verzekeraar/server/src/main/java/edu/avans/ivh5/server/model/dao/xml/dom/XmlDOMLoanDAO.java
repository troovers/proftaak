/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.server.model.dao.xml.dom;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.avans.ivh5.server.model.dao.api.LoanDAOInf;
import edu.avans.ivh5.shared.model.domain.Loan;
import edu.avans.ivh5.shared.model.domain.Member;

/**
 * The XML DOM implementation for the LoanDAO class. This class provides CRUD 
 * methods to store Loan information in an XML file using the Document Object Model
 * approach.
 * 
 * @author Robin Schellius
 */
public class XmlDOMLoanDAO implements LoanDAOInf {
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(XmlDOMLoanDAO.class);

	public XmlDOMLoanDAO() {
	}

	/**
	 * Tries to find the loans for the given in the persistent data store, in
	 * this case a MySQL database.In this POC, the lend copies of the books are
	 * not loaded - it is out of scope for now.
	 * 
	 * @param member
	 *            identifies the member whose loans are to be loaded from the
	 *            database
	 * 
	 * @return an ArrayList object containing the Loan objects that were found.
	 *         In case no loan could be found, still a valid ArrayList object is
	 *         returned. It does not contain any objects.
	 */
	public ArrayList<Loan> findLoans(Member member) {
		logger.debug("findLoans - not implemented yet.");

		return null;
	}
}

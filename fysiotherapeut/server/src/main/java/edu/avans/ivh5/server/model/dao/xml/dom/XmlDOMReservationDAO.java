/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.server.model.dao.xml.dom;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import edu.avans.ivh5.server.model.dao.api.ReservationDAOInf;
import edu.avans.ivh5.shared.model.domain.Member;
import edu.avans.ivh5.shared.model.domain.Reservation;

/**
 * The XML DOM implementation for the ReservationDAO class. This class provides CRUD 
 * methods to store Reservation information in an XML file using the Document Object Model
 * approach.

 * @author Robin Schellius
 */
public class XmlDOMReservationDAO implements ReservationDAOInf {
	
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(XmlDOMReservationDAO.class);

    public XmlDOMReservationDAO()
    {
        // Nothing to be initialized. This is a stateless class. Constructor
        // has been added to explicitly make this clear.
    }
    
    /**
     * Tries to find the reservations for the given in the persistent data store.
     * In this POC, the lend copies of the books are
     * not loaded - it is out of scope for now.
     * 
     * @param member identifies the member whose reservations are to be
     * loaded from the database
     * 
     * @return an ArrayList object containing the Reservation objects that were found.
     * In case no reservation could be found, still a valid ArrayList object is returned.
     * It does not contain any objects.
     */
    public ArrayList<Reservation> findReservations(Member member)
    {
		logger.debug("findReservations - not implemented yet.");
                
        return null;
    }
}

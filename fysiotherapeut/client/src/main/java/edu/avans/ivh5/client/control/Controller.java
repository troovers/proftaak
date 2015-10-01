/**
 * Avans Academie voor ICT & Engineering
 * Worked example - Library Information System.
 */
package edu.avans.ivh5.client.control;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.EventListener;

import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.apache.log4j.Logger;

import edu.avans.ivh5.api.RemoteMemberAdminClientIF;
import edu.avans.ivh5.api.RemoteMemberInfo;
import edu.avans.ivh5.client.view.ui.DataTableModel;
import edu.avans.ivh5.client.view.ui.UserInterface;
import edu.avans.ivh5.shared.model.domain.Member;

/**
 * <p>
 * This class contains the controller functionality from the Model-View-Control approach. 
 * The controller handles all actions and events that result from interaction with the system, 
 * in whatever way - being user interaction via the UI, or machine-to-machine interaction via
 * a data access object.
 * </p>
 * <p>
 * The controller separates the controlling functionality from the model (managing access to data)
 * and the view (managing the displaying of data).
 * </p>
 * 
 * @author Robin Schellius
 */
public class Controller implements ActionListener, EventListener, ListSelectionListener {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(Controller.class);

	// A reference to the user interface for access to view components (text fields, tables)
	private UserInterface userinterface = null;
	
	// Reference to the member manager.
	private RemoteMemberAdminClientIF manager = null;
	
	// When we find members on a host and service, we store them here for later retrieval.
	ArrayList<RemoteMemberInfo> globalMemberList = null;
	
	/**
	 * Constructor, initializing generally required references to user interface components.
	 */
	public Controller(RemoteMemberAdminClientIF mgr) 
	{
		logger.debug("Constructor");
		manager = mgr;
	}
	
	/**
	 * Set the link to the user interface, so we can display the results of actions.
	 * 
	 * @param userinterface
	 */
	public void setUIRef(UserInterface ui) {
		this.userinterface = ui;
	}


	/**
	 * Find the member identified by membershipNr and display its information.
	 * 
	 * @param hostname Name/IP address of the host to find the member on.
	 * @param servicename Name of the service to find the host on.
	 * @param membershipNr Number of the manager to be found.
	 */
	public void doFindMember(String host, String service,int membershipNr) 
	{
		logger.debug("doFindMember " + host + " " + service + " " + membershipNr);
		
		Member member;
		if (manager == null || userinterface == null) {
			logger.error("Manager or userinterface is null!");
		} else {
			try {
				member = manager.findMember(host, service, membershipNr);

				if (member != null) {
					userinterface.setMemberDetails(member);
				} else {
					logger.debug("Member " + membershipNr + " not found");
					userinterface.setStatusText("Member " + membershipNr + " not found.");
				}
			} catch (RemoteException e) {
				logger.error("Error: " + e.getMessage());
			}
		}
	}

	/**
	 * Find all members on all hosts and services.
	 */
	public void doFindAllMembers() {
		logger.debug("doFindAllMembers on all hosts/services");

		if (manager != null) {
			try {
				globalMemberList = manager.findAllMembers();
			} catch (RemoteException e) {
				logger.error("Error finding members: " + e.getMessage());
			}
		} else {
			logger.error("Manager is null! Could not find members!");
		}

		// Count the number of members that we found.
		int count = (globalMemberList == null) ? 0 : globalMemberList.size();
		// Construct a status message.
		String status = "Found " + count + " members"; 
		logger.debug(status);
		userinterface.setStatusText(status);
		
		// If we found any results, set the results in the DataModel.
		// Setting the values in the DataModel will automatically 
		// update the table and display the contents.
		if(count > 0) {
	 		DataTableModel data = userinterface.getDataTableModel();
			data.setValues(globalMemberList);
		}
	}

	/**
	 * <p>
	 * Performs the corresponding action for a button. This method can handle
	 * actions for each button in the window. The method itself selects the
	 * appropriate handling based on the ActionCommand that we have set on each button. 
	 * </p>
	 * <p>
	 * Whenever a new button is added to the UI, provide it with an ActionCommand name and 
	 * provide the appropriate handling here.
	 * </p>
	 * 
	 * @param ActionEvent The event that is fired by the JVM whenever an action occurs.
	 */
	public void actionPerformed(ActionEvent e) {
	
		if (e.getActionCommand().equals("FIND_MEMBER")) {
			/**
			 * Find Member is called when the user inserts a 
			 * membershipNr and presses Search.
			 */
			try {	
				int membershipNr = Integer.parseInt(userinterface.getSearchValue().trim());
				
				if(null == globalMemberList) {
					logger.debug("Cannot perform search; first find all members.");
				} else {
					// We have to find membershipNr in the globalMemberList, since we have
					// to find the host and service it lives on
					for(RemoteMemberInfo member : globalMemberList) {
						if(member.getMember().getMembershipNumber() == membershipNr) {
							// Found it
							String host = member.getHostname();
							String service = member.getServicename();
							userinterface.eraseMemberDetails();
							doFindMember(host, service, membershipNr);
						}
					}
				}
			} catch (NumberFormatException ex) {
				logger.error("Wrong input, only numbers allowed");
				userinterface.setStatusText("Wrong input, only numbers allowed.");
				userinterface.setSearchBoxText("");
			}
		} else if (e.getActionCommand().equals("FIND_ALL_MEMBERS")) {
			/**
			 * Do a search for all members on all available services, and 
			 * display them in the table.
			 */
			try {
				logger.debug("Find all members on all hosts/services");
				doFindAllMembers();			
			} catch (Exception ex) {
				logger.error("Error: " + ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	/**
	 * This method handles list selection events. Whenever the user selects a row in the memberlist
	 * of the user interface, valueChanged is fired and the appropriate action is taken. 
	 */
	public void valueChanged(ListSelectionEvent e) {
		
	    ListSelectionModel lsm = (ListSelectionModel)e.getSource();
	
	    // There can be multiple events going on, since the user could possibly select multiple
	    // rows and even individual columns. We first wait until the user is finished adjusting. 
	    if(e.getValueIsAdjusting() == false) {
	
	    	// There could be multiple rows (or columns) selected, even 
	    	// though ListSelectionModel = SINGLE_SELECTION. Find out which 
	    	// indexes are selected.
	        int minIndex = lsm.getMinSelectionIndex();
	        int maxIndex = lsm.getMaxSelectionIndex();
	        for (int i = minIndex; i <= maxIndex; i++) {
	            if (lsm.isSelectedIndex(i)) 
	            {
	                String selectedMember = (String) userinterface.getDataTableModel().getValueAt(i, 0);
	                String hostname = (String) userinterface.getDataTableModel().getValueAt(i, 3);
	                String servicename = (String) userinterface.getDataTableModel().getValueAt(i, 4);
	                
	                if (selectedMember != null && !selectedMember.equals("")) {                        
	                    logger.debug("Selected member = " + selectedMember);
	                    doFindMember(hostname, servicename, Integer.parseInt(selectedMember));
	                }
	            }
	        }
	    }
	}
}
package edu.avans.ivh5.api;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

import edu.avans.ivh5.shared.model.domain.Member;

/**
 * Interface that describes the available methods on a remote library server.
 * 
 * @author Robin Schellius
 *
 */
public interface RemoteMemberAdminServerIF extends Remote {

	/**
	 * Find all members on the remote server, and return all results in a single list.
	 * 
	 * @return A list of information about the member, and the host and service that is was found on.
	 * @throws RemoteException
	 */
	public ArrayList<RemoteMemberInfo> findAllMembersOnServer() throws RemoteException;

	/**
     * Tries to find the Member object matching the given membership number.
     * 
     * @param membershipNumber the member unique number
     * @return if a matching member was found, a reference to the Member's
     * ImmutableMember interface is returned, null otherwise.
     */
	public Member findMemberOnServer(String hostname, String service, int membershipNumber) throws RemoteException;


}

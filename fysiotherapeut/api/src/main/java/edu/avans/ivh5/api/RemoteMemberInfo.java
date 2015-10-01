package edu.avans.ivh5.api;

import java.io.Serializable;
import java.util.Comparator;

import edu.avans.ivh5.shared.model.domain.ImmutableMember;
import edu.avans.ivh5.shared.model.domain.Member;

/**
 * <p>
 * This class contains information about a Member, and the host and service that it 
 * belongs to. In a Client-Server application, Members can be found on remote servers, so
 * we need to track on which server a Member was found, in order to be able to retrieve
 * it later.</p>
 * <p>
 * Instances of RemoteMemberInfo are kept in an Collection, such as ArrayList. Since Java
 * cannot know how it needs to sort these instances - it is not a simple integer -  we have 
 * to tell it how to. The Comparable interface provides a method compareTo. 
 * Implementing that method enables the sorting as we want it. 
 * </p>
 * 
 * @author Robin Schellius
 *
 */
public class RemoteMemberInfo implements Serializable, Comparable<RemoteMemberInfo> {

	private String hostname;
	private String servicename;
	private ImmutableMember member;
	
	// required by Serializable
	private static final long serialVersionUID = 2736416361882370162L;

	/**
	 * Constructor.
	 * 
	 * @param host
	 * @param service
	 */
	public RemoteMemberInfo(String host, String service, ImmutableMember m) {

		hostname = host;
		servicename = service;
		member = m;
	}
	
	/**
	 * @return the hostname
	 */
	public String getHostname() {
		return hostname;
	}

	/**
	 * @return the servicename
	 */
	public String getServicename() {
		return servicename;
	}

	/**
	 * @return the member
	 */
	public ImmutableMember getMember() {
		return member;
	}

	/**
	 * The compareTo method enables us to sort an array of RemoteMemberInfo objects.
	 * This method defines how we order two objects in the array. We chose here to 
	 * order on membershipNr, but any attribute could have been chosen.
	 */
	public int compareTo(RemoteMemberInfo toCompareTo) {
		
		// 'our' membershipNr
		int myMembershipNr = this.getMember().getMembershipNumber();
		// the 'other' object's membershipNr
		int otherMembershipNr = ((RemoteMemberInfo) toCompareTo).getMember().getMembershipNumber();
		 
		// sort in ascending order
		return myMembershipNr - otherMembershipNr;
	}
	
}

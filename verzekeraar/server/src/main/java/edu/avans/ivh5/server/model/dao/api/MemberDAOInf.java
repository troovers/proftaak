/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.server.model.dao.api;

import java.util.ArrayList;

import edu.avans.ivh5.shared.model.domain.ImmutableMember;
import edu.avans.ivh5.shared.model.domain.Member;

/**
 * This interface describes the methods that must be available in the MemberDAO 
 * implementation for a specific data source. 
 *
 * @author ppthgast
 */
public interface MemberDAOInf {
    
    /**
     * Tries to find the member identified by the given membership number
     * in the persistent data store, in this case a MySQL database. All loans
     * and reservations for the member are loaded as well. In this POC, the
     * reserved books and/or lend copies of the books are not loaded - it is
     * out of scope for now.
     * 
     * @param membershipNumber identifies the member to be loaded from the database
     * 
     * @return the Member object to be found. In case member could not be found,
     * null is returned.
     */
    public Member findMember(int membershipNumber);

    /**
     * Find all members using the current DAO.
     * 
     * @return a list of members, or null if none were found.
     */
    public ArrayList<ImmutableMember> findAllMembers();

    /**
     * Removes the given member from the database.
     * 
     * @param memberToBeRemoved an object of the Member class representing the
     * member to be removed.
     * 
     * @return true if execution of the SQL-statement was successful, false
     * otherwise.
     */
    public boolean removeMember(Member memberToBeRemoved);

    /**
     * Inserts the given member into the data source.
     * 
     * @param memberToInsert an object of the Member class representing the
     * member to be inserted.
     * 
     * @return int The ID of the inserted Member if execution was successful, -1
     * otherwise.
     */
    public int insertMember(Member memberToInsert);

    /*
     * This interface is not completed yet. Methods that probably should
     * be available here could handle the creating, updating and deleting o
     * of Member information from the data source that is used.
     * 
     */

}

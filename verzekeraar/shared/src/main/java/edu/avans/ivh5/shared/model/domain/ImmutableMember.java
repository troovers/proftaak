/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.avans.ivh5.shared.model.domain;

import java.rmi.Remote;

/**
 *
 * @author ppthgast
 */
public interface ImmutableMember extends Remote {
    
    /**
     * Retrieves the last name of the Member
     * 
     * @return last name
     */
    public String getLastname();

    /**
     * Retrieves the emailaddress of the Member
     * 
     * @return emailaddress
     */
    public String getEmailaddress();

    /**
     * Retrieves the house number of the Member
     * 
     * @return house number
     */
    public String getHouseNumber();
    
    /**
     * Retrieves the membership number of the Member
     * 
     * @return membership number
     */
    public int getMembershipNumber();

    /**
     * Retrieves the city of the Member
     *
     * @return city
     */
    public String getCity();

    /**
     * Retrieves the street of the Member
     * 
     * @return street
     */
    public String getStreet();
    
    /**
     * Retrieves the phone number of the Member
     * 
     * @return phone number
     */
    public String getPhoneNumber();

    /**
     * Retrieves the first name of the Member
     * 
     * @return first name
     */
    public String getFirstname();
    
    /**
     * Retrieves the amount of fine that the Member still has to pay
     * 
     * @return amount of fine
     */
    public double getFine();
    
    /**
     * Determines whether the Member still has loans that were not returned yet.
     * 
     * @return true if the Member still has loans, false otherwise.
     */
    public boolean hasLoans();
    
     /**
     * Determines whether the Member still has to pay some amount of fine.
     * 
     * @return true if the Member still has some unpaid fine, false otherwise.
     */
    public boolean hasFine();
    
     /**
     * Determines whether the Member still has reservations for a (number of)
     * books.
     * 
     * @return true if the Member still has reservations, false otherwise.
     */
    public boolean hasReservations();
    
     /**
     * Determines whether the Member can be removed from the system. The applicable
     * business rules are taken into account here.
     * 
     * @return true if the Member can be removed, false otherwise.
     */
    public boolean isRemovable();
}

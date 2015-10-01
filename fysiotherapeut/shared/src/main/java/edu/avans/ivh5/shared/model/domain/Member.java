/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.shared.model.domain;

import java.io.Serializable;
import java.util.ArrayList;


/**
 *
 * @author ppthgast
 */
@SuppressWarnings("serial")
public class Member implements ImmutableMember, Serializable {
    
    private int membershipNumber;
    private String firstname;
    private String lastname;
    private String street;
    private String houseNumber;
    private String city;
    private String phoneNumber;
    private String emailaddress;
    private double fine;
    
    private ArrayList<Loan> loans;
    private ArrayList<Reservation> reservations;
            
    public Member(int membershipNumber, String firstname, String lastname)
    {
        this.membershipNumber = membershipNumber;
        this.firstname = firstname;
        this.lastname = lastname;
        
        street = "";
        houseNumber = "";
        city = "";
        phoneNumber = "";
        emailaddress = "";
        fine = 0.00;
        
        loans = new ArrayList<Loan>();
        reservations = new ArrayList<Reservation>();
    }
    
    @Override
    public String getLastname()
    {
        return lastname;
    }

    public void setLastname(String lastname)
    {
        this.lastname = lastname;
    }

    @Override
    public String getEmailaddress()
    {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress)
    {
        this.emailaddress = emailaddress;
    }

    @Override
    public String getHouseNumber()
    {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber)
    {
        this.houseNumber = houseNumber;
    }

    @Override
    public int getMembershipNumber()
    {
        return membershipNumber;
    }

    public void setMembershipNumber(int membershipNumber)
    {
        this.membershipNumber = membershipNumber;
    }

    @Override
    public String getCity() 
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    @Override
    public String getStreet()
    {
        return street;
    }

    public void setStreet(String street)
    {
        this.street = street;
    }

    @Override
    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber)
    {
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String getFirstname()
    {
        return firstname;
    }

    public void setFirstname(String firstname)
    {
        this.firstname = firstname;
    }
    
    @Override
    public double getFine()
    {
        return fine;
    }
    
    public void setFine(double fine)
    {
        this.fine = fine;
    }
    
    public void setLoans(Loan[] loans)
    {
        removeAllLoans();
        
        for(Loan theLoan: loans)
        {
            addLoan(theLoan);
        }
    }
    
    // 
    // Added by rschelli - was this missing, or is it missing on purpose?
    //
    public ArrayList<Loan> getLoans() {
    	return loans;
    }
    
    public void addLoan(Loan newLoan)
    {
        loans.add(newLoan);
    }
    
    public void removeAllLoans()
    {
        loans.clear();
    }
    
    public void addReservation(Reservation newReservation)
    {
        reservations.add(newReservation);
    }
    
    // 
    // Added by rschelli - was this missing, or is it missing on purpose?
    //
    public ArrayList<Reservation> getReservations() {
    	return reservations;
    }

    public boolean remove()
    {
        // Result is always true. If we later on use a database from which
        // the member needs to be removed as well, we can return a more
        // meaningfull value.
        boolean result = true;
        
        removeAllReservations();
        
        return result;
    }
    
    private void removeAllReservations()
    {
        while(!reservations.isEmpty())
        {
            Reservation reservation = reservations.get(0);
            reservation.remove();
        }
        
        // Alternatives for this construction with the temporary copy of the
        // ArrayList field are for example:
        //
        // -- 1 --
        // Start removing the reservations starting from the END OF the list
        // moving backwards until the list is empty. Starting from the first
        // element towards the end of the list will not succeed since the
        // index values are messed up.
        //    int index = reservations.size() - 1;
        //    
        //    while(index >= 0)
        //    {
        //        reservations.r
        //        Reservation reservation = reservations.get(index);
        //        reservation.remove();
        //        
        //        index--;
        //    }
        //
        // -- 2 --
        //
        //    ArrayList<Reservation> tempList = new ArrayList<>(reservations);
        //      
        //    for(Reservation r: reservations)
        //    {
        //        r.remove();
        //    }
        //
        // This defenitely needs clarification. Using a for each or iterator
        // on a list, has the limitation that deletion of elements from that
        // list is not allowed. Therefore a new ArrayList object is created,
        // referring to the same Reservation objects as the reservations field.
        // The local variable tempList is not modified during execution of the
        // loop. The reservations field on the other hand is.
    }
    
    @Override
    public boolean hasLoans()
    {
        return !loans.isEmpty();
    }
    
    @Override
    public boolean hasFine()
    {
        return fine > 0;
    }
    
    @Override
    public boolean hasReservations()
    {
        return !reservations.isEmpty();
    }
    
    @Override
    public boolean isRemovable()
    {
        return !hasLoans() && !hasFine();
    }
    
    public void removeReservation(Reservation reservation)
    {
        reservations.remove(reservation);
    }
    
    @Override
    public boolean equals(Object o)
    {
        boolean equal = false;
        
        if(o == this)
        {
            // Dezelfde instantie van dezelfde klasse, dus per definitie gelijk.
            equal = true;
        }
        else
        {
            if(o instanceof Member)
            {
                Member l = (Member)o;
                
                // Member wordt geidentificeerd door membershipNumber, 
                // dus alleen hierop controlleren is voldoende.
                equal = this.membershipNumber == l.membershipNumber;
            }
        }
        
        return equal;
    }
    
    @Override
    public int hashCode()
    {
        // Deze implementatie is gebaseerd op de best practice zoals beschreven
        // in Effective Java, 2nd edition, Joshua Bloch.
        
        // membershipNumber is uniek, dus voldoende als hashcode.
        return membershipNumber;
    }
    
    /**
     * Returns the membershipNumber when member.toString() is called. Utility method
     * for debugging purposes.
     *  
     * @return membershipNumber as a String.
     */
    @Override
    public String toString() {
    	return Integer.toString(membershipNumber);
    }
    
}

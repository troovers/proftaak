/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.shared.model.domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author ppthgast
 */
@SuppressWarnings("serial")
public class Loan implements Serializable {
    
    private Date returnDate;
    
    private Member member;
    private Copy copy;
    
    /**
     * Constructor voor Loan. Omdat een Loan niet zonder Member en Copy
     * kan, zijn deze direct als input voor de constructor vereist.
     * 
     * @param member het Member voor wie de Loan geldt
     * @param copy het Copy dat het Member heeft geleend
     * @param returnDate datum op welke het copy geretourneerd dient te worden.
     */
    public Loan(Date returnDate, Member member, Copy copy)
    {
        this.returnDate = returnDate;
        this.member = member;
        this.copy = copy;
    }
    
    /**
     * Accessor methode om het betrokken Member voor deze Loan op te halen.
     * @return het betrokken Member
     */
    public Member getMember()
    {
        return member;
    }
    
    /**
     * Accessor methode om het betrokken Copy voor deze Loan op te halen.
     * @return het betrokken Copy
     */
    public Copy getCopy()
    {
        return copy;
    }
    
    /**
     * Accessor methode om de retourdaum voor deze Loan op te halen.
     * @return de retourdatum
     */
    public Date getReturnDate()
    {
        return returnDate;
    }
}

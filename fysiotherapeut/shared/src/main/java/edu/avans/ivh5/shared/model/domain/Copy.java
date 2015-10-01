/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.shared.model.domain;

import java.io.Serializable;

/**
 *
 * @author ppthgast
 */
@SuppressWarnings("serial")
public class Copy implements Serializable {
    
    private int copyID;
    private int lendingPeriod;
    
    // null indien exmplaar niet uitgeleend.
    private Loan loan;
    
    private Book book;
    
    public Copy(int copyID, int lendingPeriod, Book book)
    {
        this.copyID = copyID;
        this.lendingPeriod = lendingPeriod;
        this.book = book;
        
        loan = null;
    }
    
    public int getCopyID()
    {
        return copyID;
    }
    
    public int getLendingPeriod()
    {
        return lendingPeriod;
    }
    
    public Book getBook()
    {
        return book;
    }
    
    public void setLoan(Loan newLoan)
    {
        loan = newLoan;
    }
    
    public Loan getLoan()
    {
        return loan;
    }
    
    public boolean isLent()
    {
        return loan != null;
    }
}

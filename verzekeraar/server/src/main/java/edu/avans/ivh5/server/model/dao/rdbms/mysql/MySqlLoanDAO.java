/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.server.model.dao.rdbms.mysql;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;

import edu.avans.ivh5.server.model.dao.api.LoanDAOInf;
import edu.avans.ivh5.shared.model.domain.Loan;
import edu.avans.ivh5.shared.model.domain.Member;

/**
 * The MySQL implementation of the LoanDAO. This class provides methods to
 * store Loan information in a MySQL database. 
 *
 * @author ppthgast
 */
public class MySqlLoanDAO implements LoanDAOInf
{
	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(MySqlLoanDAO.class);

	private MySqlConnection connection;

    public MySqlLoanDAO()
    {
    	connection = new MySqlConnection();
    }
    
    /**
     * Tries to find the loans for the given in the persistent data store, in
     * this case a MySQL database.In this POC, the lend copies of the books are
     * not loaded - it is out of scope for now.
     * 
     * @param member identifies the member whose loans are to be
     * loaded from the database
     * 
     * @return an ArrayList object containing the Loan objects that were found.
     * In case no loan could be found, still a valid ArrayList object is returned.
     * It does not contain any objects.
     */
    public ArrayList<Loan> findLoans(Member member)
    {
		logger.debug("findLoans");

		ArrayList<Loan> loans = new ArrayList<Loan>();
        
        if(member != null)
        {
            // First open a database connnection
            if(connection.openConnection())
            {
                // If a connection was successfully setup, execute the SELECT statement.
                int membershipNumber = member.getMembershipNumber();
                ResultSet resultset = connection.executeSQLStatement(
                    "SELECT * FROM loan WHERE MembershipNr = " + membershipNumber + ";");

                if(resultset != null)
                {
                    try
                    {
                        while(resultset.next())
                        {
                            // The value for the CopyID in the row is ignored
                            // for this POC: no Copy objects are loaded. Having the
                            // Loan objects without the Copy objects will do fine
                            // to determine whether the owning Member can be removed.
                            Date returnDate = resultset.getDate("ReturnDate");

                            Loan newLoan = new Loan(returnDate, member, null);
                            loans.add(newLoan);
                       }
                    }
                    catch(SQLException e)
                    {
                        System.out.println(e);
                        loans.clear();
                    }
                }
                // else an error occurred leave array list empty.

                // We had a database connection opened. Since we're finished,
                // we need to close it.
                connection.closeConnection();
            }
        }
        
        return loans;
    }
}

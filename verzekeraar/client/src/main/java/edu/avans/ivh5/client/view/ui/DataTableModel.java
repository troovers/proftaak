package edu.avans.ivh5.client.view.ui;

import java.util.ArrayList;
import java.util.Vector;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import edu.avans.ivh5.api.RemoteMemberInfo;
import edu.avans.ivh5.shared.model.domain.Member;

/**
 * This class provides a data model for the JTable component in the UI. Using this data model
 * enables us to quickly perform operations on the table containing member information. The contents of 
 * this data model are automatically displayed in the corresponding JTable. 
 * 
 * @author Robin Schellius
 */
@SuppressWarnings("serial")
public class DataTableModel extends AbstractTableModel {

	private Vector<String[]> cache; // will hold String[] objects . . .
	private int colCount;
	private String[] headers;

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(DataTableModel.class);

	/**
	 * Constructor.
	 */
	public DataTableModel() {
		logger.debug("Constructor");

		// colcount is hardcoded here; change if you want to display more columns.
		colCount = 5;
		headers = new String[colCount];
		cache = new Vector<String[]>();
	}

	/**
	 * Set the values of the data table model.
	 * 
	 * @param values String[][] containing the values to be set.
	 */
	public void setValues(String[][] values) {
		logger.debug("setValues String[][]");
	
		cache = new Vector<String[]>();
		try {
			int i = 0;
			while (i < values.length) {
				String[] row = new String[colCount];
				for (int j = 0; j < colCount; j++) {
					row[j] = values[i][j];
				}
				cache.addElement(row);
				i++;
			}
			// notify everyone that we have a new table.
			fireTableChanged(null); 
		} catch (Exception e) {
			cache = new Vector<String[]>();
			logger.error(e.getMessage());
		}
	}
	
	/**
	 * Set the values of the data table model.
	 * 
	 * @param members ArrayList containing the values to be set.
	 */
	public void setValues(ArrayList<RemoteMemberInfo> members) {
		
		logger.debug("setValues RemoteMemberInfo");
		int colcount = getColumnCount();
		int rowcount = members.size();
		String[][] values = new String[rowcount][colcount];
		
		for(int i = 0; i < members.size(); i++) 
		{
			RemoteMemberInfo info = members.get(i);
			Member m = (Member) info.getMember();
			
			String memberNr = Integer.toString(m.getMembershipNumber());
			String firstname = m.getFirstname();
			String lastname = m.getLastname();
			String hostname = info.getHostname();
			String service = info.getServicename();
			
			String[] value = { memberNr, firstname, lastname, hostname, service};
	
			values[i] = value;
		}
		
		setValues(values);
	}

	/**
	 * Set the header row of the table. The header row displays the column names in the table.
	 * 
	 * @param header Array of strings containing the column names.
	 */
	public void setTableHeader(String[] header) {
		logger.debug("setTableHeader");
	
		headers = new String[colCount];
		for (int h = 0; h < colCount; h++) {
			headers[h] = header[h];
		}
	}

	/**
	 * Get the name of column [i].
	 */
	public String getColumnName(int i) { 
		return headers[i];
	}

	/**
	 * Get the number of columns in the model.
	 */
	public int getColumnCount() {
		return colCount;
	}

	/**
	 * Get the number of rows.
	 */
	public int getRowCount() {
		return cache.size();
	}

	/**
	 * Get the value at [row,col].
	 */
	public Object getValueAt(int row, int col) {
		return ((String[]) cache.elementAt(row))[col];
	}
}
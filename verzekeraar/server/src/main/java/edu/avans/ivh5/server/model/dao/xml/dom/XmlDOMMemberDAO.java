package edu.avans.ivh5.server.model.dao.xml.dom;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.avans.ivh5.server.model.dao.api.MemberDAOInf;
import edu.avans.ivh5.shared.model.domain.Book;
import edu.avans.ivh5.shared.model.domain.Copy;
import edu.avans.ivh5.shared.model.domain.ImmutableMember;
import edu.avans.ivh5.shared.model.domain.Loan;
import edu.avans.ivh5.shared.model.domain.Member;

/**
 * The XML DOM implementation for the MemberDAO class. This class provides CRUD
 * methods to store Member information in an XML file using the Document Object
 * Model approach.
 * 
 * @author Robin Schellius
 */
public class XmlDOMMemberDAO implements MemberDAOInf {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(XmlDOMMemberDAO.class);

	private XmlDOMDocument domdocument = null;
	private Document document = null;

	/**
	 * Constructor
	 */
	public XmlDOMMemberDAO() {
		this.domdocument = new XmlDOMDocument();
		this.document = domdocument.getDocument();
	}

	/**
	 * Try to find the member identified by the given membership number in the
	 * persistent data store. All loans and reservations for the member are
	 * loaded as well. In this POC, the reserved books and/or lend copies of the
	 * books are not loaded - it is out of scope for now.
	 * 
	 * @param membershipNumber
	 *            identifies the member to be loaded from the database
	 * 
	 * @return the Member object to be found. In case member could not be found,
	 *         null is returned.
	 */
	public Member findMember(int membershipNumber) {

		logger.debug("findMember " + membershipNumber);

		Member member = null;

		if (document != null) {
			// Get all <member> elements from the document
			NodeList list = document.getElementsByTagName("member");

			for (int i = 0; i < list.getLength(); i++) {

				Node node = list.item(i);
				if (node instanceof Element) {

					Element child = (Element) node;
					String attribute = child.getAttribute("membershipNumber");
					if (Integer.parseInt(attribute) == membershipNumber) {

						logger.debug("found member " + attribute);

						// Construct the Member to be returned with the
						// information we found.
						String firstName = child
								.getElementsByTagName("firstname").item(0)
								.getTextContent();
						String lastName = child
								.getElementsByTagName("lastname").item(0)
								.getTextContent();

						member = new Member(Integer.parseInt(attribute),
								firstName, lastName);
						member.setStreet(child.getElementsByTagName("street")
								.item(0).getTextContent());
						member.setHouseNumber(child
								.getElementsByTagName("housenumber").item(0)
								.getTextContent());
						member.setCity(child.getElementsByTagName("city")
								.item(0).getTextContent());
						member.setFine(Double.parseDouble(child
								.getElementsByTagName("fine").item(0)
								.getTextContent()));
					}
				}
			}
		} else {
			logger.error("XML document is null!");
		}
		
		if (member == null)
			logger.debug("Member not found");

		return member;
	}

	/**
	 * Returns a list of MembershipNumbers of Members that exist in the given DAO,
	 * or null if none were found.
	 * 
	 * @return Array of integers indicating the MembershipNumbers, or null if 
	 * none were found.
	 * 
	 * @author Robin Schellius
	 */
	public ArrayList<ImmutableMember> findAllMembers() {
		logger.debug("findAllMembers");

		ArrayList<ImmutableMember> result = new ArrayList<ImmutableMember>();

		if (document != null) {
			// Get all <member> elements from the document
			NodeList list = document.getElementsByTagName("member");

			for (int i = 0; i < list.getLength(); i++) {
				Node node = list.item(i);
				if (node instanceof Element) {
					Element child = (Element) node;

					String membershipNr = child.getAttribute("membershipNumber");
					String firstname = child.getElementsByTagName("firstname").item(0).getTextContent();
					String lastname = child.getElementsByTagName("lastname").item(0).getTextContent();

					logger.debug("Adding " + membershipNr + " " + firstname + " " + lastname + " to result");
					result.add(new Member(Integer.parseInt(membershipNr), firstname, lastname));
				}
			}
		} else {
			logger.error("XML document is null!");
		}
		
		logger.debug("returning result: " + result.size() + " items");
		return result;
    }

	/**
	 * Removes the given member.
	 * 
	 * @param memberToBeRemoved
	 *            an object of the Member class representing the member to be
	 *            removed.
	 * 
	 * @return true if deletion was successful, false otherwise.
	 */
	public boolean removeMember(Member memberToBeRemoved) {
		logger.debug("removeMember membershipNumber - not yet implemented");

		boolean result = false;
		return result;
	}

	/**
	 * Inserts the given member into the datasource.
	 * 
	 * @param newMember
	 *            an object of the Member class representing the member to be
	 *            inserted.
	 * 
	 * @return true if execution was successful, false otherwise.
	 */
	public int insertMember(Member newMember) {

		logger.debug("insertMember " + newMember.getMembershipNumber());

		//
		// For consistency reasons, we should check whether the newMember
		// doesn't already exist in our data source. Since this file is only
		// for demonstration purpose, we do not perform that check here.
		//

		// Get the members element, of which there is only one.
		Node rootElement = document.getElementsByTagName("members").item(0);

		// Create the member with its attributes and sub-elements
		Element member = document.createElement("member");
		rootElement.appendChild(member);

		// Add the member's number as an attribute to member
		Attr attr = document.createAttribute("membershipNumber");
		// Implicitly convert int to String
		attr.setValue("" + newMember.getMembershipNumber());
		member.setAttributeNode(attr);
		// or shorter: member.setAttribute("id", "1");

		Element firstname = document.createElement("firstname");
		firstname.appendChild(document.createTextNode(newMember.getFirstname()));
		member.appendChild(firstname);

		Element lastname = document.createElement("lastname");
		lastname.appendChild(document.createTextNode(newMember.getLastname()));
		member.appendChild(lastname);

		Element street = document.createElement("street");
		street.appendChild(document.createTextNode(newMember.getStreet()));
		member.appendChild(street);

		Element houseNumber = document.createElement("housenumber");
		houseNumber.appendChild(document.createTextNode(newMember.getHouseNumber()));
		member.appendChild(houseNumber);

		Element city = document.createElement("city");
		city.appendChild(document.createTextNode(newMember.getCity()));
		member.appendChild(city);

		Element phoneNumber = document.createElement("phoneNumber");
		phoneNumber.appendChild(document.createTextNode(newMember.getPhoneNumber()));
		member.appendChild(phoneNumber);

		Element emailAddress = document.createElement("emailaddress");
		emailAddress.appendChild(document.createTextNode(newMember.getEmailaddress()));
		member.appendChild(emailAddress);

		Element fine = document.createElement("fine");
		fine.appendChild(document.createTextNode("" + newMember.getFine()));
		member.appendChild(fine);

		// Add loan elements to a member. There could be multiple.
		// A loan includes the loandata, the copy information, and info about
		// the book.
		if (newMember.hasLoans()) {

			Element loans = document.createElement("loans");
			member.appendChild(loans);

			for (Loan loan : newMember.getLoans()) {
				Element loanElement = document.createElement("loan");
				loans.appendChild(loanElement);

				Copy copy = loan.getCopy();
				loanElement.setAttribute("copyid", "" + copy.getCopyID());

				Element bookElement = document.createElement("book");
				loanElement.appendChild(bookElement);

				Book book = copy.getBook();

				Element isbn = document.createElement("isbn");
				isbn.appendChild(document.createTextNode("" + book.getISBN()));
				bookElement.appendChild(isbn);

				Element title = document.createElement("title");
				title.appendChild(document.createTextNode(book.getTitle()));
				bookElement.appendChild(title);

				Element author = document.createElement("author");
				author.appendChild(document.createTextNode(book.getAuthor()));
				bookElement.appendChild(author);

				Element lendingPeriod = document.createElement("lendingperiod");
				lendingPeriod.appendChild(document.createTextNode(""
						+ copy.getLendingPeriod()));
				loanElement.appendChild(lendingPeriod);

			}
		}

		// Adding Reservations is left to the reader.

		domdocument.writeDocument();

		// TODO Return valid result from insertMember in XmlDOMMemberDAO
		return 0;
	}
}

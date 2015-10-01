/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.avans.ivh5.server.model.dao.xml.dom;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import edu.avans.ivh5.shared.util.Settings;

/**
 * XmlDOMDocument contains the XML input document that functions as a data source for the
 * XML DOM data access objects. It validates and builds the document object model (DOM). 
 * The DOM is used throughout the application to perform CRUD-operations on the XML data. 
 * It could be viewed as an in-memory database, and needs to be saved at regular times.
 *   
 * @author Robin Schellius
 */
public class XmlDOMDocument {

	// Get a logger instance for the current class
	static Logger logger = Logger.getLogger(XmlDOMDocument.class);

	// The file that contains the XML data.
	private String xmlFilename;
	
	// The file that contains the schema to validate the data file.
	private String xmlSchema;

	// The document (structure) that will contain the object model of the XML file.
	private Document document = null;

	/**
	 * Constructor
	 */
	public XmlDOMDocument() {
		logger.debug("Constructor");
		
		// Read filenames from the previously installed properties; undefined otherwise.
		xmlFilename = Settings.props.getProperty(Settings.propXmlFileName);
		xmlSchema = Settings.props.getProperty(Settings.propXmlSchema);

		logger.debug("xmlFilename = " + xmlFilename);
		logger.debug("xmlSchema = " + xmlSchema);
	}

	/**
	 * Get the XML document that functions as the data source. We try to validate
	 * it, if the given XSD file is available.
	 * 
	 * @return The XML DOM document that holds objects representing the XML elements in the file.
	 */
	public Document getDocument() {

		logger.debug("getDocument");

		if (document == null) {
			// First validate that the XML datasource conforms the schema.
			Schema schema = getValidationSchema();
			if (schema == null) {
				logger.error("Schema file not found or contains errors, XML file not validated!");
				// Here we could decide to cancel initialization of the application.
				// For now, we do not.
			} else {
				validateDocument(xmlFilename, schema);
			}

			// Whether validated or not, try to build the
			// Document Object Model from the inputfile
			document = buildDocument(xmlFilename);
			if (document == null) {
				logger.fatal("No XML data source found! Cannot read application data!");
				// Again, here we could decide to cancel initialization of the application.
				// A non-validated inputfile could lead to read-errors when reading domain
				// objects from the data source. For now, we continue. Fingers crossed.
			}
		}
		return document;
	}

	/**
	 * Write the in-memory document object model to the given file. This is
	 * useful when the document object model has been modified, for example by
	 * adding or deleting members, books, reservations or loans.
	 */
	public void writeDocument() {

		logger.debug("writing XML document to file " + xmlFilename);

		try {
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(document);
			StreamResult result = new StreamResult(new File(xmlFilename));
			transformer.transform(source, result);

			logger.debug("done writing file");

		} catch (TransformerConfigurationException e) {
			logger.error(e.getMessage());
		} catch (TransformerException e) {
			logger.error(e.getMessage());
		}
	}

	/**
	 * Build a DOM document using the XML input filename.
	 * 
	 * @param filename The file that provides the XML contents to create the document object model.
	 * @return The DOM document that was created, or null otherwise.
	 */
	private Document buildDocument(String filename) {

		logger.debug("buildDocument");

		DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = builderFactory.newDocumentBuilder();
			File file = new File(filename);
			if (file.exists()) {
				document = builder.parse(new FileInputStream(file));
			} else {
				logger.fatal("Could not read file " + filename);
			}
		} catch (ParserConfigurationException e) {
			logger.error(e.getMessage());
		} catch (SAXException e) {
			logger.error(e.getMessage());
		} catch (IOException e) {
			logger.error(e.getMessage());
		}

		return document;
	}

	/**
	 * Create the schema object that enables validation of the XML document. 
	 * 
	 * @return The schema which is created from the schema file, or null otherwise.
	 */
	private Schema getValidationSchema() {

		logger.debug("getValidationSchema " + xmlSchema);
		Schema schema = null;

		try {
			String language = XMLConstants.W3C_XML_SCHEMA_NS_URI;
			SchemaFactory factory = SchemaFactory.newInstance(language);
			File xmlSchemaFile = new File(xmlSchema);
			if(xmlSchemaFile.exists()) {
				schema = factory.newSchema(xmlSchemaFile);
				logger.debug("Schema created");
			} else {
				logger.error("Schemafile does not exist.");
			}
		} catch (Exception e) {
			logger.error("Error reading schema file: " + e.getMessage());
		}

		return schema;
	}

	/**
	 * Perform the actual validation of the XML file using the provided schema.
	 * 
	 * @param xmlFile	The file containing the XML data.
	 * @param schema	The schema containing validation rules.
	 */
	private boolean validateDocument(String xmlFile, Schema schema) {
		logger.debug("validateDocument");

		boolean result = false;

		try {
			Validator validator = schema.newValidator();
			validator.validate(new StreamSource(xmlFile));
			result = true;
			logger.debug("Valid XML: " + xmlFile);
		} catch (IOException e) {
			logger.error("I/O error: " + e.getMessage());
		} catch (SAXException e) {
			logger.error("Parse exception: " + e.getMessage());
		}
		return result;
	}
}

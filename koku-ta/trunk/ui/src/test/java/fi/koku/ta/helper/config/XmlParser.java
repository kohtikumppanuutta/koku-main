package fi.koku.ta.helper.config;

import java.io.IOException;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Element;

//import org.apache.xerces.jaxp.DocumentBuilderFactoryImpl;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class XmlParser {
	
	private Configuration configuration;
	
	private Document dom;
	private String xmlFilePath;

	public XmlParser(Configuration configuration,String xmlFilePath) throws ParserConfigurationException,SAXException,IOException {
		
		this.configuration = configuration;
		
		dom = null;
		this.xmlFilePath=xmlFilePath;
		parseXmlFile();
		parseDocument();
		
		printInformation();
		
	}
	
	public Document getDocument() {
		return dom;
	}
	
	public String getXmlFilePath() {
		return xmlFilePath;
	}
	
	
	private void parseXmlFile() throws ParserConfigurationException,SAXException,IOException {
		
		//get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		//Using factory get an instance of document builder
		DocumentBuilder db = dbf.newDocumentBuilder();
		
		//parse using builder to get DOM representation of the XML file
		dom = db.parse(xmlFilePath);
				
	}


	private void parseDocument(){
		
		// normalize text representation
        dom.getDocumentElement().normalize();
        
        Element doc = dom.getDocumentElement();
        
		NodeList nl;
		
		//Parse <URL_Employee> element
		nl = doc.getElementsByTagName("URL_Employee");
		if(nl != null && nl.getLength() > 0) {
			Element element = (Element)nl.item(0);
			String parsedValue=element.getTextContent();
			if (parsedValue!=null) {
				configuration.setURL_Employee(new String(parsedValue));
			}
		}
		
		//Parse <URL_Guardian> element
		nl = doc.getElementsByTagName("URL_Guardian");
		if(nl != null && nl.getLength() > 0) {
			Element element = (Element)nl.item(0);
			String parsedValue=element.getTextContent();
			if (parsedValue!=null) {
				configuration.setURL_Guardian(new String(parsedValue));
			}
		}
		
	}	
	
	private void printInformation() {
		
		System.out.format("--- Parsed configuration information\n");
		System.out.format("URL_Employee:<%s>\n",configuration.getURL_Employee());
		System.out.format("URL_Guardian:<%s>\n",configuration.getURL_Guardian());
		System.out.format("---\n");

	}
	
}
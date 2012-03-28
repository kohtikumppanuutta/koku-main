package fi.koku.ta.helper.config;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;


public class Configuration {
	
	private static String xmlInputFile = "config_SystemUnderTest.xml";
	
	private String URL_Employee; //Työntekijä
	private String URL_Guardian; //Huoltaja
	
	
	public Configuration() {
		
		URL_Employee="x";
		URL_Guardian="x";
		
		init(Configuration.xmlInputFile);
		
	}
	
	/*
	 * Alternative constructor
	 */
	public Configuration(String xmlFile_config_SystemUnderTest) {
		
		URL_Employee="x";
		URL_Guardian="x";
		
		init(xmlFile_config_SystemUnderTest);
	}
	
	
	public String getURL_Employee() {
		return URL_Employee;
	}
	
	public void setURL_Employee(String url) {
		URL_Employee=url;
	}

	
	public String getURL_Guardian() {
		return URL_Guardian;
	}
	
	public void setURL_Guardian(String url) {
		URL_Guardian=url;
	}
	
	
	private void init(String xmlFile_config_SystemUnderTest) {
		
		String filePath= "resources/" + xmlFile_config_SystemUnderTest;
		
		try {
			parseXml_Configuration(filePath);
		}
		catch (Exception e) {
			System.out.format("*** FAILED to parse configuration XML file\n\n%s\n",e.getMessage());
		}

	}
	
	private void parseXml_Configuration(String filePath) throws ParserConfigurationException,SAXException,IOException {
		new XmlParser(this,filePath);
	}
	
	
	
	

	

}

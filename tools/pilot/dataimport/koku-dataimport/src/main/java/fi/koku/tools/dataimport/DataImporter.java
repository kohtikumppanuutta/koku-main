package fi.koku.tools.dataimport;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import au.com.bytecode.opencsv.CSVReader;

public class DataImporter {

  private static final int HELMI_CUSTOMER_PIC = 6;
  private static final int EFFICA_CUSTOMER_PIC = 5;
  private static final int EMPLOYEE_PIC = 4;
  private static final int HELMI_CUSTOMER = 3;
  private static final int EFFICA_CUSTOMER = 2;
  private static final int EMPLOYEE = 1;
  private static final int KAHVA_EMPLOYEE = 0;
  
  public static void main(String[] args) throws Exception {
    	
    	int choise =0;
    	
    	try {
    		if( args.length>0 )
        	{
    		if(args[0].contains("noui")) //no ui-version
    		  {
    			try {
    				//throws exception if choise not defined
    				try {
    				choise =  Integer.parseInt(args[1]);
    				} catch (NumberFormatException e) {
    					e.printStackTrace();
    					System.out.println("Error. No choise defined. Select from following:\n" +
        						"noui 1 = Kahva\n" +
        						"noui 2 = Employee\n" +
        						"noui 3 = Effica\n" +
        						"noui 4 = Helmi\n" + e.getMessage());
    				}
    				//kahva
        			if(choise == 1)
        			  {
        				new DataImporter(args,1);
        			  } 			
        			//employee
        			else if(choise == 2)
        			  {
        				new DataImporter(args,2);
        			  }
        			//effica
        			else if(choise == 3)
        			  {
        				new DataImporter(args,3);
        			  }
        			//helmi
        			else if(choise == 4)
        			  {
        				new DataImporter(args,4);
        			  }
        			} catch (Exception e) {
	        			 e.printStackTrace();
	        			 System.out.println("Error executing dataimport: " + e.getMessage());	        			 
	        		}
    		   }
        	}
    		else
    		   new DataImporter(args);
    } catch (Exception e) {      
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "There was an error executing the dataimport. " +
      		"Please check the console.\nException: " + e.getMessage());     
    }
  }

  // ui-version of Dataimporter
  public DataImporter(String[] args) throws Exception {
	
    Object[] options = new Object[] { "Kahva Employee", "Employee", "Effica customer", "Helmi customer", "Employee PICs",
        "Effica customer PICs", "Helmi customer PICs" };
    int returnvalue = JOptionPane.showOptionDialog(null, "Select file type.", null, JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    if (returnvalue == -1) {
      System.out.println("No file type selected, exiting.");
      return;
    }

    JFileChooser chooser = new JFileChooser(new File("c:/users/hekkata/desktop"));
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = chooser.showOpenDialog(null);
    
    if (result != JFileChooser.APPROVE_OPTION) {
      System.out.println("No file selected, exiting.");
      return;
    }

    WSCaller caller= null;
    
    if (result == JFileChooser.APPROVE_OPTION) {
      try { //try to instantiate WSCaller, error if properties file not found.
    	caller = new WSCaller();
      } catch (IOException e)
      {
    	  e.printStackTrace();
			 System.out.println("There was an error while executing dataimport" + e.getMessage());
      }
      
      File file = chooser.getSelectedFile();

      
      // Kahva Employee
      if (returnvalue == KAHVA_EMPLOYEE) {    	
    	  kahvaEmployeeWriteLDIF(file, caller);
      }

      // Employee
      if (returnvalue == EMPLOYEE) {
    	  employeeWriteLDIF(file);
      }

      // Effica Customer
      else if (returnvalue == EFFICA_CUSTOMER) {
    	  efficaCustomerWriteLDIF(file, caller);
      }
      
      // Helmi Customer
      else if (returnvalue == HELMI_CUSTOMER) {
    	  helmiCustomerWriteLDIF(file, caller);
      }
      
      // Employee Hetu
      else if (returnvalue == EMPLOYEE_PIC) {
        CSVReader reader = new CSVReader(new FileReader(file));
        try {
          new PICWriter().writeEmployeePICFile(reader, caller, file.getParentFile());
        } finally {
          reader.close();
        }
      }
      
      // Effica Customer Hetu
      else if (returnvalue == EFFICA_CUSTOMER_PIC) {
        CSVReader reader = new CSVReader(new FileReader(file));
        try {
          new PICWriter().writeEfficaCustomerPICFile(reader, file.getParentFile());
        } finally {
          reader.close();
        }
      }
      
      // Helmi Customer Hetu
      else if (returnvalue == HELMI_CUSTOMER_PIC) {
        CSVReader reader = new CSVReader(new FileReader(file));
        try {
          new PICWriter().writeHelmiCustomerPICFile(reader, file.getParentFile());
        } finally {
          reader.close();
        }
      }

      System.out.println("Done.");
    }
  }
  
//non-ui (commandline) version of Dataimporter
  public DataImporter(String[] args, int selection ) throws Exception {
	
	BufferedReader stdin = new BufferedReader ( new InputStreamReader( System.in ) );

	String fileName ="";	
	System.out.println("Give CSV-filename: ");
	
	try {
	  fileName = stdin.readLine();
	} catch (IOException e)	{ //this can be without catch, only with finally
		stdin.close();
		 e.printStackTrace();
		 System.out.println("Could not read filename. Exiting" + e.getMessage());
	}
	
    File file = new File(fileName + ".csv");
    
    try {
    	if (file.exists()){
    		System.out.println("csv-file used: " + file.getAbsolutePath());
    	}
    	else {
    		throw new SecurityException("File " + file.getAbsolutePath() + " does not exist. Exiting.");
    	}
	  } catch (SecurityException e)	{
			 e.printStackTrace();
			 System.out.println("There was an error while executing dataimport" + e.getMessage());
	}
    
    WSCaller caller= null;	
	try { //try to instantiate WSCaller, error if properties file not found.
    	caller = new WSCaller();
      } catch (IOException e)
      {
    	  e.printStackTrace();
			 System.out.println("There was an error while executing dataimport" + e.getMessage());
      }
    
      // Kahva Employee
      if (selection == 1) { //KAHVA_EMPLOYEE
        kahvaEmployeeWriteLDIF(file, caller);        
      }
      
      // Employee
      if (selection == 2) { //EMPLOYEE
        employeeWriteLDIF(file);        
      }

      // Effica Customer
      else if (selection == 3) { //EFFICA_CUSTOMER
        efficaCustomerWriteLDIF(file, caller);        
      }
      
      // Helmi Customer
      else if (selection == 4) { //HELMI_CUSTOMER 
    	helmiCustomerWriteLDIF(file, caller);
      }      
      
      System.out.println("Done.");
    }

  

private void kahvaEmployeeWriteLDIF(File file, WSCaller caller) throws Exception
  {
//Kahva Employee
    CSVReader reader = new CSVReader(new FileReader(file));
    try {
      new LDIFWriter().writeEmployeeLDIF(InputParser.employeeFromKahva(reader, caller), file.getParentFile());
    } finally {
      reader.close();
    }
  }
  

private void employeeWriteLDIF(File file) throws Exception
{
  // Employee
    CSVReader reader = new CSVReader(new FileReader(file));
	try {
      new LDIFWriter().writeEmployeeLDIF(InputParser.employeeFromCSV(reader), file.getParentFile());       
    } finally {
      reader.close();
    }
  }

private void efficaCustomerWriteLDIF(File file, WSCaller caller) throws Exception
{
  // Effica Customer
    CSVReader reader = new CSVReader(new FileReader(file));
    try {
      new LDIFWriter().writeEfficaCustomerLDIF(reader, file.getParentFile());
    } finally {
      reader.close();
    }

    reader = new CSVReader(new FileReader(file));
    try {
      new CustomerCreator().createEfficaCustomers(reader, caller, file.getParentFile());
    } finally {
      reader.close();
    }
  }
  

private void helmiCustomerWriteLDIF(File file, WSCaller caller) throws Exception
{
  // Helmi Customer
    CSVReader reader = new CSVReader(new FileReader(file));
    try {
      new LDIFWriter().writeHelmiCustomerLDIF(reader, file.getParentFile());
    } finally {
      reader.close();
    }

    reader = new CSVReader(new FileReader(file));
    try {
      new CustomerCreator().createHelmiCustomers(reader, caller, file.getParentFile());
    } finally {
      reader.close();
    }
  }
  
  
} //end of class


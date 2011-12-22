package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileReader;

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
    try {
      new DataImporter(args);
    } catch (Exception e) {      
      e.printStackTrace();
      JOptionPane.showMessageDialog(null, "There was an error executing the dataimport. " +
      		"Please check the console.\nException: " + e.getMessage());     
    }
  }

  public DataImporter(String[] args) throws Exception {

    Object[] options = new Object[] { "Kahva Employee", "Employee", "Effica customer", "Helmi customer", "Employee PICs",
        "Effica customer PICs", "Helmi customer PICs" };
    int returnvalue = JOptionPane.showOptionDialog(null, "Select file type.", null, JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    if (returnvalue == -1) {
      System.out.println("No file type selected, exiting.");
      return;
    }

    JFileChooser chooser = new JFileChooser(new File("c:/users/hanhian/desktop"));
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = chooser.showOpenDialog(null);
    
    if (result != JFileChooser.APPROVE_OPTION) {
      System.out.println("No file selected, exiting.");
      return;
    }

    if (result == JFileChooser.APPROVE_OPTION) {
      WSCaller caller = new WSCaller();
      File file = chooser.getSelectedFile();

      // Kahva Employee
      if (returnvalue == KAHVA_EMPLOYEE) {
        CSVReader reader = new CSVReader(new FileReader(file));
        try {
          new LDIFWriter().writeEmployeeLDIF(InputParser.employeeFromKahva(reader, caller), file.getParentFile());
        } finally {
          reader.close();
        }
      }

      // Employee
      if (returnvalue == EMPLOYEE) {
        CSVReader reader = new CSVReader(new FileReader(file));
        try {
          new LDIFWriter().writeEmployeeLDIF(InputParser.employeeFromCSV(reader), file.getParentFile());
          
          // TODO tähän vielä liittymä johonkin jossa kahvan tietoja säilytetään
        } finally {
          reader.close();
        }
      }

      // Effica Customer
      else if (returnvalue == EFFICA_CUSTOMER) {
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
      
      // Helmi Customer
      else if (returnvalue == HELMI_CUSTOMER) {
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
}
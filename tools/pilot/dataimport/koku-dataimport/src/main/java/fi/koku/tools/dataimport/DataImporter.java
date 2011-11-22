package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import au.com.bytecode.opencsv.CSVReader;

public class DataImporter {

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

    Object[] options = new Object[] { "Employee", "Effica customer", "Helmi customer" };
    int returnvalue = JOptionPane.showOptionDialog(null, "Select file type.", null, JOptionPane.DEFAULT_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    if (returnvalue != 0 && returnvalue != 1 && returnvalue != 2) {
      JOptionPane.showMessageDialog(null, "No file type selected, exiting.");
      return;
    }

    JFileChooser chooser = new JFileChooser(new File("c:/users/hanhian/desktop"));
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = chooser.showOpenDialog(null);

    if (result == JFileChooser.APPROVE_OPTION) {
      WSCaller caller = new WSCaller();
      File file = chooser.getSelectedFile();

      // User selected Employee
      if (returnvalue == 0) {

        System.out.println("employee");
        CSVReader reader = new CSVReader(new FileReader(file));
        try {
          new LDIFWriter().writeEmployeeLDIF(reader, caller, file.getParentFile());
        } finally {
          reader.close();
        }
      // Effica Customer
      } else if (returnvalue == 1) {

        System.out.println("effica");
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
      // Helmi Customer
      } else if (returnvalue == 2) {
        System.out.println("helmi");
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
    }
    System.out.println("Done.");
  }
}
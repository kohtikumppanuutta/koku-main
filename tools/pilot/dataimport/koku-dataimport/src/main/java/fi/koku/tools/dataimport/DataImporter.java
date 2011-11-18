package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import au.com.bytecode.opencsv.CSVReader;

public class DataImporter {

  public static void main(String[] args) throws Exception {
    new DataImporter(args);
  }

  public DataImporter(String[] args) throws Exception {

    Object[] options = new Object[] {"Employee", "Customer"};
    int returnvalue = JOptionPane.showOptionDialog(null, "Select file type.", null, JOptionPane.YES_NO_OPTION,
        JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

    if (returnvalue != 0 && returnvalue != 1) {
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
        CSVReader reader = new CSVReader(new FileReader(file));
        try {
          new LDIFWriter().writeEmployeeLDIF(reader, caller, file.getParentFile());
        } finally {
          reader.close();
        }
      // User selected Customer
      } else if (returnvalue == 1) {
        CSVReader reader = new CSVReader(new FileReader(file));
        try {
          new LDIFWriter().writeCustomerLDIF(reader, file.getParentFile());
        } finally {
          reader.close();
        }

        reader = new CSVReader(new FileReader(file));        
        try {
          new CustomerCreator().createCustomers(reader, caller, file.getParentFile());
        } finally {
          reader.close();
        }               
      }
    }
  }
}
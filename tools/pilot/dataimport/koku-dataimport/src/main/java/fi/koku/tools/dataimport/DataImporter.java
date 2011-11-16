package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileReader;

import javax.swing.JFileChooser;

import au.com.bytecode.opencsv.CSVReader;

import fi.koku.services.entity.customer.v1.AuditInfoType;
import fi.koku.services.entity.customer.v1.CustomerServiceFactory;
import fi.koku.services.entity.customer.v1.CustomerServicePortType;
import fi.koku.services.entity.customer.v1.CustomerType;

public class DataImporter {

  public static void main(String[] args) throws Exception {
    new DataImporter(args);
  }

  public DataImporter(String[] args) throws Exception {
    if (args.length == 0 || args.length > 1) {
      System.err.println("Please provide the file type. Available types are 'customer' and 'employee' ");
      return;
    }

    JFileChooser chooser = new JFileChooser();
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = chooser.showOpenDialog(null);

    if (result == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      CSVReader reader = new CSVReader(new FileReader(file));

      if ("customer".equalsIgnoreCase(args[0])) {
        WSCaller caller = new WSCaller();
        //caller.communityService();
        //caller.customerService();
        //caller.kahvaService();
        caller.kahvaServiceNew();
      }
      else if ("employee".equalsIgnoreCase(args[0])) {
        new EmployeeWriter().writeEmployeeLDIF(reader);
      }
      else {
        System.err.println("Please provide the file type. Available types are 'customer' and 'employee' ");
      }
    }
  }
}
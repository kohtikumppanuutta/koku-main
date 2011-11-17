package fi.koku.tools.dataimport;

import au.com.bytecode.opencsv.CSVReader;

public class CustomerCreator {

  public void createCustomers(CSVReader reader, WSCaller caller) throws Exception {
    String[] nextLine;
    while ((nextLine = reader.readNext()) != null) {

//      caller.addCustomer();
//      caller.addCustomer();
    }
  }
}
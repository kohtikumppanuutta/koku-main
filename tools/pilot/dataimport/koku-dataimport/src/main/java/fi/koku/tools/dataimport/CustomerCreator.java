package fi.koku.tools.dataimport;

import au.com.bytecode.opencsv.CSVReader;

public class CustomerCreator {

  public void createCustomers(CSVReader reader, WSCaller caller) throws Exception {
    String[] nextLine;
    while ((nextLine = reader.readNext()) != null) {
      System.out.println(nextLine[0]);
      System.out.println(nextLine[1]);
      System.out.println(nextLine[2]);
      System.out.println(nextLine[3]);
      System.out.println(nextLine[4]);
      System.out.println(nextLine[5]);
      System.out.println(nextLine[6]);
      System.out.println(nextLine[7]);
      System.out.println(nextLine[8]);
      System.out.println(nextLine[9]);
      System.out.println(nextLine[10]);
      System.out.println(nextLine[11]);
      System.out.println(nextLine[12]);
      
      //caller.addCustomers(reader)
      caller.addCustomer();
    }
  }
}
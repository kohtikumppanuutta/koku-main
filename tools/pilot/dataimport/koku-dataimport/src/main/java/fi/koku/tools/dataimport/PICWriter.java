package fi.koku.tools.dataimport;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import fi.arcusys.tampere.hrsoa.entity.User;

public class PICWriter {

  private static final String NOT_FOUND_EMPLOYEE_IDS_FILENAME = "NotFoundEmployeeIDS.txt";
  private static final String HELMI_CUSTOMER_PI_CS_FILENAME = "HelmiCustomerPICs.txt";
  private static final String EFFICA_CUSTOMER_PI_CS_FILENAME = "EfficaCustomerPICs.txt";
  private static final String EMPLOYEE_PI_CS_FILENAME = "EmployeePICs.txt";
  private static final int HELMI_PM_2_PIC = 17;
  private static final int HELMI_PM_1_PIC = 16;
  private static final int EMPLOYEE_PIC = 0;

  public void writeEmployeePICFile(CSVReader reader, WSCaller caller, File parent) throws Exception {
    List<String> userPICs = new ArrayList<String>();
    List<String> failedTOAddIDs = new ArrayList<String>();

    String[] l;
    while ((l = reader.readNext()) != null) {

      User user = caller.getUserById(l[EMPLOYEE_PIC]);
      if (user == null) {
        failedTOAddIDs.add(l[EMPLOYEE_PIC]);
      } else {              
        addNotNull(userPICs,user.getSsn());    
      }
    }

    System.out.println("Count: " + userPICs.size());
    Utils.writeIDsToFile(parent, userPICs, EMPLOYEE_PI_CS_FILENAME);
    if (!failedTOAddIDs.isEmpty()) {
      Utils.writeIDsToFile(parent, failedTOAddIDs, NOT_FOUND_EMPLOYEE_IDS_FILENAME);
    }
  }
  
  public void writeEfficaCustomerPICFile(CSVReader reader, WSCaller caller, File parent) throws Exception {
    List<String> customerIDs = new ArrayList<String>();
    String[] l;
    while ((l = reader.readNext()) != null) {      
      addNotNull(customerIDs, l[4]);
      addNotNull(customerIDs, l[16]);
    }
    System.out.println("Count: " + customerIDs.size());
    Utils.writeIDsToFile(parent, customerIDs, EFFICA_CUSTOMER_PI_CS_FILENAME);
  }
    
  public void writeHelmiCustomerPICFile(CSVReader reader, WSCaller caller, File parent) throws Exception {
    List<String> customerIDs = new ArrayList<String>();
    String[] l;
    while ((l = reader.readNext()) != null) {
      String childPIC = Utils.createUID(l[3], l[4]);
      addNotNull(customerIDs, childPIC);
      addNotNull(customerIDs, l[HELMI_PM_1_PIC]);
      addNotNull(customerIDs, l[HELMI_PM_2_PIC]);
    }
    System.out.println("Count: " + customerIDs.size());
    Utils.writeIDsToFile(parent, customerIDs, HELMI_CUSTOMER_PI_CS_FILENAME);
  }
  
  private void addNotNull(Collection<String> col, String toAdd) {
    if (toAdd != null && toAdd.length() > 0 && !col.contains("'"+toAdd.trim()+"'")) {           
      col.add("'"+toAdd.trim()+"'");
    } 
    
//    if (toAdd != null && toAdd.length() > 0 && !col.contains(toAdd.trim())) {           
//      col.add(toAdd.trim());
//    } 
  }  
}
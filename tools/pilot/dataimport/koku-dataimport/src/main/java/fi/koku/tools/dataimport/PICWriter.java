package fi.koku.tools.dataimport;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;

import au.com.bytecode.opencsv.CSVReader;
import fi.arcusys.tampere.hrsoa.entity.User;

public class PICWriter {

  private static final String NOT_FOUND_EMPLOYEE_IDS_FILENAME = "NotFoundEmployeeIDS.txt";
  private static final String HELMI_CUSTOMER_PI_CS_FILENAME = "HelmiCustomerPICs.txt";
  private static final String EFFICA_CUSTOMER_PI_CS_FILENAME = "EfficaCustomerPICs.txt";
  private static final String EMPLOYEE_PI_CS_FILENAME = "EmployeePICs.txt";

  public void writeEmployeePICFile(CSVReader reader, WSCaller caller, File parent) throws Exception {
    Collection<String> userPICs = new LinkedHashSet<String>();
    Collection<String> failedTOAddIDs = new LinkedHashSet<String>();

    String[] l;
    while ((l = reader.readNext()) != null) {

      User user = caller.getUserById(l[Columns.EMPLOYEE_FRMT_1_ID]);
      if (user == null) {
        failedTOAddIDs.add(l[Columns.EMPLOYEE_FRMT_1_ID]);
      } else {
        Utils.addNotNull(userPICs, user.getSsn());
      }
    }

    System.out.println("Count: " + userPICs.size());
    Utils.writeIDsToFile(parent, userPICs, EMPLOYEE_PI_CS_FILENAME);
    if (!failedTOAddIDs.isEmpty()) {
      Utils.writeIDsToFile(parent, failedTOAddIDs, NOT_FOUND_EMPLOYEE_IDS_FILENAME);
    }
  }

  public void writeEfficaCustomerPICFile(CSVReader reader, File parent) throws Exception {
    Collection<String> customerIDs = new LinkedHashSet<String>();
    String[] l;
    while ((l = reader.readNext()) != null) {
      Utils.addNotNull(customerIDs, l[Columns.EFFICA_CHILD_PIC]);
      Utils.addNotNull(customerIDs, l[Columns.EFFICA_PM_PIC]);
      Utils.addNotNull(customerIDs, l[Columns.EFFICA_PM_2_PIC]);
    }
    System.out.println("Count: " + customerIDs.size());
    Utils.writeIDsToFile(parent, customerIDs, EFFICA_CUSTOMER_PI_CS_FILENAME);
  }

  public void writeHelmiCustomerPICFile(CSVReader reader, File parent) throws Exception {
    Collection<String> customerIDs = new LinkedHashSet<String>();
    String[] l;
    while ((l = reader.readNext()) != null) {

      String childPIC = Utils.createUID(l[Columns.HELMI_CHILD_UID_POSTFIX], l[Columns.HELMI_CHILD_BIRTHDATE]);
      Utils.addNotNull(customerIDs, childPIC);
      Utils.addNotNull(customerIDs, l[Columns.HELMI_PM_1_PIC]);
      Utils.addNotNull(customerIDs, l[Columns.HELMI_PM_2_PIC]);
    }
    System.out.println("Count: " + customerIDs.size());
    Utils.writeIDsToFile(parent, customerIDs, HELMI_CUSTOMER_PI_CS_FILENAME);
  }  
}
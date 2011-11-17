package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import fi.arcusys.tampere.hrsoa.entity.User;

public class LDIFWriter {

  private static final String VIRKAILIJA = "virkailija";
  private static final String KUNTALAINEN = "Kuntalainen";
  private static final String CUSTOMER_GROUP_LDIF_FILE = "CustomerGroup.ldif";
  private static final String CUSTOMER_LDIF_FILE = "Customer.ldif";
  private static final String EMPLOYEE_GROUP_LDIF_FILE = "EmployeeGroup.ldif";
  private static final String EMPLOYEE_LDIF_FILE = "Employee.ldif";

  public void writeEmployeeLDIF(CSVReader reader, WSCaller caller, File parent) throws Exception {
    List<String> userIDs = new ArrayList<String>();        
    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(parent, EMPLOYEE_LDIF_FILE));

      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        User user = caller.getUserById(nextLine[0]);
        // System.out.println(nextLine[0]);
        writePersonLDIF(writer, user.getUserId(), user.getFirstName(),
            user.getLastName(), user.getEmail(), user.getSsn());
        userIDs.add(user.getUserId());
      }

      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }             
    System.out.println("Employee LDIF written successfully");
    
    try {
      writer = new FileWriter(new File(parent, EMPLOYEE_GROUP_LDIF_FILE));      
      writeGroupLDIF(writer, VIRKAILIJA, userIDs);
      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    System.out.println("Employee Group LDIF written successfully");
  }
  
  public void writeCustomerLDIF(CSVReader reader, File parent) throws Exception {
    List<String> userIDs = new ArrayList<String>();        
    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(parent, CUSTOMER_LDIF_FILE));

      String[] nextLine;
      while ((nextLine = reader.readNext()) != null) {
        //User user = caller.getUserById(nextLine[0]);
        // System.out.println(nextLine[0]);
//        writePersonLDIF(writer, user.getUserId(), user.getFirstName(),
//            user.getLastName(), user.getEmail(), user.getSsn());
//        userIDs.add(user.getUserId());
      }
      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }             
    System.out.println("Customer LDIF written successfully");
    
    try {
      writer = new FileWriter(new File(parent, CUSTOMER_GROUP_LDIF_FILE));      
      writeGroupLDIF(writer, KUNTALAINEN, userIDs);
      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    System.out.println("Customer Group LDIF written successfully");
  }
  
  /**
   Speksi
   # Virkailija, Groups, koku, example.org
   dn: cn=${listType},ou=Groups,o=koku,dc=example,dc=org
   cn: ${listType}
   objectClass: groupOfNames
   objectClass: top
   member: dn: cn=${portal.user.name},ou=People,o=koku,dc=example,dc=org 
   */
  private void writeGroupLDIF(FileWriter writer, String listType, List<String> userIDs) throws Exception{
    writer.write("dn: cn=" + listType + ",ou=Groups,o=koku,dc=example,dc=org" + "\n");
    writer.write("cn: " + listType + "\n");
    writer.write("objectClass: groupOfNames" + "\n");
    writer.write("objectClass: top" + "\n");      
    for (String userID : userIDs) {
      writer.write("member: dn: cn=" + userID + ",ou=People,o=koku,dc=example,dc=org" + "\n");
    }
    writer.write("\n");    
  }
  

  /**
    Speksi:  
    # kalle.kuntalainen, People, koku, example.org
    dn: cn=${portal.user.name},ou=People,o=koku,dc=example,dc=org
    cn: ${portal.user.name}
    givenName: ${user.firstname}
    objectClass: inetOrgPerson
    objectClass: top
    sn: ${user.lasname}
    userPassword: test
    mail: ${user.email}
    uid: ${user.pic}
  */
  private void writePersonLDIF(FileWriter writer, String userId, String firstName, String lastName,
      String email, String uid) throws Exception {
    writer.write("dn: cn=" + userId + ",ou=People,o=koku,dc=example,dc=org" + "\n");
    writer.write("cn:" + userId + "\n");
    writer.write("givenName:" + firstName + "\n");
    writer.write("objectClass: inetOrgPerson" + "\n");
    writer.write("objectClass: top" + "\n");
    writer.write("sn: " + lastName + "\n");
    writer.write("userPassword: test" + "\n");
    writer.write("mail: " + email + "\n");
    writer.write("uid: " + uid + "\n");
    writer.write("\n");
  }
}

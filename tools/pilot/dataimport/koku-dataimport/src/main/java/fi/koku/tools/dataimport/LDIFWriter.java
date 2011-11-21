package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import au.com.bytecode.opencsv.CSVReader;
import fi.arcusys.tampere.hrsoa.entity.User;
import fi.koku.services.entity.customer.v1.PhoneNumbersType;

public class LDIFWriter {

  private static final int EFFICA_PM_PHONE = 24;
  private static final int EFFICA_PM_GSM = 25;
  private static final int EFFICA_CHILD_UID = 4;
  private static final int EFFICA_CHILD_TEL = 15;
  private static final int EFFICA_CHILD_LASTNAME = 6;
  private static final int EFFICA_CHILD_FIRSTNAMES = 7;
  private static final int EFFICA_PM_UID = 16;
  private static final int EFFICA_PM_EMAIL = 22;
  private static final int EFFICA_PM_LASTNAME = 17;
  private static final int EFFICA_PM_FIRSTNAMES = 18;
  

  private static final int HELMI_PM_1_UID = 16;
  private static final int HELMI_PM_2_UID = 17;
  
  private static final String VIRKAILIJA = "virkailija";
  private static final String KUNTALAINEN = "kuntalainen";  
  private static final String EFFICA_CUSTOMER_GROUP_LDIF_FILE = "EfficaCustomerGroup.ldif";
  private static final String EFFICA_CUSTOMER_LDIF_FILE = "EfficaCustomer.ldif";
  private static final String HELMI_CUSTOMER_GROUP_LDIF_FILE = "HelmiCustomerGroup.ldif";
  private static final String HELMI_CUSTOMER_LDIF_FILE = "HelmiCustomer.ldif";
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
        // TODO is user.getUserId() the correct id for this case?
        writePersonLDIF(writer, user.getUserId(), user.getFirstName(), user.getLastName(), null, user.getEmail(),
            user.getSsn());
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

  public void writeEfficaCustomerLDIF(CSVReader reader, File parent) throws Exception {
    List<String> addedUserIDs = new ArrayList<String>();
    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(parent, EFFICA_CUSTOMER_LDIF_FILE));

      String[] l;
      while ((l = reader.readNext()) != null) {

        // lapsi
        writePersonLDIF(writer, l[EFFICA_CHILD_UID], getFirstName(l[EFFICA_CHILD_FIRSTNAMES]),
            l[EFFICA_CHILD_LASTNAME], l[EFFICA_CHILD_TEL], null, l[EFFICA_CHILD_UID]);
        addedUserIDs.add(l[EFFICA_CHILD_UID]);

        // päähenkilö
        if (!addedUserIDs.contains(l[EFFICA_PM_UID])) {
          // TODO nextLine[23] voi sisältää toisen sähköpostiosoitteen, voiko
          // sen lisätä LDIF:iin?
          writePersonLDIF(writer, l[EFFICA_PM_UID], getFirstName(l[EFFICA_PM_FIRSTNAMES]), l[EFFICA_PM_LASTNAME],
              getPreferredPhone(l[EFFICA_PM_GSM], l[EFFICA_PM_PHONE]), l[EFFICA_PM_EMAIL], l[EFFICA_PM_UID]);
          addedUserIDs.add(l[EFFICA_PM_UID]);
        }

        // puoliso
        // TODO nextLine[30] voi sisältää toisen sähköpostiosoitteen, voiko sen
        // lisätä LDIF:iin?
        // writePersonLDIF(writer, "PUUTTUUU", getFirstName(nextLine[27]),
        // nextLine[26], nextLine[29], null);
        // userIDs.add("PUUTTUUU");
      }
      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    System.out.println("Customer LDIF written successfully");

    try {
      writer = new FileWriter(new File(parent, EFFICA_CUSTOMER_GROUP_LDIF_FILE));
      writeGroupLDIF(writer, KUNTALAINEN, addedUserIDs);
      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    System.out.println("Customer Group LDIF written successfully");
  }
  
  private String getPreferredPhone(String phone1, String phone2) {    
    if (phone1 != null && phone1.length() > 0) {
      return phone1;
    } else if (phone2 != null && phone2.length() > 0) {
      return phone2;
    }
    
    return null;
  }

  public void writeHelmiCustomerLDIF(CSVReader reader, File parent) throws Exception{
    List<String> addedUserIDs = new ArrayList<String>();
    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(parent, HELMI_CUSTOMER_LDIF_FILE));

      String[] l;
      while ((l = reader.readNext()) != null) {
        
        String childUID = createUID(l[3], l[4]);
             
        // lapsi
        writePersonLDIF(writer, childUID, getFirstName(l[5]), l[7], null, null, childUID);
        addedUserIDs.add(childUID);
        
        String[] phones = splitLines(l[13]);
        
        // TODO Tässä on buki, kun on vain yksi email osoite
        String[] emails = splitLines(l[14]);
        String[] guardians = splitLines(l[15]);     
                       
        // päähenkilö
        if (!addedUserIDs.contains(l[HELMI_PM_1_UID])) {
          // this is not a copy paste mistake, the Helmi data has
          // the last name first
          String lastName = getFirstName(guardians[0]);
          String firstName = getSecondName(guardians[0]);
          writePersonLDIF(writer, l[HELMI_PM_1_UID], firstName, lastName, phones[0], emails[0], l[HELMI_PM_1_UID]);
          addedUserIDs.add(l[HELMI_PM_1_UID]);
        }
                
        // toinen huoltaja
        if (guardians[1] != null && guardians[1].length() > 0 && !addedUserIDs.contains(l[HELMI_PM_2_UID])) {
          // this is not a copy paste mistake, the Helmi data has
          // the last name first
          String lastName = getFirstName(guardians[1]);
          String firstName = getSecondName(guardians[1]);
          writePersonLDIF(writer, l[HELMI_PM_2_UID], firstName, lastName, phones[0], emails[1], l[HELMI_PM_2_UID]);
          addedUserIDs.add(l[HELMI_PM_2_UID]);
        }      
      }
      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    System.out.println("Customer LDIF written successfully");

    try {
      writer = new FileWriter(new File(parent, HELMI_CUSTOMER_GROUP_LDIF_FILE));
      writeGroupLDIF(writer, KUNTALAINEN, addedUserIDs);
      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    System.out.println("Customer Group LDIF written successfully");    
  }

  private String createUID(String UID_postfix, String childBirthDate) {    
    String shortened = parseBirthDate(childBirthDate);
    String[] parts = childBirthDate.split("\\."); 
    String separator;
    
    if(Integer.parseInt(parts[2]) > 1999){
      separator = "A";
    }
    else{
      separator = "-";
    }
    return shortened + separator + UID_postfix;
  }

  private String parseBirthDate(String birthDateString) {
    String[] parts = birthDateString.split("\\.");
    String date = "";

    if (parts[0].length() == 1) {
      date = "0" + parts[0];
    }
    else{
      date = parts[0];
    }
    
    if (parts[1].length() == 1) {
      date = date + "0" + parts[1];
    }
    else{
      date = date + parts[1];
    } 
    
    date = date + parts[2].substring(2, 4);
    return date;
  }

  private String[] splitLines(String source) {  
    if (source == null) {     
      return new String[] { null, null };
    }

    if (source.contains("\n")) {
      return source.split("\n");
    }
    return new String[] { source, null };
  }

  private String getFirstName(String names) throws Exception {
    if (names == null) {
      throw new Exception("Names cannot be null");
    }
    // remove leading and trailing whitespace
    names = names.trim();

    if (names.contains(" ")) {
      return names.substring(0, names.indexOf(" "));
    }
    return names;
  }
  
  private String getSecondName(String names) throws Exception {
    if (names == null) {
      throw new Exception("Names cannot be null");
    }
    // remove leading and trailing whitespace
    names = names.trim();

    if (names.contains(" ")) {
      String temp = names.substring(names.indexOf(" "));           
      return temp.trim();
    }
    return names;
  }

  /**
   * Speksi
   * # Virkailija, Groups, koku, example.org dn:
   * cn=${listType},ou=Groups,o=koku,dc=example,dc=org
   * cn: ${listType}
   * objectClass: groupOfNames 
   * objectClass: top
   * member: cn=${portal.user.name},ou=People,o=koku,dc=example,dc=org
   */
  private void writeGroupLDIF(FileWriter writer, String listType, List<String> userIDs) throws Exception {
    writer.write("dn: cn=" + listType + ",ou=Groups,o=koku,dc=example,dc=org" + "\n");
    writer.write("cn: " + listType + "\n");
    writer.write("objectClass: groupOfNames" + "\n");
    writer.write("objectClass: top" + "\n");
    for (String userID : userIDs) {
      writer.write("member: cn=" + userID + ",ou=People,o=koku,dc=example,dc=org" + "\n");
    }
    writer.write("\n");
  }

  /**
   * Speksi: # kalle.kuntalainen, People, koku, example.org dn:
   * cn=${portal.user.name},ou=People,o=koku,dc=example,dc=org cn:
   * ${portal.user.name} givenName: ${user.firstname} objectClass: inetOrgPerson
   * objectClass: top sn: ${user.lasname} userPassword: test mail: ${user.email}
   * uid: ${user.pic}
   */
  private void writePersonLDIF(FileWriter writer, String userId, String firstName, String lastName, String tel, String email,
      String uid) throws Exception {
    writer.write("dn: cn=" + userId + ",ou=People,o=koku,dc=example,dc=org" + "\n");
    writer.write("cn: " + userId + "\n");
    writer.write("givenName: " + firstName + "\n");
    writer.write("objectClass: inetOrgPerson" + "\n");
    writer.write("objectClass: top" + "\n");
    writer.write("sn: " + lastName + "\n");
    writer.write("userPassword: test" + "\n");
    if (tel != null && tel.length() != 0) {
      writer.write("telephoneNumber: " + tel + "\n");
    }  
    if (email != null && email.length() != 0) {
      writer.write("mail: " + email + "\n");
    }
    writer.write("uid: " + uid + "\n");    
    writer.write("\n");
  } 
}

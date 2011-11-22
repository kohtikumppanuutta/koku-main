package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;
import fi.arcusys.tampere.hrsoa.entity.User;
import fi.koku.services.entity.customer.v1.PhoneNumbersType;

public class LDIFWriter {

  private static final int HELMI_CHILD_GROUP = 19;
  private static final int HELMI_CHILD_UNIT = 18;
  private static final int HELMI_PMS = 15;
  private static final int HELMI_PM_EMAILS = 14;
  private static final int HELMI_PM_PHONES = 13;
  private static final int HELMI_CHILD_LASTNAME = 7;
  private static final int HELMI_CHILD_FIRSTNAMES = 6;
  private static final int HELMI_CHILD_BIRTHDATE = 4;
  private static final int HELMI_CHILD_UID_POSTFIX = 3;
  private static final int HELMI_PM_1_UID = 16;
  private static final int HELMI_PM_2_UID = 17;

  private static final int EFFICA_CHILD_UNIT = 2;
  private static final int EFFICA_CHILD_GROUP = 3;
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

  private static final String VIRKAILIJA = "virkailija";
  private static final String KUNTALAINEN = "kuntalainen";
  private static final String EFFICA_CUSTOMER_GROUP_LDIF_FILE = "EfficaCustomerGroup.ldif";
  private static final String EFFICA_CUSTOMER_LDIF_FILE = "EfficaCustomer.ldif";
  private static final String HELMI_CUSTOMER_GROUP_LDIF_FILE = "HelmiCustomerGroup.ldif";
  private static final String HELMI_CUSTOMER_LDIF_FILE = "HelmiCustomer.ldif";
  private static final String EMPLOYEE_GROUP_LDIF_FILE = "EmployeeGroup.ldif";
  private static final String EMPLOYEE_LDIF_FILE = "Employee.ldif";
  private static final String NOT_FOUND_EMPLOYEE_IDS_TXT = "NotFoundEmployeeIDS.txt";

  public void writeEmployeeLDIF(CSVReader reader, WSCaller caller, File parent) throws Exception {
    List<String> userIDs = new ArrayList<String>();
    List<String> failedTOAddIDs = new ArrayList<String>();
    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(parent, EMPLOYEE_LDIF_FILE));

      String[] l;
      while ((l = reader.readNext()) != null) {
        
        // remove whitespace
        for (int i = 0; i < l.length; i++) {
          l[i] = l[i].trim();
        }  
        
        User user = caller.getUserById(l[0]);
        if (user == null) {
          failedTOAddIDs.add(l[0]);
        } else {
          // try to avoid duplicates
          if (!userIDs.contains(user.getUserId())) {
            // TODO is user.getUserId() the correct id for this case?
            writePersonLDIF(writer, user.getUserId(), user.getFirstName(), user.getLastName(), null, user.getEmail(),
                user.getSsn());
            userIDs.add(user.getUserId());
          }
        }
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
    
    if (failedTOAddIDs.size() > 0) {
      Utils.writeIDsToFile(parent, failedTOAddIDs, NOT_FOUND_EMPLOYEE_IDS_TXT);
    }
  }

  public void writeEfficaCustomerLDIF(CSVReader reader, File parent) throws Exception {
    List<String> addedUserIDs = new ArrayList<String>();
    Map<String, List<String>> childGroupToUIDs = new HashMap<String, List<String>>();

    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(parent, EFFICA_CUSTOMER_LDIF_FILE));

      String[] l;
      while ((l = reader.readNext()) != null) {

        // remove whitespace
        for (int i = 0; i < l.length; i++) {
          l[i] = l[i].trim();
        }  
                       
        // lapsi
        if (!addedUserIDs.contains(l[EFFICA_CHILD_UID])) {
          writePersonLDIF(writer, l[EFFICA_CHILD_UID], Utils.getFirstName(l[EFFICA_CHILD_FIRSTNAMES]),
              l[EFFICA_CHILD_LASTNAME], l[EFFICA_CHILD_TEL], null, l[EFFICA_CHILD_UID]);
          addedUserIDs.add(l[EFFICA_CHILD_UID]);
        }
        
        String groupID = l[EFFICA_CHILD_UNIT] + ", " + l[EFFICA_CHILD_GROUP];
        List<String> groupUIDs = childGroupToUIDs.get(groupID);
        if (groupUIDs == null) {
          groupUIDs = new ArrayList<String>();
        }        
        if (!groupUIDs.contains(l[EFFICA_CHILD_UID])) {
          groupUIDs.add(l[EFFICA_CHILD_UID]);
        }
        childGroupToUIDs.put(groupID, groupUIDs);

        // päähenkilö
        if (!addedUserIDs.contains(l[EFFICA_PM_UID])) {
          // TODO l[23] voi sisältää toisen sähköpostiosoitteen, voiko
          // sen lisätä LDIF:iin?
          writePersonLDIF(writer, l[EFFICA_PM_UID], Utils.getFirstName(l[EFFICA_PM_FIRSTNAMES]), l[EFFICA_PM_LASTNAME],
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

    writeChildGroupLDIFs(parent, childGroupToUIDs);
  }

  public void writeHelmiCustomerLDIF(CSVReader reader, File parent) throws Exception {
    List<String> addedUserIDs = new ArrayList<String>();
    Map<String, List<String>> childGroupToUIDs = new HashMap<String, List<String>>();
    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(parent, HELMI_CUSTOMER_LDIF_FILE));

      String[] l;
      while ((l = reader.readNext()) != null) {       
        
        String[] phones = Utils.splitLines(l[HELMI_PM_PHONES]);
        String[] emails = Utils.splitLines(l[HELMI_PM_EMAILS]);
        String[] guardians = Utils.splitLines(l[HELMI_PMS]);
        
        // remove whitespace
        for (int i = 0; i < l.length; i++) {
          l[i] = l[i].trim();
        }  

        String childUID = Utils.createUID(l[HELMI_CHILD_UID_POSTFIX], l[HELMI_CHILD_BIRTHDATE]);

        // lapsi
        if (!addedUserIDs.contains(childUID)) {
          writePersonLDIF(writer, childUID, Utils.getFirstName(l[HELMI_CHILD_FIRSTNAMES]), l[HELMI_CHILD_LASTNAME],
              null, null, childUID);
          addedUserIDs.add(childUID);
        }

        String groupID = l[HELMI_CHILD_UNIT] + ", " + l[HELMI_CHILD_GROUP];
        List<String> groupUIDs = childGroupToUIDs.get(groupID);
        if (groupUIDs == null) {
          groupUIDs = new ArrayList<String>();
        }
        
        if (!groupUIDs.contains(childUID)) {
          groupUIDs.add(childUID);
        }
        childGroupToUIDs.put(groupID, groupUIDs);
        
        // päähenkilö
        if (!addedUserIDs.contains(l[HELMI_PM_1_UID])) {
          // this is not a copy paste mistake, the Helmi data has
          // the last name first
          String lastName = Utils.getFirstName(guardians[0]);
          String firstNames = Utils.getSecondName(guardians[0]);
          writePersonLDIF(writer, l[HELMI_PM_1_UID], Utils.getFirstName(firstNames), lastName, phones[0], emails[0],
              l[HELMI_PM_1_UID]);
          addedUserIDs.add(l[HELMI_PM_1_UID]);
        }

        // toinen huoltaja
        if (guardians[1] != null && guardians[1].length() > 0 && !addedUserIDs.contains(l[HELMI_PM_2_UID])) {
          // this is not a copy paste mistake, the Helmi data has
          // the last name first
          String lastName = Utils.getFirstName(guardians[1]);
          String firstNames = Utils.getSecondName(guardians[1]);
          writePersonLDIF(writer, l[HELMI_PM_2_UID], Utils.getFirstName(firstNames), lastName, phones[1], emails[1],
              l[HELMI_PM_2_UID]);
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

    writeChildGroupLDIFs(parent, childGroupToUIDs);
  }

  private void writeChildGroupLDIFs(File parent, Map<String, List<String>> childGroupToUIDs) throws IOException,
      Exception {
    FileWriter writer = null;
    for (String groupName : childGroupToUIDs.keySet()) {
      List<String> groupUIDs = childGroupToUIDs.get(groupName);

      try {
        writer = new FileWriter(new File(parent, groupName + ".ldif"));
        writeGroupLDIF(writer, groupName, groupUIDs);
        writer.close();
      } finally {
        if (writer != null) {
          writer.close();
        }
      }
    }
    System.out.println("Child Group LDIFs written successfully");
  }

  private String getPreferredPhone(String phone1, String phone2) {
    if (phone1 != null && phone1.length() > 0) {
      return phone1;
    } else if (phone2 != null && phone2.length() > 0) {
      return phone2;
    }

    return null;
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

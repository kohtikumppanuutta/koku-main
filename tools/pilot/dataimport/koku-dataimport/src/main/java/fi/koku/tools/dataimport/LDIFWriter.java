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

public class LDIFWriter {

  private static final String REGISTRIES = "Registries";
  private static final String ORG_UNITS = "OrgUnits";
  private static final String KK_SERVICEAREA_SCHOOL_HEALTH = "kk.servicearea.schoolHealth";
  private static final String KK_SERVICEAREA_DAYCARE = "kk.servicearea.daycare";
  private static final String KK_SERVICEAREA_CHILD_HEALTH = "kk.servicearea.childHealth";
  private static final String DAYCAREREGISTRY = "daycareregistry";
  private static final String HEALTHCAREREGISTRY = "healthcareregistry";
  private static final String EMPLOYEE_ORG_UNIT_FILENAME_PREFIX = "employeeOrgUnit_";
  private static final String LDIF_FILE_SUFFIX = ".ldif";
  private static final String EMPLOYEE_REGISTRY_FILENAME_PREFIX = "employeeRegistry_";
  private static final String KOULUTERVEYDENHUOLLON_TYÖNTEKIJÄ = "Kouluterveydenhuollon työntekijä";
  private static final String PÄIVÄKODIN_TYÖNTEKIJÄ = "Päiväkodin työntekijä";
  private static final String NEUVOLAN_TYÖNTEKIJÄ = "Neuvolan työntekijä";
  
  private static final int EMPLOYEE_ID = 0;
  private static final int EMPLOYEE_GROUP = 1;
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
  private static final String EFFICA_ALL_LDIF_FILE = "Effica_All.ldif";
  private static final String HELMI_ALL_LDIF_FILE = "Helmi_All.ldif";
  private static final String HELMI_CUSTOMER_GROUP_LDIF_FILE = "HelmiCustomerGroup.ldif";
  private static final String HELMI_CUSTOMER_LDIF_FILE = "HelmiCustomer.ldif";
  private static final String EMPLOYEE_GROUP_LDIF_FILE = "EmployeeGroup.ldif";
  private static final String EMPLOYEE_LDIF_FILE = "Employee.ldif";
  private static final String EMPLOYEE_ALL_LDIF_FILE = "Employee_all.ldif";
  private static final String NOT_FOUND_EMPLOYEE_IDS_TXT = "NotFoundEmployeeIDS.txt";

  public void writeEmployeeLDIF(CSVReader reader, WSCaller caller, File parent) throws Exception {
    List<String> userIDs = new ArrayList<String>();
    Map<String, List<String>> groupToUsers = new HashMap<String, List<String>>();
    List<String> failedTOAddIDs = new ArrayList<String>();
    FileWriter writer = null;
    FileWriter allWriter = null;
    FileWriter structureWriter = null;

    try {
      allWriter = new FileWriter(new File(parent, EMPLOYEE_ALL_LDIF_FILE));
      structureWriter = new FileWriter(new File(parent, "Employee_structure.ldif"));

      try {
        writer = new FileWriter(new File(parent, EMPLOYEE_LDIF_FILE));

        String[] l;
        while ((l = reader.readNext()) != null) {

          // remove whitespace
          for (int i = 0; i < l.length; i++) {
            l[i] = l[i].trim();
          }

          User user = caller.getUserById(l[EMPLOYEE_ID]);
          if (user == null) {
            failedTOAddIDs.add(l[EMPLOYEE_ID]);
          } else {
            // try to avoid duplicates
            if (!userIDs.contains(user.getUserId())) {
              // TODO is user.getUserId() the correct id for this case?
              writePersonLDIF(writer, allWriter, user.getUserId(), user.getFirstName(), user.getLastName(), null, user.getEmail(),
                  user.getSsn());              
              userIDs.add(user.getUserId());

              List<String> users = groupToUsers.get(l[EMPLOYEE_GROUP]);
              if (users == null) {
                users = new ArrayList<String>();
              }

              if (!users.contains(user.getUserId())) {
                users.add(user.getUserId());
              }
              groupToUsers.put(l[EMPLOYEE_GROUP], users);
            }
          }
        }

        writer.close();
      } finally {
        if (writer != null) {
          writer.close();
        }
      }
      System.out.println("Employee LDIF written");

      try {
        writer = new FileWriter(new File(parent, EMPLOYEE_GROUP_LDIF_FILE));
        writeGroupsLDIF(writer, allWriter, VIRKAILIJA, userIDs);
        writeGroupsStructureLDIF(structureWriter, VIRKAILIJA);
        writer.close();
      } finally {
        if (writer != null) {
          writer.close();
        }
      }
      System.out.println("Employee Group LDIF written");

      for (String group : groupToUsers.keySet()) {

        if (NEUVOLAN_TYÖNTEKIJÄ.equals(group) || PÄIVÄKODIN_TYÖNTEKIJÄ.equals(group)
            || KOULUTERVEYDENHUOLLON_TYÖNTEKIJÄ.equals(group)) {

          List<String> users = groupToUsers.get(group);

          try {
            writer = new FileWriter(new File(parent, EMPLOYEE_REGISTRY_FILENAME_PREFIX + group + LDIF_FILE_SUFFIX));

            if (NEUVOLAN_TYÖNTEKIJÄ.equals(group)) {
              writeKokuCommunitiesLDIF(writer, allWriter, HEALTHCAREREGISTRY, REGISTRIES, users);
              writeKokuCommunitiesStructureLDIF(structureWriter, HEALTHCAREREGISTRY, REGISTRIES);
            } else if (PÄIVÄKODIN_TYÖNTEKIJÄ.equals(group)) {
              writeKokuCommunitiesLDIF(writer, allWriter, DAYCAREREGISTRY, REGISTRIES, users);
              writeKokuCommunitiesStructureLDIF(structureWriter, DAYCAREREGISTRY, REGISTRIES);
            } else if (KOULUTERVEYDENHUOLLON_TYÖNTEKIJÄ.equals(group)) {
              writeKokuCommunitiesLDIF(writer, allWriter, HEALTHCAREREGISTRY, REGISTRIES, users);
              writeKokuCommunitiesStructureLDIF(structureWriter, HEALTHCAREREGISTRY, REGISTRIES);
            }

            writer.close();
          } finally {
            if (writer != null) {
              writer.close();
            }
          }

          try {
            writer = new FileWriter(new File(parent, EMPLOYEE_ORG_UNIT_FILENAME_PREFIX + group + LDIF_FILE_SUFFIX));

            if (NEUVOLAN_TYÖNTEKIJÄ.equals(group)) {
              writeKokuCommunitiesLDIF(writer, allWriter, KK_SERVICEAREA_CHILD_HEALTH, ORG_UNITS, users);
              writeKokuCommunitiesStructureLDIF(structureWriter, KK_SERVICEAREA_CHILD_HEALTH, ORG_UNITS);
            } else if (PÄIVÄKODIN_TYÖNTEKIJÄ.equals(group)) {
              writeKokuCommunitiesLDIF(writer, allWriter, KK_SERVICEAREA_DAYCARE, ORG_UNITS, users);
              writeKokuCommunitiesStructureLDIF(structureWriter, KK_SERVICEAREA_DAYCARE, ORG_UNITS);
            } else if (KOULUTERVEYDENHUOLLON_TYÖNTEKIJÄ.equals(group)) {
              writeKokuCommunitiesLDIF(writer, allWriter, KK_SERVICEAREA_SCHOOL_HEALTH, ORG_UNITS, users);
              writeKokuCommunitiesStructureLDIF(structureWriter, KK_SERVICEAREA_SCHOOL_HEALTH, ORG_UNITS);
            }

            writer.close();
          } finally {
            if (writer != null) {
              writer.close();
            }
          }
        }
      }

      allWriter.close();
      structureWriter.close();
    } finally {
      if (allWriter != null) {
        allWriter.close();
      }
      
      if (structureWriter != null) {
        structureWriter.close();
      }
    }
    
    System.out.println("Employee ALL LDIF written");    
    System.out.println("Employee Registry LDIFs written");
    System.out.println("Employee OrgUnit LDIFs written");
      
    if (failedTOAddIDs.size() > 0) {
      Utils.writeIDsToFile(parent, failedTOAddIDs, NOT_FOUND_EMPLOYEE_IDS_TXT);
    }
  }

  public void writeEfficaCustomerLDIF(CSVReader reader, File parent) throws Exception {
    List<String> addedUserIDs = new ArrayList<String>();
    Map<String, List<String>> childGroupToUIDs = new HashMap<String, List<String>>();

    FileWriter writer = null;
    FileWriter allWriter = null;
    FileWriter structureWriter = null;

    try {
      allWriter = new FileWriter(new File(parent, EFFICA_ALL_LDIF_FILE));
      structureWriter = new FileWriter(new File(parent, "Effica_customer_structure.ldif"));

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
            writePersonLDIF(writer, allWriter, l[EFFICA_CHILD_UID], Utils.getFirstName(l[EFFICA_CHILD_FIRSTNAMES]),
                l[EFFICA_CHILD_LASTNAME], l[EFFICA_CHILD_TEL], null, l[EFFICA_CHILD_UID]);            
            addedUserIDs.add(l[EFFICA_CHILD_UID]);
          }

          String groupID = l[EFFICA_CHILD_UNIT] + " " + l[EFFICA_CHILD_GROUP];
          groupID = groupID.replaceAll(",", "");
          groupID = groupID.replaceAll("\\+", "");
          
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
            writePersonLDIF(writer, allWriter, l[EFFICA_PM_UID], Utils.getFirstName(l[EFFICA_PM_FIRSTNAMES]),
                l[EFFICA_PM_LASTNAME], getPreferredPhone(l[EFFICA_PM_GSM], l[EFFICA_PM_PHONE]), l[EFFICA_PM_EMAIL],
                l[EFFICA_PM_UID]);           
            addedUserIDs.add(l[EFFICA_PM_UID]);
          }

          // puoliso
          // TODO nextLine[30] voi sisältää toisen sähköpostiosoitteen, voiko
          // sen
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
      System.out.println("Effica Customer LDIF written");

      try {
        writer = new FileWriter(new File(parent, EFFICA_CUSTOMER_GROUP_LDIF_FILE));
        writeGroupsLDIF(writer, allWriter, KUNTALAINEN, addedUserIDs);
        writeGroupsStructureLDIF(structureWriter, KUNTALAINEN);
        writer.close();
      } finally {
        if (writer != null) {
          writer.close();
        }
      }
      System.out.println("Effica Customer Group LDIF written");

      writeChildGroupLDIFs(parent, allWriter, structureWriter, childGroupToUIDs);

      allWriter.close();
      structureWriter.close();
    } finally {
      if (allWriter != null) {
        allWriter.close();
      }
      
      if (structureWriter != null) {
        structureWriter.close();
      }
    }
    System.out.println("Effica all LDIF written");
  }

  public void writeHelmiCustomerLDIF(CSVReader reader, File parent) throws Exception {
    List<String> addedUserIDs = new ArrayList<String>();
    Map<String, List<String>> childGroupToUIDs = new HashMap<String, List<String>>();
    FileWriter writer = null;
    FileWriter allWriter = null;
    FileWriter structureWriter = null;

    try {
      allWriter = new FileWriter(new File(parent, HELMI_ALL_LDIF_FILE));
      structureWriter = new FileWriter(new File(parent, "Helmi_customer_structure.ldif"));
      
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
            writePersonLDIF(writer, allWriter, childUID, Utils.getFirstName(l[HELMI_CHILD_FIRSTNAMES]), l[HELMI_CHILD_LASTNAME],
                null, null, childUID);            
            addedUserIDs.add(childUID);
          }

          String groupID = l[HELMI_CHILD_UNIT] + " " + l[HELMI_CHILD_GROUP];
          groupID = groupID.replaceAll(",", "");
          groupID = groupID.replaceAll("\\+", "");
          
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
            writePersonLDIF(writer, allWriter, l[HELMI_PM_1_UID], Utils.getFirstName(firstNames), lastName, phones[0], emails[0],
                l[HELMI_PM_1_UID]);
            addedUserIDs.add(l[HELMI_PM_1_UID]);
          }

          // toinen huoltaja
          if (guardians[1] != null && guardians[1].length() > 0 && 
              l[HELMI_PM_2_UID] != null && l[HELMI_PM_2_UID].length() > 0 &&
              !addedUserIDs.contains(l[HELMI_PM_2_UID])) {
            // this is not a copy paste mistake, the Helmi data has
            // the last name first
            String lastName = Utils.getFirstName(guardians[1]);
            String firstNames = Utils.getSecondName(guardians[1]);
            writePersonLDIF(writer, allWriter, l[HELMI_PM_2_UID], Utils.getFirstName(firstNames), lastName, phones[1], emails[1],
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
      System.out.println("Customer LDIF written");

      try {
        writer = new FileWriter(new File(parent, HELMI_CUSTOMER_GROUP_LDIF_FILE));
        writeGroupsLDIF(writer, allWriter, KUNTALAINEN, addedUserIDs);
        writeGroupsStructureLDIF(structureWriter, KUNTALAINEN);
        writer.close();
      } finally {
        if (writer != null) {
          writer.close();
        }
      }
      System.out.println("Customer Group LDIF written");

      writeChildGroupLDIFs(parent, allWriter, structureWriter, childGroupToUIDs);

      allWriter.close();
      structureWriter.close();
    } finally {
      if (allWriter != null) {
        allWriter.close();
      }
      
      if (structureWriter != null) {
        structureWriter.close();
      }
    }
  }

  private void writeChildGroupLDIFs(File parent, FileWriter allWriter, FileWriter structureWriter,
      Map<String, List<String>> childGroupToUIDs) throws IOException,    Exception {
    FileWriter writer = null;
    for (String groupName : childGroupToUIDs.keySet()) {
      List<String> groupUIDs = childGroupToUIDs.get(groupName);

      try {
        writer = new FileWriter(new File(parent, groupName + LDIF_FILE_SUFFIX));
        writeCommunitiesGroupsLDIF(writer, allWriter, groupName, groupUIDs);   
        writeCommunitiesGroupsStructureLDIF(structureWriter, groupName);
        writer.close();
      } finally {
        if (writer != null) {
          writer.close();
        }
      }
    }
    System.out.println("Child Group LDIFs written");
  }

  private String getPreferredPhone(String phone1, String phone2) {
    if (phone1 != null && phone1.length() > 0) {
      return phone1;
    } else if (phone2 != null && phone2.length() > 0) {
      return phone2;
    }

    return null;
  }
  
  private void writeKokuCommunitiesLDIF(FileWriter writer, FileWriter allWriter, String cn, String ou,
      List<String> userIDs) throws Exception {
    writeKokuCommunitiesLDIF(writer, cn, ou, userIDs);
    writeKokuCommunitiesLDIF(allWriter, cn, ou, userIDs);
  }

  private void writeKokuCommunitiesLDIF(FileWriter writer, String cn, String ou, List<String> userIDs) throws Exception {  
    writer.write("dn: cn=" + cn + ",ou=" + ou + ",ou=KokuCommunities,o=koku,dc=example,dc=org" + "\n");
    writer.write("changetype: modify\n");
    writer.write("add: member\n");
    for (String userID : userIDs) {
      writer.write("member: cn=" + userID + ",ou=People,o=koku,dc=example,dc=org" + "\n");
    }
    writer.write("\n");
  }
  
  private void writeKokuCommunitiesStructureLDIF(FileWriter writer, String cn, String ou) throws Exception {  
    writer.write("dn: cn=" + cn + ",ou=" + ou + ",ou=KokuCommunities,o=koku,dc=example,dc=org" + "\n");
    writer.write("cn: " + cn + "\n");
    writer.write("objectClass: groupOfNames" + "\n");
    writer.write("objectClass: top" + "\n");
    writer.write("member: " + "\n");
    writer.write("\n");
  }

  private void writeCommunitiesGroupsLDIF(FileWriter writer, FileWriter allWriter, String listType, List<String> userIDs)
      throws Exception {
    writeCommunitiesGroupsLDIF(writer, listType, userIDs);
    writeCommunitiesGroupsLDIF(allWriter, listType, userIDs);
  }  
  
  private void writeCommunitiesGroupsLDIF(FileWriter writer, String listType, List<String> userIDs) throws Exception {
    writer.write("dn: cn=" + listType + ",ou=Groups,ou=KokuCommunities,o=koku,dc=example,dc=org" + "\n");
    writer.write("changetype: modify\n");
    writer.write("add: member\n");
    for (String userID : userIDs) {
      writer.write("member: cn=" + userID + ",ou=People,o=koku,dc=example,dc=org" + "\n");
    }
    writer.write("\n");
  }
  
  private void writeCommunitiesGroupsStructureLDIF(FileWriter writer, String listType) throws Exception {
    writer.write("dn: cn=" + listType + ",ou=Groups,ou=KokuCommunities,o=koku,dc=example,dc=org" + "\n");
    writer.write("cn: " + listType + "\n");
    writer.write("objectClass: groupOfNames" + "\n");
    writer.write("objectClass: top" + "\n");
    writer.write("member: " + "\n");
    writer.write("\n");
  }
  
  private void writeGroupsLDIF(FileWriter writer, FileWriter allWriter, String listType, List<String> userIDs)
      throws Exception {
    writeGroupsLDIF(writer, listType, userIDs);
    writeGroupsLDIF(allWriter, listType, userIDs);
  }  
  // cn=Oravat,ou=Groups,ou=KokuCommunities,o=koku,dc=example,dc=org
  private void writeGroupsLDIF(FileWriter writer, String listType, List<String> userIDs) throws Exception {
    writer.write("dn: cn=" + listType + ",ou=Groups,o=koku,dc=example,dc=org" + "\n");
    writer.write("changetype: modify\n");
    writer.write("add: member\n");
    for (String userID : userIDs) {
      writer.write("member: cn=" + userID + ",ou=People,o=koku,dc=example,dc=org" + "\n");
    }
    writer.write("\n");
  }
  
  private void writeGroupsStructureLDIF(FileWriter writer, String listType) throws Exception {
    writer.write("dn: cn=" + listType + ",ou=Groups,o=koku,dc=example,dc=org" + "\n");
    writer.write("cn: " + listType + "\n");
    writer.write("objectClass: groupOfNames" + "\n");
    writer.write("objectClass: top" + "\n");
    writer.write("member: " + "\n");
    writer.write("\n");
  }
  
  private void writePersonLDIF(FileWriter writer, FileWriter allWriter, String userId, String firstName,
      String lastName, String tel, String email, String uid) throws Exception {
    writePersonLDIF(writer, userId, firstName, lastName, tel, email, uid);
    writePersonLDIF(allWriter, userId, firstName, lastName, tel, email, uid);
  }
  
  private void writePersonLDIF(FileWriter writer, String userId, String firstName, String lastName, String tel, String email,
      String uid) throws Exception {
    writer.write("dn: cn=" + userId + ",ou=People,o=koku,dc=example,dc=org" + "\n");
    writer.write("cn: " + userId + "\n");
    writer.write("givenName: " + firstName + "\n");
    writer.write("objectClass: inetOrgPerson" + "\n");
    writer.write("objectClass: top" + "\n");
    writer.write("sn: " + lastName + "\n");
    //userPassword:: dGVzdA==
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
package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import javax.swing.JFileChooser;

import au.com.bytecode.opencsv.CSVReader;
import fi.koku.tools.dataimport.model.Employee;

public class LDIFWriter {

  private static final String HELMI_CUSTOMER_STRUCTURE_FILENAME = "Helmi_customer_structure.ldif";
  private static final String EFFICA_CUSTOMER_STRUCTURE_FILENAME = "Effica_customer_structure.ldif";
  private static final String EMPLOYEE_STRUCTURE_FILENAME = "Employee_structure.ldif";
  private static final String REGISTRIES = "Registries";
  private static final String ORG_UNITS = "OrgUnits";
  private static final String KK_SERVICEAREA_SCHOOL_HEALTH = "kk.servicearea.schoolHealth";
  private static final String KK_SERVICEAREA_DAYCARE = "kk.servicearea.daycare";
  private static final String KK_SERVICEAREA_CHILD_HEALTH = "kk.servicearea.childHealth";
  private static final String KK_SERVICEAREA_BASIC_EDUCATION = "kk.servicearea.basicEducation";
  private static final String DAYCAREREGISTRY = "daycareregistry";
  private static final String HEALTHCAREREGISTRY = "healthcareregistry";
  private static final String EMPLOYEE_ORG_UNIT_FILENAME_PREFIX = "employeeOrgUnit_";
  private static final String LDIF_FILE_SUFFIX = ".ldif";
  private static final String EMPLOYEE_REGISTRY_FILENAME_PREFIX = "employeeRegistry_";
  private static final String KOULUTERVEYDENHUOLLON_TYÖNTEKIJÄ = "Kouluterveydenhuollon työntekijä";
  private static final String PÄIVÄKODIN_TYÖNTEKIJÄ = "Päiväkodin työntekijä";
  private static final String PÄIVÄKODIN_JOHTAJAT = "Päiväkodin johtajat";
  private static final String NEUVOLAN_TYÖNTEKIJÄ = "Neuvolan työntekijä";

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
  
  private Map<String, String> userIDtoPIC = new HashMap<String, String>();
  private int userIDCounter = 0;
  private SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

  public void writeEmployeeLDIF(Collection<Employee> employees, File parent) throws Exception {
    Collection<String> userIDs = new LinkedHashSet<String>();
    Map<String, LinkedHashSet<String>> groupToUsers = new HashMap<String, LinkedHashSet<String>>();

    FileWriter writer = null;
    FileWriter allWriter = null;
    FileWriter structureWriter = null;

    try {
      allWriter = new FileWriter(new File(parent, EMPLOYEE_ALL_LDIF_FILE));
      structureWriter = new FileWriter(new File(parent, EMPLOYEE_STRUCTURE_FILENAME));

      try {
        writer = new FileWriter(new File(parent, EMPLOYEE_LDIF_FILE));

        for (Employee employee : employees) {
          writePersonLDIF(writer, allWriter, employee.getUserId(), employee.getFirstName(), employee.getLastName(),
              null, employee.getEmail(), employee.getSsn());
          userIDs.add(employee.getUserId());

          LinkedHashSet<String> groupEmployees = groupToUsers.get(employee.getGroup());
          if (groupEmployees == null) {
            groupEmployees = new LinkedHashSet<String>();
          }
          if (!groupEmployees.contains(employee.getUserId())) {
            groupEmployees.add(employee.getUserId());
          }
          groupToUsers.put(employee.getGroup(), groupEmployees);
        }
        writer.close();
      } finally {
        if (writer != null) {
          writer.close();
        }
      }
      System.out.println("Employee LDIF written");

      writeEmployeeGroupLDIFs(parent, userIDs, groupToUsers, allWriter, structureWriter);

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
    System.out.println("Employee Structure LDIFs written");
  }

  public void writeEfficaCustomerLDIF(CSVReader reader, File parent) throws Exception {
    Collection<String> addedUserPICSs = new LinkedHashSet<String>();
    Map<String, Collection<String>> childGroupToUIDs = new HashMap<String, Collection<String>>();

    Collection<String> previousUserPICSs = new LinkedHashSet<String>();
    
    // if there is already added users to LDAP you can give the pics as csv
    selectAddedUserPics(previousUserPICSs);
    
    addedUserPICSs.addAll(previousUserPICSs);
    
    FileWriter writer = null;
    FileWriter allWriter = null;
    FileWriter structureWriter = null;

    try {
      allWriter = new FileWriter(new File(parent, EFFICA_ALL_LDIF_FILE));
      structureWriter = new FileWriter(new File(parent, EFFICA_CUSTOMER_STRUCTURE_FILENAME));

      try {
        writer = new FileWriter(new File(parent, EFFICA_CUSTOMER_LDIF_FILE));

        String[] l;
        while ((l = reader.readNext()) != null) {

          // remove whitespace
          for (int i = 0; i < l.length; i++) {
            l[i] = l[i].trim();
          }
              
          // lapsi
          if (Utils.isNotNullOrEmpty(l[Columns.EFFICA_CHILD_PIC])
              && !addedUserPICSs.contains(l[Columns.EFFICA_CHILD_PIC])) {
            writePersonLDIF(writer, allWriter, Utils.getFirstName(l[Columns.EFFICA_CHILD_FIRSTNAMES]),
                l[Columns.EFFICA_CHILD_LASTNAME], l[Columns.EFFICA_CHILD_PHONE], null, l[Columns.EFFICA_CHILD_PIC]);
            addedUserPICSs.add(l[Columns.EFFICA_CHILD_PIC]);

            addChildToGroup(childGroupToUIDs, l[Columns.EFFICA_CHILD_UNIT], l[Columns.EFFICA_CHILD_GROUP],
                l[Columns.EFFICA_CHILD_PIC]);
          }

          // päähenkilö
          if (Utils.isNotNullOrEmpty(l[Columns.EFFICA_PM_PIC]) && !addedUserPICSs.contains(l[Columns.EFFICA_PM_PIC])) {
            writePersonLDIF(writer, allWriter, Utils.getFirstName(l[Columns.EFFICA_PM_FIRSTNAMES]),
                l[Columns.EFFICA_PM_LASTNAME],
                getPreferredPhone(l[Columns.EFFICA_PM_PHONE_2], l[Columns.EFFICA_PM_PHONE]),
                l[Columns.EFFICA_PM_EMAIL], l[Columns.EFFICA_PM_PIC]);
            addedUserPICSs.add(l[Columns.EFFICA_PM_PIC]);
          }

          // puoliso
          if (Utils.isNotNullOrEmpty(l[Columns.EFFICA_PM_2_PIC])
              && !addedUserPICSs.contains(l[Columns.EFFICA_PM_2_PIC])) {
            writePersonLDIF(writer, allWriter, Utils.getFirstName(l[Columns.EFFICA_PM_2_FIRSTNAMES]),
                l[Columns.EFFICA_PM_2_LASTNAME], l[Columns.EFFICA_PM_2_PHONE], l[Columns.EFFICA_PM_2_EMAIL],
                l[Columns.EFFICA_PM_2_PIC]);
            addedUserPICSs.add(l[Columns.EFFICA_PM_2_PIC]);
          }
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
        
        //remove the previous user pics
        addedUserPICSs.removeAll(previousUserPICSs);
        
        writeGroupsLDIFWithPics(writer, allWriter, KUNTALAINEN, addedUserPICSs);
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
    Collection<String> addedUserPICs = new LinkedHashSet<String>();
    Map<String, Collection<String>> childGroupToUIDs = new HashMap<String, Collection<String>>();
    FileWriter writer = null;
    FileWriter allWriter = null;
    FileWriter structureWriter = null;

    try {
      allWriter = new FileWriter(new File(parent, HELMI_ALL_LDIF_FILE));
      structureWriter = new FileWriter(new File(parent, HELMI_CUSTOMER_STRUCTURE_FILENAME));

      try {
        writer = new FileWriter(new File(parent, HELMI_CUSTOMER_LDIF_FILE));

        String[] l;
        while ((l = reader.readNext()) != null) {

          String[] phones = Utils.splitLines(l[Columns.HELMI_PM_PHONES]);
          String[] emails = Utils.splitLines(l[Columns.HELMI_PM_EMAILS]);
          String[] guardians = Utils.splitLines(l[Columns.HELMI_PMS]);

          // remove whitespace
          for (int i = 0; i < l.length; i++) {
            l[i] = l[i].trim();
          }

          String childPIC = Utils.createUID(l[Columns.HELMI_CHILD_UID_POSTFIX], l[Columns.HELMI_CHILD_BIRTHDATE]);

          // lapsi
          if (!addedUserPICs.contains(childPIC)) {
            writePersonLDIF(writer, allWriter, Utils.getFirstName(l[Columns.HELMI_CHILD_FIRSTNAMES]),
                l[Columns.HELMI_CHILD_LASTNAME], null, null, childPIC);
            addedUserPICs.add(childPIC);
          }

          addChildToGroup(childGroupToUIDs, l[Columns.HELMI_CHILD_UNIT], l[Columns.HELMI_CHILD_GROUP], childPIC);

          // päähenkilö
          if (guardians[0] != null && guardians[0].length() > 0 && l[Columns.HELMI_PM_1_PIC] != null
              && l[Columns.HELMI_PM_1_PIC].length() > 0 && !addedUserPICs.contains(l[Columns.HELMI_PM_1_PIC])) {
            // this is not a copy paste mistake, the Helmi data has
            // the last name first
            String lastName = Utils.getFirstName(guardians[0]);
            String firstNames = Utils.getSecondName(guardians[0]);
            writePersonLDIF(writer, allWriter, Utils.getFirstName(firstNames), lastName, phones[0], emails[0],
                l[Columns.HELMI_PM_1_PIC]);
            addedUserPICs.add(l[Columns.HELMI_PM_1_PIC]);
          }

          // toinen huoltaja
          if (guardians[1] != null && guardians[1].length() > 0 && l[Columns.HELMI_PM_2_PIC] != null
              && l[Columns.HELMI_PM_2_PIC].length() > 0 && !addedUserPICs.contains(l[Columns.HELMI_PM_2_PIC])) {
            // this is not a copy paste mistake, the Helmi data has
            // the last name first
            String lastName = Utils.getFirstName(guardians[1]);
            String firstNames = Utils.getSecondName(guardians[1]);
            writePersonLDIF(writer, allWriter, Utils.getFirstName(firstNames), lastName, phones[1], emails[1],
                l[Columns.HELMI_PM_2_PIC]);
            addedUserPICs.add(l[Columns.HELMI_PM_2_PIC]);
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
        writeGroupsLDIFWithPics(writer, allWriter, KUNTALAINEN, addedUserPICs);
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
  
  private void selectAddedUserPics(Collection<String> piclist) throws Exception {   
    JFileChooser chooser = new JFileChooser(new File("c:/users/hanhian/desktop"));
    chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = chooser.showOpenDialog(null);

    if (result == JFileChooser.APPROVE_OPTION) {
      File file = chooser.getSelectedFile();
      CSVReader reader = new CSVReader(new FileReader(file));
      
      String[] l;
      while ((l = reader.readNext()) != null) {
        // read the only value in the piclist
        Utils.addNotNull(piclist, l[0]);
      }
    }  
  }
  
  private void writeEmployeeGroupLDIFs(File parent, Collection<String> userIDs,
      Map<String, LinkedHashSet<String>> groupToUsers, FileWriter allWriter, FileWriter structureWriter)
      throws IOException, Exception {
    
    FileWriter writer = null;
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
          || KOULUTERVEYDENHUOLLON_TYÖNTEKIJÄ.equals(group) || PÄIVÄKODIN_JOHTAJAT.equals(group)) {

        Collection<String> users = groupToUsers.get(group);

        try {
          writer = new FileWriter(new File(parent, EMPLOYEE_REGISTRY_FILENAME_PREFIX + group + LDIF_FILE_SUFFIX));

          if (NEUVOLAN_TYÖNTEKIJÄ.equals(group)) {
            writeKokuCommunitiesLDIF(writer, allWriter, HEALTHCAREREGISTRY, REGISTRIES, users);
            writeKokuCommunitiesStructureLDIF(structureWriter, HEALTHCAREREGISTRY, REGISTRIES);
          } else if (PÄIVÄKODIN_TYÖNTEKIJÄ.equals(group) || PÄIVÄKODIN_JOHTAJAT.equals(group)) {
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
          } else if (PÄIVÄKODIN_TYÖNTEKIJÄ.equals(group) || PÄIVÄKODIN_JOHTAJAT.equals(group)) {
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
    System.out.println("Employee Registry LDIFs written");
    System.out.println("Employee OrgUnit LDIFs written");
  }

  private void addChildToGroup(Map<String, Collection<String>> childGroupToUIDs, String childUnit, String childGroup,
      String childPic) {
    String groupID = childUnit + " " + childGroup;
    groupID = groupID.replaceAll(",", "");
    groupID = groupID.replaceAll("\\+", "");

    Collection<String> groupUIDs = childGroupToUIDs.get(groupID);
    if (groupUIDs == null) {
      groupUIDs = new LinkedHashSet<String>();
    }
    if (!groupUIDs.contains(childPic)) {
      groupUIDs.add(childPic);
    }
    childGroupToUIDs.put(groupID, groupUIDs);
  }
  
  private void writeChildGroupLDIFs(File parent, FileWriter allWriter, FileWriter structureWriter,
      Map<String, Collection<String>> childGroupToUIDs) throws IOException, Exception {
    FileWriter writer = null;
    for (String groupName : childGroupToUIDs.keySet()) {
      Collection<String> groupUIDs = childGroupToUIDs.get(groupName);

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
  
  // Communitites LDIFs

  private void writeKokuCommunitiesLDIF(FileWriter writer, FileWriter allWriter, String cn, String ou,
      Collection<String> userIDs) throws Exception {
    writeKokuCommunitiesLDIF(writer, cn, ou, userIDs);
    writeKokuCommunitiesLDIF(allWriter, cn, ou, userIDs);
  }

  private void writeKokuCommunitiesLDIF(FileWriter writer, String cn, String ou, Collection<String> userIDs) throws Exception {
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

  // Community groups LDIFs
  
  private void writeCommunitiesGroupsLDIF(FileWriter writer, FileWriter allWriter, String listType, Collection<String> userIDs)
      throws Exception {
    writeCommunitiesGroupsLDIF(writer, listType, userIDs);
    writeCommunitiesGroupsLDIF(allWriter, listType, userIDs);
  }

  private void writeCommunitiesGroupsLDIF(FileWriter writer, String listType, Collection<String> pics) throws Exception {
    writer.write("dn: cn=" + listType + ",ou=Groups,ou=KokuCommunities,o=koku,dc=example,dc=org" + "\n");
    writer.write("changetype: modify\n");
    writer.write("add: member\n");
    for (String pic : pics) {
      writer.write("member: cn=" + getUserID(pic) + ",ou=People,o=koku,dc=example,dc=org" + "\n");
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
  
  // groups LDIFS

  private void writeGroupsLDIF(FileWriter writer, FileWriter allWriter, String listType, Collection<String> userIDs)
      throws Exception {
    writeGroupsLDIF(writer, listType, userIDs);
    writeGroupsLDIF(allWriter, listType, userIDs);
  }
  
  private void writeGroupsLDIFWithPics(FileWriter writer, FileWriter allWriter, String listType, Collection<String> userPICs)
      throws Exception {    
    Collection<String> userIDs = new LinkedHashSet<String>();    
    for (String pic : userPICs) {
      userIDs.add(getUserID(pic));
    }    
    writeGroupsLDIF(writer, listType, userIDs);
    writeGroupsLDIF(allWriter, listType, userIDs);
  }

  private void writeGroupsLDIF(FileWriter writer, String listType, Collection<String> userIDs) throws Exception {
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
  
  // Person LDIFs
  
  private void writePersonLDIF(FileWriter writer, FileWriter allWriter, String userId, String firstName,
      String lastName, String tel, String email, String pic) throws Exception {
    writePersonLDIF(writer, userId, firstName, lastName, tel, email, pic);
    writePersonLDIF(allWriter, userId, firstName, lastName, tel, email, pic);
  }

  private void writePersonLDIF(FileWriter writer, FileWriter allWriter, String firstName,
      String lastName, String tel, String email, String pic) throws Exception {
    String userId = getUserID(pic);
    
    writePersonLDIF(writer, userId, firstName, lastName, tel, email, pic);
    writePersonLDIF(allWriter, userId, firstName, lastName, tel, email, pic);
  }
  
  private void writePersonLDIF(FileWriter writer, String userId, String firstName, String lastName, String tel,
      String email, String pic) throws Exception {
    writer.write("dn: cn=" + userId + ",ou=People,o=koku,dc=example,dc=org" + "\n");
    writer.write("cn: " + userId + "\n");
    writer.write("givenName: " + firstName + "\n");
    writer.write("objectClass: inetOrgPerson" + "\n");
    writer.write("objectClass: top" + "\n");
    writer.write("sn: " + lastName + "\n");
    // userPassword:: dGVzdA==
    writer.write("userPassword: test" + "\n");
    if (tel != null && tel.length() != 0) {
      writer.write("telephoneNumber: " + tel + "\n");
    }
    if (email != null && email.length() != 0) {
      writer.write("mail: " + email + "\n");
    }
    writer.write("uid: " + pic + "\n");
    writer.write("\n");
  }
  
  protected String getUserID(String pic){
    if(userIDtoPIC.get(pic) == null){
      userIDCounter ++;
      String userID = "user_" + format.format(new Date()) + "_" + userIDCounter;      
      userIDtoPIC.put(pic, userID);
    }  
    return userIDtoPIC.get(pic);
  }
}
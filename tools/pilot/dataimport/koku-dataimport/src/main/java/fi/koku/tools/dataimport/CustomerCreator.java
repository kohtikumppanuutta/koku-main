package fi.koku.tools.dataimport;

import java.io.File;
import java.util.Collection;
import java.util.LinkedHashSet;

import au.com.bytecode.opencsv.CSVReader;
import fi.koku.services.entity.community.v1.CommunityType;
import fi.koku.services.entity.community.v1.MemberType;

public class CustomerCreator {

  private static final String TURVAKIELTO = "TURVAKIELTO";
  private static final String HELMI_ADDED_COMMUNITY_IDS_TXT = "helmi_changed_communityIDs.txt";
  private static final String HELMI_ADDED_CUSTOMER_IDS_TXT = "helmi_addedCustomerIDs.txt";
  private static final String EFFICA_ADDED_COMMUNITY_IDS_TXT = "effica_changed_communityIDs.txt";
  private static final String EFFICA_ADDED_CUSTOMER_IDS_TXT = "effica_addedCustomerIDs.txt";
  private static final String COMMUNITY_SUFFIX = "_huollettavat";

  public void createEfficaCustomers(CSVReader reader, WSCaller caller, File parent) throws Exception {

    Collection<String> customerIDs = new LinkedHashSet<String>();
    Collection<String> communityIDs = new LinkedHashSet<String>();

    int counter = 0;
    
    String[] l;
    while ((l = reader.readNext()) != null) {
      try {

        System.out.println(counter ++);
        
        // remove whitespace
        for (int i = 0; i < l.length; i++) {
          l[i] = l[i].trim();
        }

        // Create child customer
        if (caller.getCustomerByPic(l[Columns.EFFICA_CHILD_PIC]) == null) {
          customerIDs.add(caller.addCustomer(l[Columns.EFFICA_CHILD_PIC], l[Columns.EFFICA_CHILD_BIRTH_DATE],
              l[Columns.EFFICA_CHILD_LASTNAME], Utils.getFirstName(l[Columns.EFFICA_CHILD_FIRSTNAMES]),
              l[Columns.EFFICA_CHILD_FIRSTNAMES], l[Columns.EFFICA_CHILD_KANSALAISUUSKOODI],
              l[Columns.EFFICA_CHILD_KUNTAKOODI], l[Columns.EFFICA_CHILD_KIELIKOODI],
              removeTurvaKielto(l[Columns.EFFICA_CHILD_ADDRESS]), l[Columns.EFFICA_CHILD_POST_OFFICE],
              l[Columns.EFFICA_CHILD_POST_CODE], l[Columns.EFFICA_CHILD_PHONE], null, null, null));
        }
        
        // Create 'päämies'
        if (caller.getCustomerByPic(l[Columns.EFFICA_PM_PIC]) == null) {
          customerIDs.add(caller.addCustomer(l[Columns.EFFICA_PM_PIC], getBirthDate(l[Columns.EFFICA_PM_PIC]),
              l[Columns.EFFICA_PM_LASTNAME], Utils.getFirstName(l[Columns.EFFICA_PM_FIRSTNAMES]),
              l[Columns.EFFICA_PM_FIRSTNAMES], l[Columns.EFFICA_PM_KANSALAISUUSKOODI],
              l[Columns.EFFICA_PM_KUNTAKOODI], l[Columns.EFFICA_PM_KIELIKOODI], removeTurvaKielto(l[Columns.EFFICA_PM_ADDRESS]),
              l[Columns.EFFICA_PM_POST_OFFICE], l[Columns.EFFICA_PM_POST_CODE], l[Columns.EFFICA_PM_PHONE],
              l[Columns.EFFICA_PM_PHONE_2], l[Columns.EFFICA_PM_EMAIL], l[Columns.EFFICA_PM_EMAIL_2]));
        }

        if (Utils.isNotNullOrEmpty(l[Columns.EFFICA_PM_PIC]) && Utils.isNotNullOrEmpty(l[Columns.EFFICA_CHILD_PIC])) {
          addChildToCommunity(caller, communityIDs, l[Columns.EFFICA_PM_PIC], l[Columns.EFFICA_PM_FIRSTNAMES],
              l[Columns.EFFICA_CHILD_PIC]);
        }

        // Create 'puoliso'
        if (l[Columns.EFFICA_PM_2_PIC].length() > 0 && caller.getCustomerByPic(l[Columns.EFFICA_PM_2_PIC]) == null) {         
          customerIDs.add(caller.addCustomer(l[Columns.EFFICA_PM_2_PIC], getBirthDate(l[Columns.EFFICA_PM_2_PIC]),
              l[Columns.EFFICA_PM_2_LASTNAME], Utils.getFirstName(l[Columns.EFFICA_PM_2_FIRSTNAMES]),
              l[Columns.EFFICA_PM_2_FIRSTNAMES], l[Columns.EFFICA_PM_2_KANSALAISUUSKOODI],
              l[Columns.EFFICA_PM_2_KUNTAKOODI], l[Columns.EFFICA_PM_2_KIELIKOODI], removeTurvaKielto(l[Columns.EFFICA_PM_2_ADDRESS]),
              l[Columns.EFFICA_PM_2_POST_OFFICE], l[Columns.EFFICA_PM_2_POST_CODE], l[Columns.EFFICA_PM_2_PHONE],
              null, l[Columns.EFFICA_PM_2_EMAIL], l[Columns.EFFICA_PM_2_EMAIL_2]));
        }

        if (Utils.isNotNullOrEmpty(l[Columns.EFFICA_PM_2_PIC]) && Utils.isNotNullOrEmpty(l[Columns.EFFICA_CHILD_PIC])) {
          addChildToCommunity(caller, communityIDs, l[Columns.EFFICA_PM_2_PIC], l[Columns.EFFICA_PM_2_FIRSTNAMES],
              l[Columns.EFFICA_CHILD_PIC]);
        }

      } catch (Exception e) {
        System.err.println("Data: ");
        for (int i = 0; i < l.length; i++) {
          System.err.print(l[i] + ", ");
        }
        System.err.println();
        throw e;
      }
    }

    System.out.println(customerIDs.size() + " customers added");
    System.out.println(communityIDs.size() + " communities added");

    Utils.writeIDsToFile(parent, customerIDs, EFFICA_ADDED_CUSTOMER_IDS_TXT);
    Utils.writeIDsToFile(parent, communityIDs, EFFICA_ADDED_COMMUNITY_IDS_TXT);
  }

  public void createHelmiCustomers(CSVReader reader, WSCaller caller, File parent) throws Exception {

    Collection<String> customerIDs = new LinkedHashSet<String>();
    Collection<String> communityIDs = new LinkedHashSet<String>();

    String[] l;
    while ((l = reader.readNext()) != null) {
      try {
        String[] guardians = Utils.splitLines(l[Columns.HELMI_PMS]);
        String[] phones = Utils.splitLines(l[Columns.HELMI_PM_PHONES]);
        String[] emails = Utils.splitLines(l[Columns.HELMI_PM_EMAILS]);

        // remove whitespace
        for (int i = 0; i < l.length; i++) {
          l[i] = l[i].trim();
        }

        String childPIC = Utils.createUID(l[Columns.HELMI_CHILD_UID_POSTFIX], l[Columns.HELMI_CHILD_BIRTHDATE]);

        // Create child customer
        if (caller.getCustomerByPic(childPIC) == null) {
          // TODO add post office and post code
          customerIDs.add(caller.addCustomer(childPIC, Utils.parseBirthDate(l[Columns.HELMI_CHILD_BIRTHDATE]),
              l[Columns.HELMI_CHILD_LASTNAME], Utils.getFirstName(l[Columns.HELMI_CHILD_FIRSTNAMES]),
              l[Columns.HELMI_CHILD_FIRSTNAMES], l[Columns.HELMI_CHILD_NATIONALITY], l[Columns.HELMI_CHILD_KUNTA],
              l[Columns.HELMI_CHILD_LANGUAGE], l[Columns.HELMI_ADDRESS], null, null, null, null, null, null));
        }

        if (guardians[0] != null && guardians[0].length() > 0 && l[Columns.HELMI_PM_1_PIC] != null
            && l[Columns.HELMI_PM_1_PIC].length() > 0) {

          // Create 'päämies'
          if (caller.getCustomerByPic(l[Columns.HELMI_PM_1_PIC]) == null) {
            // this is not a copy paste mistake, the Helmi data has
            // the last name first
            String lastName = Utils.getFirstName(guardians[0]);
            String firstNames = Utils.getSecondName(guardians[0]);

            // TODO add post office and post code
            // TODO replace hardcoded values
            customerIDs.add(caller.addCustomer(l[Columns.HELMI_PM_1_PIC], getBirthDate(l[Columns.HELMI_PM_1_PIC]),
                lastName, Utils.getFirstName(firstNames), firstNames, "FI", "837", "FI", l[Columns.HELMI_ADDRESS],
                null, null, null, phones[0], emails[0], null));
          }

          addChildToCommunity(caller, communityIDs, l[Columns.HELMI_PM_1_PIC], Utils.getSecondName(guardians[0]),
              childPIC);
        }

        if (guardians[1] != null && guardians[1].length() > 0 && l[Columns.HELMI_PM_2_PIC] != null
            && l[Columns.HELMI_PM_2_PIC].length() > 0) {
          
          // Create 'toinen huoltaja'
          if (caller.getCustomerByPic(l[Columns.HELMI_PM_2_PIC]) == null) {
            // this is not a copy paste mistake, the Helmi data has
            // the last name first
            String lastName = Utils.getFirstName(guardians[1]);
            String firstNames = Utils.getSecondName(guardians[1]);

            // TODO add post office and post code
            // TODO replace hardcoded values
            customerIDs.add(caller.addCustomer(l[Columns.HELMI_PM_2_PIC], getBirthDate(l[Columns.HELMI_PM_2_PIC]),
                lastName, Utils.getFirstName(firstNames), firstNames, "FI", "837", "FI", l[Columns.HELMI_ADDRESS],
                null, null, null, phones[1], emails[1], null));
          }

          addChildToCommunity(caller, communityIDs, l[Columns.HELMI_PM_2_PIC], Utils.getSecondName(guardians[1]),
              childPIC);
        }
      } catch (Exception e) {
        System.err.println("Data: ");
        for (int i = 0; i < l.length; i++) {
          System.err.print(l[i] + ", ");
        }
        System.err.println();
        throw e;
      }
    }

    System.out.println(customerIDs.size() + " customers added");
    System.out.println(communityIDs.size() + " communities added");

    Utils.writeIDsToFile(parent, customerIDs, HELMI_ADDED_CUSTOMER_IDS_TXT);
    Utils.writeIDsToFile(parent, communityIDs, HELMI_ADDED_COMMUNITY_IDS_TXT);
  }

  private void addChildToCommunity(WSCaller caller, Collection<String> communityIDs, String guardianPic,
      String guardianFirstNames, String childPIC) throws Exception {

    // Check if 'päämies' community exists
    String communityID;
    CommunityType community = caller.getCommunity(guardianPic);
    if (community != null) {
      communityID = community.getId();

      // check if the child is already in the community
      if (community.getMembers() != null) {
        for (MemberType member : community.getMembers().getMember()) {
          if (member.getPic().equals(childPIC)) {
            return;
          }
        }
      }
    } else {
      // no existing community, create it
      communityID = caller.createCommunity(Utils.getFirstName(guardianFirstNames) + COMMUNITY_SUFFIX, guardianPic);  
    }
    
    // store for printout
    communityIDs.add(communityID);
    
    // add child to parent's community
    caller.updateCommunity(communityID, childPIC);
  }

  private String removeTurvaKielto(String text) {
    if (text != null && TURVAKIELTO.equals(text)) {
      return "";
    } else {
      return text;
    }
  }

  private String getBirthDate(String pic) throws Exception {

    if (pic == null) {
      throw new Exception("Pic is invalid: " + pic);
    }

    if (pic.contains("-")) {
      return pic.substring(0, pic.indexOf("-"));
    }

    else if (pic.contains("A") || pic.contains("a")) {
      String temp = pic.toLowerCase();
      return temp.substring(0, temp.indexOf("a"));
    }

    else {
      throw new Exception("Pic is invalid: " + pic);
    }
  }
}
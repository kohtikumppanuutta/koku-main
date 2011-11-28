package fi.koku.tools.dataimport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class CustomerCreator {

  private static final int HELMI_PM_2_PIC = 17;
  private static final int HELMI_PM_1_PIC = 16;
  private static final int HELMI_CHILD_LANGUAGE = 10;
  private static final int HELMI_CHILD_KUNTA = 9;
  private static final int HELMI_CHILD_NATIONALITY = 8;
  private static final int HELMI_CHILD_FIRSTNAMES = 6;
  private static final int HELMI_CHILD_LASTNAME = 7;
  private static final int HELMI_PM_EMAILS = 14;
  private static final int HELMI_PM_PHONES = 13;
  private static final int HELMI_PMS = 15;
  private static final int HELMI_CHILD_BIRTHDATE = 4;
  private static final int HELMI_CHILD_UID_POSTFIX = 3;
  private static final int HELMI_ADDRESS = 12;
  private static final String TURVAKIELTO = "TURVAKIELTO";  
  
  private static final String HELMI_ADDED_COMMUNITY_IDS_TXT = "helmi_added_communityIDs.txt";
  private static final String HELMI_ADDED_CUSTOMER_IDS_TXT = "helmi_addedCustomerIDs.txt";
  
  private static final int EFFICA_PM_EMAIL_2 = 23;
  private static final int EFFICA_PM_EMAIL = 22;
  private static final int EFFICA_PM_PHONE_2 = 25;
  private static final int EFFICA_PM_PHONE = 24;
  private static final int EFFICA_PM_POST_CODE = 20;
  private static final int EFFICA_PM_POST_OFFICE = 21;
  private static final int EFFICA_PM_ADDRESS = 19;
  private static final int EFFICA_PM_FIRSTNAMES = 18;
  private static final int EFFICA_PM_LASTNAME = 17;
  private static final int EFFICA_PM_PIC = 16;
  private static final int EFFICA_CHILD_PHONE = 15;
  private static final int EFFICA_CHILD_POST_CODE = 13;
  private static final int EFFICA_CHILD_POST_OFFICE = 14;
  private static final int EFFICA_CHILD_ADDRESS = 12;
  private static final int EFFICA_CHILD_KIELIKOODI = 9;
  private static final int EFFICA_CHILD_KUNTAKOODI = 8;
  private static final int EFFICA_CHILD_KANSALAISUUSKOODI = 10;
  private static final int EFFICA_CHILD_FIRSTNAMES = 7;
  private static final int EFFICA_CHILD_LASTNAME = 6;
  private static final int EFFICA_CHILD_BIRTH_DATE = 5;
  private static final int EFFICA_CHILD_PIC = 4;
  
  private static final String EFFICA_ADDED_COMMUNITY_IDS_TXT = "effica_added_communityIDs.txt";
  private static final String EFFICA_ADDED_CUSTOMER_IDS_TXT = "effica_addedCustomerIDs.txt";
  
  private static final String COMMUNITY_SUFFIX = "_huollettavat";
  
  public void createEfficaCustomers(CSVReader reader, WSCaller caller, File parent) throws Exception {

    List<String> customerIDs = new ArrayList<String>();
    Map<String, String> picToCommunity = new HashMap<String, String>();

    String[] l;
    while ((l = reader.readNext()) != null) {
      try {

        // remove whitespace
        for (int i = 0; i < l.length; i++) {
          l[i] = l[i].trim();
        }

        // Create child customer
        if (caller.getCustomerByPic(l[EFFICA_CHILD_PIC]) == null) {
          customerIDs.add(caller.addCustomer(l[EFFICA_CHILD_PIC], l[EFFICA_CHILD_BIRTH_DATE], l[EFFICA_CHILD_LASTNAME],
              Utils.getFirstName(l[EFFICA_CHILD_FIRSTNAMES]), l[EFFICA_CHILD_FIRSTNAMES],
              l[EFFICA_CHILD_KANSALAISUUSKOODI], l[EFFICA_CHILD_KUNTAKOODI], l[EFFICA_CHILD_KIELIKOODI],
              removeTurvaKielto(l[EFFICA_CHILD_ADDRESS]), l[EFFICA_CHILD_POST_OFFICE], l[EFFICA_CHILD_POST_CODE],
              l[EFFICA_CHILD_PHONE], null, null, null));
        }
        
        // Create 'päämies'
        if (caller.getCustomerByPic(l[EFFICA_PM_PIC]) == null) {
          // TODO replace hard-coded values with real ones.
          customerIDs.add(caller.addCustomer(l[EFFICA_PM_PIC], getBirthDate(l[EFFICA_PM_PIC]), l[EFFICA_PM_LASTNAME],
              Utils.getFirstName(l[EFFICA_PM_FIRSTNAMES]), l[EFFICA_PM_FIRSTNAMES], "FI", "011", "FI",
              l[EFFICA_PM_ADDRESS], l[EFFICA_PM_POST_OFFICE], l[EFFICA_PM_POST_CODE], l[EFFICA_PM_PHONE],
              l[EFFICA_PM_PHONE_2], l[EFFICA_PM_EMAIL], l[EFFICA_PM_EMAIL_2]));
        }

        addChildToCommunity(caller, picToCommunity, l[EFFICA_PM_PIC], l[EFFICA_PM_FIRSTNAMES], l[EFFICA_CHILD_PIC]);
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
    System.out.println(picToCommunity.size() + " communities added");

    Utils.writeIDsToFile(parent, customerIDs, EFFICA_ADDED_CUSTOMER_IDS_TXT);
    Utils.writeIDsToFile(parent, picToCommunity.values(), EFFICA_ADDED_COMMUNITY_IDS_TXT);
  }

  public void createHelmiCustomers(CSVReader reader, WSCaller caller, File parent) throws Exception {

    List<String> customerIDs = new ArrayList<String>();
    Map<String, String> picToCommunity = new HashMap<String, String>();

    String[] l;
    while ((l = reader.readNext()) != null) {
      try {
        String[] guardians = Utils.splitLines(l[HELMI_PMS]);
        String[] phones = Utils.splitLines(l[HELMI_PM_PHONES]);
        String[] emails = Utils.splitLines(l[HELMI_PM_EMAILS]);

        // remove whitespace
        for (int i = 0; i < l.length; i++) {
          l[i] = l[i].trim();
        }

        String childPIC = Utils.createUID(l[HELMI_CHILD_UID_POSTFIX], l[HELMI_CHILD_BIRTHDATE]);

        // Create child customer
        if (caller.getCustomerByPic(childPIC) == null) {
          // TODO add post office and post code
          customerIDs.add(caller.addCustomer(childPIC, Utils.parseBirthDate(l[HELMI_CHILD_BIRTHDATE]),
              l[HELMI_CHILD_LASTNAME], Utils.getFirstName(l[HELMI_CHILD_FIRSTNAMES]), l[HELMI_CHILD_FIRSTNAMES],
              l[HELMI_CHILD_NATIONALITY], l[HELMI_CHILD_KUNTA], l[HELMI_CHILD_LANGUAGE], l[HELMI_ADDRESS], null, null,
              null, null, null, null));
        }

        // Create 'päämies'
        if (caller.getCustomerByPic(l[HELMI_PM_1_PIC]) == null) {
          // this is not a copy paste mistake, the Helmi data has
          // the last name first
          String lastName = Utils.getFirstName(guardians[0]);
          String firstNames = Utils.getSecondName(guardians[0]);

          // TODO add post office and post code
          // TODO replace hardcoded values
          customerIDs.add(caller.addCustomer(l[HELMI_PM_1_PIC], getBirthDate(l[HELMI_PM_1_PIC]), lastName,
              Utils.getFirstName(firstNames), firstNames, "FI", "011", "FI", l[HELMI_ADDRESS], null, null, null,
              phones[0], emails[0], null));
        }

        addChildToCommunity(caller, picToCommunity, l[HELMI_PM_1_PIC], Utils.getSecondName(guardians[0]), childPIC);

        if (guardians[1] != null && guardians[1].length() > 0 && l[HELMI_PM_2_PIC] != null
            && l[HELMI_PM_2_PIC].length() > 0) {
          // Create 'toinen huoltaja'
          if (caller.getCustomerByPic(l[HELMI_PM_2_PIC]) == null) {
            // this is not a copy paste mistake, the Helmi data has
            // the last name first
            String lastName = Utils.getFirstName(guardians[1]);
            String firstNames = Utils.getSecondName(guardians[1]);

            // TODO add post office and post code
            // TODO replace hardcoded values
            customerIDs.add(caller.addCustomer(l[HELMI_PM_2_PIC], getBirthDate(l[HELMI_PM_2_PIC]), lastName,
                Utils.getFirstName(firstNames), firstNames, "FI", "011", "FI", l[HELMI_ADDRESS], null, null, null,
                phones[1], emails[1], null));
          }

          addChildToCommunity(caller, picToCommunity, l[HELMI_PM_2_PIC], Utils.getSecondName(guardians[1]), childPIC);
        }
      } catch (Exception e) {
        System.err.println("Data: ");
        for (int i = 0; i < l.length; i++) {
          System.err.print(l[i]+", ");
        }
        System.err.println();
        throw e;
      }
    }

    System.out.println(customerIDs.size() + " customers added");
    System.out.println(picToCommunity.size() + " communities added");

    Utils.writeIDsToFile(parent, customerIDs, HELMI_ADDED_CUSTOMER_IDS_TXT);
    Utils.writeIDsToFile(parent, picToCommunity.values(), HELMI_ADDED_COMMUNITY_IDS_TXT);
  }

  private void addChildToCommunity(WSCaller caller, Map<String, String> picToCommunity, String pic, String guardianFirstNames,
      String childPIC) throws Exception {
    
    // create 'päämies' community if it does not exist
    String communityID = picToCommunity.get(pic);
    if (communityID == null) {
      communityID = caller.createCommunity(
          Utils.getFirstName(guardianFirstNames) + COMMUNITY_SUFFIX, pic);
      picToCommunity.put(pic, communityID);
    }
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
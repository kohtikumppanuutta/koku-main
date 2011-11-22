package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.bytecode.opencsv.CSVReader;

public class CustomerCreator {

  private static final int HELMI_ADDRESS = 12;
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
  private static final String COMMUNITY_SUFFIX = "_huollettavat";
  private static final String EFFICA_ADDED_COMMUNITY_IDS_TXT = "effica_added_communityIDs.txt";
  private static final String EFFICA_ADDED_CUSTOMER_IDS_TXT = "effica_addedCustomerIDs.txt";
  
  private static final String HELMI_ADDED_COMMUNITY_IDS_TXT = "helmi_added_communityIDs.txt";
  private static final String HELMI_ADDED_CUSTOMER_IDS_TXT = "helmi_addedCustomerIDs.txt";

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
              l[EFFICA_CHILD_ADDRESS], l[EFFICA_CHILD_POST_OFFICE], l[EFFICA_CHILD_POST_CODE], l[EFFICA_CHILD_PHONE],
              null, null, null));
        }
        
        // Create 'päämies'
        if (caller.getCustomerByPic(l[EFFICA_PM_PIC]) == null) {
          // TODO replace hard-coded values with real ones.
          customerIDs.add(caller.addCustomer(l[EFFICA_PM_PIC], getBirthDate(l[EFFICA_PM_PIC]), l[EFFICA_PM_LASTNAME],
              Utils.getFirstName(l[EFFICA_PM_FIRSTNAMES]), l[EFFICA_PM_FIRSTNAMES], "FI", "011", "FI",
              l[EFFICA_PM_ADDRESS], l[EFFICA_PM_POST_OFFICE], l[EFFICA_PM_POST_CODE], l[EFFICA_PM_PHONE],
              l[EFFICA_PM_PHONE_2], l[EFFICA_PM_EMAIL], l[EFFICA_PM_EMAIL_2]));
        }
        // TODO Puolisoa ei lisätä ainakaan vielä

        // create community if it does not exist
        String communityID = picToCommunity.get(l[EFFICA_PM_PIC]);
        if (communityID == null) {
          communityID = caller.createCommunity(Utils.getFirstName(l[EFFICA_PM_FIRSTNAMES]) + COMMUNITY_SUFFIX,
              l[EFFICA_PM_PIC]);
          picToCommunity.put(l[16], communityID);
        }
        // add child to parent's community
        caller.updateCommunity(communityID, l[EFFICA_CHILD_PIC]);
      } catch (Exception e) {
        System.err.println("Data: ");
        for (int i = 0; i < l.length; i++) {
          System.err.print(l[i] + ", ");
        }
        System.err.println();        
        throw e;
      }
    }

    System.out.println(customerIDs.size() + " customers added successfully");
    System.out.println(picToCommunity.size() + " communities added successfully");

    Utils.writeIDsToFile(parent, customerIDs, EFFICA_ADDED_CUSTOMER_IDS_TXT);
    Utils.writeIDsToFile(parent, picToCommunity.values(), EFFICA_ADDED_COMMUNITY_IDS_TXT);
  }
  
  public void createHelmiCustomers(CSVReader reader, WSCaller caller, File parent) throws Exception {

    List<String> customerIDs = new ArrayList<String>();
    Map<String, String> picToCommunity = new HashMap<String, String>();

    String[] l;
    while ((l = reader.readNext()) != null) {
      try {
        String[] guardians = Utils.splitLines(l[15]);
        String[] phones = Utils.splitLines(l[13]);
        String[] emails = Utils.splitLines(l[14]);

        // remove whitespace
        for (int i = 0; i < l.length; i++) {
          l[i] = l[i].trim();
        }

        String childPIC = Utils.createUID(l[3], l[4]);

        // Create child customer
        if (caller.getCustomerByPic(childPIC) == null) {          
          // TODO add post office and post code
          customerIDs.add(caller.addCustomer(childPIC, Utils.parseBirthDate(l[4]), l[7], Utils.getFirstName(l[6]),
              l[6], l[8], l[9], l[10], l[HELMI_ADDRESS], null, null, null, null, null, null));
        }
        
        // Create 'päämies'
        if (caller.getCustomerByPic(l[16]) == null) {
          // this is not a copy paste mistake, the Helmi data has
          // the last name first
          String lastName = Utils.getFirstName(guardians[0]);
          String firstNames = Utils.getSecondName(guardians[0]);

          // TODO add post office and post code
          // TODO replace hardcoded values
          customerIDs.add(caller.addCustomer(l[16], getBirthDate(l[16]), lastName, Utils.getFirstName(firstNames),
              firstNames, "FI", "011", "FI", l[HELMI_ADDRESS], null, null, null, phones[0], emails[0], null));
        }

        // create 'päämies' community if it does not exist
        String communityID = picToCommunity.get(l[16]);
        if (communityID == null) {
          // Still not copy paste mistake
          communityID = caller.createCommunity(
              Utils.getFirstName(Utils.getSecondName(guardians[0])) + COMMUNITY_SUFFIX, l[16]);
          picToCommunity.put(l[16], communityID);
        }
        // add child to parent's community
        caller.updateCommunity(communityID, childPIC);

        // Create 'toinen huoltaja'
        if (guardians[1] != null && guardians[1].length() > 0 && caller.getCustomerByPic(l[17]) == null) {
          // this is not a copy paste mistake, the Helmi data has
          // the last name first
          String lastName = Utils.getFirstName(guardians[1]);
          String firstNames = Utils.getSecondName(guardians[1]);

          // TODO add post office and post code
          // TODO replace hardcoded values
          customerIDs.add(caller.addCustomer(l[17], getBirthDate(l[17]), lastName, Utils.getFirstName(firstNames),
              firstNames, "FI", "011", "FI", l[HELMI_ADDRESS], null, null, null, phones[1], emails[1], null));
        }

        // create community if it does not exist
        communityID = picToCommunity.get(l[17]);
        if (communityID == null) {
          // Still not copy paste mistake
          communityID = caller.createCommunity(
              Utils.getFirstName(Utils.getSecondName(guardians[1])) + COMMUNITY_SUFFIX, l[17]);
          picToCommunity.put(l[17], communityID);
        }
        // add child to parent's community
        caller.updateCommunity(communityID, childPIC);
      } catch (Exception e) {
        System.err.println("Data: ");
        for (int i = 0; i < l.length; i++) {
          System.err.print(l[i]+", ");
        }
        System.err.println();
        throw e;
      }
    }

    System.out.println(customerIDs.size() + " customers added successfully");
    System.out.println(picToCommunity.size() + " communities added successfully");

    Utils.writeIDsToFile(parent, customerIDs, HELMI_ADDED_CUSTOMER_IDS_TXT);
    Utils.writeIDsToFile(parent, picToCommunity.values(), HELMI_ADDED_COMMUNITY_IDS_TXT);
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
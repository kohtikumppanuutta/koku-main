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

  private static final int PM_EMAIL_2 = 23;
  private static final int PM_EMAIL = 22;
  private static final int PM_PHONE_2 = 25;
  private static final int PM_PHONE = 24;
  private static final int PM_POST_CODE = 20;
  private static final int PM_POST_OFFICE = 21;
  private static final int PM_ADDRESS = 19;
  private static final int PM_FIRSTNAMES = 18;
  private static final int PM_LASTNAME = 17;
  private static final int PM_PIC = 16;
  private static final int CHILD_PHONE = 15;
  private static final int CHILD_POST_CODE = 13;
  private static final int CHILD_POST_OFFICE = 14;
  private static final int CHILD_ADDRESS = 12;
  private static final int CHILD_KIELIKOODI = 9;
  private static final int CHILD_KUNTAKOODI = 8;
  private static final int CHILD_KANSALAISUUSKOODI = 10;
  private static final int CHILD_FIRSTNAMES = 7;
  private static final int CHILD_LASTNAME = 6;
  private static final int CHILD_BIRTH_DATE = 5;
  private static final int CHILD_PIC = 4;
  private static final String COMMUNITY_SUFFIX = "_huollettavat";
  private static final String ADDED_COMMUNITY_IDS_TXT = "added_communityIDs.txt";
  private static final String ADDED_CUSTOMER_IDS_TXT = "addedCustomerIDs.txt";

  public void createCustomers(CSVReader reader, WSCaller caller, File parent) throws Exception {

    List<String> customerIDs = new ArrayList<String>();
    Map<String, String> picToCommunity = new HashMap<String, String>();

    String[] l;
    while ((l = reader.readNext()) != null) {
      // Create child customer
      customerIDs.add(caller.addCustomer(l[CHILD_PIC], l[CHILD_BIRTH_DATE], l[CHILD_LASTNAME],
          getFirstName(l[CHILD_FIRSTNAMES]), l[CHILD_FIRSTNAMES], l[CHILD_KANSALAISUUSKOODI], l[CHILD_KUNTAKOODI],
          l[CHILD_KIELIKOODI], l[CHILD_ADDRESS], l[CHILD_POST_OFFICE], l[CHILD_POST_CODE], l[CHILD_PHONE], null, null,
          null));

      // Create 'päämies'
      if (caller.getCustomerByPic(l[16]) == null) {
        // TODO replace hard-coded values with real ones.
        customerIDs.add(caller.addCustomer(l[PM_PIC], getBirthDate(l[PM_PIC]), l[PM_LASTNAME],
            getFirstName(l[PM_FIRSTNAMES]), l[PM_FIRSTNAMES], "FI", "011", "FI", l[PM_ADDRESS], l[PM_POST_OFFICE],
            l[PM_POST_CODE], l[PM_PHONE], l[PM_PHONE_2], l[PM_EMAIL], l[PM_EMAIL_2]));
      }
      // TODO Puolisoa ei lisätä ainakaan vielä

      // create community if it does not exist
      String communityID = picToCommunity.get(l[PM_PIC]);
      if (communityID == null) {
        communityID = caller.createCommunity(getFirstName(l[PM_FIRSTNAMES]) + COMMUNITY_SUFFIX, l[PM_PIC]);
        picToCommunity.put(l[16], communityID);
      }
      // add child to parent's community
      caller.updateCommunity(communityID, l[CHILD_PIC]);
    }

    System.out.println(customerIDs.size() + " customers added successfully");
    System.out.println(picToCommunity.size() + " communities added successfully");

    writeAddedIDs(parent, customerIDs, ADDED_CUSTOMER_IDS_TXT);
    writeAddedIDs(parent, picToCommunity.values(), ADDED_COMMUNITY_IDS_TXT);
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

  private void writeAddedIDs(File parent, Collection<String> ids, String filename) throws Exception {
    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(parent, filename));

      for (String id : ids) {
        writer.write(id + ", ");
      }

      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    System.out.println(filename + " written successfully");
  }

  private String getBirthDate(String pic) throws Exception {

    if (pic == null) {
      throw new Exception("Pic is invalid: " + pic);
    }

    if (pic.contains("A") || pic.contains("a")) {
      String temp = pic.toLowerCase();
      return temp.substring(0, temp.indexOf("a"));
    } else if (pic.contains("-")) {
      return pic.substring(0, pic.indexOf("-"));
    } else {
      throw new Exception("Pic is invalid: " + pic);
    }
  }
}
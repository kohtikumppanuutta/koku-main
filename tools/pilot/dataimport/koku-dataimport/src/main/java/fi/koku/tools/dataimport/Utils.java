package fi.koku.tools.dataimport;

import java.io.File;
import java.io.FileWriter;
import java.util.Collection;

public class Utils {

  public static void writeIDsToFile(File parent, Collection<String> ids, String filename) throws Exception {
    FileWriter writer = null;
    try {
      writer = new FileWriter(new File(parent, filename));

      for (String id : ids) {
        writer.write(id + ", ");
        //writer.write(id + "\n");
      }

      writer.close();
    } finally {
      if (writer != null) {
        writer.close();
      }
    }
    System.out.println(filename + " written");
  }
  
  public static String createUID(String UID_postfix, String childBirthDate) {
    String shortened = parseBirthDate(childBirthDate);
    String[] parts = childBirthDate.split("\\.");
    String separator;

    if (Integer.parseInt(parts[2]) > 1999) {
      separator = "A";
    } else {
      separator = "-";
    }
    return shortened + separator + UID_postfix;
  }

  public static String parseBirthDate(String birthDateString) {
    String[] parts = birthDateString.split("\\.");
    String date = "";

    if (parts[0].length() == 1) {
      date = "0" + parts[0];
    } else {
      date = parts[0];
    }

    if (parts[1].length() == 1) {
      date = date + "0" + parts[1];
    } else {
      date = date + parts[1];
    }

    date = date + parts[2].substring(2, 4);
    return date;
  }
  
  public static String[] splitLines(String source) {
    if (source == null) {
      return new String[] { null, null };
    }

    String[] temp = source.split("\n");
    if (temp.length > 1) {
      for (int i = 0; i < temp.length; i++) {
        temp[i] = temp[i].trim();
      }
    }
    if (temp.length == 1) {
      temp = new String[] { temp[0], null };
    }
    if (temp.length == 0) {
      temp = new String[] { null, null };
    }
    return temp;
  }
  
  public static String getFirstName(String names) throws Exception {
    if (names == null) {
      throw new Exception("Names cannot be null");
    }

    // remove leading and trailing whitespace
    String temp = names.trim();

    if (temp.contains(" ")) {
      return temp.substring(0, temp.indexOf(" "));
    }
    return temp;
  }
  
  public static String getSecondName(String names) throws Exception {
    if (names == null) {
      throw new Exception("Names cannot be null");
    }
    // remove leading and trailing whitespace
    String temp = names.trim();

    if (temp.contains(" ")) {
      return temp.substring(temp.indexOf(" ")).trim();
    }
    return temp;
  }
  
  public static void addNotNull(Collection<String> col, String toAdd) {
    if (toAdd != null && toAdd.length() > 0 && !col.contains("'" + toAdd.trim() + "'")) {
      col.add("'" + toAdd.trim() + "'");
    }

//     if (toAdd != null && toAdd.length() > 0 && !col.contains(toAdd.trim())) {
//     col.add(toAdd.trim());
//     }
  }
  
  public static boolean isNotNullOrEmpty(String s) {
    if (s != null && !s.trim().isEmpty()) {
      return true;
    } else {
      return false;
    }
  }
}
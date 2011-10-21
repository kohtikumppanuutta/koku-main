package fi.koku.settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Provides access to system settings.
 * 
 * @author laukksa
 *
 */
public class KoKuPropertiesUtil {

  private static Properties properties = new Properties();
  
  private static final String PROPERTIES_FILE_NAME = "koku-settings.properties";
  
  private KoKuPropertiesUtil() {
  }
  
  static {
    InputStream is = null;
    try {
      final String serverConfDir = System.getProperty("jboss.server.home.dir") + "/conf";
      File file = new File(new File(serverConfDir), PROPERTIES_FILE_NAME);
      is = new FileInputStream(file);
      properties.load(is);
    } catch (IOException e) {
      throw new RuntimeException("Cannot read " + PROPERTIES_FILE_NAME, e);
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          // Ignore this
        }
      }
    }
  }
  
  /**
   * Lookup property with key.
   * 
   * @param key
   * @return value
   */
  public static String get(String key) {
    return properties.getProperty(key);
  }  
  
}

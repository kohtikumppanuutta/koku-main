/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku.auth;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Provides allowed roles for specific operation.
 * 
 * @author laukksa
 *
 */
public class KoKuRoleUtil {
  
  private static final String ROLE_SEPARATOR = ",";
  
  private static Map<String, Set<String>> roles = null;
  
  static {
    InputStream is = null;
    try {
      is = KoKuRoleUtil.class.getClassLoader().getResourceAsStream("koku-auth-roles.properties");
      if (is != null) {
        Properties properties = new Properties();
        properties.load(is);
        roles = parseProperties(properties);
      } else {
        throw new RuntimeException("Cannot read koku-auth-roles.properties.");
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
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
   * Build roles map.
   * 
   * @param properties
   * @return roles map
   */
  private static Map<String, Set<String>> parseProperties(Properties properties) {
    Map<String, Set<String>> roles = new HashMap<String, Set<String>>();
    for (Iterator<?> iter = properties.keySet().iterator(); iter.hasNext();) {
      final String key = (String) iter.next();
      final String roleStr = (String) properties.get(key);
      if (roleStr == null) {
        roles.put(key, new HashSet<String>());
      } else {
        roles.put(key, new HashSet<String>(Arrays.asList(roleStr.split(ROLE_SEPARATOR))));
      }
    }
    return roles;
  }
  
  /**
   * Lookup role(s) for operation.
   * 
   * @param operation
   * @return roles
   */
  public static Set<String> getAllowedRoles(String operation) {
    Set<String> r = roles.get(operation);
    if (r == null) {
      throw new RuntimeException("No roles found for operation: " + operation);
    }
    return r;
  }
  
}

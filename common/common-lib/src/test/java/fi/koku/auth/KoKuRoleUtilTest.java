/*
 * Copyright 2012 Ixonos Plc, Finland. All rights reserved.
 * 
 * This file is part of Kohti kumppanuutta.
 *
 * This file is licensed under GNU LGPL version 3.
 * Please see the 'license.txt' file in the root directory of the package you received.
 * If you did not receive a license, please contact the copyright holder
 * (http://www.ixonos.com/).
 *
 */
package fi.koku.auth;

import org.junit.Test;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.hamcrest.CoreMatchers.*;

/**
 * Tests for KoKuRoleUtil.
 * 
 * @author laukksa
 *
 */
public class KoKuRoleUtilTest {
  
  @Test
  public void testGetAllowedRoles() {
    assertThat(KoKuRoleUtil.getAllowedRoles("DeleteCommunity").size(), is(1));
    assertTrue(KoKuRoleUtil.getAllowedRoles("DeleteCommunity").contains("ROLE_C"));
    
    assertThat(KoKuRoleUtil.getAllowedRoles("ViewLogFile").size(), is(2));
    assertTrue(KoKuRoleUtil.getAllowedRoles("ViewLogFile").contains("ROLE_LOG_VIEWER"));
    assertTrue(KoKuRoleUtil.getAllowedRoles("ViewLogFile").contains("ROLE_LOG_ADMIN"));
        
    assertThat(KoKuRoleUtil.getAllowedRoles("UpdateCommunity").size(), is(3));
    assertTrue(KoKuRoleUtil.getAllowedRoles("UpdateCommunity").contains("ROLE_A"));
    assertTrue(KoKuRoleUtil.getAllowedRoles("UpdateCommunity").contains("ROLE_B"));
    assertTrue(KoKuRoleUtil.getAllowedRoles("UpdateCommunity").contains("ROLE_C"));
    
    try {
      KoKuRoleUtil.getAllowedRoles("UpdateUser");
      fail("No exception thrown.");
    } catch (RuntimeException e) {
      // Expected
    }
  }

}

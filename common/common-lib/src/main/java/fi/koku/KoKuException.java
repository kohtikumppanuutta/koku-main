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
package fi.koku;

/**
 * Base interface for koku exceptions.
 * 
 * @author laukksa
 *
 */
public interface KoKuException {

  public static final int COMMON_ERROR_CODE = 1000;
  
  public static final String COMMON_ERROR_MESSAGE = "Error occured.";
  
  public static final int NOT_AUTHORIZED_ERROR_CODE = 5000;
  
  public static final String NOT_AUTHORIZED_ERROR_MESSAGE = "Operation not authorized.";  
  
  int getErrorCode();
  
}

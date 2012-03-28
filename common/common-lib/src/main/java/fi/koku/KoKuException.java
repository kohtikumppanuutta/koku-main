/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
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

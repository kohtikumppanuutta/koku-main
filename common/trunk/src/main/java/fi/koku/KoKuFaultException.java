/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
package fi.koku;

/**
 * Fault exception base class.
 * 
 * @author aspluma
 * @author laukksa
 */
public class KoKuFaultException extends RuntimeException implements KoKuException {
  private static final long serialVersionUID = 1L;

  private int errorCode;
  
  public KoKuFaultException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public KoKuFaultException(int errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }

}

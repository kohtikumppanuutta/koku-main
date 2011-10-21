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

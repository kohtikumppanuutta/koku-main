package fi.koku;

/**
 * Contingency exception base class.
 * 
 * @author aspluma
 */
public class KoKuContingencyException extends Exception implements KoKuException {
  private static final long serialVersionUID = 1L;

  private int errorCode;
  
  public KoKuContingencyException(int errorCode, String message, Throwable cause) {
    super(message, cause);
    this.errorCode = errorCode;
  }

  public KoKuContingencyException(int errorCode, String message) {
    super(message);
    this.errorCode = errorCode;
  }

  public int getErrorCode() {
    return errorCode;
  }
}

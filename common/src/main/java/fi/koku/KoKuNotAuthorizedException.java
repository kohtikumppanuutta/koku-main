package fi.koku;

/**
 * Indicates that user is not authorized to execute the operation.
 * 
 * @author laukksa
 *
 */
public class KoKuNotAuthorizedException extends KoKuFaultException {

  private static final long serialVersionUID = -4520100781840320139L;

  public KoKuNotAuthorizedException(int errorCode, String message) {
    super(errorCode, message);
  }

}

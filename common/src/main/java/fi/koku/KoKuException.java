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
  
  int getErrorCode();
  
}

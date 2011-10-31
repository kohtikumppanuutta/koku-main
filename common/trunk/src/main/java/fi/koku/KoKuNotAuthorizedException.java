/*
 * Copyright 2011 Ixonos Plc, Finland. All rights reserved.
 * 
 * You should have received a copy of the license text along with this program.
 * If not, please contact the copyright holder (http://www.ixonos.com/).
 * 
 */
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

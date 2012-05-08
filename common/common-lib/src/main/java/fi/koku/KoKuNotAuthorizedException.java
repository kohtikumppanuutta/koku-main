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
